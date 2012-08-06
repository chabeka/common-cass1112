package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docubase.dfce.commons.jobs.JobUtils;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import fr.urssaf.image.sae.lotinstallmaj.modele.DfceConfig;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;

/**
 * Opérations de mise à jour du SAE.
 * 
 * 
 */
@Service
public final class MajLotServiceImpl implements MajLotService {

   public static final String CODE_ACTIVITE = "CODEACTIVITENONOBLIGATOIRE";
   public static final String DUREE_CONSERVATION = "DUREECONSERVATIONDEMANDEDELAICOTISANT";
   public static final String CASSANDRA_120510 = "CASSANDRA_120510";
   public static final String CASSANDRA_120512 = "CASSANDRA_120512";
   public static final String CASSANDRA_120513 = "CASSANDRA_120513";
   public static final String DFCE_110_INDEX_DATES = "DFCE_110_INDEX_DATES";
   public static final String DFCE_110_CASSANDRA = "DFCE_110_CASSANDRA";

   public static final int DUREE_1825 = 1825;
   public static final int DUREE_1643 = 1643;

   private final ServiceProvider serviceProvider = ServiceProvider
         .newServiceProvider();

   // LOGGER
   private static final Logger LOG = LoggerFactory
         .getLogger(MajLotServiceImpl.class);

   @Autowired
   private DfceConfig dfceConfig;

   @Autowired
   private CassandraConfig cassandraConfig;

   /**
    * {@inheritDoc}
    */
   @Override
   public void demarre(String nomOperation, String[] argSpecifiques) {

      // Selon l'opération à lancer
      if (CODE_ACTIVITE.equalsIgnoreCase(nomOperation)) {

         updateCodeActivite();

      } else if (DUREE_CONSERVATION.equalsIgnoreCase(nomOperation)) {

         updateDureeConservation();

      } else if (CASSANDRA_120510.equalsIgnoreCase(nomOperation)) {

         updateCassandra120510();

      } else if (CASSANDRA_120512.equalsIgnoreCase(nomOperation)) {

         updateCassandra120512();

      } else if (CASSANDRA_120513.equalsIgnoreCase(nomOperation)) {

         updateCassandra120513();

      } else if (DFCE_110_INDEX_DATES.equalsIgnoreCase(nomOperation)) {

         updateIndexationDFCE110();

      } else if (DFCE_110_CASSANDRA.equalsIgnoreCase(nomOperation)) {

         this.updateDFCE110CASSANDRA();

      } else {

         // Opération inconnue => log + exception runtime
         String message = String.format(
               "Erreur technique : L'opération %s est inconnue", nomOperation);
         LOG.error(message);
         throw new MajLotRuntimeException(message);

      }

   }

   /**
    * Connexion à DFCE
    */
   private void connectDfce() {

      serviceProvider.connect(dfceConfig.getLogin(), dfceConfig.getPassword(),
            dfceConfig.getUrlToolkit());

   }

   /**
    * Mise à jour de la base métier pour la métadonnée CodeActivite à rendre non
    * obligatoire.
    */
   private void updateCodeActivite() {

      // Log
      LOG
            .info("Début de l'opération : Modification de la structure de la base DFCE pour rendre la métadonnée CodeActivite non obligatoire");

      // Connection à DFCE
      connectDfce();

      // recupération de la metadonnee CodeActivite et verification qu'elle est
      // bien dans l'état
      // attendu, à savoir qu'elle est obligatoire.
      BaseAdministrationService baseService = serviceProvider
            .getBaseAdministrationService();
      Base base = baseService.getBase(dfceConfig.getBasename());
      BaseCategory baseCategory = base.getBaseCategory("act");
      int minValues = baseCategory.getMinimumValues();

      // Vérifie que la mise à jour est à faire
      if (minValues == 1) {

         // Il faut mettre à jour la structure de la base
         baseCategory.setMinimumValues(0);
         baseService.updateBase(base);

         // Log
         LOG
               .info("Mise à jour effectuée avec succès : le CodeActivite n'est plus obligatoire");

      } else {

         // Le code activité n'est pas obligatoire (maj déjà effectuée, ou
         // nouvelle base)
         LOG
               .info("Rien à faire : la métadonnée CodeActivite est déjà en non obligatoire ");

      }

   }

