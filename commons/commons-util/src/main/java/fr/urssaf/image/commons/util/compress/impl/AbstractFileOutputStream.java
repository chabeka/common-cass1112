package fr.urssaf.image.commons.util.compress.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CheckedOutputStream;

import org.apache.log4j.Logger;

abstract class AbstractFileOutputStream<F extends OutputStream>
   extends
      AbstractOutputStream<F> {

   protected static final Logger LOG = Logger.getLogger(AbstractFileOutputStream.class);

   private final String compressFileName;

   private final String fileName;

   protected AbstractFileOutputStream(String compressFileName, String fileName) {
      super(fileName);
      this.compressFileName = compressFileName;
      this.fileName = fileName;
   }

   /**
    * Méthode de compression
    * 
    * @return checksum du fichier compressé au format hexa
    * @throws IOException
    */
   public String compress() throws IOException {

      // création d'un flux d'écriture sur fichier
      FileOutputStream dest = new FileOutputStream(this.compressFileName);
      try {

         CheckedOutputStream checksum = ArchiveUtil.crc32(dest);
   
         try {
   
            return write(checksum);
   
         } finally {
            if (checksum!=null) {
               checksum.close();
            }
         }
         
      }
      finally {
         if (dest!=null) {
            dest.close();
         }
      }
   }

   protected String write(CheckedOutputStream checksum) throws IOException {

      this.compress(checksum);

      LOG.debug("compression de " + this.fileName + " en "
            + this.compressFileName);

      return Long.toHexString(checksum.getChecksum().getValue());
   }

}
