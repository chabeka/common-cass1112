/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support.cql;


import java.util.Date;
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
import fr.urssaf.image.sae.droit.dao.model.Pagmp;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PagmpCqlSupportTest {

  private static final String CODE1 = "code1";

  private static final String DESCRIPTION1 = "description1";

  private static final String PRMD1 = "prmd1";

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private PagmpCqlSupport support;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreateFind() {

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode(CODE1);
    pagmp.setDescription(DESCRIPTION1);
    pagmp.setPrmd(PRMD1);

    support.create(pagmp);

    final Pagmp res = support.find(CODE1);

    Assert.assertNotNull("le pagm ne doit pas être null", res);
    Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
                        .getCode());
    Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
                        res.getDescription());
    Assert.assertEquals("le PRMD doit être correct", PRMD1, res.getPrmd());
  }

  @Test
  public void testCreateDelete() {

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode(CODE1);
    pagmp.setDescription(DESCRIPTION1);
    pagmp.setPrmd(PRMD1);

    support.create(pagmp);

    support.delete(CODE1);

    final Pagmp res = support.find(CODE1);

    Assert.assertNull(
                      "aucune référence de l'action unitaire ne doit être trouvée", res);
  }

  @Test
  public void testCreateFindAll() {

    Pagmp pagmp = new Pagmp();
    pagmp.setCode(CODE1);
    pagmp.setDescription(DESCRIPTION1);
    pagmp.setPrmd(PRMD1);

    support.create(pagmp);

    pagmp = new Pagmp();
    pagmp.setCode("code2");
    pagmp.setDescription("description2");
    pagmp.setPrmd("prmd2");

    support.create(pagmp);

    pagmp = new Pagmp();
    pagmp.setCode("code3");
    pagmp.setDescription("description3");
    pagmp.setPrmd("prmd3");

    support.create(pagmp);

    final List<Pagmp> list = support.findAll(10);

    Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
                        .size());

    for (int i = 1; i < 4; i++) {
      final String code = "code" + i;
      final String description = "description" + i;
      final String prmd = "prmd" + i;

      boolean found = false;
      int index = 0;
      while (!found && index < list.size()) {
        if (code.equals(list.get(index).getCode())) {
          Assert.assertEquals("la description doit être correcte",
                              description, list.get(index).getDescription());
          Assert.assertEquals("le PRMD doit être correct", prmd, list.get(
                                                                          index).getPrmd());

          found = true;
        }
        index++;
      }

      Assert.assertTrue("le code " + code + " doit etre trouvé", found);
    }
  }

  @Test
  public void testUpdate() {

    final Pagmp pagmp1 = new Pagmp();
    pagmp1.setCode(CODE1);
    pagmp1.setDescription(DESCRIPTION1);
    pagmp1.setPrmd(PRMD1);

    support.create(pagmp1);

    final Pagmp pagmp1bis = new Pagmp();
    pagmp1bis.setCode(CODE1);
    pagmp1bis.setDescription("description2");
    pagmp1bis.setPrmd(PRMD1);

    support.create(pagmp1bis);

    final Pagmp pagmpResult = support.find(CODE1);
    Assert.assertEquals(pagmpResult, pagmp1bis);
  }

  @Test
  public void testNullCodeValue() {
    try {

      final Pagmp pagmp1 = new Pagmp();
      pagmp1.setCode(null);

      support.create(pagmp1);
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
      Assert.assertEquals(e.getMessage(), "l'objet pagmp ne peut etre null");
    }
  }
}
