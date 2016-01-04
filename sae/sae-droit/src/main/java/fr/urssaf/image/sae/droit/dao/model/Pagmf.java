package fr.urssaf.image.sae.droit.dao.model;

/**
 * 
 * Bean permettant de stocker le contenu d'une ligne de la CF DroitPagmf.
 *
 */
public class Pagmf {

   private String codePagmf;
   private String description;
   private String codeFormatControlProfil;
   /**
    * @return identifiant du Pagmf
    */
   public final String getCodePagmf() {
      return codePagmf;
   }
   /**
    * @param codePagmf identifiant du Pagmf à setter
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
    * @param description la description du contrôle to set
    */
   public final void setDescription(String description) {
      this.description = description;
   }
   /**
    * @return code du profil de contrôle contenant les paramètres à sa mise en oeuvre.
    */
   public final String getCodeFormatControlProfil() {
      return codeFormatControlProfil;
   }
   /**
    * @param codeFormatControlProfil du profil de contrôle contenant les paramètres à sa mise en oeuvre. to set
    */
   public final void setCodeFormatControlProfil(String codeFormatControlProfil) {
      this.codeFormatControlProfil = codeFormatControlProfil;
   }
   
   
   
}