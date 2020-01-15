/**
 *   (AC75095351) Description du fichier
 */
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

import fr.urssaf.image.sae.droit.dao.cql.IActionUnitaireDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;

/**
 * (AC75095351) Classe de migration de ActionUnitaire
 */
@Component
public class MigrationActionUnitaire {

  @Autowired
  private IActionUnitaireDaoCql actionUnitaireDaoCql;

  @Autowired
  private ActionUnitaireSupport actionUnitaireSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationActionUnitaire.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */

  public Diff migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromThriftToCql- start ");
    final List<ActionUnitaire> actionsUnitairesThrift = actionUnitaireSupport.findAll();

    if (!actionsUnitairesThrift.isEmpty()) {
      actionUnitaireDaoCql.saveAll(actionsUnitairesThrift);
    }
    final List<ActionUnitaire> actionsUnitairesCql = new ArrayList<>();
    final Iterator<ActionUnitaire> actionsUnitairesIterator = actionUnitaireDaoCql.findAllWithMapper();
    actionsUnitairesIterator.forEachRemaining(actionsUnitairesCql::add);
    final Diff compare = compareActionsUnitaires(actionsUnitairesThrift, actionsUnitairesCql);
    // logCompare(compare, actionsUnitairesThrift, actionsUnitairesCql);
    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromThriftToCql- end ");
    return compare;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */

  public Diff migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromCqlTothrift- start ");

    final Iterator<ActionUnitaire> actionsUnitairesIterator = actionUnitaireDaoCql.findAllWithMapper();
    final List<ActionUnitaire> actionsUnitairesCql = new ArrayList<>();
    while (actionsUnitairesIterator.hasNext()) {
      final ActionUnitaire actionUnitaire = actionsUnitairesIterator.next();

      actionsUnitairesCql.add(actionUnitaire);
      actionUnitaireSupport.create(actionUnitaire, new Date().getTime());
    }

    final List<ActionUnitaire> actionsUnitairesThrift = actionUnitaireSupport.findAll();
    final Diff compare = compareActionsUnitaires(actionsUnitairesThrift, actionsUnitairesCql);
    // logCompare(compare, actionsUnitairesThrift, actionsUnitairesCql);
    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromCqlTothrift- end ");
    return compare;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param actionsUnitairesThrift
   * @param actionsUnitairesCql
   */
  public Diff compareActionsUnitaires(final List<ActionUnitaire> actionsUnitairesThrift, final List<ActionUnitaire> actionsUnitairesCql) {

    Collections.sort(actionsUnitairesThrift);
    Collections.sort(actionsUnitairesCql);
    final Javers javers = JaversBuilder
                                       .javers()
                                       .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
                                       .build();
    final Diff diff = javers.compareCollections(actionsUnitairesThrift, actionsUnitairesCql, ActionUnitaire.class);
    return diff;
  }

}
