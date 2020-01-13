/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.rnd;

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
import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;

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
  public Diff migrationFromThriftToCql() {

    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- start ");

    final List<Correspondance> correspondancesThrift = correspondancesSupport.getAllCorrespondances();

    if (!correspondancesThrift.isEmpty()) {
      correspondanceDaoCql.saveAll(correspondancesThrift);
    }
    final List<Correspondance> correspondancesCql = new ArrayList<>();
    final Iterator<Correspondance> correspondanceRndsIterator = correspondanceDaoCql.findAllWithMapper();
    correspondanceRndsIterator.forEachRemaining(correspondancesCql::add);
    final Diff diff = comparecorrespondancesRnds(correspondancesThrift, correspondancesCql);
    LOGGER.info(" MigrationCorrespondancesRnd - migrationFromThriftToCql- end ");
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift() {

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
  private Diff comparecorrespondancesRnds(final List<Correspondance> correspondancesRndsThrift, final List<Correspondance> correspondancesRndsCql) {

    Collections.sort(correspondancesRndsThrift);
    Collections.sort(correspondancesRndsCql);
    final Javers javers = JaversBuilder
                                       .javers()
                                       .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
                                       .build();
    final Diff diff = javers.compareCollections(correspondancesRndsThrift, correspondancesRndsThrift, Correspondance.class);
    return diff;
  }


}