   /**
    * mise à jour de la durée de conservation du type de document 3.1.3.1.1
    */
   private void updateDureeConservation() {

      // Log
      LOG
            .info("Début de l'opération : Modification de la durée de conservation du type de document 3.1.3.1.1 (1643 -> 1825)");

      // Connection à DFCE
      connectDfce();

      // Récupération de la durée de conservation existante
      StorageAdministrationService storageAdmin = serviceProvider
            .getStorageAdministrationService();
      LifeCycleRule lifeCycleRule = storageAdmin.getLifeCycleRule("3.1.3.1.1");
      int dureeConservation = lifeCycleRule.getLifeCycleLength();

      // Vérifie que la mise à jour est à faire
      if (dureeConservation == DUREE_1825) {

         // La durée de conservation est déjà bonne
         LOG
               .info("Rien à faire : la durée de conservation de 3.1.3.1.1 est déjà bonne (1825)");

      } else {

         // TODO : en attente du traitement du JIRA CRTL-81
         throw new MajLotRuntimeException(
               "Opération non réalisable : en attente du traitement du JIRA CRTL-81");

      }

   }

   /**
    * Pour lot 120510 du SAE : création du keyspace "SAE" dans cassandra, en
    * version 1
    */
   private void updateCassandra120510() {
      LOG.info("Début de l'opération : création du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      SAECassandraUpdater updater = new SAECassandraUpdater(cassandraConfig);
      updater.updateToVersion1();
      LOG.info("Fin de l'opération : création du keyspace SAE");
   }

   /**
    * Pour lot 120512 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 2
    */
   private void updateCassandra120512() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      SAECassandraUpdater updater = new SAECassandraUpdater(cassandraConfig);
      updater.updateToVersion2();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 120513 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 3
    */
   private void updateCassandra120513() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      SAECassandraUpdater updater = new SAECassandraUpdater(cassandraConfig);
      updater.updateToVersion3();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Indexation pour les métadonnées système qui n'étaient pas indexées en
    * 1.0.1
    */
   private void updateIndexationDFCE110() {

      // Log
      LOG.info("début d'indexation des métadonnées systèmes ");

      // Connection à DFCE
      connectDfce();

      String parameters = "category.names=SM_CREATION_DATE|SM_ARCHIVAGE_DATE|"
            + "SM_MODIFICATION_DATE, timestamp=" + System.currentTimeMillis();
      try {
         serviceProvider.getJobAdministrationService().start(
               JobUtils.INDEX_CATEGORIES_JOB, parameters);
         
         LOG.info("fin d'indexation des métadonnées systèmes ");
         
      } catch (NoSuchJobException e) {
         LOG.error("échec indexation", e);
      } catch (JobInstanceAlreadyExistsException e) {
         LOG.error("échec indexation", e);
      } catch (JobParametersInvalidException e) {
         LOG.error("échec indexation", e);
      }

   }

   private void updateDFCE110CASSANDRA() {

      LOG
            .info("Début de l'opération : création des nouvelles CF pour la version 1.1.0 de DFCE");
      // Récupération de la chaîne de connexion au cluster cassandra
      // SAECassandraUpdater updater = new SAECassandraUpdater(cassandraConfig);
      // updater.updateToVersion4();
      throw new NotImplementedException(
            "création des nouvelles CF pour la version 1.1.0 de DFCE n'est pas implémentée");

      // LOG.info("Fin de l'opération : création des nouvelles CF pour la version 1.1.0 de DFC");
   }

}
