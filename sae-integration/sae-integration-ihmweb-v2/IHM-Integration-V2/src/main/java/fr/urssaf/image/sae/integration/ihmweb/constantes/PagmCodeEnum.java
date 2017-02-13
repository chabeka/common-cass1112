/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.constantes;

/**
 * Enumeration des actions composant un PAGM.
 */
public enum PagmCodeEnum {
   /**
    * PAGM_TOUTES_ACTIONS
    */
   PAGM_TOUTES_ACTIONS {

      @Override
      public String toString() {
         return "PAGM_TOUTES_ACTIONS";
      }

   },
   /**
    * PAGM_TOUTES_ACTIONS_NAT
    */
   PAGM_TOUTES_ACTIONS_NAT {

      @Override
      public String toString() {
         return "PAGM_TOUTES_ACTIONS_NAT";
      }

   },
   /**
    * INT_PAGM_COPIE_ALL
    */
   INT_PAGM_COPIE_ALL {

      @Override
      public String toString() {
         return "INT_PAGM_COPIE_ALL";
      }

   },
   /**
    * INT_PAGM_COPIE_COPIE
    */
   INT_PAGM_COPIE_COPIE {

      @Override
      public String toString() {
         return "INT_PAGM_COPIE_COPIE";
      }
   },
   /**
    * INT_PAGM_COPIE_COPIE_LIMITE
    */
   INT_PAGM_COPIE_COPIE_LIMITE {

      @Override
      public String toString() {
         return "INT_PAGM_COPIE_COPIE_LIMITE ";
      }
   },
   /**
    * INT_PAGM_COPIE_SANS_COPIE
    */
   INT_PAGM_COPIE_SANS_COPIE {

      @Override
      public String toString() {
         return "INT_PAGM_COPIE_SANS_COPIE";
      }
   }

}
