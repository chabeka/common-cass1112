/**
 *   (AC75095351) Description du fichier
 */
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
import fr.urssaf.image.sae.droit.dao.cql.IPagmaDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Pagma Thrift<-> Cql
 */
@Component
public class MigrationPagma implements IMigration {

  @Autowired
  private IPagmaDaoCql pagmaDaoCql;

  @Autowired
  private PagmaSupport pagmaSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagma.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_PAGMA - migrationFromThriftToCql- start ");

    final List<Pagma> pagmasThrift = pagmaSupport.findAll();

    if (!pagmasThrift.isEmpty()) {
      pagmaDaoCql.saveAll(pagmasThrift);
    }
    final List<Pagma> pagmasCql = new ArrayList<>();
    final Iterator<Pagma> pagmasIterator = pagmaDaoCql.findAllWithMapper();
    pagmasIterator.forEachRemaining(pagmasCql::add);
    comparePagmas(pagmasThrift, pagmasCql);
    LOGGER.info(" MIGRATION_PAGMA - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_PAGMA - migrationFromCqlTothrift- start ");

    final Iterator<Pagma> pagmas = pagmaDaoCql.findAllWithMapper();
    final List<Pagma> pagmasCql = new ArrayList<>();
    while (pagmas.hasNext()) {
      final Pagma pagma = pagmas.next();
      pagmasCql.add(pagma);
      pagmaSupport.create(pagma, new Date().getTime());
    }
    final List<Pagma> pagmasThrift = pagmaSupport.findAll();
    comparePagmas(pagmasThrift, pagmasCql);
    LOGGER.info(" MIGRATION_PAGMA - migrationFromCqlTothrift- end ");
  }

  /**
   * Comparaison des liste en taille et en contenu
   * 
   * @param pagmasThrift
   * @param pagmasCql
   */
  private void comparePagmas(final List<Pagma> pagmasThrift, final List<Pagma> pagmasCql) {

    LOGGER.info("MIGRATION_PAGMA -- SizeThriftDroitPagma=" + pagmasThrift.size());
    LOGGER.info("MIGRATION_PAGMA -- SizeCqlDroitPagma=" + pagmasCql.size());
    if (CompareUtils.compareListsGeneric(pagmasThrift, pagmasCql)) {
      LOGGER.info("MIGRATION_PAGMA -- Les listes Pagma sont identiques");
    } else {
      LOGGER.warn("MIGRATION_PAGMA -- ATTENTION: Les listes Pagma sont différentes ");
    }
  }
}
