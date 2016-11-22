package fr.urssaf.image.sae.integration.ihmweb.modele;



/**
 * Statuts possibles pour le résultat d'un test
 */
public enum DenominationEnum{ 
   
   /**
    * DENOMINATION
    */
   DENOMINATION {
     
      @Override
      public String toString() {
         return "Denomination";
      }
      
   },
   
   
   /**
    * Echec
    */
   DENOMINATION_TEST_LIBRE_OK {
     
      @Override
      public String toString() {
         return "Test 3300-Copie-OK-Test-Libre";
      }
      
   },
   
   /**
    * Le test n'a pas été passé
    */
   DENOMINATION_TEST_LIBRE_OK_COPIE {
     
      @Override
      public String toString() {
         return "Test 3300-Copie-OK-Test-Libre-Copie";
      }
      
   }
   
};
