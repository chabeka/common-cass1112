package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.ClockResolution;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;

/**
 * Générateur d'id utilisant cassandra et zookeeper La valeur du dernier id
 * utilisé est stockée dans cassandra On utilise zookeeper pour accéder de
 * manière exclusive à la séquence
 * 
 */
public class CassandraIdGenerator implements IdGenerator {

   private static final Logger LOG = LoggerFactory
         .getLogger(CassandraIdGenerator.class);
   private static final String SEQUENCE_CF = "Sequences";
   private static final String SEQUENCE_KEY = "sequences";
   private final String sequenceName;
   private final ColumnFamilyTemplate<String, String> template;
   private final CuratorFramework curatorClient;
   private final Keyspace keyspace;
   private static final int DEFAULT_TIME_OUT = 20;
   private static final long ONE_THOUSAND = 1000L;
   private ClockResolution clockResolution;

   /**
    * Temps maximum de décalage d'horloge qu'il nous parait acceptable, en micro-secondes 
    */
   private static final int MAX_TIME_SYNCHRO_ERROR = 10 * 1000 * 1000;
   /**
    * Temps maximum de décalage d'horloge, en micro-secondes. Au delà, on logue une warning. 
    */
   private static final int MAX_TIME_SYNCHRO_WARN = 2 * 1000 * 1000;
   private int lockTimeOut = DEFAULT_TIME_OUT;

   /**
    * Constructeur
    * @param keyspace      Keyspace cassandra
    * @param curatorClient Connexion à zookeeper
    * @param sequenceName  Nom de la séquence (doit identifier la séquence de manière unique)
    */
   public CassandraIdGenerator(Keyspace keyspace,
         CuratorFramework curatorClient, String sequenceName) {
      this.keyspace = keyspace;      
      this.sequenceName = sequenceName;
      this.curatorClient = curatorClient;
      template = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            SEQUENCE_CF, StringSerializer.get(), StringSerializer.get());
   }

   /**
    * Indique le fournisseur d'heure à utiliser.
    * Utile à des fin de test uniquement.
    * @param clockResolution  : le fournisseur d'heure à utiliser
    * @return 
    */
   public final void setClockResolution(ClockResolution clockResolution) {
      this.clockResolution = clockResolution;
   }
   
   /**
    * @return renvoie le timestamp courant
    */
   private long getCurrentClock() {
      if (clockResolution != null) return clockResolution.createClock();
      return keyspace.createClock();
   }
   
   @Override
   public final long getNextId() {

      ZookeeperMutex mutex = new ZookeeperMutex(curatorClient, "/sequences/"
            + sequenceName);
      try {
         if (!mutex.acquire(lockTimeOut, TimeUnit.SECONDS)) {
            throw new IdGeneratorException(
                  "Erreur lors de la tentative d'acquisition du lock pour la séquence "
                        + sequenceName
                        + " : on n'a pas obtenu le lock au bout de 20 secondes.");
         }
         // On a le lock.
         // On lit la valeur courante de la séquence
         HColumn<String, Long> currentSequence = readCurrentSequence();

         // On écrit dans cassandra la valeur incrémentée
         long newValue = incrementSequenceValue(currentSequence);

         // On vérifie qu'on a toujours le lock. Si oui, on peut utiliser la
         // séquence.
         if (mutex.isObjectStillLocked(lockTimeOut, TimeUnit.SECONDS)) {
            // On peut utiliser la valeur incrémentée
            return newValue;
         } else {
            throw new IdGeneratorException(
                  "Erreur lors de la tentative d'acquisition du lock pour la séquence "
                        + sequenceName + ". Problème de connexion zookeeper ?");
         }

      } finally {
         mutex.release();
      }
   }

   /**
    * Lit la valeur de la séquence dans cassandra
    * @return  colonne lue
    */
   private HColumn<String, Long> readCurrentSequence() {
      return template.querySingleColumn(SEQUENCE_KEY,
            sequenceName, LongSerializer.get());
   }

   /**
    * Incrémente la séquence, et la fait persister dans cassandra 
    * @param currentSequence   colonne cassandra contenant la valeur courante de la séquence
    * @return nouvelle valeur de la séquence
    */
   private long incrementSequenceValue(HColumn<String, Long> currentSequence) {
      long currentValue;
      long currentClock;
      if (currentSequence == null) {
         currentValue = 0;
         currentClock = 0;
      }
      else {
         currentValue = currentSequence.getValue();
         currentClock = currentSequence.getClock();
      }
      // On s'assure que le nouveau timestamp est supérieur à l'ancien
      long newClock = getCurrentClock();      
      if (newClock <= currentClock) {
         // On s'assure que le décalage n'est pas trop important
         // Les clocks sont exprimés en micro-secondes
         if ((currentClock - newClock) > MAX_TIME_SYNCHRO_ERROR) {
            throw new IdGeneratorException(
                  "Erreur lors de la tentative d'acquisition du lock pour la séquence "
                  + sequenceName + ". Vérifier la sychronisation des horloges des serveurs. "
                  + " Ancien timestamp :" + currentClock + " - Nouveau timestamp : " + newClock);
         }
         if ((currentClock - newClock) > MAX_TIME_SYNCHRO_WARN) {
            LOG.warn("Attention, les horloges des serveurs semblent désynchronisées. Le décalage est au moins de " 
                  + (currentClock - newClock) / ONE_THOUSAND + " ms");
         }
         // Sinon, on positionne le nouveau timestamp juste au dessus de l'ancien
         newClock = currentClock + 1;
      }
      ColumnFamilyUpdater<String, String> updater = template
            .createUpdater(SEQUENCE_KEY);
      long newValue = currentValue + 1;
      updater.setLong(sequenceName, newValue);
      updater.setClock(newClock);
      template.update(updater);
      return newValue;
   }

   /**
    * Spécifie le timeout du lock, en seconde (par défaut : 20s)
    * @param lockTimeOut timeout, en seconde
    */
   public final void setLockTimeOut(int lockTimeOut) {
      this.lockTimeOut = lockTimeOut;
   }

   /**
    * Récupère la valeur du timeout pour le lock
    * @return  timeout, en seconde
    */
   public final int getLockTimeOut() {
      return lockTimeOut;
   }

}
