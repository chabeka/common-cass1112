package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.util.concurrent.TimeUnit;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;

/**
 * Générateur d'id utilisant cassandra et zookeeper La valeur du dernier id
 * utilisé est stockée dans cassandra On utilise zookeeper pour accéder de
 * manière exclusive à la séquence
 * 
 */
public class CassandraIdGenerator implements IdGenerator {

   private static final String SEQUENCE_CF = "Sequences";
   private static final String SEQUENCE_KEY = "sequences";
   private final String sequenceName;
   private final ColumnFamilyTemplate<String, String> template;
   private final CuratorFramework curatorClient;
   private static final int DEFAULT_TIME_OUT = 20;

   private final JobClockSupport jobClockSupport;

   private int lockTimeOut = DEFAULT_TIME_OUT;

   /**
    * Constructeur
    * @param keyspace      Keyspace cassandra
    * @param curatorClient Connexion à zookeeper
    * @param sequenceName  Nom de la séquence (doit identifier la séquence de manière unique)
    * @param jobClockSupport support de l'horloge
    */
   public CassandraIdGenerator(Keyspace keyspace,
         CuratorFramework curatorClient, String sequenceName,JobClockSupport jobClockSupport) {
      this.sequenceName = sequenceName;
      this.curatorClient = curatorClient;
      this.jobClockSupport = jobClockSupport;
      template = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            SEQUENCE_CF, StringSerializer.get(), StringSerializer.get());
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
      
      long newClock;  
      
      if (currentSequence == null) {
         currentValue = 0;
         newClock = jobClockSupport.currentCLock();  
      }
      else {
         currentValue = currentSequence.getValue();
         newClock = jobClockSupport.currentCLock(currentSequence);  
      }
      
      // On s'assure que le nouveau timestamp est supérieur à l'ancien
         
      
      ColumnFamilyUpdater<String, String> updater = template
            .createUpdater(SEQUENCE_KEY);
      long newValue = currentValue + 1;
      updater.setClock(newClock);
      updater.setLong(sequenceName, newValue);
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
