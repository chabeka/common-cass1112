package fr.urssaf.image.sae.rnd.dao.support.cql;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RndCqlSupportTest {

  @Autowired
  private RndCqlSupport rndCqlSupport;

  @Autowired
  private CassandraServerBean server;


  @After
  public void after() throws Exception {
    server.resetDataOnly();
  }

  @Test
  public void init() {
    try {
      if (server.isCassandraStarted()) {
        server.resetData();
      }
      Assert.assertTrue(true);
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAjouterRndSuccess() {

    final TypeDocument typeDocCree = new TypeDocument();
    typeDocCree.setCloture(false);
    typeDocCree.setCode("1.2.1.1.1");
    typeDocCree.setCodeActivite("2");
    typeDocCree.setCodeFonction("1");
    typeDocCree.setDureeConservation(300);
    typeDocCree.setLibelle("Libellé 1.2.1.1.1");
    typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

    rndCqlSupport.ajouterRnd(typeDocCree);

    TypeDocument typeDoc = null;

    try {
      typeDoc = rndCqlSupport.getRnd("1.2.1.1.1");
      Assert.assertEquals("Le type de doc doit être identique", typeDoc,
                          typeDocCree);
    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }

    try {
      typeDoc = rndCqlSupport.getRnd("1.3.1.1.1");
      Assert.assertEquals("Le type de doc doit être null", null,
                          typeDoc);
    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }
  }

}
