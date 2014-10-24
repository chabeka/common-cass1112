package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "consultationAffichable" du WS SaeService
 */
public class TestWsConsultationAffichableFormulaire extends TestWsParentFormulaire {

   private final ConsultationAffichableFormulaire consultationAffichable = new ConsultationAffichableFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "consultationAffichable"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "consultationAffichable"
    */
   public final ConsultationAffichableFormulaire getConsultationAffichable() {
      return this.consultationAffichable;
   }

}
