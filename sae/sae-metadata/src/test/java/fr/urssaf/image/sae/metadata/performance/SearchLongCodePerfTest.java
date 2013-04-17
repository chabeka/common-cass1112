package fr.urssaf.image.sae.metadata.performance;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

/**
 * Classe de test pour la recherche .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class SearchLongCodePerfTest {
	
	 public  static   long maxElapsedTime = 800;
	 public  static   int users = 5;	
   /**
    * Test de performance de l'insertion en masse.
    * 
    * @return Temps d'exécution.
    */
   @SuppressWarnings("PMD.JUnit4SuitesShouldUseSuiteAnnotation")
   public static Test suite() {
   final   SearchLongCodeTestPerfFactory factory = new SearchLongCodeTestPerfFactory(
            SearchLongCodeTest.class);
  final    TestSuite testCase = factory.makeTestSuite();
      final Test loadTest = new LoadTest(testCase, users);
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
