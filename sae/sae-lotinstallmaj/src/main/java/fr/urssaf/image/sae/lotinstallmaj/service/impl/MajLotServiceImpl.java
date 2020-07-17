package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.lotinstallmaj.component.DFCEConnexionComponent;
import fr.urssaf.image.sae.lotinstallmaj.component.Initializer;
import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersion;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotAlreadyInstallUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotInexistantUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotManualUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRestartTomcatException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotUnknownDFCEVersion;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import fr.urssaf.image.sae.lotinstallmaj.modele.DataBaseModel;
import fr.urssaf.image.sae.lotinstallmaj.modele.InfoLot;
import fr.urssaf.image.sae.lotinstallmaj.modele.SaeCategory;
import fr.urssaf.image.sae.lotinstallmaj.service.DFCECassandraUpdaterV2;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.DFCECassandraUpdaterCQL;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAECassandraUpdaterCQL;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

/**
 * Opérations de mise à jour du SAE.
 */
@Service
public final class MajLotServiceImpl implements MajLotService {

   public static final int DUREE_1825 = 1825;

   public static final int DUREE_1643 = 1643;

   /**
    * Enum qui décrit les différentes GED qui sont concernées par les
    * opérations demandées.
    */
   public static enum APPL_CONCERNEE {
      GNS("GNS"), GNT("GNT"), DFCE("DFCE");

      /**
       * Nom de la GED.
       */
      private final String applName;

