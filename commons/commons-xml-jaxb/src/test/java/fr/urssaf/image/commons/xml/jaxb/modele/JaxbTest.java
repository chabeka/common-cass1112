package fr.urssaf.image.commons.xml.jaxb.modele;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD")
public class JaxbTest {

	private static final String REPERTORY;

	private static final Date PAST;

	private static final Date FUTURE;

	static {
		REPERTORY = SystemUtils.getJavaIoTmpDir().getAbsolutePath();

		Calendar past = Calendar.getInstance();
		past.set(1999, 11, 31);
		past = DateUtils.truncate(past, Calendar.DATE);

		PAST = past.getTime();

		Calendar future = Calendar.getInstance();
		future.set(2999, 11, 31);
		future = DateUtils.truncate(future, Calendar.DATE);

		FUTURE = future.getTime();

		System.setProperty("file.encoding", "UTF-8");

	}

	private ObjectFactory factory;

	@Before
	public void before() {
		factory = new ObjectFactory();
	}

	private DocumentType createDocument(int id, String titre, Date date) {
		DocumentType document = factory.createDocumentType();
		document.id = id;
		document.titre = titre;
		document.date = date;

		return document;
	}

	private AuteurType createAuteur(int id, String nom) {
		AuteurType auteur = factory.createAuteurType();
		auteur.id = id;
		auteur.nom = nom;

		return auteur;
	}

	private EtatType createEtat(int id, String libelle, Date date) {
		EtatType etat = factory.createEtatType();
		etat.id = id;
		etat.libelle = libelle;
		etat.date = date;

		return etat;
	}

	@Test
	public void marshall() throws JAXBException {

		Documents docs = factory.createDocuments();

		AuteurType auteur0 = this.createAuteur(0, "auteur 0é");
		AuteurType auteur1 = this.createAuteur(1, "auteur 1");

		EtatType etat0 = this.createEtat(0, "open", FUTURE);
		EtatType etat1 = this.createEtat(1, "close", PAST);
		EtatType etat2 = this.createEtat(2, "open", FUTURE);

		DocumentType document0 = this.createDocument(0, "titre 0", PAST);
		DocumentType document1 = this.createDocument(1, "titre 1", FUTURE);

		document0.setAuteur(auteur0);
		document1.setAuteur(auteur1);

		document0.getEtat().add(etat0);
		document0.getEtat().add(etat1);
		document1.getEtat().add(etat2);

		docs.getDocument().add(document0);
		docs.getDocument().add(document1);

		JAXBContext context = JAXBContext.newInstance(Documents.class);

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		marshaller.marshal(docs, System.out);

		File output = new File(FilenameUtils.concat(REPERTORY, "documents.xml"));

		marshaller.marshal(docs, output);

	}

	@Test
	public void unmarshall() throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(Documents.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		File input = new File("src/test/resources/documents.xml");
		Documents docs = (Documents) unmarshaller.unmarshal(input);

		assertDocument(docs.document.get(0), 0, "titre éloise", 0, "auteur 0",
				PAST);
		assertEtat(docs.document.get(0).getEtat(), 0, 0, "open", FUTURE);
		assertEtat(docs.document.get(0).getEtat(), 1, 1, "close", PAST);

		assertDocument(docs.document.get(1), 1, "titre 1", 1, "auteur 1",
				FUTURE);
		assertEtat(docs.document.get(1).getEtat(), 0, 2, "open", FUTURE);
	}

	private void assertDocument(DocumentType document, int id, String titre,
			int idAuteur, String nomAuteur, Date date) {

		assertEquals(Integer.valueOf(id), document.id);
		assertEquals(titre, document.titre);
		assertEquals(Integer.valueOf(idAuteur), document.auteur.id);
		assertEquals(nomAuteur, document.auteur.nom);
		assertEquals(date, document.getDate());
	}

	private void assertEtat(List<EtatType> etats, int index, int id,
			String libelle, Date date) {

		assertEquals(Integer.valueOf(id), etats.get(index).id);
		assertEquals(libelle, etats.get(index).libelle);
		assertEquals(date, etats.get(index).date);
	}

}
