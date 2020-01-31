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
import fr.urssaf.image.sae.droit.dao.cql.IPrmdDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;

/**
 * (AC75095351) Classe de migration Prmd Thrift<-> Cql
 */
@Component
public class MigrationPrmd implements IMigrationR {

  @Autowired
  private IPrmdDaoCql prmdDaoCql;

  @Autowired
  private PrmdSupport prmdSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPrmd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql(final Javers javers) {

    LOGGER.info(" MIGRATION_PRMD - migrationFromThriftToCql- start ");

    final List<Prmd> prmdsThrift = prmdSupport.findAll();

    if (!prmdsThrift.isEmpty()) {
      prmdDaoCql.saveAll(prmdsThrift);
    }
    final List<Prmd> prmdsCql = new ArrayList<>();
    final Iterator<Prmd> prmdsIterator = prmdDaoCql.findAllWithMapper();
    prmdsIterator.forEachRemaining(prmdsCql::add);
    final Diff diff = comparePrmds(prmdsThrift, prmdsCql, javers);
    LOGGER.info(" MIGRATION_PRMD - migrationFromThriftToCql- end ");
    LOGGER.info(" MIGRATION_PRMD - migrationFromThriftToCql- nbThrift={} ", prmdsThrift.size());
    LOGGER.info(" MIGRATION_PRMD - migrationFromThriftToCql- nbCql={} ", prmdsCql.size());
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift(final Javers javers) {

    MigrationPrmd.LOGGER.info(" MIGRATION_PRMD - migrationFromCqlTothrift- start ");

    final Iterator<Prmd> prmdsCqlIterator = prmdDaoCql.findAllWithMapper();
    final List<Prmd> prmdsCql = new ArrayList<>();
    while (prmdsCqlIterator.hasNext()) {
      final Prmd prmd = prmdsCqlIterator.next();
      prmdsCql.add(prmd);
      prmdSupport.create(prmd, new Date().getTime());
    }
    final List<Prmd> prmdsThrift = prmdSupport.findAll();
    final Diff diff = comparePrmds(prmdsThrift, prmdsCql, javers);
    LOGGER.info(" MIGRATION_PRMD - migrationFromCqlTothrift- end ");
    LOGGER.info(" MIGRATION_PRMD - migrationFromCqlTothrift- nbThrift={} ", prmdsThrift.size());
    LOGGER.info(" MIGRATION_PRMD - migrationFromCqlTothrift- nbCql={} ", prmdsCql.size());
    return diff;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param prmdsThrift
   * @param prmdsCql
   */
  public Diff comparePrmds(final List<Prmd> prmdsThrift, final List<Prmd> prmdsCql, final Javers javers) {

    Collections.sort(prmdsThrift);
    Collections.sort(prmdsCql);

    final Diff diff = javers.compareCollections(prmdsThrift, prmdsCql, Prmd.class);
    return diff;
  }

}