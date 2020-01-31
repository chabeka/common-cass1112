package fr.urssaf.javaDriverTest.helper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;

/**
 * Utilitaires pour génération de requêtes CQL
 */
public class CQLHelper {

   private CQLHelper() {
      // Classe statique
   }

   public static String getColumnsWithTimestamp(final CqlSession session, final String keyspace, final String tableName) {
      final TableMetadata table = session.getMetadata().getKeyspace(keyspace).get().getTable(tableName).get();
      final Map<CqlIdentifier, ColumnMetadata> columns = table.getColumns();
      final List<ColumnMetadata> primaryKey = table.getPrimaryKey();

      String request = "";
      for (final Entry<CqlIdentifier, ColumnMetadata> entry : columns.entrySet()) {
         final CqlIdentifier colName = entry.getKey();
         final ColumnMetadata colMetadata = entry.getValue();
         if (!request.equals("")) {
            request = request + ",";
         }
         request = request + colName;
         if (!primaryKey.contains(colMetadata)) {
            request = request + ",writetime(" + colName + ")";
         }
      }
      return request;
   }

   public static String getDumpQueryWithTimestamp(final CqlSession session, final String keyspace, final String tableName) {
      return "select " + getColumnsWithTimestamp(session, keyspace, tableName) + " from " + keyspace + "." + tableName;
   }

}
