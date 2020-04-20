/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata.test.utils;

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.utils.MetadataUtils;

/**
 * (AC75095351) Classe de test de MetadataUtils pour extraction de dataset
 */
public class MetadataUtilsTest {

  @Test
  public void testExtractDataset() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-metadonnees.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "Metadata");

    final List<MetadataReference> listMetaData = MetadataUtils.convertRowsToMetadata(list);
    Assert.assertTrue(listMetaData.size() == 74);// Attention une valeur est répétée
  }

  @Test
  public void testExtractDataset2() {
    /*
     * final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-metadonnees.xml");
     * final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "Metadata");
     */

    final List<MetadataReference> listMetaData = MetadataUtils.getMetadataFromFile(MockFactoryBean.class);
    Assert.assertTrue(listMetaData.size() == 74);// Attention une valeur est répétée
  }
}
