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
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmfCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * (AC75095351) Classe facade pour support Pagmf
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class PagmfSupportFacade implements IPagmsSupportFacade<Pagmf> {

  private final String cfName = Constantes.CF_DROIT_PAGMF;

  private final PagmfSupport pagmfSupport;

  private final PagmfCqlSupport pagmfCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux Pagmf
   */
  @Autowired
  public PagmfSupportFacade(final PagmfSupport pagmfSupport,
                            final PagmfCqlSupport pagmfCqlSupport,
                            final JobClockSupport clockSupport) {
    this.pagmfSupport = pagmfSupport;
    this.pagmfCqlSupport = pagmfCqlSupport;
    this.clockSupport = clockSupport;
  }

  @Override
  public final void create(final Pagmf pagmf) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmfSupport.create(pagmf, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmfCqlSupport.create(pagmf);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmfCqlSupport.create(pagmf);
      pagmfSupport.create(pagmf, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmfSupportFacade/create/Mode API inconnu");
    }
  }

  @Override
  public final Pagmf find(final String code) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return pagmfSupport.find(code);

    case MODE_API.DATASTAX:
      return pagmfCqlSupport.find(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:

      return pagmfSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:

      return pagmfCqlSupport.find(code);

    default:
      throw new ModeGestionAPIUnkownException("PagmfSupportFacade/find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Pagmf> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return pagmfSupport.findAll();

    case MODE_API.DATASTAX:
      return pagmfCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return pagmfSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return pagmfCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("PagmfSupportFacade/findall/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmfSupport.delete(id, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      pagmfCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmfSupport.delete(id, clockSupport.currentCLock());
      pagmfCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmfSupportFacade/delete/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(final Pagmf pagmf, final Mutator<String> mutator) {

    // INUTILE

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String id, final Mutator<String> mutator) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      pagmfSupport.delete(id, clockSupport.currentCLock(), mutator);
      break;

    case MODE_API.DATASTAX:
      pagmfCqlSupport.delete(id);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      pagmfSupport.delete(id, clockSupport.currentCLock(), mutator);
      pagmfCqlSupport.delete(id);
      break;

    default:
      throw new ModeGestionAPIUnkownException("PagmfSupportFacade/delete(mutator)/Mode API inconnu");
    }

  }
}
