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
public class DocumentLoadDaoTest {

	@Autowired
   private DocumentFindDao findDao;

	@Test
	public void loadEtatsByHQL() {
		
		List<Document> documents = findDao.loadEtatsByHQL();
		assertCollection(documents);
	}
	
	@Test
	public void loadEtatsByCriteria() {
		
		List<Document> documents = findDao.loadEtatsByCriteria();
		assertCollection(documents);
	}
	
	@Test
   public void loadEtatsByInitialize() {
      
      List<Document> documents = findDao.loadEtatsByInitialize();
      
      assertEquals(5, documents.size());

      //check doc
      assertEquals("titre 1", documents.get(0).getTitre());
      assertEquals("titre 2", documents.get(1).getTitre());
      assertEquals("titre 3", documents.get(2).getTitre());
      assertEquals("titre 4", documents.get(3).getTitre());
      assertEquals("titre 5", documents.get(4).getTitre());
      
      //check etats
      assertEquals(1, documents.get(0).getEtats().size());
      assertEquals(3, documents.get(1).getEtats().size());
      assertEquals(2, documents.get(2).getEtats().size());
      assertEquals(0, documents.get(3).getEtats().size());
      assertEquals(0, documents.get(4).getEtats().size());
   }
	
	private void assertCollection(List<Document> documents) {

		assertEquals(3, documents.size());

		//check doc
		assertEquals("titre 1", documents.get(0).getTitre());
		assertEquals("titre 2", documents.get(1).getTitre());
		assertEquals("titre 3", documents.get(2).getTitre());
		
		//check etats
		assertEquals(1, documents.get(0).getEtats().size());
		assertEquals(2, documents.get(1).getEtats().size());
		assertEquals(2, documents.get(2).getEtats().size());
		
	}
	

}
