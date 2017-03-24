package fr.urssaf.image.commons.util.compress.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

abstract class AbstractFileInputStream<F extends ArchiveInputStream> {

   private final String compressFileName;

   private final String repertory;

   protected AbstractFileInputStream(String compressFileName, String repertory) {
      this.compressFileName = compressFileName;
      this.repertory = repertory;
   }

   /**
    * Méthode de décompression
    * 
    * @throws IOException exception sur les fichiers
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public void uncompress() throws IOException {

      // création d'un flux d'écriture sur fichier
      FileInputStream inputStream = new FileInputStream(this.compressFileName);
      try {
         ArchiveInputStream archiveInput = getArchiveInputStream(inputStream);
         try {
            ArchiveEntry archiveEntry = archiveInput.getNextEntry();
            while (archiveEntry != null) {
   
               // traitement des fichiers compressés
   
               String name = this.getFileName(archiveEntry);
   
               FileUtils.forceMkdir(new File(FilenameUtils.getFullPath(name)));
   
               if (!archiveEntry.isDirectory()) {
                  FileOutputStream output = new FileOutputStream(name);
                  try {
                     IOUtils.copy(archiveInput, output);
                  } finally {
                     if (output!=null) {
                        output.close();
                     }
                  }
               }
   
               archiveEntry = archiveInput.getNextEntry();
            }
         } finally {
            if (archiveInput!=null) {
               archiveInput.close();
            }
         }
      }
      finally {
         if (inputStream!=null) {
            inputStream.close();
         }
      }

   }

   protected String getFileName(ArchiveEntry archiveEntry) {
      return FilenameUtils.concat(repertory, archiveEntry.getName());
   }

   protected abstract F getArchiveInputStream(InputStream inputStream);
}
