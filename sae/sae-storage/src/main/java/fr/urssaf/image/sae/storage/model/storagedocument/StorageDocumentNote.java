package fr.urssaf.image.sae.storage.model.storagedocument;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Modèle de note d'un document
 */
@SuppressWarnings("PMD.LongVariable")
public class StorageDocumentNote implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = 6166512533704504649L;

   /**
    * Identifiant de la note
    */
   @JsonIgnore
   private UUID uuid;

   /**
    * Identifiant du document auquel la note est rattachée
    */
   @JsonIgnore
   private UUID docUuid;

   /**
    * Contenu de la note
    */
   private String contenu;

   /**
    * Date de création de la note
    */
   @JsonFormat(shape=Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
   private Date dateCreation;

   /**
    * Auteur de la note
    */
   private String auteur;

   /**
    * Construit un {@link StorageDocumentNote }.
    * 
    * @param uuid
    *           Identifiant de la note
    * @param docUuid
    *           Identifiant du docuemnt auquel la note est rattachée
    * @param contenu
    *           Contenu de la note
    * @param dateCreation
    *           Date de création de la note
    * @param auteur
    *           Auteur de la note
    */
   public StorageDocumentNote(UUID uuid, UUID docUuid, String contenu,
         Date dateCreation, String auteur) {
      this.uuid = uuid;
      this.docUuid = docUuid;
      this.contenu = contenu;
      this.dateCreation = dateCreation;
      this.auteur = auteur;
   }

   /**
    * Construit un {@link StorageDocumentNote } par défaut.
    * 
    */
   public StorageDocumentNote() {
      // ici on ne fait rien
   }

   /**
    * @return the uuid
    */
   @JsonIgnore
   public final UUID getUuid() {
      return uuid;
   }

   /**
    * @param uuid
    *           the uuid to set
    */
   @JsonIgnore
   public final void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   /**
    * @return the docUuid
    */
   @JsonIgnore
   public final UUID getDocUuid() {
      return docUuid;
   }

   /**
    * @param docUuid
    *           the docUuid to set
    */
   @JsonIgnore
   public final void setDocUuid(UUID docUuid) {
      this.docUuid = docUuid;
   }

   /**
    * @return the contenu
    */
   public final String getContenu() {
      return contenu;
   }

   /**
    * @param contenu
    *           the contenu to set
    */
   public final void setContenu(String contenu) {
      this.contenu = contenu;
   }

   /**
    * @return the dateCreation
    */
   public final Date getDateCreation() {
      return dateCreation;
   }

   /**
    * @param dateCreation
    *           the dateCreation to set
    */
   public final void setDateCreation(Date dateCreation) {
      this.dateCreation = dateCreation;
   }

   /**
    * @return the auteur
    */
   public final String getAuteur() {
      return auteur;
   }

   /**
    * @param auteur
    *           the auteur to set
    */
   public final void setAuteur(String auteur) {
      this.auteur = auteur;
   }
}
