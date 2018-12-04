package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class TestWsConsultationGNTGNSFormulaire extends TestWsParentFormulaire {

   private final ConsultationGNTGNSFormulaire consultationGNTGNS = new ConsultationGNTGNSFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "consultation"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultation"
    */
   public final ConsultationGNTGNSFormulaire getConsultation() {
      return this.consultationGNTGNS;
   }

}
