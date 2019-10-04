/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class RegSecuriteServiceDatasTest {

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
  private static final Map<String, Object> INFOS;
  static {
    INFOS = new HashMap<>();
    INFOS.put(KEY, VALUE);
  }

  @Autowired
  private RegSecuriteService service;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private TraceRegSecuriteSupport support;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;

  @After
  public void begin() throws Exception {
    GestionModeApiUtils.setModeApiThrift(Constantes.CF_PARAMETERS);
  }

  @After
  public void after() throws Exception {
    server.resetDataOnly();
    //server.resetDataOnly();
  }

  @Test
  public void testAucunRetourBorneInferieure() {
    createTraces();

    // on fixe les bornes inférieure à la première trace de la journée
    final Date dateStart = DateUtils.addHours(DATE, -3);
    final Date dateFin = DateUtils.addHours(DATE, -2);

    final List<TraceRegSecuriteIndex> result = service.lecture(dateStart, dateFin,
                                                               10, true);
    Assert.assertNull("il ne doit y avoir aucun résultat", result);
  }

  @Test
  public void testRetourUnSeulElementLimite() {
    createTraces();

    final Date dateStart = DateUtils.addHours(DATE, -2);
    final Date dateFin = DateUtils.addHours(DATE, 2);

    final List<TraceRegSecuriteIndex> result = service.lecture(dateStart, dateFin,
                                                               1, true);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        1, result.size());
    Assert.assertTrue("l'enregistrement doit correspondre au plus récent",
                      result.get(0).getContexte().contains("DATE_SUP"));
  }

  @Test
  public void testRetour3ElementsMemeJour() {
    createTraces();

    final Date dateStart = DATE_INF;
    final Date dateEnd = DATE_SUP;

    final List<TraceRegSecuriteIndex> result = service.lecture(dateStart, dateEnd,
                                                               10, true);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        3, result.size());
    Assert.assertTrue("le premier enregistrement doit etre le plus récent",
                      result.get(0).getContexte().contains("DATE_SUP"));
    Assert.assertTrue("le deuxième enregistrement doit etre lintermédiaire",
                      result.get(1).getContexte().contains("[DATE]"));
    Assert.assertTrue("le troisième enregistrement doit etre le plus ancien",
                      result.get(2).getContexte().contains("DATE_INF"));

  }

  @Test
  public void testRetourTousElements() {
    createTraces();

    final Date dateStart = DateUtils.addSeconds(DATE_JOUR_PRECEDENT, -1);
    final Date dateEnd = DateUtils.addSeconds(DATE_JOUR_SUIVANT, 1);

    final List<TraceRegSecuriteIndex> result = service.lecture(dateStart, dateEnd,
                                                               10, true);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        5, result.size());
    Assert.assertTrue("le premier enregistrement doit etre le plus récent",
                      result.get(0).getContexte().contains("DATE_JOUR_SUIVANT"));
    Assert.assertTrue(
                      "le deuxième enregistrement doit etre le max du jour précédent",
                      result.get(1).getContexte().contains("[DATE_SUP]"));
    Assert
    .assertTrue(
                "le troisième enregistrement doit etre l'intermediaire du jour précédent",
                result.get(2).getContexte().contains("[DATE]"));
    Assert.assertTrue(
                      "le quatrieme enregistrement doit etre le min du jour précédent",
                      result.get(3).getContexte().contains("[DATE_INF]"));
    Assert.assertTrue(
                      "le cinquième enregistrement doit etre le max du jour -2", result
                      .get(4).getContexte().contains("[DATE_JOUR_PRECEDENT]"));
  }

  @Test
  public void testGetBean() {
    createTraces();
    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DATE);
    final String suffixe = " [DATE]";
    final TraceRegSecurite result = service.lecture(uuid);
    Assert.assertNotNull("il doit y avoir un résultat");
    Assert.assertNotNull("l'objet doit etre trouvé", result);
    Assert.assertEquals("l'action doit etre correcte", CONTEXTE + suffixe,
                        result.getContexte());
    Assert.assertEquals("le code evenement doit etre correcte", CODE_EVT
                        + suffixe, result.getCodeEvt());
    Assert.assertEquals("le contrat doit etre correcte", CONTRAT + suffixe,
                        result.getContratService());
    Assert.assertEquals("l'identifiant doit etre correcte", uuid, result
                        .getIdentifiant());
    Assert.assertEquals("le login doit etre correcte", LOGIN + suffixe,
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
                  VALUE, result.getInfos().get(KEY));

  }

  @Test
  @Ignore("TODO : erreur à 23h43 : il ne doit y avoir qu'une trace expected:<1> but was:<2>")
  public void testSuppression() {
    createTraces();

    service.purge(DATE_JOUR_PRECEDENT, 500);
    service.purge(DATE, 500);

    List<TraceRegSecuriteIndex> result = service.lecture(DATE_JOUR_PRECEDENT,
                                                         DATE, 100, false);
    Assert.assertNull(
                      "il ne doit plus rester de traces pour les deux jours donnés",
                      result);

    result = service.lecture(DATE,
                             DateUtils.addSeconds(DATE_JOUR_SUIVANT, 1), 100, false);
    Assert.assertEquals("il ne doit y avoir qu'une trace", 1, result.size());
    Assert.assertTrue("il doit s'agir de la trace du jour +1", result.get(0)
                      .getCodeEvt().contains("DATE_JOUR_SUIVANT"));

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

  private void createTraces() {
    createTrace(DATE, " [DATE]");
    createTrace(DATE_INF, " [DATE_INF]");
    createTrace(DATE_SUP, " [DATE_SUP]");
    createTrace(DATE_JOUR_SUIVANT, " [DATE_JOUR_SUIVANT]");
    createTrace(DATE_JOUR_PRECEDENT, " [DATE_JOUR_PRECEDENT]");
  }

  private void createTrace(final Date date, final String suffixe) {
    final TraceRegSecurite trace = new TraceRegSecurite(timeUUIDSupport
                                                        .buildUUIDFromDate(date), date);
    trace.setContexte(CONTEXTE + suffixe);
    trace.setCodeEvt(CODE_EVT + suffixe);
    trace.setContratService(CONTRAT + suffixe);
    trace.setLogin(LOGIN + suffixe);
    trace.setInfos(INFOS);

    support.create(trace, new Date().getTime());
  }
}
