
package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IPagmfDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

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
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_PAGMF - migrationFromThriftToCql- start ");

    final List<Pagmf> pagmfsThrift = pagmfSupport.findAll();

    if (!pagmfsThrift.isEmpty()) {
      pagmfDaoCql.saveAll(pagmfsThrift);
    }
    final List<Pagmf> pagmfsCql = new ArrayList<>();
    final Iterator<Pagmf> pagmfsIterator = pagmfDaoCql.findAllWithMapper();
    pagmfsIterator.forEachRemaining(pagmfsCql::add);
    final boolean result = comparePagmfs(pagmfsThrift, pagmfsCql);
    LOGGER.info(" MIGRATION_PAGMF - migrationFromThriftToCql- end ");
    return result;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_PAGMF - migrationFromCqlTothrift- start ");

    final Iterator<Pagmf> pagmfs = pagmfDaoCql.findAllWithMapper();
    final List<Pagmf> pagmfsCql = new ArrayList<>();
    while (pagmfs.hasNext()) {
      final Pagmf pagmf = pagmfs.next();
      pagmfsCql.add(pagmf);
      pagmfSupport.create(pagmf, new Date().getTime());
    }
    final List<Pagmf> pagmfsThrift = pagmfSupport.findAll();
    final boolean result = comparePagmfs(pagmfsThrift, pagmfsCql);
    LOGGER.info(" MIGRATION_PAGMF - migrationFromCqlTothrift- end ");
    return result;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param pagmfsThrift
   * @param pagmfsCql
   */
  private boolean comparePagmfs(final List<Pagmf> PagmfsThrift, final List<Pagmf> PagmfsCql) {
    final boolean result = CompareUtils.compareListsGeneric(PagmfsThrift, PagmfsCql);

    if (result) {
      LOGGER.info("MIGRATION_PAGMF -- Les listes Pagmf sont identiques");
    } else {
      LOGGER.info("MIGRATION_PAGMF -- NbThrift=" + PagmfsThrift.size());
      LOGGER.info("MIGRATION_PAGMF-- NbCql=" + PagmfsCql.size());
      LOGGER.warn("MIGRATION_PAGMF -- ATTENTION: Les listes Pagmf sont différentes ");
    }
    return result;
  }
}
