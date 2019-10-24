/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.droit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.cql.IPrmdDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;

/**
 * (AC75095351) Classe de migration Prmd Thrift<-> Cql
 */
@Component
public class MigrationPrmd {

  @Autowired
  private IPrmdDaoCql prmdDaoCql;

  @Autowired
  private PrmdSupport prmdSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPrmd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationPrmd - migrationFromThriftToCql- start ");

    final List<Prmd> prmds = prmdSupport.findAll();

    if (!prmds.isEmpty()) {
      prmdDaoCql.saveAll(prmds);
    }

    LOGGER.info(" MigrationPrmd - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationPrmd - migrationFromCqlTothrift- start ");

    final Iterator<Prmd> prmds = prmdDaoCql.findAllWithMapper();
    while (prmds.hasNext()) {
      final Prmd prmd = prmds.next();
      prmdSupport.create(prmd, new Date().getTime());
    }

    LOGGER.info(" MigrationPrmd - migrationFromCqlTothrift- end ");
  }
}
