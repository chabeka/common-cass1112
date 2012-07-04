package fr.urssaf.image.sae.integration.droits.utils;

import java.util.List;
import java.util.Map;

/**
 * Méthodes utilitaires pour les traces
 */
public final class LogUtils {

   
   private LogUtils() {
      
   }
   
   /**
    * Convertit une liste de String en un String pour la sortie dans une trace
    * @param list la liste
    * @return la String pour la trace
    */
   public static String listeToString(List<String> list) {
      
      StringBuffer sBuffer = new StringBuffer(); 
      
      for (String s: list) {
         sBuffer.append(s);
         sBuffer.append(';');
      }
      
      return sBuffer.toString();
      
   }

   /**
    * Convertit une map de String,String en String pour la sortie dans une trace
    * @param laMap la map à convertir
    * @return la String pour la trace
    */
   public static String mapToString(Map<String, String> laMap) {

      if (laMap.isEmpty()) {

         return "vide";

      } else {

         StringBuffer sBuffer = new StringBuffer();

         for (Map.Entry<String, String> entry : laMap.entrySet()) {
            sBuffer.append(entry.getKey());
            sBuffer.append('=');
            sBuffer.append(entry.getValue());
            sBuffer.append(';');
         }

         return sBuffer.toString();

      }

   }
   
}
