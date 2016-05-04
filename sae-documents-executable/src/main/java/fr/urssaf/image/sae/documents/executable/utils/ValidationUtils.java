package fr.urssaf.image.sae.documents.executable.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.documents.executable.model.AbstractParametres;
import fr.urssaf.image.sae.documents.executable.model.AddMetadatasParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;

/**
 * Cette classe contient les methodes vérifier les arguments du programme main
 * et les paramètres obligatoires dans le fichier properties.
 */
public final class ValidationUtils {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ValidationUtils.class);

   /** Cette classe n'est pas faite pour être instanciée. */
   private ValidationUtils() {
      assert false;
   }

   /**
    * Methode permettant de vérifier si l'arguments testé est vide.
    * 
    * @param args
    *           tableau des arguments
    * @param index
    *           index de l'argument testé
    * @return boolean
    */
   public static boolean isArgumentsVide(final String[] args, final int index) {
      return ArrayUtils.getLength(args) <= index
            || StringUtils.isBlank(args[index]);
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre mode de
    * vérification.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamModeVerif(final Properties properties,
         final FormatValidationParametres parametres) {
      boolean erreurVerif = false;
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
      return erreurVerif;
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre requête lucène.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamRequeteLucene(final Properties properties,
         final AbstractParametres parametres) {
      boolean erreurVerif = false;
      String requeteLucene = properties.getProperty("format.requete.lucene");
      if (StringUtils.isBlank(requeteLucene)) {
         LOGGER.warn("Le paramètre {} ne doit pas être vide",
               "format.requete.lucene");
         erreurVerif = true;
      } else {
         LOGGER.info("Requête lucène : {}", requeteLucene);
         parametres.setRequeteLucene(requeteLucene);
      }
      return erreurVerif;
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre requête lucène pour
    * l'ajout de métadonnées.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifAddMetaParamRequeteLucene(
         final Properties properties, final AbstractParametres parametres) {

      final String paramName = "addMeta.requete.lucene";
      final String reqLucene = properties.getProperty(paramName);

      if (isStringParam(paramName, reqLucene, new onVerifiedCallback() {
         @Override
         public void onVerified() {
            parametres.setRequeteLucene(reqLucene);
         }
      }))
         return false;
      else
         return true;
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre taille du pool.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamTaillePool(final Properties properties,
         final AbstractParametres parametres) {
      boolean erreurVerif = false;
      String taillePool = properties.getProperty("format.taille.pool");
      if (NumberUtils.isDigits(taillePool)) {
         int valeurTaillePool = Integer.valueOf(taillePool);
         LOGGER.info("Taille du pool de thread : {}", valeurTaillePool);
         parametres.setTaillePool(valeurTaillePool);
      } else {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.taille.pool");
         erreurVerif = true;
      }
      return erreurVerif;
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre taille de la queue
    * en attente d'exécution.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamTailleQueue(final Properties properties,
         final AbstractParametres parametres) {
      boolean erreurVerif = false;
      String tailleQueue = properties.getProperty("format.taille.queue");
      if (NumberUtils.isDigits(tailleQueue)) {
         int valeurTailleQueue = Integer.valueOf(tailleQueue);
         LOGGER.info("Taille de la queue du pool de thread : {}",
               valeurTailleQueue);
         parametres.setTailleQueue(valeurTailleQueue);
      } else {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.taille.queue");
         erreurVerif = true;
      }
      return erreurVerif;
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre nombre maximum de
    * documents.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean : false si validation réussie
    */
   public static boolean verifParamNbMaxDocuments(final Properties properties,
         final FormatValidationParametres parametres) {
      String paramName = "format.nombre.max.documents";
      final String nombreMaxDocs = properties.getProperty(paramName);
      if (isNumericParam(paramName, nombreMaxDocs, new onVerifiedCallback() {
         @Override
         public void onVerified() {
            int nbMaxDoc = Integer.valueOf(nombreMaxDocs);
            parametres.setNombreMaxDocs(nbMaxDoc);
         }
      }))
         return false;
      else
         return true;
   }

   /**
    * Methode permettant de vérifier la saisie de la taille du pas d'exécution.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean : false si validation réussie
    */
   public static boolean verifParamTaillePasExecution(
         final Properties properties, final AbstractParametres parametres) {

      final String paramName = "format.taille.pas.execution";
      final String paramValue = properties.getProperty(paramName);

      if (isNumericParam(paramName, paramValue, new onVerifiedCallback() {
         @Override
         public void onVerified() {
            int value = Integer.valueOf(paramValue);
            parametres.setTaillePasExecution(value);
         }
      }))
         return false;
      else
         return true;
   }

   /**
    * Methode permettant de vérifier la saisie du paramètre
    * 'addMeta.taille.pas.execution'
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean : false si validation réussie
    */
   public static boolean verifAddMetaParamTaillePasExecution(
         final Properties properties, final AbstractParametres parametres) {
      final String paramName = "addMeta.taille.pas.execution";
      return verifParamTaillePasExecution(properties, parametres, paramName);
   }

   /**
    * Vérification de la taille du Pool de Theards
    * 
    * @param properties
    *           properties
    * @param parametres
    *           parametres
    * @return Renvoie vrai si taille OK
    */
   public static boolean verifAddMetaParamTaillePool(
         final Properties properties, final AbstractParametres parametres) {
      final String paramName = "addMeta.taille.pool";
      final String paramValue = properties.getProperty(paramName);

      if (isNumericParam(paramName, paramValue, new onVerifiedCallback() {
         @Override
         public void onVerified() {
            int value = Integer.valueOf(paramValue);
            parametres.setTaillePool(value);
         }
      }))
         return false;
      else
         return true;
   }

   /**
    * Vérification de la taille de la queue en attente d'exécution.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           parametres
    * @return Renvoie vrai si taille OK
    */
   public static boolean verifAddMetaParamTailleQueue(
         final Properties properties, final AbstractParametres parametres) {
      final String paramName = "addMeta.taille.queue";
      final String paramValue = properties.getProperty(paramName);

      if (isNumericParam(paramName, paramValue, new onVerifiedCallback() {
         @Override
         public void onVerified() {
            int value = Integer.valueOf(paramValue);
            parametres.setTailleQueue(value);
         }
      }))
         return false;
      else
         return true;
   }

   /**
    * Méthode générique de vérification du paramètre numérique taille du pas
    * d'execution
    * 
    * @param properties
    *           properties
    * @param parametres
    *           parametres
    * @param paramName
    *           nom paramètre
    * @return boolean : false si validation réussie
    */
   public static boolean verifParamTaillePasExecution(
         final Properties properties, final AbstractParametres parametres,
         final String paramName) {
      final String paramValue = properties.getProperty(paramName);

      if (isNumericParam(paramName, paramValue, new onVerifiedCallback() {
         @Override
         public void onVerified() {
            int value = Integer.valueOf(paramValue);
            parametres.setTaillePasExecution(value);
         }
      }))
         return false;
      else
         return true;
   }

   /**
    * Interface inline permettant la methode de verification.
    */
   private interface onVerifiedCallback {
      /**
       * Methode de verification.
       */
      void onVerified();
   }

   /**
    * Vérifier un paramètre numérique
    * 
    * @param paramName
    * @param paramValue
    * @param callback
    * @return
    */
   private static boolean isNumericParam(String paramName, String paramValue,
         onVerifiedCallback callback) {
      if (NumberUtils.isDigits(paramValue)) {
         LOGGER.info("Paramètere '{}' : {}", paramName, paramValue);
         callback.onVerified();
         return true;
      } else {
         String mssg = "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres";
         LOGGER.warn(mssg, paramName);
         return false;
      }
   }

   /**
    * Vérifier un paramètre de type string
    * 
    * @param paramName
    * @param paramValue
    * @param callback
    * @return
    */
   private static boolean isStringParam(String paramName, String paramValue,
         onVerifiedCallback callback) {
      if (StringUtils.isNotBlank(paramValue)) {
         LOGGER.info("Paramètere '{}' : {}", paramName, paramValue);
         callback.onVerified();
         return true;
      } else {
         String mssg = "Le paramètre {} ne doit pas être vide.";
         LOGGER.warn(mssg, paramName);
         return false;
      }
   }

   /**
    * Methode permettant d'initialiser le paramètre liste des métadonnées.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    */
   public static void initParamMetadonnees(final Properties properties,
         final FormatValidationParametres parametres) {
      String metadonnees = properties.getProperty("format.metadonnees");
      if (StringUtils.isNotBlank(metadonnees)) {
         parametres.setMetadonnees(Arrays.asList(metadonnees.split(",")));
      } else {
         parametres.setMetadonnees(new ArrayList<String>());
      }
      LOGGER.info("Métadonnées : {}", parametres.getMetadonnees());
   }

   /**
    * Methode permettant de vérifier la saisie de la taille du pas d'exécution.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifAddMetaParamListeMetadonnes(
         final Properties properties, final AddMetadatasParametres parametres) {

      boolean success = false;

      String parameter = "addMeta.metadonnees";
      String metadonnees = properties.getProperty(parameter);

      if (StringUtils.isBlank(metadonnees)) {
         success = true;
         LOGGER.warn("Le paramètre {} ne doit pas être vide", parameter);
      } else {
         if (!metadonnees.contains(":")) {
            success = true;
            String paramFormat = parameter + " 'cot:1,cpt:0,drh:0'";
            LOGGER.warn("Le format du paramètre {} est invalide. ", parameter);
            LOGGER.info("Exemple de format du paramètre {}.", paramFormat);
         } else {
            // -- Récupération de la liste des métadonnées
            Map<String, String> metasMap = new HashMap<String, String>();
            for (String meta : Arrays.asList(metadonnees.split(","))) {
               String[] metaData = meta.split(":");
               if (metaData.length == 2) {
                  metasMap.put(metaData[0], metaData[1]);
               } else if (metaData.length == 1) {
                  metasMap.put(metaData[0], null);
               }
            }
            parametres.setMetadonnees(metasMap);
         }
      }
      return success;
   }

   /**
    * Methode permettant d'initialiser le paramètre temps maximum de traitement.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    */
   public static void initParamTempsMaxTraitement(final Properties properties,
         final FormatValidationParametres parametres) {
      String tempsMaxTraitement = properties
            .getProperty("format.temps.max.traitement");
      if (NumberUtils.isDigits(tempsMaxTraitement)) {
         parametres.setTempsMaxTraitement(Integer.valueOf(tempsMaxTraitement));
      } else {
         parametres.setTempsMaxTraitement(0);
      }
      LOGGER.info("Temps maximum de traitement : {}", parametres
            .getTempsMaxTraitement());
   }

   /**
    * Methode permettant de vérifier la saisie du chemin du répertoire
    * temporaire.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamCheminRepertoireTemporaire(
         final Properties properties,
         final FormatValidationParametres parametres) {
      boolean erreurVerif = false;
      String cheminRepertoireTemporaire = properties
            .getProperty("format.chemin.repertoire.temporaire");
      if (StringUtils.isNotBlank(cheminRepertoireTemporaire)) {
         File chemin = new File(cheminRepertoireTemporaire);
         if (chemin.exists()) {
            parametres
                  .setCheminRepertoireTemporaire(cheminRepertoireTemporaire);
         } else {
            LOGGER
                  .warn(
                        "Le paramètre {} doit contenir le chemin d'un répertoire, et ce répertoire doit exister",
                        "format.chemin.repertoire.temporaire");
            erreurVerif = true;
         }
      }
      LOGGER.info("Chemin du répertoire temporaire : {}", parametres
            .getCheminRepertoireTemporaire());
      return erreurVerif;
   }
   
   /**
    * Methode permettant de vérifier la saisie du chemin du fichier csv.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifAddMetaParamCheminCSV(
         final Properties properties,
         final AddMetadatasParametres parametres) {
      boolean erreurVerif = false;
      String cheminFichierCSV = properties
            .getProperty("addMeta.chemin.fichier.csv");
      if (StringUtils.isNotBlank(cheminFichierCSV)) {
         File chemin = new File(cheminFichierCSV);
         if (chemin.exists()) {
            parametres
                  .setCheminFichier(cheminFichierCSV);
            LOGGER.info("Paramètere '{}' : {}", "addMeta.chemin.fichier.csv", cheminFichierCSV);
         } else {
            LOGGER
               .warn(
                  "Le paramètre {} doit contenir le chemin d'un fichier, et ce fichier doit exister",
                  "addMeta.chemin.fichier.csv");
            erreurVerif = true;
         }
      } else {
         LOGGER
            .warn(
               "Le paramètre {} doit contenir le chemin d'un fichier, et ce fichier doit exister",
               "addMeta.chemin.fichier.csv");
         erreurVerif = true;
      }
      return erreurVerif;
   }
}
