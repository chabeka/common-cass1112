/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * (AC75095351) Classe facade pour support ContratService
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class ContratServiceSupportFacade implements IContratServiceSupportFacade<ServiceContract> {

  private final String cfName = Constantes.CF_DROIT_CONTRAT_SERVICE;

  private final ContratServiceSupport contratServiceSupport;

  private final ContratServiceCqlSupport contratServiceCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux ContratService
   */
  @Autowired
  public ContratServiceSupportFacade(final ContratServiceSupport contratServiceSupport,
                                     final ContratServiceCqlSupport contratServiceCqlSupport,
                                     final JobClockSupport clockSupport) {
    this.contratServiceSupport = contratServiceSupport;
    this.contratServiceCqlSupport = contratServiceCqlSupport;
    this.clockSupport = clockSupport;
  }

  @Override
  public final void create(final ServiceContract contratService) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      contratServiceSupport.create(contratService, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      contratServiceCqlSupport.create(contratService);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      contratServiceCqlSupport.create(contratService);
      contratServiceSupport.create(contratService, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("ContratServiceSupportFacade/Create/Mode API inconnu");
    }
  }

  @Override
  public final ServiceContract find(final String code) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return contratServiceSupport.find(code);

    case MODE_API.DATASTAX:
      return contratServiceCqlSupport.find(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return contratServiceSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return contratServiceCqlSupport.find(code);

    default:

      throw new ModeGestionAPIUnkownException("ContratServiceSupportFacade/find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ServiceContract> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return contratServiceSupport.findAll();

    case MODE_API.DATASTAX:
      return contratServiceCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:

      return contratServiceSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:

      return contratServiceCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("ContratServiceSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      contratServiceSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      contratServiceCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      contratServiceSupport.delete(id, clockSupport.currentCLock());
      contratServiceCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("ContratServiceSupportFacade/delete/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ServiceContract> findAll(final int maxKeysToRead) {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return contratServiceSupport.findAll(maxKeysToRead);

    case MODE_API.DATASTAX:
      return contratServiceCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return contratServiceSupport.findAll(maxKeysToRead);

    case MODE_API.DUAL_MODE_READ_CQL:
      return contratServiceCqlSupport.findAll(maxKeysToRead);

    default:
      throw new ModeGestionAPIUnkownException("ContratServiceSupportFacade/findAllMax/Mode API inconnu");
    }
  }

}
