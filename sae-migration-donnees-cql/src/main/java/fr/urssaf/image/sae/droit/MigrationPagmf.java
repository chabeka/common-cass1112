
package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IPagmfDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;

/**
 * (AC75095351) Classe de migration Pagmf Thrift<-> Cql
 */
@Component
public class MigrationPagmf implements IMigrationR {

  @Autowired
  private IPagmfDaoCql pagmfDaoCql;

  @Autowired
  private PagmfSupport pagmfSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmf.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql(final Javers javers) {

    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromThriftToCql- start ");

    final List<Pagmf> pagmfsThrift = pagmfSupport.findAll();

    if (!pagmfsThrift.isEmpty()) {
      pagmfDaoCql.saveAll(pagmfsThrift);
    }
    final List<Pagmf> pagmfsCql = new ArrayList<>();
    final Iterator<Pagmf> pagmfsIterator = pagmfDaoCql.findAllWithMapper();
    pagmfsIterator.forEachRemaining(pagmfsCql::add);
    final Diff diff = comparePagmfs(pagmfsThrift, pagmfsCql);
    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromThriftToCql- end ");
    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromThriftToCql- nbThrift= {} ", pagmfsThrift.size());
    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromThriftToCql- nbCql= {} ", pagmfsCql.size());
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift(final Javers javers) {

    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromCqlTothrift- start ");

    final Iterator<Pagmf> pagmfs = pagmfDaoCql.findAllWithMapper();
    final List<Pagmf> pagmfsCql = new ArrayList<>();
    while (pagmfs.hasNext()) {
      final Pagmf pagmf = pagmfs.next();
      pagmfsCql.add(pagmf);
      pagmfSupport.create(pagmf, new Date().getTime());
    }
    final List<Pagmf> pagmfsThrift = pagmfSupport.findAll();
    final Diff diff = comparePagmfs(pagmfsThrift, pagmfsCql);
    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromCqlTothrift- end ");
    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromCqlTothrift- nbThrift= {} ", pagmfsThrift.size());
    MigrationPagmf.LOGGER.info(" MIGRATION_PAGMF - migrationFromCqlTothrift- nbCql= {} ", pagmfsCql.size());
    return diff;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param pagmfsThrift
   * @param pagmfsCql
   */
  public Diff comparePagmfs(final List<Pagmf> pagmfsThrift, final List<Pagmf> pagmfsCql) {
    Collections.sort(pagmfsThrift);
    Collections.sort(pagmfsCql);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();
    final Diff diff = javers.compareCollections(pagmfsThrift, pagmfsCql, Pagmf.class);
    return diff;
  }
}
