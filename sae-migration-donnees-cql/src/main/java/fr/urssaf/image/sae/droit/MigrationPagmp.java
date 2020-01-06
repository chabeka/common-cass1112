package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.droit.dao.cql.IPagmpDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Pagmp Thrift<-> Cql
 */
@Component
public class MigrationPagmp implements IMigration {

  @Autowired
  private IPagmpDaoCql pagmpDaoCql;

  @Autowired
  private PagmpSupport pagmpSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagmp.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_PAGMP - migrationFromThriftToCql- start ");

    final List<Pagmp> pagmpsThrift = pagmpSupport.findAll();

    if (!pagmpsThrift.isEmpty()) {
      pagmpDaoCql.saveAll(pagmpsThrift);
    }
    final List<Pagmp> pagmpsCql = new ArrayList<>();
    final Iterator<Pagmp> pagmpsIterator = pagmpDaoCql.findAllWithMapper();
    pagmpsIterator.forEachRemaining(pagmpsCql::add);
    comparePagmps(pagmpsThrift, pagmpsCql);
    LOGGER.info(" MIGRATION_PAGMP - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_PAGMP - migrationFromCqlTothrift- start ");

    final Iterator<Pagmp> pagmps = pagmpDaoCql.findAllWithMapper();
    final List<Pagmp> pagmpsCql = new ArrayList<>();
    while (pagmps.hasNext()) {
      final Pagmp pagmp = pagmps.next();
      pagmpsCql.add(pagmp);
      pagmpSupport.create(pagmp, new Date().getTime());
    }
    final List<Pagmp> pagmpsThrift = pagmpSupport.findAll();
    comparePagmps(pagmpsThrift, pagmpsCql);
    LOGGER.info(" MIGRATION_PAGMP - migrationFromCqlTothrift- end ");
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param pagmpsThrift
   * @param pagmpsCql
   */
  private void comparePagmps(final List<Pagmp> pagmpsThrift, final List<Pagmp> pagmpsCql) {

    LOGGER.info("MIGRATION_PAGMP -- SizeThriftDroitpagmp=" + pagmpsThrift.size());
    LOGGER.info("MIGRATION_PAGMP -- SizeCqlDroitpagmp=" + pagmpsCql.size());
    if (CompareUtils.compareListsGeneric(pagmpsThrift, pagmpsCql)) {
      LOGGER.info("MIGRATION_PAGMP -- Les listes pagmp sont identiques");
    } else {
      LOGGER.warn("MIGRATION_PAGMP-- ATTENTION: Les listes pagmp sont différentes ");
    }
  }
}
