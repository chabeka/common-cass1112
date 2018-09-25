package fr.urssaf.image.sae.commons.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

public class ColumnUtil {

  /**
   * Retourne une liste de {@link Field} pour classe donnée. Recherche aussi dans le parent de premier de la classe
   *
   * @param persistenceClass
   *          la classe contenant les {@link Field}
   * @param annotation
   *          L'annotation posée sur le {@link Field}
   * @return {@link List} de {@link Field}
   */
  public static List<Field> getClassFieldsByAnnotation(final Class persistenceClass, final Class annotation) {

    final List<Field> fields = getField(persistenceClass, annotation);
    fields.addAll(getField(persistenceClass.getSuperclass(), annotation));

    return fields;
  }

  /**
   * Retourne le {@link Field} de la "Simple PartionKey" de la classe
   *
   * @param persistenceClass
   *          La classe contenant la clé de partionnment
   * @return
   */
  public static Field getSimplePartionKeyField(final Class persistenceClass) {

    final List<Field> fields = getClassFieldsByAnnotation(persistenceClass, PartitionKey.class);
    Assert.isTrue(fields.size() == 1, " La Table " + persistenceClass.getName() + " n'a qu'une seule clé de partionnement");

    return fields.isEmpty() ? null : fields.get(0);
  }

  /**
   * Retourne la {@link List} des {@link Field} de la "Composite Keys" de la classe
   *
   * @param persistenceClass
   * @return
   */
  public static List<Field> getCompositePartionKeyField(final Class persistenceClass, final Class annotation) {
    final List<Field> fields = getClassFieldsByAnnotation(persistenceClass, PartitionKey.class);

    Assert.isTrue(fields.isEmpty() || fields.size() > 1, " La Table a une clé de partionnement composée.");

    return (fields.isEmpty() || fields.size() < 1) ? null : fields;
  }

  /**
   * Verifie si la classe contient une clé de partionnement Simple ou Composée
   * <br>
   * Si Clé de partionnement Simple, la classe doit obligatoirement contenir un et un seul
   * attribut avec l'annotation {@link PartitionKey}
   * <br>
   * Si Clé de partionnement Composée (Composite Keys), la classe doit obligatoirement contenir au moin deux
   * attributs avec l'annotation {@link PartitionKey}
   *
   * @param clazz
   *          La classe
   * @return True si
   */
  public static boolean isSimpleOrCompositePartionKey(final Class clazz) {
    // check if class are composite Partion key
    List<Field> fields = ColumnUtil.getClassFieldsByAnnotation(clazz, PartitionKey.class);
    final boolean isCompositeKey = fields.size() >= 2;
    if (isCompositeKey) {
      return true;
    }
    // check if class are Simple Partion key
    fields = ColumnUtil.getClassFieldsByAnnotation(clazz, PartitionKey.class);
    final boolean isSimpleKey = fields.size() == 1;
    if (isCompositeKey || isSimpleKey) {
      return true;
    }

    return false;
  }

  /**
   * @param object
   * @param annotation
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Field> getField(final Class object, final Class annotation) {
    final List<Field> fields = new ArrayList<>();
    for (final Field field : object.getDeclaredFields()) {
      if (field.getAnnotation(annotation) != null) {
        fields.add(field);
      }
    }
    return fields;
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
