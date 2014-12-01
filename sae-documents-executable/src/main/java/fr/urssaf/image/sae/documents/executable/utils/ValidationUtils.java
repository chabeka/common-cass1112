package fr.urssaf.image.sae.documents.executable.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
         final FormatValidationParametres parametres) {
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
    * Methode permettant de vérifier la saisie du paramètre taille du pool.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamTaillePool(final Properties properties,
         final FormatValidationParametres parametres) {
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
    * Methode permettant de vérifier la saisie du paramètre nombre maximum de
    * documents.
    * 
    * @param properties
    *           properties
    * @param parametres
    *           paramètres
    * @return boolean
    */
   public static boolean verifParamNbMaxDocuments(final Properties properties,
         final FormatValidationParametres parametres) {
      boolean erreurVerif = false;
      String nombreMaxDocs = properties
            .getProperty("format.nombre.max.documents");
      if (NumberUtils.isDigits(nombreMaxDocs)) {
         int nbMaxDoc = Integer.valueOf(nombreMaxDocs);
         LOGGER.info("Nombre maximum de documents : {}", nbMaxDoc);
         parametres.setNombreMaxDocs(nbMaxDoc);
      } else {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.nombre.max.documents");
         erreurVerif = true;
      }
      return erreurVerif;
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
   public static boolean verifParamTaillePasExecution(
         final Properties properties,
         final FormatValidationParametres parametres) {
      boolean erreurVerif = false;
      String taillePasExecution = properties
            .getProperty("format.taille.pas.execution");
      if (NumberUtils.isDigits(taillePasExecution)) {
         int valeurTaillePasExecution = Integer.valueOf(taillePasExecution);
         LOGGER
               .info("Taille du pas d'exécution : {}", valeurTaillePasExecution);
         parametres.setTaillePasExecution(valeurTaillePasExecution);
      } else {
         LOGGER
               .warn(
                     "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres",
                     "format.taille.pas.execution");
         erreurVerif = true;
      }
      return erreurVerif;
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
}
