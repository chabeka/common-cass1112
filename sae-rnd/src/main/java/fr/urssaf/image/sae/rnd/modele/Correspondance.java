package fr.urssaf.image.sae.rnd.modele;

import java.util.Date;

/**
 * Objet représentant la correspondance entre un code rnd temporaire et un code
 * définitif
 * 
 * 
 */
public class Correspondance {

   /**
    * Le code temporaire
    */
   private String codeTemporaire;

   /**
    * Le code définitif
    */
   private String codeDefinitif;

   /**
    * La version en cours durant l'existence de ce code temporaire
    */
   private String versionCourante;

   /**
    * La date de début de mise à jour du traitement de cette correspondance
    */
   private Date dateDebutMaj;

   /**
    * La date de fin de mise à jour du traitement de cette correspondance
    */
   private Date dateFinMaj;

   /**
    * L'état de la mise à jour
    */
   private EtatCorrespondance etat;

   /**
    * @return the codeTemporaire
    */
   public final String getCodeTemporaire() {
      return codeTemporaire;
   }

   /**
    * @param codeTemporaire
    *           the codeTemporaire to set
    */
   public final void setCodeTemporaire(String codeTemporaire) {
      this.codeTemporaire = codeTemporaire;
   }

   /**
    * @return the codeDefinitif
    */
   public final String getCodeDefinitif() {
      return codeDefinitif;
   }

   /**
    * @param codeDefinitif
    *           the codeDefinitif to set
    */
   public final void setCodeDefinitif(String codeDefinitif) {
      this.codeDefinitif = codeDefinitif;
   }

   /**
    * @return the dateDebutMaj
    */
   public final Date getDateDebutMaj() {
      return getDateCopy(dateDebutMaj);
   }

   /**
    * @param dateDebutMaj
    *           the dateDebutMaj to set
    */
   public final void setDateDebutMaj(Date dateDebutMaj) {
      this.dateDebutMaj = getDateCopy(dateDebutMaj);
   }

   /**
    * @return the dateFinMaj
    */
   public final Date getDateFinMaj() {
      return getDateCopy(dateFinMaj);
   }

   /**
    * @param dateFinMaj
    *           the dateFinMaj to set
    */
   public final void setDateFinMaj(Date dateFinMaj) {
      this.dateFinMaj = getDateCopy(dateFinMaj);
   }

   /**
    * @return the etat
    */
   public final EtatCorrespondance getEtat() {
      return etat;
   }

   /**
    * @param etat
    *           the etat to set
    */
   public final void setEtat(EtatCorrespondance etat) {
      this.etat = etat;
   }

   /**
    * @return the versionCourante
    */
   public final String getVersionCourante() {
      return versionCourante;
   }

   /**
    * @param versionCourante
    *           the versionCourante to set
    */
   public final void setVersionCourante(String versionCourante) {
      this.versionCourante = versionCourante;
   }

   private Date getDateCopy(Date date) {
      Date tDate = null;
      if (date != null) {
         tDate = new Date(date.getTime());
      }

      return tDate;
   }
}
