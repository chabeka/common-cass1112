/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.commons.dfce.service.impl.DFCEServicesImpl;
import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;

/**
 * TODO (ac75007394) Description du type
 */
public class DocumentDeleterTest {

   @Test
   public void deleteGNT_CSPP_Test() throws Exception {
      final DFCEServices dfceServices = getDfceServices("hwi7gntcveappli1.cve.recouv", "GNT-PROD", 8080);
      final String servers = "cnp6gntcvecas1.cve.recouv,cnp6gntcvecas2.cve.recouv"; // Charge GNT
      final String cassandraLocalDC = "DC6";
      final CqlSession session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      final DocumentDeleter deleter = new DocumentDeleter(session);
      deleter.deleteDocumentsAfterDate(dfceServices, "20180125");
   }

   private DFCEServices getDfceServices(final String server, final String base) throws MalformedURLException {
      return getDfceServices(server, base, 80);
   }

   private DFCEServices getDfceServices(final String server, final String base, final int port) throws MalformedURLException {
      final DFCEConnection params = new DFCEConnection();
      params.setHostName(server);
      params.setHostPort(port);
      params.setBaseName(base);
      params.setContextRoot("/dfce-webapp/toolkit/");
      params.setSecure(true);
      params.setLogin("_ADMIN");
      params.setPassword("DOCUBASE");
      params.setTimeout(3000);
      final String url = "http://" + params.getHostName() + ":" + params.getHostPort() + params.getContextRoot();
      params.setUrlToolkit(url);
      params.setServerUrl(new URL(url));
      params.setCheckHash(true);
      params.setDigestAlgo("SHA-1");
      params.setNbtentativecnx(3);

      final DFCEServices services = new DFCEServicesImpl(params);
      return services;
   }
}
