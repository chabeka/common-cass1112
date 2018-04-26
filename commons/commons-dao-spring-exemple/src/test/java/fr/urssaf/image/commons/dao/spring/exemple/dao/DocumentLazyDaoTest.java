package fr.urssaf.image.commons.dao.spring.exemple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings({"PMD.JUnitAssertionsShouldIncludeMessage"})
public class DocumentLazyDaoTest {

	private static final Logger LOG= Logger
			.getLogger(DocumentLazyDaoTest.class);

	@Autowired
	private DocumentDao documentDao;
	
	@Test
	public void lazyException() {

		Document document = documentDao.get(3);
		try {
			document.getAuteur().getNom();
			fail("une erreur Lazy doit être levé");
		} catch (LazyInitializationException e) {
			assertEquals("could not initialize proxy - no Session", e
					.getMessage());
		}

	}

	@Test
	@Transactional(propagation = Propagation.SUPPORTS)
	public void noLazyException() {

		Document document = documentDao.get(3);
		assertNotNull(document.getAuteur().getNom());

	}

	@Test
	@SuppressWarnings("PMD.ConsecutiveLiteralAppends")
	public void collectionlazyException() {

		Document document = documentDao.get(3);
		try {
			
			assertCollection(document);
			fail("une erreur Lazy doit être levé");

		} catch (LazyInitializationException e) {

			StringBuffer message = new StringBuffer(150);
			message.append("failed to lazily initialize ");
			message.append("a collection of role: ");
			message.append("fr.urssaf.image.commons.dao.spring.exemple.modele.Document.etats, ");
			message.append("no session or session was closed");

			assertEquals(message.toString(), e.getMessage());
		}

	}

	@Test
	@Transactional(propagation = Propagation.SUPPORTS)
	public void collectionNolazyException() {

		Document document = documentDao.get(3);
		LOG.debug(document.getEtats().size());
		
		assertCollection(document);

	}
	
	private void assertCollection(Document document){
		assertEquals(2, document.getEtats().size());
	}


}
