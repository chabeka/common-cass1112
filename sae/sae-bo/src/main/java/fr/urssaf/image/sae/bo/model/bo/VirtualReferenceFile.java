/**
 * 
 */
package fr.urssaf.image.sae.bo.model.bo;

/**
 * Objet de référence des documents virtuels (le fichier auquel ils font
 * référence)
 * 
 */
public class VirtualReferenceFile {

   private String filePath;

   private String hash;

   private String typeHash;

   /**
    * @return le chemin du chemin
    */
   public final String getFilePath() {
      return filePath;
   }

   /**
    * @param filePath
    *           le chemin du chemin
    */
   public final void setFilePath(String filePath) {
      this.filePath = filePath;
   }

   /**
    * @return le hash du fichier
    */
   public final String getHash() {
      return hash;
   }

   /**
    * @param hash
    *           le hash du fichier
    */
   public final void setHash(String hash) {
      this.hash = hash;
   }

   /**
    * @return le type de hash utilisé
    */
   public final String getTypeHash() {
      return typeHash;
   }

   /**
    * @param typeHash
    *           le type de hash utilisé
    */
   public final void setTypeHash(String typeHash) {
      this.typeHash = typeHash;
   }

}
