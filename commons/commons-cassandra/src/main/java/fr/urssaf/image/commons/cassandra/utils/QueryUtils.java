/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.utils;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.annotations.PartitionKey;

/**
 * TODO (AC75095028) Description du type
 */
public class QueryUtils {
  /**
   * TODO (AC75095028) Description du champ
   */
  private static final String END_BATCH = " APPLY BATCH";

  /**
   * TODO (AC75095028) Description du champ
   */
  private static final String BEGIN_BATCH = "BEGIN BATCH ";

  /**
   * échappement des guillements autour du nom de la CF
   */
  private static final String BACKSLASH = "\"";

  private static final Logger LOGGER = LoggerFactory.getLogger(QueryUtils.class);

  /**
   * create an insert query with fields and associated value of fields
   *
   * @param <T>
   * @param insert
   * @param entity
   */
  public static <T> void createInsert(final List<Field> fields, final Insert insert, final T entity) {

    Assert.notNull(fields, "fields must not be null");
    Assert.notNull(insert, "insert statement must not be null");
    Assert.notNull(entity, "entity must not be null");

    for (final Field field : fields) {
      try {
        field.setAccessible(true);
        if (field.get(entity) != null) {
          insert.value(field.getName().toLowerCase(), field.get(entity));
        }
      }
      catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Création d'une requête de suppresion
   *
   * @param <T>
   *          la classe portant le nom de la colonne familly
   * @param delete
   *          le {@link Statement} de suppression
   * @param entity
   *          l'entité à supprimer
   */
  public static <T> void createDeleteQuery(final Class clazz, final Delete delete, final T entity) {

    Assert.notNull(clazz, "clazz must not be null");
    Assert.notNull(delete, "delete statement must not be null");
    Assert.notNull(entity, "entity must not be null");

    final boolean cfKey = ColumnUtil.isSimplePartitionKey(clazz) || ColumnUtil.isCompositePartitionKey(clazz);
    Assert.isTrue(cfKey, "La clé de partionnement de la classe ne peut être null");

    final List<Field> fields = ColumnUtil.getClassFieldsByAnnotation(clazz, PartitionKey.class);
    // final Field keyField = ColumnUtil.getSimplePartionKeyField(clazz);

    for (final Field keyField : fields) {
      final String keyName = keyField.getName();
      for (final Field field : Utils.getEntityFileds(clazz)) {
        try {
          if (field.getName().equals(keyName)) {
            field.setAccessible(true);
            if (field.get(entity) != null) {
              delete.where(eq(keyName, field.get(entity)));
            }
          }
        }
        catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * Insertion d'entités bean avec un BATCH
   *
   * @param clazz
   *          la classe portant le nom de la colonne familly
   * @param entities
   *          les entités à inserrer
   * @param columnFamillyName
   *          le nom de la colonne familly
   * @return
   */
  public static <T> String createInsertBatch(final Class clazz, final Iterable<? extends T> entities, final String columnFamillyName) {

    Assert.notNull(clazz, "clazz must not be null");
    Assert.isTrue(entities.iterator().hasNext(), "entities must not be empty");
    Assert.notNull(columnFamillyName, "columnFamillyName must not be null");

    String batch = BEGIN_BATCH;
    for (final T entity : entities) {
      final Insert insert = QueryBuilder.insertInto(columnFamillyName);
      final List<Field> fields = Utils.getEntityFileds(clazz);
      QueryUtils.createInsert(fields, insert, entity);
      batch += insert.toString();
    }
    batch += END_BATCH;
    return batch;
  }

  /**
   * @param <T>
   *          la classe portant le nom de la colonne familly
   * @param select
   *          le {@link Statement} d'insertion
   * @param entity
   *          l'entité à inserrer
   */
  public static <T> void createSelectQueryById(final Class clazz, final Select select, final T entity) {

    Assert.notNull(clazz, "clazz must not be null");
    Assert.notNull(select, "select statement must not be null");
    Assert.notNull(entity, "entity must not be null");

    // check if class are simple or composite partionkey for cassandra
    boolean cfKey = ColumnUtil.isSimplePartitionKey(clazz);
    cfKey = cfKey && ColumnUtil.isCompositePartitionKey(clazz);
    Assert.isTrue(cfKey, "La clé de l'entité à supprimer ne peut être null");

    final List<Field> fields = ColumnUtil.getClassFieldsByAnnotation(clazz, PartitionKey.class);

    for (final Field keyField : fields) {
      final String keyName = keyField.getName();
      for (final Field field : Utils.getEntityFileds(clazz)) {
        try {
          if (field.getName().equals(keyName)) {
            field.setAccessible(true);
            if (field.get(entity) != null) {
              select.where(eq(keyName, field.get(entity)));
            }
          }
        }
        catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * @param clazz
   *          la classe portant le nom de la colonne familly
   * @param entities
   *          les entités à inserrer
   * @param columnFamillyName
   *          le nom de la colonne familly
   * @return un {@link BatchStatement} inserrant toutes entités
   */
  public static <T> BatchStatement createBatchStatement(final Class clazz, final Iterable<T> entities, final String columnFamillyName) {

    Assert.notNull(clazz, "clazz must not be null");
    Assert.isTrue(entities.iterator().hasNext(), "entities must not be empty");
    Assert.notNull(columnFamillyName, "columnFamillyName must not be null");

    final BatchStatement statement = new BatchStatement();
    for (final T entity : entities) {
      final Insert insert = QueryBuilder.insertInto(BACKSLASH + columnFamillyName + BACKSLASH);
      final List<Field> fieldsMT = Utils.getEntityFileds(clazz);
      QueryUtils.createInsert(fieldsMT, insert, entity);
      statement.add(insert);
    }
    return statement;
  }
}
