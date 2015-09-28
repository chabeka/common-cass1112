package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour le test 2501
 */
public class Test2501Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
      
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
    * Le sous-formulaire pour l'appel à l'opération "consultation"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultation"
    */
   public final ConsultationFormulaire getConsultation() {
      return this.consultation;
   }
}
