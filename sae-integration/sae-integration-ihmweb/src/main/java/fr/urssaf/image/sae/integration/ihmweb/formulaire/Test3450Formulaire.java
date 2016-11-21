package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class Test3450Formulaire extends TestWsParentFormulaire {


   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   private final ConsultationGNTGNSFormulaire consultation = new ConsultationGNTGNSFormulaire(this);
   
   private String dernierIdArchiv;
   private String dernierSha1;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "consultationAffichable"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultationAffichable"
    */
   public final ConsultationGNTGNSFormulaire getConsultationGNTGNS() {
      return this.consultation;
   }
   
   /**
    * Le dernier id d'archivage obtenu en réponse de la capture unitaire
    * 
    * @return Le dernier id d'archivage obtenu en réponse de la capture unitaire
    */
   public final String getDernierIdArchivage() {
      return dernierIdArchiv;
   }

   
   /**
    * Le dernier id d'archivage obtenu en réponse de la capture unitaire
    * 
    * @param dernierIdArchiv Le dernier id d'archivage obtenu en réponse de la capture unitaire
    */
   public final void setDernierIdArchivage(String dernierIdArchiv) {
      this.dernierIdArchiv = dernierIdArchiv;
   }


   /**
    * Le SHA-1 du dernier document envoyé lors de la capture unitaire
    * 
    * @return Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final String getDernierSha1() {
      return dernierSha1;
   }


   /**
    * Le SHA-1 du dernier document envoyé lors de la capture unitaire
    * 
    * @param dernierSha1 Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }
   
   
}