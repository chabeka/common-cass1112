package fr.urssaf.image.commons.util.tempfile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link TempFileUtils} 
 */
@SuppressWarnings("PMD")
public final class TempFileUtilsTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(TempFileUtils.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaires de la méthode {@link TempFileUtils#getFullTemporaryFileName(String)}
    */
   @Test
   public void getFullTemporaryFileName() {
      
      String extension;
      String resultat;
      String tmpDir = System.getProperty("java.io.tmpdir");
      
      // Test général
      extension = "xml";
      resultat = TempFileUtils.getFullTemporaryFileName(extension);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
      assertTrue(
            "Vérifie que l'on soit dans le répertoire temporaire",
            resultat.startsWith(tmpDir));
      assertTrue(
            "Vérifie que l'extention soit correcte",
            resultat.endsWith(extension));
      
      // Test extension à null
      extension = null;
      resultat = TempFileUtils.getFullTemporaryFileName(extension);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
      
      // Test extension ne commençant pas par un "."
      extension = "txt";
      resultat = TempFileUtils.getFullTemporaryFileName(extension);
      assertNotNull("Vérifie que le résultat n'est pas nul",resultat);
      assertTrue("Vérifie que le résultat n'est pas vide",resultat.length()>0);
      assertTrue(
            "Vérifie que l'extention soit correcte",
            resultat.endsWith("." + extension));
      
   }
   
   
}
