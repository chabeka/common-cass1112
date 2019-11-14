package fr.urssaf.image.sae.rnd.service;

import java.util.Date;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.rnd.util.ModeAPIRndUtils;
import fr.urssaf.image.sae.rnd.utils.SaeLogAppender;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MajRndExceptionServiceTest {

  @Autowired
  private MajRndService majRndService;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private SaeBddSupport saeBddSupport;

  // Mocks
  @Autowired
  private RndRecuperationService rndRecuperationService;

  private Logger logger;

  private SaeLogAppender logAppender;

  @Before
  public void before() throws SaeBddRuntimeException, RndRecuperationException {
    EasyMock.resetToNice(rndRecuperationService);

    logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    logAppender = new SaeLogAppender(Level.INFO, "fr.urssaf.image.sae");
    logger.addAppender(logAppender);
    ModeAPIRndUtils.setAllRndModeAPIThrift();

    // EasyMock.reset(rndRecuperationService);

  }

  @After
  public void after() throws Exception {
    server.resetData();
    logger.detachAppender(logAppender);
    EasyMock.reset(rndRecuperationService);
  }

  @Test
  public void c_testLancerSaeBddRuntimeException() throws Exception {
    try {
      // On lance la mise à jour alors que le paramètre de version n'existe
      // pas.
      majRndService.lancer();
      Assert
      .fail("le service doit une lever une exception de type MajRndException");

    } catch (final MajRndException e) {

      Assert.assertEquals("exception non attendue",
                          SaeBddRuntimeException.class, e.getCause().getClass());
    }
  }

  @Test
  public void a_testLancerRndRecuperationException() throws Exception {

    final VersionRnd version = new VersionRnd();
    version.setDateMiseAJour(new Date());
    version.setVersionEnCours("11.4");
    saeBddSupport.updateVersionRnd(version);

    // Exception lors de la récupération de la version
    EasyMock.expect(rndRecuperationService.getVersionCourante())
    .andThrow(
              new RndRecuperationException(
                  "Exception récupération version ADRN")).anyTimes();

    EasyMock.replay(rndRecuperationService);
    try {
      majRndService.lancer();
      Assert
      .fail("le service doit une lever une exception de type MajRndException");

    } catch (final MajRndException e) {

      Assert.assertEquals("exception non attendue",
                          RndRecuperationException.class, e.getCause().getClass());
    }
  }

  @Test
  public void b_testLancerRndRecuperationException2() throws Exception {
    // server.resetData();
    final VersionRnd version = new VersionRnd();
    version.setDateMiseAJour(new Date());
    version.setVersionEnCours("11.4");
    saeBddSupport.updateVersionRnd(version);

    EasyMock.expect(rndRecuperationService.getVersionCourante())
    .andReturn("11.5")
    .anyTimes();

    // Exception lors de la récupération du RND dans l'ADRN
    EasyMock.expect(
                    rndRecuperationService
                    .getListeRnd(EasyMock.anyObject(String.class))).andThrow(
                                                                             new RndRecuperationException("Exception récupération RND"))
    .anyTimes();

    EasyMock.replay(rndRecuperationService);
    try {
      majRndService.lancer();
      Assert
      .fail("le service doit une lever une exception de type MajRndException");

    } catch (final MajRndException e) {

      Assert.assertEquals("exception non attendue",
                          RndRecuperationException.class, e.getCause().getClass());
    }
  }
}
