/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PagmCqlSupportTest {

  private static final String ID_CLIENT = "idClient";

  private static final String CODE1 = "code1";

  private static final String DESCRIPTION1 = "description1";

  private static final String PAGMA1 = "pagma1";

  private static final String PAGMP1 = "pagmp1";

  private static final Map<String, String> PARAMETRES1 = new HashMap<>();
  static {
    PARAMETRES1.put("cle1", "valeur1");
    PARAMETRES1.put("cle2", "valeur2");
  }

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private PagmCqlSupport support;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreateFind() {

    final PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(ID_CLIENT);
    pagmCql.setCode(CODE1);
    pagmCql.setDescription(DESCRIPTION1);
    pagmCql.setPagma(PAGMA1);
    pagmCql.setPagmp(PAGMP1);
    pagmCql.setParametres(PARAMETRES1);

    support.create(pagmCql);

    final List<Pagm> list = support.findByIdClient(ID_CLIENT);

    Assert.assertEquals("longueur de liste correcte", 1, list.size());

    final Pagm res = list.get(0);

    Assert.assertNotNull("le pagm ne doit pas être null", res);
    Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
                        .getCode());
    Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
                        res.getDescription());
    Assert.assertEquals("le pagma doit être correct", PAGMA1, res.getPagma());
    Assert.assertEquals("le pagmp doit être correct", PAGMP1, res.getPagmp());

    Assert.assertEquals("il doit y avoir deux paramètres", 2, res
                        .getParametres().size());
    Assert.assertTrue("toutes les clés doivent concorder", PARAMETRES1
                      .keySet().containsAll(res.getParametres().keySet()));
    for (final String key : PARAMETRES1.keySet()) {
      Assert.assertEquals("la valeur de la clé " + key
                          + " doit etre correcte", PARAMETRES1.get(key), res
                          .getParametres().get(key));
    }

    Assert.assertNull("le flag compressionPdfActive doit être null", res.getCompressionPdfActive());
    Assert.assertNull("le seuilCompressionPdf doit être null", res.getSeuilCompressionPdf());
  }

  @Test
  public void testCreateDelete() {

    final PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(ID_CLIENT);
    pagmCql.setCode(CODE1);
    pagmCql.setDescription(DESCRIPTION1);
    pagmCql.setPagma(PAGMA1);
    pagmCql.setPagmp(PAGMP1);
    pagmCql.setParametres(PARAMETRES1);

    support.create(pagmCql);

    support.delete(pagmCql);
    final List<Pagm> list = support.findByIdClient(ID_CLIENT);
    Assert.assertTrue("aucune référence pagm ne doit être trouvée",
                      list == null || list.isEmpty());
  }

  @Test
  public void testCreateFindAll() {

    PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(ID_CLIENT);
    pagmCql.setCode(CODE1);
    pagmCql.setDescription(DESCRIPTION1);
    pagmCql.setPagma(PAGMA1);
    pagmCql.setPagmp(PAGMP1);
    pagmCql.setParametres(PARAMETRES1);

    support.create(pagmCql);

    pagmCql = new PagmCql();
    pagmCql.setIdClient(ID_CLIENT);
    pagmCql.setCode("code2");
    pagmCql.setDescription("description2");
    pagmCql.setPagma("pagma2");
    pagmCql.setPagmp("pagmp2");

    Map<String, String> param = new HashMap<>();
    param.put("cle3", "valeur3");
    param.put("cle4", "valeur4");
    pagmCql.setParametres(param);

    support.create(pagmCql);

    pagmCql = new PagmCql();
    pagmCql.setIdClient(ID_CLIENT);
    pagmCql.setCode("code3");
    pagmCql.setDescription("description3");
    pagmCql.setPagma("pagma3");
    pagmCql.setPagmp("pagmp3");

    param = new HashMap<>();
    param.put("cle5", "valeur5");
    param.put("cle6", "valeur6");
    pagmCql.setParametres(param);

    support.create(pagmCql);
    final List<Pagm> list = support.findByIdClient(ID_CLIENT);

    Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
                        .size());

    for (int i = 1; i < 4; i++) {
      final String code = "code" + i;
      final String description = "description" + i;
      final String pagma = "pagma" + i;
      final String pagmp = "pagmp" + i;
      final int indexMap = i * 2;
      param = new HashMap<>();
      param.put("cle" + (indexMap - 1), "valeur" + (indexMap - 1));
      param.put("cle" + indexMap, "valeur" + indexMap);
      pagmCql.setParametres(param);

      boolean found = false;
      int index = 0;
      while (!found && index < list.size()) {
        if (code.equals(list.get(index).getCode())) {
          Assert.assertEquals("la description doit être correcte",
                              description, list.get(index).getDescription());
          Assert.assertEquals("le pagma doit être correct", pagma, list
                              .get(index).getPagma());
          Assert.assertEquals("le pagmp doit être correct", pagmp, list
                              .get(index).getPagmp());

          Assert.assertEquals("il doit y avoir deux paramètres", 2, list
                              .get(index).getParametres().size());
          Assert.assertTrue("toutes les clés doivent concorder", param
                            .keySet().containsAll(
                                                  list.get(index).getParametres().keySet()));
          for (final String key : param.keySet()) {
            Assert.assertEquals("la valeur de la clé " + key
                                + " doit etre correcte", param.get(key), list
                                .get(index).getParametres().get(key));

            found = true;
          }

          Assert.assertNull("le flag compressionPdfActive doit être null", list.get(index).getCompressionPdfActive());
          Assert.assertNull("le seuilCompressionPdf doit être null", list.get(index).getSeuilCompressionPdf());
        }

        index++;
      }

      Assert.assertTrue("le code " + code + " doit etre trouvé", found);
    }

  }

  @Test
  public void testCreateFindWithCompression() {

    final PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(ID_CLIENT);
    pagmCql.setCode("code4");
    pagmCql.setDescription("description4");
    pagmCql.setPagma("pagma4");
    pagmCql.setPagmp("pagmp4");

    final Map<String, String> param = new HashMap<>();
    param.put("cle7", "valeur7");
    param.put("cle8", "valeur8");
    pagmCql.setParametres(param);
    pagmCql.setCompressionPdfActive(Boolean.TRUE);
    pagmCql.setSeuilCompressionPdf(Integer.valueOf(1048576)); // 1Mo

    support.create(pagmCql);

    final List<Pagm> list = support.findByIdClient(ID_CLIENT);

    Assert.assertEquals("longueur de liste correcte", 1, list.size());

    final Pagm res = list.get(0);

    Assert.assertNotNull("le pagm ne doit pas être null", res);
    Assert.assertEquals("l'identifiant (code) doit être correct", "code4", res
                        .getCode());
    Assert.assertEquals("la description doit être correcte", "description4",
                        res.getDescription());
    Assert.assertEquals("le pagma doit être correct", "pagma4", res.getPagma());
    Assert.assertEquals("le pagmp doit être correct", "pagmp4", res.getPagmp());

    Assert.assertEquals("il doit y avoir deux paramètres", 2, res
                        .getParametres().size());
    Assert.assertTrue("toutes les clés doivent concorder", param
                      .keySet().containsAll(res.getParametres().keySet()));
    for (final String key : param.keySet()) {
      Assert.assertEquals("la valeur de la clé " + key
                          + " doit etre correcte", param.get(key), res
                          .getParametres().get(key));
    }

    Assert.assertEquals("le flag compressionPdfActive doit être correct", Boolean.TRUE, res.getCompressionPdfActive());
    Assert.assertEquals("le seuilCompressionPdf doit être correct", Integer.valueOf(1048576), res.getSeuilCompressionPdf());
  }

  @Test
  public void testUpdate() {
    final Map<String, String> param = new HashMap<>();
    param.put("cle7", "valeur7");
    param.put("cle8", "valeur8");
    final PagmCql pagm1 = new PagmCql();
    pagm1.setIdClient(ID_CLIENT);
    pagm1.setCode(CODE1);
    pagm1.setDescription(DESCRIPTION1);
    pagm1.setPagma("pagma4");
    pagm1.setPagmp("pagmp4");
    pagm1.setParametres(param);
    pagm1.setCompressionPdfActive(Boolean.TRUE);
    pagm1.setSeuilCompressionPdf(Integer.valueOf(1048576)); // 1Mo

    support.create(pagm1);

    final PagmCql pagm1bis = new PagmCql();
    pagm1bis.setIdClient(ID_CLIENT);
    pagm1bis.setCode(CODE1);
    pagm1bis.setDescription("description2");
    pagm1bis.setPagma("pagma4");
    pagm1bis.setPagmp("pagmp4");

    pagm1bis.setParametres(param);
    pagm1bis.setCompressionPdfActive(Boolean.TRUE);
    pagm1bis.setSeuilCompressionPdf(Integer.valueOf(1048576)); // 1Mo

    support.create(pagm1bis);

    final PagmCql pagmResult = support.find(ID_CLIENT);
    Assert.assertEquals(pagmResult, pagm1bis);
  }

  @Test
  public void testNullIdClientValue() {
    try {

      final PagmCql pagm1 = new PagmCql();
      pagm1.setIdClient(null);

      support.create(pagm1);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "le code client ne peut etre null");

    }

  }

  @Test
  public void testNullEntity() {
    try {
      support.create(null);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "l'objet pagm ne peut etre null");
    }
  }
}
