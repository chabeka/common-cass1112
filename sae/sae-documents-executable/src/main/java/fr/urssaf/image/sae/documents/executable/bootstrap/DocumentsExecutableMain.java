package fr.urssaf.image.sae.documents.executable.bootstrap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;
import fr.urssaf.image.sae.documents.executable.service.TraitementService;

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

   public static final String[] AVAIBLE_SERVICES = new String[] { VERIFICATION_FORMAT };

   private final String configLocation;

   /**
    * Constructeur.
    * @param configLocation chemin de la configuration de l'exécutable
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
   protected void execute(String[] args) {

      if (ArrayUtils.getLength(args) <= 0 || StringUtils.isBlank(args[0])) {
         LOGGER.warn("Le service demandé doit être renseigné.");

         return;
      }

      String service = args[0];

      if (!ArrayUtils.contains(AVAIBLE_SERVICES, service)) {
         LOGGER.warn("Le service demande {} n'est pas valide", service);
         LOGGER.info("Services disponibles :\r{}", AVAIBLE_SERVICES);

         return;
      }

      if (ArrayUtils.getLength(args) <= 1 || StringUtils.isBlank(args[1])) {
         LOGGER
               .warn("Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");

         return;
      }

      if (ArrayUtils.getLength(args) <= 2 || StringUtils.isBlank(args[2])) {
         LOGGER
               .warn("Le chemin complet du fichier de paramètrage de vérification de format doit être renseigné.");

         return;
      }

      Properties properties = chargerFichierParam(args[2]);
      if (properties == null) {
         return;
      }

      FormatValidationParametres parametres = verifierConfFichierParam(properties);
      if (parametres == null) {
         return;
      }

      String saeConfiguration = args[1];
      ApplicationContext context = ContextFactory.createSAEApplicationContext(
            this.configLocation, saeConfiguration);
      
      executeService(service, context, parametres);
   }

   /**
    * Methode permettant de charger le fichier de paramètrage.
    * 
    * @param pathFichierParam
    *           chemin du fichier de paramètrage
    * @return Properties
    */
   private Properties chargerFichierParam(String pathFichierParam) {
      Properties properties = new Properties();
      InputStream stream = null;
      try {
         stream = new FileInputStream(pathFichierParam);
         properties.load(stream);
      } catch (FileNotFoundException e) {
         LOGGER
               .error(
                     "Le fichier de paramètrage de vérification de format n'a pas été trouvé : {}",
                     pathFichierParam);
         properties = null;
      } catch (IOException e) {
         LOGGER
               .error(
                     "Le fichier de paramètrage de vérification de format ne peut pas être lu : {}",
                     pathFichierParam);
         properties = null;
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOGGER
                     .error(
                           "Le fichier de paramètrage de vérification de format ne peut pas être fermé : {}",
                           pathFichierParam);
               properties = null;
            }
         }
      }
      return properties;
   }

   /**
    * Methode permettant de verifier la conf du fichier de parametrage.
    * 
    * @param properties
    *           properties du fichier de parametrage
    * @return <b>FormatValidationParametres</b> null s'il y a une erreur de
    *         configuration
    */
   protected FormatValidationParametres verifierConfFichierParam(
         Properties properties) {
      FormatValidationParametres parametres = new FormatValidationParametres();
      boolean erreurVerif = false;

      // verifie le mode de vérification (obligatoire)
      String modeVerif = properties.getProperty("format.mode.verification");
      if (MODE_VERIFICATION.IDENTIFICATION.name().equals(modeVerif)) {
         parametres.setModeVerification(MODE_VERIFICATION.IDENTIFICATION);
         LOGGER.info("Mode de vérification : {}", parametres
               .getModeVerification().name());
      } else if (MODE_VERIFICATION.VALIDATION.name().equals(modeVerif)) {
         parametres.setModeVerification(MODE_VERIFICATION.VALIDATION);
         LOGGER.info("Mode de vérification : {}", parametres
               .getModeVerification().name());
      } else if (MODE_VERIFICATION.IDENT_VALID.name().equals(modeVerif)) {
         parametres.setModeVerification(MODE_VERIFICATION.IDENT_VALID);
         LOGGER.info("Mode de vérification : {}", parametres
               .getModeVerification().name());
      } else {
         LOGGER.warn("Le paramètre {} doit être dans la liste suivante : {}",
               "format.mode.verification", MODE_VERIFICATION.values());
         erreurVerif = true;
      }

      // verifie la requête lucène (obligatoire)
      String requeteLucene = properties.getProperty("format.requete.lucene");
      if (StringUtils.isBlank(requeteLucene)) {
         LOGGER.warn("Le paramètre {} ne doit pas être vide",
               "format.requete.lucene");
         erreurVerif = true;
      } else {
         LOGGER.info("Requête lucène : {}", requeteLucene);
         parametres.setRequeteLucene(requeteLucene);
      }

      // verifie la taille du pool de thread (obligatoire)
      String taillePool = properties.getProperty("format.taille.pool");
      if (!NumberUtils.isDigits(taillePool)) {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.taille.pool");
         erreurVerif = true;
      } else {
         int valeurTaillePool = Integer.valueOf(taillePool);
         LOGGER.info("Taille du pool de thread : {}", valeurTaillePool);
         parametres.setTaillePool(valeurTaillePool);
      }

      // verifie le nombre maximum de documents (obligatoire)
      String nombreMaxDocs = properties
            .getProperty("format.nombre.max.documents");
      if (!NumberUtils.isDigits(nombreMaxDocs)) {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.nombre.max.documents");
         erreurVerif = true;
      } else {
         int nbMaxDoc = Integer.valueOf(nombreMaxDocs);
         LOGGER.info("Nombre maximum de documents : {}", nbMaxDoc);
         parametres.setNombreMaxDocs(nbMaxDoc);
      }

      // verifie la taille du pas d'execution (obligatoire)
      String taillePasExecution = properties
            .getProperty("format.taille.pas.execution");
      if (!NumberUtils.isDigits(taillePasExecution)) {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.taille.pas.execution");
         erreurVerif = true;
      } else {
         int valeurTaillePasExecution = Integer.valueOf(taillePasExecution);
         LOGGER
               .info("Taille du pas d'exécution : {}", valeurTaillePasExecution);
         parametres.setTaillePasExecution(valeurTaillePasExecution);
      }

      // verifie la liste des metadonnées (facultatif)
      String metadonnees = properties.getProperty("format.metadonnees");
      if (StringUtils.isNotBlank(metadonnees)) {
         parametres.setMetadonnees(Arrays.asList(metadonnees.split(",")));
      } else {
         parametres.setMetadonnees(new ArrayList<String>());
      }
      LOGGER.info("Métadonnées : {}", parametres.getMetadonnees());

      // verifie le temps maximum de traitement (facultatif, valeur 0 par
      // defaut)
      String tempsMaxTraitement = properties
            .getProperty("format.temps.max.traitement");
      if (NumberUtils.isDigits(tempsMaxTraitement)) {
         parametres.setTempsMaxTraitement(Integer.valueOf(tempsMaxTraitement));
      } else {
         parametres.setTempsMaxTraitement(0);
      }
      LOGGER.info("Temps maximum de traitement : {}", parametres
            .getTempsMaxTraitement());

      // verifie si on a eu une erreur de verification
      // dans ce cas, on met l'objet parametre a null
      if (erreurVerif) {
         parametres = null;
      }

      return parametres;
   }
   
   /**
    * Methode permettant de rendre évolutif cet exécutable (ajout d'eventuel service supplémentaire).
    * @param service nom du service
    * @param context contexte spring
    * @param parametres parametres du fichier de paramètrage
    */
   protected void executeService(String service, ApplicationContext context, FormatValidationParametres parametres) {
      
      if (VERIFICATION_FORMAT.equals(service)) {
         verifierFormat(context, parametres);
      } 
   }
   
   /**
    * Methode permettant de lancer l'identification et/ou la validation sur des fichiers.
    * @param context contexte spring
    * @param parametres parametres du fichier de paramètrage
    */
   protected void verifierFormat(ApplicationContext context, FormatValidationParametres parametres) {
      
      try {
         TraitementService traitementService = context.getBean(TraitementService.class);
         traitementService.identifierValiderFichiers(parametres);
      } catch (RuntimeException e) {
         LOGGER.error("Une erreur s'est produite lors de l'exécution du traitement : {}", e.getMessage());
         throw e;
      } finally {
         ((ClassPathXmlApplicationContext) context).close();
      }
      
   }
}
