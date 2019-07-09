/**
 *  TODO (AC75094891) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import org.junit.Assert;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionCapturePoolThreadExecutor;

/**
 * TODO (AC75094891) Description du type
 */
public class PoolExecutorTestExecutionListener extends AbstractTestExecutionListener {

  private InsertionCapturePoolThreadExecutor poolExecutorInsertion;

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterTestClass(final TestContext testContext) throws Exception {
    super.afterTestClass(testContext);

    poolExecutorInsertion = testContext.getApplicationContext().getBean(InsertionCapturePoolThreadExecutor.class);

    try {
      if (!poolExecutorInsertion.isShutdown()) {
        poolExecutorInsertion.destroy();
      }

    }
    catch (final Exception e) {
      Assert.fail("Shutdown pool thread KO");
    }

  }

}
