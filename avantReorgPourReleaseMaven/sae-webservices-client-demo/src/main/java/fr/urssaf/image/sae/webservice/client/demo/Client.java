package fr.urssaf.image.sae.webservice.client.demo;

import org.apache.commons.lang.ArrayUtils;

import fr.urssaf.image.sae.webservice.client.demo.service.PingSecureService;
import fr.urssaf.image.sae.webservice.client.demo.service.PingService;

/**
 * Principale classe executable de client demo<br>
 * 
 * 
 */
public final class Client {

   private Client() {

   }

   /**
    * Méthode principale de l'exécutable<br>
    * Redirection vers les autres méthodes exécutables de client demo<br>
    * <br>
    * paramètres ordonnés:<br>
    * <br>
    * arg[0]: action
    * <ul>
    * <li>ping: appel du ping (voir {@link PingService})</li>
    * <li>ping_secure: appel du ping sécurisé (voir {@link PingSecureService})
    * <ul>
    * <li>arg[1]: role</li>
    * </ul>
    * </li>
    * </ul>
    * 
    * @param args
    *           arguments
    */
   public static void main(String[] args) {

      if (ArrayUtils.isEmpty(args)) {
         throw new IllegalArgumentException("action required");
      }

      String[] newArgs = (String[]) ArrayUtils.subarray(args, 1, args.length);

      if ("ping".equals(args[0])) {

         PingService.main(newArgs);

      } else if ("ping_secure".equals(args[0])) {

         PingSecureService.main(newArgs);

      } else {
         throw new IllegalArgumentException("Unknown action defined: "
               + args[0]);
      }

   }

}
