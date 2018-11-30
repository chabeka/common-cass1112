package fr.urssaf.image.sae.integration.ihmweb.modele;

import org.apache.commons.lang.StringUtils;


/**
 * Statuts possibles pour le résultat d'un test
 */
public enum TestStatusEnum{ 
   
   /**
    * Succès
    */
   Succes {
     
      @Override
      public String toString() {
         return "Succès";
      }
      
   },
   
   
   /**
    * Echec
    */
   Echec {
     
      @Override
      public String toString() {
         return "Echec";
      }
      
   },
   
   /**
    * Le test n'a pas été passé
    */
   NonPasse {
     
      @Override
      public String toString() {
         return "Non passé";
      }
      
   },
   
   
   /**
    * Le test n'a pas été lancé
    */
   NonLance {
     
      @Override
      public String toString() {
         return "Non lancé";
      }
      
   },
   
   
   /**
    * Aucun status pour le test (pour les tests "libres" par exemple)
    */
   SansStatus {
      
      @Override
      public String toString() {
         return StringUtils.EMPTY;
      }
      
   },
   
   
   /**
    * A contrôler manuellement : on n'a pas eu d'erreurs, mais on n'est pas sûr du résultat
    */
   AControler {
      
      @Override
      public String toString() {
         return "A contrôler";
      }
      
   }
};
