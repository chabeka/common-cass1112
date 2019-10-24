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

import fr.urssaf.image.sae.droit.dao.cql.IPagmfDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;

/**
 * (AC75095351) Classe de migration Pagmf Thrift<-> Cql
 */
@Component
public class MigrationPagmf {

  @Autowired
  private IPagmfDaoCql pagmfDaoCql;

  @Autowired
  private PagmfSupport pagmfSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmf.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationPagmf - migrationFromThriftToCql- start ");

    final List<Pagmf> pagmfs = pagmfSupport.findAll();

    if (!pagmfs.isEmpty()) {
      pagmfDaoCql.saveAll(pagmfs);
    }

    LOGGER.info(" MigrationPagmf - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationPagmf - migrationFromCqlTothrift- start ");

    final Iterator<Pagmf> pagmfs = pagmfDaoCql.findAllWithMapper();
    while (pagmfs.hasNext()) {
      final Pagmf pagmf = pagmfs.next();
      pagmfSupport.create(pagmf, new Date().getTime());
    }

    LOGGER.info(" MigrationPagmf - migrationFromCqlTothrift- end ");
  }
}
