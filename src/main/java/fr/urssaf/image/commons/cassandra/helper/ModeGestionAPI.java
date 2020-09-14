/**
 *
 */
package fr.urssaf.image.commons.cassandra.helper;

import java.util.HashMap;

/**
 * Cette classe est une classe "statique" qui permet de stocker la
 * HashMap contenant les CF existantes et le mode d'API
 * Le nom de la CF sera la clé de la HashMap et le mode la value
 *
 * @author AC75007648
 */
public final class ModeGestionAPI {

  // Cette HashMap est celle que toute classe peut obtenir de façon publique
  public static HashMap<String, String> listeCfsModes = new HashMap<>();

  // Cette interface est le référencement des modes de fonctionnement API
  public static interface MODE_API {

    String HECTOR = "HECTOR";

    String DATASTAX = "DATASTAX";

    String DUAL_MODE_READ_THRIFT = "DUAL_MODE_READ_THRIFT";

    String DUAL_MODE_READ_CQL = "DUAL_MODE_READ_CQL";

  }

  private ModeGestionAPI() {
  }


}
