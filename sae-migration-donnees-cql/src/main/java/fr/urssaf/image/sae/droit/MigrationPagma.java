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

import fr.urssaf.image.sae.droit.dao.cql.IPagmaDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;

/**
 * (AC75095351) Classe de migration Pagma Thrift<-> Cql
 */
@Component
public class MigrationPagma {

  @Autowired
  private IPagmaDaoCql pagmaDaoCql;

  @Autowired
  private PagmaSupport pagmaSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagma.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationPagm - migrationFromThriftToCql- start ");

    final List<Pagma> pagmas = pagmaSupport.findAll();

    if (!pagmas.isEmpty()) {
      pagmaDaoCql.saveAll(pagmas);
    }

    LOGGER.info(" MigrationPagm - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationPagm - migrationFromCqlTothrift- start ");

    final Iterator<Pagma> pagmas = pagmaDaoCql.findAllWithMapper();
    while (pagmas.hasNext()) {
      final Pagma pagma = pagmas.next();
      pagmaSupport.create(pagma, new Date().getTime());
    }

    LOGGER.info(" MigrationPagm - migrationFromCqlTothrift- end ");
  }
}
