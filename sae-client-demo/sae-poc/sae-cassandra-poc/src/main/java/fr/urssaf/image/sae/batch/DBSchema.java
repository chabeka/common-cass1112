/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.batch;

import com.datastax.driver.core.Session;

/**
 * TODO (AC75095028) Description du type
 */
public class DBSchema {

  public static void createKeyspace(final Session session) {
    session.execute(
                    "CREATE KEYSPACE SAE " +
                        " WITH REPLICATION = { " +
                        "'class' : 'SimpleStrategy', " +
                        "'replication_factor' : 3" +
                        "};");
  }

  public static void createTableMetadata(final Session session) {
    session.execute(
                    "CREATE TABLE metadata (" +
                        "shortCode text PRIMARY KEY," +
                        "longCode text," +
                        "type text," +
                        "requiredForArchival boolean," +
                        "requiredForStorage boolean, " +
                        "length int," +
                        "pattern text," +
                        "consultable boolean," +
                        "defaultConsultable boolean," +
                        "searchable boolean, " +
                        "internal boolean," +
                        "archivable boolean," +
                        "label text," +
                        "description text," +
                        "hasDictionary boolean," +
                        "dictionaryName text," +
                        "isIndexed boolean," +
                        "modifiable boolean," +
                        "clientAvailable boolean," +
                        "leftTrimable boolean," +
                        "rightTrimable boolean," +
                        "transferable boolean" +

                        ");");
  }

  public static void createTableDictionary(final Session session) {
    session.execute(
                    "CREATE TABLE dictionary (" +
                        "id uuid PRIMARY KEY," +
                        "value text" +
                        ");");
  }

  public static void createTablePOC(final Session session) {
    session.execute(
                    "CREATE TABLE POC (" +
                        "id uuid PRIMARY KEY," +
                        "value text" +
                        ");");
  }
}
