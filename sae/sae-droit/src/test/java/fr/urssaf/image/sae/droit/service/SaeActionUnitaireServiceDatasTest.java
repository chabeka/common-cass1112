/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaeActionUnitaireServiceDatasTest {

   @Autowired
   private SaeActionUnitaireService service;

   @Autowired
   private ActionUnitaireSupport actionSupport;

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private CassandraServerBean cassandraServer;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test(expected = DroitRuntimeException.class)
   public void testActionUnitaireExistante() {

      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("codeAction");
      actionUnitaire.setDescription("description action");

      actionSupport.create(actionUnitaire, clockSupport.currentCLock());

      service.createActionUnitaire(actionUnitaire);

   }

   @Test
   public void testActionUnitaireSucces() {

      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("codeAction");
      actionUnitaire.setDescription("description action");

      service.createActionUnitaire(actionUnitaire);

      ActionUnitaire storedAction = actionSupport.find("codeAction");

      Assert.assertEquals("les deux actions doivent etre identiques",
            actionUnitaire, storedAction);

   }

}