      /**
       * Constructeur.
       *
       * @param gedName
       *          Nom de la GED.
       */
      private APPL_CONCERNEE(final String gedName) {
         applName = gedName;
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

   private static final Logger LOG = LoggerFactory.getLogger(MajLotServiceImpl.class);

   private static final String PARAMETERS_CF_NAME = "parameters";

   @Autowired
   private DFCEConnexionComponent dfceConnexionComponent;

   @Autowired
   private ApplicationContext context;

   @Autowired
   private SAECassandraUpdater updater;

   @Autowired
   private SAECassandraUpdaterCQL updaterCQL;

   @Autowired
   private DFCECassandraUpdaterThriftImpl dfceCassandraUpdaterThrift;

   @Autowired
   private DFCECassandraUpdaterCQL dfceCassandraUpdaterCQL;

   private DFCECassandraUpdaterV2 dfceCassandraUpdater;

   @Autowired
   private ModeApiCqlSupport modeApiCqlSupport;

   @Autowired
   private CassandraConfig cassandraConfig;

   @Autowired
   private DFCEIndexeMetaUpdaterService dfceIndexeMetaUpdaterService;

   @Autowired
   private Initializer initializer;

   @Value("${sae.typePlateforme}")
   private String typePlatform;

   @PostConstruct
   private void postConstruct() {
      try {
         if (modeApiCqlSupport.isModeCql(PARAMETERS_CF_NAME)) {
            dfceCassandraUpdater = dfceCassandraUpdaterCQL;
         } else if (modeApiCqlSupport.isModeThrift(PARAMETERS_CF_NAME)) {
            dfceCassandraUpdater = dfceCassandraUpdaterThrift;
         }
      }
      catch (final InvalidQueryException e) {
         LOG.info("Nous sommes mode HECTOR!");
         dfceCassandraUpdater = dfceCassandraUpdaterThrift;
      }

   }

   /**
    * {@inheritDoc}
    * 
    * @throws MajLotManualUpdateException
    * @throws MajLotUnknownDFCEVersion
    * @throws MajLotGeneralException
    */
   @Override
   public void demarreCreateSAE() throws MajLotRestartTomcatException, MajLotManualUpdateException, MajLotUnknownDFCEVersion {

      LOG.debug("Démarrage des opérations de création de la base SAE");
      updater.beforeCreate();
      if (dfceCassandraUpdater.isReadyForUpdate()) {
         createGedBase();
         initializer.createBaseDfce();
      } else {
         final String message = "DFCE a été mis jour. Vous devez redémarrer le Serveur Tomcat de la Webapp DFCE,"
               + "puis relancer la commande de creation [script_name] --create ";

         throw new MajLotRestartTomcatException(message);
      }

      LOG.debug("Opérations de création terminées sur la base SAE");
   }

   /**
    * Methode permettant de renvoyer la GED qui est concernée par le
    * traitement.
    * 
    * @param gedConcernee
    *          {@link APPL_CONCERNEE}
    * @return la GED qui est concernée par le traitement.
    */
   private APPL_CONCERNEE retrieveGedConcerne(final String gedConcernee) {
      APPL_CONCERNEE gedConcerneeReturn;
      if (!StringUtils.isEmpty(gedConcernee)) {
         if (APPL_CONCERNEE.GNS.getApplName().equals(gedConcernee)) {
            gedConcerneeReturn = APPL_CONCERNEE.GNS;
         } else if (APPL_CONCERNEE.GNT.getApplName().equals(gedConcernee)) {
            gedConcerneeReturn = APPL_CONCERNEE.GNT;
         } else {
            // Serveur GED inconnue => log + exception runtime
            final String message = String.format("Erreur technique : Le serveur GED %s est inconnue", gedConcernee);
            LOG.error(message);
            throw new MajLotRuntimeException(message);
         }

      } else {
         // Opération inconnue => log + exception runtime
         final String message = "Erreur technique : Le serveur GED n'est pas renseigné. Veuillez indiquer le serveur sur lequel doit avoir lieu l'opération svp.";
         LOG.error(message);
         throw new MajLotRuntimeException(message);
      }

      LOG.debug("GED concernée par l'opération : " + gedConcerneeReturn.getApplName());
      return gedConcerneeReturn;
   }

   /**
    * Mise à jour de la base métier pour la métadonnée CodeActivite à rendre
    * non obligatoire.
    */
   private void updateCodeActivite() {

      // Log
      LOG.info(
            "Début de l'opération : Modification de la structure de la base DFCE pour rendre la métadonnée CodeActivite non obligatoire");

      // Connection à DFCE
      dfceConnexionComponent.connectDfce();
      final ServiceProvider serviceProvider = dfceConnexionComponent.getServiceProvider();

      // recupération de la metadonnee CodeActivite et verification qu'elle
      // est
      // bien dans l'état
      // attendu, à savoir qu'elle est obligatoire.
      final BaseAdministrationService baseService = serviceProvider.getBaseAdministrationService();
      final Base base = baseService.getBase(dfceConnexionComponent.getDfceConnexionParameter().getBaseName());
      final BaseCategory baseCategory = base.getBaseCategory("act");
      final int minValues = baseCategory.getMinimumValues();

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
      dfceConnexionComponent.disconnectDfce();
   }

   /**
    * mise à jour de la durée de conservation du type de document 3.1.3.1.1
    */
   private void updateDureeConservation() {

      // Log
      LOG.info(
            "Début de l'opération : Modification de la durée de conservation du type de document 3.1.3.1.1 (1643 -> 1825)");

      // Connection à DFCE
      dfceConnexionComponent.connectDfce();
      final ServiceProvider serviceProvider = dfceConnexionComponent.getServiceProvider();

      // Récupération de la durée de conservation existante
      final StorageAdministrationService storageAdmin = serviceProvider.getStorageAdministrationService();
      final LifeCycleRule lifeCycleRule = storageAdmin.getLifeCycleRule("3.1.3.1.1");
      // Depuis DFCe 1.7.0, le cycle de vie peut comporter des etapes
      // Coté Ged Nationale, nous n'en aurons qu'une seule
      final int dureeConservation = lifeCycleRule.getSteps().get(0).getLength();

      // Vérifie que la mise à jour est à faire
      if (dureeConservation == DUREE_1825) {

         // La durée de conservation est déjà bonne
         LOG.info("Rien à faire : la durée de conservation de 3.1.3.1.1 est déjà bonne (1825)");

      } else {
         dfceConnexionComponent.disconnectDfce();
         // TODO : en attente du traitement du JIRA CRTL-81
         throw new MajLotRuntimeException("Opération non réalisable : en attente du traitement du JIRA CRTL-81");
      }
      dfceConnexionComponent.disconnectDfce();
   }

   /**
    * Pour lot 120510 du SAE : création du keyspace "SAE" dans cassandra, en
    * version 1
    */
   private void updateCassandra120510(final boolean isRedo) {
      LOG.info("Début de l'opération : création du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion1(isRedo);
      LOG.info("Fin de l'opération : création du keyspace SAE");
   }

   /**
    * Pour lot 120512 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 2
    */
   private void updateCassandra120512(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion2(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 120910 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 3
    */
   private void updateCassandra120910(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion3(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 13xx10 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 4
    */
   private void updateCassandra130400(final boolean isRedo) {
      LOG.info("Début de l'opération : Lot 130400 - Mise à jour du keyspace SAE");
      updater.updateToVersion4(isRedo);
      LOG.info("Fin de l'opération : Lot 130400 - Mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 130700 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 5
    */
   private void updateCassandra130700(final boolean isRedo) {

      LOG.info("Début de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
      updater.updateToVersion5(isRedo);
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 131100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 6
    */
   private void updateCassandra131100(final boolean isRedo) {

      LOG.info("Début de l'opération : Lot 131100 - Mise à jour du keyspace SAE");
      updater.updateToVersion6(isRedo);
      LOG.info("Fin de l'opération : Lot 131100 - Mise à jour du keyspace SAE");

   }

   /**
    * Pour lot 140700 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 7
    */
   private void updateCassandra140700(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 140700 - Référentiel des formats");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion7(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 8
    */
   private void updateCassandra150100(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 141200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion8(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150400 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 9
    */
   private void updateCassandra150400(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 150400");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion9(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150600 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 10
    */
   private void updateCassandra150600(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 150600");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion10(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 150601 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 11
    */
   private void updateCassandra150601(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 150601");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion11(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151000 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 12
    */
   private void updateCassandra151000(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151000");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion12(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151001 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 13
    */
   private void updateCassandra151001(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151001");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion13(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151200 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 14
    */
   private void updateCassandra151200(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion14(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 151201 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 15
    */
   private void updateCassandra151201(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 151201");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion15(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160300 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 16
    */
   private void updateCassandra160300(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160300");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion16(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160400 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 17
    */
   private void updateCassandra160400(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160400");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion17(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160600 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 18
    */
   private void updateCassandra160600(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160600");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion18(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160601 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 19
    */
   private void updateCassandra160601(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160601");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion19(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160900 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 20
    */
   private void updateCassandra160900(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160900");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion20(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 160901 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 21
    */
   private void updateCassandra160901(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 160901");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion21(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 161100 du SAE : mise à jour du keyspace "SAE" dans cassandra, en
    * version 22
    */
   private void updateCassandra161100(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 161100");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion22(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170200(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion23(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170201(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170201");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion24(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170202(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170202");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion25(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170900(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170900");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion26(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra170901(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 170901");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion27(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra180300(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 180300");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion28(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra180900(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 180900");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion29(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra180901(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 180900");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion30(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra190700(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 190700");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion31(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra200200(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 200200");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion32(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra200500(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 200500");
      // Récupération de la chaîne de connexion au cluster cassandra
      updater.updateToVersion33(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   private void updateCassandra201100(final boolean isRedo) {
      LOG.info("Début de l'opération : mise à jour du keyspace SAE pour le lot 201100");
      // Récupération de la chaîne de connexion au cluster cassandra
      updaterCQL.updateToVersion34(isRedo);
      LOG.info("Fin de l'opération : mise à jour du keyspace SAE");
   }

   /**
    * Pour lot 120912 du SAE : mise à jour du modèle de données des documents.
    */
   private void updateMetaSepa() {
      LOG.info("Début de l'opération : ajout des métadonnées au document");

      LOG.info("- début de récupération des catégories à ajouter");
      final XStream xStream = new XStream();
      xStream.processAnnotations(DataBaseModel.class);

      try (InputStream stream = context.getResource("metaSepa.xml").getInputStream();
            Reader reader = new InputStreamReader(stream, Charset.forName("UTF-8"))) {

         final DataBaseModel model = DataBaseModel.class.cast(xStream.fromXML(reader));

         LOG.info("- fin de récupération des catégories à ajouter");

         // -- connexion a DFCE
         dfceConnexionComponent.connectDfce();
         final ServiceProvider serviceProvider = dfceConnexionComponent.getServiceProvider();

         final Base base = serviceProvider.getBaseAdministrationService().getBase(dfceConnexionComponent.getDfceConnexionParameter().getBaseName());

         final List<BaseCategory> baseCategories = new ArrayList<>();
         final ToolkitFactory toolkit = ToolkitFactory.getInstance();
         for (final SaeCategory category : model.getDataBase().getSaeCategories().getCategories()) {
            final Category categoryDfce = serviceProvider.getStorageAdministrationService()
                  .findOrCreateCategory(category.getName(), category.categoryDataType());
            final BaseCategory baseCategory = toolkit.createBaseCategory(categoryDfce, category.isIndex());
            baseCategory.setEnableDictionary(category.isEnableDictionary());
            baseCategory.setMaximumValues(category.getMaximumValues());
            baseCategory.setMinimumValues(category.getMinimumValues());
            baseCategory.setSingle(category.isSingle());
            baseCategories.add(baseCategory);
         }

         LOG.info("- début d'insertion des catégories");
         for (final BaseCategory baseCategory : baseCategories) {
            base.addBaseCategory(baseCategory);
         }

         serviceProvider.getBaseAdministrationService().updateBase(base);

         LOG.info("- fin d'insertion des catégories");

      }
      catch (final IOException e) {
         LOG.warn("impossible de récupérer le fichier contenant les données");
      }
      finally {
         dfceConnexionComponent.disconnectDfce();
      }

      LOG.info("Fin de l'opération : ajout des métadonnées au document");
   }


   /**
    * Ajout de métadonnées dans DFCE à partir d'un fichier xml contenant les
    * métadonnées ex : meta130400.xml (dans /src/main/resources/)
    * 
    * @param fichierlisteMeta
    *          le fichier contenant les métadonnées
    * @param nomOperation
    *          Nom de la commande pour affichage dans les traces
    * @deprecated : Utilier updateBaseDfce() à la place désormais
    */
   @Deprecated
   private void updateMeta(final String fichierlisteMeta, final String nomOperation) {

      // -- connexion a DFCE
      dfceConnexionComponent.connectDfce();
      final ServiceProvider serviceProvider = dfceConnexionComponent.getServiceProvider();

      LOG.info("Début de l'opération : Création des nouvelles métadonnées ({})", nomOperation);

      LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Début");

      final XStream xStream = new XStream();
      xStream.processAnnotations(DataBaseModel.class);

      try (InputStream stream = context.getResource(fichierlisteMeta).getInputStream();
            Reader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));) {

         final DataBaseModel model = DataBaseModel.class.cast(xStream.fromXML(reader));

         LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Fin");

         // -- MAJ des métadonnées dans DFCE
         List<SaeCategory> categories;
         categories = model.getDataBase().getSaeCategories().getCategories();
         dfceIndexeMetaUpdaterService.updateBaseDfce(categories);
         LOG.debug("MAJ des métadonnées dans DFCE - Fin");

      }
      catch (final IOException e) {
         LOG.warn("impossible de récupérer le fichier contenant les données");
      }
      finally {
         dfceConnexionComponent.disconnectDfce();
      }


      LOG.info("Fin de l'opération : Création des nouvelles métadonnées ({})", nomOperation);
   }





   /**
    * Pour lot 130700 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.2.x de DFCE
    */
   private void updateDFCE130700() {

      LOG.info("Début de l'opération : Lot 130700 - Mise à jour du schéma DFCE");
      final DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(cassandraConfig);
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
      final DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(cassandraConfig);
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
      final DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(cassandraConfig);
      dfceUpdater.updateToVersion129_P5();
      LOG.info("Fin de l'opération : Lot 150400_P5 - Mise à jour du schéma DFCE");
   }

   /**
    * Pour lot 151000 du SAE : mise à jour du keyspace "Docubase" pour le
    * passage à la version 1.6.1 de DFCE
    */
   private void updateDFCE151000() {

      LOG.info("Début de l'opération : Lot 151000 - Mise à jour du schéma DFCE");
      final DFCECassandraUpdater dfceUpdater = new DFCECassandraUpdater(cassandraConfig);
      dfceUpdater.updateToVersion170();
      LOG.info("Fin de l'opération : Lot 151000 - Mise à jour du schéma DFCE");
   }


   /**
    * Création de la base GED
    * 
    * @throws MajLotManualUpdateException
    * @throws MajLotGeneralException
    */
   private void createGedBase() {
      if (LotVersion.getLastAvailableVersion() > updater.getDatabaseVersion()) {
         updater.updateToVersion1(false);
         updater.updateToVersion2(false);
         updater.updateToVersion3(false);
         updater.updateToVersion4(false);
         updater.updateToVersion5(false);
         updater.updateToVersion6(false);
         updater.updateToVersion7(false);
         updater.updateToVersion8(false);
         updater.updateToVersion9(false);
         updater.updateToVersion10(false);
         updater.updateToVersion11(false);
         updater.updateToVersion12(false);
         updater.updateToVersion13(false);
         updater.updateToVersion14(false);
         updater.updateToVersion15(false);
         updater.updateToVersion16(false);
         updater.updateToVersion17(false);
         updater.updateToVersion18(false);
         updater.updateToVersion19(false);
         updater.updateToVersion20(false);
         updater.updateToVersion21(false);
         updater.updateToVersion22(false);
         updater.updateToVersion23(false);
         updater.updateToVersion24(false);
         updater.updateToVersion25(false);
         updater.updateToVersion26(false);
         updater.updateToVersion27(false);
         updater.updateToVersion28(false);
         updater.updateToVersion29(false);
         updater.updateToVersion30(false);
         /*
          * updater.updateToVersion31(false);
          * updater.updateToVersion32(false);
          * updater.updateToVersion33(false);
          */
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void info() {
      LOG.info("\n*************************** Informations courantes de la base *******************************\n");
      LOG.info("La version actuelle de la base de données est : {} \n", updater.getDatabaseVersion());
      LOG.info("La dernière version disponible de la base de données est : {} \n", LotVersion.getLastAvailableVersion());
      LOG.info("*************************** Fin ***************************************\n");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InfoLot getVersionInfo(final int version) {
      final InfoLot info = LotVersion.getInfoLotByVersion(version);
      LOG.info("==================== Informations sur la version {} ====================== \n\n", info.getVersion());
      LOG.info("Le nom du lot est {} \n", info.getNomLot());
      LOG.info("Descriptif du lot : {} \n", info.getDescriptif());
      LOG.info("\n==================== Fin ======================\n");
      return info;
   }



   /**
    * {@inheritDoc}
    */
   @Override
   public void forceVersion(final int version) {
      updater.updateDatabaseVersion(version);
      LOG.info("La base a bien été mise en version {}", version);
   }

   /**
    * {@inheritDoc}
    * 
    * @throws MajLotAlreadyInstallUpdateException
    * @throws MajLotInexistantUpdateException
    * @throws MajLotUnknownDFCEVersion
    * @throws MajLotRestartTomcatException
    */
   @Override
   public InfoLot updateToVersion(final int version)
         throws MajLotGeneralException, MajLotManualUpdateException,
         MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException, MajLotUnknownDFCEVersion, MajLotRestartTomcatException {
      final InfoLot infoLot = updateToVersion(version, false);

      return infoLot;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws MajLotManualUpdateException
    * @throws MajLotGeneralException
    * @throws MajLotInexistantUpdateException
    * @throws MajLotAlreadyInstallUpdateException
    * @throws MajLotUnknownDFCEVersion
    * @throws MajLotRestartTomcatException
    */
   @Override
   public void redo(final int version)
         throws MajLotGeneralException, MajLotManualUpdateException, MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
         MajLotUnknownDFCEVersion, MajLotRestartTomcatException {
      updateToVersion(version, true);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InfoLot beforeUpdate(final int version) throws MajLotManualUpdateException, MajLotInexistantUpdateException {
      final InfoLot infoLot = LotVersion.getInfoLotByVersion(version);

      // Si le la mise à jour doit être faite en dehors du lot-install
      if (infoLot.isManuel()) {
         LOG.info("Lancement de la mise à jour : {}\n", version);
         throw new MajLotManualUpdateException(infoLot.getDescriptif());
      }

      return infoLot;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws MajLotUnknownDFCEVersion
    * @throws MajLotRestartTomcatException
    * @throws MajLotInexistantUpdateException
    */
   @Override
   public void selectUpdate(final int version, final boolean isRedo)
         throws MajLotGeneralException, MajLotAlreadyInstallUpdateException, MajLotUnknownDFCEVersion, MajLotRestartTomcatException,
         MajLotInexistantUpdateException {

      final int last = LotVersion.getLastAvailableVersion();
      // Si la base dfce est à jour et que la webapp est démarré
      if (dfceCassandraUpdater.isReadyForUpdate()) {
         if (version == LotVersion.CASSANDRA_120510.getNumVersionLot()) {
            updateCassandra120510(isRedo);
         } else if (version == LotVersion.CASSANDRA_120512.getNumVersionLot()) {
            updateCassandra120512(isRedo);
         } else if (version == LotVersion.CASSANDRA_121110.getNumVersionLot()) {
            updateCassandra120910(isRedo);
         } else if (version == LotVersion.CASSANDRA_130400.getNumVersionLot()) {
            updateCassandra130400(isRedo);
         } else if (version == LotVersion.CASSANDRA_130700.getNumVersionLot()) {
            updateCassandra130700(isRedo);
         } else if (version == LotVersion.CASSANDRA_131100.getNumVersionLot()) {
            updateCassandra131100(isRedo);
         } else if (version == LotVersion.CASSANDRA_140700.getNumVersionLot()) {
            updateCassandra140700(isRedo);
         } else if (version == LotVersion.CASSANDRA_150100.getNumVersionLot()) {
            updateCassandra150100(isRedo);
         } else if (version == LotVersion.CASSANDRA_151000.getNumVersionLot()) {
            updateCassandra151000(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_150400.getNumVersionLot()) {
            updateCassandra150400(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_150600.getNumVersionLot()) {
            updateCassandra150600(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_150601.getNumVersionLot()) {
            updateCassandra150601(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_151001.getNumVersionLot()) {
            updateCassandra151001(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_151200.getNumVersionLot()) {
            updateCassandra151200(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_151201.getNumVersionLot()) {
            updateCassandra151201(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_160300.getNumVersionLot()) {
            updateCassandra160300(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_160400.getNumVersionLot()) {
            updateCassandra160400(isRedo);
         } else if (version == LotVersion.GNS_CASSANDRA_DFCE_160600.getNumVersionLot()) {
            updateCassandra160600(isRedo);
         } else if (version == LotVersion.GNS_CASSANDRA_DFCE_160601.getNumVersionLot()) {
            updateCassandra160601(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_160900.getNumVersionLot()) {
            updateCassandra160900(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_160901.getNumVersionLot()) {
            updateCassandra160901(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_161100.getNumVersionLot()) {
            updateCassandra161100(isRedo);
         } else if (version == LotVersion.GNS_CASSANDRA_DFCE_170200.getNumVersionLot()) {
            updateCassandra170200(isRedo);
         } else if (version == LotVersion.CASSANDRA_170201.getNumVersionLot()) {
            updateCassandra170201(isRedo);
         } else if (version == LotVersion.GNS_CASSANDRA_DFCE_170202.getNumVersionLot()) {
            updateCassandra170202(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_170900.getNumVersionLot()) {
            updateCassandra170900(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_170901.getNumVersionLot()) {
            updateCassandra170901(isRedo);
         } else if (version == LotVersion.GNS_CASSANDRA_DFCE_180300.getNumVersionLot()) {
            updateCassandra180300(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_180900.getNumVersionLot()) {
            updateCassandra180900(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_180901.getNumVersionLot()) {
            updateCassandra180901(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_190700.getNumVersionLot()) {
            updateCassandra190700(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_200200.getNumVersionLot()) {
            updateCassandra200200(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_200500.getNumVersionLot()) {
            updateCassandra200500(isRedo);
         } else if (version == LotVersion.CASSANDRA_DFCE_201100.getNumVersionLot()) {
            updateCassandra201100(isRedo);
         } else {
            throw new MajLotInexistantUpdateException("Mise à jour en version : " + version + " inexistante!! "
                  + "Veuillez crééer cette mise à jour puis réexecutez votre commande.");
         }
      } else {
         throw new MajLotRestartTomcatException();
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void afterUpdate(final int version) {
      final InfoLot infoLot = LotVersion.getInfoLotByVersion(version);
      if (version == LotVersion.getLastAvailableVersion()) {
         dfceIndexeMetaUpdaterService.updateMetaAndIndexesComposites(infoLot.getNomLot());
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void beforeCreate() {

   }

}
