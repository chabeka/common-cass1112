package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IPagmaDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;

/**
 * (AC75095351) Classe de migration Pagma Thrift<-> Cql
 */
@Component
public class MigrationPagma implements IMigrationR {

  @Autowired
  private IPagmaDaoCql pagmaDaoCql;

  @Autowired
  private PagmaSupport pagmaSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagma.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql(final Javers javers) {

    MigrationPagma.LOGGER.info(" MIGRATION_PAGMA - migrationFromThriftToCql- start ");

    final List<Pagma> pagmasThrift = pagmaSupport.findAll();

    if (!pagmasThrift.isEmpty()) {
      pagmaDaoCql.saveAll(pagmasThrift);
    }
    final List<Pagma> pagmasCql = new ArrayList<>();
    final Iterator<Pagma> pagmasIterator = pagmaDaoCql.findAllWithMapper();
    pagmasIterator.forEachRemaining(pagmasCql::add);
    final Diff diff = comparePagmas(pagmasThrift, pagmasCql, javers);
    LOGGER.info(" MIGRATION_PAGMA - migrationFromThriftToCql- end ");
    LOGGER.info(" MIGRATION_PAGMA - migrationFromThriftToCql- nbThrift={} ", pagmasThrift.size());
    LOGGER.info(" MIGRATION_PAGMA - migrationFromThriftToCql- nbCql={} ", pagmasCql.size());

    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift(final Javers javers) {

    MigrationPagma.LOGGER.info(" MIGRATION_PAGMA - migrationFromCqlTothrift- start ");

    final Iterator<Pagma> pagmas = pagmaDaoCql.findAllWithMapper();
    final List<Pagma> pagmasCql = new ArrayList<>();
    while (pagmas.hasNext()) {
      final Pagma pagma = pagmas.next();
      pagmasCql.add(pagma);
      pagmaSupport.create(pagma, new Date().getTime());
    }
    final List<Pagma> pagmasThrift = pagmaSupport.findAll();
    final Diff diff = comparePagmas(pagmasThrift, pagmasCql, javers);
    LOGGER.info(" MIGRATION_PAGMA - migrationFromCqlTothrift- end ");
    LOGGER.info(" MIGRATION_PAGMA - migrationFromCqlTothrift- nbThrift={} ", pagmasThrift.size());
    LOGGER.info(" MIGRATION_PAGMA - migrationFromCqlTothrift- nbCql={} ", pagmasCql.size());
    return diff;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param pagmasThrift
   * @param pagmasCql
   */
  public Diff comparePagmas(final List<Pagma> pagmasThrift, final List<Pagma> pagmasCql, final Javers javers) {

    Collections.sort(pagmasThrift);
    Collections.sort(pagmasCql);

    final Diff diff = javers.compareCollections(pagmasThrift, pagmasCql, Pagma.class);
    return diff;
  }
}
