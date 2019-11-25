/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.utils;

import java.util.Collections;
import java.util.List;

/**
 * (AC75095351) Classe qui permet de vérifier l'égalité entre deux listes, les entités associés doivent implémenter un Comparable
 * et la fonction equals doit être implémentée
 */
public class CompareUtils {


  /**
   * Comparaison générique de deux listes
   * es entités associés doivent implémenter un Comparable
   * et la fonction equals doit être implémentée
   * 
   * @param list1
   * @param list2
   * @return boolean
   */
  public static <T extends Comparable<? super T>> boolean compareListsGeneric(final List<T> list1, final List<T> list2) {
    // On trie les listes
    Collections.sort(list1);
    Collections.sort(list2);
    // on renvoie un booléen de comparaions des listes
    return list1.equals(list2);
  }
}
