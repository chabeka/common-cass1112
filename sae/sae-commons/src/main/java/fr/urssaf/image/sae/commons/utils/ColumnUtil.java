package fr.urssaf.image.sae.commons.utils;

import java.lang.reflect.Field;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

public class ColumnUtil {

  /**
   * @param persistenceClass
   * @return
   */
  public static Field getKeyField(final Class persistenceClass, final Class annotation) {
    final Field field = getField(persistenceClass, annotation);
    if (field == null) {
      return getField(persistenceClass.getSuperclass(), annotation);
    }
    return field;
  }

  /**
   * @param object
   * @param annotation
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Field getField(final Class object, final Class annotation) {
    for (final Field field : object.getDeclaredFields()) {
      if (field.getAnnotation(annotation) != null) {
        return field;
      }
    }
    return null;
  }

  /**
   * @param object
   * @return
   */
  public static String getSimpleKeyFieldName(final Class object) {
    final Field keyField = ColumnUtil.getKeyField(object, PartitionKey.class);
    final String keyName = keyField.getName();
    return keyName;
  }

  /**
   * @param object
   * @return
   */
  public static String getColumnFamily(final Class<?> object) {

    String columnFamillyName = "";
    final Table columnFamilyTable = object.getAnnotation(Table.class);
    if (columnFamilyTable != null && !columnFamilyTable.name().equals("")) {
      columnFamillyName = columnFamilyTable.name();
    } else {
      columnFamillyName = object.getSimpleName();
    }
    return columnFamillyName;
  }
}
