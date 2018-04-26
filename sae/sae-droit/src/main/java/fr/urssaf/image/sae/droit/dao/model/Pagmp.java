/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

/**
 * Classe de modèle d'un PAGMp
 * 
 */
public class Pagmp {

   /** identifiant unique du PAGMp */
   private String code;

   /** code du PRMD correspondant */
   private String prmd;

   /** description du référentiel du PRMD */
   private String description;

   /**
    * @return l'identifiant unique du PAGMp
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           identifiant unique du PAGMp
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return le code du PRMD
    */
   public final String getPrmd() {
      return prmd;
   }

   /**
    * @param prmd
    *           code du PRMD
    */
   public final void setPrmd(String prmd) {
      this.prmd = prmd;
   }

   /**
    * @return the description
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           the description to set
    */
   public final void setDescription(String description) {
      this.description = description;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof Pagmp) {
         Pagmp pagmp = (Pagmp) obj;
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
