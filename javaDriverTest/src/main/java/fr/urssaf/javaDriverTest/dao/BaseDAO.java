/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest.dao;

import java.util.UUID;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * TODO (ac75007394) Description du type
 */
public class BaseDAO {

   public static UUID getBaseUUID(final Session session, final String baseName) throws Exception {
      final Row row = session.execute("select base_uuid from dfce.base where id=?", baseName).one();
      return row.getUUID("base_uuid");
   }

   public static UUID getBaseUUID(final Session session) throws Exception {
      final ResultSet rs = session.execute("select id, base_uuid from dfce.base");
      for (final Row row : rs) {
         final String baseName = row.getString("id");
         final UUID baseUUID = row.getUUID("base_uuid");
         if (baseName.contains("GNT") || baseName.contains("GNS") || baseName.contains("SAE")) {
            return baseUUID;
         }
      }
      return null;
   }

}
