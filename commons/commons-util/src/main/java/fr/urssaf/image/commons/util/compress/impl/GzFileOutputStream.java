package fr.urssaf.image.commons.util.compress.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import fr.urssaf.image.commons.util.file.FileWriterUtil;

/**
 * Classe de compression des fichier de type gzip<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.CompressUtil#gzip(String, String)}
 */
public class GzFileOutputStream extends
      AbstractFileOutputStream<GzipCompressorOutputStream> {

   /**
    * initialisation des fichiers de compression
    * 
    * @param compressFileName
    *           nom de l'archive (avec une extension .gz)
    * @param fileName
    *           nom du fichier à compresser
    */
   public GzFileOutputStream(String compressFileName, String fileName) {
      super(compressFileName, fileName);
   }

   @Override
   protected final void compressFile(File file, GzipCompressorOutputStream out)
         throws IOException {
      FileWriterUtil.copy(file, out);

   }

   @Override
   protected final GzipCompressorOutputStream createOutputStream(
         BufferedOutputStream buff) throws IOException {
      return new GzipCompressorOutputStream(buff);
   }
}
