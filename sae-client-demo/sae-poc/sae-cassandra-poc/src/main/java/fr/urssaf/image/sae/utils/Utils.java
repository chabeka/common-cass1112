/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.utils;

/**
 * TODO (AC75095028) Description du type
 */
public class Utils {

  public static Boolean getBooleanValue(final String str) {
    Boolean value = Boolean.FALSE;

    if (str != null) {
      value = Boolean.getBoolean(str);
    }

    return value;
  }

}
