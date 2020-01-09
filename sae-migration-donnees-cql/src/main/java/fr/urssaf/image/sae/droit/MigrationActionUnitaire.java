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

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IActionUnitaireDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.model.ActionUnitaireM;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration de ActionUnitaire
 */
@Component
public class MigrationActionUnitaire implements IMigrationR {

  @Autowired
  private IActionUnitaireDaoCql actionUnitaireDaoCql;

  @Autowired
  private ActionUnitaireSupport actionUnitaireSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationActionUnitaire.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromThriftToCql- start ");
    final List<ActionUnitaire> actionsUnitairesThrift = actionUnitaireSupport.findAll();

    if (!actionsUnitairesThrift.isEmpty()) {
      actionUnitaireDaoCql.saveAll(actionsUnitairesThrift);
    }
    final List<ActionUnitaire> actionsUnitairesCql = new ArrayList<>();
    final Iterator<ActionUnitaire> actionsUnitairesIterator = actionUnitaireDaoCql.findAllWithMapper();
    actionsUnitairesIterator.forEachRemaining(actionsUnitairesCql::add);
    final boolean compare = compareActionsUnitaires(actionsUnitairesThrift, actionsUnitairesCql);
    logCompare(compare, actionsUnitairesThrift, actionsUnitairesCql);
    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromThriftToCql- end ");
    return compare;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromCqlTothrift- start ");

    final Iterator<ActionUnitaire> actionsUnitairesIterator = actionUnitaireDaoCql.findAllWithMapper();
    final List<ActionUnitaire> actionsUnitairesCql = new ArrayList<>();
    while (actionsUnitairesIterator.hasNext()) {
      final ActionUnitaire actionUnitaire = actionsUnitairesIterator.next();

      actionsUnitairesCql.add(actionUnitaire);
      actionUnitaireSupport.create(actionUnitaire, new Date().getTime());
    }

    final List<ActionUnitaire> actionsUnitairesThrift = actionUnitaireSupport.findAll();
    final boolean compare = compareActionsUnitaires(actionsUnitairesThrift, actionsUnitairesCql);
    logCompare(compare, actionsUnitairesThrift, actionsUnitairesCql);
    LOGGER.info(" MIGRATION_ACTION_UNITAIRE - migrationFromCqlTothrift- end ");
    return compare;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param actionsUnitairesThrift
   * @param actionsUnitairesCql
   */
  public boolean compareActionsUnitaires(final List<ActionUnitaire> actionsUnitairesThrift, final List<ActionUnitaire> actionsUnitairesCql) {

    final List<ActionUnitaireM> actionsUnitairesThriftM = convertList(actionsUnitairesThrift);
    final List<ActionUnitaireM> actionsUnitairesCqlM = convertList(actionsUnitairesCql);
    actionsUnitairesCqlM.get(0).setDescription("M");// TEST comparaison

    return CompareUtils.compareListsGeneric(actionsUnitairesThriftM, actionsUnitairesCqlM);
  }

  public void logCompare(final boolean compare, final List<ActionUnitaire> actionsUnitairesThrift, final List<ActionUnitaire> actionsUnitairesCql) {
    if (compare) {
      LOGGER.info("MIGRATION_ACTION_UNITAIRE -- Les listes ActionUnitaires sont identiques");
    } else {
      LOGGER.info("MIGRATION_ACTION_UNITAIRE -- NbThrift=" + actionsUnitairesThrift.size());
      LOGGER.info("MIGRATION_ACTION_UNITAIRE -- NbCql=" + actionsUnitairesCql.size());
      LOGGER.warn("MIGRATION_ACTION_UNITAIRE -- ATTENTION: Les listes ActionUnitaire sont différentes ");
    }
  }

  private List<ActionUnitaireM> convertList(final List<ActionUnitaire> actionsUnitaires) {

    final List<ActionUnitaireM> actionsUnitairesM = new ArrayList<>();
    for (final ActionUnitaire actionUnitaire : actionsUnitaires) {
      final ActionUnitaireM actionUnitaireM = new ActionUnitaireM();
      actionUnitaireM.setCode(actionUnitaire.getCode());
      actionUnitaireM.setDescription(actionUnitaire.getDescription());
      actionsUnitairesM.add(actionUnitaireM);
    }
    return actionsUnitairesM;
  }

}
