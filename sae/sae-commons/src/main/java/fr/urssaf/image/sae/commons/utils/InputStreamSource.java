/**
 * 
 */
package fr.urssaf.image.sae.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * Classe permettant de réaliser des dataHandlers avec des inputStreams
 * 
 */
public class InputStreamSource implements DataSource {

   private InputStream inputStream;

   /**
    * Constructeur
    * 
    * @param inputStream
    *           le stream
    */
   public InputStreamSource(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getContentType() {
      return "*/*";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final InputStream getInputStream() throws IOException {
      return inputStream;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getName() {
      return "InputStreamSource";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final OutputStream getOutputStream() throws IOException {
      throw new UnsupportedOperationException("Not implemented");
   }

}
