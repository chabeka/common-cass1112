package fr.urssaf.image.sae.services.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Méthodes utilitaires sur le hostname
 */
public final class HostnameUtil {

   private HostnameUtil() {
      // Constructeur privé
   }

   private static final Logger LOG = LoggerFactory
         .getLogger(HostnameUtil.class);

   /**
    * Renvoie InetAddress.getLocalHost()
    * 
    * @return InetAddress.getLocalHost()
    * @throws UnknownHostException
    *            levée par InetAddress.getLocalHost
    */
   public static InetAddress getLocalHost() throws UnknownHostException {
      return InetAddress.getLocalHost();
   }

   /**
    * Renvoie le hostname en masquant les exceptions
    * 
    * @return le hostname, ou null si non trouvé
    */
   public static String getHostname() {
      String hostName = null;

      try {
         InetAddress address = InetAddress.getLocalHost();
         hostName = address.getHostName();

      } catch (UnknownHostException e) {
         LOG
               .warn(
                     "Impossible de récupérer les informations relatives à la machine locale",
                     e);
      }
      return hostName;
   }

   /**
    * Renvoie l'adresse IP en masquant les exceptions
    * 
    * @return l'adresse IP, ou null si non trouvée
    */
   public static String getIP() {
      String hostIP = null;

      try {
         InetAddress address = InetAddress.getLocalHost();
         hostIP = address.getHostAddress();

      } catch (UnknownHostException e) {
         LOG
               .warn(
                     "Impossible de récupérer les informations relatives à la machine locale",
                     e);
      }
      return hostIP;
   }

}
