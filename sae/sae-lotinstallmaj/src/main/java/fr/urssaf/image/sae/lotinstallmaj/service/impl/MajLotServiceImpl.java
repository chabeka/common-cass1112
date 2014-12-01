package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import fr.urssaf.image.sae.lotinstallmaj.modele.DataBaseModel;
import fr.urssaf.image.sae.lotinstallmaj.modele.SaeCategory;
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
   public static final String CASSANDRA_121110 = "CASSANDRA_121110";
   public static final String CASSANDRA_140700 = "CASSANDRA_140700";
   public static final String CASSANDRA_150100 = "CASSANDRA_150100";
   public static final String META_SEPA = "META_SEPA";
   public static final String META_130400 = "META_130400";
   public static final String META_150100 = "META_150100";
   public static final String CASSANDRA_130400 = "CASSANDRA_130400";
   public static final String CASSANDRA_130700 = "CASSANDRA_130700";
   public static final String CASSANDRA_131100 = "CASSANDRA_131100";
   public static final String DFCE_130700 = "DFCE_130700";
   public static final String CASSANDRA_DROITS_GED = "CASSANDRA_DROITS_GED";
   public static final String CREATION_GED = "CREATION_GED";

   public static final int DUREE_1825 = 1825;
   public static final int DUREE_1643 = 1643;

   private final ServiceProvider serviceProvider = ServiceProvider
         .newServiceProvider();

   // LOGGER
   private static final Logger LOG = LoggerFactory
         .getLogger(MajLotServiceImpl.class);

   @Autowired
   private DFCEConnection dfceConfig;

   @Autowired
   private ApplicationContext context;

   @Autowired
   private SAECassandraUpdater updater;

   @Autowired
   private GedCassandraUpdater gedUpdater;

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

      } else if (CASSANDRA_121110.equalsIgnoreCase(nomOperation)) {

         updateCassandra120910();

      } else if (META_SEPA.equalsIgnoreCase(nomOperation)) {

         updateMetaSepa();

      } else if (META_130400.equalsIgnoreCase(nomOperation)) {
         // Pour lot 130400 du SAE : Ajout de la métadonnée
         // ReferenceDocumentaire
         updateMeta("meta130400.xml", "META_130400");

      } else if (CASSANDRA_130400.equalsIgnoreCase(nomOperation)) {

         updateCassandra130400();

      } else if (DFCE_130700.equalsIgnoreCase(nomOperation)) {

         updateDFCE130700();

      } else if (CASSANDRA_130700.equalsIgnoreCase(nomOperation)) {

         updateCassandra130700();

      } else if (CASSANDRA_131100.equalsIgnoreCase(nomOperation)) {

         updateCassandra131100();

      } else if (CASSANDRA_140700.equalsIgnoreCase(nomOperation)) {

         updateCassandra140700();

      } else if (CASSANDRA_150100.equalsIgnoreCase(nomOperation)) {

         updateCassandra150100();

      } else if (META_150100.equalsIgnoreCase(nomOperation)) {
         // Pour lot 150100 du SAE : Ajout de la métadonnée
         // CodePartenaire et DateArchivageGNT
         updateMeta("meta150100.xml", "META_150100");

      } else if (CASSANDRA_DROITS_GED.equalsIgnoreCase(nomOperation)) {

         updateCassandraDroitsGed();

      } else if (CREATION_GED.equalsIgnoreCase(nomOperation)) {

         createGedBase();

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
            dfceConfig.getUrlToolkit(), dfceConfig.getTimeout());

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
      Base base = baseService.getBase(dfceConfig.getBaseName());
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
      updater.updateToVersion2();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 120910 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 3
    */
   private void updateCassandra120910() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion3();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 13xx10 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 4
    */
   private void updateCassandra130400() {
      LOG
            .info("Début de l'opération : Lot 130400 - Mise à jour du keyspace SAE");
      updater.updateToVersion4();
      LOG.info("Fin de l'opération : Lot 130400 - Mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 130700 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 5
    */
   private void updateCassandra130700() {

      LOG
            .info("Début de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
      updater.updateToVersion5();
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 131100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 6
    */
   private void updateCassandra131100() {

      LOG
            .info("Début de l'opération : Lot 131100 - Mise à jour du keyspace SAE");
      updater.updateToVersion6();
      LOG.info("Fin de l'opération : Lot 131100 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 140700 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 7
    */
   private void updateCassandra140700() {
      LOG
            .info("Début de l'opération : mise à jour du keyspace SAE pour le lot 140700 - Référentiel des formats");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion7();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }
   
   /**
    * Pour lot 150100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 8
    */
   private void updateCassandra150100() {
      LOG
      .info("Début de l'opération : mise à jour du keyspace SAE pour le lot 141200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion8();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Ajout des droits GED
    */
   private void updateCassandraDroitsGed() {

      LOG
            .info("Début de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
      gedUpdater.updateAuthorizationAccess();
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 120912 du SAE : mise à jour du modèle de données des documents.
    */
   private void updateMetaSepa() {
      LOG.info("Début de l'opération : ajout des métadonnées au document");

      LOG.info("- début de récupération des catégories à ajouter");
      XStream xStream = new XStream();
      xStream.processAnnotations(DataBaseModel.class);
      Reader reader = null;
      InputStream stream = null;

      try {
         stream = context.getResource("metaSepa.xml").getInputStream();
         reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
         DataBaseModel model = DataBaseModel.class
               .cast(xStream.fromXML(reader));

         LOG.info("- fin de récupération des catégories à ajouter");

         // connexion a DFCE
         connectDfce();

         Base base = serviceProvider.getBaseAdministrationService().getBase(
               dfceConfig.getBaseName());

         final List<BaseCategory> baseCategories = new ArrayList<BaseCategory>();
         final ToolkitFactory toolkit = ToolkitFactory.getInstance();
         for (SaeCategory category : model.getDataBase().getSaeCategories()
               .getCategories()) {
            final Category categoryDfce = serviceProvider
                  .getStorageAdministrationService().findOrCreateCategory(
                        category.getName(), category.categoryDataType());
            final BaseCategory baseCategory = toolkit.createBaseCategory(
                  categoryDfce, category.isIndex());
            baseCategory.setEnableDictionary(category.isEnableDictionary());
            baseCategory.setMaximumValues(category.getMaximumValues());
            baseCategory.setMinimumValues(category.getMinimumValues());
            baseCategory.setSingle(category.isSingle());
            baseCategories.add(baseCategory);
         }

         LOG.info("- début d'insertion des catégories");
         for (BaseCategory baseCategory : baseCategories) {
            base.addBaseCategory(baseCategory);
         }

         serviceProvider.getBaseAdministrationService().updateBase(base);

         LOG.info("- fin d'insertion des catégories");

      } catch (IOException e) {
         LOG.warn("impossible de récupérer le fichier contenant les données");
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOG.debug("impossible de fermer le reader");
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOG.debug("impossible de fermer le flux de données");
            }

         }

         serviceProvider.disconnect();
      }

      LOG.info("Fin de l'opération : ajout des métadonnées au document");
   }

   /**
    * Ajout de métadonnées dans DFCE à partir d'un fichier xml contenant les
    * métadonnées ex : meta130400.xml (dans /src/main/resources/)
    * 
    * @param fichierlisteMeta
    *           le fichier contenant les métadonnées
    * @param nomOperation
    *           Nom de la commande pour affichage dans les traces
    */
   private void updateMeta(String fichierlisteMeta, String nomOperation) {

      LOG.info(
            "Début de l'opération : Création des nouvelles métadonnées ({})",
            nomOperation);

      LOG
            .debug("Lecture du fichier XML contenant les métadonnées à ajouter - Début");
      XStream xStream = new XStream();
      xStream.processAnnotations(DataBaseModel.class);
      Reader reader = null;
      InputStream stream = null;

      try {
         stream = context.getResource(fichierlisteMeta).getInputStream();
         reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
         DataBaseModel model = DataBaseModel.class
               .cast(xStream.fromXML(reader));

         LOG
               .debug("Lecture du fichier XML contenant les métadonnées à ajouter - Fin");

         // connexion a DFCE
         connectDfce();

         Base base = serviceProvider.getBaseAdministrationService().getBase(
               dfceConfig.getBaseName());

         final List<BaseCategory> baseCategories = new ArrayList<BaseCategory>();
         final ToolkitFactory toolkit = ToolkitFactory.getInstance();

         LOG.debug("Création des métadonnées dans DFCE - Début");

         for (SaeCategory category : model.getDataBase().getSaeCategories()
               .getCategories()) {

            // Test de l'existence de la métadonnée dans DFCE
            if (serviceProvider.getStorageAdministrationService().getCategory(
                  category.getName()) == null) {
               final Category categoryDfce = serviceProvider
                     .getStorageAdministrationService().findOrCreateCategory(
                           category.getName(), category.categoryDataType());
               final BaseCategory baseCategory = toolkit.createBaseCategory(
                     categoryDfce, category.isIndex());
               baseCategory.setEnableDictionary(category.isEnableDictionary());
               baseCategory.setMaximumValues(category.getMaximumValues());
               baseCategory.setMinimumValues(category.getMinimumValues());
               baseCategory.setSingle(category.isSingle());
               baseCategories.add(baseCategory);
               LOG.info("La métadonnée {} va être ajoutée.", category
                     .getDescriptif());
            } else {
               LOG.info("La métadonnée {} existe déjà.", category
                     .getDescriptif());
            }
         }

         for (BaseCategory baseCategory : baseCategories) {
            base.addBaseCategory(baseCategory);
         }

         serviceProvider.getBaseAdministrationService().updateBase(base);

         LOG.debug("Création des métadonnées dans DFCE - Fin");

      } catch (IOException e) {
         LOG.warn("impossible de récupérer le fichier contenant les données");
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOG.debug("impossible de fermer le reader");
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOG.debug("impossible de fermer le flux de données");
            }

         }

         serviceProvider.disconnect();
      }

      LOG
            .info("Fin de l'opération : Création des nouvelles métadonnées ({})", nomOperation);
   }

   /**
    * Pour lot 130700 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.2.x de DFCE
    */
   private void updateDFCE130700() {

      LOG
            .info("Début de l'opération : Lot 130700 - Mise à jour du schéma DFCE");
      DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(
            cassandraConfig);
      dfceUpdater.updateToVersion110();
      dfceUpdater.updateToVersion120();
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du schéma DFCE");

   }

   /**
    * Création de la base GED
    */
   private void createGedBase() {
      updater.updateToVersion1();
      updater.updateToVersion2();
      updater.updateToVersion3();
      updater.updateToVersion4();
      updater.updateToVersion5();
      updater.updateToVersion6();
      updater.updateToVersion7();
      updater.updateToVersion8();
   }

}
