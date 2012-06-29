/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaePagmaServiceDatasTest {

   @Autowired
   private SaePagmaService service;

   @Autowired
   private PagmaSupport pagmaSupport;

   @Autowired
   private ActionUnitaireSupport actionSupport;

   @Autowired
   private JobClockSupport clockSupport;

   @Test(expected = PagmaReferenceException.class)
   public void testPagmaDejaExistant() {

      Pagma pagma = new Pagma();
      pagma.setCode("codePagma");
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

      pagmaSupport.create(pagma, clockSupport.currentCLock());

      service.createPagma(pagma);

   }

   @Test(expected = ActionUnitaireReferenceException.class)
   public void testActionInexistante() {

      Pagma pagma = new Pagma();
      pagma.setCode("codePagma");
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

      service.createPagma(pagma);

   }

   @Test
   public void testCreationSucces() {

      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("action1");
      actionUnitaire.setDescription("description");
      actionSupport.create(actionUnitaire, clockSupport.currentCLock());

      Pagma pagma = new Pagma();
      pagma.setCode("codePagma");
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

      service.createPagma(pagma);

      Pagma storePagma = pagmaSupport.find("codePagma");
      Assert.assertEquals("le pagma doit être créé correctement", pagma,
            storePagma);

   }

}
