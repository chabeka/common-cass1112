package fr.urssaf.image.sae.webservices.xsd;



import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.urssaf.image.sae.webservices.xsd.XSDValidator.SAXParseExceptionType;


/**
 * Classe de Test du fichier XSD SaeService.xsd
 * 
 */
@SuppressWarnings({
   "PMD.TooManyMethods",
   "PMD.JUnitTestsShouldIncludeAssert",
   "PMD.JUnit4TestShouldUseTestAnnotation"})
public class XsdTest {
   /**
    * Adresse du fichier xsd de référence du WSDL
    */
   private static final String FIC_XSD = "src/test/resources/SaeService.xsd";

   /**
    * Le répertoire où se trouve les fichiers XML de test
    */
   private static final String REP_TESTS_XSD = "src/test/resources/tests_xsd/";

   private static final Logger LOGGER = Logger.getLogger(XsdTest.class);

   /**
    * Donne le chemin du fichier XML
    * 
    * @param casTest
    *           Nom du cas de test
    * 
    * @return Chemin du fichier XML à tester
    */
   private String getFileName(String casTest) {
      return REP_TESTS_XSD + casTest + ".xml";
   }

   /**
    * Renvoie le chemin complet du Xsd
    * 
    * @return Le chemin complet du Xsd
    * 
    * @throws FileNotFoundException Si le XSD est introuvable
    */
   private String getFullPathXSD() throws FileNotFoundException {
      File fic = new File(FIC_XSD);
      if (!fic.exists()) {
         throw new FileNotFoundException("Fichier XSD introuvable");
      }
      return fic.getAbsolutePath();
   }

   /**
    * Exécute un test dont on attend la réussite
    * 
    * @param numero
    *           le numéro du test avec les ex : 002
    */
   private void testReussite(String numero) {
      List<SAXParseExceptionType> erreurs = null;
      try {
         // Appel de la méthode à tester;
         erreurs = XSDValidator.validXMLWithDOM(getFileName("cas" + numero),
               getFullPathXSD());
      } catch (Exception ex) {
         fail("Exception innattendue dans csTest" + numero + " : " + ex.getMessage());
      }
      if ( ! erreurs.isEmpty() ) {
         LOGGER.debug("=");
         LOGGER.debug("=== ERREUR dans casTest" + numero + "===");
         XSDValidator.afficher(erreurs);
      }

      assertTrue("Il y a des erreurs (voir Console)!", erreurs.isEmpty());

   }

   private void testEchec(String numero) {
      List<SAXParseExceptionType> erreurs = null;
      try {
         // Appel de la méthode à tester;
         erreurs = XSDValidator.validXMLWithDOM(getFileName("cas" + numero),
               getFullPathXSD());
      } catch (Exception ex) {
         fail("Exception innattendue dans casTest" + numero + " : "
               + ex.getMessage());
      }
      if (! erreurs.isEmpty()) {
         LOGGER.debug("=");
         LOGGER.debug("=== ERREUR dans casTest" + numero + "Echec ===");
         XSDValidator.afficher(erreurs);

      }

      assertFalse("Une erreur inconnue (voir Console) !",   erreurs.isEmpty());
   }

   /* LES TESTS */

   /**
    * Archivage unitaire satandard
    */
   @Test
   public void casTest001() {
      testReussite("001");
   }

   /**
    * Année hors cadre
    */
   @Test
   public void casTest002Echec() {
      testEchec("002");
   }

   /**
    * Inversion Jour et Mois dans la date
    */
   @Test
   public void casTest003Echec() {
      testEchec("003");
   }

   /**
    * documents n'est pas au pluriel
    */
   @Test
   public void casTest004Echec() {
      testEchec("004");
   }

   /**
    * Pass de nom de fichier ou de répertoir à la fin
    */
   @Test
   public void casTest005Echec() {
      testEchec("005");
   }

   /**
    * Une requête de consultation OK (Test Id en Hexadecimal)
    */
   @Test
   public void casTest006() {
      testReussite("006");
   }

   /**
    * Requête avec hexa porté en majuscules
    */
   @Test
   public void casTest007() {
      testReussite("007");
   }
   
   /**
    * Manque un chiffre dans l'ID
    */
   @Test
   public void casTest008Echec() {
      testEchec("008");
   }
   
   /**
    * Lettre intruse dans l'ID
    */
   @Test 
   public void casTest009Echec() {
      testEchec("009");
   }
   
