/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;
import fr.urssaf.image.sae.dfcetools.helper.DFCEServicesHelper;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.IndexPaginationSearchQuery;
import net.docubase.toolkit.model.search.SearchResult;

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

   @Test
   /**
    * Exemple de rattrapage d'une suppression de masse qui est plantée à cause d'un document qui est supprimé mais pour lequel
    * il reste des index.
    */
   public void rattrapagePurgeWattTest() throws Exception {
      // L'environnement concerné
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi69progednatgntcot1boappli3.cer69.recouv", "GNT-PROD", 8080);
      final String servers = "cnp69gntcas1.cer69.recouv,cnp69gntcas2.cer69.recouv";
      final String cassandraLocalDC = "LYON_SP";
      final CqlSession session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      // La requête utilisée par la suppression de masse
      final String query = "(cot:\"true\") AND (cop:\"UR317\") AND (swa:\"PRET\") AND SM_ARCHIVAGE_DATE:[20190925 TO 20220927]";
      final String indexToUse = "cot&cop&swa&SM_ARCHIVAGE_DATE&";

      // On spécifie soit l'uuid du document qui pose problème. Sinon, l'ensemble des documents rencontrés sont vérifiés/nettoyés
      // final String uuidToSearch = "2a509ccb-173c-4a5d-b950-1fb23d196fe2";
      final String uuidToSearch = null;
      // Max de doc "corrects" parcourus avant de s'arrêter
      final int maxGoodCounter = 500;

      final IndexPaginationSearchQuery searchQuery = new IndexPaginationSearchQuery(query, dfceServices.getBase());
      final List<String> indexOrderPreferenceList = new ArrayList<>();
      indexOrderPreferenceList.add(indexToUse);
      searchQuery.setIndexOrderPreference(indexOrderPreferenceList);
      searchQuery.setSearchLimit(200); // Pagination

      int counter = 0;
      int alreadyGoodCounter = 0;
      boolean shouldStop = false;
      while (!shouldStop) {
         final SearchResult searchResult = dfceServices.search(searchQuery);
         final String lastReadIndex = searchResult.getLastReadIndex();
         searchQuery.setCurrentStep(lastReadIndex);

         final List<Document> documents = searchResult.getDocuments();
         for (final Document doc : documents) {
            final Date archivageDate = doc.getArchivageDate();
            final UUID uuid = doc.getUuid();
            counter++;
            System.out.println(counter + " " + uuid + " " + archivageDate);
            if (uuidToSearch == null || uuidToSearch.equals(uuid.toString().toLowerCase())) {
               if (uuidToSearch != null) {
                  final List<Criterion> criterions = doc.getAllCriterions();
                  for (final Criterion criterion : criterions) {
                     System.out.println(criterion.getCategoryName() + " : " + criterion.getWordValue());
                  }
                  final String pattern = "yyyyMMddHHmmssSSS";
                  final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                  sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                  System.out.println("SM_ARCHIVAGE_DATE : " + sdf.format(archivageDate));
               }

               final DocumentIndexChecker checker = new DocumentIndexChecker(session, dfceServices);
               final boolean cleaned = checker.cleanDocumentIfNeeded(doc);
               if (!cleaned) {
                  alreadyGoodCounter++;
               }
               if (uuidToSearch == null) {
                  shouldStop = alreadyGoodCounter >= maxGoodCounter;
               } else {
                  shouldStop = true;
               }

               if (shouldStop) {
                  break;
               }
            }
         }
         if (lastReadIndex == null) {
            break;
         }
      }
      System.out.println("Compteur : " + counter);
   }

}
