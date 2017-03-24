package fr.urssaf.image.commons.util.compress;

import java.io.IOException;

import fr.urssaf.image.commons.util.compress.impl.GzFileInputStream;
import fr.urssaf.image.commons.util.compress.impl.TarFileInputStream;
import fr.urssaf.image.commons.util.compress.impl.TarGzFileInputStream;
import fr.urssaf.image.commons.util.compress.impl.ZipFileInputStream;

/**
 * Cette classe permet le désarchivage de
 * <ul>
 * <li>zip</li>
 * <li>tar</li>
 * <li>tgz</li>
 * <li>gz</li>
 * </ul>
 * 
 * @author Bertrand BARAULT
 * 
 */
public final class UnCompressUtil {

   private UnCompressUtil() {

   }

   /**
    * décompression d'un fichier zip
    * <p>
    * ex: unzip d'un fichier doc.zip dans /etc/doc<br>
    * <code>UnCompressUtil.unzip("/tmp/doc.zip", "/etc/doc"); </code>
    * </p>
    * 
    * @param zipFile
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void unzip(String zipFile, String repertory)
         throws IOException {

      ZipFileInputStream inputStream = new ZipFileInputStream(zipFile,
            repertory);

      inputStream.uncompress();

   }

   /**
    * décompression d'un fichier tar
    * <p>
    * ex: untar d'un fichier doc.tar dans /etc/doc<br>
    * <code>UnCompressUtil.untar("/tmp/doc.tar", "/etc/doc"); </code>
    * </p>
    * 
    * @param tarFile
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void untar(String tarFile, String repertory)
         throws IOException {

      TarFileInputStream inputStream = new TarFileInputStream(tarFile,
            repertory);

      inputStream.uncompress();

   }

   /**
    * décompression d'un fichier gzip
    * <p>
    * ex: untar d'un fichier doc.tar dans /etc/doc<br>
    * <code>UnCompressUtil.ungz("/tmp/doc.gz", "/etc/doc"); </code>
    * </p> 
    * 
    * @param gzFile
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void ungz(String gzFile, String repertory)
         throws IOException {

      GzFileInputStream inputStream = new GzFileInputStream(gzFile,
            repertory);

      inputStream.uncompress();

   }

   /**
    * décompression d'un fichier tgz
    * <p>
    * ex: untar d'un fichier doc.tgz dans /etc/doc<br>
    * <code>UnCompressUtil.untgz("/tmp/doc.tgz", "/etc/doc"); </code>
    * </p> 
    * 
    * @param tgzFile
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void untgz(String tgzFile, String repertory)
         throws IOException {

      TarGzFileInputStream inputStream = new TarGzFileInputStream(tgzFile,
            repertory);

      inputStream.uncompress();

   }
}
