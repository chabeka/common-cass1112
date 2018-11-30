package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "recherche" du WS SaeService
 */
public class TestWsRechercheFormulaire extends TestWsParentFormulaire {

   private final RechercheFormulaire recherche = new RechercheFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "recherche"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "recherche"
    */
   public final RechercheFormulaire getRecherche() {
      return this.recherche;
   }

}
