package fr.urssaf.image.sae.dfcetools.helper;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;

import java.net.URL;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.commons.dfce.service.impl.DFCEServicesImpl;

/**
 * Utilitaires liés à DFCEServices
 */
public class DFCEServicesHelper {

   public static DFCEServices getDfceServices(final String server, final String base) {
      return getDfceServices(server, base, 80);
   }

   public static DFCEServices getDfceServices(final String server, final String base, final int port) {
      final DFCEConnection params = new DFCEConnection();
      params.setHostName(server);
      params.setHostPort(port);
      params.setBaseName(base);
      params.setContextRoot("/dfce-webapp/");
      params.setSecure(true);
      params.setLogin("_ADMIN");
      params.setPassword("DOCUBASE");
      params.setTimeout(3000);
      final String url = "http://" + params.getHostName() + ":" + params.getHostPort() + params.getContextRoot();
      params.setUrlToolkit(url);
      params.setServerUrl(sneak(() -> new URL(url)));
      params.setCheckHash(true);
      params.setDigestAlgo("SHA-1");
      params.setNbtentativecnx(3);

      final DFCEServices services = new DFCEServicesImpl(params);
      return services;
   }

}
