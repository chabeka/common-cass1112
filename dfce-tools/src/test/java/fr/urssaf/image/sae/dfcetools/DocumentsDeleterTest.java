/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;
import fr.urssaf.image.sae.dfcetools.helper.DFCEServicesHelper;

/**
 * TODO (ac75007394) Description du type
 */
public class DocumentsDeleterTest {

   @Test
   public void deleteGNT_CSPP_Test() throws Exception {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi7gntcveappli1.cve.recouv", "GNT-PROD", 8080);
      final String servers = "cnp6gntcvecas1.cve.recouv,cnp6gntcvecas2.cve.recouv"; // Charge GNT
      final String cassandraLocalDC = "DC6";
      final CqlSession session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      final DocumentsDeleter deleter = new DocumentsDeleter(session);
      deleter.deleteDocumentsAfterDate(dfceServices, "20180125");
   }

}
