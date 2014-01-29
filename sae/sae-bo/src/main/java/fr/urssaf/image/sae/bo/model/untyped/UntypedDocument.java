package fr.urssaf.image.sae.bo.model.untyped;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import fr.urssaf.image.sae.bo.model.AbstractDocument;

/**
 * Classe représentant un document c'est-à-dire un tableau de byte correspondant
 * au contenu du document et la liste des métadonnées(liste de paires (code,
 * valeur) dont les valeurs sont non typées).<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>uMetadatas : La liste des métadonnées non typées.</li>
 * </ul>
 */
public class UntypedDocument extends AbstractDocument {
   private List<UntypedMetadata> uMetadatas;

   /**
    * @return La liste des métadonnées métiers.
    */
   public final List<UntypedMetadata> getUMetadatas() {
      return uMetadatas;
   }

   /**
    * @param metadatas
    *           : La liste des métadonnées métiers.
    */
   public final void setUMetadatas(final List<UntypedMetadata> metadatas) {
      this.uMetadatas = metadatas;
   }

   /**
    * Construit un objet de type {@link UntypedDocument}.
    */
   public UntypedDocument() {
      super();
   }

   /**
    * Construit un objet de type {@link UntypedDocument}
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param filePath
    *           : Le chemin du fichier.
    * @param metadatas
    *           : La liste des métadonnées non typés.
    */
   public UntypedDocument(final String filePath, final byte[] content,
         final List<UntypedMetadata> metadatas) {
      super(content, filePath);
      this.uMetadatas = metadatas;
   }

   /**
    * Construit un objet de type {@link UntypedDocument}
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param metadatas
    *           : La liste des métadonnées non typés.
    */
   public UntypedDocument(final byte[] content,
         final List<UntypedMetadata> metadatas) {
      super(content);
      this.uMetadatas = metadatas;
   }

   /**
    * Construit un objet de type {@link UntypedDocument}
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param filePath
    *           : Le chemin du document métier.F
    * @param fileName
    *           : Le nom du fichier.
    * @param metadatas
    *           : La liste des métadonnées non typés.
    */
   public UntypedDocument(final byte[] content, final String filePath, final String fileName,
         final List<UntypedMetadata> metadatas) {
      super(filePath, fileName, content);
      this.uMetadatas = metadatas;
   }

   /**
    * Construit un objet de type {@link UntypedDocument}
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param fileName
    *           : Le nom du fichier.
    * @param metadatas
    *           : La liste des métadonnées non typés.
    */
   public UntypedDocument(final byte[] content, final String fileName,
         final List<UntypedMetadata> metadatas) {
      super(fileName, content);
      this.uMetadatas = metadatas;
   }

   /**
    * {@inheritDoc}
    */
   // CHECKSTYLE:OFF
   public String toString() {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SHORT_PREFIX_STYLE);
      toStrBuilder.append("chemin du fichier", getFilePath());
      toStrBuilder.append("identifiant d'archivage ", getUuid());
      if (uMetadatas != null) {
         for (UntypedMetadata uMetadata : uMetadatas) {
            toStrBuilder.append(uMetadata.toString());
         }
      }
      return toStrBuilder.toString();
   }
   // CHECKSTYLE:ON
}
