package fr.urssaf.image.sae.metadata.referential.support.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
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
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.DictionaryCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DictionarySupportFacadeTest {


  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private DictionarySupportFacade supportFacade;

  @Autowired
  private DictionarySupport support;

  @Autowired
  private DictionaryCqlSupport supportCql;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @After
  public void end() throws Exception {
    cassandraServer.resetDataOnly();
    ;
  }

  @Test
  public void init() throws Exception {

    if (cassandraServer.isCassandraStarted()) {
      cassandraServer.resetData();
    }
    Assert.assertTrue(true);

  }

  @Test(expected = ModeGestionAPIUnkownException.class)
  public void testModeAPIInconnu() throws DictionaryNotFoundException {
    // On se met sur mode API inconnu
    modeApiSupport.updateModeApi("UNKNOWN", Constantes.CF_DICTIONARY);

    supportFacade.find("TEST");
  }

  @Test
  public void testCreationDualReadThrift() throws DictionaryNotFoundException {
    // On se met sur mode dual read thrift
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT, Constantes.CF_DICTIONARY);
    // On ajoute un dictionary avec la facade sur les tables thrift et cql
    final String identifiant = "idTest";
    supportFacade.addElement(identifiant, "valueTest");
    // On recherche le dictionary avec la facade
    final Dictionary dictionaryFacade = supportFacade.find(identifiant);
    // On recherche le dictionary thrift
    final Dictionary dictionaryThrift = support.find(identifiant);
    // On vérifie que les deux dictionary sont bien les mêmes
    Assert.assertEquals(dictionaryFacade, dictionaryThrift);
  }

  @Test
  public void testCreationDualReadCql() throws DictionaryNotFoundException {
    // On se met sur mode dual read cql
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL, Constantes.CF_DICTIONARY);
    // On ajoute un dictionary avec la facade sur les tables thrift et cql
    final String identifiant = "idTest";
    supportFacade.addElement(identifiant, "valueTest");
    // On recherche le dictionary avec la facade
    final Dictionary dictionaryFacade = supportFacade.find(identifiant);
    // On recherche le dictionary Cql
    final Dictionary dictionaryCql = supportCql.find(identifiant);
    // On vérifie que les deux dictionary sont bien les mêmes
    Assert.assertEquals(dictionaryFacade, dictionaryCql);
  }


}
