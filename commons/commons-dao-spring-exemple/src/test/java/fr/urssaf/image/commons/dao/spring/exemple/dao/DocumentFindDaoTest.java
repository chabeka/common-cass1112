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
@SuppressWarnings({"PMD.JUnitAssertionsShouldIncludeMessage"})
public class DocumentFindDaoTest {

   @Autowired
   private DocumentFindDao findDao;

   @Test
   public void findByCriteria() {
      List<Document> documents = findDao.findByCriteria();

      assertDocuments(documents);

   }

   @Test
   public void findBySQL() {
      List<Document> documents = findDao.findBySQL();

      assertDocuments(documents);
   }

   @Test
   public void findByHQL() {
      List<Document> documents = findDao.findByHQL();

      assertDocuments(documents);
   }

   private void assertDocuments(List<Document> documents) {

      assertEquals(2, documents.size());

      assertEquals("titre 3", documents.get(0).getTitre());
      assertEquals("titre 5", documents.get(1).getTitre());

      assertEquals("auteur 1", documents.get(0).getAuteur().getNom());
      assertEquals("auteur 0", documents.get(1).getAuteur().getNom());
   }
}
