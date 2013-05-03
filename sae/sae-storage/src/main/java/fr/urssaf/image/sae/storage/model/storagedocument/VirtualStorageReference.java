/**
 * 
 */
package fr.urssaf.image.sae.storage.model.storagedocument;


/**
 * Objet de référence des documents virtuels (le fichier auquel ils font
 * référence)
 * 
 */
public class VirtualStorageReference {

   private String filePath;

   /**
    * @return the filePath
    */
   public final String getFilePath() {
      return filePath;
   }

   /**
    * @param filePath
    *           the filePath to set
    */
   public final void setFilePath(String filePath) {
      this.filePath = filePath;
   }

}
