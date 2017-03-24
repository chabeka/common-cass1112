package fr.urssaf.image.commons.webservice.rpc.aed.generate;

import javax.net.ssl.HttpsURLConnection;

import org.apache.axis.wsdl.WSDL2Java;

import fr.urssaf.image.commons.webservice.rpc.aed.context.AEDConfig;
import fr.urssaf.image.commons.webservice.rpc.aed.context.AEDSSLContextFactory;

/**
 * Classe de génération de code d'un webservice.<br>
 * Basée sur le Framework <u>Apache Axis</u><br>
 * elle instancie un objet WSDLToJava {@link org.apache.axis.wsdl.WSDL2Java}. <br>
 * <br>
 * Utilisé pour les webservices de type RPC/Encoded
 */
public final class AEDGenerateSource {

   private AEDGenerateSource() {

   }

   /**
    * Exécutable pour la génération du code source
    * 
    * @param args
    *           pas prise en compte
    */
   public static void main(String[] args) {

      try {
         HttpsURLConnection.setDefaultSSLSocketFactory(AEDSSLContextFactory
               .getSSLContext().getSocketFactory());
      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

      String[] newArgs = new String[] { "-osrc/main/java", "-v",
            "-p" + AEDConfig.PACKAGE, AEDConfig.WSDL };

      WSDL2Java.main(newArgs);

   }
}
