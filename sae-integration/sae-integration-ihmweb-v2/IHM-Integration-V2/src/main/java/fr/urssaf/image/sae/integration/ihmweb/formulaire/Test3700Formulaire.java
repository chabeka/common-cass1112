package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class Test3700Formulaire extends TestWsParentFormulaire {
   
   private final ModificationMasseFormulaire modifMasseDecl =  new ModificationMasseFormulaire(this);
   
   private final ModificationMasseResultatFormulaire modifMasseResult = new ModificationMasseResultatFormulaire();
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);

   public ModificationMasseFormulaire getModifMasseDecl() {
      return modifMasseDecl;
   }

   public ModificationMasseResultatFormulaire getModifMasseResult() {
      return modifMasseResult;
   }

   public ComptagesTdmFormulaire getComptagesFormulaire() {
      return comptagesFormulaire;
   }
   
   

}
