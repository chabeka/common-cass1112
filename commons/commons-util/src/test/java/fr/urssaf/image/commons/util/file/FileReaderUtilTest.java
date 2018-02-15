package fr.urssaf.image.commons.util.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link FileReaderUtil}
 */
@SuppressWarnings("PMD")
public class FileReaderUtilTest {

   
   private final static String FILE_LECTURE = "src/test/resources/file/file_test.txt";

   private final static String CRLF_FILE_LECTURE = "\r\n";
   
   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(FileReaderUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   private void assertFile(String text) {
      
      String message = "La lecture du fichier a échoué";
      
      String expected = 
         "1ère ligne du fichier" + 
         CRLF_FILE_LECTURE +
         "2ème ligne du fichier" + 
         CRLF_FILE_LECTURE + 
         "dernière ligne du fichier";
      
      String actual = text;
      
      assertEquals(message,expected,actual);
      
   }
   
   /**
    * Test unitaire de la méthode {@link FileReaderUtil#read(String, String)}
    */
   @Test
   public void read_V1() throws IOException {
      
      String text = FileReaderUtil.read(FILE_LECTURE,"UTF-8");
      
      assertFile(text);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link FileReaderUtil#read(File, String)}
    */
   @Test
   public void read_V2() throws IOException {
      
      File file = new File(FILE_LECTURE);
      
      String text = FileReaderUtil.read(file,"UTF-8");
      
      assertFile(text);
      
   }
   
}
