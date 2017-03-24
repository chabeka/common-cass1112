package fr.urssaf.image.commons.util.compress.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * Classe de compression des fichier de type zip<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.CompressUtil#zip(String, String)}
 */
public class ZipFileOutputStream extends
      AbstractFileOutputStream<ZipArchiveOutputStream> {

   private final String fileName;

   /**
    * initialisation des fichiers de compression
    * 
    * @param compressFileName
    *           nom de l'archive (avec une extension .zip)
    * @param fileName
    *           nom du fichier ou du répertoire à compresser
    */
   public ZipFileOutputStream(String compressFileName, String fileName) {
      super(compressFileName, fileName);
      this.fileName = fileName;
   }

   @Override
   protected final ZipArchiveOutputStream createOutputStream(
         BufferedOutputStream buff) {

      // création d'un flux d'écriture Zip
      ZipArchiveOutputStream out = new ZipArchiveOutputStream(buff);

      // spécifier la qualité de la compression 0..9
      out.setLevel(Deflater.BEST_COMPRESSION);

      return out;
   }

   @Override
   protected final void compressFile(File file, ZipArchiveOutputStream out)
         throws IOException {

      ZipArchiveEntry entry = new ZipArchiveEntry(file, ArchiveUtil.entry(
            this.fileName, file));

      ArchiveUtil.copy(file, out, entry);
   }

}
