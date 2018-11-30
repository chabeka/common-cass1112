package fr.urssaf.image.sae.integration.ihmweb.utils;

import org.apache.commons.lang.StringUtils;


/**
 * Des méthodes utilitaires divers 
 */
public final class TestUtils {

   private TestUtils() {
      
   }
   
   
   /**
    * Concaténation de deux messages d'erreurs, avec ajout d'un retour charriot
    * entre les deux. Gère les cas de messages vides ou null.
    * 
    * @param msg1 le premier message d'erreur
    * @param msg2 le deuxième message d'erreur
    * @return la concaténation des deux messages d'erreur
    */
   public static String concatMessagesErreurs(String msg1, String msg2) {
      
      StringBuffer strBuffer = new StringBuffer();
      
      if (StringUtils.isNotBlank(msg1)) {
         strBuffer.append(msg1);
      }
      
      if (StringUtils.isNotBlank(msg2)) {
         if (strBuffer.length()>0) {
            strBuffer.append("\r\n");
         }
         strBuffer.append(msg2);
      }
      
      return strBuffer.toString();

   }
   
   
   
}
