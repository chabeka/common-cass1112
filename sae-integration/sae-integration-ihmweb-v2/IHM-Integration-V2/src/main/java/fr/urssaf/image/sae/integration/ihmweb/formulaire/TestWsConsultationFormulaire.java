package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "consultation" du WS SaeService
 */
public class TestWsConsultationFormulaire extends TestWsParentFormulaire {

   private final ConsultationFormulaire consultation = new ConsultationFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "consultation"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultation"
    */
   public final ConsultationFormulaire getConsultation() {
      return this.consultation;
   }

}
