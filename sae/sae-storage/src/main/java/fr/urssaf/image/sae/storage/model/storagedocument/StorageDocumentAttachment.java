package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.Date;
import java.util.UUID;

import javax.activation.DataHandler;

/**
 * Classe concrète représentant un document attaché
 */
public class StorageDocumentAttachment {

   /**
    * UUID du document parent
    */
   private UUID docUuid;

   /**
    * Nom de la pièce jointe
    */
   private String name;

   /**
    * Extension de la pièce jointe
    */
   private String extension;

   /**
    * Empreinte de contrôle de la pièce jointe
    */
   private String hash;
   
   /**
    * Date d'archivage
    */
   private Date dateArchivage;
   
   /**
    * Contenu de la pièce jointe
    */
   private DataHandler contenu;

   /**
    * Construit un {@link StorageDocumentAttachment }.
    * 
    * @param docUuid
    *           UUID du document parent
    * @param name
    *           Nom de la pièce jointe
    * @param extension
    *           Extension de la pièce jointe
    * @param hash
    *           Empreinte de contrôle de la pièce jointe
    * @param docStram
    *           Contenu de la pièce jointe
    */
   public StorageDocumentAttachment(UUID docUuid, String name,
         String extension, String hash, Date dateArchivage, DataHandler contenu) {
      this.docUuid = docUuid;
      this.name = name;
      this.extension = extension;
      this.hash = hash;
      this.contenu = contenu;
      this.dateArchivage = dateArchivage;
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
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the extension
    */
   public String getExtension() {
      return extension;
   }

   /**
    * @param extension
    *           the extension to set
    */
   public void setExtension(String extension) {
      this.extension = extension;
   }

   /**
    * @return the hash
    */
   public String getHash() {
      return hash;
   }

   /**
    * @param hash
    *           the hash to set
    */
   public void setHash(String hash) {
      this.hash = hash;
   }

   /**
    * @return the docStream
    */
   public DataHandler getContenu() {
      return contenu;
   }

   /**
    * @param docStream
    *           the docStream to set
    */
   public void setContenu(DataHandler contenu) {
      this.contenu = contenu;
   }

   /**
    * @return the dateArchivage
    */
   public Date getDateArchivage() {
      return dateArchivage;
   }

   /**
    * @param dateArchivage the dateArchivage to set
    */
   public void setDateArchivage(Date dateArchivage) {
      this.dateArchivage = dateArchivage;
   }

}
