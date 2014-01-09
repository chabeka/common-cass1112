package fr.urssaf.image.sae.webservices.modele;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * Implémentation du DataSource pour le service de consultation en MTOM, pour
 * lequel il faut spécifier le type MIME de la pièce jointe
 */
public final class ConsultationDataSource implements DataSource {

   private final byte[] content;

   private final String typeMime;

   /**
    * Constructeur
    * 
    * @param content
    *           le contenu du document à renvoyer
    * @param typeMime
    *           le type pronom, pour le convertir en type MIME par la suite
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public ConsultationDataSource(byte[] content, String typeMime) {

      this.content = content;

      this.typeMime = typeMime;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getContentType() {
      return typeMime;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(content);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName() {
      // inutilisé
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public OutputStream getOutputStream() throws IOException {
      // inutilisé
      return null;
   }

}
