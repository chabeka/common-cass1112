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
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaeMetadataSupportFacadeTest {


  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private SaeMetadataSupportFacade supportFacade;

  @Autowired
  private SaeMetadataSupport support;

  @Autowired
  private SaeMetadataCqlSupport supportCql;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @After
  public void end() throws Exception {
    cassandraServer.resetDataOnly();
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
    modeApiSupport.updateModeApi("UNKNOWN", Constantes.CF_METADATA);
    supportFacade.find("TEST");
  }

  @Test
  public void testCreationDualReadThrift() throws DictionaryNotFoundException {
    // On se met sur mode dual read thrift
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT, Constantes.CF_METADATA);
    final MetadataReference metadata = getMetadataReference();
    // On ajoute un metadata avec la facade sur les tables thrift et cql
    supportFacade.create(metadata);
    // On supprime le metadata cql
    // supportCql.delete(CODE1);
    // On recherche le metadata avec la facade
    final MetadataReference metadataFacade = supportFacade.find(metadata.getLongCode());
    // On recherche le metadata thrift
    final MetadataReference metadataThrift = support.find(metadata.getLongCode());
    // On vérifie que les deux metadata sont bien les mêmes
    Assert.assertEquals(metadataFacade, metadataThrift);
  }

  @Test
  public void testCreationDualReadCql() throws DictionaryNotFoundException {
    // On se met sur mode dual read cql
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL, Constantes.CF_METADATA);
    final MetadataReference metadata = getMetadataReference();
    // On ajoute un metadata avec la facade sur les tables thrift et cql
    supportFacade.create(metadata);
    // On recherche le metadata avec la facade
    final MetadataReference metadataFacade = supportFacade.find(metadata.getLongCode());
    // On recherche le metadata Cql
    final MetadataReference metadataCql = supportCql.find(metadata.getLongCode());
    // On vérifie que les deux metadata sont bien les mêmes
    Assert.assertEquals(metadataFacade, metadataCql);
  }

  /**
   * @return
   */
  private MetadataReference getMetadataReference() {
    final MetadataReference metadata = new MetadataReference();
    metadata.setLongCode("ApplicationTest");
    metadata.setShortCode("ate");
    metadata.setType("String");
    metadata.setRequiredForArchival(true);
    metadata.setRequiredForStorage(true);
    metadata.setLength(15);
    metadata.setPattern("");
    metadata.setConsultable(true);
    metadata.setDefaultConsultable(false);
    metadata.setSearchable(true);
    metadata.setInternal(false);
    metadata.setArchivable(true);
    metadata.setLabel("Application test");
    metadata.setDescription("Code de l'application qui a produit le fichier");
    metadata.setHasDictionary(false);
    metadata.setDictionaryName("");
    metadata.setIsIndexed(false);
    metadata.setModifiable(false);
    metadata.setClientAvailable(true);
    metadata.setLeftTrimable(true);
    metadata.setRightTrimable(true);
    metadata.setTransferable(true);
    return metadata;
  }
}
