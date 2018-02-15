package fr.urssaf.image.commons.webservice.exemple.rpc.encoded.service;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.webservice.exemple.rpc.encoded.modele.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@SuppressWarnings({"PMD.JUnitAssertionsShouldIncludeMessage"})
public class DocumentServiceTest {

	@Autowired
	private DocumentService service;

	@Test
	public void allDocuments() throws RemoteException {

		List<Document> documents = service.allDocuments();

		assertEquals(3, documents.size());

		assertEquals("titre 0", documents.get(0).getTitre());
		assertEquals("titre 1", documents.get(1).getTitre());
		assertEquals("titre 2", documents.get(2).getTitre());
	}

	@Test
	public void getDocument() throws RemoteException {

		Document document = service.get(0);
		assertEquals("titre 0", document.getTitre());

	}

	@Test
	@Ignore
	public void saveDocument() throws RemoteException {

		Document document = new Document();
		document.setTitre("titre test");
		service.save(document);

		List<Document> documents = service.allDocuments();
		assertEquals(4, documents.size());
		document = service.get(3);
		assertEquals("titre test", document.getTitre());

	}
}
