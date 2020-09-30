/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.util.UUID;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;
import fr.urssaf.image.sae.dfcetools.helper.DFCEServicesHelper;
import net.docubase.toolkit.model.document.Document;

/**
 * TODO (ac75007394) Description du type
 */
public class DocumentIndexCheckerTest {

   /**
    * Exemple d'appel pour vérifier l'indexation d'un document
    */
   @Test
   public void checkDocumentTest() {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi31intgntv6boappli1.gidn.recouv", "GNT-INT", 8080);
      final String servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv";
      final String cassandraLocalDC = "DC1";
      final CqlSession session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);
      final DocumentIndexChecker checker = new DocumentIndexChecker(session, dfceServices);
      final Document doc = dfceServices.getDocumentByUUID(UUID.fromString("e1e33253-5ba8-402d-ab04-63d8f4f4d382"));
      if (doc == null) {
         throw new RuntimeException("Doc non trouvé");
      }
      checker.checkDocument(doc);
   }


}
