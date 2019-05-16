/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest.dao;

import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * TODO (ac75007394) Description du type
 */
public class BaseDAO {

   public static UUID getBaseUUID(final CqlSession session, final String baseName) throws Exception {
      final SimpleStatement statement = SimpleStatement.builder("select base_uuid from dfce.base where id = :baseName")
                                                       .addNamedValue("baseName", baseName)
                                                       .build();
      final Row row = session.execute(statement).one();
      return row.getUuid("base_uuid");
   }

   public static UUID getBaseUUID(final CqlSession session) throws Exception {
      final ResultSet rs = session.execute("select id, base_uuid from dfce.base");
      for (final Row row : rs) {
         final String baseName = row.getString("id");
         final UUID baseUUID = row.getUuid("base_uuid");
         if (baseName.contains("GNT") || baseName.contains("GNS") || baseName.contains("SAE")) {
            return baseUUID;
         }
      }
      return null;
   }

}
