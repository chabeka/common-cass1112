package fr.urssaf.image.sae.commons.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class SearchObjectClassUtil {

   /**
    * Méthode utilitaire permettant de retrouver une classe dans un object à
    * analyser. Cette utilitaire ne gère pas les type primitifs.
    *
    * @param objAnalyse
    *           Object à analyser
    * @param arrayKlazzSearch
    *           Classes recherchées
    * @return Objet trouvé
    */
   public static Object searchObjectByClass(final Object objAnalyse,
                                            final String... arrayKlazzSearch) {
      final List<String> listeKlazzSearch = Arrays.asList(arrayKlazzSearch);
      final String lofPrefix = "searchObjectByClass - ";
      Object objFind = null;
      if (objAnalyse == null || arrayKlazzSearch == null) {
         return null;
      } else {
         final Class klazz = objAnalyse.getClass();
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
               for (final Field field : klazz.getDeclaredFields()) {
                  field.setAccessible(true);
                  final Object f = field.get(objAnalyse);
                  if (f != null) {
                     field.setAccessible(false);

                     final Class klazzFind = f.getClass();
                     if (listeKlazzSearch.contains(klazzFind.toString())) {
                        return f;
                     } else {
                        objFind = searchObjectByClass(f, arrayKlazzSearch);
                     }

                     if (objFind != null) {
                        break;
                     }
                  }
               }
            } catch (final IllegalAccessException e) {
               e.printStackTrace();
            } catch (final IllegalArgumentException e) {
               e.printStackTrace();
            }
         }
      }

      return objFind;
   }

}
