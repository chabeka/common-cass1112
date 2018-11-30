package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour le test 103
 */
public class Test103Formulaire extends TestWsParentFormulaire {


   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   
   private final RechercheFormulaire recherche = new RechercheFormulaire(this) ;
   
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }
   
      
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "recherche"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "recherche"
    */
   public final RechercheFormulaire getRecherche() {
      return this.recherche;
   }

   
}
