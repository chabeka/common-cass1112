package fr.urssaf.image.commons.util.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 * Classe pour de lecture et d'écriture d'un fichier partagé.<br>
 * Le fichier est verrouillé dès que l'on y accède<br>
 * Cette classe se base sur {@link java.io.RandomAccessFile}
 * 
 */
public class LockFile {

   private RandomAccessFile randomAccessFile;

   private FileChannel fileChannel;
   private FileLock fileLock;
   private File file;

   private static final Logger LOGGER = Logger.getLogger(LockFile.class);

   /**
    * initialisation du fichier partagé
    * 
    * @param file fichier partagé
    * @throws IOException exception sur le fichier
    */
   public LockFile(String file) throws IOException {
      this(new File(file));
   }

   /**
    * initialisation du fichier partagé
    * 
    * @param file fichier partagé
    * @throws IOException exception sur le fichier
    */
   public LockFile(File file) throws IOException {
      this.file = file;

      init();
   }

   /**
    * Méthode de lecture en mode verrouillé
    * 
    * @return la chaine de caractère du contenu du fichier
    * @throws IOException execption sur la lecture
    */
   public final String read() throws IOException {

      lock(true);

      LOGGER.trace("lecture de " + file.getName());

      StringBuffer text = new StringBuffer();
      try {
         String line = randomAccessFile.readLine();
         while (line != null) {
            text.append(line);
            text.append(StringEscapeUtils.escapeJava("n"));
            line = randomAccessFile.readLine();
         }
         return text.toString();
      } finally {

         release();
         LOGGER.trace("fin de lecture de " + file.getName());

      }

   }

   /**
    * Méthode d'écriture en mode verrouillé
    * 
    * @param text chaine de caractères à écrire en fin de fichier
    * @throws IOException exception sur le fichier
    */
   public final void write(String text) throws IOException {

      lock(false);

      LOGGER.trace("écriture sur " + file.getName());

      try {
         randomAccessFile.seek(randomAccessFile.length());
         randomAccessFile.write(text.getBytes());

      } finally {

         release();
         LOGGER.trace("fin d'écriture de " + file.getName());

      }

   }

   /**
    * méthode pour initialiser le randomAccessFile en rw et le fileChannel
    * 
    * @throws IOException
    */
   private void init() throws IOException {
      this.randomAccessFile = new RandomAccessFile(file, "rw");
      this.fileChannel = randomAccessFile.getChannel();

   }

   /**
    * libération du verrou et fermeture du fichier
    * 
    * @throws IOException
    */
   private void release() throws IOException {

      synchronized (this) {
         fileLock.release();
         randomAccessFile.close();

         this.notifyAll();
      }

   }

   /**
    * instruction d'attente pour l'obtention du fichier
    * 
    * @param shared
    * @throws IOException
    */
   private void lock(boolean shared) throws IOException {
      synchronized (this) {
         while (isLock(shared)) {
            try {
               this.wait();
            } catch (InterruptedException e) {

            }
         }
      }
   }

   /**
    * Condition pour obtenir le verrou sur le fichier
    * 
    * @param shared
    *           partagé en mode lecture et exclusif en mode écriture
    * @return
    * @throws IOException
    */
   private boolean isLock(boolean shared) throws IOException {

      boolean lock = false;

      try {
         fileLock = fileChannel.lock(0L, Long.MAX_VALUE, shared);
      } catch (OverlappingFileLockException e) {
         lock = true;
      } catch (ClosedChannelException e) {
         init();
         fileLock = fileChannel.lock(0L, Long.MAX_VALUE, shared);
      }

      return lock;
   }
}