   /**
    * Archivage unitaire avec contenu base64 et liste de métadonnées
    */
   @Test
   public void casTest11() {
      testReussite("011");
   }
   
   /**
    * Un des codes des métadonnées est vide !
    */
   @Test
   public void casTest12() {
      testEchec("012");
   }
   
   /**
    * Une des valeur des métadonnées est vide. C'est OK !
    */
   @Test
   public void casTest13() {
      testReussite("013");
   }
   
   /**
    * Un code en trop dans les métadonnées
    * @return
    */
   @Test
   public void casTest14() {
      testEchec("014");
   }
   
   /**
    * Il manque un code dans une méta donnée
    */
   @Test
   public void casTest15() {
      testEchec("015");
   }
   
   /**
    * Une valeur en trop dans la métadonnée
    */
   @Test 
   public void casTest16() {
      testEchec("016");
   }
   
   /**
    * Un valeur absente dans une metadonnée
    */
   @Test
   public void casTest17() {
      testEchec("017");
   }
   
   /**
    * Pas de métadonnée dans la liste : C'est OK !
    */
   @Test
   public void casTest18() {
      testReussite("018");
   }
   
   /**
    * Une recherche avec retour de métadonnées
    */
   @Test
   public void casTest19() {
      testReussite("019");
   }
   
   /**
    * Une recherche sans code c'est permis
    */
   @Test
   public void casTest20() {
      testReussite("020");
   }
   
   /**
    * Chaine de recherche vide ! 
    */
   @Test
   public void casTest21(){
      testEchec("021");
   }
   
   /**
    * Un résultat de recherche crédible
    */
   @Test
   public void casTest22(){
      testReussite("022");
   }
   
   /**
    * Les métadonnées sont manquantes sur un résultat
    */
   @Test
   public void casTest23(){
      testEchec("023");
   }
   
   /**
    * Un uuid est manquant
    */
   @Test
   public void casTest24() {
      testEchec("024");
   }
   
   /**
    * Pas de résultats trouvé (collection resultats vide) => OK !
    */
   @Test
   public void casTest25() {
      testReussite("025");
   }
   
   /**
    * Réponse d'archivage unitaire standard
    */
   @Test
   public void casTest26() {
      testReussite("026");
   }
   /**
    * Réponses d'archivage unitaire multiples
    */
   @Test
   public void casTest27() {
      testEchec("027");
   }
   
   /**
    * Pas de réponse ! C'est Ko !
    */
   @Test
   public void casTest28() {
      testEchec("028");
   }
   
   /**
    * Demande d'archivage de masse standard.
    */
   @Test
   public void casTest29() {
      testReussite("029");
   }
   
   /**
    * Demande d'archivage de masse sans sommaire : impossible !
    */
   @Test
   public void casTest30() {
      testEchec("030");
   }
   
   /**
    * Demande avec sommaire doublé
    */
   @Test
   public void casTest31() {
      testEchec("031");
   }
   
   /**
    * Une demande de consultation standard
    */
   @Test
   public void casTest32() {
      testReussite("032");
   }
   
   /**
    * Une demande avec l'urlConsultation directe doublée => echec
    */
   @Test
   public void casTest33() {
      testEchec("033");
   }
   
   /**
    * Test avec l'id d'Archivage doublé =>echec
    */
   @Test
   public void casTest34() {
      testEchec("034");
   }
   
   /**
    * L'id d'archive est absent
    */
   @Test
   public void casTest35() {
      testEchec("035");
   }
   
   /**
    * Echec à cause de l'objet numérique doublé
    */
   @Test
   public void castest38(){
      testEchec("038");
   }
   
   /**
    * Echec car metadonnées doublonnées
    */
   @Test
   public void casTest39() {
      testEchec("039");
   }
   
   /**
    * Echec car pas de méta données
    */
   @Test
   public void casTest40() {
      testEchec("040");
   }
   /**
    * Echec car pas d'objet numérique
    */
   @Test 
   public void casTest41() {
      testEchec("041");
   }
   

   /**
    * Demande d'archivage de masse avec une mauvaise URL ecde pour le sommaire.
    */
   @Test
   public void casTest42() {
      testEchec("042");
   }
   
   /**
    * Demande d'archivage de unitaire avec une mauvaise URL ecde (le numéro du traitement est incorrect).
    */
   @Test
   public void casTest43() {
      testEchec("043");
   }
   
   
}
