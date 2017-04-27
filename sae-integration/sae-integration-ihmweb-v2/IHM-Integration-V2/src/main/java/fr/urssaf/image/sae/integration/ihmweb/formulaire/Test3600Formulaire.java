package fr.urssaf.image.sae.integration.ihmweb.formulaire;

public class Test3600Formulaire extends TestWsParentFormulaire {
   
   private final TransfertMasseFormulaire transMasseDecl = new TransfertMasseFormulaire(this);
   
   private final TransfertMasseResultatFormulaire transMasseResult = new TransfertMasseResultatFormulaire();

   private final ComptagesTdmFormulaire comptagesFormulaire = new ComptagesTdmFormulaire(this);

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
