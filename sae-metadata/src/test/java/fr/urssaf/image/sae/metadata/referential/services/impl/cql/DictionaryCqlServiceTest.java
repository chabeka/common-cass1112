package fr.urssaf.image.sae.metadata.referential.services.impl.cql;

import java.util.Arrays;
import java.util.List;

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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.support.cql.DictionaryCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;

/**
 * classe de test du service {@link DictionarySupportImpl}
 *
 */
// FIXME : réactiver les testes après la mise en place des fichiers application
// contexte
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DictionaryCqlServiceTest {

  @Autowired
  private DictionaryCqlSupport dictCqlSupport;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private DictionaryService service;

  private final String cfName = Constantes.CF_DICTIONARY;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @After
  public void after() throws Exception {
    server.resetDataOnly();
  }

  @Test
  public void init() throws Exception {

    if (server.isCassandraStarted()) {
      server.resetData();
    }
    Assert.assertTrue(true);
  }

  @Before
  public void setup() throws Exception {

    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, cfName);


  }

  /**
   * Test de création d'un dictionnaire
   */
  @Test
  public void testService() throws DictionaryNotFoundException {
    final String id = "dictionnaireTest";
    final List<String> values = Arrays.asList("ValeurTest", "ValeurTest2");
    service.addElements(id, values);
    final Dictionary dict = dictCqlSupport.find(id);
    Assert.assertNotNull(dict);
    Assert.assertTrue(id.equals(dict.getIdentifiant()));
  }

}
