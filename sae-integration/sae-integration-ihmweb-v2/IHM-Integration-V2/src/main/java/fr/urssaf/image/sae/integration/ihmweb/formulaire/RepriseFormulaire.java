package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.UUIDList;

public class RepriseFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();
   
   private UUIDList idJob = new UUIDList();
   
   public RepriseFormulaire(TestWsParentFormulaire parent){
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
