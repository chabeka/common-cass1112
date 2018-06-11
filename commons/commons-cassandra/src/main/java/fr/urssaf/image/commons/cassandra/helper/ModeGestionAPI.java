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

    String DUAL_MODE = "DUAL_MODE";
  }

  private ModeGestionAPI() {
  }

  /**
   * Permet de setter la HasMap avec le contenu de la BDD
   *
   * @param listeCfsModes
   */
  public static void setListeCfsModes(final HashMap<String, String> listeCfsModes) {
    ModeGestionAPI.listeCfsModes = listeCfsModes;
  }

  /**
   * Permet de lire la HashMap
   *
   * @return listeCfsModes
   */
  public static HashMap<String, String> getListeCfsModes() {
    return listeCfsModes;
  }

  /**
   * Retourne le mode d'API pour une CF
   *
   * @param cfName
   * @return listeCfsModes
   */
  public static String getModeApiCf(final String cfName) {
    // Dans le cas ou la HashMap contient le nom de la CF
    // passée en paramètre comme clé
    if (listeCfsModes.containsKey(cfName)) {
      return listeCfsModes.get(cfName);
    } else {
      // Sinon par défaut on renvoit le fonctionnement avec Hector
      return MODE_API.HECTOR;
    }
  }
}
