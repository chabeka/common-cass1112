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
import fr.urssaf.image.sae.droit.dao.cql.IPagmpDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;

/**
 * (AC75095351) Classe de migration Pagmp Thrift<-> Cql
 */
@Component
public class MigrationPagmp implements IMigrationR {

  @Autowired
  private IPagmpDaoCql pagmpDaoCql;

  @Autowired
  private PagmpSupport pagmpSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmp.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql(final Javers javers) {

    MigrationPagmp.LOGGER.info(" MIGRATION_PAGMP - migrationFromThriftToCql- start ");

    final List<Pagmp> pagmpsThrift = pagmpSupport.findAll();

    if (!pagmpsThrift.isEmpty()) {
      pagmpDaoCql.saveAll(pagmpsThrift);
    }
    final List<Pagmp> pagmpsCql = new ArrayList<>();
    final Iterator<Pagmp> pagmpsIterator = pagmpDaoCql.findAllWithMapper();
    pagmpsIterator.forEachRemaining(pagmpsCql::add);
    final Diff diff = comparePagmps(pagmpsThrift, pagmpsCql, javers);
    MigrationPagmp.LOGGER.info(" MIGRATION_PAGMP - migrationFromThriftToCql- end ");
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift(final Javers javers) {

    MigrationPagmp.LOGGER.info(" MIGRATION_PAGMP - migrationFromCqlTothrift- start ");

    final Iterator<Pagmp> pagmps = pagmpDaoCql.findAllWithMapper();
    final List<Pagmp> pagmpsCql = new ArrayList<>();
    while (pagmps.hasNext()) {
      final Pagmp pagmp = pagmps.next();
      pagmpsCql.add(pagmp);
      pagmpSupport.create(pagmp, new Date().getTime());
    }
    final List<Pagmp> pagmpsThrift = pagmpSupport.findAll();
    final Diff diff = comparePagmps(pagmpsThrift, pagmpsCql, javers);
    MigrationPagmp.LOGGER.info(" MIGRATION_PAGMP - migrationFromCqlTothrift- end ");
    return diff;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param pagmpsThrift
   * @param pagmpsCql
   */
  public Diff comparePagmps(final List<Pagmp> pagmpsThrift, final List<Pagmp> pagmpsCql, final Javers javers) {

    Collections.sort(pagmpsThrift);
    Collections.sort(pagmpsCql);
    final Diff diff = javers.compareCollections(pagmpsThrift, pagmpsCql, Pagmp.class);
    return diff;

  }
}
