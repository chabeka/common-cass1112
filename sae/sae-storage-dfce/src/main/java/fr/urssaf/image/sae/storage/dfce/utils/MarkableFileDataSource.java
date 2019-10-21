package fr.urssaf.image.sae.storage.dfce.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.FileDataSource;

/**
 * Il s'agit d'un FileDataSource, mais dont l'inputStream supporte les méthodes mark/reset
 *
 */
public class MarkableFileDataSource extends FileDataSource implements Closeable, AutoCloseable {

   private MarkableFileInputStream fis;

   /**
    * Constructeur
    * @param file
    */
   public MarkableFileDataSource(final File file) {
      super(file);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InputStream getInputStream() throws IOException {
      if (fis == null) {
         fis = new MarkableFileInputStream(new FileInputStream(getFile()));
      }
      return fis;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close() throws IOException {
      if (fis != null) {
         fis.realClose();
      }
   }
}
