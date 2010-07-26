package fr.urssaf.image.commons.util.compress.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarOutputStream extends
      AbstractOutputStream<TarArchiveOutputStream> {

   private final String fileName;

   public TarOutputStream(String fileName) {
      super(fileName);
      this.fileName = fileName;

   }

   @Override
   protected void compressFile(File file, TarArchiveOutputStream out)
         throws IOException {

      TarArchiveEntry entry = new TarArchiveEntry(file, ArchiveUtil.entry(
            this.fileName, file));
      ArchiveUtil.copy(file, out, entry);

   }

   @Override
   protected TarArchiveOutputStream createOutputStream(BufferedOutputStream buff) {
      return new TarArchiveOutputStream(buff);
   }
}
