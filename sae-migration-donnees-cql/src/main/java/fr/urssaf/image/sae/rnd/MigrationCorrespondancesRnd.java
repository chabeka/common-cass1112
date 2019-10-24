/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.rnd;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;

/**
 * (AC75095351) Classe de migration Rnd Thrift<-> Cql
 */
@Component
public class MigrationCorrespondancesRnd {

  @Autowired
  private ICorrespondancesDaoCql correspondanceDaoCql;

  @Autowired
  private CorrespondancesRndSupport correspondancesSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationCorrespondancesRnd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- start ");

    final List<Correspondance> correspondances = correspondancesSupport.getAllCorrespondances();

    if (!correspondances.isEmpty()) {
      correspondanceDaoCql.saveAll(correspondances);
    }

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromCqlTothrift- start ");


    final Iterator<Correspondance> correspondances = correspondanceDaoCql.findAllWithMapper();

    while (correspondances.hasNext()) {
      final Correspondance correspondance = correspondances.next();
      correspondancesSupport.ajouterCorrespondance(correspondance, new Date().getTime());
    }


    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromCqlTothrift- end ");
  }
}
