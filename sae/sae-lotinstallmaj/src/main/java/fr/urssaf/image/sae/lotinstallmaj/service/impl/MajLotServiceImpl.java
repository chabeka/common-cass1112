package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.apache.commons.lang.StringUtils;
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
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

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
   public static final String CASSANDRA_DFCE_150400 = "CASSANDRA_DFCE_150400";
   public static final String CASSANDRA_DFCE_150600 = "CASSANDRA_DFCE_150600";
   public static final String CASSANDRA_DFCE_150601 = "CASSANDRA_DFCE_150601";
   public static final String CASSANDRA_DFCE_151200 = "CASSANDRA_DFCE_151200";
   public static final String CASSANDRA_DFCE_151201 = "CASSANDRA_DFCE_151201";
   public static final String CASSANDRA_DFCE_160300 = "CASSANDRA_DFCE_160300";
   public static final String CASSANDRA_DFCE_160400 = "CASSANDRA_DFCE_160400";
   public static final String GNS_CASSANDRA_DFCE_160600 = "GNS_CASSANDRA_DFCE_160600";
   public static final String GNT_CASSANDRA_DFCE_160600 = "GNT_CASSANDRA_DFCE_160600";
   public static final String GNS_CASSANDRA_DFCE_160601 = "GNS_CASSANDRA_DFCE_160601";
   public static final String GNT_CASSANDRA_DFCE_160601 = "GNT_CASSANDRA_DFCE_160601";
   public static final String CASSANDRA_DFCE_160900 = "CASSANDRA_DFCE_160900";
   public static final String CASSANDRA_DFCE_160901 = "CASSANDRA_DFCE_160901";
   public static final String CASSANDRA_DFCE_161100 = "CASSANDRA_DFCE_161100";
   public static final String GNS_CASSANDRA_DFCE_170200 = "GNS_CASSANDRA_DFCE_170200";
   public static final String GNT_CASSANDRA_DFCE_170200 = "GNT_CASSANDRA_DFCE_170200";
   public static final String CASSANDRA_170201 = "CASSANDRA_170201";
   public static final String GNS_CASSANDRA_DFCE_170202 = "GNS_CASSANDRA_DFCE_170202";
   public static final String GNT_CASSANDRA_DFCE_170202 = "GNT_CASSANDRA_DFCE_170202";
   public static final String CASSANDRA_DFCE_170900 = "CASSANDRA_DFCE_170900";
   public static final String CASSANDRA_DFCE_170901 = "CASSANDRA_DFCE_170901";
   public static final String CASSANDRA_DFCE_180300 = "CASSANDRA_DFCE_180300";

   public static final String META_SEPA = "META_SEPA";
   public static final String META_130400 = "META_130400";
   public static final String META_150100 = "META_150100";
   public static final String CASSANDRA_130400 = "CASSANDRA_130400";
   public static final String CASSANDRA_130700 = "CASSANDRA_130700";
   public static final String CASSANDRA_131100 = "CASSANDRA_131100";
   public static final String DFCE_130700 = "DFCE_130700";
   public static final String DFCE_150400 = "DFCE_150400";
   public static final String DFCE_150400_P5 = "DFCE_150400_P5";
   public static final String DFCE_151000 = "DFCE_151000";
   public static final String CASSANDRA_151000 = "CASSANDRA_151000";
   public static final String CASSANDRA_DFCE_151001 = "CASSANDRA_DFCE_151001";
   public static final String CASSANDRA_DROITS_GED = "CASSANDRA_DROITS_GED";
   public static final String GNS_DISABLE_COMPOSITE_INDEX = "GNS_DISABLE_COMPOSITE_INDEX";
   public static final String GNT_DISABLE_COMPOSITE_INDEX = "GNT_DISABLE_COMPOSITE_INDEX";

   public static final int DUREE_1825 = 1825;
   public static final int DUREE_1643 = 1643;

   /**
    * Enum qui décrit les différentes GED qui sont concernées par les opérations
    * demandées.
    */
   public static enum APPL_CONCERNEE {
      GNS("GNS"), GNT("GNT"), DFCE("DFCE");

      /**
       * Nom de la GED.
       */
      private String applName;

      /**
       * Constructeur.
       *
       * @param gedName
       *           Nom de la GED.
       */
      private APPL_CONCERNEE(final String gedName) {
         this.applName = gedName;
      }

      /**
       * Getter pour gedName
       * 
       * @return the gedName
       */
      public String getApplName() {
         return applName;
      }

   }

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

      } else if (CASSANDRA_DFCE_150400.equalsIgnoreCase(nomOperation)) {
         // -- Mise à jour cassandra
         updateCassandra150400();
         // -- Mise à jour DFCE
         updateMetaDfce("META_150400");

      } else if (DFCE_150400.equalsIgnoreCase(nomOperation)) {

         updateDFCE150400();

      } else if (DFCE_150400_P5.equalsIgnoreCase(nomOperation)) {

         updateDFCE150400_P5();

      } else if (GNS_DISABLE_COMPOSITE_INDEX.equalsIgnoreCase(nomOperation)) {

         disableCompositeIndex(APPL_CONCERNEE.GNS);

      } else if (GNT_DISABLE_COMPOSITE_INDEX.equalsIgnoreCase(nomOperation)) {

         disableCompositeIndex(APPL_CONCERNEE.GNT);

      } else if (CASSANDRA_DFCE_150600.equalsIgnoreCase(nomOperation)) {
         // -- Mise à jour cassandra
         updateCassandra150600();
         // -- Mise à jour DFCE
         updateMetaDfce("META_150600");

      } else if (CASSANDRA_DFCE_150601.equalsIgnoreCase(nomOperation)) {
         // -- Mise à jour cassandra
         updateCassandra150601();
         // -- Creation des index composite dans DFCE
         // addIndexesCompositeToDfce("DFCE_150601");
         // 160600 :Mise en commentaire car si on créé une base du départ, les
         // index
         // composite seront créés à partir de la nouvelle méthode différenciant
         // GNT et GNS

      } else if (DFCE_151000.equalsIgnoreCase(nomOperation)) {
         updateDFCE151000();

      } else if (CASSANDRA_151000.equalsIgnoreCase(nomOperation)) {
         updateCassandra151000();
         updateMetaDfce("META_151000");

      } else if (CASSANDRA_DFCE_151001.equalsIgnoreCase(nomOperation)) {
         updateCassandra151001();
         updateMetaDfce("META_151001");

      } else if (CASSANDRA_DROITS_GED.equalsIgnoreCase(nomOperation)) {
         updateCassandraDroitsGed();

      } else if (CASSANDRA_DFCE_151200.equalsIgnoreCase(nomOperation)) {
         updateCassandra151200();
         updateMetaDfce("META_151200");
      } else if (CASSANDRA_DFCE_151201.equalsIgnoreCase(nomOperation)) {
         updateCassandra151201();
         // Pas de modif côté DFCE donc pas d'appel updateMetaDfce
      } else if (CASSANDRA_DFCE_160300.equalsIgnoreCase(nomOperation)) {
         updateCassandra160300();
         // addIndexesCompositeToDfce("META_160300");
         // 160600 : Mise en commentaire car si on créé une base du départ, les
         // index
         // composite seront créés à partir de la nouvelle méthode différenciant
         // GNT et GNS

      } else if (CASSANDRA_DFCE_160400.equalsIgnoreCase(nomOperation)) {
         updateCassandra160400();
         updateMetaDfce("META_160400");
         // Indexation du numeroIdArchivage
         updateDFCE160400();
      } else if (GNS_CASSANDRA_DFCE_160600.equalsIgnoreCase(nomOperation)) {
         updateCassandra160600();
         // Ajout des index composites
         addIndexesCompositeToDfce("META_160600", APPL_CONCERNEE.GNS);
      } else if (GNT_CASSANDRA_DFCE_160600.equalsIgnoreCase(nomOperation)) {
         updateCassandra160600();
         // Création des métadonnées
         updateMetaDfce("META_160600");
         // Ajout des index composites
         addIndexesCompositeToDfce("META_160600", APPL_CONCERNEE.GNT);
      } else if (GNS_CASSANDRA_DFCE_160601.equalsIgnoreCase(nomOperation)) {
         updateCassandra160601();
         // Création des métadonnées
         updateMetaDfce("META_160601");
         // Ajout des index composites
         addIndexesCompositeToDfce("META_160601", APPL_CONCERNEE.GNS);
      } else if (GNT_CASSANDRA_DFCE_160601.equalsIgnoreCase(nomOperation)) {
         updateCassandra160601();
         // Création des métadonnées
         updateMetaDfce("META_160601");
         // Ajout des index composites
         addIndexesCompositeToDfce("META_160601", APPL_CONCERNEE.GNT);
      } else if (CASSANDRA_DFCE_160900.equalsIgnoreCase(nomOperation)) {
         updateCassandra160900();
         // Création des métadonnées
         updateMetaDfce("META_160900");
      } else if (CASSANDRA_DFCE_160901.equalsIgnoreCase(nomOperation)) {
         updateCassandra160901();
         // Création des métadonnées
         updateMetaDfce("META_160901");
      } else if (CASSANDRA_DFCE_161100.equalsIgnoreCase(nomOperation)) {
         updateCassandra161100();
      } else if (GNS_CASSANDRA_DFCE_170200.equalsIgnoreCase(nomOperation)) {
         updateCassandra170200();
         // Ajout des index composites
         addIndexesCompositeToDfce("META_170200", APPL_CONCERNEE.GNS);
      } else if (GNT_CASSANDRA_DFCE_170200.equalsIgnoreCase(nomOperation)) {
         updateCassandra170200();
         // Ajout des index composites
         addIndexesCompositeToDfce("META_170200", APPL_CONCERNEE.GNT);
      } else if (CASSANDRA_170201.equalsIgnoreCase(nomOperation)) {
         updateCassandra170201();
      } else if (GNS_CASSANDRA_DFCE_170202.equalsIgnoreCase(nomOperation)) {
         // Ajout des index composites
         addIndexesCompositeToDfce("META_170202", APPL_CONCERNEE.GNS);
         // Update keyspace SAE
         updateCassandra170202();
      } else if (GNT_CASSANDRA_DFCE_170202.equalsIgnoreCase(nomOperation)) {
         // Ajout des index composites
         addIndexesCompositeToDfce("META_170202", APPL_CONCERNEE.GNT);
         // Update keyspace SAE
         updateCassandra170202();
      } else if (CASSANDRA_DFCE_170900.equalsIgnoreCase(nomOperation)) {
         // Update keyspace SAE
         updateCassandra170900();
      } else if (CASSANDRA_DFCE_170901.equalsIgnoreCase(nomOperation)) {
         // Update keyspace SAE
         updateCassandra170901();
         // Ajout nouvelles des métadonnées
         updateMetaDfce("META_170901");
      } else if (CASSANDRA_DFCE_180300.equalsIgnoreCase(nomOperation)) {
         // Update keyspace SAE
         updateCassandra180300();
         // MAJ des métadonnées
         updateMetaDfce("META_180300");

      } else {

         // Opération inconnue => log + exception runtime
         String message = String.format(
               "Erreur technique : L'opération %s est inconnue", nomOperation);
         LOG.error(message);
         throw new MajLotRuntimeException(message);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void demarreUpdateDFCE(final String applicationConcernee) {
      LOG.debug("Démarrage des opérations sur la base DFCE");
      String applConcernee = StringUtils.isNotEmpty(applicationConcernee) ? applicationConcernee
            : null;

      if (APPL_CONCERNEE.DFCE.getApplName().equals(applConcernee)) {
         this.installServeurDFCE();  
         LOG.debug("Opérations terminées sur la base DFCE");
      } else {
         String message = String.format(
               "Erreur technique : L'application %s est inconnue. La modification de la base de données DFCE doit être executé avec la commande 'DFCE'.",
               applConcernee);
         LOG.error(message);
         throw new MajLotRuntimeException(message);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void demarreCreateMetadatasIndexesDroitsSAE(
         final String applicationConcernee) {
      LOG.debug("Démarrage des opérations de création des métadatas sur la base SAE");
      String gedConcerneeStr = StringUtils.isNotEmpty(applicationConcernee) ? applicationConcernee
            : null;

      APPL_CONCERNEE gedConcerneeEnum = retrieveGedConcerne(gedConcerneeStr);

      if (APPL_CONCERNEE.GNT.equals(gedConcerneeEnum)) {
         // Update des droits GED
         updateCassandraDroitsGed();
      }

      // Update commun de la base SAE
      commonUpdateSAE(gedConcerneeEnum);

      LOG.debug("Opérations de création des métadatas terminées sur la base SAE");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void demarreCreateSAE() {
      LOG.debug("Démarrage des opérations de création de la base SAE");
      this.createGedBase();
      LOG.debug("Opérations de création terminées sur la base SAE");

   }

   /**
    * Methode permettant de renvoyer la GED qui est concernée par le traitement.
    * 
    * @param gedConcernee
    *           {@link APPL_CONCERNEE}
    * @return la GED qui est concernée par le traitement.
    */
   private APPL_CONCERNEE retrieveGedConcerne(String gedConcernee) {
      APPL_CONCERNEE gedConcerneeReturn;
      if (!StringUtils.isEmpty(gedConcernee)) {
         if (APPL_CONCERNEE.GNS.getApplName().equals(gedConcernee)) {
            gedConcerneeReturn = APPL_CONCERNEE.GNS;
         } else if (APPL_CONCERNEE.GNT.getApplName().equals(gedConcernee)) {
            gedConcerneeReturn = APPL_CONCERNEE.GNT;
         } else {
            // Serveur GED inconnue => log + exception runtime
            String message = String.format(
                  "Erreur technique : Le serveur GED %s est inconnue",
                  gedConcernee);
            LOG.error(message);
            throw new MajLotRuntimeException(message);
         }

      } else {
         // Opération inconnue => log + exception runtime
         String message = "Erreur technique : Le serveur GED n'est pas renseigné. Veuillez indiquer le serveur sur lequel doit avoir lieu l'opération svp.";
         LOG.error(message);
         throw new MajLotRuntimeException(message);
      }

      LOG.debug("GED concernée par l'opération : "
            + gedConcerneeReturn.getApplName());
      return gedConcerneeReturn;
   }

   /**
    * Methode permettant de réaliser les updates pour le serveur DFCE.
    */
   private void installServeurDFCE() {
      // Update de la base DFCE
      commonUpdateDFCE();
   }

   /**
    * Methode permettant de réaliser les updates pour le serveur DFCE.
    */
   private void commonUpdateDFCE() {
      updateDFCE130700();
      updateDFCE150400();
      updateDFCE150400_P5();
      updateDFCE151000();
   }

   /**
    * Methode permettant de créer et updater la base SAE
    * 
    * @param gedConcernee
    *           {@link APPL_CONCERNEE}
    */
   private void commonUpdateSAE(APPL_CONCERNEE gedConcernee) {
      // META_130400
      updateMeta("meta130400.xml", "META_130400");
      // META_150100
      updateMeta("meta150100.xml", "META_150100");
      // META_SEPA
      updateMetaSepa();
      // CASSANDRA_DFCE_150400
      updateMetaDfce("META_150400");
      // CASSANDRA_DFCE_150600
      updateMetaDfce("META_150600");
      // CASSANDRA_151000
      updateMetaDfce("META_151000");
      // CASSANDRA_DFCE_151001
      updateMetaDfce("META_151001");
      // CASSANDRA_DFCE_151200
      updateMetaDfce("META_151200");
      // CASSANDRA_DFCE_160400
      updateMetaDfce("META_160400");
      if (APPL_CONCERNEE.GNT.equals(gedConcernee)) {
         // Ajout des index composites GNT_CASSANDRA_DFCE_160600
         addIndexesCompositeToDfce("META_160600", APPL_CONCERNEE.GNT);
         // Ajout des index composites GNT_CASSANDRA_DFCE_160601
         addIndexesCompositeToDfce("META_160601", APPL_CONCERNEE.GNT);
         // Ajout des index composites GNT_CASSANDRA_DFCE_170202
         addIndexesCompositeToDfce("META_170202", APPL_CONCERNEE.GNT);

      } else if (APPL_CONCERNEE.GNS.equals(gedConcernee)) {
         // Ajout des index composites GNS_CASSANDRA_DFCE_160600
         addIndexesCompositeToDfce("META_160600", APPL_CONCERNEE.GNS);
         // Ajout des index composites GNS_CASSANDRA_DFCE_160601
         addIndexesCompositeToDfce("META_160601", APPL_CONCERNEE.GNS);
         // Ajout des index composites GNS_CASSANDRA_DFCE_170202
         addIndexesCompositeToDfce("META_170202", APPL_CONCERNEE.GNS);
      }
      // CASSANDRA_DFCE_160900
      updateMetaDfce("META_160900");
      // CASSANDRA_DFCE_160901
      updateMetaDfce("META_160901");
      // CASSANDRA_DFCE_170901
      updateMetaDfce("META_170901");
   }

   /**
    * Connexion à DFCE
    */
   private void connectDfce() {
      serviceProvider.connect(dfceConfig.getLogin(), dfceConfig.getPassword(),
            dfceConfig.getUrlToolkit(), dfceConfig.getTimeout());
   }

   /**
    * Déconnexion à DFCE
    */
   private void disconnectDfce() {
      serviceProvider.disconnect();
   }

   /**
    * Mise à jour de la base métier pour la métadonnée CodeActivite à rendre non
    * obligatoire.
    */
   private void updateCodeActivite() {

      // Log
      LOG.info("Début de l'opération : Modification de la structure de la base DFCE pour rendre la métadonnée CodeActivite non obligatoire");

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
         LOG.info("Mise à jour effectuée avec succès : le CodeActivite n'est plus obligatoire");

      } else {

         // Le code activité n'est pas obligatoire (maj déjà effectuée, ou
         // nouvelle base)
         LOG.info("Rien à faire : la métadonnée CodeActivite est déjà en non obligatoire ");

      }

   }

   /**
    * mise à jour de la durée de conservation du type de document 3.1.3.1.1
    */
   private void updateDureeConservation() {

      // Log
      LOG.info("Début de l'opération : Modification de la durée de conservation du type de document 3.1.3.1.1 (1643 -> 1825)");

      // Connection à DFCE
      connectDfce();

      // Récupération de la durée de conservation existante
      StorageAdministrationService storageAdmin = serviceProvider
            .getStorageAdministrationService();
      LifeCycleRule lifeCycleRule = storageAdmin.getLifeCycleRule("3.1.3.1.1");
      // Depuis DFCe 1.7.0, le cycle de vie peut comporter des etapes
      // Coté Ged Nationale, nous n'en aurons qu'une seule
      int dureeConservation = lifeCycleRule.getSteps().get(0).getLength();

      // Vérifie que la mise à jour est à faire
      if (dureeConservation == DUREE_1825) {

         // La durée de conservation est déjà bonne
         LOG.info("Rien à faire : la durée de conservation de 3.1.3.1.1 est déjà bonne (1825)");

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
      LOG.info("Début de l'opération : Lot 130400 - Mise à jour du keyspace SAE");
      updater.updateToVersion4();
      LOG.info("Fin de l'opération : Lot 130400 - Mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 130700 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 5
    */
   private void updateCassandra130700() {

      LOG.info("Début de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
      updater.updateToVersion5();
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 131100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 6
    */
   private void updateCassandra131100() {

      LOG.info("Début de l'opération : Lot 131100 - Mise à jour du keyspace SAE");
      updater.updateToVersion6();
      LOG.info("Fin de l'opération : Lot 131100 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 140700 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 7
    */
   private void updateCassandra140700() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 140700 - Référentiel des formats");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion7();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 8
    */
   private void updateCassandra150100() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 141200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion8();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150400 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 9
    */
   private void updateCassandra150400() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 150400");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion9();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150600 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 10
    */
   private void updateCassandra150600() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 150600");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion10();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150601 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 11
    */
   private void updateCassandra150601() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 150601");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion11();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151000 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 12
    */
   private void updateCassandra151000() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151000");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion12();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151001 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 13
    */
   private void updateCassandra151001() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151001");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion13();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151200 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 14
    */
   private void updateCassandra151200() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion14();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151201 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 15
    */
   private void updateCassandra151201() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151201");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion15();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160300 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 16
    */
   private void updateCassandra160300() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160300");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion16();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160400 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 17
    */
   private void updateCassandra160400() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160400");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion17();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160600 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 18
    */
   private void updateCassandra160600() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160600");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion18();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160601 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 19
    */
   private void updateCassandra160601() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160601");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion19();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160900 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 20
    */
   private void updateCassandra160900() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160900");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion20();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160901 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 21
    */
   private void updateCassandra160901() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160901");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion21();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 161100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 22
    */
   private void updateCassandra161100() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 161100");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion22();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170200() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion23();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170201() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170201");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion24();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170202() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170202");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion25();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170900() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170900");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion26();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170901() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170901");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion27();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra180300() {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170901");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion28();
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Ajout des droits GED
    */
   private void updateCassandraDroitsGed() {
      LOG.info("Début de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
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

         // -- connexion a DFCE
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

   // ATTENTION, depuis la version 160600, afin de différencier GNT/GNS, cette
   // méthode ne gère que les méta DFCE et plus les indexes composite
   // Pour ajouter les indexes composites il faut utiliser
   // addIndexesCompositeToDfce
   private void updateMetaDfce(String operation) {

      // -- Récupération de la liste des métadonnées
      LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Début");
      RefMetaInitialisationService service = updater.getRefMetaInitService();
      List<MetadataReference> metadonnees = service.getListMetas();

      LOG.info(
            "Début de l'opération : Création des nouvelles métadonnées ({})",
            operation);

      // -- Mise à jour des métas
      updateBaseDfce(service.genereMetaBaseDfce(metadonnees));

      LOG.info("Fin de l'opération : Création des nouvelles métadonnées ({})",
            operation);
   }

   private void addIndexesCompositeToDfce(String operation,
         APPL_CONCERNEE gedConcernee) {

      // -- Récupération de la liste des métadonnées
      LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Début");
      RefMetaInitialisationService service = updater.getRefMetaInitService();

      LOG.info(
            "Début de l'opération : Création des nouveaux index composite ({})",
            operation);
      // -- Crétion des indexes composites (Si ils n'existent pas déjà)
      if (gedConcernee.equals(APPL_CONCERNEE.GNS)) {
         createIndexesCompositeIfNotExist(service.getIndexesCompositesGNS());
      } else if (gedConcernee.equals(APPL_CONCERNEE.GNT)) {
         createIndexesCompositeIfNotExist(service.getIndexesCompositesGNT());
      }

      LOG.info(
            "Fin de l'opération : Création des nouveaux index composite ({})",
            operation);
   }

   /**
    * Ajout de métadonnées dans DFCE à partir d'un fichier xml contenant les
    * métadonnées ex : meta130400.xml (dans /src/main/resources/)
    * 
    * @param fichierlisteMeta
    *           le fichier contenant les métadonnées
    * @param nomOperation
    *           Nom de la commande pour affichage dans les traces
    * 
    * @deprecated : Utilier updateBaseDfce() à la place désormais
    * 
    */
   @Deprecated
   private void updateMeta(String fichierlisteMeta, String nomOperation) {

      // -- connexion a DFCE
      connectDfce();

      LOG.info(
            "Début de l'opération : Création des nouvelles métadonnées ({})",
            nomOperation);

      LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Début");

      XStream xStream = new XStream();
      xStream.processAnnotations(DataBaseModel.class);
      Reader reader = null;
      InputStream stream = null;

      try {
         stream = context.getResource(fichierlisteMeta).getInputStream();
         reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
         DataBaseModel model = DataBaseModel.class
               .cast(xStream.fromXML(reader));

         LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Fin");

         // -- MAJ des métadonnées dans DFCE
         List<SaeCategory> categories;
         categories = model.getDataBase().getSaeCategories().getCategories();
         updateBaseDfce(categories);
         LOG.debug("MAJ des métadonnées dans DFCE - Fin");

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

      LOG.info("Fin de l'opération : Création des nouvelles métadonnées ({})",
            nomOperation);
   }

   private void updateBaseDfce(List<SaeCategory> categories) {

      try {
         // -- Ouverture connexion DFCE
         connectDfce();

         String baseName = dfceConfig.getBaseName();
         Base base = serviceProvider.getBaseAdministrationService().getBase(
               baseName);

         final ToolkitFactory toolkit = ToolkitFactory.getInstance();

         LOG.debug("Création des métadonnées dans DFCE - Début");

         for (SaeCategory category : categories) {

            StorageAdministrationService service;
            service = serviceProvider.getStorageAdministrationService();

            // -- Test de l'existence de la métadonnée dans DFCE
            Category catFound = service.getCategory(category.getName());

            if (catFound == null) {

               // -- Création de la catégory

               final Category categoryDfce = service.findOrCreateCategory(
                     category.getName(), category.categoryDataType());

               final BaseCategory baseCategory = toolkit.createBaseCategory(
                     categoryDfce, category.isIndex());

               baseCategory.setEnableDictionary(category.isEnableDictionary());
               baseCategory.setMaximumValues(category.getMaximumValues());
               baseCategory.setMinimumValues(category.getMinimumValues());
               baseCategory.setSingle(category.isSingle());
               base.addBaseCategory(baseCategory);

               LOG.info("La métadonnée {} sera ajoutee.",
                     category.getDescriptif());
            } else {

               // -- Mise à jour de la catégory

               final BaseCategory baseCategory = base.getBaseCategory(catFound
                     .getName());

               baseCategory.setEnableDictionary(category.isEnableDictionary());
               baseCategory.setMaximumValues(category.getMaximumValues());
               baseCategory.setMinimumValues(category.getMinimumValues());
               baseCategory.setSingle(category.isSingle());
               baseCategory.setIndexed(category.isIndex());

               LOG.info("La métadonnée {} existe :elle sera mise a jour",
                     category.getDescriptif());
            }
         }
         serviceProvider.getBaseAdministrationService().updateBase(base);
      } finally {
         // -- Fermeture connexion DFCE
         disconnectDfce();
      }
   }

   /**
    * Création des indexes composites à partir d'une liste de noms d'indexes.
    * Les indexes composites sont créés seulement si ils n'existent pas déjà.
    * Indexe à vide l'index composite si besoin
    * 
    * @param indexes
    *           Liste contenant des tableaux de code courts de méta et le
    *           boolean indiquant si on doit indexer à vide Chaque tableau de
    *           codes meta correspond à la composition de l'indexe composite à
    *           créer.
    */
   private void createIndexesCompositeIfNotExist(Map<String[], String> indexes) {

      // -- dcfe connect
      connectDfce();

      StorageAdministrationService storageAdminService;
      storageAdminService = serviceProvider.getStorageAdministrationService();
      DFCEUpdater dfceUpdater = new DFCEUpdater(cassandraConfig);

      for (Entry<String[], String> entry : indexes.entrySet()) {
         String[] metas = entry.getKey();
         String aIndexerVide = entry.getValue();

         StringBuffer nomIndex = new StringBuffer();
         Category[] categories = new Category[metas.length];

         // -- Cache de catégories pour ne pas réinterroger la base
         Map<String, Category> cacheCategories;
         cacheCategories = new HashMap<String, Category>();

         for (int i = 0; i < metas.length; i++) {

            String codeCourt = metas[i];

            // -- On récupère la catégorie qui n'est pas encore dans le cache
            if (!cacheCategories.containsKey(codeCourt)) {
               Category category;
               category = storageAdminService.getCategory(codeCourt);
               if (category == null) {
                  LOG.error("Impossible de récupérer la Category pour code {}",
                        codeCourt);
                  throw new MajLotRuntimeException("La category '" + codeCourt
                        + "' n'a pas ete trouvee");
               }
               cacheCategories.put(codeCourt, category);
               LOG.info("Category {} récupérée", category.getName());
            }

            categories[i] = cacheCategories.get(codeCourt);

            nomIndex.append(codeCourt);
            nomIndex.append('&');
         }

         // -- creation de l'index composite
         LOG.info("Creation de l'index composite {}", nomIndex);
         CompositeIndex indexComposite = storageAdminService
               .findOrCreateCompositeIndex(categories);
         if (indexComposite == null) {
            String mssgErreur = "Impossible de créer l'index composite: "
                  + nomIndex;
            throw new MajLotRuntimeException(mssgErreur);
         }

         // Indexation à vide si besoin
         if ("oui".equals(aIndexerVide)) {
            LOG.info("Indexation de l'index composite {}", nomIndex);
            dfceUpdater.indexeAVideCompositeIndex(nomIndex.toString());
         }
      }
   }

   /**
    * Pour lot 130700 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.2.x de DFCE
    */
   private void updateDFCE130700() {

      LOG.info("Début de l'opération : Lot 130700 - Mise à jour du schéma DFCE");
      DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(
            cassandraConfig);
      dfceUpdater.updateToVersion110();
      dfceUpdater.updateToVersion120();
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du schéma DFCE");

   }

   /**
    * Pour lot 150400 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.2.9-P2 de DFCE pour la gestion des Notes
    */
   private void updateDFCE150400() {

      LOG.info("Début de l'opération : Lot 150400 - Mise à jour du schéma DFCE");
      DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(
            cassandraConfig);
      dfceUpdater.updateToVersion129_P2();
      LOG.info("Fin de l'opération : Lot 150400 - Mise à jour du schéma DFCE");
   }

   /**
    * Pour lot 150400_P5 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.2.9-P5 de DFCE pour la correction sur les méta de
    * type float et double
    */
   private void updateDFCE150400_P5() {

      LOG.info("Début de l'opération : Lot 150400_P5 - Mise à jour du schéma DFCE");
      DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(
            cassandraConfig);
      dfceUpdater.updateToVersion129_P5();
      LOG.info("Fin de l'opération : Lot 150400_P5 - Mise à jour du schéma DFCE");
   }

   /**
    * Pour le lot 150400, nous avons cree des index composite pour SICOMOR. Or,
    * lorsqu'il utilise un des index composites, la requete lancee n'utilise pas
    * le bon index composite (JIRA-154). Pour pallier temporaire ce probleme,
    * nous allons supprimer cette index composite de docubase.
    */

   // private void disableCompositeIndex() {
   //
   // RefMetaInitialisationService service = updater.getRefMetaInitService();
   // LOG.info("Début de l'opération : DISABLE_COMPOSITE_INDEX - Mise à jour de DFCE");
   // DFCEUpdater dfceUpdater = new DFCEUpdater(cassandraConfig);
   // dfceUpdater.disableCompositeIndex(service
   // .getIndexesCompositesASupprimer());
   // LOG.info("Fin de l'opération : DISABLE_COMPOSITE_INDEX - Mise à jour de DFCE");
   // }

   private void disableCompositeIndex(APPL_CONCERNEE gedConcernee) {

      RefMetaInitialisationService service = updater.getRefMetaInitService();
      LOG.info("Début de l'opération : DISABLE_COMPOSITE_INDEX - Mise à jour de DFCE");
      DFCEUpdater dfceUpdater = new DFCEUpdater(cassandraConfig);

      if (APPL_CONCERNEE.GNS.equals(gedConcernee)) {
         dfceUpdater.disableCompositeIndex(service
               .getIndexesCompositesASupprimerGNS());
      } else if (APPL_CONCERNEE.GNT.equals(gedConcernee)) {
         dfceUpdater.disableCompositeIndex(service
               .getIndexesCompositesASupprimerGNT());
      }

      LOG.info("Fin de l'opération : DISABLE_COMPOSITE_INDEX - Mise à jour de DFCE");
   }

   /**
    * Pour lot 151000 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.6.1 de DFCE
    */
   private void updateDFCE151000() {

      LOG.info("Début de l'opération : Lot 151000 - Mise à jour du schéma DFCE");
      DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(
            cassandraConfig);
      dfceUpdater.updateToVersion170();
      LOG.info("Fin de l'opération : Lot 151000 - Mise à jour du schéma DFCE");
   }

   /**
    * Pour lot 160400 du SAE : Indexation du numeroIdArchivage
    */
   private void updateDFCE160400() {

      LOG.info("Début de l'opération : Lot 160400 - Indexation du numeroIdArchivage");
      // Indexation de numeroIdArchivage
      DFCEUpdater dfceUpdater = new DFCEUpdater(cassandraConfig);
      dfceUpdater.indexeAVideIndexSimple("nid", dfceConfig.getBaseName());
      LOG.info("Fin de l'opération : Lot 160400 - Indexation du numeroIdArchivage");
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
      updater.updateToVersion9();
      updater.updateToVersion10();
      updater.updateToVersion11();
      updater.updateToVersion12();
      updater.updateToVersion13();
      updater.updateToVersion14();
      updater.updateToVersion15();
      updater.updateToVersion16();
      updater.updateToVersion17();
      updater.updateToVersion18();
      updater.updateToVersion19();
      updater.updateToVersion20();
      updater.updateToVersion21();
      updater.updateToVersion22();
      updater.updateToVersion23();
      updater.updateToVersion24();
      updater.updateToVersion25();
      updater.updateToVersion26();
      updater.updateToVersion27();
      updater.updateToVersion28();
   }

}
