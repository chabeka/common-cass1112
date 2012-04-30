package fr.urssaf.image.sae.bo.model.bo;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.AbstractDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;

/**
 * Classe représentant un document c'est-à-dire un tableau de byte correspondant
 * au contenu du document et la liste des métadonnées(liste de paires (code,
 * valeur) dont les valeurs sont typées).<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>metadatas : La liste des métadonnées métiers.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class SAEDocument extends AbstractDocument {
   private List<SAEMetadata> metadatas;

   /**
    * @return La liste des métadonnées métiers.
    */
   public final List<SAEMetadata> getMetadatas() {
      return metadatas;
   }

   /**
    * @param saeMetadatas
    *           : La liste des métadonnées métiers.
    */
   public final void setMetadatas(final List<SAEMetadata> saeMetadatas) {
      this.metadatas = saeMetadatas;
   }

   /**
    * Construit un objet de type {@link SAEDocument}.
    */
   public SAEDocument() {
      super();
   }

   /**
    * Construit un objet de type {@link SAEDocument}.
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param saeMetadatas
    *           : La liste des métadonnées métiers.
    */
   public SAEDocument(final byte[] content, final List<SAEMetadata> saeMetadatas) {
      super(content);
      this.metadatas = saeMetadatas;
   }

   /**
    * Construit un objet de type {@link UntypedDocument}
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param filePath
    *           : Le chemin du fichier.
    * @param fileName
    *           : Le nom du fichier.
    * @param metadatas
    *           : La liste des métadonnées non typés.
    */
   public SAEDocument(final String filePath, final byte[] content, final String fileName,
         final List<SAEMetadata> metadatas) {
      super(filePath, fileName, content);
      this.metadatas = metadatas;
   }

   /**
    * Construit un objet de type {@link SAEDocument}.
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param filePath
    *           : Le chemin du fichier.
    * @param saeMetadatas
    *           : La liste des métadonnées métiers.
    */
   public SAEDocument(final String filePath, final byte[] content,
         final List<SAEMetadata> saeMetadatas) {
      super(content, filePath);
      this.metadatas = saeMetadatas;
   }

   /**
    * {@inheritDoc}
    */
   // CHECKSTYLE:OFF
   public String toString() {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SHORT_PREFIX_STYLE);
      toStrBuilder.append("chemin du fichier", getFilePath());
      if (metadatas != null) {
         for (SAEMetadata metadata : metadatas) {
            toStrBuilder.append(metadata.toString());
         }
      }
      return toStrBuilder.toString();
   }
   // CHECKSTYLE:ON
}
