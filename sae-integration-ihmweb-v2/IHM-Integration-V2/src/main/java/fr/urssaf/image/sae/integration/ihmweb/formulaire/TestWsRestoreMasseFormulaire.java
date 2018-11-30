package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "restoreMasse" du WS SaeService
 */
public class TestWsRestoreMasseFormulaire extends TestWsParentFormulaire {

   private final RestoreMasseFormulaire restoreMasse = new RestoreMasseFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "restoreMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "restoreMasse"
    */
   public final RestoreMasseFormulaire getRestoreMasse() {
      return this.restoreMasse;
   }

}
