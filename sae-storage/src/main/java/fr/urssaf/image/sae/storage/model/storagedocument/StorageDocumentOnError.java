package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant un document contenant un code erreur suite à une
 * insertion qui s’est mal déroulée<BR />
 * Elle contient l'attribut :
 * <ul>
 * <li>
 * codeError : Le code d'erreur retourné par l'archivage du document</li>
 * <li>
 * messageError : Le message d'erreur retourné par l'archivage du document</li>
 * <li>
 * index : Index du document dans le sommaire</li>
 * </ul>
 */

public class StorageDocumentOnError extends AbstractStorageDocument {

   private String codeError;

   private String messageError;

   private int index;

   /**
    * Retourne le code erreur
    * 
    * @return Le code erreur
    */
   public final String getCodeError() {
      return codeError;
   }

   /**
    * Initialise le code erreur
    * 
    * @param codeError
    *           : Le code erreur
    */
   public final void setCodeError(final String codeError) {
      this.codeError = codeError;
   }

   /**
    * Construit un {@link StorageDocumentOnError }.
    * 
    * @param metadatas
    *           : Les metadatas du document
    * @param content
    *           : Le contenu du document
    * @param filePath
    *           : Le chemin du document
    * @param codeError
    *           : Le code Erreur
    */
   public StorageDocumentOnError(final List<StorageMetadata> metadatas,
         final byte[] content, final String filePath, final String codeError) {
      super(metadatas, content, filePath);
      this.codeError = codeError;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      @SuppressWarnings("PMD.LongVariable")
      final StringBuffer stringBuffer = new StringBuffer();
      if (getMetadatas() != null) {
         for (StorageMetadata metadata : getMetadatas()) {
            stringBuffer.append(metadata.toString());
         }

      }
      return new ToStringBuilder(this).append("content", getContent()).append(
            "codeError", codeError).append("messageError", messageError)
            .append("filePath", getFilePath()).append("metadatas",
                  stringBuffer.toString()).toString();

   }

   /**
    * @param messageError
    *           : Le message d'erreur retourné par l'archivage du document
    */
   public final void setMessageError(final String messageError) {
      this.messageError = messageError;
   }

   /**
    * @return Le message d'erreur retourné par l'archivage du document
    */
   public final String getMessageError() {
      return messageError;
   }

   /**
    * 
    * @param index
    *           index du document dans le sommaire
    */
   public final void setIndex(int index) {
      this.index = index;
   }

   /**
    * 
    * @return index du document dans le sommaire
    */
   public final int getIndex() {
      return index;
   }

}
