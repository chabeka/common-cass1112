package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "suppressionMasse" du WS SaeService
 */
public class TestWsSuppressionMasseFormulaire extends TestWsParentFormulaire {

   private final SuppressionMasseFormulaire suppressionMasse = new SuppressionMasseFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "suppressionMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "suppressionMasse"
    */
   public final SuppressionMasseFormulaire getSuppressionMasse() {
      return this.suppressionMasse;
   }

}
