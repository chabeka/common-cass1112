/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.storage.dfce.utils;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implémentation d'un FileInputStream supportant les méthodes mark/reset
 * voir ici : https://stackoverflow.com/questions/1094703/java-file-input-with-rewind-reset-capability
 *
 */
public class MarkableFileInputStream extends FilterInputStream {
   private static final Logger LOG = LoggerFactory.getLogger(MarkableFileInputStream.class);

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

   @Override
   /**
    * Pour gérer les retry en cas de déconnexion dfce :
    * on fait en sorte que la méthode close ne ferme pas le fichier. Ainsi, le fichier est encore
    * lisible en cas de retry.
    */
   public void close() throws IOException {
      LOG.debug("On ne ferme pas l'inputstream");
   }

   /**
    * Méthode qui ferme réellement l'inputStream
    */
   public void realClose() throws IOException {
      super.close();
   }
}
