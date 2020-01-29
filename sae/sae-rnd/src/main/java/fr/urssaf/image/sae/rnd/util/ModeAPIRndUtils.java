/**
 *  Classe Utils pour gérer modeAPI en test
 */
package fr.urssaf.image.sae.rnd.util;

import java.util.HashMap;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.commons.utils.Constantes;



public class ModeAPIRndUtils {

  public static void setAllRndModeAPIThrift() {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(Constantes.CF_RND, MODE_API.HECTOR);
    modesApiTest.put(Constantes.CF_CORRESPONDANCES_RND, MODE_API.HECTOR);
    modesApiTest.put(Constantes.CF_PARAMETERS, MODE_API.HECTOR);
    modesApiTest.put(Constantes.CF_TRACE_DESTINATAIRE, MODE_API.HECTOR);

  }

  public static void setAllRndModeAPICql() {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(Constantes.CF_RND, MODE_API.DATASTAX);
    modesApiTest.put(Constantes.CF_CORRESPONDANCES_RND, MODE_API.DATASTAX);
    modesApiTest.put(Constantes.CF_PARAMETERS, MODE_API.DATASTAX);
    modesApiTest.put(Constantes.CF_TRACE_DESTINATAIRE, MODE_API.DATASTAX);

    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

}
