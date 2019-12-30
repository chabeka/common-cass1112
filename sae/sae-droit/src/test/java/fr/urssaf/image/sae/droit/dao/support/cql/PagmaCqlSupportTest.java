/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.droit.dao.model.Pagma;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PagmaCqlSupportTest {

  private static final String CODE = "code";

  private static final String ACTION = "action";

  private static final String CODE1 = "code1";

  private static final String[] ACTIONS1 = new String[] { "action1", "action2" };

  private static final String[] ACTIONS1bis = new String[] {"action1bis", "action2bis"};

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private PagmaCqlSupport support;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreateFind() {

    final List<String> listeAu = Arrays.asList(ACTIONS1);

    final Pagma pagma = new Pagma();
    pagma.setActionUnitaires(listeAu);
    pagma.setCode(CODE1);

    support.create(pagma);

    final Pagma res = support.find(CODE1);

    Assert.assertNotNull("le pagma ne doit pas être null", res);
    Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
                        .getCode());

    Assert.assertTrue(
                      "la liste d'origine doit contenir toute la liste récupérée",
                      listeAu.containsAll(res.getActionUnitaires()));

    Assert.assertTrue(
                      "la liste récupérée doit contenir toute la liste d'origine", res
                      .getActionUnitaires().containsAll(listeAu));
  }

  @Test
  public void testCreateDelete() {

    final List<String> listeAu = Arrays.asList(ACTIONS1);

    final Pagma pagma = new Pagma();
    pagma.setActionUnitaires(listeAu);
    pagma.setCode(CODE1);

    support.create(pagma);

    support.delete(CODE1);

    final Pagma res = support.find(CODE1);

    Assert.assertNull(
                      "aucune référence de l'action unitaire ne doit être trouvée", res);
  }

  @Test
  public void testCreateFindAll() {

    for (int i = 1; i < 4; i++) {
      final List<String> listAu = new ArrayList<>();
      final int max = i * 2;
      listAu.add(ACTION + (max - 1));
      listAu.add(ACTION + max);

      final Pagma pagma = new Pagma();
      pagma.setCode(CODE + i);
      pagma.setActionUnitaires(listAu);

      support.create(pagma);
    }

    for (int i = 1; i < 4; i++) {
      final Pagma pagma = support.find(CODE + i);

      Assert.assertNotNull("le PAGMa avec le code " + CODE + i
                           + " doit être trouvé", pagma);

      final List<String> listAu = new ArrayList<>();
      final int max = i * 2;
      listAu.add(ACTION + (max - 1));
      listAu.add(ACTION + max);

      Assert.assertTrue(
                        "la liste d'origine doit contenir toute la liste récupérée",
                        listAu.containsAll(pagma.getActionUnitaires()));

      Assert.assertTrue(
                        "la liste récupérée doit contenir toute la liste d'origine",
                        pagma.getActionUnitaires().containsAll(listAu));
    }

  }

  @Test
  public void testUpdate() {
    final List<String> listeAu = Arrays.asList(ACTIONS1);
    final List<String> listeAuBis = Arrays.asList(ACTIONS1bis);
    final Pagma pagm1 = new Pagma();
    pagm1.setCode(CODE);
    pagm1.setActionUnitaires(listeAu);
    support.create(pagm1);

    final Pagma pagm1bis = new Pagma();
    pagm1bis.setCode(CODE);
    pagm1bis.setActionUnitaires(listeAuBis);
    support.create(pagm1bis);

    final Pagma pagmResult = support.find(CODE);
    Assert.assertEquals(pagmResult, pagm1bis);
  }

  @Test
  public void testNullCodeValue() {
    try {
      final List<String> listeAu = Arrays.asList(ACTIONS1);
      final Pagma pagm1 = new Pagma();
      pagm1.setCode(null);
      pagm1.setActionUnitaires(listeAu);
      support.create(pagm1);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "le code ne peut etre null");
    }

  }

  @Test
  public void testNullEntity() {
    try {
      support.create(null);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "l'objet pagma ne peut etre null");
    }
  }
}
