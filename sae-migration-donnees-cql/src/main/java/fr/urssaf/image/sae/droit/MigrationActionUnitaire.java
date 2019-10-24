/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.droit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.cql.IActionUnitaireDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;

/**
 * (AC75095351) Description du type
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
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationActionUnitaire - migrationFromThriftToCql- start ");

    final List<ActionUnitaire> actionUnitaires = actionUnitaireSupport.findAll();

    if (!actionUnitaires.isEmpty()) {
      actionUnitaireDaoCql.saveAll(actionUnitaires);
    }

    LOGGER.info(" MigrationActionUnitaire - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationActionUnitaire - migrationFromCqlTothrift- start ");

    final Iterator<ActionUnitaire> actionsUnitaires = actionUnitaireDaoCql.findAllWithMapper();
    while (actionsUnitaires.hasNext()) {
      final ActionUnitaire actionUnitaire = actionsUnitaires.next();
      actionUnitaireSupport.create(actionUnitaire, new Date().getTime());
    }

    LOGGER.info(" MigrationActionUnitaire - migrationFromCqlTothrift- end ");
  }
}
