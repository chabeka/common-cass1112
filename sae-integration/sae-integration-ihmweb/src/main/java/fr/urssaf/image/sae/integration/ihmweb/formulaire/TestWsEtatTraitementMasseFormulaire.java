package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "etatTraitementMasse" du WS SaeService
 */
public class TestWsEtatTraitementMasseFormulaire extends TestWsParentFormulaire {

   private final EtatTraitementMasseFormulaire etatTraitementMasse = new EtatTraitementMasseFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "etatTraitementMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "etatTraitementMasse"
    */
   public final EtatTraitementMasseFormulaire getEtatTraitementMasse() {
      return this.etatTraitementMasse;
   }

}
