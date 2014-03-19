package fr.urssaf.image.sae.services.controles.model;

/**
 * Objet décrivant le succès d'un contrôle de format à la capture.<br>
 * Utile notamment pour les tests unitaires afin de valider le comportement des
 * contrôles de format.
 */
public final class ControleFormatSucces {

   private String idFormatDuProfilControle;
   private boolean identificationActivee;
   private boolean identificationRealisee;
   private boolean identificationEchecMonitor;
   private boolean validationActivee;
   private boolean validationRealisee;
   private boolean validationEchecMonitor;
   private boolean surFlux;

   /**
    * L'identifiant du format du profil de contrôle à appliquer
    * 
    * @return L'identifiant du format du profil de contrôle à appliquer
    */
   public String getIdFormatDuProfilControle() {
      return idFormatDuProfilControle;
   }

   /**
    * L'identifiant du format du profil de contrôle à appliquer
    * 
    * @param idFormatDuProfilControle
    *           L'identifiant du format du profil de contrôle à appliquer
    */
   public void setIdFormatDuProfilControle(String idFormatDuProfilControle) {
      this.idFormatDuProfilControle = idFormatDuProfilControle;
   }

   /**
    * Flag indiquant si l'étape d'identification est activée sur le profil de
    * contrôle
    * 
    * @return Flag indiquant si l'étape d'identification est activée sur le
    *         profil de contrôle
    */
   public boolean isIdentificationActivee() {
      return identificationActivee;
   }

   /**
    * Flag indiquant si l'étape d'identification est activée sur le profil de
    * contrôle
    * 
    * @param identificationActivee
    *           Flag indiquant si l'étape d'identification est activée sur le
    *           profil de contrôle
    */
   public void setIdentificationActivee(boolean identificationActivee) {
      this.identificationActivee = identificationActivee;
   }

   /**
    * Flag indiquant si l'étape d'identification a été réalisée
    * 
    * @return Flag indiquant si l'étape d'identification a été réalisée
    */
   public boolean isIdentificationRealisee() {
      return identificationRealisee;
   }

   /**
    * Flag indiquant si l'étape d'identification a été réalisée
    * 
    * @param identificationRealisee
    *           Flag indiquant si l'étape d'identification a été réalisée
    */
   public void setIdentificationRealisee(boolean identificationRealisee) {
      this.identificationRealisee = identificationRealisee;
   }

   /**
    * Flag indiquant si l'étape d'identification a échoué en mode monitor
    * 
    * @return Flag indiquant si l'étape d'identification a échoué en mode
    *         monitor
    */
   public boolean isIdentificationEchecMonitor() {
      return identificationEchecMonitor;
   }

   /**
    * Flag indiquant si l'étape d'identification a échoué en mode monitor
    * 
    * @param identificationEchecMonitor
    *           Flag indiquant si l'étape d'identification a échoué en mode
    *           monitor
    */
   public void setIdentificationEchecMonitor(boolean identificationEchecMonitor) {
      this.identificationEchecMonitor = identificationEchecMonitor;
   }

   /**
    * Flag indiquant si l'étape de validation est activée sur le profil de
    * contrôle
    * 
    * @return Flag indiquant si l'étape de validation est activée sur le profil
    *         de contrôle
    */
   public boolean isValidationActivee() {
      return validationActivee;
   }

   /**
    * Flag indiquant si l'étape de validation est activée sur le profil de
    * contrôle
    * 
    * @param validationActivee
    *           Flag indiquant si l'étape de validation est activée sur le
    *           profil de contrôle
    */
   public void setValidationActivee(boolean validationActivee) {
      this.validationActivee = validationActivee;
   }

   /**
    * Flag indiquant si l'étape de validation a été réalisée
    * 
    * @return Flag indiquant si l'étape de validation a été réalisée
    */
   public boolean isValidationRealisee() {
      return validationRealisee;
   }

   /**
    * Flag indiquant si l'étape de validation a été réalisée
    * 
    * @param validationRealisee
    *           Flag indiquant si l'étape de validation a été réalisée
    */
   public void setValidationRealisee(boolean validationRealisee) {
      this.validationRealisee = validationRealisee;
   }

   /**
    * Flag indiquant si l'étape de validation a échoué en mode monitor
    * 
    * @return Flag indiquant si l'étape de validation a échoué en mode monitor
    */
   public boolean isValidationEchecMonitor() {
      return validationEchecMonitor;
   }

   /**
    * Flag indiquant si l'étape de validation a échoué en mode monitor
    * 
    * @param validationEchecMonitor
    *           Flag indiquant si l'étape de validation a échoué en mode monitor
    */
   public void setValidationEchecMonitor(boolean validationEchecMonitor) {
      this.validationEchecMonitor = validationEchecMonitor;
   }

   /**
    * Flag indiquant si le contrôle d'identification/validation est réalisé sur
    * un flux (par opposition à un chemin de fichier)
    * 
    * @return Flag indiquant si le contrôle d'identification/validation est
    *         réalisé sur un flux (par opposition à un chemin de fichier)
    */
   public boolean isSurFlux() {
      return surFlux;
   }

   /**
    * Flag indiquant si le contrôle d'identification/validation est réalisé sur
    * un flux (par opposition à un chemin de fichier)
    * 
    * @param surFlux
    *           Flag indiquant si le contrôle d'identification/validation est
    *           réalisé sur un flux (par opposition à un chemin de fichier)
    */
   public void setSurFlux(boolean surFlux) {
      this.surFlux = surFlux;
   }

}
