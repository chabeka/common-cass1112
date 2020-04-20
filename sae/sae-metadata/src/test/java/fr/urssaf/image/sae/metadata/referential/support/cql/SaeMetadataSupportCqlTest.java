package fr.urssaf.image.sae.metadata.referential.support.cql;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.impl.DictionaryServiceImpl;


/**
 * classe de test du support cql {@link DictionaryServiceImpl}
 */
//FIXME : réactiver les testes après la mise en place des fichiers application contexte
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class SaeMetadataSupportCqlTest {

  @Autowired
  SaeMetadataCqlSupport metaCqlSupport;



  @Autowired
  private CassandraServerBean server;


  @After
  public void after() throws Exception {
    server.resetData(true, MODE_API.DATASTAX);
  }

  /**
   * test permettant de verifier qu'une métadonnée est bien enregistré en base et qu'il est possible de la récupérer
   * @throws DictionaryNotFoundException
   */
  @Test
  public void createMetaTest() throws DictionaryNotFoundException{

    final MetadataReference metaDest = new MetadataReference();

    metaDest.setArchivable(Boolean.TRUE);
    metaDest.setConsultable(Boolean.TRUE);
    metaDest.setDefaultConsultable(Boolean.FALSE);
    metaDest.setDescription("meta test");
    metaDest.setInternal(Boolean.TRUE);
    metaDest.setLabel("creation Meta");
    metaDest.setLength(10);
    metaDest.setLongCode("metadonne2");
    metaDest.setPattern(StringUtils.EMPTY);
    metaDest.setRequiredForArchival(Boolean.FALSE);
    metaDest.setRequiredForStorage(Boolean.FALSE);
    metaDest.setSearchable(Boolean.TRUE);
    metaDest.setShortCode("meta2");
    metaDest.setType("Srting");
    metaDest.setHasDictionary(Boolean.FALSE);
    metaDest.setDictionaryName(StringUtils.EMPTY);
    metaDest.setIsIndexed(Boolean.TRUE);
    metaDest.setTransferable(Boolean.TRUE);


    metaCqlSupport.create(metaDest);
    final MetadataReference metafind = metaCqlSupport.find(metaDest.getLongCode());
    Assert.assertNotNull(metafind);
    Assert.assertEquals("description fausse",metaDest.getDescription(), metafind.getDescription());
    Assert.assertEquals("code court faux",metaDest.getShortCode(), metafind.getShortCode());
    Assert.assertEquals("code long faux",metaDest.getLongCode(), metafind.getLongCode());
    Assert.assertEquals("nom du dictionnaire faux", metaDest.getDictionaryName(), metafind.getDictionaryName());
    Assert.assertEquals("consultable faux",metaDest.isConsultable(), metafind.isConsultable());
    Assert.assertEquals("default consultable faux",metaDest.isDefaultConsultable(), metafind.isDefaultConsultable());
    Assert.assertEquals("recherchable faux",metaDest.isSearchable(), metafind.isSearchable());
    Assert.assertEquals("obligatoire à l'archivage faux",metaDest.isRequiredForArchival(), metafind.isRequiredForArchival());
    Assert.assertEquals("obligatoire au stockage faux",metaDest.isRequiredForStorage(), metafind.isRequiredForStorage());
    Assert.assertEquals("valeur interne faux",metaDest.isInternal(), metafind.isInternal());
    Assert.assertEquals("transferable faux",metaDest.getTransferable(), metafind.getTransferable());
  }

  /**
   * Teste permettant de vérifier que tous les éléments de la CF sont renvoyés
   */

  @Test
  public void findAllMetaTest(){
    final List<MetadataReference> listeMeta = metaCqlSupport.findAll();
    Assert.assertEquals(0, listeMeta.size());
  }

  @Test
  public void modifyMetaTest(){

  }

  @Test(expected=MetadataRuntimeException.class)
  public void createSameShortCode() {

    MetadataReference metaDest = new MetadataReference();

    metaDest.setArchivable(Boolean.TRUE);
    metaDest.setConsultable(Boolean.TRUE);
    metaDest.setDefaultConsultable(Boolean.FALSE);
    metaDest.setDescription("meta test");
    metaDest.setInternal(Boolean.TRUE);
    metaDest.setLabel("creation Meta");
    metaDest.setLength(10);
    metaDest.setLongCode("metadonne4");
    metaDest.setPattern(StringUtils.EMPTY);
    metaDest.setRequiredForArchival(Boolean.FALSE);
    metaDest.setRequiredForStorage(Boolean.FALSE);
    metaDest.setSearchable(Boolean.TRUE);
    metaDest.setShortCode("meta2");
    metaDest.setType("Srting");
    metaDest.setHasDictionary(Boolean.FALSE);
    metaDest.setDictionaryName(StringUtils.EMPTY);
    metaDest.setIsIndexed(Boolean.TRUE);


    try {
      metaCqlSupport.create(metaDest);
    } catch (final Exception e) {
      Assert.fail("erreur non attendue");
    }

    metaDest = new MetadataReference();

    metaDest.setArchivable(Boolean.TRUE);
    metaDest.setConsultable(Boolean.TRUE);
    metaDest.setDefaultConsultable(Boolean.FALSE);
    metaDest.setDescription("meta test");
    metaDest.setInternal(Boolean.TRUE);
    metaDest.setLabel("creation Meta");
    metaDest.setLength(10);
    metaDest.setLongCode("metadonne3");
    metaDest.setPattern(StringUtils.EMPTY);
    metaDest.setRequiredForArchival(Boolean.FALSE);
    metaDest.setRequiredForStorage(Boolean.FALSE);
    metaDest.setSearchable(Boolean.TRUE);
    metaDest.setShortCode("meta2");
    metaDest.setType("Srting");
    metaDest.setHasDictionary(Boolean.FALSE);
    metaDest.setDictionaryName(StringUtils.EMPTY);
    metaDest.setIsIndexed(Boolean.TRUE);


    metaCqlSupport.create(metaDest);
  }

}
