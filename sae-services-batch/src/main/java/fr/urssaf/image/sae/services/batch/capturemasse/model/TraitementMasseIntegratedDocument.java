/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.model;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

/**
 * Modèle objet pour les documents persistés dans DFCE
 * 
 */
public class TraitementMasseIntegratedDocument implements Serializable {


   /**
    * SUID
    */
   private static final long serialVersionUID = 1433593441518063701L;

   /**
    * Constructor.
    */
   public TraitementMasseIntegratedDocument() {
   }

   /**
    * Constructor.
    * 
    * @param identifiant
    *           Identifiant
    * @param documentFile
    *           Fichier document
    * @param index
    *           Index dans le sommaire
    */
   public TraitementMasseIntegratedDocument(UUID identifiant,
         File documentFile, int index) {
      this.identifiant = identifiant;
      this.documentFile = documentFile;
      this.index = index;
   }

   /**
    * Identifiant d'archivage d'un document dans DFCE
    */
   private UUID identifiant;

   /**
    * Chemin du fichier du document dans le répertoire ECDE
    */
   private File documentFile;
   
   /**
    * Index du document traité
    */
   private int index;
   
   /**
    * @return the identifiant Identifiant d'archivage d'un document dans DFCE
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }
   
   /**
    * @return l'index du document traité
    */
   public final int getIndex() {
      return index;
   }
   
   /**
    * @param ind l'index du document traité
    */
   public final void setIndex(int ind) {
      index = ind;
   }

   /**
    * @param identifiant
    *           Identifiant d'archivage d'un document dans DFCE
    */
   public final void setIdentifiant(final UUID identifiant) {
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
   public final void setDocumentFile(final File documentFile) {
      this.documentFile = documentFile;
   }

}
