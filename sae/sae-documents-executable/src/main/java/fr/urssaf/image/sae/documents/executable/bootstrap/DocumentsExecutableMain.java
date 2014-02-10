package fr.urssaf.image.sae.documents.executable.bootstrap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.service.TraitementService;
import fr.urssaf.image.sae.documents.executable.utils.ValidationUtils;

/**
 * Classe de lancement des traitements.
 */
public class DocumentsExecutableMain {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocumentsExecutableMain.class);

   public static final String VERIFICATION_FORMAT = "VERIFICATION_FORMAT";

   protected static final String[] AVAIBLE_SERVICES = new String[] { VERIFICATION_FORMAT };

   private final String configLocation;

   /**
    * Constructeur.
    * 
    * @param configLocation
    *           chemin de la configuration de l'exécutable
    */
   protected DocumentsExecutableMain(String configLocation) {
      this.configLocation = configLocation;
   }

   /**
    * Méthode appelée lors du lancement.
    * 
    * @param args
    *           arguments passés en paramètres
    */
   public static void main(String[] args) {

      LOGGER.info("Arguments de la ligne de commande : {}", StringUtils.join(
            args, ' '));

      DocumentsExecutableMain documentsExecutableMain = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable.xml");
      documentsExecutableMain.execute(args);
   }

   /**
    * Methode permettant d'exécuter le traitement.
    * 
    * @param args
    *           arguments passés en paramètres
    */
   protected final void execute(final String[] args) {

      if (ValidationUtils.isArgumentsVide(args, 0)) {
         LOGGER.warn("Le service demandé doit être renseigné.");

         return;
      }

      String service = args[0];

      if (!ArrayUtils.contains(AVAIBLE_SERVICES, service)) {
         LOGGER.warn("Le service demande {} n'est pas valide", service);
         LOGGER.info("Services disponibles :\r{}", AVAIBLE_SERVICES);

         return;
      }

      if (ValidationUtils.isArgumentsVide(args, 1)) {
         LOGGER
               .warn("Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");

         return;
      }

      if (ValidationUtils.isArgumentsVide(args, 2)) {
         LOGGER
               .warn("Le chemin complet du fichier de paramètrage de vérification de format doit être renseigné.");

         return;
      }

      Properties properties = new Properties();
      if (!chargerFichierParam(args[2], properties)) {
         return;
      }

      FormatValidationParametres parametres = new FormatValidationParametres();
      if (!verifierConfFichierParam(properties, parametres)) {
         return;
      }

      String saeConfiguration = args[1];
      ApplicationContext context = ContextFactory.createSAEApplicationContext(
            this.configLocation, saeConfiguration);

      try {
         executeService(service, context, parametres);
      } catch (Throwable throwable) { // NOPMD Par Cédric le 10/02/2014 11:30 :
                                      // On veut récupérer tous les types
                                      // d'exceptions
         LOGGER.error(
               "Une erreur s'est produite lors du traitement " + service,
               throwable);
      } finally {
         ((ClassPathXmlApplicationContext) context).close();
      }
   }

   /**
    * Methode permettant de charger le fichier de paramètrage.
    * 
    * @param pathFichierParam
    *           chemin du fichier de paramètrage
    * @return Properties
    */
   private boolean chargerFichierParam(final String pathFichierParam,
         final Properties properties) {
      boolean chargementOk = true;
      InputStream stream = null;
      try {
         stream = new FileInputStream(pathFichierParam);
         properties.load(stream);
      } catch (FileNotFoundException e) {
         LOGGER
               .error(
                     "Le fichier de paramètrage de vérification de format n'a pas été trouvé : {}",
                     pathFichierParam);
         chargementOk = false;
      } catch (IOException e) {
         LOGGER
               .error(
                     "Le fichier de paramètrage de vérification de format ne peut pas être lu : {}",
                     pathFichierParam);
         chargementOk = false;
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOGGER
                     .error(
                           "Le fichier de paramètrage de vérification de format ne peut pas être fermé : {}",
                           pathFichierParam);
               chargementOk = false;
            }
         }
      }
      return chargementOk;
   }

   /**
    * Methode permettant de verifier la conf du fichier de parametrage.
    * 
    * @param properties
    *           properties du fichier de parametrage
    * @param parametres
    *           parametres
    * @return boolean
    */
   protected final boolean verifierConfFichierParam(
         final Properties properties,
         final FormatValidationParametres parametres) {
      boolean confOk = true;

      // verifie le mode de vérification (obligatoire)
      if (ValidationUtils.verifParamModeVerif(properties, parametres)) {
         confOk = false;
      }

      // verifie la requête lucène (obligatoire)
      if (ValidationUtils.verifParamRequeteLucene(properties, parametres)) {
         confOk = false;
      }

      // verifie la taille du pool de thread (obligatoire)
      if (ValidationUtils.verifParamTaillePool(properties, parametres)) {
         confOk = false;
      }

      // verifie le nombre maximum de documents (obligatoire)
      if (ValidationUtils.verifParamNbMaxDocuments(properties, parametres)) {
         confOk = false;
      }

      // verifie la taille du pas d'execution (obligatoire)
      if (ValidationUtils.verifParamTaillePasExecution(properties, parametres)) {
         confOk = false;
      }

      // verifie la liste des metadonnées (facultatif)
      ValidationUtils.initParamMetadonnees(properties, parametres);

      // verifie le temps maximum de traitement (facultatif, valeur 0 par
      // defaut)
      ValidationUtils.initParamTempsMaxTraitement(properties, parametres);

      // verifie le chemin du répertoire temporaire (facultatif)
      if (ValidationUtils.verifParamCheminRepertoireTemporaire(properties,
            parametres)) {
         confOk = false;
      }
      return confOk;
   }

   /**
    * Methode permettant de rendre évolutif cet exécutable (ajout d'eventuel
    * service supplémentaire).
    * 
    * @param service
    *           nom du service
    * @param context
    *           contexte spring
    * @param parametres
    *           parametres du fichier de paramètrage
    */
   protected final void executeService(final String service,
         final ApplicationContext context, FormatValidationParametres parametres) {

      if (VERIFICATION_FORMAT.equals(service)) {
         verifierFormat(context, parametres);
      }
   }

   /**
    * Methode permettant de lancer l'identification et/ou la validation sur des
    * fichiers.
    * 
    * @param context
    *           contexte spring
    * @param parametres
    *           parametres du fichier de paramètrage
    */
   protected final void verifierFormat(final ApplicationContext context,
         final FormatValidationParametres parametres) {
      TraitementService traitementService = context
            .getBean(TraitementService.class);
      traitementService.identifierValiderFichiers(parametres);
   }
}
