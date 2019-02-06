package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class Test3700Formulaire extends TestWsParentFormulaire {
   
   private final ModificationMasseFormulaire modifMasseDecl =  new ModificationMasseFormulaire(this);
   
   private final ModificationMasseResultatFormulaire modifMasseResult = new ModificationMasseResultatFormulaire();
   
   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
 private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(this);
   
   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   

   public CaptureMasseResultatFormulaire getCaptureMasseResultat() {
      return captMasseResult;
   }

   public CaptureMasseFormulaire getCaptureMasseDeclenchement() {
      return captMasseDecl;
   }

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
