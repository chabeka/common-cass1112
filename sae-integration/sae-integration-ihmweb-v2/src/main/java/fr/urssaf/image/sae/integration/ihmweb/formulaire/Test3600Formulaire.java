package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class Test3600Formulaire extends TestWsParentFormulaire {
   
   private final TransfertMasseFormulaire transMasseDecl = new TransfertMasseFormulaire(this);
   
   private final TransfertMasseResultatFormulaire transMasseResult = new TransfertMasseResultatFormulaire();

   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);
   
   private final CaptureMasseFormulaire captMasseDecl = new CaptureMasseFormulaire(this);
   
   private final CaptureMasseResultatFormulaire captMasseResult = new CaptureMasseResultatFormulaire();
   
   

   public CaptureMasseResultatFormulaire getCaptureMasseResultat() {
      return captMasseResult;
   }

   public CaptureMasseFormulaire getCaptureMasseDeclenchement() {
      return captMasseDecl;
   }

   public TransfertMasseFormulaire getTransfertMasseDeclenchement() {
      return this.transMasseDecl;
   }

   public TransfertMasseResultatFormulaire getTransfertMasseResultat() {
      return this.transMasseResult;
   }

   public ComptagesTdmFormulaire getComptagesFormulaire() {
      return comptagesFormulaire;
   }
   
   
}
