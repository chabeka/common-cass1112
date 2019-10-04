/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.trace.tools;

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;

/**
 * TODO (AC75095351) Description du type
 *
 */
public class TraceDestinataireCqlUtilsTest {

  @Test
  public void test() {

    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-traces.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "TraceDestinataire");
    final List<TraceDestinataire> listTraceDestinataire = TraceDestinataireCqlUtils.convertRowsToTraceDestinataires(list);
    Assert.assertTrue(listTraceDestinataire.size() == 20);
  }
}
