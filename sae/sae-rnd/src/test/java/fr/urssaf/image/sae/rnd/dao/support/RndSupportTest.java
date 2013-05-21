package fr.urssaf.image.sae.rnd.dao.support;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class RndSupportTest {

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private JobClockSupport jobClockSupport;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testAjouterRndSuccess() {

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("1.2.1.1.1");
      typeDocCree.setCodeActivite("2");
      typeDocCree.setCodeFonction("1");
      typeDocCree.setDureeConservation(300);
      typeDocCree.setLibelle("Libellé 1.2.1.1.1");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

      TypeDocument typeDoc = null;

      try {
         typeDoc = rndSupport.getRnd("1.2.1.1.1");
         Assert.assertEquals("Le type de doc doit être identique", typeDoc,
               typeDocCree);
      } catch (Exception exception) {
         Assert.fail("aucune erreur attendue");
      }
      
      try {
         typeDoc = rndSupport.getRnd("1.3.1.1.1");
         Assert.assertEquals("Le type de doc doit être null", null,
               typeDoc);
      } catch (Exception exception) {
         Assert.fail("aucune erreur attendue");
      }
   }

}
