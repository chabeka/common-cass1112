package fr.urssaf.image.commons.util.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;


/**
 * Tests unitaires de la classe {@link FileWriterUtil}
 */
@SuppressWarnings("PMD")
public class FileWriterUtilTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(FileWriterUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test unitaire de la méthode {@link FileWriterUtil#write(String, String, String)}
    */
   @Test
   public void write() throws IOException {
      
      // Récupération de quelques propriétés système
      String tmpdir = System.getProperty("java.io.tmpdir") ;
      String lineSeparator = System.getProperty("line.separator");
      
      // Quelques paramètres
      String encoding = "UTF-8";

      // Construction du chemin complet du fichier à écrire
      SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()) ;
      String laDate = formatDate.format(new Date());
      String nomFichierTest = "FileUtilTest_write_" + laDate + ".txt";
      String fichierTest = FilenameUtils.concat(tmpdir, nomFichierTest);
      
      // Ecriture d'une première ligne
      FileWriterUtil.write("1ère ligne" + lineSeparator, fichierTest,encoding);
      try
      {

         // Vérification du contenu du fichier
         String actual = FileReaderUtil.read(fichierTest,encoding);
         String message = "Le texte relu ne correspond pas au texte écrit";
         String expected =  "1ère ligne" + lineSeparator;
         assertEquals(message,expected,actual);

         // Ecrit une 2ème ligne dans le fichier
         FileWriterUtil.write("2ème ligne" + lineSeparator, fichierTest,encoding);

         // Vérification du contenu du fichier
         actual = FileReaderUtil.read(fichierTest,encoding);
         message = "Le texte relu ne correspond pas au texte écrit";
         expected = 
            "1ère ligne" + lineSeparator + 
            "2ème ligne" + lineSeparator ;
         assertEquals(message,expected,actual);

      }
      finally
      {
         // Suppression du fichier
         File file = new File(fichierTest);
         file.delete();
      }
   }
   
}
