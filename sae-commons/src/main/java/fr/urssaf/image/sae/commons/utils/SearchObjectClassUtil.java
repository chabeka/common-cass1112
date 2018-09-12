package fr.urssaf.image.sae.commons.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class SearchObjectClassUtil {

   /**
    * Methode utilitaire permettant de retrouver une classe dans un object à
    * analyser. Cette utilitaire ne gére pas les type primitifs.
    * 
    * @param objAnalyse
    *           Object à analyser
    * @param arrayKlazzSearch
    *           Classes recherchées
    * @return Objet trouvé
    */
   public static Object searchObjectByClass(Object objAnalyse,
         String... arrayKlazzSearch) {
      List<String> listeKlazzSearch = Arrays.asList(arrayKlazzSearch);
      String lofPrefix = "searchObjectByClass - ";
      Object objFind = null;
      if (objAnalyse == null || arrayKlazzSearch == null) {
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
                  if (f != null) {
                     field.setAccessible(false);

                     Class klazzFind = f.getClass();
                     if (listeKlazzSearch.contains(klazzFind.toString())) {
                        System.out.println(lofPrefix + "Valeur recherchée :"
                              + arrayKlazzSearch.toString()
                              + " - valeur trouvée : "
                              + f.toString());
                        return f;
                     } else {
                        objFind = searchObjectByClass(f, arrayKlazzSearch);
                     }

                     if (objFind != null) {
                        break;
                     }
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
