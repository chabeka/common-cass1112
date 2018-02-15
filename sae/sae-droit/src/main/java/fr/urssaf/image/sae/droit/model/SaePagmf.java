package fr.urssaf.image.sae.droit.model;


/**
 * 
 * Bean permettant de stocker le contenu d'une ligne de la CF DroitPagmf.
 * 
 */
public class SaePagmf {

   private String codePagmf;
   private String description;
   private String formatProfile;

   /**
    * @return identifiant du Pagmf
    */
   public final String getCodePagmf() {
      return codePagmf;
   }

   /**
    * @param codePagmf
    *           identifiant du Pagmf à setter
    */
   public final void setCodePagmf(String codePagmf) {
      this.codePagmf = codePagmf;
   }

   /**
    * @return la description du contrôle
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           la description du contrôle to set
    */
   public final void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return code du profil de contrôle contenant les paramètres à sa mise en
    *         oeuvre.
    */
   public final String getFormatProfile() {
      return formatProfile;
   }

   /**
    * @param formatProfile
    *           du profil de contrôle contenant les paramètres à sa mise en
    *           oeuvre. to set
    */
   public final void setFormatProfile(String formatProfile) {
      this.formatProfile = formatProfile;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof SaePagmf) {
         SaePagmf pagmf = (SaePagmf) obj;
         areEquals = codePagmf.equals(pagmf.getCodePagmf())
               && description.equals(pagmf.getDescription())
               && formatProfile.equals(pagmf.getFormatProfile());
      }

      return areEquals;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      return "code : " + codePagmf + "\ndescription :\n" + description
            + "\ncode profile de contrôle :\n" + formatProfile;
   }

}
