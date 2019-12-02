/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.rnd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Rnd Thrift<-> Cql
 */
@Component
public class MigrationCorrespondancesRnd implements IMigration {

  @Autowired
  private ICorrespondancesDaoCql correspondanceDaoCql;

  @Autowired
  private CorrespondancesRndSupport correspondancesSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationCorrespondancesRnd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- start ");

    final List<Correspondance> correspondancesThrift = correspondancesSupport.getAllCorrespondances();

    if (!correspondancesThrift.isEmpty()) {
      correspondanceDaoCql.saveAll(correspondancesThrift);
    }
    final List<Correspondance> correspondancesCql = new ArrayList<>();
    final Iterator<Correspondance> correspondanceRndsIterator = correspondanceDaoCql.findAllWithMapper();
    correspondanceRndsIterator.forEachRemaining(correspondancesCql::add);
    comparecorrespondancesRnds(correspondancesThrift, correspondancesCql);
    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromCqlTothrift- start ");


    final Iterator<Correspondance> correspondancesIterator = correspondanceDaoCql.findAllWithMapper();
    final List<Correspondance> correspondanceRndsCql = new ArrayList<>();
    while (correspondancesIterator.hasNext()) {
      final Correspondance correspondance = correspondancesIterator.next();
      correspondanceRndsCql.add(correspondance);
      correspondancesSupport.ajouterCorrespondance(correspondance, new Date().getTime());
    }

    final List<Correspondance> correspondanceRndsThrift = correspondancesSupport.getAllCorrespondances();
    comparecorrespondancesRnds(correspondanceRndsThrift, correspondanceRndsCql);
    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromCqlTothrift- end ");
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param correspondancesRndsThrift
   * @param correspondancesRndsCql
   */
  private void comparecorrespondancesRnds(final List<Correspondance> correspondancesRndsThrift, final List<Correspondance> correspondancesRndsCql) {

    LOGGER.info("MIGRATION_CORRESPONDANCES_RND -- SizeThriftcorrespondancesRnd=" + correspondancesRndsThrift.size());
    LOGGER.info("MIGRATION_CORRESPONDANCES_RND -- SizeCqlcorrespondancesRnd=" + correspondancesRndsCql.size());
    if (CompareUtils.compareListsGeneric(correspondancesRndsThrift, correspondancesRndsCql)) {
      LOGGER.info("MIGRATION_CORRESPONDANCES_RND -- Les listes correspondancesRnd sont identiques");
    } else {
      LOGGER.warn("MIGRATION_CORRESPONDANCES_RND -- ATTENTION: Les listes correspondancesRnd sont différentes ");
    }
  }

}
