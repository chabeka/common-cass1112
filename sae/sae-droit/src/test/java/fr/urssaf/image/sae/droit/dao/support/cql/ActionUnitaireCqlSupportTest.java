/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

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
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-droit-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ActionUnitaireCqlSupportTest {

  private static final String DESCRIPTION1 = "description1";

  private static final String CODE_TEST1 = "codeTest1";

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private ActionUnitaireCqlSupport cqlsupport;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreateFind() {
    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode(CODE_TEST1);
    actionUnitaire.setDescription(DESCRIPTION1);

    cqlsupport.create(actionUnitaire);

    final ActionUnitaire recup = cqlsupport.find(CODE_TEST1);
    Assert.assertEquals("le code doit être exact", CODE_TEST1, recup.getCode());
    Assert.assertEquals("la description doit être exacte", DESCRIPTION1, recup.getDescription());
  }

  @Test
  public void testCreateDelete() {
    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode(CODE_TEST1);
    actionUnitaire.setDescription(DESCRIPTION1);

    cqlsupport.create(actionUnitaire);

    ActionUnitaire recup = cqlsupport.find(CODE_TEST1);
    Assert.assertEquals("le code doit être exact", CODE_TEST1, recup.getCode());
    Assert.assertEquals("la description doit être exacte", DESCRIPTION1, recup.getDescription());

    cqlsupport.delete(CODE_TEST1);
    recup = cqlsupport.find(CODE_TEST1);
    Assert.assertNull("aucune référence de l'action unitaire ne doit être trouvée", recup);
  }

  @Test
  public void testCreateFindAll() {

    ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode(CODE_TEST1);
    actionUnitaire.setDescription(DESCRIPTION1);

    cqlsupport.create(actionUnitaire);

    actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeTest2");
    actionUnitaire.setDescription("description2");

    cqlsupport.create(actionUnitaire);

    actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeTest3");
    actionUnitaire.setDescription("description3");

    cqlsupport.create(actionUnitaire);

    final List<ActionUnitaire> list = cqlsupport.findAll(10);

    Assert.assertEquals("vérification du nombre d'enregistrements", 3, list.size());

    for (int i = 1; i < 4; i++) {
      final String cle = "codeTest" + i;
      final String description = "description" + i;
      boolean found = false;
      int index = 0;
      while (!found && index < list.size()) {
        if (cle.equals(list.get(index).getCode())) {
          Assert.assertEquals("la description doit etre valide", description, list.get(index).getDescription());
          found = true;
        }
        index++;
      }

      Assert.assertTrue("le code " + cle + " doit etre trouvé", found);
    }
  }

  @Test
  public void testUpdate() {

    final ActionUnitaire actionUnitaire1 = new ActionUnitaire();
    actionUnitaire1.setCode(CODE_TEST1);
    actionUnitaire1.setDescription(DESCRIPTION1);
    cqlsupport.create(actionUnitaire1);

    final ActionUnitaire actionUnitaire1bis = new ActionUnitaire();
    actionUnitaire1bis.setCode(CODE_TEST1);
    actionUnitaire1bis.setDescription("TEST UPDATE");
    cqlsupport.create(actionUnitaire1bis);

    final ActionUnitaire actionUnitaireResult = cqlsupport.find(CODE_TEST1);
    Assert.assertEquals(actionUnitaireResult, actionUnitaire1bis);
  }

  @Test
  public void testNullCodeValue() {
    try {
      final ActionUnitaire actionUnitaire1 = new ActionUnitaire();
      actionUnitaire1.setCode(null);
      actionUnitaire1.setDescription(DESCRIPTION1);
      cqlsupport.create(actionUnitaire1);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "le code ne peut être null");
    }

  }

  @Test
  public void testNullEntity() {
    try {
      cqlsupport.create(null);
      Assert.assertTrue(false);
    }
    catch (final Exception e) {
      Assert.assertEquals(e.getMessage(), "l'objet actionUnitaire ne peut être null");
    }
  }

}