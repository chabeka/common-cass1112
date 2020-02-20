package fr.urssaf.image.sae.format.utils;

import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.validation.service.impl.ValidationServiceImpl;


/**
 * 
 * TU pour la classe {@link ValidationServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */

@ContextConfiguration(locations = {"/applicationContext-sae-format-failure-test.xml"})
public class AbstractReferentielFormatCqlFailureTest {

  @Autowired
  private CassandraServerBean server;
  @Autowired
  private ReferentielFormatSupportFacade referentielFormatSupportFacade;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;
  @Before
  public void setup() throws Exception {

    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
    if (server.getStartLocal()) {
      createReferentielFormat();
    }
  }

  private void createReferentielFormat() {
    final URL url = this.getClass().getResource("/cassandra-local-datasets/cassandra-local-dataset-failure-sae-format.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "ReferentielFormat");
    final List<FormatFichier> listFormatFichier = FormatFichierUtils.convertRowsToFormatFichier(list);
    for (final FormatFichier formatFichier : listFormatFichier) {
      referentielFormatSupportFacade.create(formatFichier);
    }
  }

}
