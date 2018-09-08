/**
 * 
 */
package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.List;
import java.util.UUID;

/**
 * Objet représentant un document virtuel à archiver
 * 
 */
public class VirtualStorageDocument {

   private String fileName;

   private StorageReferenceFile referenceFile;

   private List<StorageMetadata> metadatas;

   private int startPage;

   private int endPage;

   private UUID uuid;

   private String processUuid;

   /**
    * @return le nom du fichier du document virtuel
    */
   public final String getFileName() {
      return fileName;
   }

   /**
    * @param fileName
    *           le nom du fichier du document virtuel
    */
   public final void setFileName(String fileName) {
      this.fileName = fileName;
   }

   /**
    * @return la liste des métadonnées associées
    */
   public final List<StorageMetadata> getMetadatas() {
      return metadatas;
   }

   /**
    * @param metadatas
    *           la liste des métadonnées associées
    */
   public final void setMetadatas(List<StorageMetadata> metadatas) {
      this.metadatas = metadatas;
   }

   /**
    * @return le n° de la page de début
    */
   public final int getStartPage() {
      return startPage;
   }

   /**
    * @param startPage
    *           le n° de la page de début
    */
   public final void setStartPage(int startPage) {
      this.startPage = startPage;
   }

   /**
    * @return le n° de la page de fin
    */
   public final int getEndPage() {
      return endPage;
   }

   /**
    * @param endPage
    *           le n° de la page de fin
    */
   public final void setEndPage(int endPage) {
      this.endPage = endPage;
   }

   /**
    * @return le fichier de référence
    */
   public final StorageReferenceFile getReferenceFile() {
      return referenceFile;
   }

   /**
    * @param referenceFile
    *           le fichier de référence
    */
   public final void setReferenceFile(StorageReferenceFile referenceFile) {
      this.referenceFile = referenceFile;
   }

   /**
    * @return l'identifiant unique du document
    */
   public final UUID getUuid() {
      return uuid;
   }

   /**
    * @param uuid
    *           l'identifiant unique du document
    */
   public final void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   /**
    * @return l'identifiant du processus
    */
   public final String getProcessUuid() {
      return processUuid;
   }

   /**
    * @param processUuid
    *           l'identifiant du processus
    */
   public final void setProcessUuid(String processUuid) {
      this.processUuid = processUuid;
   }

}
