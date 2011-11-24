package fr.urssaf.image.sae.storage.dfce.services.performance;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TestFactory;

/**
 * Fournit une instance de service d'insertion
 * 
 * @author akenore, rhofir.
 * 
 */

public class InsertionPerfFactory extends TestFactory {
   /**
    *Test pour insertion.
    */
   @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
   public static class InsertionServiceProviderTest extends TestCase {
      /**
       * Test pour insertion.
       */
      public void test() {
         // ici on fait rien
      }
   }

   @SuppressWarnings("PMD.LongVariable")
   private final Class<?> insertionTestClass;

   /**
    * Test de performance.
    * 
    * @param testClass
    *           : classe à tester.
    */
   public InsertionPerfFactory(final Class<?> testClass) {
      super(InsertionServiceProviderTest.class);
      this.insertionTestClass = testClass;
   }

   /**
    * TestSuite.
    */
   @Override
   protected final TestSuite makeTestSuite() {
      JUnit4TestAdapter unit4TestAdapter = new JUnit4TestAdapter(
            this.insertionTestClass);
      TestSuite testSuite = new TestSuite("JUnit4TestFactory");
      testSuite.addTest(unit4TestAdapter);

      return testSuite;
   }
}
