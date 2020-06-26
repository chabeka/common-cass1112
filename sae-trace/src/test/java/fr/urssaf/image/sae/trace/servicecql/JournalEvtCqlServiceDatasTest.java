/**
 *
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

/**
 * JournalEvtCqlServiceDatasTest hérite du test AbstractServiceCqlTest
 * les annotations pour le test ne sont pas nécessaires (AC75095351)
 * *
 */
public class JournalEvtCqlServiceDatasTest extends AbstractServiceCqlTest {

  private static final Date DATE = new Date();

  private static final Date DATE_INF = DateUtils.addHours(DATE, -1);

  private static final Date DATE_SUP = DateUtils.addHours(DATE, 1);

  private static final Date DATE_JOUR_SUIVANT = DateUtils.addDays(DATE, 1);

  private static final Date DATE_JOUR_PRECEDENT = DateUtils.addDays(DATE, -1);

  private static final String VALUE = "valeur";

  private static final String KEY = "clé";

  private static final String LOGIN = "LE LOGIN";

  private static final String CONTRAT = "contrat de service";

  private static final String CODE_EVT = "code événement";

  private static final String CONTEXTE = "contexte";

  private static final Map<String, String> INFOS;
  static {
    INFOS = new HashMap<>();
    INFOS.put(KEY, VALUE);
  }

  @Autowired
  private JournalEvtService service;


  @Autowired
  private TraceJournalEvtCqlSupport support;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;


  @Test
  public void testAucunRetourBorneInferieure() {

    createTraces();

    // on fixe les bornes inférieure à la première trace de la journée
    final Date dateStart = DateUtils.addDays(DATE, -3);
    final Date dateFin = DateUtils.addDays(DATE, -2);

    final List<TraceJournalEvtIndex> result = service.lecture(dateStart,
                                                              dateFin,
                                                              10,
                                                              true);
    Assert.assertNull("il ne doit y avoir aucun résultat", result);
  }

