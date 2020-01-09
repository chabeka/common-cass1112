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

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Rnd Thrift<-> Cql
 */
@Component
public class MigrationCorrespondancesRnd implements IMigrationR {

  @Autowired
  private ICorrespondancesDaoCql correspondanceDaoCql;

  @Autowired
  private CorrespondancesRndSupport correspondancesSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationCorrespondancesRnd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- start ");

    final List<Correspondance> correspondancesThrift = correspondancesSupport.getAllCorrespondances();

    if (!correspondancesThrift.isEmpty()) {
      correspondanceDaoCql.saveAll(correspondancesThrift);
    }
    final List<Correspondance> correspondancesCql = new ArrayList<>();
    final Iterator<Correspondance> correspondanceRndsIterator = correspondanceDaoCql.findAllWithMapper();
    correspondanceRndsIterator.forEachRemaining(correspondancesCql::add);

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- end ");
    return comparecorrespondancesRnds(correspondancesThrift, correspondancesCql);
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromCqlTothrift- start ");


    final Iterator<Correspondance> correspondancesIterator = correspondanceDaoCql.findAllWithMapper();
    final List<Correspondance> correspondanceRndsCql = new ArrayList<>();
    while (correspondancesIterator.hasNext()) {
      final Correspondance correspondance = correspondancesIterator.next();
      correspondanceRndsCql.add(correspondance);
      correspondancesSupport.ajouterCorrespondance(correspondance, new Date().getTime());
    }

    final List<Correspondance> correspondanceRndsThrift = correspondancesSupport.getAllCorrespondances();

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromCqlTothrift- end ");
    return   comparecorrespondancesRnds(correspondanceRndsThrift, correspondanceRndsCql);
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param correspondancesRndsThrift
   * @param correspondancesRndsCql
   */
  private boolean comparecorrespondancesRnds(final List<Correspondance> correspondancesRndsThrift, final List<Correspondance> correspondancesRndsCql) {

    final boolean result = CompareUtils.compareListsGeneric(correspondancesRndsThrift, correspondancesRndsCql);
    if (result) {
      LOGGER.info("MIGRATION_CORRESPONDANCES_RND -- Les listes correspondancesRnd sont identiques");
    } else {
      LOGGER.info("MIGRATION_CORRESPONDANCES_RND -- NbThrift=" + correspondancesRndsThrift.size());
      LOGGER.info("MIGRATION_CORRESPONDANCES_RND -- NbCql=" + correspondancesRndsCql.size());
      LOGGER.warn("MIGRATION_CORRESPONDANCES_RND -- ATTENTION: Les listes correspondancesRnd sont différentes ");
    }
    return result;
  }


}
