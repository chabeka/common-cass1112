package fr.urssaf.image.sae.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.util.Assert;

/**
 * Classe utilitaire pour travailler dans le répertoire temporaire de l'OS
 * 
 * 
 */
public final class TempFileUtils {

   private TempFileUtils() {

   }

   private static final File TMP_FILE;

   static {

      TMP_FILE = SystemUtils.getJavaIoTmpDir();

   }

   /**
    * Nettoyage d'un répertoire situé dans le répertoire temporaire de l'OS
    * 
    * @param tempFile
    *           chemin relatif du répertoire à nettoyer dans le répertoire
    *           temporaire de l'OS
    * @throws IOException
    *            exception levée lors du nettoyage
    */
   public static void cleanTempFile(String tempFile) throws IOException {

      Assert.hasText(tempFile);

      File file = new File(FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            tempFile));

      FileUtils.cleanDirectory(file);

   }

   /**
    * Création d'un répertoire situé dans le répertoire temporaire de l'OS
    * 
    * @param tempFile
    *           chemin relatif du répertoire à créer dans le répertoire
    *           temporaire de l'OS
    * @return le répertoire crée dans le temporaire de l'OS
    * @throws IOException
    *            exception levée lors de la création
    */
   public static File createTempFile(String tempFile) throws IOException {

      Assert.hasText(tempFile);

      File file = new File(FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            tempFile));

      FileUtils.forceMkdir(file);

      return file;
   }

   /**
    * Copie un fichier dans une arborescence dans le répertoire temporaire de
    * l'OS
    * 
    * @param srcFile
    *           chemin absolu du fichier à copier
    * @param destFilename
    *           arborescence du fichier copié dans le répertoire temporaire de
    *           l'OS
    * @throws IOException
    *            exception levée lors de la copie
    */
   public static void copyTempFile(File srcFile, String destFilename)
         throws IOException {

      Assert.notNull(srcFile);
      Assert.hasText(destFilename);

      File destFile = new File(TMP_FILE.getAbsolutePath(), destFilename);

      FileUtils.copyFile(srcFile, destFile, false);
   }

}
