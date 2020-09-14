package fr.urssaf.image.commons.cassandra.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe permettanr de faire des operation specifique sur les colonnes 
 * d'une table (Colunm Family). La colonne de la table étant consideré comme 
 * un attribut de la classe qui lui est associé.
 */
public class ColumnUtil {

   /**
    * Retourne une liste de {@link Field} pour classe donnée. Recherche aussi dans le parent de premier de la classe
    *
    * @param persistenceClass
    *           la classe contenant les {@link Field}
    * @param annotation
    *           L'annotation posée sur le {@link Field}
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
    *           La classe contenant la clé de partionnment
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
   public static List<Field> getCompositePartionKeyField(final Class persistenceClass) {
      final List<Field> fields = getClassFieldsByAnnotation(persistenceClass, PartitionKey.class);

      Assert.isTrue(fields.isEmpty() || fields.size() > 1, " La Table a une clé de partionnement composée.");

      return (fields.isEmpty() || fields.size() < 1) ? null : fields;
   }

   /**
    * check if class are simple partition key
    *
    * @param clazz
    * @param isCompositeKey
    */
   public static boolean isSimplePartitionKey(final Class clazz) {
      // check if class are Simple Partion key
      final List<Field> fields = ColumnUtil.getClassFieldsByAnnotation(clazz, PartitionKey.class);
      return fields.size() == 1;
   }

   /**
    * check if class are Composite partition key
    *
    * @param clazz
    * @return
    */
   public static boolean isCompositePartitionKey(final Class clazz) {
      // check if class are composite Partion key
      final List<Field> fields = ColumnUtil.getClassFieldsByAnnotation(clazz, PartitionKey.class);
      return fields.size() >= 2;
   }

   /**
    * Retourne la liste des {@link Field} en fonction de l'annotation
    * @param object
    * 		L'objet contenant les {@link Field}
    * @param annotation
    * 		L'annotation se trouvant sur le {@link Field}
    * @return 
    * 		La liste des {@link Field}
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
    * Retourne le nom de la colunm Family associé à un modele
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
