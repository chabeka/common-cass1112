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
   
   /**
    * Convertit les métadonnées d'un PRMD en une chaîne de caractères pour l'écriture d'une trace
    * @param prmdMetas les métadonnées d'un PRMD
    * @return la chaîne de caractères pour la trace
    */
   public static String prmdMetaToString(Map<String, List<String>> prmdMetas) {
      
      if (prmdMetas.isEmpty()) {

         return "vide";

      } else {

         StringBuffer sBuffer = new StringBuffer();

         String codeMeta;
         List<String> valeurs;
         for (Map.Entry<String, List<String>> entry : prmdMetas.entrySet()) {
            
            codeMeta = entry.getKey();
            valeurs = entry.getValue();
            
            sBuffer.append(codeMeta);
            sBuffer.append("=[");
            for (String valeur: valeurs) {
               sBuffer.append(valeur);
               sBuffer.append(';');
            }
            if (valeurs.size()>0) {
               sBuffer.deleteCharAt(sBuffer.length()-1);
            }
            
            sBuffer.append(']');
            
            sBuffer.append(';');
            
         }
         
         if (prmdMetas.size()>0) {
            sBuffer.deleteCharAt(sBuffer.length()-1);
         }

         return sBuffer.toString();

      }
      
   }
   
}
