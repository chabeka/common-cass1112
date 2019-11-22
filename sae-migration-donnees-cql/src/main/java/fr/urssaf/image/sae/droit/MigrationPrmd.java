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
import fr.urssaf.image.sae.droit.dao.cql.IPrmdDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Prmd Thrift<-> Cql
 */
@Component
public class MigrationPrmd implements IMigration {

  @Autowired
  private IPrmdDaoCql prmdDaoCql;

  @Autowired
  private PrmdSupport prmdSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPrmd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_PRMD - migrationFromThriftToCql- start ");

    final List<Prmd> prmdsThrift = prmdSupport.findAll();

    if (!prmdsThrift.isEmpty()) {
      prmdDaoCql.saveAll(prmdsThrift);
    }
    final List<Prmd> prmdsCql = new ArrayList<>();
    final Iterator<Prmd> prmdsIterator = prmdDaoCql.findAllWithMapper();
    prmdsIterator.forEachRemaining(prmdsCql::add);
    comparePrmds(prmdsThrift, prmdsCql);
    LOGGER.info(" MIGRATION_PRMD - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_PRMD - migrationFromCqlTothrift- start ");

    final Iterator<Prmd> prmdsCqlIterator = prmdDaoCql.findAllWithMapper();
    final List<Prmd> prmdsCql = new ArrayList<>();
    while (prmdsCqlIterator.hasNext()) {
      final Prmd prmd = prmdsCqlIterator.next();
      prmdsCql.add(prmd);
      prmdSupport.create(prmd, new Date().getTime());
    }
    final List<Prmd> prmdsThrift = prmdSupport.findAll();
    comparePrmds(prmdsThrift, prmdsCql);
    LOGGER.info(" MIGRATION_PRMD - migrationFromCqlTothrift- end ");
  }

  /**
   * Comparaison des liste en taille et en contenu
   * 
   * @param prmdsThrift
   * @param prmdsCql
   */
  private void comparePrmds(final List<Prmd> prmdsThrift, final List<Prmd> prmdsCql) {

    LOGGER.info("MIGRATION_PRMD -- SizeThriftDroitprmd=" + prmdsThrift.size());
    LOGGER.info("MIGRATION_PRMD -- SizeCqlDroitprmd=" + prmdsCql.size());
    if (CompareUtils.compareListsGeneric(prmdsThrift, prmdsCql)) {
      LOGGER.info("MIGRATION_PRMD -- Les listes prmd sont identiques");
    } else {
      LOGGER.warn("MIGRATION_PRMD -- ATTENTION: Les listes prmd sont différentes ");
    }
  }

}
