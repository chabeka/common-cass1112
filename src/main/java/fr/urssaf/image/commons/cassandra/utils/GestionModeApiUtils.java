/**
 *
 */
package fr.urssaf.image.commons.cassandra.utils;

import java.util.HashMap;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;

/**
 * @author AC75007648 AC75095351
 *         Déplacement classe dans commons-cassandra pour utilisation sur différents projets AC75095351
 *         classe utilisée pour les tests
 */
public class GestionModeApiUtils {

  public GestionModeApiUtils() {
  };

  public static void setModeApiCql(final String cfName) {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(cfName, ModeGestionAPI.MODE_API.DATASTAX);
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

  public static void setModeApiThrift(final String cfName) {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(cfName, ModeGestionAPI.MODE_API.HECTOR);
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

  public static void setModeApiDualReadThrift(final String cfName) {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(cfName, ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT);
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

  public static void setModeApiDualReadCql(final String cfName) {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(cfName, ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL);
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

  public static void setModeApiUnknown(final String cfName) {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(cfName, "UNKOWN");
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

}
