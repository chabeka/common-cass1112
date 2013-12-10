package fr.urssaf.image.sae.droit.model;

import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;

public class SaePagmp {

   /** 
    * identifiant unique du PAGMp 
    **/
   private String code;

   /**
    * Description du PAGMp
    */
   private String description;   
   
   /**
    * PRMD associé
    */
   private String prmd;

   /**
    * @return the code
    */
   public String getCode() {
      return code;
   }

   /**
    * @param code the code to set
    */
   public void setCode(String code) {
      this.code = code;
   }

   /**
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return the prmd
    */
   public String getPrmd() {
      return prmd;
   }

   /**
    * @param prmd the prmd to set
    */
   public void setPrmd(String prmd) {
      this.prmd = prmd;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof SaePagmp) {
         SaePagmp pagmp = (SaePagmp) obj;
         areEquals = code.equals(pagmp.getCode())
               && description.equals(pagmp.getDescription())
               && prmd.equals(pagmp.getPrmd());
      }

      return areEquals;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      return "code : " + code + "\ndescription : " + description + "\nprmd : "
            + prmd;
   }
   
}
