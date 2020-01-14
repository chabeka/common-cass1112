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
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * (AC75095351) Classe facade pour support Prmd
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class PrmdSupportFacade implements ISupportFacade<Prmd> {

  private final String cfName = Constantes.CF_DROIT_PRMD;

  private final PrmdSupport prmdSupport;

  private final PrmdCqlSupport prmdCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux Prmd
   */
  @Autowired
  public PrmdSupportFacade(final PrmdSupport prmdSupport,
                           final PrmdCqlSupport prmdCqlSupport,
                           final JobClockSupport clockSupport) {
    this.prmdSupport = prmdSupport;
    this.prmdCqlSupport = prmdCqlSupport;
    this.clockSupport = clockSupport;
  }

  @Override
  public final void create(final Prmd prmd) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      prmdSupport.create(prmd, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      prmdCqlSupport.create(prmd);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      prmdCqlSupport.create(prmd);
      prmdSupport.create(prmd, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PrmdSupportFacade/create/Mode API inconnu");
    }
  }

  @Override
  public final Prmd find(final String code) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return prmdSupport.find(code);


    case MODE_API.DATASTAX:
      return prmdCqlSupport.find(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return prmdSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return prmdCqlSupport.find(code);

    default:
      throw new ModeGestionAPIUnkownException("PrmdSupportFacade/find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Prmd> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return prmdSupport.findAll();

    case MODE_API.DATASTAX:
      return prmdCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return prmdSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return prmdCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("PrmdSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      prmdSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      prmdCqlSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      prmdSupport.delete(id, clockSupport.currentCLock());
      prmdCqlSupport.delete(id, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PrmdSupportFacade/delete/Mode API inconnu");
    }
  }


}
