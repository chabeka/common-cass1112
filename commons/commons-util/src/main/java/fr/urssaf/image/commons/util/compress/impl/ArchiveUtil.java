package fr.urssaf.image.commons.util.compress.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;

import fr.urssaf.image.commons.util.file.FileWriterUtil;

final class ArchiveUtil {

   private ArchiveUtil() {

   }

   protected static void copy(File file, ArchiveOutputStream out,
         ArchiveEntry entry) throws IOException {

      // ajout de cette entrée dans le flux d'écriture de l'archive Zip
      out.putArchiveEntry(entry);

      FileWriterUtil.copy(file, out);

      // Close the current entry
      out.closeArchiveEntry();

   }

   protected static String entry(String path, File file) throws IOException {

      File pathFile = new File(path);

      String name;

      if (pathFile.isFile()) {
         name = file.getName();
      } else {

         name = file.getAbsolutePath().substring(
               FilenameUtils.getFullPath(pathFile.getAbsolutePath()).length());
      }

      return name;

   }

   protected static CheckedOutputStream crc32(FileOutputStream dest) {

      // calcul du checksum : Adler32 (plus rapide) ou CRC32
      return new CheckedOutputStream(dest, new CRC32());
   }
}
