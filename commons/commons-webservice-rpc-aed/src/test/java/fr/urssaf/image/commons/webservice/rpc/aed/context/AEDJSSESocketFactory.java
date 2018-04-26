package fr.urssaf.image.commons.webservice.rpc.aed.context;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.axis.components.net.JSSESocketFactory;

/**
 * Classe de surcharge de (@link
 * org.apache.axis.components.net.JSSESocketFactory) Cette classe est utilisé
 * lorsque l'on utilse le framework Axis pour initialiser la propriété
 * axis.socketSecureFactory<br>
 * ex: AxisProperties.setProperty("axis.socketSecureFactory", Class<? extends
 * AEDJSSESocketFactory> classe);
 * 
 */
public class AEDJSSESocketFactory extends JSSESocketFactory {

   /**
    * On est obligé de surcharger le constructeur de
    * org.apache.axis.components.net.JSSESocketFactory
    * 
    * @param attributes
    *           propriétés de la connexion
    */
   @SuppressWarnings("PMD.ReplaceHashtableWithMap")
   public AEDJSSESocketFactory(Hashtable<String, String> attributes) {
      super(attributes);

   }

   @Override
   protected final void initFactory() throws IOException {

      try {
         sslFactory = AEDSSLContextFactory.getSSLContext().getSocketFactory();
      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

   }

}
