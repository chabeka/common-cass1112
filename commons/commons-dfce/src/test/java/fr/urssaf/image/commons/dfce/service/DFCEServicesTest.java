package fr.urssaf.image.commons.dfce.service;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import org.junit.Assert;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-connection-test.xml" })
public class DFCEServicesTest {

   @Autowired
   private DFCEServices dfceServices;


   @Test
   public void getDocumentByUUID_nonExistantTest() {
      // Un UUID qui n'existe pas sur le serveur de test
      final Document doc = dfceServices.getDocumentByUUID(UUID.fromString("b64addca-590f-488f-9ec6-16bbe8f223f1"));
      Assert.assertNull(doc);
   }

   @Test
   public void searchTest() throws ExceededSearchLimitException, SearchQueryParseException {
      final SearchQuery query = new SearchQuery("srt:153556*", dfceServices.getBase());
      final SearchResult searchResult = dfceServices.search(query);
      Assert.assertTrue("Le nombre de résultats doit être supérieur ou égal à zéro", searchResult.getTotalHits() >= 0);
   }

   @Test
   public void reconnectTest() throws ExceededSearchLimitException, SearchQueryParseException {
      final SearchQuery query = new SearchQuery("srt:153556*", dfceServices.getBase());
      final SearchResult searchResult = dfceServices.search(query);
      Assert.assertTrue("Le nombre de résultats doit être supérieur ou égal à zéro", searchResult.getTotalHits() >= 0);

      dfceServices.closeConnexion();
      // Les services DFCE doivent se reconnecter tout seuls
      final SearchResult searchResult2 = dfceServices.search(query);
      Assert.assertTrue("Le nombre de résultats doit être supérieur ou égal à zéro", searchResult2.getTotalHits() >= 0);
   }

   @Test
   public void isDfceUp_reconnect() throws InterruptedException {
      dfceServices.connectTheFistTime();
     
      Assert.assertTrue("DFCE doit être Up!", dfceServices.isServerUp());
      dfceServices.closeConnexion();
      // Les services DFCE doivent se reconnecter tout seuls
      Assert.assertTrue("DFCE doit être Up!", dfceServices.isServerUp());
   }

}
