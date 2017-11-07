package fr.urssaf.image.sae.commons.utils;

import java.lang.reflect.Field;

public class SearchObjectClassUtil {

   /**
    * Methode utilitaire permettant de retrouver une classe dans un object à
    * analyser. Cette utilitaire ne gére pas les type primitifs.
    * 
    * @param objAnalyse
    *           Object à analyser
    * @param klazzSearch
    *           Classe recherchée
    * @return Objet trouvé
    */
   public static Object searchObjectByClass(Object objAnalyse, Class klazzSearch) {
      String lofPrefix = "searchObjectByClass - ";
      Object objFind = null;
      if (objAnalyse == null || klazzSearch == null) {
         System.out.println(lofPrefix
               + "'obj' ou 'klazzSearch' ne peut être null");
      } else {
         Class klazz = objAnalyse.getClass();
         if (!(klazz.isPrimitive() || objAnalyse instanceof String
               || objAnalyse instanceof Integer 
               || objAnalyse instanceof Double
               || objAnalyse instanceof Long
               || objAnalyse instanceof Float
               || objAnalyse instanceof Boolean
               || objAnalyse instanceof Byte
               || objAnalyse instanceof Short
               || objAnalyse instanceof Void
               || objAnalyse instanceof Character)) {
            try {
               for (Field field : klazz.getDeclaredFields()) {
                  field.setAccessible(true);
                  Object f = field.get(objAnalyse);
                  field.setAccessible(false);

                  Class klazzFind = f.getClass();
                  if (klazzSearch.toString().equals(klazzFind.toString())) {
                     System.out.println(lofPrefix + "Valeur recherchée :"
                           + klazzSearch.toString() + " - valeur trouvée : "
                           + f.toString());
                     return f;
                  } else {
                     objFind = searchObjectByClass(f, klazzSearch);
                  }

                  if (objFind != null) {
                     break;
                  }

               }
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            } catch (IllegalArgumentException e) {
               e.printStackTrace();
            }
         }
      }

      return objFind;
   }

}
