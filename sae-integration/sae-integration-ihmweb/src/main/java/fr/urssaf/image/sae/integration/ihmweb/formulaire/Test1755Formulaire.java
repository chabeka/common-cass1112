package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire
 */
public class Test1755Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);

   private final ModificationFormulaire modification = new ModificationFormulaire(
         this);
   
   private RechercheFormulaire rechFormulaire = new RechercheFormulaire(this);
   
   private RechercheFormulaire rechFormulaireApresModif = new RechercheFormulaire(this);
   
  
   public Test1755Formulaire() {
    super();
    //LazyList.decorate(rechFormulaireList, FactoryUtils.instantiateFactory(RechercheFormulaire.class));
   }
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }

   /**
    * Le sous-formulaire pour la recherche du résultat du traitement de masse
    * effectué auparavant
    * 
    * @return le sous-formulaire de recherche de document
    */
   public final RechercheFormulaire getRechFormulaire() {
      return this.rechFormulaire;
   }
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "modification"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "modification"
    */
   public final ModificationFormulaire getModification() {
      return this.modification;
   }

   /**
    * Le sous-formulaire pour la recherche du résultat du traitement de masse
    * effectué auparavant
    * 
    * @return le sous-formulaire de recherche de document
    */
   public final RechercheFormulaire getRechFormulaireApresModif() {
      return this.rechFormulaireApresModif;
   }
}
