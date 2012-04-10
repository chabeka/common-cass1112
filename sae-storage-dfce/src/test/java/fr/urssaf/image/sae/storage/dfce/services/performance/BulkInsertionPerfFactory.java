package fr.urssaf.image.sae.storage.dfce.services.performance;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TestFactory;

/**
 * Test de performance.
 */

public class BulkInsertionPerfFactory extends TestFactory {
   @SuppressWarnings("PMD.LongVariable")
   private final Class<?> bulkInsertionTestClass;

   /**
    *Test de performance pour insertion en masse
    */
   @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
   public static class BulkInsertionServiceProviderTest extends TestCase {
      /**
       *Test de performance pour insertion en masse
       */
      public void test() {
         // rien faire.
      }
   }

   /**
    * 
    * @param testClass
    *           : classe de test.
    */
   public BulkInsertionPerfFactory(final Class<?> testClass) {
      super(BulkInsertionServiceProviderTest.class);
      this.bulkInsertionTestClass = testClass;
   }

   /**
    *Test de performance pour insertion en masse
    */
   @Override
   protected final TestSuite makeTestSuite() {
      JUnit4TestAdapter unit4TestAdapter = new JUnit4TestAdapter(
            this.bulkInsertionTestClass);
      TestSuite testSuite = new TestSuite("JUnit4TestFactory");
      testSuite.addTest(unit4TestAdapter);

      return testSuite;
   }
}
