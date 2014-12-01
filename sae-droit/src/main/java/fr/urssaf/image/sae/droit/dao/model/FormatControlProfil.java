package fr.urssaf.image.sae.droit.dao.model;

/**
 * 
 * Bean permettant de stocker le contenu d'une ligne de la CF DroitFormatControlProfil
 *
 */
public class FormatControlProfil {

   private String formatCode;
   private String description;
   private FormatProfil controlProfil;
   
   
   /**
    * @return Code pronom correspondant au format de fichier
    */
   public final String getFormatCode() {
      return formatCode;
   }
   /**
    * @param formatCode Code pronom correspondant au format de fichier to set
    */
   public final void setFormatCode(String formatCode) {
      this.formatCode = formatCode;
   }
   
   
   /**
    * @return the description
    */
   public final String getDescription() {
      return description;
   }
   /**
    * @param description the description to set
    */
   public final void setDescription(String description) {
      this.description = description;
   }
   /**
    * @return the formatProfil
    */
   public final FormatProfil getControlProfil() {
      return controlProfil;
   }
   /**
    * @param controlProfil the formatProfil to set
    */
   public final void setControlProfil(FormatProfil controlProfil) {
      this.controlProfil = controlProfil;
   }
   
}
