/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.cql.IPagmpDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;

/**
 * (AC75095351) Classe de migration Pagmp Thrift<-> Cql
 */
@Component
public class MigrationPagmp {

  @Autowired
  private IPagmpDaoCql pagmpDaoCql;

  @Autowired
  private PagmpSupport pagmpSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmp.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationPagmp - migrationFromThriftToCql- start ");

    final List<Pagmp> pagmps = pagmpSupport.findAll();

    if (!pagmps.isEmpty()) {
      pagmpDaoCql.saveAll(pagmps);
    }

    LOGGER.info(" MigrationPagmp - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationPagmp - migrationFromCqlTothrift- start ");

    final Iterator<Pagmp> pagmps = pagmpDaoCql.findAllWithMapper();
    while (pagmps.hasNext()) {
      final Pagmp pagmp = pagmps.next();
      pagmpSupport.create(pagmp, new Date().getTime());
    }

    LOGGER.info(" MigrationPagmp - migrationFromCqlTothrift- end ");
  }
}
