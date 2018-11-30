package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de formulaire
 */
public class Test319Formulaire extends TestWsParentFormulaire {

   private List<RechercheFormulaire> rechFormulaireList = new ArrayList<RechercheFormulaire>();

   public Test319Formulaire() {
      super();
      // LazyList.decorate(rechFormulaireList,
      // FactoryUtils.instantiateFactory(RechercheFormulaire.class));
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

   public void setRechFormulaireList(List<RechercheFormulaire> recherche) {
      this.rechFormulaireList = recherche;
   }

}
