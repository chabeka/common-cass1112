/**
 *
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.tools.GestionModeApiTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
public class RegSecuriteCqlServiceTest {

  /**
   *
   */
  private static final String DATE_DEB_INF_DATE_FIN = "la date de début doit être inférieure à la date de fin";

  private static final String ARG_0 = "{0}";

  private static final String MESSAGE_OK = "le message d'erreur doit etre correct";

  private static final String ILLEGAL_EXPECTED = "Une exception IllegalArgumentException est attendue";

  @Autowired
  private RegSecuriteService service;

  private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

  private static final String cfName = "traceregsecurite";

  @Test
  public void testLectureIdentifiantObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.lecture(null);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                                              "identifiant"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testLectureDateDebutObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.lecture(null, null, 0, true);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                                              "date de début"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testLectureDateFinObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.lecture(new Date(), null, 0, true);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                                              "date de fin"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testLectureDateDebutInfDateFin() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service
             .lecture(DateUtils.addHours(new Date(), 2), new Date(), 0, true);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK, DATE_DEB_INF_DATE_FIN, exception
                                                                      .getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testLectureDateDebutEqDateFin() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      final Date date = new Date();
      service.lecture(date, date, 0, true);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK, DATE_DEB_INF_DATE_FIN, exception
                                                                      .getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testLectureLimiteObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service
             .lecture(new Date(), DateUtils.addHours(new Date(), 2), 0, true);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                                              "limite"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testPurgeDateDebutObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.purge(null, 1);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                                              "date de purge"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }

  @Test
  public void testHasRecordsDebutObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.hasRecords(null);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                                              "date"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }

  }
}
