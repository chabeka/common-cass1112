package fr.urssaf.image.commons.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.resource.ResourceUtil;
import fr.urssaf.image.commons.util.tests.TestsUtils;
import fr.urssaf.image.commons.xml.XSDValidator.SAXParseExceptionType;

/**
 * Tests unitaires de la classe {@link XSDValidator}
 */
@SuppressWarnings("PMD")
public class XSDValidatorTest {

   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils
            .testConstructeurPriveSansArgument(XSDValidator.class);
      assertTrue("Le constructeur privé n'a pas été trouvé", result);
   }

   private String getXsdFile() throws URISyntaxException {
      return ResourceUtil.getResourceFullPath(this, "/info.xsd");
   }

   /**
    * Test unitaire de la méthode
    * {@link XSDValidator#validXMLWithDOM(String, String)}<br>
    * <br>
    * Cas de test : le document XML est valide<br>
    * <br>
    * Résultat attendu : la validation ne renvoie pas d'erreur
    */
   @Test
   public void validXMLWithDOM_Succes() throws URISyntaxException,
         ParserConfigurationException, IOException, SAXException {

      // Le fichier XML à valider
      String xmlFile = "src/test/resources/info_success.xml";

      // Le fichier du schéma XSD
      // NB : on a besoin du chemin complet
      String xsdFile = getXsdFile();

      // Appel de la méthode à tester;
      List<SAXParseExceptionType> erreurs = XSDValidator.validXMLWithDOM(
            xmlFile, xsdFile);

      // Vérification du résultat
      assertTrue("La validation du XML aurait dû réussir", erreurs.isEmpty());

   }

   /**
    * Test unitaire de la méthode
    * {@link XSDValidator#validXMLWithDOM(String, String)}<br>
    * <br>
    * Cas de test : le document XML n'est pas valide<br>
    * <br>
    * Résultat attendu : la validation renvoie des erreurs
    */
   @Test
   public void validXMLWithDOM_Echec() throws URISyntaxException,
         ParserConfigurationException, IOException, SAXException {

      // Le fichier XML à valider
      String xmlFile = "src/test/resources/info_failure.xml";

      // Le fichier du schéma XSD
      // NB : on a besoin du chemin complet
      String xsdFile = getXsdFile();

      // Appel de la méthode à tester;
      List<SAXParseExceptionType> erreurs = XSDValidator.validXMLWithDOM(
            xmlFile, xsdFile);

      // Vérifications générales

      XSDValidator.afficher(erreurs);

      // La liste des erreurs ne doit pas être vide
      assertEquals(
            "Le nombre d'erreurs trouvées dans le XML n'est pas celui attendu",
            1, erreurs.size());

      // Vérifie la 1ère erreur

      SAXParseExceptionType erreur = erreurs.get(0);

      assertEquals("erreur de type", "ERROR", erreur.getType().getLabel());

      assertEquals("erreur de ligne", 10, erreur.getException().getLineNumber());

      assertEquals(
            "erreur de message",
            "cvc-complex-type.2.4.d: Invalid content was found starting with element 'baliseinconnue'. No child element is expected at this point.",
            erreur.getException().getMessage());

   }

   /**
    * Test unitaire de la méthode
    * {@link XSDValidator#validXMLWithSAX(String, String)}<br>
    * <br>
    * Cas de test : le document XML est valide<br>
    * <br>
    * Résultat attendu : la validation ne renvoie pas d'erreur
    */
   @Test
   public void validXMLFileWithSAX_Success() throws URISyntaxException,
         SAXException, IOException, ParserConfigurationException {

      // Le fichier XML à valider
      String xmlFile = "src/test/resources/info_success.xml";

      // Le fichier du schéma XSD
      // NB : on a besoin du chemin complet
      String xsdFile = getXsdFile();

      // Appel de la méthode à tester;
      List<SAXParseExceptionType> erreurs = XSDValidator.validXMLFileWithSAX(
            xmlFile, xsdFile);

      // Vérification du résultat
      assertTrue("La validation du XML aurait dû réussir", erreurs.isEmpty());

   }

   @Test
   public void validXMLStringWithSAX_Success() throws URISyntaxException,
         SAXException, IOException, ParserConfigurationException {

      // Le fichier XML à valider
      String xmlFile = FileUtils.readFileToString(new File(
            "src/test/resources/info_success.xml"));

      // Le fichier du schéma XSD
      // NB : on a besoin du chemin complet
      String xsdFile = getXsdFile();

      // Appel de la méthode à tester;
      List<SAXParseExceptionType> erreurs = XSDValidator.validXMLStringWithSAX(
            xmlFile, xsdFile);

      // Vérification du résultat
      assertTrue("La validation du XML aurait dû réussir", erreurs.isEmpty());

   }

   /**
    * Test unitaire de la méthode
    * {@link XSDValidator#validXMLWithSAX(String, String)}<br>
    * <br>
    * Cas de test : le document XML n'est pas valide<br>
    * <br>
    * Résultat attendu : la validation renvoie des erreurs
    */
   @Test
   public void validXMLFileWithSAX_Echec() throws URISyntaxException,
         ParserConfigurationException, IOException, SAXException {

      // Le fichier XML à valider
      String xmlFile = "src/test/resources/info_failure.xml";

      // Le fichier du schéma XSD
      // NB : on a besoin du chemin complet
      String xsdFile = getXsdFile();

      // Appel de la méthode à tester;
      List<SAXParseExceptionType> erreurs = XSDValidator.validXMLFileWithSAX(
            xmlFile, xsdFile);

      assertSAX_failure(erreurs);

   }

   @Test
   public void validXMLStringWithSAX_Echec() throws URISyntaxException,
         ParserConfigurationException, IOException, SAXException {

      // Le fichier XML à valider
      String xmlFile = FileUtils.readFileToString(new File(
            "src/test/resources/info_failure.xml"));

      // Le fichier du schéma XSD
      // NB : on a besoin du chemin complet
      String xsdFile = getXsdFile();

      // Appel de la méthode à tester;
      List<SAXParseExceptionType> erreurs = XSDValidator.validXMLStringWithSAX(
            xmlFile, xsdFile);

      assertSAX_failure(erreurs);

   }

   private void assertSAX_failure(List<SAXParseExceptionType> erreurs) {

      // Vérifications générales

      XSDValidator.afficher(erreurs);

      // La liste des erreurs ne doit pas être vide
      assertEquals(
            "Le nombre d'erreurs trouvées dans le XML n'est pas celui attendu",
            1, erreurs.size());

      // Vérifie la 1ère erreur

      SAXParseExceptionType erreur = erreurs.get(0);

      assertEquals("erreur de type", "ERROR", erreur.getType().getLabel());

      assertEquals("erreur de ligne", 10, erreur.getException().getLineNumber());

      assertEquals(
            "erreur de message",
            "cvc-complex-type.2.4.d: Invalid content was found starting with element 'baliseinconnue'. No child element is expected at this point.",
            erreur.getException().getMessage());
   }

}
