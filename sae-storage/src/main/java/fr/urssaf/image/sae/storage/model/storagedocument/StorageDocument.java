package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant un document contenant un identifiant unique
 * suite à une insertion qui s’est bien déroulée. Elle contient l'attribut :
 * <ul>
 * <li>
 * uuid : L'uuid du document</li>
 * </ul>
 */

public class StorageDocument extends AbstractStorageDocument {
   
   // Les attributs
   private List<StorageMetadata> metadatas;
   private List<StorageMetadata> metadatasToDelete;
   private DataHandler content;
   private String filePath;
   private String typeDoc;
   private Date creationDate;
   private String title;
   private String processId;
   private String fileName;

   /**
    * @return L'identifiant du traitement.
    */
   public final String getProcessId() {
      return processId;
   }

   /**
    * @param processId
    *           : L'identifiant du traitement.
    */
   public final void setProcessId(final String processId) {
      this.processId = processId;
   }

   /**
    * Retourne la liste des métadonnées.
    * 
    * @return Liste des métadonnées
    */
   public final List<StorageMetadata> getMetadatas() {
      return metadatas;
   }

   /**
    * Initialise la liste des métadonnées.
    * 
    * @param metadatas
    *           : La liste des métadonnées
    */
   public final void setMetadatas(final List<StorageMetadata> metadatas) {
      this.metadatas = metadatas;
   }

   /**
    * Retourne le contenu du document
    * 
    * @return Le contenu du document
    */
   @SuppressWarnings("PMD.MethodReturnsInternalArray")
   public final DataHandler getContent() {
      return content;
   }

   /**
    * Initialise le contenu du document
    * 
    * @param content
    *           : Le contenu du document
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public final void setContent(final DataHandler content) {
      this.content = content;
   }

   /**
    * Retourne le chemin du fichier
    * 
    * @return Le chemin du fichier
    */
   public final String getFilePath() {
      return filePath;
   }

   /**
    * Initialise le chemin du fichier
    * 
    * @param filePath
    *           : Le chemin du document
    */
   public final void setFilePath(final String filePath) {
      this.filePath = filePath;
   }

   /**
    * @return le type de document.
    */
   public final String getTypeDoc() {
      return typeDoc;
   }

   /**
    * @param typeDoc
    *           :le type de document.
    */
   public final void setTypeDoc(final String typeDoc) {
      this.typeDoc = typeDoc;
   }

   /**
    * @return : date de creation.
    */
   public final Date getCreationDate() {
      return getDateCopy(creationDate);
   }

   /**
    * @param creationDate
    *           : date de creation.
    */
   public final void setCreationDate(final Date creationDate) {
      this.creationDate = getDateCopy(creationDate);
   }

   /**
    * @return le titre du document.
    */
   public final String getTitle() {
      return title;
   }

   /**
    * @param title
    *           :le titre du document.
    */
   public final void setTitle(final String title) {
      this.title = title;
   }

   /**
    * @return le nom du fichier
    */
   public final String getFileName() {
      return fileName;
   }

   /**
    * Initialise le chemin du fichier
    * 
    * @param fileName
    *           : Le chemin du document
    */
   public final void setFileName(String fileName) {
      this.fileName = fileName;
   }

   private Date getDateCopy(Date date) {
      Date tDate = null;
      if (date != null) {
         tDate = new Date(date.getTime());
      }

      return tDate;
   }

   /**
    * @return the metadatasToDelete
    */
   public List<StorageMetadata> getMetadatasToDelete() {
      return metadatasToDelete;
   }

   /**
    * @param metadatasToDelete the metadatasToDelete to set
    */
   public void setMetadatasToDelete(List<StorageMetadata> metadatasToDelete) {
      this.metadatasToDelete = metadatasToDelete;
   }

   /**
    * Construit un {@link StorageDocument }.
    * 
    * @param metadatas
    *           : Les métadonnées du document
    * @param content
    *           : Le contenu du document
    * 
    */
   public StorageDocument(final List<StorageMetadata> metadatas,
         final DataHandler content) {
      this.metadatas = metadatas;
      this.content = content;

   }

   /**
    * Construit un {@link StorageDocument }.
    * 
    * @param metadatas
    *           : Les métadonnées du document
    * 
    */
   public StorageDocument(final List<StorageMetadata> metadatas) {
      this.metadatas = metadatas;
      this.content = new DataHandler(new byte[1], "");
   }

   /**
    * Construit un {@link StorageDocument }.
    * 
    * @param storageDocument
    *           : Un storageDocument
    * 
    */
   @SuppressWarnings("PMD.CallSuperInConstructor")
   public StorageDocument(final StorageDocument storageDocument) {
      setContent(storageDocument.getContent());
      setCreationDate(storageDocument.getCreationDate());
      setFilePath(storageDocument.getFilePath());
      setMetadatas(storageDocument.getMetadatas());
      setTitle(storageDocument.getTitle());
      setTypeDoc(storageDocument.getTypeDoc());
      setUuid(storageDocument.getUuid());

   }

   /**
    * 
    * Construit un {@link StorageDocument } par défaut.
    */
   public StorageDocument() {
      this.metadatas = new ArrayList<StorageMetadata>();
      this.content = new DataHandler(new byte[1], "");
   }

   /**
    * Construit un {@link StorageDocument }.
    * 
    * @param metadatas
    *           : Les métadonnées du document
    * @param content
    *           : Le contenu du document
    * @param uuid
    *           : l'uuid
    * 
    */
   public StorageDocument(final List<StorageMetadata> metadatas,
         final DataHandler content, final UUID uuid) {    
      super(uuid);
      this.metadatas = metadatas;
      this.content = content;
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
      return new ToStringBuilder(this)
            .append("creationDate", getCreationDate()).append("content",
                  getContent()).append("uuid", getUuid()).append("filePath",
                  getFilePath()).append("metadatas", stringBuffer.toString())
            .toString();
   }

}
