/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.storage.dfce.utils;

import com.google.common.primitives.Ints;

/**
 * Classe permettant de vérifier si une chaîne est un entier
 * (utilisation de la lib de google)
 * return un booléen
 */
public class IntegerUtils {
  public static boolean tryParse(final String value) {
    return Ints.tryParse(value) != null;
  }
}
