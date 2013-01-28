import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.pdfa.exception.FileExisteException;
import fr.urssaf.image.pdfa.exception.NoAnalysisFolderOrLogFolderException;
import fr.urssaf.image.pdfa.exception.NotAFileException;
import fr.urssaf.image.pdfa.exception.NotAFolderException;
import fr.urssaf.image.pdfa.validator.ValidatePDF;

/**
 * Tests unitaires de la classe ValidatePDF
 */
@SuppressWarnings("PMD")
public class ValidatePDFTest {
   private static final Logger LOGGER = LoggerFactory
   .getLogger(ValidatePDFTest.class);
   
   private String uuid = UUID.randomUUID().toString();
   private File repTemp = new File(FileUtils.getTempDirectoryPath(),uuid);
   
   @After
   public void clean(){     
      if(repTemp.exists()){
         FileUtils.deleteQuietly(repTemp);
      }
   }
   
   private File getFileFromResource(String ressourcePath) {
      try {
         return new File(this.getClass().getResource(ressourcePath).toURI()
               .getPath());
      } catch (URISyntaxException ex) {
         throw new RuntimeException(ex);
      }
   }
   //On vérifie qu'on sort bien en exception si les paramètres d'entrés ne sont pas fournis
   @Test(expected = NoAnalysisFolderOrLogFolderException.class)   
   public void validateWithNoParameter() throws IOException, NoAnalysisFolderOrLogFolderException, NotAFileException, FileExisteException, NotAFolderException{
      String[] args = new String[]{};
      ValidatePDF validate = new ValidatePDF();
      validate.main(args);

      
   }
   
   @Test(expected = NotAFolderException.class)  
   public void validateWithInvalidPdfFolder() throws IOException, NoAnalysisFolderOrLogFolderException, NotAFileException, FileExisteException, NotAFolderException{
      // on spécifie un chemin vers les fichiers PDF à analyser qui n'existe pas et on vérifie l'exception obtenue
      String[] args = new String[]{ repTemp.getAbsolutePath()+"abcdefg",repTemp.getAbsolutePath()+"abcdefgLog"};
      ValidatePDF validate = new ValidatePDF();
      validate.main(args);
      
   }
   
   // on vérifie la sortie en exception si le fichier log existe
   @Test
   public void validateWithExistingLogFile() throws IOException, NoAnalysisFolderOrLogFolderException, NotAFileException, FileExisteException, NotAFolderException{
      // on créé un fichier sur le disque pour les besoins du teste
      FileUtils.write(new File(repTemp.getAbsolutePath(),"log.txt"), "test");
      // on passe en parametre le fichier créé
      String[] args = new String[]{repTemp.getAbsolutePath(),new File(repTemp.getAbsolutePath(),"log.txt").getAbsolutePath()};
      ValidatePDF validate = new ValidatePDF();
      try{
      validate.main(args);
      // echec du test
      Assert.fail();
      }catch(FileExisteException ex){
         // on nettoie les fichiers crées         
         Assert.assertTrue(FileUtils.deleteQuietly(repTemp));
      }
      
   }
   
   @Test
   public void validateWithGivenPdfFile() throws IOException, NoAnalysisFolderOrLogFolderException, NotAFileException, FileExisteException, NotAFolderException{
      
      // on récupère le chemin vers le fichier PDF qui se trouve dans test/ressource
      File file = getFileFromResource("/E-frutiger.pdf");
      String[] args = new String[]{file.getAbsolutePath(),new File(repTemp.getAbsolutePath(),"log.txt").getAbsolutePath()};
      ValidatePDF validate = new ValidatePDF();
      // on valide le fichier
      validate.main(args);
      // on compare le fichier temoin avec celui obtenu pour vérifier que les log ont bien étaient écrit
      Assert.assertTrue(FileUtils.contentEqualsIgnoreEOL(getFileFromResource("\\log.txt"), new File(repTemp.getAbsolutePath(),"log.txt"),null));
      FileUtils.deleteQuietly(repTemp);
   }
   
   @Test
   public void validateWithGivenPdfFolder() throws IOException, NoAnalysisFolderOrLogFolderException, NotAFileException, FileExisteException, NotAFolderException{
      // on récupère le chemin vers le répertoire test/ressource
      File file = getFileFromResource("/");
      String[] args = new String[]{file.getAbsolutePath(),new File(repTemp.getAbsolutePath(),"log.txt").getAbsolutePath()};
      ValidatePDF validate = new ValidatePDF();
      // on valide le fichier
      validate.main(args);
      // on compare le fichier temoin avec celui obtenu pour vérifier que les log ont bien étaient écrit
      Assert.assertTrue(FileUtils.contentEqualsIgnoreEOL(getFileFromResource("\\log-2.txt"), new File(repTemp.getAbsolutePath(),"log.txt"),null));
      FileUtils.deleteQuietly(repTemp);
   }
   


}
