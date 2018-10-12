/**
 *
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.tools.GestionModeApiTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
public class RegTechniqueCqlServiceDatasTest {

  private static final Date DATE = new Date();

  private static final Date DATE_INF = DateUtils.addHours(DATE, -1);

  private static final Date DATE_SUP = DateUtils.addHours(DATE, 1);

  private static final Date DATE_JOUR_SUIVANT = DateUtils.addDays(DATE, 1);

  private static final Date DATE_JOUR_PRECEDENT = DateUtils.addDays(DATE, -1);

  private static final String VALUE = "valeur";

  private static final String KEY = "clé";

  private static final String STACK = "stacktrace";

  private static final String LOGIN = "LE LOGIN";

  private static final String CONTRAT = "contrat de service";

  private static final String CODE_EVT = "code événement";

  private static final String CONTEXTE = "contexte";

  private static final String cfName = "traceregtechnique";

  private static final Map<String, String> INFOS;
  static {
    INFOS = new HashMap<String, String>();
    INFOS.put(KEY, VALUE);
  }

  @Autowired
  private RegTechniqueService service;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private TraceRegTechniqueCqlSupport support;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;

  @After
  public void after() throws Exception {
    server.resetData();
  }

  @Test
  public void testAucunRetourBorneInferieure() {
    GestionModeApiTest.setModeApiCql(cfName);
    createTraces();

    // on fixe les bornes inférieure à la première trace de la journée
    final Date dateStart = DateUtils.addDays(DATE, -3);
    final Date dateFin = DateUtils.addDays(DATE, -2);

    final List<TraceRegTechniqueIndex> result = service.lecture(dateStart,
                                                                dateFin,
                                                                10,
                                                                true);
    Assert.assertNull("il ne doit y avoir aucun résultat", result);
  }

  @Test
  public void testRetourUnSeulElementLimite() {
    GestionModeApiTest.setModeApiCql(cfName);
    createTraces();

    final Date dateStart = DateUtils.addDays(DATE, -2);
    final Date dateFin = DateUtils.addDays(DATE, 2);

    final List<TraceRegTechniqueIndex> result = service.lecture(dateStart,
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
    GestionModeApiTest.setModeApiCql(cfName);
    createTraces();

    final Date dateStart = DATE;
    final Date dateEnd = DATE_SUP;

    final List<TraceRegTechniqueIndex> result = service.lecture(dateStart,
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
    GestionModeApiTest.setModeApiCql(cfName);
    createTraces();

    final Date dateStart = DateUtils.addSeconds(DATE_JOUR_PRECEDENT, -1);
    final Date dateEnd = DateUtils.addSeconds(DATE_JOUR_SUIVANT, 1);

    final List<TraceRegTechniqueIndex> result = service.lecture(dateStart,
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
    GestionModeApiTest.setModeApiCql(cfName);
    createTraces();

    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DATE);
    final String suffixe = " [DATE]";
    final TraceRegTechnique result = service.lecture(uuid);

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
    Assert.assertEquals("la stack trace doit etre valide",
                        STACK + suffixe,
                        result.getStacktrace());
    Assert
          .assertEquals(
                        "les infos supplémentaire doivent contenir une valeur correcte élément",
                        VALUE,
                        result.getInfos().get(KEY));

  }

  @Test
  public void testSuppression() {
    GestionModeApiTest.setModeApiCql(cfName);
    createTraces();

    service.purge(DATE_JOUR_PRECEDENT);
    service.purge(DATE);

    List<TraceRegTechniqueIndex> result = service.lecture(
                                                          DATE_JOUR_PRECEDENT, DATE, 100, false);
    Assert.assertNull(
                      "il ne doit plus rester de traces pour les deux jours donnés",
                      result);

    result = service.lecture(DATE,
                             DateUtils.addSeconds(DATE_JOUR_SUIVANT, 1),
                             100,
                             false);
    Assert.assertEquals("il ne doit y avoir qu'une trace", 1, result.size());
    Assert.assertTrue("il doit s'agir de la trace du jour +1", result.get(0)
                                                                     .getCodeEvt()
                                                                     .contains("DATE_JOUR_SUIVANT"));

  }

  private void createTraces() {
    createTrace(DATE, " [DATE]");
    createTrace(DATE_INF, " [DATE_INF]");
    createTrace(DATE_SUP, " [DATE_SUP]");
    createTrace(DATE_JOUR_SUIVANT, " [DATE_JOUR_SUIVANT]");
    createTrace(DATE_JOUR_PRECEDENT, " [DATE_JOUR_PRECEDENT]");
  }

  private void createTrace(final Date date, final String suffixe) {
    final TraceRegTechniqueCql trace = new TraceRegTechniqueCql(timeUUIDSupport
                                                                               .buildUUIDFromDate(date),
                                                                date);
    trace.setContexte(CONTEXTE + suffixe);
    trace.setCodeEvt(CODE_EVT + suffixe);
    trace.setContratService(CONTRAT + suffixe);
    trace.setLogin(LOGIN + suffixe);
    trace.setStacktrace(STACK + suffixe);
    trace.setInfos(INFOS);

    support.create(trace, new Date().getTime());
  }
}
