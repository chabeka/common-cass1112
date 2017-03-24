package fr.urssaf.image.commons.dao.spring.exemple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.hibernate.PropertyValueException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Auteur;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings({"PMD.JUnitAssertionsShouldIncludeMessage"})
public class DocumentModifyDaoTest {
	
	private final static String TITRE  = "titre test";
	
	private final static String UPDATE_TITRE  = "update titre";
	
	private final static String SUCCESS = "la transaction doit être un succès";
	
	private final static String FAILURE = "le test doit être un échec";

	@Autowired
	private DocumentModifyDao modifyDao;
	
	@Autowired
   private DocumentDao documentDao;

	@Test
	@Transactional
	public void saveSuccess() {

	   Auteur auteur = new Auteur();
	   auteur.setId(0);
	   
		Document document = new Document();
		document.setTitre(TITRE);
		document.setDate(new Date());
		document.setAuteur(auteur);

		modifyDao.save(document);
		
		assertEquals(6, documentDao.count());

		document = documentDao.find(document.getId());
		assertNotNull(SUCCESS, document);
		assertEquals(TITRE, document.getTitre());

	}

	@Test
	@Transactional
	public void saveSQLSuccess() {

	   Auteur auteur = new Auteur();
      auteur.setId(0);
	   
	   Document document = new Document();
		document.setTitre(TITRE);
		document.setDate(new Date());
		document.setAuteur(auteur);

		modifyDao.saveSQL(document);

		assertEquals(6, documentDao.count());

		document = documentDao.findSQL(document.getId());
		assertNotNull(SUCCESS, document);
		assertEquals(TITRE, document.getTitre());

	}

	@Test
	@Transactional
	public void saveFailure() {

	   
	   Document document = new Document();
		document.setTitre(TITRE);

		try {
		   modifyDao.save(document);
			fail(FAILURE);
		} catch (DataAccessException e) {

		   
		   PropertyValueException exception = (PropertyValueException) e
					.getCause();

			assertEquals("l'entité doit être document", Document.class
					.getCanonicalName(), exception.getEntityName());
			assertEquals("la propriété doit être date", "date", exception
					.getPropertyName());

		}
	}

	@Test (expected=DataAccessException.class)
	@Transactional
	public void saveSQLFailure() {
	   
	   Document document = new Document();
		document.setTitre(TITRE);
		modifyDao.saveSQL(document);
	}

	@Test (expected=DataAccessException.class)
	@Transactional
	public void deleteFailure() {

	   int ID_TEST = 2;

		Document document = documentDao.find(ID_TEST);

		modifyDao.delete(document);
		documentDao.find(ID_TEST);

	}

	@Test
	@Transactional
	public void deleteSQLFailure() {

	   int ID_TEST = 2;

		Document document = documentDao.find(ID_TEST);

		try {
			modifyDao.deleteSQL(document);
			fail(FAILURE);
		} catch (DataAccessException e) {
			assertEquals(5, documentDao.count());
		}

	}

	@Test
	@Transactional
	public void deleteSuccess() {

	   int ID_TEST = 4;

		Document document = documentDao.find(ID_TEST);

		modifyDao.delete(document);

		assertEquals(4, documentDao.count());
		document = documentDao.find(ID_TEST);
		assertNull(SUCCESS, document);

	}

	@Test
	@Transactional
	public void deleteSQLSuccess() {

	   int ID_TEST = 4;

		Document document = documentDao.findSQL(ID_TEST);

		modifyDao.deleteSQL(document);

		assertEquals(4, documentDao.count());
		document = documentDao.find(ID_TEST);
		assertNull(SUCCESS, document);

	}

	@Test
	@Transactional
	public void updateSuccess() {

	   int ID_TEST = 1;

		Document document = documentDao.find(ID_TEST);

		document.setTitre(UPDATE_TITRE);
		modifyDao.update(document);

		assertEquals(5, documentDao.count());

		document = documentDao.find(ID_TEST);
		assertNotNull(SUCCESS, document);
		assertEquals(UPDATE_TITRE, document.getTitre());

	}

	@Test
	@Transactional
	public void updateSuccessSQL() {

	   int ID_TEST = 1;

		Document document = documentDao.findSQL(ID_TEST);

		document.setTitre(UPDATE_TITRE);
		modifyDao.updateSQL(document);

		assertEquals(5, documentDao.count());

		document = documentDao.findSQL(ID_TEST);
		assertNotNull(SUCCESS, document);
		assertEquals(UPDATE_TITRE, document.getTitre());

	}
}
