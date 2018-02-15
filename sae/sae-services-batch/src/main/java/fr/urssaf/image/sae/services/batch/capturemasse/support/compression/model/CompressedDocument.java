package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model;

import java.io.Serializable;

/**
 * Objet contenant les informations du document compressé.
 */
public class CompressedDocument implements Serializable {
   
   private static final long serialVersionUID = 1L;
   
   /**
    * Hash du document compressé.
    */
   private String hash;
   
   /**
    * Chemin vers le fichier compressé.
    */
   private String filePath;
   
   /**
    * Nom du fichier compressé.
    */
   private String fileName;
   
   /**
    * Nom du fichier d'origine.
    */
   private String originalName;

   /**
    * Getter sur le hash du document compressé.
    * @return String
    */
   public final String getHash() {
      return hash;
   }

   /**
    * Setter sur la hash du document compressé.
    * @param hash hash
    */
   public final void setHash(String hash) {
      this.hash = hash;
   }

   /**
    * Getter sur le chemin du fichier compressé.
    * @return String
    */
   public final String getFilePath() {
      return filePath;
   }

   /**
    * Setter sur le chemin du fichier compressé.
    * @param filePath chemin du fichier compressé
    */
   public final void setFilePath(String filePath) {
      this.filePath = filePath;
   }

   /**
    * Getter sur le nom du fichier.
    * @return String
    */
   public final String getFileName() {
      return fileName;
   }

   /**
    * Setter sur le nom du fichier.
    * @param fileName nom du fichier
    */
   public final void setFileName(String fileName) {
      this.fileName = fileName;
   }
   
   /**
    * Getter sur le nom du fichier d'origine.
    * @return String
    */
   public final String getOriginalName() {
      return originalName;
   }

   /**
    * Setter sur le nom du fichier d'origine.
    * @param originalName nom du fichier d'origine
    */
   public final void setOriginalName(String originalName) {
      this.originalName = originalName;
   }
}
