package fr.urssaf.image.sae.metadata.control.services.impl;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkSearchableMetadata(List)}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SearchableControlServicesImplTest {
  @Autowired
  @Qualifier("metadataControlServices")
  private MetadataControlServices controlService;

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


  /**
   * Fournit des données pour valider la méthode
   * {@link MetadataControlServicesImpl#checkConsultableMetadata(List)}
   * 
   * @param withoutFault
   *            : boolean qui permet de prendre en compte un intrus
   * @return La liste des métadonnées.
   * @throws FileNotFoundException
   *             Exception levé lorsque le fichier n'existe pas.
   */
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  public final List<SAEMetadata> searchableData(final boolean withoutFault)
      throws FileNotFoundException {
    List<SAEMetadata> metadatas = null;
    if (withoutFault) {
      metadatas = MetadataDataProviderUtils
          .getSAEMetadata(Constants.SEARCHABLE_FILE_1);
    } else {
      metadatas = MetadataDataProviderUtils
          .getSAEMetadata(Constants.SEARCHABLE_FILE_2);
    }
    return metadatas;
  }

  /**
   * Vérifie que la liste ne contenant pas d'intrus est valide
   * 
   * @throws FileNotFoundException
   *             Exception levé lorsque le fichier n'existe pas.
   */
  @Test
  public void checkSearchableMetadataWithSearchableMetadata()
      throws FileNotFoundException {
    Assert.assertTrue(controlService.checkSearchableMetadata(
                                                             searchableData(true)).isEmpty());
  }

  /**
   * Vérifie que la liste contenant un intrus n'est valide.
   * 
   * @throws FileNotFoundException
   *             Exception levé lorsque le fichier n'existe pas.
   */
  @Test
  public void checkSearchableMetadataWithNotSearchableMetadata()
      throws FileNotFoundException {
    Assert.assertTrue(!controlService.checkSearchableMetadata(
                                                              searchableData(false)).isEmpty());
  }



}
