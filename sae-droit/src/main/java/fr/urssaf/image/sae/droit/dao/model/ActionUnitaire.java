/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

/**
 * Classe de modèle d'une action unitaire
 * 
 */
public class ActionUnitaire {

   /** identifiant unique de l'action unitaire. */
   private String code;

   /** description de l'action unitaire */
   private String description;

   /**
    * @return l'identifiant unique de l'action unitaire
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           identifiant unique de l'action unitaire
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return la description de l'action unitaire
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           description de l'action unitaire
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

      if (obj instanceof ActionUnitaire) {
         ActionUnitaire actionUnitaire = (ActionUnitaire) obj;
         areEquals = code.equals(actionUnitaire.getCode())
               && description.equals(actionUnitaire.getDescription());
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
      return "code : " + code + "\ndescription : " + description;
   }

}
