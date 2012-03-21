package fr.urssaf.image.sae.metadata.performance;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TestFactory;

/**
 * Test de performance.
 */

public class SearchLongCodeTestPerfFactory extends TestFactory {
	@SuppressWarnings("PMD.LongVariable")
	private  Class<?> searchTestClass;

	/**
	 * @param searchTestClass : La classe à tester
	 */
	public final void setSearchTestClass(final Class<?> searchTestClass) {
		this.searchTestClass = searchTestClass;
	}

	/**
	 * @return La classe à tester
	 */
	public final Class<?> getSearchTestClass() {
		return searchTestClass;
	}

	/**
	 * Test de performance pour insertion en masse
	 */
	@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
	public static class SearchLongCodeTest extends TestCase {
		/**
		 * Test de performance pour la recherche du code court à partir du code
		 * long.
		 */
		public void test() {
			// rien faire.
		}
	}

	/**
	 * 
	 * @param testClass
	 *            : classe de test.
	 */
	public SearchLongCodeTestPerfFactory(final Class<?> testClass) {
		super(SearchLongCodeTest.class);
		this.searchTestClass = testClass;
	}

	/**
	 * Test de performance pour insertion en masse
	 */
	@Override
	protected final TestSuite makeTestSuite() {
		final JUnit4TestAdapter unit4TestAdapter = new JUnit4TestAdapter(
				this.searchTestClass);
		final TestSuite testSuite = new TestSuite("JUnit4TestFactory");
		testSuite.addTest(unit4TestAdapter);
		return testSuite;
	}
}
