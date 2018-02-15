package fr.urssaf.image.commons.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.file.FileReaderUtil;
import fr.urssaf.image.commons.util.tempfile.TempFileUtils;
import fr.urssaf.image.commons.util.tests.TestsUtils;


/**
 * Tests unitaires de la classe {@link JDOMUtil}
 */
@SuppressWarnings("PMD")
public class JDOMUtilTest {

   
	private static final String FILE = "bibliotheque_jdom.xml";
	
	
	private static final String XML_CRLF = "\r\n";

	
	private Document getDocumentTestAsObjDocument() {

      Element bibliotheque = new Element("bibliotheque");

      // livre n°0
      Element livre0 = createLivre("romantisme du XXe", "Madame Bovary",
            "Gustave Flaubert");
      bibliotheque.addContent(livre0);

      // livre n°1
      Element livre1 = createLivre("naturalisme", "l'argent", "Emile Zola");
      bibliotheque.addContent(livre1);

      // Commentaire
      Comment comment = new Comment("on place ici un commentaire");
      bibliotheque.addContent(comment);

      // CDATA
      CDATA cData = new CDATA("caractères spéciaux:< > ' & + é @ £ $");
      bibliotheque.addContent(cData);

      Document document = new Document(bibliotheque);

      return document;
   }
   

   private Element createLivre(String style, String titre, String auteur) {

      Element livre = new Element("livre");
      livre.setAttribute("style", style);
      Attribute type = new Attribute("type", "poche");
      livre.setAttribute(type);

      Element titreElement = new Element("titre");
      titreElement.addContent(titre);
      livre.addContent(titreElement);

      Element auteurElement = new Element("auteur");
      auteurElement.addContent(auteur);
      auteurElement.setAttribute("nat", "FR");
      livre.addContent(auteurElement);

      return livre;
   }

   
   private String getDocumentTestAsXmlString() {
      
      StringBuilder sbExpected = new StringBuilder();
      sbExpected.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + XML_CRLF);
      sbExpected.append("<bibliotheque>" + XML_CRLF);
      sbExpected.append("  <livre style=\"romantisme du XXe\" type=\"poche\">" + XML_CRLF);
      sbExpected.append("    <titre>Madame Bovary</titre>" + XML_CRLF);
      sbExpected.append("    <auteur nat=\"FR\">Gustave Flaubert</auteur>" + XML_CRLF);
      sbExpected.append("  </livre>" + XML_CRLF);
      sbExpected.append("  <livre style=\"naturalisme\" type=\"poche\">" + XML_CRLF);
      sbExpected.append("    <titre>l'argent</titre>" + XML_CRLF);
      sbExpected.append("    <auteur nat=\"FR\">Emile Zola</auteur>" + XML_CRLF);
      sbExpected.append("  </livre>" + XML_CRLF);
      sbExpected.append("  <!--on place ici un commentaire-->" + XML_CRLF);
      sbExpected.append("  <![CDATA[caractères spéciaux:< > ' & + é @ £ $]]>" + XML_CRLF);
      sbExpected.append("</bibliotheque>" + XML_CRLF);
      sbExpected.append(XML_CRLF);
      
