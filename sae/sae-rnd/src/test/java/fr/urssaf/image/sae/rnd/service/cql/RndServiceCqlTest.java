package fr.urssaf.image.sae.rnd.service.cql;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.rnd.dao.support.cql.RndCqlSupport;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.service.RndService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RndServiceCqlTest {

  @Autowired
  private RndService rndService;

  @Autowired
  private RndCqlSupport rndCqlSupport;

  @Autowired
  private CassandraServerBean server;

  private final String cfName = Constantes.CF_RND;



  @After
  public void after() throws Exception {
    server.resetDataOnly();

  }

  @Before
  public void before() {
    GestionModeApiUtils.setModeApiCql(cfName);
    final TypeDocument typeDocCree = new TypeDocument();
    typeDocCree.setCloture(false);
    typeDocCree.setCode("1.2.1.1.1");
    typeDocCree.setCodeActivite("2");
    typeDocCree.setCodeFonction("1");
    typeDocCree.setDureeConservation(300);
    typeDocCree.setLibelle("Libellé 1.2.1.1.1");
    typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

    rndCqlSupport.ajouterRnd(typeDocCree);
  }

  @Test
  public void testGetCodeActivite() throws CodeRndInexistantException {

    final String codeRnd = "1.2.1.1.1";

    final String codeActivite = rndService.getCodeActivite(codeRnd);
    Assert.assertEquals("Le code activité est incorrect", "2", codeActivite);

    final String codeFonction = rndService.getCodeFonction(codeRnd);
    Assert.assertEquals("Le code fonction est incorrect", "1", codeFonction);

    final int duree = rndService.getDureeConservation(codeRnd);
    Assert.assertEquals("La durée de conservation est incorrecte", 300, duree);

    final TypeDocument type = rndService.getTypeDocument(codeRnd);
    final TypeDocument typeDocARecup = new TypeDocument();
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
