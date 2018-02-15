package fr.urssaf.image.commons.dao.spring.exemple.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
public class DocumentReadDaoTest {

   @Autowired
   private DocumentFindDao documentDao;

   private static final int FIRST_RESULT = 1; // c'est à dire à partir du 2ème
                                              // résultat

   private static final int MAX_RESULTS = 3;

   private static final String ORDER = "titre";

   private static final boolean INVERSE = true;

   @Test(timeout = 15000)
   public void scroll() {
      documentDao.scroll();
   }

   @Test(timeout = 15000)
   public void findByCriteria() {

      List<Document> documents = documentDao.findByCriteria(FIRST_RESULT, MAX_RESULTS,
            ORDER, INVERSE);

      assertDocuments(documents);

   }
   
   @Test(timeout = 15000)
   public void findBySQL() {

      List<Document> documents = documentDao.findBySQL(FIRST_RESULT, MAX_RESULTS,
            ORDER, INVERSE);

      assertDocuments(documents);

   }

   private void assertDocuments(List<Document> documents) {

      assertEquals(3, documents.size());

      // check doc
      assertEquals("titre 4", documents.get(0).getTitre());
      assertEquals("titre 3", documents.get(1).getTitre());
      assertEquals("titre 2", documents.get(2).getTitre());

      // check auteur
      assertEquals("auteur 2", documents.get(0).getAuteur().getNom());
      assertEquals("auteur 1", documents.get(1).getAuteur().getNom());
      assertEquals("auteur 4", documents.get(2).getAuteur().getNom());

   }
}
