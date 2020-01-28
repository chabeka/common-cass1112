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
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * (AC75095351) Classe facade pour support Pagmp
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class PagmpSupportFacade implements IPagmsSupportFacade<Pagmp> {

  private final String cfName = Constantes.CF_DROIT_PAGMP;

  private final PagmpSupport pagmpSupport;

  private final PagmpCqlSupport pagmpCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux Pagmp
   */
  @Autowired
  public PagmpSupportFacade(final PagmpSupport pagmpSupport,
                            final PagmpCqlSupport pagmpCqlSupport,
                            final JobClockSupport clockSupport) {
    this.pagmpSupport = pagmpSupport;
    this.pagmpCqlSupport = pagmpCqlSupport;
    this.clockSupport = clockSupport;
  }

  @Override
  public final void create(final Pagmp pagmp) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmpSupport.create(pagmp, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmpCqlSupport.create(pagmp);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmpCqlSupport.create(pagmp);
      pagmpSupport.create(pagmp, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmpSupportFacade/create/Mode API inconnu");
    }
  }

  @Override
  public final Pagmp find(final String code) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return pagmpSupport.find(code);


    case MODE_API.DATASTAX:
      return pagmpCqlSupport.find(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return pagmpSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return pagmpCqlSupport.find(code);

    default:
      throw new ModeGestionAPIUnkownException("PagmpSupportFacade/find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Pagmp> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return pagmpSupport.findAll();

    case MODE_API.DATASTAX:
      return pagmpCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return pagmpSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return pagmpCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("PagmpSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmpSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmpCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmpSupport.delete(id, clockSupport.currentCLock());
      pagmpCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmpSupportFacade/delete/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(final Pagmp pagmp, final Mutator<String> mutator) {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmpSupport.create(pagmp, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmpCqlSupport.create(pagmp);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmpCqlSupport.create(pagmp);
      pagmpSupport.create(pagmp, clockSupport.currentCLock(), mutator);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmpSupportFacade/create(mutator)/Mode API inconnu");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id, final Mutator<String> mutator) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmpSupport.delete(id, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmpCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmpSupport.delete(id, clockSupport.currentCLock(), mutator);
      pagmpCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmpSupportFacade/delete(mutator)/Mode API inconnu");
    }

  }
}
