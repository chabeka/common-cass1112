/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.model;

import java.util.UUID;

/**
 * Objet représentant un document virtuel archivé
 * 
 */
public class CaptureMasseVirtualDocument {

   private UUID referenceUUID;

   private UUID uuid;

   private int index;

   /**
    * @return l'identifiant unique du fichier de référence
    */
   public final UUID getReferenceUUID() {
      return referenceUUID;
   }

   /**
    * @param referenceUUID
    *           l'identifiant unique du fichier de référence
    */
   public final void setReferenceUUID(UUID referenceUUID) {
      this.referenceUUID = referenceUUID;
   }

   /**
    * @return l'identifiant unique du document virtuel
    */
   public final UUID getUuid() {
      return uuid;
   }

   /**
    * @param uuid
    *           l'identifiant unique du document virtuel
    */
   public final void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   /**
    * @return l'index du document
    */
   public final int getIndex() {
      return index;
   }

   /**
    * @param index
    *           l'index du document
    */
   public final void setIndex(int index) {
      this.index = index;
   }

}
