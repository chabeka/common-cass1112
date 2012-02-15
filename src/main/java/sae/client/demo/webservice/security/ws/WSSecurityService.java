package sae.client.demo.webservice.security.ws;

import java.io.InputStream;
import java.util.UUID;

import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.security.util.StreamUtils;

/**
 * Classe de service de WS Security
 * 
 * 
 */
public class WSSecurityService {

   private static final String WS_SECURITY = "security/wsseSecurity.xml";

   /**
    * Instanciation de l'entête WS Security basé sur le format du fichier
    * "security/wsseSecurity.xml"<br>
    * <br>
    * Les valeurs entre [] sont remplacées par les valeurs passées en argument
    * de la méthode <br>
    * <br>
    * Pour [messageID] un identifiant est généré à chaque appel de la méthode
    * 
    * @param assertion
    *           valeur de [Assertion]
    * @param identifiant
    *           valeur de [AssertionID]
    * @return entête WS Security
    */
   public final String createWSSEHeader(
         String assertion, 
         UUID identifiant) {

      InputStream wsseStream = ResourceUtils.loadResource(this, WS_SECURITY);

      String[] searchList = new String[] { "[Assertion]", "[AssertionID]" };
      String[] replacementList = new String[] { assertion, identifiant.toString() };

      return StreamUtils.createObject(wsseStream, searchList, replacementList);

   }

}
