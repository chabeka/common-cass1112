package fr.urssaf.image.sae.integration.ihmweb.formulaire;


import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.UUIDList;

public class DeblocageFormulaire extends GenericForm {
   
   private ResultatTest resultats = new ResultatTest();
   
   private UUIDList idJob = new UUIDList();
   
   public DeblocageFormulaire(TestWsParentFormulaire parent)
   {
      super(parent);
   }

   public ResultatTest getResultats() {
      return resultats;
   }

   public void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

   public UUIDList getIdJob() {
      return idJob;
   }

   public void setIdJob(UUIDList idJob) {
      this.idJob = idJob;
   }
   
   
}
