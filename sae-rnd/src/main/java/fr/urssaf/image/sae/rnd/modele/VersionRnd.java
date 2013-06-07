package fr.urssaf.image.sae.rnd.modele;

import java.util.Date;

/**
 * Classe contenant les informations sur la version du RND en cours dans le SAE
 * 
 *
 */
public class VersionRnd {

   /**
    * Numéro de la version en cours dans le SAE
    */
   private String versionEnCours;
   
   
   /**
    * Date de la dernière mise à jour de la version du RND
    */
   private Date dateMiseAJour;


   /**
    * @return the versionEnCours
    */
   public final String getVersionEnCours() {
      return versionEnCours;
   }


   /**
    * @param versionEnCours the versionEnCours to set
    */
   public final void setVersionEnCours(String versionEnCours) {
      this.versionEnCours = versionEnCours;
   }


   /**
    * @return the dateMiseAJour
    */
   public final Date getDateMiseAJour() {
      return dateMiseAJour;
   }


   /**
    * @param dateMiseAJour the dateMiseAJour to set
    */
   public final void setDateMiseAJour(Date dateMiseAJour) {
      this.dateMiseAJour = dateMiseAJour;
   }
   
   
}