      return sbExpected.toString();
      
   }
	
	
	/**
	 * Test unitaire du constructeur privé, pour le code coverage
	 */
	@Test
	public void constructeurPrive() throws TestConstructeurPriveException {
	   Boolean result = TestsUtils.testConstructeurPriveSansArgument(JDOMUtil.class);
	   assertTrue("Le constructeur privé n'a pas été trouvé",result);
	}
	
	
	/**
	 * Test unitaire de la méthode {@link JDOMUtil#writeToFile(Document, File)}
	 */
	@Test
	public void writeToFile() throws IOException {
	   
	   // Création d'un document XML de test
	   Document document = getDocumentTestAsObjDocument();
	   
	   // Obtention d'un nom de fichier temporaire
	   String fichierTest = TempFileUtils.getFullTemporaryFileName("xml");
	   File file = new File(fichierTest);
	   
	   // Ecriture du document
	   String encoding = CharEncoding.UTF_8;
	   JDOMUtil.writeToFile(document, file,encoding);
	   try {
	      
	      // Lecture du fichier
	      String actual = FileReaderUtil.read(file, encoding);
	      
	      // Vérification
	      String documentAsString = getDocumentTestAsXmlString();
	      assertEquals(
	            "Le contenu du fichier XML est incorrect",
	            documentAsString,
	            actual);
	      
	   }
	   finally {
	      FileUtils.deleteQuietly(file);
	   }

	}
	
		
	@Test
   public void writeToString() throws IOException {

	   // Création d'un document XML de test
      Document document = getDocumentTestAsObjDocument();
      
      // Appel de la méthode à tester
      String actual = JDOMUtil.writeToString(document,CharEncoding.UTF_8);
      
      // Vérification du résultat
      String documentAsString = getDocumentTestAsXmlString();
      assertEquals(
            "Le contenu du fichier XML est incorrect",
            documentAsString,
            actual);

   }


	@SuppressWarnings("unchecked")
   @Test
	public void read() throws JDOMException, IOException {

		File file = new File("src/test/resources/" + FILE);
		Document document = JDOMUtil.readFromFile(file, CharEncoding.UTF_8);
		
		Element element = document.getRootElement(); 
		
		assertNotNull("Le noeud racine n'a pas été trouvé",element);

		assertEquals("Problème sur le noeud racine","bibliotheque", element.getName());

		List<Element> livres = element.getChildren();

		Element livre1 = livres.get(0);
		assertEquals("Problème sur le 1er noeud livre","livre", livre1.getName());
		assertLivre(livre1, "romantisme du XXe", "Madame Bovary","Gustave Flaubert");

		Element livre2 = livres.get(1);
		assertEquals("Problème sur le 2ème noeud livre","livre", livre2.getName());
		assertLivre(livre2, "naturalisme", "l'argent", "Emile Zola");

		assertEquals("il y a plus de livres que prévus", 2, livres.size());

	}

	
	@SuppressWarnings("unchecked")
   private void assertLivre(Element livre, String style, String titre,
			String auteur) {

		// on test les attributs style et type
		List<Attribute> attributes = livre.getAttributes();

		Attribute styleAttribut = attributes.get(0);
		assertEquals(
		      "Mauvais attribut",
		      "style", 
		      styleAttribut.getName());
		assertEquals(
		      "Problème sur la valeur de l'attribut " + "style",
		      style, 
		      styleAttribut.getValue());

		Attribute typeAttribut = attributes.get(1);
		assertEquals(
		      "Mauvais attribut", 
		      "type" ,
		      typeAttribut.getName());
		assertEquals(
		      "Problème sur la valeur de l'attribut " + "type", 
		      "poche", 
		      typeAttribut.getValue());

		assertEquals(
		      "il y a plus d'attributs que prévus", 
		      2, 
		      attributes.size());

		List<Element> elements = livre.getChildren();
		// on test le titre
		Element titreElement = elements.get(0);
		assertEquals("Problème sur le titre du livre",titre, titreElement.getValue());

		// on test l'auteur
		Element auteurElement = elements.get(1);
		assertEquals("L'auteur est incorrect",auteur, auteurElement.getValue());

		assertEquals("il y a plus de caractéristiques que prévues", 2, elements
				.size());

	}

	
	@SuppressWarnings("unchecked")
   @Test
	public void readFromStringUTF8Test() throws JDOMException, IOException
	{
	   
	   String xml = 
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
         XML_CRLF + 
         "<bibliotheque>" + 
         XML_CRLF +
         "  <livre>leLivre éè</livre>" +
         XML_CRLF +
         "</bibliotheque>" + 
         XML_CRLF + 
         XML_CRLF;
	   
	   Document document = JDOMUtil.readFromStringUTF8(xml);
	   
	   // Vérification sur le document
	   assertNotNull("Echec de la transformation de la String xml en objet Document",document);
	   
	   // Vérification sur le noeud racine
	   Element noeudRacine = document.getRootElement();
	   assertNotNull("Noeud racine non trouvé",noeudRacine);
	   assertEquals(
	         "Le noeud racine devrait être un noeud <bibliotheque>",
	         "bibliotheque",
	         noeudRacine.getName());
	   
	   // Vérifie qu'il y a un moins 1 livre
	   List<Element> livres = noeudRacine.getChildren();
	   assertNotNull("On aurait du trouvé 1 noeud <livre>",livres);
	   assertEquals("On aurait du trouvé 1 noeud <livre>",false,livres.isEmpty());
	   
	   // Vérification sur le livre
	   Element livre = livres.get(0);
	   assertNotNull("On aurait du trouvé 1 noeud <livre>",livres);
	   assertEquals("Le noeud livre devrait être un noeud <livre>","livre",livre.getName());
	   assertEquals("La valeur du noeud livre est incorrecte","leLivre éè",livre.getValue());
	   
	}

}
