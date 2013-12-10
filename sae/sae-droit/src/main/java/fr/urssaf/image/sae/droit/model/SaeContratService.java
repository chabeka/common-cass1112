package fr.urssaf.image.sae.droit.model;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;

public class SaeContratService extends ServiceContract {

  
   /**
    * Liste des PAGM du contrat de service
    */
   private List<SaePagm> saePagms;

   /**
    * Liste des Prmd rattachés au contrat de service
    */
   private List<SaePrmd> saePrmds;
   
   /**
    * @return the saePagms
    */
   public List<SaePagm> getSaePagms() {
      return saePagms;
   }

   /**
    * @param saePagms the saePagms to set
    */
   public void setSaePagms(List<SaePagm> saePagms) {
      this.saePagms = saePagms;
   }

   /**
    * @return the saePrmd
    */
   public List<SaePrmd> getSaePrmds() {
      return saePrmds;
   }

   /**
    * @param saePrmd the saePrmd to set
    */
   public void setSaePrmds(List<SaePrmd> saePrmds) {
      this.saePrmds = saePrmds;
   }

}
