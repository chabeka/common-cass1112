package fr.urssaf.image.sae.webservice.client.demo.component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import fr.urssaf.image.sae.webservice.client.demo.util.PropertiesUtils;
import fr.urssaf.image.sae.webservice.client.demo.util.ResourceUtils;

/**
 * Configuration d'un serveur de web services par défaut<br>
 * <br>
 * La configuration du serveur se trouve dans
 * sae-webservices-client-demo.properties<br>
 * paramètres:
 * <ul>
 * <li><code>server.url</code>: url du serveur</li>
 * </ul>
  * Cette classe est un singleton<br>
 * l'unique instance est accessible avec la méthode {@link #getInstance()}
 */
public final class DefaultServer {

   private static final String PARAM_PROPERTIES = "sae-client-demo.properties";

   private static final String SERVER_URL = "server.url";

   private final URL url;

   private DefaultServer() {

      this(PARAM_PROPERTIES);

   }

   protected DefaultServer(String paramProperties) {

      Properties properties = PropertiesUtils.load(ResourceUtils.loadResource(
            this, paramProperties));

      if (!properties.containsKey(SERVER_URL)) {
         throw new IllegalArgumentException("'server.url' is required in "
               + paramProperties);
      }

      try {
         this.url = new URL(properties.getProperty(SERVER_URL));
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException("'server.url' is bad URL ", e);
      }

   }

   private static DefaultServer server = new DefaultServer();

   /**
    * 
    * @return instance du serveur de web service
    */
   public static DefaultServer getInstance() {

      return server;
   }

   /**
    * 
    * @return URL du serveur de web service
    */
   public URL getUrl() {

      return this.url;
   }
}
