/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTests;

/**
 * 
 * 
 */
public class ListeCasDeTestFormulaire {

   /**
    * Liste des cas de tests
    */
   private EcdeTests ecdeTests;

   /**
    * Cas de test en mode saisie
    */
   private EcdeTest ecdeTest;

   /**
    * @return the ecdeTests
    */
   public EcdeTests getEcdeTests() {
      return ecdeTests;
   }

   /**
    * @param ecdeTests
    *           the ecdeTests to set
    */
   public void setEcdeTests(EcdeTests ecdeTests) {
      this.ecdeTests = ecdeTests;
   }

   /**
    * @return the ecdeTest
    */
   public EcdeTest getEcdeTest() {
      return ecdeTest;
   }

   /**
    * @param ecdeTest
    *           the ecdeTest to set
    */
   public void setEcdeTest(EcdeTest ecdeTest) {
      this.ecdeTest = ecdeTest;
   }

}
