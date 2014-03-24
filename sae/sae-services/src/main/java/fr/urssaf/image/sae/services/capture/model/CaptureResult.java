package fr.urssaf.image.sae.services.capture.model;

import java.util.UUID;

/**
 * Objet contenant le résultat de la capture unitaire.
 */
public class CaptureResult {
   private UUID idDoc;
   private boolean identificationActivee;
   private boolean identificationEchecMonitor;
   private String idFormatReconnu;
   private boolean validationActivee;
   private boolean validationEchecMonitor;
   private String detailEchecValidation;

   /**
    * Méthode permettant de récupérer l'identifiant du document archivé.
    * 
    * @return UUID
    */
   public final UUID getIdDoc() {
      return idDoc;
   }

   /**
    * Méthode permettant de mettre à jour l'identifiant du document archivé.
    * 
    * @param idDoc
    *           identifiant du document archivé
    */
   public final void setIdDoc(UUID idDoc) {
      this.idDoc = idDoc;
   }

   /**
    * Flag indiquant si l'étape d'identification est activée sur le profil de
    * contrôle
    * 
    * @return Flag indiquant si l'étape d'identification est activée sur le
    *         profil de contrôle
    */
   public final boolean isIdentificationActivee() {
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
   public final void setIdentificationActivee(boolean identificationActivee) {
      this.identificationActivee = identificationActivee;
   }

   /**
    * Flag indiquant si l'étape d'identification a échoué en mode monitor
    * 
    * @return Flag indiquant si l'étape d'identification a échoué en mode
    *         monitor
    */
   public final boolean isIdentificationEchecMonitor() {
      return identificationEchecMonitor;
   }

   /**
    * Flag indiquant si l'étape d'identification a échoué en mode monitor
    * 
    * @param identificationEchecMonitor
    *           Flag indiquant si l'étape d'identification a échoué en mode
    *           monitor
    */
   public final void setIdentificationEchecMonitor(
         boolean identificationEchecMonitor) {
      this.identificationEchecMonitor = identificationEchecMonitor;
   }

   /**
    * Flag indiquant si l'étape de validation est activée sur le profil de
    * contrôle
    * 
    * @return Flag indiquant si l'étape de validation est activée sur le profil
    *         de contrôle
    */
   public final boolean isValidationActivee() {
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
   public final void setValidationActivee(boolean validationActivee) {
      this.validationActivee = validationActivee;
   }

   /**
    * Flag indiquant si l'étape de validation a échoué en mode monitor
    * 
    * @return Flag indiquant si l'étape de validation a échoué en mode monitor
    */
   public final boolean isValidationEchecMonitor() {
      return validationEchecMonitor;
   }

   /**
    * Flag indiquant si l'étape de validation a échoué en mode monitor
    * 
    * @param validationEchecMonitor
    *           Flag indiquant si l'étape de validation a échoué en mode monitor
    */
   public final void setValidationEchecMonitor(boolean validationEchecMonitor) {
      this.validationEchecMonitor = validationEchecMonitor;
   }

   /**
    * Identifiant du format reconnu
    * 
    * @return Identifiant du format reconnu
    */
   public final String getIdFormatReconnu() {
      return idFormatReconnu;
   }

   /**
    * Identifiant du format reconnu
    * 
    * @param idFormatReconnu
    *           Identifiant du format reconnu
    */
   public final void setIdFormatReconnu(String idFormatReconnu) {
      this.idFormatReconnu = idFormatReconnu;
   }

   /**
    * Détail des échecs de validation
    * 
    * @return Détail des échecs de validation
    */
   public final String getDetailEchecValidation() {
      return detailEchecValidation;
   }

   /**
    * Détail des échecs de validation
    * 
    * @param detailEchecValidation
    *           Détail des échecs de validation
    */
   public final void setDetailEchecValidation(String detailEchecValidation) {
      this.detailEchecValidation = detailEchecValidation;
   }
}
