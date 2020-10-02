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
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * (AC75095351) Classe facade pour support Pagma
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class PagmaSupportFacade implements IPagmsSupportFacade<Pagma> {

  private final String cfName = Constantes.CF_DROIT_PAGMA;

  private final PagmaSupport pagmaSupport;

  private final PagmaCqlSupport pagmaCqlSupport;

  private final JobClockSupport clockSupport;

  private final ModeAPIService modeApiService;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux Pagma
   */
  @Autowired
  public PagmaSupportFacade(final PagmaSupport pagmaSupport,
                            final PagmaCqlSupport pagmaCqlSupport,
                            final JobClockSupport clockSupport,
                            final ModeAPIService modeApiService) {
    this.pagmaSupport = pagmaSupport;
    this.pagmaCqlSupport = pagmaCqlSupport;
    this.clockSupport = clockSupport;
    this.modeApiService = modeApiService;
  }

  @Override
  public final void create(final Pagma pagma) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmaSupport.create(pagma, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmaCqlSupport.create(pagma);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmaCqlSupport.create(pagma);
      pagmaSupport.create(pagma, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmaSupportFacade/create/Mode API inconnu");
    }
  }

  @Override
  public final Pagma find(final String code) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      return pagmaSupport.find(code);

    case MODE_API.DATASTAX:
      return pagmaCqlSupport.find(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return pagmaSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return pagmaCqlSupport.find(code);

    default:
      throw new ModeGestionAPIUnkownException("PagmaSupportFacade/fincd/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Pagma> findAll() {
    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      return pagmaSupport.findAll();

    case MODE_API.DATASTAX:
      return pagmaCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return pagmaSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return pagmaCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("PagmaSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmaSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmaCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmaSupport.delete(id, clockSupport.currentCLock());
      pagmaCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmaSupportFacade/delete/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(final Pagma pagma, final Mutator<String> mutator) {
    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmaSupport.create(pagma, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmaCqlSupport.create(pagma);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmaCqlSupport.create(pagma);
      pagmaSupport.create(pagma, clockSupport.currentCLock(), mutator);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmaSupportFacade/create(mutator)/Mode API inconnu");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id, final Mutator<String> mutator) {
    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmaSupport.delete(id, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmaCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmaSupport.delete(id, clockSupport.currentCLock(), mutator);
      pagmaCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmaSupportFacade/delete(mutator)/Mode API inconnu");
    }

  }
}
