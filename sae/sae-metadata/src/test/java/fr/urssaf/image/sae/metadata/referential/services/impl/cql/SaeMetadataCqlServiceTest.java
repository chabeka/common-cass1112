package fr.urssaf.image.sae.metadata.referential.services.impl.cql;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.services.impl.SaeMetaDataServiceImpl;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;


/**
 * classe de test du service {@link SaeMetaDataServiceImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
@Ignore("Ces tests accèdent directement à DFCE. Il s'agit de tests d'intégration.")
public class SaeMetadataCqlServiceTest {

  @Autowired
  private SaeMetadataCqlSupport metaCqlSupport;

  @Autowired
  private SaeMetaDataService service;

  @Autowired
  private CassandraServerBean server;

  private final String cfName = Constantes.CF_METADATA;

  @Before
  public void setup() throws Exception {

    GestionModeApiUtils.setModeApiCql(cfName);
  }

  @After
  public void end() throws Exception {
    server.resetData(true, MODE_API.DATASTAX);
  }

  /**
   * test permettant de verifier qu'une métadonnée est bien enregistré en base
   * et qu'il est possible de la récupérer
   */

  @Test
  public void testServiceCreate() {

    final MetadataReference metaDest = new MetadataReference();
    metaDest.setArchivable(Boolean.TRUE);//
    metaDest.setConsultable(Boolean.TRUE);//
    metaDest.setDefaultConsultable(Boolean.FALSE);//
    metaDest.setDescription("meta test");//
    metaDest.setInternal(Boolean.TRUE);//
    metaDest.setLabel("creation Meta");//
    metaDest.setLength(10);//
    metaDest.setLongCode("metadonne2");//
    metaDest.setPattern(StringUtils.EMPTY);//
    metaDest.setRequiredForArchival(Boolean.FALSE);//
    metaDest.setRequiredForStorage(Boolean.FALSE);//
    metaDest.setSearchable(Boolean.TRUE);//
    metaDest.setShortCode("meta2");//
    metaDest.setType("String");//
    metaDest.setHasDictionary(Boolean.FALSE);//
    metaDest.setDictionaryName(StringUtils.EMPTY);//
    metaDest.setIsIndexed(Boolean.TRUE);//
    metaDest.setLeftTrimable(true);//
    metaDest.setRightTrimable(true);//
    metaDest.setTransferable(true);//
    metaDest.setModifiable(false);//
    metaDest.setClientAvailable(true);//
    service.create(metaDest);

    final MetadataReference metafind = metaCqlSupport.find(metaDest.getLongCode());
    Assert.assertNotNull(metafind);
    Assert.assertEquals("description fausse", metaDest.getDescription(),
                        metafind.getDescription());
    Assert.assertEquals("code court faux", metaDest.getShortCode(), metafind
                        .getShortCode());
    Assert.assertEquals("code long faux", metaDest.getLongCode(), metafind
                        .getLongCode());
    Assert.assertEquals("nom du dictionnaire faux", metaDest
                        .getDictionaryName(), metafind.getDictionaryName());
    Assert.assertEquals("consultable faux", metaDest.isConsultable(),
                        metafind.isConsultable());
    Assert.assertEquals("default consultable faux", metaDest
                        .isDefaultConsultable(), metafind.isDefaultConsultable());
    Assert.assertEquals("recherchable faux", metaDest.isSearchable(),
                        metafind.isSearchable());
    Assert.assertEquals("obligatoire à l'archivage faux", metaDest
                        .isRequiredForArchival(), metafind.isRequiredForArchival());
    Assert.assertEquals("obligatoire au stockage faux", metaDest
                        .isRequiredForStorage(), metafind.isRequiredForStorage());
    Assert.assertEquals("valeur interne faux", metaDest.isInternal(),
                        metafind.isInternal());
    Assert.assertEquals("recherchable faux", metaDest.isSearchable(),
                        metafind.isSearchable());
  }
}
