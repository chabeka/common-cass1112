package fr.urssaf.image.commons.webservice.generate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * Classe utilitaire pour les test de génération
 */
final class GenerateUtil {
    
   private final String path;
   
   protected GenerateUtil(String path){
      this.path = FilenameUtils.concat(SystemUtils.getJavaIoTmpDir()
            .getAbsolutePath(), path);
   }
   
   protected void init() throws IOException {

      // Création d'un répertoire temporaire
      File directory = new File(path);
      FileUtils.forceMkdir(directory);
      FileUtils.cleanDirectory(directory);

   }
   
   public String getPath(){
      return this.path;
   }

   protected void clean() throws IOException {

      // Nettoyage des fichiers créés
      File directory = new File(path);
      FileUtils.cleanDirectory(directory);
      FileUtils.deleteDirectory(directory);

   }
   
   protected void assertFile(String packagepath, String file) {

      String filePath = FilenameUtils.concat(FilenameUtils.concat(path,
            packagepath), file);
      assertTrue("le fichier " + filePath + " n'existe pas", new File(filePath)
            .exists());

   }

   protected void assertFiles(String packagepath, int files) {

      String filePath = FilenameUtils.concat(path, packagepath);

      assertEquals("le nombre de classes générées est faux", files, new File(
            filePath).list().length);

   }

}
