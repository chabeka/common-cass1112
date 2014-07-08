package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire pour les tests qui comprennent un appel à
 * l'opération 'archivage unitaire', puis à l'opération de 'recherche', puis à l'opération 
 * de 'suppression' du WS SaeService
 */
public class TestFormulaireSuppressionCuRe extends TestWsParentFormulaire {
   
   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);
   
   private final RechercheFormulaire rechercheFormulaire = new RechercheFormulaire(this);

   private final SuppressionFormulaire suppression = new SuppressionFormulaire(
         this);
   
   private final RechercheFormulaire rechercheFormulaireApresSuppr = new RechercheFormulaire(this);
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }
   
   /**
    * Le sous formulaire de la recherche
    * @return le sous formulaire de la recherche
    */

   public RechercheFormulaire getRecherche() {
      return rechercheFormulaire;
   }

   /**
    * Le sous-formulaire pour l'appel à l'opération "modification"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "modification"
    */
   public final SuppressionFormulaire getSuppression() {
      return this.suppression;
   }

   /**
    * Le sous formulaire de la recherche
    * @return le sous formulaire de la recherche
    */

   public RechercheFormulaire getRechercheApresSuppr() {
      return rechercheFormulaireApresSuppr;
   }
}
