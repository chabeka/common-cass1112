package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de formulaire
 */
public class Test1700Formulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(
         this);

   private final ModificationFormulaire modification = new ModificationFormulaire(
         this);
   
   private List<RechercheFormulaire> rechFormulaireList = new ArrayList<RechercheFormulaire>();
   
  
   public Test1700Formulaire() {
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
   public final List<RechercheFormulaire> getRechFormulaireList() {
      return this.rechFormulaireList;
   }
   
   public void setRechFormulaireList(List<RechercheFormulaire> recherche){
      this.rechFormulaireList = recherche;
   }

   /**
    * Le sous-formulaire pour l'appel à l'opération "modification"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "modification"
    */
   public final ModificationFormulaire getModification() {
      return this.modification;
   }

}
