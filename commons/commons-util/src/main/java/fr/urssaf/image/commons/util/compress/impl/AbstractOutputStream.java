package fr.urssaf.image.commons.util.compress.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

abstract class AbstractOutputStream<F extends OutputStream> {

   protected static final Logger LOG = Logger
         .getLogger(AbstractOutputStream.class);

   private final String fileName;

   private String[] extensions;

   protected AbstractOutputStream(String fileName) {
      this.fileName = fileName;
   }

   public void setExtensions(String... extensions) {
      this.extensions = extensions;
   }

   protected void compress(OutputStream dest) throws IOException {

      // création d'un buffer d'écriture
      BufferedOutputStream buff = new BufferedOutputStream(dest);
      try {

         // création d'un flux d'écriture pour la compression
         F out = createOutputStream(buff);
         try {
            write(out);
         } finally {
            // fermeture du flux d'écriture
            if (out!=null) {
               out.close();
            }
         }
      
      }
      finally {
         if (buff!=null) {
            buff.close();
         }
      }

   }

   @SuppressWarnings("unchecked")
   protected void write(F out) throws IOException {

      // creation du fichier à compresser
      File file = new File(this.fileName);

      if (file.isDirectory()) {
         
         Collection<File> files = FileUtils.listFiles(
               file, 
               this.extensions,
               true);
         
         for (File tmpFile : files) {
            compressFile(tmpFile, out);
         }
         
      } else {
         compressFile(file, out);
      }
   }

   protected abstract F createOutputStream(BufferedOutputStream buff)
         throws IOException;

   protected abstract void compressFile(File file, F out) throws IOException;

}
