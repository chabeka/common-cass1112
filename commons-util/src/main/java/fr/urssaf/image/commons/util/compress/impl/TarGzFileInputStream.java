package fr.urssaf.image.commons.util.compress.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Classe de décompression des fichier de type tgz<br>
 * <br>
 * un exemple est utilisé dans la classe utilitaire
 * {@link fr.urssaf.image.commons.util.compress.UnCompressUtil#untgz(String, String)}
 * 
 */
public class TarGzFileInputStream {

   private final String compressFileName;
   private final String repertory;

   /**
    * initialisation des fichiers de décompression
    * 
    * @param compressFileName
    *           nom du fichier à décompresser
    * @param repertory
    *           répertoire de décompression
    */
   public TarGzFileInputStream(String compressFileName, String repertory) {
      this.compressFileName = compressFileName;
      this.repertory = repertory;
   }

   /**
    * Méthode de décompression
    * 
    * @throws IOException
    *            exception sur les fichiers
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final void uncompress() throws IOException {

      FileInputStream fileInput = new FileInputStream(this.compressFileName);
      try {
         GzipCompressorInputStream gzInput = new GzipCompressorInputStream(
               fileInput);
         try {

            ByteArrayOutputStream gzOut = new ByteArrayOutputStream();

            try {

               IOUtils.copy(gzInput, gzOut);

               ByteArrayInputStream input = new ByteArrayInputStream(gzOut
                     .toByteArray());
               try {
                  ArchiveInputStream archiveInput = new TarArchiveInputStream(
                        input);
                  try {
                     ArchiveEntry archiveEntry = archiveInput.getNextEntry();

                     while (archiveEntry != null) {

                        // traitement des fichiers compressés
                        String name = FilenameUtils.concat(repertory,
                              archiveEntry.getName());
                        FileUtils.forceMkdir(new File(FilenameUtils
                              .getFullPath(name)));
                        if (!archiveEntry.isDirectory()) {
                           FileOutputStream output = new FileOutputStream(name);
                           try {
                              IOUtils.copy(archiveInput, output);
                           } finally {
                              output.close();
                           }
                        }

                        archiveEntry = archiveInput.getNextEntry();

                     }

                  } finally {
                     if (archiveInput != null) {
                        archiveInput.close();
                     }
                  }
               } finally {
                  if (input != null) {
                     input.close();
                  }
               }

            } finally {
               if (gzOut != null) {
                  gzOut.close();
               }
            }

         } finally {
            if (gzInput != null) {
               gzInput.close();
            }
         }

      } finally {
         if (fileInput != null) {
            fileInput.close();
         }
      }

   }
}
