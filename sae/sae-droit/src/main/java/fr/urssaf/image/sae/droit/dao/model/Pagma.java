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

}
