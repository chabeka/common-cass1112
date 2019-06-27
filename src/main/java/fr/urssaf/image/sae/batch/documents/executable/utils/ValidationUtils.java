package fr.urssaf.image.sae.batch.documents.executable.utils;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.batch.documents.executable.model.AbstractParametres;

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
    * Méthode générique de vérification du paramètre requete lucene
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
   public static boolean verifParamReqLucene(final Properties properties, 
         final AbstractParametres parametres, final String paramName) {
      final String paramValue = properties.getProperty(paramName);

      if (isStringParam(paramName, paramValue)){
         parametres.setRequeteLucene(paramValue);
         return true;
      }
      else {
         return false;
      }
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
   public static boolean verifParamTailleQueue(final Properties properties, 
         final AbstractParametres parametres, final String paramName) {
      final String paramValue = properties.getProperty(paramName);

      if (isNumericParam(paramName, paramValue)){
         parametres.setTailleQueue(Integer.valueOf(paramValue));
         return true;
      }
      else {
         return false;
      }
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

      if (isNumericParam(paramName, paramValue)){
         parametres.setTaillePasExecution(Integer.valueOf(paramValue));
         return true;
      }
      else {
         return false;
      }
   }
   
   /**
    * Vérification du paramètre numérique "taille du pool"
    * 
    * @param properties
    *           properties
    * @param parametres
    *           parametres
    * @param paramName
    *           nom paramètre
    * @return boolean : false si validation réussie
    */
   public static boolean verifParamTaillePool(
         final Properties properties, final AbstractParametres parametres,
         final String paramName) {
      final String paramValue = properties.getProperty(paramName);
      if(isNumericParam(paramName, paramValue)){
         parametres.setTaillePool(Integer.valueOf(paramValue));
         return true;
      }
      else {
         System.out.println("dgdgdgdgdg: "+properties+ ":" + paramValue);
         return false;
      }
   }

   /**
    * Vérifier un paramètre numérique
    * 
    * @param paramName
    * @param paramValue
    * @param callback
    * @return
    */
   private static boolean isNumericParam(String paramName, String paramValue) {
      if (NumberUtils.isDigits(paramValue)) {
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
   private static boolean isStringParam(String paramName, String paramValue) {
      if (StringUtils.isNotBlank(paramValue)) {
         return true;
      } else {
         String mssg = "Le paramètre {} ne doit pas être vide.";
         LOGGER.warn(mssg, paramName);
         return false;
      }
   }

   /**
    * vérification du paramètre "dossier de travail" 
    * @param properties
    * @param parametres
    * @param param
    * @return
    */
   public static boolean verifParamDossierTravail(Properties properties,
         AbstractParametres parametres, String param) {
      final String paramValue = properties.getProperty(param);

      if (isStringParam(param, paramValue)){
         File path = new File(paramValue);
         if(path.isDirectory() && path.canWrite()){
            parametres.setDossierTravail(paramValue);
            return true;
         }
         String mssg = "Le paramètre {} doit correspondre au chemin d'un " +
         		"dossier acessible en écriture. Valeur fournie : '{}'";
         LOGGER.warn(mssg, param ,paramValue);
         return false;
      }
      else {
         return false;
      }
   }
   
   /**
    * Vérification param temps d'attente (en ms) en cas de 
    * rejet d'ajout d'un traitement dans la pile
    * 
    * @param properties
    * @param parametres
    * @param param
    * @return
    */
   public static boolean verifParamQueueSleepTime(Properties properties,
         AbstractParametres parametres, String param) {
      final String paramValue = properties.getProperty(param);
      if (NumberUtils.isDigits(paramValue)) {
         parametres.setQueueSleepTime(Integer.valueOf(paramValue));
         return true;
      } else {
         String mssg = "Le paramètre {} ne doit pas être vide et doit contenir uniquement des chiffres";
         LOGGER.warn(mssg, param);
         return false;
      }
   }
}
