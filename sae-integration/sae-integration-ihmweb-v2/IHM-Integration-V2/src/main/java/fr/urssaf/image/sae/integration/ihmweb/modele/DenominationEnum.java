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
    * Test libre OK
    */
   DENOMINATION_TEST_LIBRE_OK {

      @Override
      public String toString() {
         return "Test 3300-Copie-OK-Test-Libre";
      }

   },

   /**
    * Test libre OK COPIE
    */
   DENOMINATION_TEST_LIBRE_OK_COPIE {

      @Override
      public String toString() {
         return "Test 3300-Copie-OK-Test-Libre-Copie";
      }

   },

   /**
    * Cas simple OK
    */
   DENOMINATION_CAS_SIMPLE_OK {

      @Override
      public String toString() {
         return "Test 3301-Copie-OK-CasSimple";
      }

   },

   /**
    * Cas simple OK COPIE
    */
   DENOMINATION_CAS_SIMPLE_OK_COPIE {

      @Override
      public String toString() {
         return "Test 3301-Copie-OK-CasSimpleCopie";
      }

   },

   /**
    * Fonction activité OK
    */
   DENOMINATION_FONCTION_ACTIVITE_OK {

      @Override
      public String toString() {
         return "Test 3302-Copie-OK-FonctionActivite";
      }

   },

   /**
    * Fonction activité OK COPIE
    */
   DENOMINATION_FONCTION_ACTIVITE_OK_COPIE {

      @Override
      public String toString() {
         return "Test 3302-Copie-OK-FonctionActiviteCopie";
      }

   },

   /**
    * Sans code activité OK
    */
   DENOMINATION_SANS_CODE_ACTIVITE_OK {

      @Override
      public String toString() {
         return "Test 3303-Copie-OK-SansCodeActivite";
      }

   },

   /**
    * Sans code activité OK COPIE
    */
   DENOMINATION_SANS_CODE_ACTIVITE_OK_COPIE {

      @Override
      public String toString() {
         return "Test 3303-Copie-OK-SansCodeActiviteCopie";
      }

   },

   /**
    * Meta avec dico OK
    */
   DENOMINATION_META_AVEC_DICO_OK {

      @Override
      public String toString() {
         return "Test 3305-Copie-OK-MetaAvecDico";
      }

   },

   /**
    * Meta avec dico OK COPIE
    */
   DENOMINATION_META_AVEC_DICO_OK_COPIE {

      @Override
      public String toString() {
         return "Test 3305-Copie-OK-MetaAvecDicoCopie";
      }

   },

   /**
    * Meta non renseignée OK
    */
   DENOMINATION_META_NON_RENSEIGNEE_OK {

      @Override
      public String toString() {
         return "Test 3306-Copie-OK-MetaNonRenseignee";
      }

   },

   /**
    * Meta non renseignée OK COPIE
    */
   DENOMINATION_META_NON_RENSEIGNEE_OK_COPIE {

      @Override
      public String toString() {
         return "Test 3306-Copie-OK-MetaNonRenseigneeCopie";
      }

   },

   /**
    * Code RND inexistant OK
    */
   DENOMINATION_CODE_RND_INEXISTANT_KO {

      @Override
      public String toString() {
         return "Test 3351-Copie-KO-CodeRNDInexistant";
      }

   },

   /**
    * Code RND inexistant OK COPIE
    */
   DENOMINATION_CODE_RND_INEXISTANT_KO_COPIE {

      @Override
      public String toString() {
         return "Test 3351-Copie-KO-CodeRNDInexistantCopie";
      }

   },

   /**
    * Code RND non autorisé KO
    */
   DENOMINATION_CODE_RND_NON_AUTORISE_KO {

      @Override
      public String toString() {
         return "Test 3352-Copie-KO-CodeRNDNonAutorise";
      }

   },

   /**
    * Code RND non autorisé KO COPIE
    */
   DENOMINATION_CODE_RND_NON_AUTORISE_KO_COPIE {

      @Override
      public String toString() {
         return "Test 3352-Copie-KO-CodeRNDNonAutoriseCopie";
      }

   },

   /**
    * Meta non modifible KO
    */
   DENOMINATION_META_NON_MODIFIABLE_KO {

      @Override
      public String toString() {
         return "Test 3353-Copie-KO-MetaNonModifiable";
      }

   },

   /**
    * Meta non modifible KO COPIE
    */
   DENOMINATION_META_NON_MODIFIABLE_KO_COPIE {

      @Override
      public String toString() {
         return "Test 3353-Copie-KO-MetaNonModifiableCopie";
      }

   }

};
