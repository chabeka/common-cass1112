package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.UUID;

/**
 * Classe abstraite contenant les attributs communs des différents types de
 * documents destinés au stockage.</BR> Elle contient les attributs :
 * <ul>
 * <li>
 * uuid : Identifiant du document.</li>
 * <li>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractStorageDocument {
   // L'attribut
   private UUID uuid;
   
   /**
    * Construit un nouveau {@link AbstractStorageDocument } par défaut.
    */
   public AbstractStorageDocument() {
      // Ici on ne fait rien.
   }
   
   /**
    * Constructeur.
    * @param uuid Identifiant du document.
    */
   public AbstractStorageDocument(UUID uuid) {
      this.uuid = uuid;
   }

   /**
    * Retourne l’identifiant unique universel
    * 
    * @return UUID du document
    */
   public final UUID getUuid() {
      return uuid;
   }

   /**
    * Initialise l’identifiant unique universel.
    * 
    * @param uuid
    *           : L'identifiant universel unique
    */
   public final void setUuid(final UUID uuid) {
      this.uuid = uuid;
   }
}
