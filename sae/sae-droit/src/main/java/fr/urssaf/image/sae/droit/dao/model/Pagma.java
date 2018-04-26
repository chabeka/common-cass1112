/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;

/**
 * Classe de modèle d'un PAGMa
 * 
 */
public class Pagma {

   /** code unique du PAGMa */
   private String code;

   /** liste des codes des actions unitaires du PAGMa */
   private List<String> actionUnitaires;

   /**
    * @return le code unique du PAGMa
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           code unique du PAGMa
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return la liste des codes des actions unitaires du PAGMa
    */
   public final List<String> getActionUnitaires() {
      return actionUnitaires;
   }

   /**
    * @param actionUnitaires
    *           liste des codes des actions unitaires du PAGMa
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

      if (obj instanceof Pagma) {
         Pagma pagma = (Pagma) obj;
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
