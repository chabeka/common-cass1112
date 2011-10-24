package fr.urssaf.image.sae.webservices.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Classe de manipulation du répertoire ECDE.<br>
 * <br>
 * Le répertoire ECDE se trouve dans le répertoire 'ecde' dans le répertoire
 * temporaire de l'OS
 * 
 * 
 */
public final class EcdeManager {

   private static final File ECDE_REPERTORY;

   static {

      ECDE_REPERTORY = new File(FilenameUtils.concat(SystemUtils
            .getJavaIoTmpDir().getAbsolutePath(), "ecde"));

      try {
         FileUtils.forceMkdir(ECDE_REPERTORY);
      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }
   }

   private EcdeManager() {

   }

   /**
    * nettoyage du répertoire ECDE
    * 
    * @throws IOException
    *            échec du nettoyage
    */
   public static void cleanEcde() throws IOException {

      FileUtils.cleanDirectory(ECDE_REPERTORY);
   }

   /**
    * ajoute un fichier dans l'ECDE
    * 
    * @param srcFile
    *           fichier à ajouter
    * @param destFilename
    *           emplacement dans l'ECDE
    * @throws IOException
    *            échec de la copie
    */
   public static void copyFile(File srcFile, String destFilename)
         throws IOException {

      File destFile = new File(ECDE_REPERTORY.getAbsolutePath(), destFilename);
      FileUtils.copyFile(srcFile, destFile);
   }

}
