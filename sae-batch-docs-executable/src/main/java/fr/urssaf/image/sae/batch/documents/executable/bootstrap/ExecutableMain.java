package fr.urssaf.image.sae.batch.documents.executable.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;

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
   private static final Logger LOGGER = 
      LoggerFactory.getLogger(ExecutableMain.class);
   
   /**
    * Nom des services disponibles
    */
   public static final String IMPORT_DOCUMENTS = "IMPORT_DOCUMENTS";
   public static final String EXPORT_DOCUMENTS = "EXPORT_DOCUMENTS";
   public static final String DELETE_DOCUMENTS = "DELETE_DOCUMENTS";
   
   /**
    * Nom des environnements
    */
   public static final String INTEGRATION_COMMUNE_GNS    = "INTEGRATION_COMMUNE_GNS";
   public static final String INTEGRATION_COMMUNE_GNT    = "INTEGRATION_COMMUNE_GNT";
   public static final String INTEGRATION_NATIONALE_GNS  = "INTEGRATION_NATIONALE_GNS";
   public static final String INTEGRATION_NATIONALE_GNT  = "INTEGRATION_NATIONALE_GNT";
   public static final String VALIDATION_NATIONALE_GNS   = "VALIDATION_NATIONALE_GNS";
   public static final String VALIDATION_NATIONALE_GNT   = "VALIDATION_NATIONALE_GNT";
   public static final String FORMATION_GNS   = "FORMATION_GNS";
   public static final String FORMATION_GNT   = "FORMATION_GNT";
   public static final String INTEGRATION_INTERNE_GNT    = "INTEGRATION_INTERNE_GNT";
   public static final String INTEGRATION_INTERNE_GNS    = "INTEGRATION_INTERNE_GNS";

   private static final String[] AVAIBLE_SERVICES = new String[] {
      IMPORT_DOCUMENTS, EXPORT_DOCUMENTS, DELETE_DOCUMENTS 
   };
   
   private String[] DELETE_IMPORT_ALLOWED_ENVS = {
      "ENV_DEVELOPPEMENT",
      INTEGRATION_COMMUNE_GNT, INTEGRATION_COMMUNE_GNS, 
      INTEGRATION_NATIONALE_GNT, INTEGRATION_NATIONALE_GNS,
      VALIDATION_NATIONALE_GNT, VALIDATION_NATIONALE_GNS,
      FORMATION_GNS, FORMATION_GNT
   };
   
   /**
    *  Liste liste des envirennements disponibles
    */
   ConfigurationsEnvironnement envList;  
   
   /**
    * Constructeur.
    * 
    * @param ctxPath
    *           chemin de la configuration de l'exécutable
    *           
    * @param fileConf
    *           fichier de configuration des environnements
    */
   protected ExecutableMain(File fileConf) {
      
      ConfigurationServiceImpl configSce;
      configSce = new ConfigurationServiceImpl();
      
      try {
         envList = configSce.chargerConfiguration(fileConf);
      } catch (NullPointerException e) {
         throw new RuntimeException("Le chemin du fichier ne peut être null", e);
      } catch (IOException e) {
         throw new RuntimeException("Echec du chargements des environnements", e);
      }
   }

   /**
    * Méthode appelée lors du lancement.
    * 
    * @param args
    *           arguments passés en paramètres
    */
   public static void main(String[] args) {
      LOGGER.info("Arguments CMD : [{}]", StringUtils.join(args, ", "));
      final String environments = "config/environnements.xml";
      InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(environments);
      try {
         File conf = inputStreamToTempFile(resource);
         ExecutableMain main = new ExecutableMain(conf);
         main.execute(args);
      } catch (IOException e) {
         LOGGER.error("Echec chargement fichier de configuration des environnements");
      }
   }

   /**
    * Methode permettant d'exécuter le traitement.
    * 
    * @param args
    *           arguments passés en paramètres
    * @throws FileNotFoundException 
    */
   protected final void execute(final String[] args){

      if (ValidationUtils.isArgumentsVide(args, 0)) {
         LOGGER.warn("Le service demandé doit être renseigné.");
         return;
      }
      if (ValidationUtils.isArgumentsVide(args, 1)) {
         LOGGER.warn("L'environnement cible être renseigné.");
         return;
      }

      final String service       = args[0];
      final String environment   = args[1];
      
      //-- Controle la compatibilité de de service demandé avec l'env cible
      if(!checkServiceEnvironment(service, environment)){
         return;
      }
      
      String reqLucene     = "";
      String workDirPath   = "";
      
      //-- En cas d'import/export le dossier de travail doit êtr spécifié
      if(service.equals(IMPORT_DOCUMENTS) || service.equals(EXPORT_DOCUMENTS)){
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le chemin du dossier de travail est obligatoire pour le service {}", service);
            return;
         }
         workDirPath = args[2];
      }

      //-- Le service nécessite qu'on spécifie une requete lucene
      if(service.equals(DELETE_DOCUMENTS)){
         if (ValidationUtils.isArgumentsVide(args, 2)) {
            LOGGER.warn("Le paramètre 'requete lucène' est obligatoire pour le service {}", service);
            return;
         }
         reqLucene = args[2];
      }
      if(service.equals(EXPORT_DOCUMENTS)){
         if (ValidationUtils.isArgumentsVide(args, 3)) {
            LOGGER.warn("Le paramètre 'requete lucene' est obligatoire pour le service {}", service);
            return;
         }
         reqLucene = args[3];
      }
      
      try { 
         // -- Execution du service demandé
         executeService(envList, service, environment, workDirPath, reqLucene);
         
      } catch (Throwable throwable) {
         String erreur = "Une erreur s'est produite lors du traitement : " + service;
         LOGGER.error(erreur, throwable);
      }
   }

   private boolean ckeckPorpertyKey(String kname, Properties props){
      if(!props.containsKey(kname)){
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
    *           properties
    * @param parametres
    *           parametres
    * @return true si vérif OK
    */
   public boolean vefierParametresExecution(final Properties properties,
         final AbstractParametres parametres, final String service) {
      
      String param = null;
      
      if(service.equals(DELETE_DOCUMENTS) || service.equals(EXPORT_DOCUMENTS)){
         //-- verifie la requête lucène 
         param = "param.requete.lucene";
         if(ckeckPorpertyKey(param, properties)){
            if (!ValidationUtils.verifParamReqLucene(properties, parametres, param)){
               return false;
            }
         } else {
            return false;
         }
      }
      
      //-- verifie la taille du pool de thread (obligatoire)
      param = "param.taille.pool";
      if(ckeckPorpertyKey(param, properties)){
         if (!ValidationUtils.verifParamTaillePool(properties, parametres, param)){
            return false;
         }
      }

      //-- verifie la taille du pas d'execution (obligatoire)
      param = "param.taille.pas.execution";
      if(ckeckPorpertyKey(param, properties)){
         if (!ValidationUtils.verifParamTaillePasExecution(properties, parametres, param)){
            return false;
         }
      }
         
      //-- verifie la taille de la taille de la file d'attente (obligatoire)
      param = "param.taille.queue";
      if(ckeckPorpertyKey(param, properties)){
         if (!ValidationUtils.verifParamTailleQueue(properties, parametres, param)){
            return false;
         }
      }
      
      if(service.equals(IMPORT_DOCUMENTS) || service.equals(EXPORT_DOCUMENTS)){
         //-- verifie le chemin vers le dossier de travail
         param = "param.chemin.dossier.travrail";
         if(ckeckPorpertyKey(param, properties)){
            if (!ValidationUtils.verifParamDossierTravail(properties, parametres, param)){
               return false;
            }
         }
      }
      
      //-- Temps d'attente en ms en cas de rejet d'ajout d'un traitement dans la pile
      param = "param.queue.sleep.time.ms";
      if(ckeckPorpertyKey(param, properties)){
         if (!ValidationUtils.verifParamQueueSleepTime(properties, parametres, param)){
            return false;
         }
      }
      
      return true;
   }

   
   /**
    * Convert a {@link ConfigurationEnvironnement} to a {@link Properties} object.
    * 
    * @param env : Environment configuration
    * 
    * @return Properties object mapping of env paramaters
    */
   public static Properties getDfceConfiguration(ConfigurationEnvironnement env){
      Properties properties = new Properties();
      
      String dfce_host  = env.getDfceAddress().getHost();
      String dfce_base  = env.getDfceBaseName();
      String dfce_port  = String.valueOf(env.getDfceAddress().getPort());
      String dfce_login = env.getDfceLogin();
      String dfce_psswd = env.getDfcePwd();
      String dfce_tmout = env.getDfceTimeout();
      String dfce_ctxRoot = env.getDfceAddress().getPath();
      String dfce_secure = env.getDfceSecure();
      
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
   
   private boolean checkServiceEnvironment(String service, String env){
      
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
      
      String message = "ERREUR : Impossible de lancer le service " +
                        service +" sur l'environnement " + env;
      
      if(service.equals(DELETE_DOCUMENTS) || service.equals(IMPORT_DOCUMENTS)){
         if(!ArrayUtils.contains(DELETE_IMPORT_ALLOWED_ENVS, env)){
            throw new RuntimeException(message);
         }
      }
      return true;
   }
   
   
   /**
    * Methode de lancement des services
    * 
    * @param service
    *           nom du service
    * @param properties
    *           fichiers properties
    * @param context
    *           contexte spring
    */
   protected final void executeService(ConfigurationsEnvironnement configEnv, 
         final String service, final String environment, 
         final String workDir, final String reqLucene) {
      
      Properties properties = new Properties();
      ConfigurationEnvironnement destConfigEnv = null;
      
      if(!configEnv.existe(environment)){
         throw new RuntimeException("L'environnement demandé: "+environment+" n'existe pas.");
      }
      if(!checkServiceEnvironment(service, environment)){
         LOGGER.info("ERREUR : Echec de la vérification des enfironnements");
         return;
      }
      
      Properties dfceProperties;
      destConfigEnv = configEnv.getConfiguration(environment);
      dfceProperties = getDfceConfiguration(destConfigEnv);
      
      //-- Connexion DFCE correspondante de l'environnement choisit
      DFCEConnection dfceConnection = DFCEConnectionFactory
         .createDFCEConnectionByDFCEConfiguration(dfceProperties);
      
      //-- Configuration à utiliser pour l'exécution
      properties.put("param.taille.pool", "5");
      properties.put("param.taille.pas.execution", "10000");
      properties.put("param.taille.queue", "15");
      properties.put("param.queue.sleep.time.ms", "200");
      
      LOGGER.info("EXEC :  service = {}, environnement = {}", service, environment);
      
      if(service.equals(DELETE_DOCUMENTS)){
         properties.put("param.requete.lucene", reqLucene);
         DeleteDocsParametres parametres = new DeleteDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            //-- On execute le service
            executeDeleteDocuments(dfceConnection, parametres);
         }
      } else if(service.equals(EXPORT_DOCUMENTS)){
         properties.put("param.requete.lucene", reqLucene);
         properties.put("param.chemin.dossier.travrail", workDir);
         ExportDocsParametres parametres = new ExportDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            //-- Export des documents de la prod
            executeExportDocuments(dfceConnection, parametres);
         }
      } else if(service.equals(IMPORT_DOCUMENTS)){
         properties.put("param.chemin.dossier.travrail", workDir);
         ImportDocsParametres parametres = new ImportDocsParametres();
         if (vefierParametresExecution(properties, parametres, service)) {
            //-- Import des documents dans un environnements
            executeImportDocuments(dfceConnection, parametres);
         }
      }
   }

   /**
    * Methode permettant de lancer la suppression de documents
    * 
    * @param context
    *           contexte spring
    * @param parametres
    *           parametres d'exécution de l'opération
    */
   private final void executeDeleteDocuments(DFCEConnection dfceConnection,
         final DeleteDocsParametres parametres) {
      
      if(dfceConnection.getBaseName().equals("SAE-PROD")
            || dfceConnection.getBaseName().equals("GNT-PROD")) {
         String message = "ERREUR : DELETE interdit en PRODUCTION !";
         throw new RuntimeException(message);
      }
      
      TraitementService traitementService;
      DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      traitementService.deleteDocuments(parametres);
   }

   /**
    * Methode permettant de lancer l'import de documents
    * 
    * @param context
    *           contexte spring
    * @param parametres
    *           parametres d'exécution de l'opération
    */
   private final void executeImportDocuments(final DFCEConnection dfceConnection,
         final ImportDocsParametres parametres) {
      
      if(dfceConnection.getBaseName().equals("SAE-PROD")
            || dfceConnection.getBaseName().equals("GNT-PROD")) {
         String message = "ERREUR : IMPORT interdit en PRODUCTION !";
         throw new RuntimeException(message);
      }
      
      TraitementService traitementService;
      DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      parametres.setImportDir(parametres.getDossierTravail());
      traitementService.importDocuments(parametres);
   }   
   
   public static boolean DEBUG_MODE = false;
   
   /**
    * Methode permettant de lancer l'export de documents
    * 
    * @param context
    *           contexte spring
    * @param parametres
    *           parametres d'exécution de l'opération
    */
   private final void executeExportDocuments(final DFCEConnection dfceConnection,
         final ExportDocsParametres parametres) {
      
      TraitementService traitementService;
      DfceService dfceService = new DfceServiceImpl(dfceConnection);
      traitementService = new TraitementServiceImpl(dfceService);
      traitementService.exportDocuments(parametres);
   }   
   
   /**
    * Méthode utilitaire : transforme un InputStream en fichier (temportaire)
    * @param resource
    * 
    * @return Un objet {@link File} stocké dans les fichier temporaire du système
    * 
    * @throws IOException
    */
   private static File inputStreamToTempFile(InputStream resource) throws IOException{
      OutputStream outputStream = null;
      File tmpFile = null;
      int read = 0;
      byte[] bytes = new byte[1024]; 
      try {
         tmpFile = File.createTempFile("tmp_sae-docs-batch-exec_", ".xml");
         outputStream = new FileOutputStream(tmpFile);
         while ((read = resource.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
         }
      } finally {
         if (resource != null) {
            try {
               resource.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (outputStream != null) {
            try {
               outputStream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return tmpFile;
   }
}