  @Test
  public void testRetourUnSeulElementLimite() {

    createTraces();

    final Date dateStart = DateUtils.addDays(DATE, -2);
    final Date dateFin = DateUtils.addDays(DATE, 2);

    final List<TraceJournalEvtIndex> result = service.lecture(dateStart,
                                                              dateFin,
                                                              1,
                                                              true);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        1,
                        result.size());
    Assert.assertTrue("l'enregistrement doit correspondre au plus récent",
                      result.get(0).getContexte().contains("DATE_JOUR_SUIVANT"));
  }

  @Test
  public void testRetour3ElementsMemeJour() {

    createTraces();

    final Date dateStart = DATE;
    final Date dateEnd = DATE_SUP;

    final List<TraceJournalEvtIndex> result = service.lecture(dateStart,
                                                              dateEnd,
                                                              10,
                                                              true);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        3,
                        result.size());

    Assert.assertTrue("le premier enregistrement doit etre le plus ancien",
                      result.get(0).getContexte().contains("DATE_INF"));
    Assert.assertTrue("le deuxième enregistrement doit etre le plus récent",
                      result.get(1).getContexte().contains("DATE"));
    Assert.assertTrue("le troisième enregistrement doit etre lintermédiaire",
                      result.get(2).getContexte().contains("[DATE_SUP]"));

  }

  @Test
  public void testRetourTousElements() {

    createTraces();

    final Date dateStart = DateUtils.addSeconds(DATE_JOUR_PRECEDENT, -1);
    final Date dateEnd = DateUtils.addSeconds(DATE_JOUR_SUIVANT, 1);

    final List<TraceJournalEvtIndex> result = service.lecture(dateStart,
                                                              dateEnd,
                                                              10,
                                                              true);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        5,
                        result.size());
    Assert.assertTrue("le premier enregistrement doit etre le plus récent",
                      result.get(0).getContexte().contains("DATE_JOUR_SUIVANT"));
    Assert.assertTrue("le deuxieme enregistrement doit etre le plus ancien",
                      result.get(1).getContexte().contains("DATE_INF"));
    Assert.assertTrue("le troisième enregistrement doit etre le plus récent",
                      result.get(2).getContexte().contains("DATE"));
    Assert.assertTrue("le quatrième enregistrement doit etre lintermédiaire",
                      result.get(3).getContexte().contains("[DATE_SUP]"));
    Assert.assertTrue(
                      "le cinquième enregistrement doit etre le max du jour -2", result
                      .get(4).getContexte().contains("[DATE_JOUR_PRECEDENT]"));
  }

  @Test
  public void testGetBean() {

    createTraces();
    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DATE);
    final String suffixe = " [DATE]";
    final TraceJournalEvt result = service.lecture(uuid);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertNotNull("l'objet doit etre trouvé", result);
    Assert.assertEquals("l'action doit etre correcte",
                        CONTEXTE + suffixe,
                        result.getContexte());
    Assert.assertEquals("le code evenement doit etre correcte", CODE_EVT
                        + suffixe, result.getCodeEvt());
    Assert.assertEquals("le contrat doit etre correcte",
                        CONTRAT + suffixe,
                        result.getContratService());
    Assert.assertEquals("l'identifiant doit etre correcte", uuid, result
                        .getIdentifiant());
    Assert.assertEquals("le login doit etre correcte",
                        LOGIN + suffixe,
                        result.getLogin());
    Assert.assertEquals("la date doit etre correcte", DATE, result
                        .getTimestamp());
    Assert.assertEquals(
                        "les infos supplémentaire doivent contenir un élément", 1, result
                        .getInfos().size());
    Assert.assertTrue("les infos supplémentaire doivent une clé correcte",
                      result.getInfos().keySet().contains(KEY));
    Assert
    .assertEquals(
                  "les infos supplémentaire doivent contenir une valeur correcte élément",
                  VALUE,
                  result.getInfos().get(KEY));

  }

  @Test
  public void testSuppression() {

    createTraces();

    service.purge(DATE_JOUR_PRECEDENT, 1);
    service.purge(DATE, 1);

    List<TraceJournalEvtIndex> result = service.lecture(DATE_JOUR_PRECEDENT,
                                                        DATE,
                                                        100,
                                                        false);
    Assert.assertNull(
                      "il ne doit plus rester de traces pour les deux jours donnés",
                      result);

    result = service.lecture(DATE,
                             DateUtils.addDays(DATE_JOUR_SUIVANT, 1),
                             100,
                             false);
    Assert.assertEquals("il ne doit y avoir qu'une trace", 1, result.size());
    Assert.assertTrue("il doit s'agir de la trace du jour +1", result.get(0)
                      .getCodeEvt()
                      .contains("DATE_JOUR_SUIVANT"));
    // TODO test si l'index a été suprimé

  }

  @Test
  public void testHasRecordsTheDayBefore() {

    createTrace(DATE, " [DATE]");

    final boolean hasRecords = service.hasRecords(DATE_JOUR_PRECEDENT);

    Assert.assertFalse("il ne doit pas y avoir de trace", hasRecords);

  }

  @Test
  public void testHasRecords() {

    createTrace(DATE, " [DATE]");

    final boolean hasRecords = service.hasRecords(DATE);

    Assert.assertTrue("il doit y avoir une trace", hasRecords);

  }

  @Test
  public void testHasRecordsAucun() {

    final boolean hasRecords = service.hasRecords(DATE);

    Assert.assertFalse("il ne pas doit y avoir une trace", hasRecords);

  }

  @Test
  public void testExport() throws IOException {



    final Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.YEAR, 2013);
    calendar.set(Calendar.MONTH, 3);
    calendar.set(Calendar.DATE, 15);
    calendar.set(Calendar.AM_PM, Calendar.AM);
    calendar.set(Calendar.HOUR, 11);
    calendar.set(Calendar.MINUTE, 45);
    calendar.set(Calendar.SECOND, 50);

    createTrace(calendar.getTime(), " [DATE]", UUID
                .fromString("1b163270-80bd-11e2-8148-005056c00008"));

    final String dirPath = System.getProperty("java.io.tmpdir");
    final File tempDirectory = new File(dirPath);
    final File repertoire = new File(tempDirectory, "export");
    repertoire.mkdir();

    final String path = service
        .export(calendar.getTime(),
                repertoire.getAbsolutePath(),
                "6f5e4930-80cc-11e2-8759-005056c00008",
            "00000");

    final String sha1Attendu = "61d37fcf05a3ea89ad02653a412e5147d8b8b9d6";
    final String sha1Obtenu = calculeSha1(new File(path));

    FileUtils.deleteDirectory(repertoire);

    Assert.assertEquals(
                        "le sha1 doit etre correct : le fichier doit etre correct",
                        sha1Attendu,
                        sha1Obtenu);

  }

  private String calculeSha1(final File file) throws IOException {

    final FileInputStream fis = new FileInputStream(file);
    try {

      return DigestUtils.shaHex(fis);

    }
    finally {
      if (fis != null) {
        fis.close();
      }
    }

  }

  private void createTraces() {
    createTrace(DATE, " [DATE]");
    createTrace(DATE_INF, " [DATE_INF]");
    createTrace(DATE_SUP, " [DATE_SUP]");
    createTrace(DATE_JOUR_SUIVANT, " [DATE_JOUR_SUIVANT]");
    createTrace(DATE_JOUR_PRECEDENT, " [DATE_JOUR_PRECEDENT]");
  }

  private void createTrace(final Date date, final String suffixe) {
    final TraceJournalEvtCql trace = new TraceJournalEvtCql(timeUUIDSupport
                                                            .buildUUIDFromDate(date),
                                                            date);
    trace.setContexte(CONTEXTE + suffixe);
    trace.setCodeEvt(CODE_EVT + suffixe);
    trace.setContratService(CONTRAT + suffixe);
    trace.setLogin(LOGIN + suffixe);
    trace.setInfos(INFOS);
    trace.setPagms(Arrays.asList("PAGM " + suffixe));

    support.create(trace, new Date().getTime());
  }

  private void createTrace(final Date date, final String suffixe, final UUID id) {
    final TraceJournalEvtCql trace = new TraceJournalEvtCql(id, date);
    trace.setContexte(CONTEXTE + suffixe);
    trace.setCodeEvt(CODE_EVT + suffixe);
    trace.setContratService(CONTRAT + suffixe);
    trace.setLogin(LOGIN + suffixe);
    trace.setInfos(INFOS);
    trace.setPagms(Arrays.asList("PAGM " + suffixe));

    support.create(trace, new Date().getTime());
  }

}
