package fr.urssaf.image.commons.util.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.fixture.FixtureClasse1;
import fr.urssaf.image.commons.util.fixture.FixtureClasse2;


/**
 * Tests unitaires de la classe {@link TestsUtils} 
 *
 */
@SuppressWarnings("PMD")
public class TestsUtilsTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(TestsUtils.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName}<br>
    * Version sans argument 
    */
   @Test
   public void getTemporaryFileName_Test1() {
      String resultat = TestsUtils.getTemporaryFileName();
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName(String)}<br>
    * Version avec 1 argument (préfixe)<br>
    * Cas de test : le préfixe est null
    */
   @Test
   public void getTemporaryFileName_Test2() {
      String prefixe = null;
      String resultat = TestsUtils.getTemporaryFileName(prefixe);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName(String)}<br>
    * Version avec 1 argument (préfixe)<br>
    * Cas de test : le préfixe est vide
    */
   @Test
   public void getTemporaryFileName_Test3() {
      String prefixe = "";
      String resultat = TestsUtils.getTemporaryFileName(prefixe);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName(String)}<br>
    * Version avec 1 argument (préfixe)<br>
    * Cas de test : le préfixe est renseigné
    */
   @Test
   public void getTemporaryFileName_Test4() {
      String prefixe = "le_prefixe";
      String resultat = TestsUtils.getTemporaryFileName(prefixe);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>prefixe.length());
      assertTrue("Vérifie que le résultat commence par le préfixe demandé",resultat.startsWith(prefixe));
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName(String,String)}<br>
    * Version avec 2 arguments (préfixe + suffixe)<br>
    * Cas de test : préfixe et suffixe non renseignés
    */
   @Test
   public void getTemporaryFileName_Test5() {
      String prefixe = null;
      String suffixe = null;
      String resultat = TestsUtils.getTemporaryFileName(prefixe,suffixe);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName(String,String)}<br>
    * Version avec 2 arguments (préfixe + suffixe)<br>
    * Cas de test : préfixe et suffixe vides
    */
   @Test
   public void getTemporaryFileName_Test6() {
      String prefixe = "";
      String suffixe = "";
      String resultat = TestsUtils.getTemporaryFileName(prefixe,suffixe);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
   }
   
   
   /**
    * Test de la méthode {@link TestsUtils#getTemporaryFileName(String,String)}<br>
    * Version avec 2 arguments (préfixe + suffixe)<br>
    * Cas de test : préfixe et suffixe renseignés
    */
   @Test
   public void getTemporaryFileName_Test7() {
      String prefixe = "le_prefixe";
      String suffixe = "le_suffixe";
      String resultat = TestsUtils.getTemporaryFileName(prefixe,suffixe);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>prefixe.length());
      assertTrue("Vérifie que le résultat commence par le préfixe demandé",resultat.startsWith(prefixe));
      assertTrue("Vérifie que le résultat se termine par le suffixe demandé",resultat.endsWith(suffixe));
   }
   
   
   
   /**
    * Tests unitaires de la méthode {@link TestsUtils#testConstructeurPriveSansArgument(Class)}
    */
   @Test
   public void testConstructeurPriveSansArgument()
   throws TestConstructeurPriveException {
    
      Boolean result;
      
      // Cas de plusieurs constructeurs privés, dont 1 sans paramètre
      result = TestsUtils.testConstructeurPriveSansArgument(FixtureClasse1.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
      
      // Cas d'un constructeur privé sans paramètre qui lève une exception
      Boolean bOk = false;
      try {
         result = TestsUtils.testConstructeurPriveSansArgument(FixtureClasse2.class);
         // assertTrue("Le constructeur privé n'a pas été trouvé",result);
      }
      catch (TestConstructeurPriveException ex) {
         // OK, l'exception est levée
         bOk = true;
      }
      if (!bOk) {
         fail("L'exception attendue n'a pas été levée");
      }
      
   }
   
}
