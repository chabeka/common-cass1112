/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.util.UUID;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;
import fr.urssaf.image.sae.dfcetools.helper.CompositeIndexHelper;

/**
 * TODO (ac75007394) Description du type
 */
public class IndexCleanerTest {

   /**
    * Exemple d'appel pour nettoyer les index d'un document qui est supprimé mais qui remonte encore dans des résultats de recherche
    */
   @Test
   public void compositeIndexCleanTest() throws Exception {
      final String servers = "cnp69gntcas1.cer69.recouv,cnp69gntcas2.cer69.recouv";
      final String cassandraLocalDC = "LYON_SP";
      final CqlSession session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      final IndexCleaner cleaner = new IndexCleaner(session, "cot&cop&swa&SM_ARCHIVAGE_DATE&", "STRING");
      final String indexValue = CompositeIndexHelper.getCompositeIndexValue(new String[] {"true", "UR317", "PRET", "20190926085404903"});
      final UUID docUUID = UUID.fromString("6fb4e5e8-aafb-4783-8f0d-6f09ec87198a");
      // cleaner.verifyOneEntry(indexValue, docUUID);
      // cleaner.cleanOneEntry(indexValue, docUUID);
      cleaner.verifyOneEntry(indexValue, docUUID);
   }

}
