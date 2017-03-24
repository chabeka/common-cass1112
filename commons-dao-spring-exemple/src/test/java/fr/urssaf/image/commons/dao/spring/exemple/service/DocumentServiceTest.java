package fr.urssaf.image.commons.dao.spring.exemple.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.PropertyValueException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.dao.DocumentDao;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Auteur;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
public class DocumentServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private DocumentService documentService;

	@Autowired
	private DocumentDao documentDao;

	@Test
	@Transactional
	public void save() {

	   Auteur auteur = new Auteur();
      auteur.setId(0);
      
      Document document;
      
      List<Document> documents = new ArrayList<Document>();
      
      document = new Document();
      document.setTitre("titre test 1");
      document.setDate(new Date());
      document.setAuteur(auteur);
      documents.add(document);
      
      document = new Document();
      document.setTitre("titre test 2");
      document.setDate(new Date());
      document.setAuteur(auteur);
      documents.add(document);
      
      document = new Document();
      document.setTitre("titre test 3");
      document.setDate(new Date());
      document.setAuteur(auteur);
      documents.add(document);
      
      documentService.save(documents);
      
      assertDocument(documents.get(0).getId(), "titre test 1");
      assertDocument(documents.get(1).getId(), "titre test 2");
      assertDocument(documents.get(2).getId(), "titre test 3");

	}

	@Test
	public void saveFailure() {

	   Auteur auteur = new Auteur();
	   auteur.setId(0);
	   
	   Document document;
	   
	   List<Document> documents = new ArrayList<Document>();
	   
	   document = new Document();
	   document.setTitre("titre test 1");
	   document.setDate(new Date());
	   document.setAuteur(auteur);
	   documents.add(document);
	   
	   document = new Document();
      document.setTitre("titre test 2");
      document.setDate(null);
      document.setAuteur(auteur);
      documents.add(document);
      
      document = new Document();
      document.setTitre("titre test 3");
      document.setDate(new Date());
      document.setAuteur(auteur);
      documents.add(document);
	   

		try {
			
		   documentService.save(documents);
			
			fail("le test doit être un échec");
						
		} catch (DataAccessException e) {
			
			PropertyValueException exception = (PropertyValueException) e.getCause();

			assertEquals(
			      "l'entité doit être document", 
			      Document.class.getCanonicalName(), 
			      exception.getEntityName());
			
			assertEquals(
			      "la propriété doit être date", 
			      "date",
			      exception.getPropertyName());
			
		}

	}
	
	@SuppressWarnings("PMD")
	private void assertDocument(int id, String titre) {
		Document doc = documentDao.find(id);
		assertEquals(titre, doc.getTitre());
	}
}
