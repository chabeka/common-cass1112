package fr.urssaf.image.commons.dfce.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe utilitaire pour les objets de type {@link URL}
 * 
 * 
 */
public final class UrlUtils {

   private UrlUtils() {

   }

   /**
    * Creation d'une URL
    * 
    * @param hostName
    *           host de l'URL
    * @param hostPort
    *           port de l'URL
    * @param contextRoot
    *           file de l'URL
    * @param secure
    *           si true alors le protocol de l'url est https sinon http
    * @return URL
    * @throws MalformedURLException
    *            le format de l'uRL est incorrect
    * 
    */
   public static URL createURL(String hostName, int hostPort,
         String contextRoot, boolean secure) throws MalformedURLException {

      String protocol = secure ? "https" : "http";
      URL url = new URL(protocol, hostName, hostPort, contextRoot);

      return url;
   }

}
