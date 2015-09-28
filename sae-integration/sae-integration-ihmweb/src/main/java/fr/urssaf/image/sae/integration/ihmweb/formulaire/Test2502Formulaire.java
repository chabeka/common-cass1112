package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour le test 2502
 */
public class Test2502Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   private final AjoutNoteFormulaire ajoutNote = new AjoutNoteFormulaire(this) ;      
   
   private final ConsultationFormulaire consultation = new ConsultationFormulaire(this);
   
   private String dernierSha1;
   
   /**
    * Sha1 du dernier doc capturé
    * @return Sha1 du dernier doc capturé
    */
   public String getDernierSha1() {
      return dernierSha1;
   }

   /**
    * Sha1 du dernier doc capturé
    */
   public void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }

   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   } 

   /**
    * Le sous-formulaire pour l'appel à l'opération "ajoutNote"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "ajoutNote"
    */
   public final AjoutNoteFormulaire getAjoutNote() {
      return this.ajoutNote;
   } 
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "consultation"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultation"
    */
   public final ConsultationFormulaire getConsultation() {
      return this.consultation;
   }
}
