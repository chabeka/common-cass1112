package fr.urssaf.image.commons.util.resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link ResourceUtil}
 */
@SuppressWarnings("PMD")
public class ResourceUtilTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(ResourceUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test unitaire de la méthode {@link ResourceUtil#getResourceFullPath(Object, String)}
    */
   @Test
   public void getResourceFullPath() throws URISyntaxException {
      
      // NB : on ne pourra vérifier exactement le résultat de la méthode
      // getResourceFullPath() car il est fonction du répertoire dans lequel
      // les sources sont compilées et testées
      
      String resourcePath ;
      String resultat ;
            
      resourcePath = "/compress/archive/archive_1.txt";
      resultat = ResourceUtil.getResourceFullPath(this, resourcePath);
      assertNotNull("La récupération du chemin complet de la ressource a échoué",resultat);
      
      resourcePath = "/nexistepas.txt";
      try {
         resultat = ResourceUtil.getResourceFullPath(this, resourcePath);
      }
      catch (NullPointerException ex) {
         // OK
      }
      
   }
   
}
