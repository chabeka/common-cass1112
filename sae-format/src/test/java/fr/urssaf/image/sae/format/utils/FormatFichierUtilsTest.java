/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.format.utils;

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * (AC75095351) Test d'extraction de FormatFichier à partir de dataset thrift
 */
public class FormatFichierUtilsTest {

  @Test
  public void testExtractFormatFichier() {
    // final URL url = this.getClass().getResource("/cassandra-local-datasets/cassandra-local-dataset-sae-format.xml");
    final URL url = this.getClass().getResource("/cassandra-local-datasets//cassandra-local-dataset-failure-sae-format.xml");

    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "ReferentielFormat");
    final List<FormatFichier> listFormatFichier = FormatFichierUtils.convertRowsToFormatFichier(list);
    Assert.assertTrue(listFormatFichier.size() == list.size());
  }
}
