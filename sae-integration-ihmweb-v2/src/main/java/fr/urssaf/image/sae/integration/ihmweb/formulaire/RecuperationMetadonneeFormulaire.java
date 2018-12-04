package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

public class RecuperationMetadonneeFormulaire extends GenericForm {
   private ResultatTest resultats = new ResultatTest();
   
   public RecuperationMetadonneeFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   /**
    * Constructeur
    * 
    */
   public RecuperationMetadonneeFormulaire() {
      super();
   }

   public ResultatTest getResultats() {
      return resultats;
   }

   public void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

}
