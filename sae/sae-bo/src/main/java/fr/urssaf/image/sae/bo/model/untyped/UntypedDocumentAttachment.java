package fr.urssaf.image.sae.bo.model.untyped;

import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Classe représentant un document attaché c'est-à-dire un tableau de byte
 * correspondant au contenu du document et la liste des métadonnées(liste de
 * paires (code, valeur) dont les valeurs sont non typées).<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>uMetadatas : La liste des métadonnées non typées.</li>
 * </ul>
 */
public class UntypedDocumentAttachment {

   /**
    * Contenu du document
    */
   private DataHandler content;

   /**
    * Liste des métadonnées
    */
   private List<UntypedMetadata> uMetadatas;

   /**
    * UUID du document parent
    */
   private UUID docUuid;

   /**
    * Construit un objet de type {@link UntypedDocumentAttachment}
    * 
    * @param content
    *           : Le contenu du document métier.
    * @param metadatas
    *           : La liste des métadonnées non typés.
    */
   public UntypedDocumentAttachment(final UUID docUuid,
         final DataHandler content, final List<UntypedMetadata> metadatas) {
      this.content = content;
      this.uMetadatas = metadatas;
      this.docUuid = docUuid;

   }

   /**
    * Affichage de l'objet
    */
   public String toString() {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SHORT_PREFIX_STYLE);
      toStrBuilder.append("UUID document parent", getDocUuid());

      if (uMetadatas != null) {
         for (UntypedMetadata uMetadata : uMetadatas) {
            toStrBuilder.append(uMetadata.toString());
         }
      }
      return toStrBuilder.toString();
   }



   /**
    * @return the content
    */
   public DataHandler getContent() {
      return content;
   }

   /**
    * @param content
    *           the content to set
    */
   public void setContent(DataHandler content) {
      this.content = content;
   }

   /**
    * @return the docUuid
    */
   public UUID getDocUuid() {
      return docUuid;
   }

   /**
    * @param docUuid
    *           the docUuid to set
    */
   public void setDocUuid(UUID docUuid) {
      this.docUuid = docUuid;
   }

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
}
