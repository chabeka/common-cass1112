package fr.urssaf.image.sae.batch.documents.executable.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.urssaf.image.sae.batch.documents.executable.exception.ExportDocsRuntimeException;


/**
 * Objet permettant de stocker les paramètres concernant 
 * l'expport de documents
 */
public class ExportDocsParametres extends AbstractParametres{

   private String exportDir;
     
   /**
    * Get : Chemin du répertoire se stockage des document à exporter
    * @return
    */
   public String getExportDir() {
      if(exportDir == null){
         Date now = new Date();
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
         
         String workDir = super.getDossierTravail();
         if(workDir == null){
            throw new ExportDocsRuntimeException("Le paramètre 'dossierTravail' ne doit pas être null !");
         }
         String exportDir = "EXPORT_" + dateFormat.format(now);
         this.setExportDir(workDir + File.separator + exportDir);
      }
      return exportDir;
   }
   
   /**
    * Set export directory
    * @param exportDir directory path
    */
   private void setExportDir(String exportDir) {
      this.exportDir = exportDir;
   }
}
