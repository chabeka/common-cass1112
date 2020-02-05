package fr.urssaf.image.sae.batch.documents.executable.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.dfce.manager.DFCEConnectionFactory;
import fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.batch.documents.executable.model.AbstractParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationsEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.model.DeleteDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ExportDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ImportDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import fr.urssaf.image.sae.batch.documents.executable.service.TraitementService;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.ConfigurationServiceImpl;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.DfceServiceImpl;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.TraitementServiceImpl;
import fr.urssaf.image.sae.batch.documents.executable.utils.ValidationUtils;

/**
 * Classe de lancement des traitements.
 */
public class ExecutableMain {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory.getLogger(ExecutableMain.class);

   /**
    * Nom des services disponibles
    */
   public static final String IMPORT_DOCUMENTS = "IMPORT_DOCUMENTS";

   public static final String EXPORT_DOCUMENTS = "EXPORT_DOCUMENTS";

   public static final String DELETE_DOCUMENTS = "DELETE_DOCUMENTS";

   public static final String DELETE_DOCUMENTS_CORBEILLE = "DELETE_DOCUMENTS_CORBEILLE";

   public static final String GEL_DOCUMENT = "GEL_DOCUMENT";

   public static final String DEGEL_DOCUMENT = "DEGEL_DOCUMENT";

   /**
    * Nom des environnements
    */
   public static final String INTEGRATION_CLIENTE_GNS = "INTEGRATION_CLIENTE_GNS";

   public static final String INTEGRATION_CLIENTE_GNT = "INTEGRATION_CLIENTE_GNT";

   public static final String INTEGRATION_INTERNE_2_GNT = "INTEGRATION_INTERNE_2_GNT";

   public static final String INTEGRATION_CLIENTE_PAJE_GNS = "INTEGRATION_CLIENTE_PAJE_GNS";

   public static final String INTEGRATION_CLIENTE_PAJE_GNT = "INTEGRATION_CLIENTE_PAJE_GNT";

   public static final String INTEGRATION_NATIONALE_C1_GNS = "INTEGRATION_NATIONALE_C1_GNS";

   public static final String INTEGRATION_NATIONALE_C1_GNT = "INTEGRATION_NATIONALE_C1_GNT";

   public static final String INTEGRATION_NATIONALE_C2_GNS = "INTEGRATION_NATIONALE_C1_GNS";

   public static final String INTEGRATION_NATIONALE_C2_GNT = "INTEGRATION_NATIONALE_C1_GNT";

   public static final String VALIDATION_NATIONALE_L1_GNS = "VALIDATION_NATIONALE_L1_GNS";

   public static final String VALIDATION_NATIONALE_L1_GNT = "VALIDATION_NATIONALE_L1_GNT";

   public static final String VALIDATION_NATIONALE_L4_GNS = "VALIDATION_NATIONALE_L4_GNS";

   public static final String VALIDATION_NATIONALE_L4_GNT = "VALIDATION_NATIONALE_L4_GNT";

   public static final String FORMATION_GNS = "FORMATION_GNS";

   public static final String FORMATION_GNT = "FORMATION_GNT";

   public static final String INTEGRATION_INTERNE_GNT = "INTEGRATION_INTERNE_GNT";

   public static final String INTEGRATION_INTERNE_GNS = "INTEGRATION_INTERNE_GNS";

   public static final String DEVELOPPPEMENT_GNT = "DEVELOPPPEMENT_GNT";

   public static final String DEVELOPPPEMENT_GNS = "DEVELOPPPEMENT_GNS";

   public static final String PIC_GNT = "PIC_GNT";

   public static final String PIC_GNS = "PIC_GNS";

   private static final String[] AVAIBLE_SERVICES = new String[] {
         IMPORT_DOCUMENTS,
         EXPORT_DOCUMENTS,
         DELETE_DOCUMENTS,
         GEL_DOCUMENT,
         DEGEL_DOCUMENT,
         DELETE_DOCUMENTS_CORBEILLE
   };

