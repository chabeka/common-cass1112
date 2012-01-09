package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour le test 102
 */
public class Test102Formulaire extends TestWsParentFormulaire {


   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire() ;
   
   
   private final RechercheFormulaire recherche = new RechercheFormulaire() ;
   
   
   
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
