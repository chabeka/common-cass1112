package fr.urssaf.image.commons.util.compress.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CheckedOutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Classe de compression des fichier de type tgz<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.CompressUtil#tgz(String, String)}
 */
public class TarGzFileOutputStream {

   private final String tgz;

   private final TarOutputStream tarOutputStream;

   /**
    * initialisation des fichiers de compression
    * 
    * @param tgz
    *           nom de l'archive compressée (avec une extension .tgz)
    * @param fileName
    *           nom du fichier à compresser
    */
   public TarGzFileOutputStream(String tgz, String fileName) {
      this.tgz = tgz;
      tarOutputStream = new TarOutputStream(fileName);
   }

   /**
    * initialisation des filtres
    * 
    * @param extensions
    *           filtre sur les extensions
    */
   public final void setExtensions(String... extensions) {
      tarOutputStream.setExtensions(extensions);
   }

   /**
    * Méthode de compression
    * 
    * @return checksum en crc32 du tgz
    * @throws IOException
    *            exception sur le fichier
    */
   public final String compress() throws IOException {

      // création d'un buffer d'écriture
      ByteArrayOutputStream tarDest = new ByteArrayOutputStream();
      try {
         tarOutputStream.compress(tarDest);

         // gzip du fichier
         FileOutputStream dest = new FileOutputStream(tgz);
         try {

            CheckedOutputStream checksum = ArchiveUtil.crc32(dest);
            try {

               // création d'un buffer d'écriture
               BufferedOutputStream buff = new BufferedOutputStream(checksum);
               try {

                  // création d'un flux d'écriture pour la compression
                  GzipCompressorOutputStream out = new GzipCompressorOutputStream(
                        buff);
                  try {

                     // création d'un buffer d'écriture
                     tarDest.writeTo(out);

                  } finally {
                     // fermeture du flux d'écriture
                     if (out != null) {
                        out.close();
                     }
                  }

                  return Long.toHexString(checksum.getChecksum().getValue());

               } finally {
                  if (buff != null) {
                     buff.close();
                  }
               }

            } finally {
               if (checksum != null) {
                  checksum.close();
               }
            }

         } finally {
            if (dest != null) {
               dest.close();
            }
         }

      } finally {
         // fermeture du flux d'écriture
         if (tarDest != null) {
            tarDest.close();
         }
      }

   }

}
