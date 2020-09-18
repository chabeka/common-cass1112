/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeAPIService;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * (AC75095351) Classe facade pour support ActionUnitaire
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class ActionUnitaireSupportFacade implements ISupportFacade<ActionUnitaire> {

  private final String cfName = Constantes.CF_DROIT_ACTION_UNITAIRE;

  private final ActionUnitaireSupport actionUnitaireSupport;

  private final ActionUnitaireCqlSupport actionUnitaireCqlSupport;

  private final JobClockSupport clockSupport;

  private final ModeAPIService modeApiService;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux Actions unitaires
   */
  @Autowired
  public ActionUnitaireSupportFacade(final ActionUnitaireSupport actionUnitaireSupport,
                                     final ActionUnitaireCqlSupport actionUnitaireCqlSupport,
                                     final JobClockSupport clockSupport, final ModeAPIService modeApiService) {
    this.actionUnitaireSupport = actionUnitaireSupport;
    this.actionUnitaireCqlSupport = actionUnitaireCqlSupport;
    this.clockSupport = clockSupport;
    this.modeApiService=modeApiService;
  }

  @Override
  public final void create(final ActionUnitaire actionUnitaire) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      actionUnitaireSupport.create(actionUnitaire, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      actionUnitaireCqlSupport.create(actionUnitaire);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      actionUnitaireCqlSupport.create(actionUnitaire);
      actionUnitaireSupport.create(actionUnitaire, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("ActionUnitaireSupportFacade/Create/Mode API inconnu");

    }
  }

  @Override
  public final ActionUnitaire find(final String code) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      return actionUnitaireSupport.find(code);

    case MODE_API.DATASTAX:
      return actionUnitaireCqlSupport.find(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return actionUnitaireSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return actionUnitaireCqlSupport.find(code);

    default:
      throw new ModeGestionAPIUnkownException("ActionUnitaireSupportFacade/Find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ActionUnitaire> findAll() {
    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      return actionUnitaireSupport.findAll();

    case MODE_API.DATASTAX:
      return actionUnitaireCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return actionUnitaireSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return actionUnitaireCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("ActionUnitaireSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      actionUnitaireSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      actionUnitaireCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      actionUnitaireSupport.delete(id, clockSupport.currentCLock());
      actionUnitaireCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("ActionUnitaireSupportFacade/delete/Mode API inconnu");
    }
  }


}
