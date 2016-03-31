package fr.urssaf.image.sae.rnd.service;

import org.junit.Before;
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
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class RndServiceTest {
   
   @Autowired
   private RndService rndService;
   
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
   
   @Before
   public void before() {
      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("1.2.1.1.1");
      typeDocCree.setCodeActivite("2");
      typeDocCree.setCodeFonction("1");
      typeDocCree.setDureeConservation(300);
      typeDocCree.setLibelle("Libellé 1.2.1.1.1");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @Test
   public void testGetCodeActivite() throws CodeRndInexistantException {
      
      String codeRnd = "1.2.1.1.1";

      String codeActivite = rndService.getCodeActivite(codeRnd);
      Assert.assertEquals("Le code activité est incorrect", "2", codeActivite);

      String codeFonction = rndService.getCodeFonction(codeRnd);
      Assert.assertEquals("Le code fonction est incorrect", "1", codeFonction);
      
      int duree = rndService.getDureeConservation(codeRnd);
      Assert.assertEquals("La durée de conservation est incorrecte", 300, duree);
      
      TypeDocument type = rndService.getTypeDocument(codeRnd);
      TypeDocument typeDocARecup = new TypeDocument();
      typeDocARecup.setCloture(false);
      typeDocARecup.setCode("1.2.1.1.1");
      typeDocARecup.setCodeActivite("2");
      typeDocARecup.setCodeFonction("1");
      typeDocARecup.setDureeConservation(300);
      typeDocARecup.setLibelle("Libellé 1.2.1.1.1");
      typeDocARecup.setType(TypeCode.ARCHIVABLE_AED);
      Assert.assertEquals("Le type de doc est incorrect", typeDocARecup, type);
      
   }
   

   

}
