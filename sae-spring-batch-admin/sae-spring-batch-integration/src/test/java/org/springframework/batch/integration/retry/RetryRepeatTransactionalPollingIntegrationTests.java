package org.springframework.batch.integration.retry;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.support.transaction.TransactionAwareProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.Lifecycle;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@MessageEndpoint
public class RetryRepeatTransactionalPollingIntegrationTests implements ApplicationContextAware {

  private final Log logger = LogFactory.getLog(getClass());

  private volatile static List<String> list = new ArrayList<>();

  @Autowired
  private SimpleRecoverer recoverer;

  @Autowired
  private SimpleService service;

  private Lifecycle lifecycle;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    lifecycle = (Lifecycle) applicationContext;
  }

  private static volatile int count = 0;

  @After
  public void clearLists() {
    list.clear();
    count = 0;
  }

  public String input() {
    logger.debug("Polling: " + count);
    if (list.isEmpty()) {
      return null;
    }
    return list.remove(0);
  }

  public void output(final String message) {
    count++;
    logger.debug("Handled: " + message);
  }

  @Test
  @DirtiesContext
  public void testSunnyDay() throws Exception {
    list = TransactionAwareProxyFactory.createTransactionalList(Arrays.asList(StringUtils
                                                                                         .commaDelimitedListToStringArray("a,b,c,d,e,f,g,h,j,k")));
    final List<String> expected = Arrays.asList(StringUtils.commaDelimitedListToStringArray("a,b,c,d"));
    service.setExpected(expected);
    waitForResults(lifecycle, expected.size(), 60);
    assertEquals(4, service.getProcessed().size()); // a,b,c,d
    assertEquals(expected, service.getProcessed());
  }

  @Test
  @DirtiesContext
  public void testRollback() throws Exception {
    list = TransactionAwareProxyFactory.createTransactionalList(Arrays.asList(StringUtils
                                                                                         .commaDelimitedListToStringArray("a,b,fail,d,e,f,g,h,j,k")));
    final List<String> expected = Arrays.asList(StringUtils.commaDelimitedListToStringArray("a,b,fail,fail,d,e,f"));
    service.setExpected(expected);
    waitForResults(lifecycle, expected.size(), 200); // (a,b), (fail), (fail), ([fail],d), (e,f)
    System.err.println(service.getProcessed());
    assertEquals(7, service.getProcessed().size()); // a,b,fail,fail,d,e,f
    assertEquals(1, recoverer.getRecovered().size()); // fail
    assertEquals(expected, service.getProcessed());
  }

  private void waitForResults(final Lifecycle lifecycle, final int count, final int maxTries) throws InterruptedException {
    lifecycle.start();
    int timeout = 0;
    while (service.getProcessed().size() < count && timeout++ < maxTries) {
      Thread.sleep(1);
    }
    lifecycle.stop();
    while (lifecycle.isRunning()) {
      Thread.sleep(5);
      logger.debug("lifecycle.stop : En cours");
    }
    logger.debug("lifecycle.stop : Fin");
  }

}
