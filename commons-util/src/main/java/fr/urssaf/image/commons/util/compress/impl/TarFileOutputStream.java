package fr.urssaf.image.commons.util.compress.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

/**
 * Classe de compression des fichier de type tar<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.CompressUtil#tar(String, String)}
 */
public class TarFileOutputStream extends
      AbstractFileOutputStream<TarArchiveOutputStream> {

   private final TarOutputStream tarOutputStream;

   /**
    * initialisation des fichiers de compression
    * 
    * @param compressFileName
    *           nom de l'archive (avec une extension .tar)
    * @param fileName
    *           nom du fichier ou du répertoire à compresser
    */
   public TarFileOutputStream(String compressFileName, String fileName) {
      super(compressFileName, fileName);
      tarOutputStream = new TarOutputStream(fileName);
   }

   @Override
   protected final void compressFile(File file, TarArchiveOutputStream out)
         throws IOException {

      tarOutputStream.compressFile(file, out);

   }

   @Override
   protected final TarArchiveOutputStream createOutputStream(
         BufferedOutputStream buff) {
      return tarOutputStream.createOutputStream(buff);
   }
}