   private final String[] DELETE_IMPORT_ALLOWED_ENVS = { PIC_GNT,
         PIC_GNS,
         "ENV_DEVELOPPEMENT",
         DEVELOPPPEMENT_GNT, DEVELOPPPEMENT_GNS, INTEGRATION_CLIENTE_GNT, INTEGRATION_CLIENTE_GNS,
         INTEGRATION_NATIONALE_C1_GNS, INTEGRATION_NATIONALE_C1_GNT, INTEGRATION_NATIONALE_C2_GNS,
         INTEGRATION_NATIONALE_C2_GNT,
         VALIDATION_NATIONALE_L1_GNS, VALIDATION_NATIONALE_L1_GNT, VALIDATION_NATIONALE_L4_GNS,
         VALIDATION_NATIONALE_L4_GNT,
         FORMATION_GNS, FORMATION_GNT, INTEGRATION_INTERNE_GNT, INTEGRATION_INTERNE_GNS, INTEGRATION_INTERNE_GNT,
         INTEGRATION_INTERNE_2_GNT, INTEGRATION_CLIENTE_PAJE_GNS, INTEGRATION_CLIENTE_PAJE_GNT
   };

   /**
    * Liste liste des environnements disponibles
    */
   ConfigurationsEnvironnement envList;

   /**
    * Constructeur.
    * 
    * @param ctxPath
    *          chemin de la configuration de l'exécutable
    * @param fileConf
    *          fichier de configuration des environnements
    */
   protected ExecutableMain(final File fileConf) {

      ConfigurationServiceImpl configSce;
      configSce = new ConfigurationServiceImpl();

      try {
         envList = configSce.chargerConfiguration(fileConf);
      }
      catch (final NullPointerException e) {
         throw new RuntimeException("Le chemin du fichier ne peut être null", e);
      }
      catch (final IOException e) {
         throw new RuntimeException("Echec du chargements des environnements", e);
      }
   }

   /**
    * Méthode appelée lors du lancement.
    * 
    * @param args
    *          arguments passés en paramètres
    */
   public static void main(final String[] args) {
      LOGGER.info("Arguments CMD : [{}]", StringUtils.join(args, ", "));
      final String environments = "config/environnements.xml";
      final InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(environments);
      try {
         final File conf = inputStreamToTempFile(resource);
         final ExecutableMain main = new ExecutableMain(conf);
         main.execute(args);
      }
      catch (final IOException e) {
         LOGGER.error("Echec chargement fichier de configuration des environnements");
      }
   }

