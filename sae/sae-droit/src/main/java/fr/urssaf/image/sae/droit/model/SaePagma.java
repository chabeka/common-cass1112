package fr.urssaf.image.sae.droit.model;

import java.util.List;

/**
 * Classe représentant les PAGMa
 * 
 *
 */
public class SaePagma {

   /**
    * Code du PAGMa
    */
   private String code;

   /**
    * Liste des codes actions unitaires
    */
   private List<String> actionUnitaires;

   /**
    * @return the code
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           the code to set
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return the actionUnitaires
    */
   public final List<String> getActionUnitaires() {
      return actionUnitaires;
   }

   /**
    * @param actionUnitaires
    *           the actionUnitaires to set
    */
   public final void setActionUnitaires(List<String> actionUnitaires) {
      this.actionUnitaires = actionUnitaires;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof SaePagma) {
         SaePagma pagma = (SaePagma) obj;
         areEquals = code.equals(pagma.getCode())
               && actionUnitaires.size() == pagma.getActionUnitaires().size()
               && actionUnitaires.containsAll(pagma.getActionUnitaires());
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
      StringBuffer buffer = new StringBuffer();
      for (String action : actionUnitaires) {
         buffer.append("action = " + action + "\n");
      }

      return "code : " + code + "\nactions :\n" + buffer.toString();
   }
   
}