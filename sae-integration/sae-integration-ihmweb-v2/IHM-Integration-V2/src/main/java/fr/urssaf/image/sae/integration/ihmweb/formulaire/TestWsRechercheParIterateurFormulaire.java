package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "recherche" du WS SaeService
 */
public class TestWsRechercheParIterateurFormulaire extends TestWsParentFormulaire {

   private final RechercheParIterateurFormulaire recherche = new RechercheParIterateurFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "recherche"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "recherche"
    */
   public final RechercheParIterateurFormulaire getRecherche() {
      return this.recherche;
   }

}
