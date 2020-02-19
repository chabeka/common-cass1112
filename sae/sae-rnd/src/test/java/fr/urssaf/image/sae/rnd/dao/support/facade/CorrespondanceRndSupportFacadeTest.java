package fr.urssaf.image.sae.rnd.dao.support.facade;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.CorrespondancesRndCqlSupport;
import fr.urssaf.image.sae.rnd.exception.MajCorrespondancesException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CorrespondanceRndSupportFacadeTest {

  @Autowired
  private CorrespondancesRndSupportFacade supportFacade;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private CorrespondancesRndSupport support;

  @Autowired
  private CorrespondancesRndCqlSupport supportCql;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @After
  public void after() throws Exception {
    server.clearTables();
  }

  @Ignore
  @Test(expected = ModeGestionAPIUnkownException.class)
  public void testModeAPIInconnu() throws MajCorrespondancesException {
    // On se met sur mode API inconnu
    modeApiSupport.updateModeApi("UNKNOWN", Constantes.CF_CORRESPONDANCES_RND);
    supportFacade.getCorrespondance("TEST", "1");
  }

  @Test
  public void testCreationDualReadThrift() throws MajCorrespondancesException {
    // On se met sur mode dual read thrift
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT, Constantes.CF_CORRESPONDANCES_RND);

    // On ajoute un rnd avec la facade sur les tables thrift et cql
    final String codeTemporaire = "a.a.a.a.a";
    final String version = "11.4";
    final String codeDefinitif = "1.1.1.1.1";
    final Correspondance correspondance = new Correspondance();
    correspondance.setCodeDefinitif(codeDefinitif);
    correspondance.setCodeTemporaire(codeTemporaire);
    correspondance.setDateDebutMaj(new Date());
    correspondance.setDateFinMaj(new Date());
    correspondance.setEtat(EtatCorrespondance.CREATED);
    correspondance.setVersionCourante(version);

    supportFacade.ajouterCorrespondance(correspondance);

    // On recherche le rnd avec la facade
    final Correspondance correspondanceFacade = supportFacade.getCorrespondance(codeTemporaire, version);
    // On recherche le rnd thrift
    final Correspondance correspondanceThrift = support.find(codeTemporaire, version);
    // On vérifie que les deux dictionary sont bien les mêmes
    Assert.assertEquals(correspondanceFacade, correspondanceThrift);
  }

  @Test
  public void testCreationDualReadCql() throws MajCorrespondancesException {
    // On se met sur mode dual read cql
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL, Constantes.CF_CORRESPONDANCES_RND);
    // On ajoute un rnd avec la facade sur les tables thrift et cql
    final String codeTemporaire = "a.a.a.a.a";
    final String version = "11.4";
    final String codeDefinitif = "1.1.1.1.1";
    final Correspondance correspondance = new Correspondance();
    correspondance.setCodeDefinitif(codeDefinitif);
    correspondance.setCodeTemporaire(codeTemporaire);
    correspondance.setDateDebutMaj(new Date());
    correspondance.setDateFinMaj(new Date());
    correspondance.setEtat(EtatCorrespondance.CREATED);
    correspondance.setVersionCourante(version);

    supportFacade.ajouterCorrespondance(correspondance);

    // On recherche le rnd avec la facade
    final Correspondance correspondanceFacade = supportFacade.getCorrespondance(codeTemporaire, version);
    Assert.assertNotNull(correspondanceFacade);
    // On recherche le rnd thrift
    final Correspondance correspondanceCql = supportCql.find(codeTemporaire, version);
    Assert.assertNotNull(correspondanceCql);
    // On vérifie que les deux dictionary sont bien les mêmes
    Assert.assertEquals(correspondanceFacade, correspondanceCql);
  }

}
