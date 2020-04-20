package fr.urssaf.image.sae.webservices.security.igc.cql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.naming.NamingException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.igc.util.TextUtils;
import fr.urssaf.image.sae.webservices.security.igc.IgcFactory;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
@Ignore("TODO : Tests à reprendre car la factory ne se base plus sur une variable JNDI mais sur une clé d'un fichier Properties")
public class IgcFactoryCqlTest {

  private static final String FAIL_MSG = "le test doit échouer";

  private static final Logger LOG = LoggerFactory.getLogger(IgcFactoryCqlTest.class);

  private static final String EXPT_EXPECTED = "Exception non attendue";

  private SimpleNamingContextBuilder builder;

  private static final String IGC_CONFIG_JNDI = "java:comp/env/SAE_Fichier_Configuration_IGC";

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;
  @Before
  public void before() throws NamingException {

    builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);

  }

  private static ApplicationContext createApplicationContext() {

    return new ClassPathXmlApplicationContext(
                                              new String[] { "applicationContext-security.xml" });
  }

  @Test
  public void loadIgcConfig_failure_igcConfig_required() throws IOException,
  NamingException {

    // paramétrage de la variable JNDI
    builder.bind(IGC_CONFIG_JNDI, "");

    try {
      createApplicationContext();
      fail(FAIL_MSG);
    } catch (final Exception e) {

      final Throwable exception = ExceptionUtils.getRootCause(e);

      LOG.debug(exception.getMessage());

      assertEquals(EXPT_EXPECTED, IllegalArgumentException.class, exception
                   .getClass());

      assertEquals(EXPT_EXPECTED, IgcFactory.IGC_CONFIG_REQUIRED, exception
                   .getMessage());

    }

  }

  @Test
  public void loadIgcConfig_failure_igcConfig_notexist() throws IOException {

    final String igcConfig = "notexist.xml";

    builder.bind(IGC_CONFIG_JNDI, igcConfig);

    try {
      createApplicationContext();
      fail(FAIL_MSG);
    } catch (final Exception e) {

      final Throwable exception = ExceptionUtils.getRootCause(e);

      LOG.debug(exception.getMessage());

      assertEquals(EXPT_EXPECTED, IllegalArgumentException.class, exception
                   .getClass());

      assertEquals(EXPT_EXPECTED, TextUtils.getMessage(
                                                       IgcFactory.IGC_CONFIG_NOTEXIST, igcConfig), exception
                   .getMessage());

    }
  }

}
