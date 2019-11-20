/**
 *
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.HashMap;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;

/**
 * @author AC75007648
 */
public class GestionModeApiTest {

   public GestionModeApiTest() {
   };

   public static void setModeApiCql(final String cfName) {
      final HashMap<String, String> modesApiTest = new HashMap<String, String>();
      modesApiTest.put(cfName, "DATASTAX");
      ModeGestionAPI.setListeCfsModes(modesApiTest);
   }

   public static void setModeApiThrift(final String cfName) {
      final HashMap<String, String> modesApiTest = new HashMap<String, String>();
      modesApiTest.put(cfName, "HECTOR");
      ModeGestionAPI.setListeCfsModes(modesApiTest);
   }

}
