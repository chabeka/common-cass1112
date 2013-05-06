/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;

/**
 * Classe permettant de réaliser des manipulations sur les fichiers
 * 
 */
public final class SaeFileUtils {

   /**
    * 
    */
   private static final int BUFFER_SIZE = 1024;
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaeFileUtils.class);

   private SaeFileUtils() {
   }

   /**
    * Créer un fichier gzip à partir d'un fichier dont le chemin est fourni
    * 
    * @param path
    *           chemin du fichier d'origine
    * @return le chemin du fichier zip
    */
   public static String generateGZip(String path) {
      String trcPrefix = "generateZip()";
      GZIPOutputStream outStream = null;
      InputStream inStream = null;
      String zipPath = path + ".gz";

      try {
         inStream = new FileInputStream(path);

         outStream = new GZIPOutputStream(new BufferedOutputStream(
               new FileOutputStream(zipPath)));

         byte[] rBytes = new byte[BUFFER_SIZE];
         int len;
         while ((len = inStream.read(rBytes)) != -1) {
            outStream.write(rBytes, 0, len);
         }

         return zipPath;

      } catch (FileNotFoundException exception) {
         throw new TraceExecutableRuntimeException(exception);

      } catch (IOException exception) {
         throw new TraceExecutableRuntimeException(exception);

      } finally {

         if (inStream != null) {
            try {
               inStream.close();
            } catch (IOException exception) {
               LOGGER.info("{} - impossible de fermer le flux lecture",
                     trcPrefix);
            }
         }

         if (outStream != null) {
            try {
               outStream.finish();
               outStream.close();
            } catch (IOException exception) {
               LOGGER.info("{} - impossible de fermer le flux d'écriture",
                     trcPrefix);
            }
         }
      }
   }

   /**
    * calcule le hash en SHA-1 du fichier passé en paramètre
    * 
    * @param file
    *           le fichier dont il faut extraire le hash
    * @return le hash du fichier
    * 
    */
   public static String calculateSha1(File file) {
      String trcPrefix = "calculateSha1()";
      FileInputStream fis = null;

      try {
         fis = new FileInputStream(file);
         return DigestUtils.shaHex(fis);

      } catch (IOException exception) {
         throw new TraceExecutableRuntimeException(exception);

      } finally {
         if (fis != null) {
            try {
               fis.close();
            } catch (IOException exception) {
               LOGGER.info("{} - impossible de fermer le flux de lecture",
                     trcPrefix);
            }
         }
      }

   }
}
