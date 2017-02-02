package fr.urssaf.image.sae.integration.ihmweb.utils;

import org.apache.commons.lang.StringUtils;


/**
 * Méthodes utilitaires pour les classes de transtypage
 */
public final class PropertyEditorUtils {

   private PropertyEditorUtils() {
      
   }
   
   
   /**
    * Eclate une chaîne de caractères sur le "retour charriot".<br>
    * La méthode associe "retour charriot" à "\r", "\n", "\r\n"
    * 
    * @param text le texte à éclater
    * @return le texte éclaté
    */
   public static String[] eclateSurRetourCharriot(String text) {
      String textWork = text.replace("\r\n", "\r");
      textWork = textWork.replace("\n", "\r");
      textWork = StringUtils.trim(textWork);
      return StringUtils.split(textWork, "\r");
   }
   
}
