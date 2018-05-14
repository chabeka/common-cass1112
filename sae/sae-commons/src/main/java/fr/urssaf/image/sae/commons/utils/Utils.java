/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO (AC75095028) Description du type
 */
public class Utils {

  /**
   * Get bean fields (include super class fields)
   *
   * @param entity
   * @return
   */
  public static List<Field> getEntityFileds(final Class entity) {
    final List<Field> fields = new LinkedList<>();
    for (final Field field : entity.getDeclaredFields()) {
      fields.add(field);
    }
    if (!listFieldsFromSuperClass(entity).isEmpty()) {
      fields.addAll(listFieldsFromSuperClass(entity));
    }
    return fields;
  }

  /**
   * Get bean fields from super class
   *
   * @param class1
   * @return
   */
  public static List<Field> listFieldsFromSuperClass(final Class<?> class1) {
    final List<Field> fields = new LinkedList<>();
    for (final Field field : class1.getSuperclass().getDeclaredFields()) {
      fields.add(field);
    }
    return fields;
  }
}
