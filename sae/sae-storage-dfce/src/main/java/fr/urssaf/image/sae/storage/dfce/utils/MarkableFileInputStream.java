/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.storage.dfce.utils;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Implémentation d'un FileInputStream supportant les méthodes mark/reset
 * voir ici : https://stackoverflow.com/questions/1094703/java-file-input-with-rewind-reset-capability
 *
 */
public class MarkableFileInputStream extends FilterInputStream {
   private final FileChannel myFileChannel;
   private long mark = -1;

   public MarkableFileInputStream(final FileInputStream fis) {
      super(fis);
      myFileChannel = fis.getChannel();
   }

   @Override
   public boolean markSupported() {
      return true;
   }

   @Override
   public synchronized void mark(final int readlimit) {
      try {
         mark = myFileChannel.position();
      } catch (final IOException ex) {
         mark = -1;
      }
   }

   @Override
   public synchronized void reset() throws IOException {
      if (mark == -1) {
         throw new IOException("not marked");
      }
      myFileChannel.position(mark);
   }
}