   /**
    * Méthode permettant d'exécuter le traitement.
    * 
    * @param args
    *          arguments passés en paramètres
    * @throws FileNotFoundException
    */
   protected final void execute(final String[] args) {

      if (ValidationUtils.isArgumentsVide(args, 0)) {
         LOGGER.warn("Le service demandé doit être renseigné.");
         return;
      }
      if (ValidationUtils.isArgumentsVide(args, 1)) {
         LOGGER.warn("L'environnement cible être renseigné.");
         return;
      }

      final String service = args[0];
      final String environment = args[1];
      // Vérifie l'existence de l'environnement
      if (!envList.existe(environment)) {
         throw new RuntimeException("L'environnement demandé: " + environment + " n'existe pas.");
      }

      // -- Contrôle la compatibilité de de service demandé avec l'env cible
      if (!checkServiceEnvironment(service, environment)) {
         return;
      }

      if (service.equals(GEL_DOCUMENT)) {
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le paramètre 'document_uuid' est obligatoire pour le service {}", service);
            return;
         }
         final String docUUIDAsString = args[2];
         final UUID docUUID = UUID.fromString(docUUIDAsString);
         executeGel(envList, environment, docUUID);
      }
      if (service.equals(DEGEL_DOCUMENT)) {
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le paramètre 'document_uuid' est obligatoire pour le service {}", service);
            return;
         }
         final String docUUIDAsString = args[2];
         final UUID docUUID = UUID.fromString(docUUIDAsString);
         executeDegel(envList, environment, docUUID);
      }

      String reqLucene = "";
      String workDirPath = "";

      // -- En cas d'import/export le dossier de travail doit être spécifié
      if (service.equals(IMPORT_DOCUMENTS) || service.equals(EXPORT_DOCUMENTS)) {
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le chemin du dossier de travail est obligatoire pour le service {}", service);
            return;
         }
         workDirPath = args[2];
      }

      // -- Le service nécessite qu'on spécifie une requete lucene
      if (service.equals(DELETE_DOCUMENTS)) {
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le paramètre 'requete lucène' est obligatoire pour le service {}", service);
            return;
         }
         reqLucene = args[2];
      } else if (service.equals(DELETE_DOCUMENTS_CORBEILLE)) {
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le paramètre 'requete lucène' est obligatoire pour le service {}", service);
            return;
         }
         reqLucene = args[2];
      }

      if (service.equals(EXPORT_DOCUMENTS)) {
         if (ValidationUtils.isArgumentsVide(args, 3)) {
            LOGGER.warn("Le paramètre 'requete lucene' est obligatoire pour le service {}", service);
            return;
         }
         reqLucene = args[3];
      }

      try {
         // -- Execution du service demandé
         executeService(envList, service, environment, workDirPath, reqLucene);

      }
      catch (final Throwable throwable) {
         final String erreur = "Une erreur s'est produite lors du traitement : " + service;
         LOGGER.error(erreur, throwable);
      }
   }

   private boolean ckeckPorpertyKey(final String kname, final Properties props) {
      if (!props.containsKey(kname)) {
         LOGGER.info("Le paramètere <{}> : est obligatoire", kname);
         return false;
      }
      return true;
   }

   /**
    * Vérication des proprétés et remplissage
    * de l'objet {@link ExportDocsParametres}
    * 
    * @param properties
    *          properties
    * @param parametres
    *          parametres
    * @return true si vérif OK
    */
   public boolean vefierParametresExecution(final Properties properties,
         final AbstractParametres parametres, final String service) {

      String param = null;

      if (service.equals(DELETE_DOCUMENTS) || service.equals(EXPORT_DOCUMENTS) || service.equals(DELETE_DOCUMENTS_CORBEILLE)) {
         // -- verifie la requête lucène
         param = "param.requete.lucene";
         if (ckeckPorpertyKey(param, properties)) {
            if (!ValidationUtils.verifParamReqLucene(properties, parametres, param)) {
               return false;
            }
         } else {
            return false;
         }
      }

      // -- verifie la taille du pool de thread (obligatoire)
      param = "param.taille.pool";
      if (ckeckPorpertyKey(param, properties)) {
         if (!ValidationUtils.verifParamTaillePool(properties, parametres, param)) {
            return false;
         }
      }

      // -- verifie la taille du pas d'execution (obligatoire)
      param = "param.taille.pas.execution";
      if (ckeckPorpertyKey(param, properties)) {
         if (!ValidationUtils.verifParamTaillePasExecution(properties, parametres, param)) {
            return false;
         }
      }

      // -- verifie la taille de la taille de la file d'attente (obligatoire)
      param = "param.taille.queue";
      if (ckeckPorpertyKey(param, properties)) {
         if (!ValidationUtils.verifParamTailleQueue(properties, parametres, param)) {
            return false;
         }
      }

      if (service.equals(IMPORT_DOCUMENTS) || service.equals(EXPORT_DOCUMENTS)) {
         // -- verifie le chemin vers le dossier de travail
         param = "param.chemin.dossier.travrail";
         if (ckeckPorpertyKey(param, properties)) {
            if (!ValidationUtils.verifParamDossierTravail(properties, parametres, param)) {
               return false;
            }
         }
      }

      // -- Temps d'attente en ms en cas de rejet d'ajout d'un traitement dans la pile
      param = "param.queue.sleep.time.ms";
      if (ckeckPorpertyKey(param, properties)) {
         if (!ValidationUtils.verifParamQueueSleepTime(properties, parametres, param)) {
            return false;
         }
      }

      return true;
   }

   /**
    * Convert a {@link ConfigurationEnvironnement} to a {@link Properties} object.
    * 
    * @param env
    *          : Environment configuration
    * @return Properties object mapping of env paramaters
    */
   public static Properties getDfceConfiguration(final ConfigurationEnvironnement env) {
      final Properties properties = new Properties();

      final String dfce_host = env.getDfceAddress().getHost();
      final String dfce_base = env.getDfceBaseName();
      final String dfce_port = String.valueOf(env.getDfceAddress().getPort());
      final String dfce_login = env.getDfceLogin();
      final String dfce_psswd = env.getDfcePwd();
      final String dfce_tmout = env.getDfceTimeout();
      final String dfce_ctxRoot = env.getDfceAddress().getPath();
      final String dfce_secure = env.getDfceSecure();

      properties.setProperty(DFCEConnectionParameter.DFCE_HOSTNAME, dfce_host);
      properties.setProperty(DFCEConnectionParameter.DFCE_BASE_NAME, dfce_base);
      properties.setProperty(DFCEConnectionParameter.DFCE_HOSTPORT, dfce_port);
      properties.setProperty(DFCEConnectionParameter.DFCE_LOGIN, dfce_login);
      properties.setProperty(DFCEConnectionParameter.DFCE_PASSWORD, dfce_psswd);
      properties.setProperty(DFCEConnectionParameter.DFCE_TIMEOUT, dfce_tmout);
      properties.setProperty(DFCEConnectionParameter.DFCE_CONTEXTROOT, dfce_ctxRoot);
      properties.setProperty(DFCEConnectionParameter.DFCE_SECURE, dfce_secure);

      return properties;
   }

   private boolean checkServiceEnvironment(final String service, final String env) {

      if (!ArrayUtils.contains(AVAIBLE_SERVICES, service)) {
         LOGGER.warn("Le service demande {} n'est pas valide", service);
         LOGGER.info("Services disponibles :\r{}", Arrays.toString(AVAIBLE_SERVICES));
         return false;
      }

      if (!envList.getListeNoms().contains(env)) {
         LOGGER.warn("L'environment demande {} n'est pas valide", env);
         LOGGER.info("Services disponibles :\r{}", envList.getListeNoms());
         return false;
      }

      if (service.equals(DELETE_DOCUMENTS) || service.equals(IMPORT_DOCUMENTS)
            || service.equals(GEL_DOCUMENT) || service.equals(DEGEL_DOCUMENT) || service.equals(DELETE_DOCUMENTS_CORBEILLE)) {
         if (!ArrayUtils.contains(DELETE_IMPORT_ALLOWED_ENVS, env)) {
            final String message = "ERREUR : Impossible de lancer le service " +
                  service + " sur l'environnement " + env;
            throw new RuntimeException(message);
         }
      }
      return true;
   }

   /**
    * Méthode de lancement des services
    * 
    * @param service
    *          nom du service
    * @param properties
    *          fichiers properties
    * @param context
    *          contexte spring
    */
   protected final void executeService(final ConfigurationsEnvironnement configEnv,
         final String service, final String environment,
         final String workDir, final String reqLucene) {

      final Properties properties = new Properties();
      ConfigurationEnvironnement destConfigEnv = null;

      Properties dfceProperties;
      destConfigEnv = configEnv.getConfiguration(environment);
      dfceProperties = getDfceConfiguration(destConfigEnv);

      // -- Connexion DFCE correspondante de l'environnement choisit
      final DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceProperties);

      // -- Configuration à utiliser pour l'exécution
      properties.put("param.taille.pool", "5");
      properties.put("param.taille.pas.execution", "10000");
      properties.put("param.taille.queue", "15");
      properties.put("param.queue.sleep.time.ms", "200");

      LOGGER.info("EXEC :  service = {}, environnement = {}", service, environment);

      if (service.equals(DELETE_DOCUMENTS)) {
         properties.put("param.requete.lucene", reqLucene);
         final DeleteDocsParametres parametres = new DeleteDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            // -- On execute le service
            executeDeleteDocuments(dfceConnection, parametres);
         }
      } else if (service.equals(EXPORT_DOCUMENTS)) {
         properties.put("param.requete.lucene", reqLucene);
         properties.put("param.chemin.dossier.travrail", workDir);
         final ExportDocsParametres parametres = new ExportDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            // -- Export des documents de la prod
            executeExportDocuments(dfceConnection, parametres);
         }
      } else if (service.equals(IMPORT_DOCUMENTS)) {
         properties.put("param.chemin.dossier.travrail", workDir);
         final ImportDocsParametres parametres = new ImportDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            // -- Import des documents dans un environnements
            executeImportDocuments(dfceConnection, parametres);
         }
      } else if (service.equals(DELETE_DOCUMENTS_CORBEILLE)) {
         properties.put("param.requete.lucene", reqLucene);
         final DeleteDocsParametres parametres = new DeleteDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            // -- On execute le service
            executeDeleteDocumentsCorbeille(dfceConnection, parametres);
         }
      }
   }

   private DfceService getDfceService(final ConfigurationsEnvironnement configEnv, final String environment) {
      final ConfigurationEnvironnement destConfigEnv = configEnv.getConfiguration(environment);
      final Properties dfceProperties = getDfceConfiguration(destConfigEnv);
      final DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceProperties);
      final DfceService dfceService = new DfceServiceImpl(dfceConnection);
      return dfceService;
   }

   private final void executeGel(final ConfigurationsEnvironnement configEnv, final String environment, final UUID docUUID) {

      final DfceService dfceService = getDfceService(configEnv, environment);
      final TraitementService traitementService = new TraitementServiceImpl(dfceService);
      traitementService.gelDocument(docUUID);
   }

   private final void executeDegel(final ConfigurationsEnvironnement configEnv, final String environment, final UUID docUUID) {

      final DfceService dfceService = getDfceService(configEnv, environment);
      final TraitementService traitementService = new TraitementServiceImpl(dfceService);
      traitementService.degelDocument(docUUID);
   }

   /**
    * Méthode permettant de lancer la suppression de documents
    * 
    * @param context
    *          contexte spring
    * @param parametres
    *          paramètres d'exécution de l'opération
    */
   private final void executeDeleteDocuments(final DFCEConnection dfceConnection,
         final DeleteDocsParametres parametres) {

      if (dfceConnection.getBaseName().equals("SAE-PROD")
            || dfceConnection.getBaseName().equals("GNT-PROD")) {
         final String message = "ERREUR : DELETE interdit en PRODUCTION !";
         throw new RuntimeException(message);
      }

      TraitementService traitementService;
      final DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      traitementService.deleteDocuments(parametres);
   }

   /**
    * Méthode permettant de lancer l'import de documents
    * 
    * @param context
    *          contexte spring
    * @param parametres
    *          paramètres d'exécution de l'opération
    */
   private final void executeImportDocuments(final DFCEConnection dfceConnection,
         final ImportDocsParametres parametres) {

      if (dfceConnection.getBaseName().equals("SAE-PROD")
            || dfceConnection.getBaseName().equals("GNT-PROD")) {
         final String message = "ERREUR : IMPORT interdit en PRODUCTION !";
         throw new RuntimeException(message);
      }

      TraitementService traitementService;
      final DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      parametres.setImportDir(parametres.getDossierTravail());
      traitementService.importDocuments(parametres);
   }

   public static boolean DEBUG_MODE = false;

   /**
    * Méthode permettant de lancer l'export de documents
    * 
    * @param context
    *          contexte spring
    * @param parametres
    *          paramètres d'exécution de l'opération
    */
   private final void executeExportDocuments(final DFCEConnection dfceConnection,
         final ExportDocsParametres parametres) {

      TraitementService traitementService;
      final DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      traitementService.exportDocuments(parametres);
   }

   /**
    * Méthode permettant de lancer la suppression de documents de la corbeille
    * 
    * @param context
    *          contexte spring
    * @param parametres
    *          paramètres d'exécution de l'opération
    */
   private void executeDeleteDocumentsCorbeille(final DFCEConnection dfceConnection, final DeleteDocsParametres parametres) {

      TraitementService traitementService;
      final DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      traitementService.deleteDocumentsCorbeille(parametres);
   }

   /**
    * Méthode utilitaire : transforme un InputStream en fichier (temportaire)
    * 
    * @param resource
    * @return Un objet {@link File} stocké dans les fichier temporaire du système
    * @throws IOException
    */
   private static File inputStreamToTempFile(final InputStream resource) throws IOException {
      OutputStream outputStream = null;
      File tmpFile = null;
      int read = 0;
      final byte[] bytes = new byte[1024];
      try {
         tmpFile = File.createTempFile("tmp_sae-docs-batch-exec_", ".xml");
         outputStream = new FileOutputStream(tmpFile);
         while ((read = resource.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
         }
      }
      finally {
         if (resource != null) {
            try {
               resource.close();
            }
            catch (final IOException e) {
               e.printStackTrace();
            }
         }
         if (outputStream != null) {
            try {
               outputStream.close();
            }
            catch (final IOException e) {
               e.printStackTrace();
            }
         }
      }
      return tmpFile;
   }
}
