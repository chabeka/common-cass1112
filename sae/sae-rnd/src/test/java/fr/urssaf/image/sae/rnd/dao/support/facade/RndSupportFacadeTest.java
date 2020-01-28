package fr.urssaf.image.sae.rnd.dao.support.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.RndCqlSupport;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RndSupportFacadeTest {

  @Autowired
  private RndSupportFacade supportFacade;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private RndSupport support;

  @Autowired
  private RndCqlSupport supportCql;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RndSupportFacadeTest.class);

  @After
  public void after() throws Exception {
    server.resetDataOnly();
  }

  @Test(expected = ModeGestionAPIUnkownException.class)
  public void testModeAPIInconnu() throws CodeRndInexistantException {
    // On se met sur mode API inconnu
    GestionModeApiUtils.setModeApiUnknown(Constantes.CF_RND);
    supportFacade.getRnd("TEST");
  }

  @Test
  public void testCreationDualReadThrift() throws CodeRndInexistantException {
    // On se met sur mode dual read thrift
    GestionModeApiUtils.setModeApiDualReadThrift(Constantes.CF_RND);
    // On ajoute un rnd avec la facade sur les tables thrift et cql
    final String code = "1.2.1.1.1";
    final TypeDocument typeDocCree = new TypeDocument();
    typeDocCree.setCloture(false);
    typeDocCree.setCode("1.2.1.1.1");
    typeDocCree.setCodeActivite("2");
    typeDocCree.setCodeFonction("1");
    typeDocCree.setDureeConservation(300);
    typeDocCree.setLibelle("Libellé 1.2.1.1.1");
    typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

    supportFacade.ajouterRnd(typeDocCree);

    // On recherche le rnd avec la facade
    final TypeDocument typeDocumentFacade = supportFacade.getRnd(code);
    // On recherche le rnd thrift
    final TypeDocument typeDocumentThrift = support.getRnd(code);
    // On vérifie que les deux dictionary sont bien les mêmes
    Assert.assertEquals(typeDocumentFacade, typeDocumentThrift);
  }

  @Test
  public void testCreationDualReadCql() throws CodeRndInexistantException {
    // On se met sur mode dual read cql
    GestionModeApiUtils.setModeApiDualReadCql(Constantes.CF_RND);
    // On ajoute un rnd avec la facade sur les tables thrift et cql
    final String code = "1.2.1.1.1";
    final TypeDocument typeDocCree = new TypeDocument();
    typeDocCree.setCloture(false);
    typeDocCree.setCode("1.2.1.1.1");
    typeDocCree.setCodeActivite("2");
    typeDocCree.setCodeFonction("1");
    typeDocCree.setDureeConservation(300);
    typeDocCree.setLibelle("Libellé 1.2.1.1.1");
    typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

    supportFacade.ajouterRnd(typeDocCree);

    // On recherche le rnd avec la facade
    final TypeDocument typeDocumentFacade = supportFacade.getRnd(code);
    // On recherche le rnd thrift
    final TypeDocument typeDocumentCql = supportCql.getRnd(code);
    // On vérifie que les deux dictionary sont bien les mêmes
    Assert.assertEquals(typeDocumentFacade, typeDocumentCql);
  }

  @Test
  public void zzz() {
    try {
      if (server.isCassandraStarted()) {
        server.resetData();
      }
      Assert.assertTrue(true);
    }
    catch (final Exception e) {
      LOGGER.error("Une erreur s'est produite lors du resetData de cassandra: {}", e.getMessage());
    }
  }

}
