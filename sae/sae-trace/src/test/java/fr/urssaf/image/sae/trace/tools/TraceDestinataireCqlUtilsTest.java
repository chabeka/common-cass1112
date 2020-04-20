/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.trace.tools;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;

/**
 * Test extraction de ressources par fichier xml (AC75095351)
 */
public class TraceDestinataireCqlUtilsTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TraceDestinataireCqlUtilsTest.class);

  @Test
  public void test() throws URISyntaxException {

    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-traces.xml");
    LOGGER.warn("url" + url);
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.toURI().getPath(), "TraceDestinataire");
    LOGGER.warn("sizeList=" + list.size());
    final List<TraceDestinataire> listTraceDestinataire = TraceDestinataireCqlUtils.convertRowsToTraceDestinataires(list);
    LOGGER.warn("sizeListTraceDestinataire=" + listTraceDestinataire.size());
    Assert.assertTrue(listTraceDestinataire.size() == 20);
  }
}
