/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.model;

import java.io.File;
import java.util.UUID;

/**
 * Modèle objet pour les documents persistés dans DFCE
 * 
 */
public class CaptureMasseIntegratedDocument {

   /**
    * Identifiant d'archivage d'un document dans DFCE
    */
   private UUID identifiant;

   /**
    * Chemin du fichier du document dans le répertoire ECDE
    */
   private File documentFile;

   /**
    * @return the identifiant Identifiant d'archivage d'un document dans DFCE
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }

   /**
    * @param identifiant
    *           Identifiant d'archivage d'un document dans DFCE
    */
   public final void setIdentifiant(UUID identifiant) {
      this.identifiant = identifiant;
   }

   /**
    * @return the documentFile Chemin du fichier du document dans le répertoire
    *         ECDE
    */
   public final File getDocumentFile() {
      return documentFile;
   }

   /**
    * @param documentFile
    *           Chemin du fichier du document dans le répertoire ECDE
    */
   public final void setDocumentFile(File documentFile) {
      this.documentFile = documentFile;
   }

}
