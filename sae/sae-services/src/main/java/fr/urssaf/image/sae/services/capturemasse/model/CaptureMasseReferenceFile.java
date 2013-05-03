/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.model;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;

/**
 * Objet représentant un document virtuel archivé
 * 
 */
public class CaptureMasseReferenceFile {

   private String fileName;

   private StorageReferenceFile reference;

   /**
    * @return le nom du fichier de référence
    */
   public final String getFileName() {
      return fileName;
   }

   /**
    * @param fileName
    *           le nom du fichier de référence
    */
   public final void setFileName(String fileName) {
      this.fileName = fileName;
   }

   /**
    * @return le fichier inséré dans DFCE
    */
   public final StorageReferenceFile getReference() {
      return reference;
   }

   /**
    * @param reference
    *           le fichier inséré dans DFCE
    */
   public final void setReference(StorageReferenceFile reference) {
      this.reference = reference;
   }

}
