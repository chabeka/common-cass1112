/**
 *  TODO (AC75095028) Description du fichier
 */

package fr.urssaf.image.sae.trace.dao.supportcql;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
public class TraceRegSecuriteCqlSupportTest {

  private static final String VALUE = "valeur";

  private static final String KEY = "clé";

  private static final Date DATE = new Date();

  private static final String LOGIN = "LE LOGIN";

  private static final String CONTRAT = "contrat de service";

  private static final List<String> PAGMS = Arrays.asList("PAGM1", "PAGM2");

  private static final String CODE_EVT = "code événement";

  private static final String CONTEXT = "contexte";

  private static final Map<String, String> INFOS;

  static {
    INFOS = new HashMap<String, String>();
    INFOS.put(KEY, VALUE);
  }

  @Autowired
  private TraceRegSecuriteCqlSupport supportcql;

  @Autowired
  private CassandraServerBeanCql server;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;

  @After
  public void after() throws Exception {
    server.resetData();
  }

  @Test
  public void testCreateFindSuccess() {

    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(new Date());
    createTrace(uuid);

    final Optional<TraceRegSecuriteCql> securiteOp = supportcql.find(uuid);
    Assert.assertTrue("L'objet est non null", securiteOp.isPresent());

    final List<TraceRegSecuriteIndexCql> list = supportcql.findByDate(DATE, null);
    Assert.assertNotNull("la liste recherchée ne doit pas etre nulle", list);
    Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
                        1,
                        list.size());
    checkBeanIndex(list.get(0), uuid);

  }

  @Test
  public void testDelete() {
    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(new Date());
    createTrace(uuid);

    final long nbTracesPurgees = supportcql.delete(new Date(), new Date().getTime());

    final Optional<TraceRegSecuriteCql> securiteOp = supportcql.find(uuid);
    Assert.assertFalse("aucune trace ne doit etre touvée", securiteOp.isPresent());

    final List<TraceRegSecuriteIndexCql> list = supportcql.findByDate(DATE, null);
    Assert.assertTrue("aucun index ne doit etre present, donc aucune trace",
                      CollectionUtils.isEmpty(list));

    Assert.assertEquals("Le nombre de traces purgées est incorrect",
                        1L,
                        nbTracesPurgees);

  }

  @Test
  public void testCreateFindByPlageSuccess() {

    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DATE);
    createTrace(uuid);

    final Optional<TraceRegSecuriteCql> exploitationOpt = supportcql.find(uuid);
    Assert.assertTrue("L'objet doit etre non null", exploitationOpt.isPresent());
    checkBean(exploitationOpt.get(), uuid);

  }

  private void checkBean(final TraceRegSecuriteCql securite, final UUID uuid) {
    Assert.assertNotNull("l'objet doit etre trouvé", securite);
    Assert.assertEquals("le contexte doit etre correcte", CONTEXT, securite
                                                                           .getContexte());
    Assert.assertEquals("le code evenement doit etre correcte",
                        CODE_EVT,
                        securite.getCodeEvt());
    Assert.assertEquals("le contrat doit etre correcte", CONTRAT, securite
                                                                          .getContratService());
    checkPagms(securite.getPagms());
    Assert.assertEquals("l'identifiant doit etre correcte", uuid, securite
                                                                          .getIdentifiant());
    Assert.assertEquals("le login doit etre correcte", LOGIN, securite
                                                                      .getLogin());
    Assert.assertEquals("la date doit etre correcte", DATE, securite
                                                                    .getTimestamp());
    Assert.assertEquals(
                        "les infos supplémentaire doivent contenir un élément", 1, securite
                                                                                           .getInfos().size());
    Assert.assertTrue("les infos supplémentaire doivent une clé correcte",
                      securite.getInfos().keySet().contains(KEY));
    Assert
          .assertEquals(
                        "les infos supplémentaire doivent contenir une valeur correcte élément",
                        VALUE,
                        securite.getInfos().get(KEY));

  }

  private void checkBeanIndex(final TraceRegSecuriteIndexCql index, final UUID uuid) {
    Assert.assertNotNull("l'objet doit etre trouvé", index);
    Assert.assertEquals("le contexte doit etre correcte", CONTEXT, index
                                                                        .getContexte());
    Assert.assertEquals("le code evenement doit etre correcte",
                        CODE_EVT,
                        index.getCodeEvt());
    Assert.assertEquals("le contrat doit etre correcte", CONTRAT, index
                                                                       .getContrat());
    checkPagms(index.getPagms());
    Assert.assertEquals("l'identifiant doit etre correcte", uuid, index
                                                                       .getIdentifiant());
    Assert.assertEquals("le login doit etre correcte", LOGIN, index
                                                                   .getLogin());
    Assert.assertEquals("la date doit etre correcte", DATE, index
                                                                 .getTimestamp());

  }

  private void checkPagms(final List<String> pagms) {
    Assert.assertNotNull("La liste des PAGM ne doit pas être nulle", pagms);
    Assert.assertEquals("La liste des PAGM doit contenir 2 éléments",
                        2,
                        pagms.size());
    Assert.assertTrue("La liste des PAGM doit contenir le PAGM \"PAGM1\"",
                      pagms.contains("PAGM1"));
    Assert.assertTrue("La liste des PAGM doit contenir le PAGM \"PAGM2\"",
                      pagms.contains("PAGM2"));
  }

  private void createTrace(final UUID uuid) {
    final TraceRegSecuriteCql trace = new TraceRegSecuriteCql(uuid, DATE);
    trace.setContexte(CONTEXT);
    trace.setCodeEvt(CODE_EVT);
    trace.setContratService(CONTRAT);
    trace.setPagms(PAGMS);
    trace.setLogin(LOGIN);
    trace.setInfos(INFOS);

    supportcql.create(trace, new Date().getTime());
  }
}
