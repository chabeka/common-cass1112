package fr.urssaf.image.sae.storage.dfce.services.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.dfce.services.provider.impl.InsertionServiceProviderTest;

/**
 * Classe de test de performance pour l'insertion.
 * 
 *
 */
public class InsertionPerfTest extends CommonsServicesProvider {
   /**
    * Test de performance de l'insertion par le service provider
    * 
    * @return Temps d'exécution.
    */
   @SuppressWarnings("PMD.JUnit4SuitesShouldUseSuiteAnnotation")
   public static Test suite() {
      long maxElapsedTime = 50000000000L;
      int users = 15;
      InsertionPerfFactory factory = new InsertionPerfFactory(
            InsertionServiceProviderTest.class);
      TestSuite testCase = factory.makeTestSuite();
      Test loadTest = new LoadTest(testCase, users);
      return new TimedTest(loadTest, maxElapsedTime,false);
   }

   /**
    * Test de performance de l'insertion par le service provider
    */
   @org.junit.Test
   public void validateInsertionPerf() {
      junit.textui.TestRunner.run(suite());
   }
}
