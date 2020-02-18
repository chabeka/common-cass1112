package fr.urssaf.image.sae.metadata.control.services.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * 
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkConsultableMetadata(List)}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TrimMetaControlServicesImplTest {

  @Autowired
  @Qualifier("metadataControlServices")
  private MetadataControlServices controlService;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(TrimMetaControlServicesImplTest.class);

  @After
  public void after() throws Exception {
    server.resetData();
  }

  @Before
  public void start() throws Exception {
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
  }

  @Test
  public void init() throws Exception {

    if (server.isCassandraStarted()) {
      server.resetData();
    }
    Assert.assertTrue(true);
  }

  /**
   * Vérifie que les métadonnées devant être trimées l'ont bien été
   */
  @Test
  public void checkTrimMetadata()
      throws FileNotFoundException {

    final List<SAEMetadata> metadatas = MetadataDataProviderUtils.getSAEMetadata(Constants.TRIM_FILE_1);
    final String contratService = "";
    final List<String> listePagm = new ArrayList<>();
    final String login = "";
    final List<SAEMetadata> trimMetadatas = controlService.trimMetadata(metadatas, contratService, listePagm, login);

    for (final SAEMetadata saeMetadata : trimMetadatas) {
      if (saeMetadata.getLongCode().equals("Denomination")) {
        Assert.assertEquals("La dénomination doit avoir été trimée", "Denomination", saeMetadata.getValue());      
      }
    }

  }

}
