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
   *          Object à analyser
   * @param reentrantLevel
   *          niveau sur rappel de méthode
   * @param arrayKlazzSearch
   *          Classes recherchées
   * @return Objet trouvé
   */
  public static Object searchObjectByClass(final Object objAnalyse, int reentrantLevel,
                                           final String... arrayKlazzSearch) {
    if (reentrantLevel == 0) {
      reentrantLevel = 1;
    } else {
      reentrantLevel++;
    }
    final List<String> listeKlazzSearch = Arrays.asList(arrayKlazzSearch);
    Object objFind = null;
    if (objAnalyse == null || arrayKlazzSearch == null) {
      reentrantLevel--;
      return null;
    } else {
      // On récupere la class de l'objet à analyser
      final Class klazz = objAnalyse.getClass();
      // On ne gére pas les primitif et les types génériques
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
          // On boucle sur les champs de la classe
          for (final Field field : klazz.getDeclaredFields()) {
            // On rend le champs accessible
            field.setAccessible(true);
            // On récupére l'objet representant le champ
            final Object f = field.get(objAnalyse);
            if (f != null) {
              // On supprime l'accessibilité du champ
              field.setAccessible(false);

              // On récupére la classe du champ à analyser
              final Class klazzFind = f.getClass();
              // On vérifie que la classe trouvée correspond aux classes recherchées
              if (listeKlazzSearch.contains(klazzFind.toString())) {
                reentrantLevel--;
                return f;
              } else {
                // Si on n'a pas trouvé la classe recherché, on parcours le champ. On fait attention pour le cas des classes static et des singleton
                // car le champ peut être la classe elle même et on risque de boucler à l'infini.
                // On limite à 8 niveau pour éviter les StackOverflowError.
                if (reentrantLevel < 8 && !klazz.equals(klazzFind) && klazzFind.getDeclaredFields() != null && klazzFind.getDeclaredFields().length > 0) {
                  objFind = searchObjectByClass(f, reentrantLevel, arrayKlazzSearch);
                }
              }

              // Classe trouvé, on sort
              if (objFind != null) {
                break;
              }
            }
          }
        }
        catch (final IllegalAccessException e) {
          e.printStackTrace();
        }
        catch (final IllegalArgumentException e) {
          e.printStackTrace();
        }
      }
    }

    reentrantLevel--;
    return objFind;
  }

  /**
   * Méthode utilitaire permettant de retrouver une classe dans un object à
   * analyser. Cette utilitaire ne gère pas les type primitifs.
   *
   * @param objAnalyse
   *          Object à analyser
   * @param arrayKlazzSearch
   *          Classes recherchées
   * @return Objet trouvé
   */
  public static Object searchObjectByClass(final Object objAnalyse,
                                           final String... arrayKlazzSearch) {
    return searchObjectByClass(objAnalyse, 0, arrayKlazzSearch);
  }

}
