package fr.urssaf.image.commons.util.compress.impl;

import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/**
 * Classe de décompression des fichier de type tar<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.UnCompressUtil#untar(String, String)}
 * 
 */
public class TarFileInputStream extends
      AbstractFileInputStream<TarArchiveInputStream> {

   /**
    * initialisation des fichiers de décompression
    * 
    * @param compressFileName
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    */
   public TarFileInputStream(String compressFileName, String repertory) {
      super(compressFileName, repertory);

   }

   @Override
   protected final TarArchiveInputStream getArchiveInputStream(
         InputStream inputStream) {
      return new TarArchiveInputStream(inputStream);
   }

}
