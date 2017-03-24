package fr.urssaf.image.commons.util.compress.impl;

import java.io.InputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

/**
 * Classe de décompression des fichier de type zip<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.UnCompressUtil#unzip(String, String)}
 * 
 */
public class ZipFileInputStream extends
      AbstractFileInputStream<ZipArchiveInputStream> {

   /**
    * initialisation des fichiers de décompression
    * 
    * @param compressFileName
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    */
   public ZipFileInputStream(String compressFileName, String repertory) {
      super(compressFileName, repertory);
   }

   @Override
   protected final ZipArchiveInputStream getArchiveInputStream(
         InputStream inputStream) {
      return new ZipArchiveInputStream(inputStream);
   }
}
