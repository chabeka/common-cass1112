/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele;

/**
 * 
 * 
 */
public class BeanConfig {

   /**
    * Chemin complet du fichier de sauvegarde
    */
   private String savedFilePath;

   /**
    * Numéro de version
    */
   private String version;

   /**
    * url du WS
    */
   private String url;
   
   /**
    * chemin complet du fichier généré lifeCycle
    */
   private String lifeCycleFilePath;

   /**
    * chemin du fichier de sauvegarde
    * 
    * @return the savedFilePath
    */
   public final String getSavedFilePath() {
      return savedFilePath;
   }

   /**
    * chemin du fichier de sauvegarde
    * 
    * @param savedFilePath
    *           the savedFilePath to set
    */
   public final void setSavedFilePath(String savedFilePath) {
      this.savedFilePath = savedFilePath;
   }

   /**
    * version voulue
    * 
    * @return the version
    */
   public final String getVersion() {
      return version;
   }

   /**
    * version voulue
    * 
    * @param version
    *           the version to set
    */
   public final void setVersion(String version) {
      this.version = version;
   }

   /**
    * url du WS
    * 
    * @return the url
    */
   public final String getUrl() {
      return url;
   }

   /**
    * url du WS
    * 
    * @param url
    *           the url to set
    */
   public final void setUrl(String url) {
      this.url = url;
   }

   /**
    * @return the lifeCycleFilePath
    */
   public final String getLifeCycleFilePath() {
      return lifeCycleFilePath;
   }

   /**
    * @param lifeCycleFilePath the lifeCycleFilePath to set
    */
   public final void setLifeCycleFilePath(String lifeCycleFilePath) {
      this.lifeCycleFilePath = lifeCycleFilePath;
   }

}
