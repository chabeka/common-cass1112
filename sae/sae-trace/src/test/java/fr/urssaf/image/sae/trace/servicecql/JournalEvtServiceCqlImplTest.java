/**
 *
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.tools.GestionModeApiTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
public class JournalEvtServiceCqlImplTest {

  private static final String REPERTOIRE_EXISTS = "le répertoire doit exister";

  private static final String REPERTOIRE_FICHIER = "le paramètre n'est pas un répertoire";

  private static final String DATE_DEB_INF_DATE_FIN = "la date de début doit être inférieure à la date de fin";

  private static final String ARG_0 = "{0}";

  private static final String MESSAGE_OK = "le message d'erreur doit etre correct";

  private static final String ILLEGAL_EXPECTED = "Une exception IllegalArgumentException est attendue";

  @Autowired
  private JournalEvtService service;

  private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

  public String cfName = "tracejournalevt";


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
      .lecture(DateUtils.addYears(new Date(), -2), new Date(), 0, true);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                              "limite"),
                          exception
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

  @Test
  public void testExportDateObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.export(null, null, null, null);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                              "date d'export"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }
  }

  @Test
  public void testExportRepertoireObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.export(new Date(), null, null, null);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                              "répertoire"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }
  }

  @Test
  public void testExportIdObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.export(new Date(), "fichier", null, null);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                              "identifiant du journal précédent"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }
  }

  @Test
  public void testExportHashObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.export(new Date(), "fichier", "c", null);
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK,
                          StringUtils.replace(MESSAGE_ERREUR,
                                              ARG_0,
                              "hash du journal précédent"),
                          exception.getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }
  }

  @Test
  public void testExportRepertoireExisteObligatoire() {
    GestionModeApiTest.setModeApiCql(cfName);
    try {
      service.export(new Date(), "fichierInexistant", "c", "d");
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK, REPERTOIRE_EXISTS, exception
                          .getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);
    }
  }

  @Test
  public void testExportRepertoireIsRepertoireObligatoire() throws IOException {
    GestionModeApiTest.setModeApiCql(cfName);
    final File file = File.createTempFile("repertoire", ".tmp");

    try {
      FileUtils.writeStringToFile(file,
          "ceci est un fichier et non un répertoire");
      service.export(new Date(), file.getAbsolutePath(), "c", "d");
      Assert.fail(ILLEGAL_EXPECTED);

    }
    catch (final IllegalArgumentException exception) {
      Assert.assertEquals(MESSAGE_OK, REPERTOIRE_FICHIER, exception
                          .getMessage());

    }
    catch (final Exception exception) {
      Assert.fail(ILLEGAL_EXPECTED);

    }
    finally {
      FileUtils.deleteQuietly(file);
    }
  }

}
