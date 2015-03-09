/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ActionUnitaireSupportTest {


   private static final String DESCRIPTION1 = "description1";

   private static final String CODE_TEST1 = "codeTest1";

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private ActionUnitaireSupport support;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testCreateFind() {
      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(CODE_TEST1);
      actionUnitaire.setDescription(DESCRIPTION1);

      support.create(actionUnitaire, new Date().getTime());

      ActionUnitaire recup = support.find(CODE_TEST1);
      Assert.assertEquals("le code doit être exact", CODE_TEST1, recup
            .getCode());
      Assert.assertEquals("la description doit être exacte", DESCRIPTION1,
            recup.getDescription());
   }

   @Test
   public void testCreateDelete() {
      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(CODE_TEST1);
      actionUnitaire.setDescription(DESCRIPTION1);

      support.create(actionUnitaire, new Date().getTime());

      ActionUnitaire recup = support.find(CODE_TEST1);
      Assert.assertEquals("le code doit être exact", CODE_TEST1, recup
            .getCode());
      Assert.assertEquals("la description doit être exacte", DESCRIPTION1,
            recup.getDescription());

      support.delete(CODE_TEST1, new Date().getTime());
      recup = support.find(CODE_TEST1);
      Assert
            .assertNull(
                  "aucune référence de l'action unitaire ne doit être trouvée",
                  recup);
   }

   @Test
   public void testCreateFindAll() {

      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(CODE_TEST1);
      actionUnitaire.setDescription(DESCRIPTION1);

      support.create(actionUnitaire, new Date().getTime());

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("codeTest2");
      actionUnitaire.setDescription("description2");

      support.create(actionUnitaire, new Date().getTime());

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("codeTest3");
      actionUnitaire.setDescription("description3");

      support.create(actionUnitaire, new Date().getTime());

      List<ActionUnitaire> list = support.findAll(10);

      Assert.assertEquals("vérification du nombre d'enregistrements", 4, list
            .size());

      for (int i = 1; i < 4; i++) {
         String cle = "codeTest" + i;
         String description = "description" + i;
         boolean found = false;
         int index = 0;
         while (!found && index < list.size()) {
            if (cle.equals(list.get(index).getCode())) {
               Assert.assertEquals("la description doit etre valide",
                     description, list.get(index).getDescription());
               found = true;
            }
            index++;
         }

         Assert.assertTrue("le code " + cle + " doit etre trouvé", found);
      }
   }

}
