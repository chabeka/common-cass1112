package fr.urssaf.image.commons.webservice.ssl;

import java.io.IOException;
import java.util.Hashtable;

import javax.net.ssl.SSLContext;

import org.apache.axis.components.net.JSSESocketFactory;
import org.apache.axis.components.net.SecureSocketFactory;

/**
 * Classe de surcharge de (@link
 * org.apache.axis.components.net.JSSESocketFactory) Cette classe est utilisé
 * lorsque l'on utilse le framework Axis pour initialiser la propriété
 * axis.socketSecureFactory<br>
 * ex: AxisProperties.setProperty("axis.socketSecureFactory", Class<? extends
 * AbstractJSSESocketFactory> classe);
 * 
 */
public abstract class AbstractJSSESocketFactory extends JSSESocketFactory
      implements SecureSocketFactory {

   /**
    * On est obligé de surcharger le constructeur de
    * org.apache.axis.components.net.JSSESocketFactory
    * 
    * @param attributes
    *           propriétés de la connexion
    */
   @SuppressWarnings("PMD.ReplaceHashtableWithMap")
   public AbstractJSSESocketFactory(Hashtable<String, String> attributes) {
      super(attributes);
   }

   /**
    * Méthode pour surcharger SSLSocketFactory de
    * org.apache.axis.components.net.JSSESocketFactory
    */
   @Override
   protected final void initFactory() throws IOException {

      sslFactory = getSSLContext().getSocketFactory();

   }

   /**
    * Méthode à surcharger pour renvoyer un objet de type SSLContext
    * 
    * @return SSLContext
    */
   public abstract SSLContext getSSLContext();

}
