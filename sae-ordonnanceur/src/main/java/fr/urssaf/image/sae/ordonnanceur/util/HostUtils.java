package fr.urssaf.image.sae.ordonnanceur.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * classe utilitaire pour la classe {@link InetAddress}
 * 
 * 
 */
public final class HostUtils {

   private HostUtils() {

   }

   /**
    * 
    * @return nom de la machine locale
    * @throws UnknownHostException
    *            exception levée si l'adresse ne peut pas être trouvée
    */
   public static String getLocalHostName() throws UnknownHostException {

      InetAddress server = InetAddress.getLocalHost();

      String serverName = server.getHostName();

      return serverName;
   }
}
