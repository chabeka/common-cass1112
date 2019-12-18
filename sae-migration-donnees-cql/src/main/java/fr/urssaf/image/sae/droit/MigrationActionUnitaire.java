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
import fr.urssaf.image.sae.droit.dao.cql.IActionUnitaireDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Description du type
 */
@Component
public class MigrationActionUnitaire implements IMigration {

  @Autowired
  private IActionUnitaireDaoCql actionUnitaireDaoCql;

  @Autowired
  private ActionUnitaireSupport actionUnitaireSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationActionUnitaire.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromThriftToCql- start ");
    final List<ActionUnitaire> actionsUnitairesThrift = actionUnitaireSupport.findAll();

    if (!actionsUnitairesThrift.isEmpty()) {
      actionUnitaireDaoCql.saveAll(actionsUnitairesThrift);
    }
    final List<ActionUnitaire> actionsUnitairesCql = new ArrayList<>();
    final Iterator<ActionUnitaire> actionsUnitairesIterator = actionUnitaireDaoCql.findAllWithMapper();
    actionsUnitairesIterator.forEachRemaining(actionsUnitairesCql::add);
    compareActionsUnitaires(actionsUnitairesThrift, actionsUnitairesCql);
    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromCqlTothrift- start ");

    final Iterator<ActionUnitaire> actionsUnitairesIterator = actionUnitaireDaoCql.findAllWithMapper();
    final List<ActionUnitaire> actionsUnitairesCql = new ArrayList<>();
    while (actionsUnitairesIterator.hasNext()) {
      final ActionUnitaire actionUnitaire = actionsUnitairesIterator.next();

      actionsUnitairesCql.add(actionUnitaire);
      actionUnitaireSupport.create(actionUnitaire, new Date().getTime());
    }

    final List<ActionUnitaire> actionsUnitairesThrift = actionUnitaireSupport.findAll();
    compareActionsUnitaires(actionsUnitairesThrift, actionsUnitairesCql);
    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromCqlTothrift- end ");
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param actionsUnitairesThrift
   * @param actionsUnitairesCql
   */
  private void compareActionsUnitaires(final List<ActionUnitaire> actionsUnitairesThrift, final List<ActionUnitaire> actionsUnitairesCql) {

    LOGGER.info("MIGRATION_ACTION_UNITAIRE -- SizeThriftDroitActionUnitaire=" + actionsUnitairesThrift.size());
    LOGGER.info("MIGRATION_ACTION_UNITAIRE -- SizeCqlDroitActionUnitaire=" + actionsUnitairesCql.size());
    if (CompareUtils.compareListsGeneric(actionsUnitairesThrift, actionsUnitairesCql)) {
      LOGGER.info("MIGRATION_ACTION_UNITAIRE -- Les listes ActionUnitaire sont identiques");
    } else {
      LOGGER.warn("MIGRATION_ACTION_UNITAIRE -- ATTENTION: Les listes ActionUnitaire sont différentes ");
    }
  }
}
