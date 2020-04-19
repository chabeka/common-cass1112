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
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.PagmUtils;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * (AC75095351) Classe facade pour support Pagm
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class PagmSupportFacade {

  private final String cfName = Constantes.CF_DROIT_PAGM;

  private final PagmSupport pagmSupport;

  private final PagmCqlSupport pagmCqlSupport;

  private final JobClockSupport clockSupport;

  private final ModeAPIService modeApiService;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux Pagm
   */
  @Autowired
  public PagmSupportFacade(final PagmSupport pagmSupport,
                           final PagmCqlSupport pagmCqlSupport,
                           final JobClockSupport clockSupport,
                           final ModeAPIService modeApiService) {
    this.pagmSupport = pagmSupport;
    this.pagmCqlSupport = pagmCqlSupport;
    this.clockSupport = clockSupport;
    this.modeApiService = modeApiService;
  }


  public final void create(final String idClient, final Pagm pagm) {

    final PagmCql pagmCql = PagmUtils.convertPagmToPagmCql(idClient, pagm);

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmSupport.create(idClient, pagm, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmCqlSupport.create(pagmCql);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmCqlSupport.create(pagmCql);
      pagmSupport.create(idClient, pagm, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmSupportFacade/create/Mode API inconnu");
    }
  }


  public final List<Pagm> find(final String code) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      return pagmSupport.find(code);

    case MODE_API.DATASTAX:

      return pagmCqlSupport.findByIdClient(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return pagmSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return pagmCqlSupport.findByIdClient(code);

    default:
      throw new ModeGestionAPIUnkownException("PagmSupportFacade/find/Mode API inconnu");
    }
  }



  /**
   * {@inheritDoc}
   */

  public void delete(final String idClient, final String codePagm) {

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmSupport.delete(idClient, codePagm, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmCqlSupport.delete(idClient, clockSupport.currentCLock());
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmSupport.delete(idClient,codePagm, clockSupport.currentCLock());
      pagmCqlSupport.delete(idClient, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmSupportFacade/delete/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public void create(final String idClient, final Pagm pagm, final Mutator<String> mutator) {
    final PagmCql pagmCql = PagmUtils.convertPagmToPagmCql(idClient, pagm);

    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmSupport.create(idClient, pagm, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmCqlSupport.create(pagmCql);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmCqlSupport.create(pagmCql);
      pagmSupport.create(idClient, pagm, clockSupport.currentCLock(), mutator);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmSupportFacade/create(mutator)/Mode API inconnu");
    }

  }

  /**
   * {@inheritDoc}
   */

  public void delete(final String idClient, final String codePagm, final Mutator<String> mutator) {
    switch (modeApiService.getModeAPI(cfName)) {

    case MODE_API.HECTOR:
      pagmSupport.delete(idClient, codePagm, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmCqlSupport.delete(idClient, clockSupport.currentCLock());
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmSupport.delete(idClient, codePagm, clockSupport.currentCLock(), mutator);
      pagmCqlSupport.delete(idClient, clockSupport.currentCLock());
      break;

    default:
      pagmSupport.delete(idClient, codePagm, clockSupport.currentCLock(), mutator);
    }
  }

}
