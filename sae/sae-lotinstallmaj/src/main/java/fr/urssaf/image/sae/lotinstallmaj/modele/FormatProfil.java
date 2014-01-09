package fr.urssaf.image.sae.lotinstallmaj.modele;

/**
 * 
 * Bean permettant de stocker le contenu d'une ligne pour le FormatProfilSerializer
 *
 */
public class FormatProfil {

   private String fileFormat;
   private boolean formatIdentification;
   private boolean formatValidation;
   private String formatValidationMode;
   
   
   /**
    * @return indicateur de contrôle d'identification
    */
   public final boolean isFormatIdentification() {
      return formatIdentification;
   }
   /**
    * @param formatIdentification indicateur de contrôle d'identification to set
    */
   public final void setFormatIdentification(boolean formatIdentification) {
      this.formatIdentification = formatIdentification;
   }
   
   /**
    * @return indicateur de contrôle de validation
    */
   public final boolean isFormatValidation() {
      return formatValidation;
   }
   /**
    * @param formatValidation indicateur de contrôle de validation to set
    */
   public final void setFormatValidation(boolean formatValidation) {
      this.formatValidation = formatValidation;
   }
   
   
   /**
    * @return Mode de validation (Strict/Monitor)
    */
   public final String getFormatValidationMode() {
      return formatValidationMode;
   }
   /**
    * @param formatValidationMode Mode de validation (Strict/Monitor) to set
    */
   public final void setFormatValidationMode(String formatValidationMode) {
      this.formatValidationMode = formatValidationMode;
   }
   /**
    * @return the fileFormat
    */
   public final String getFileFormat() {
      return fileFormat;
   }
   /**
    * @param fileFormat the fileFormat to set
    */
   public final void setFileFormat(String fileFormat) {
      this.fileFormat = fileFormat;
   }
   
   
}
