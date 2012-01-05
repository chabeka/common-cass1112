package fr.urssaf.image.sae.storage.dfce.services.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.dfce.services.provider.impl.BulkInsertionServiceProviderTest;

/**
 * Classe de test pour l'insertion en masse.
 */
public class BulkInsertionPerfTest extends CommonsServicesProvider {
   /**
    * Test de performance de l'insertion en masse.
    * 
    * @return Temps d'exécution.
    */
   @SuppressWarnings("PMD.JUnit4SuitesShouldUseSuiteAnnotation")
   public static Test suite() {
      long maxElapsedTime = 180000000;
      int users = 5;
      BulkInsertionPerfFactory factory = new BulkInsertionPerfFactory(
            BulkInsertionServiceProviderTest.class);
      TestSuite testCase = factory.makeTestSuite();
      Test loadTest = new LoadTest(testCase, users);
      return new TimedTest(loadTest, maxElapsedTime);
   }

   /**
    * Test de performance de l'insertion en masse.
    */
   @org.junit.Test
   public void validateInsertionPerf() {
      junit.textui.TestRunner.run(suite());
   }

}
