/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.service.utils;

import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.document.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * classe contenant les traces permettant de connaitre les metadonnees des
 * documents
 * 
 */
public final class TraceDatasUtils {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceDatasUtils.class);

   private TraceDatasUtils(){
   }
   
   /**
    * Loggue les valeurs anciennes et nouvelles des métadonnées à modifier,
    * ainsi que la concordance avec la valeur de la métadonnée du documentt
    * 
    * @param documents
    *           les documents à modifier
    * @param metadonnees
    *           les métadonnées et leur nouvelle valeur
    * @param oldMetadonnees
    *           les métadonnées et leur ancienne valeur
    * @param lineNumber
    *           le numéro de la ligne
    */
   public static void traceMetas(List<Document> documents,
         Map<String, Object> metadonnees, Map<String, Object> oldMetadonnees,
         int lineNumber) {
      String type;
      for (Document document : documents) {
         for (String key : metadonnees.keySet()) {
            if (document.getSingleCriterion(key) != null) {
               if (document.getSingleCriterion(key).getWord().equals(
                     metadonnees.get(key))) {
                  type = "NVL_VALEUR";
               } else if (document.getSingleCriterion(key).getWord().equals(
                     oldMetadonnees.get(key))) {
                  type = "ANC_VALEUR";
               } else {
                  type = "AUT_VALUE";
               }
               LOGGER.trace(lineNumber + ";" + document.getFileUUID() + ";"
                     + key + ";" + document.getSingleCriterion(key).getWord()
                     + ";" + oldMetadonnees.get(key) + ";"
                     + metadonnees.get(key) + ";" + type);
            }

         }
      }
   }

}
