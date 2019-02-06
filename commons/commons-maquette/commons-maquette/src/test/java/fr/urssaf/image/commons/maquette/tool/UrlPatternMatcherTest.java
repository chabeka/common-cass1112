package fr.urssaf.image.commons.maquette.tool;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.fixture.UriPatternTestItem;
import fr.urssaf.image.commons.maquette.fixture.UriPatternTestListe;
import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;

/**
 * Tests unitaires de la classe {@link UrlPatternMatcher}
 *
 */
@SuppressWarnings("PMD")
public class UrlPatternMatcherTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException 
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(UrlPatternMatcher.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaire de la méthode {@link UrlPatternMatcher#match}
    */
   @Test
   public void match() {
      
      // Récupère la liste des tests
      List<UriPatternTestItem> listeTests = UriPatternTestListe.getListeTests();
      
      // Test chaque pattern
      Boolean bActual;
      String message;
      for(UriPatternTestItem item:listeTests) {
         
         // Appel de la méthode à tester
         bActual = UrlPatternMatcher.match(item.getPattern(), item.getUri()) ;
         
         // Vérification du résultat
         if (item.getResultatAttendu()) {
            message = "L'URI devrait être matchée";
         }
         else {
            message = "L'URI ne devrait pas être matchée";
         }
         message += " (URI=\"%s\", Pattern=\"%s\"";
         message = String.format(message, item.getUri(),item.getPattern());
         assertEquals(message,item.getResultatAttendu(),bActual);
         
      }
            
   }
   
   
   /**
    * Test unitaire de la méthode {@link UrlPatternMatcher#matchOne}
    */
   @Test
   public void matchOne() {
      
      String uri = "/images/img1.gif";
      Boolean bExpected ;
      Boolean bActual;
      String[] patterns;
      
      // 1 seul pattern qui match
      patterns = new String[1];
      patterns[0] = "/images/*";
      bActual = UrlPatternMatcher.matchOne(patterns, uri);
      bExpected = true;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // 1 seul pattern qui ne match pas
      patterns = new String[1];
      patterns[0] = "/images2/*";
      bActual = UrlPatternMatcher.matchOne(patterns, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // aucun pattern avec null (test aux limites)
      bActual = UrlPatternMatcher.matchOne(null, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // aucun pattern avec tableau vide (test aux limites)
      patterns = new String[0];
      bActual = UrlPatternMatcher.matchOne(patterns, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // l'URI est null (test aux limites)
      patterns = new String[1];
      patterns[0] = "/images/*";
      bActual = UrlPatternMatcher.matchOne(patterns, null);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // 1 pattern qui match, un autre qui ne match pas
      patterns = new String[2];
      patterns[0] = "/images2/*";
      patterns[1] = "/images/*";
      bActual = UrlPatternMatcher.matchOne(patterns, uri);
      bExpected = true;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link UrlPatternMatcher#matchAll}
    */
   @Test
   public void matchAll() {
      
      String uri = "/images/img1.gif";
      Boolean bExpected ;
      Boolean bActual;
      String[] patterns;
      
      // 1 seul pattern qui match
      patterns = new String[1];
      patterns[0] = "/images/*";
      bActual = UrlPatternMatcher.matchAll(patterns, uri);
      bExpected = true;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // 1 seul pattern qui ne match pas
      patterns = new String[1];
      patterns[0] = "/images2/*";
      bActual = UrlPatternMatcher.matchAll(patterns, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // aucun pattern avec un null (test aux limites)
      bActual = UrlPatternMatcher.matchAll(null, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // aucun pattern avec tableau vide (test aux limites)
      patterns = new String[0];
      bActual = UrlPatternMatcher.matchAll(patterns, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // l'URI est null (test aux limites)
      patterns = new String[1];
      patterns[0] = "/images/*";
      bActual = UrlPatternMatcher.matchAll(patterns, null);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);

      // 1 pattern qui match, un autre qui ne match pas
      patterns = new String[2];
      patterns[0] = "/images2/*";
      patterns[1] = "/images/*";
      bActual = UrlPatternMatcher.matchAll(patterns, uri);
      bExpected = false;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
      // 2 pattern qui match
      patterns = new String[2];
      patterns[0] = "/images/*";
      patterns[1] = "*.gif";
      bActual = UrlPatternMatcher.matchAll(patterns, uri);
      bExpected = true;
      assertEquals("Le matchage n'a pas fonctionné correctement",bExpected,bActual);
      
   }
   
}
