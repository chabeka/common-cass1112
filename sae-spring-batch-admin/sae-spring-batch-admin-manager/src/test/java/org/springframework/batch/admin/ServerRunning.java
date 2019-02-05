package org.springframework.batch.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.client.RestTemplate;

public class ServerRunning extends TestWatcher {

  private static Log logger = LogFactory.getLog(ServerRunning.class);

  private boolean serverOnline = true;

  private final String url;

  /**
   * {@inheritDoc}
   */
  @Override
  public Statement apply(final Statement base, final Description description) {
    // Check at the beginning, so this can be used as a static field
    Assume.assumeTrue(serverOnline);

    try {

      final RestTemplate template = new RestTemplate();
      final ResponseEntity<String> result = template.exchange(url + "/home.json",
                                                              HttpMethod.GET,
                                                              null,
                                                              String.class);
      final String body = result.getBody();
      Assert.assertTrue("No home page found", body != null && body.length() > 0);

    }
    catch (final Exception e) {
      logger.warn("Not executing tests because basic connectivity test failed", e);
      serverOnline = false;
      Assume.assumeNoException(e);
    }

    return super.apply(base, description);
  }

  /**
   * @return a new rule that assumes an existing running broker
   */
  public static ServerRunning isRunning(final String url) {
    return new ServerRunning(SystemPropertyUtils.resolvePlaceholders(url));
  }

  private ServerRunning(final String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

}
