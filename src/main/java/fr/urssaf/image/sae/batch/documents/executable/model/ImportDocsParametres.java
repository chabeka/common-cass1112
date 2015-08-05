package fr.urssaf.image.sae.batch.documents.executable.model;


/**
 * Objet permettant de stocker les paramètres concernant 
 * l'import de documents
 */
public class ImportDocsParametres extends AbstractParametres{

   private String importDir;

   /**
    * Get : Chemin du répertoire se stockage des document à exporter
    * @return
    */
   public String getImportDir() {
      return importDir;
   }
   
   /**
    * Set : Chemin du répertoire se stockage des document à exporter
    * @return
    */
   public void setImportDir(String dirPath) {
      this.importDir = dirPath;
   }
}
