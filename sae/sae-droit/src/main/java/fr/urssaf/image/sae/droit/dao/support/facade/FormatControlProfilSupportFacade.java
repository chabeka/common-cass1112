/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * * (AC75095351) Classe facade pour support FormatControlProfil
 * Un traitement spécifique est effectué en fonction du MODE_API
 */
@Component
public class FormatControlProfilSupportFacade implements IFormatControlProfilFacade<FormatControlProfil> {

  private final String cfName = Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL;

  private final FormatControlProfilSupport formatControlProfilSupport;

  private final FormatControlProfilCqlSupport formatControlProfilCqlSupport;

  private final JobClockSupport clockSupport;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(FormatControlProfilSupportFacade.class);

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée aux formatControlProfil
   */
  @Autowired
  public FormatControlProfilSupportFacade(final FormatControlProfilSupport formatControlProfilSupport,
                                          final FormatControlProfilCqlSupport formatControlProfilCqlSupport,
                                          final JobClockSupport clockSupport) {

    this.formatControlProfilSupport = formatControlProfilSupport;
    this.formatControlProfilCqlSupport = formatControlProfilCqlSupport;
    this.clockSupport = clockSupport;
  }

  @Override
  public final void create(final FormatControlProfil formatControlProfil) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      formatControlProfilSupport.create(formatControlProfil, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      formatControlProfilCqlSupport.create(formatControlProfil);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      formatControlProfilCqlSupport.create(formatControlProfil);
      formatControlProfilSupport.create(formatControlProfil, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("FormatControlProfilSupportFacade/create/Mode API inconnu");
    }
  }

  @Override
  public final FormatControlProfil find(final String code) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return formatControlProfilSupport.find(code);


    case MODE_API.DATASTAX:
      return formatControlProfilCqlSupport.find(code);


    case MODE_API.DUAL_MODE_READ_THRIFT:

      return formatControlProfilSupport.find(code);

    case MODE_API.DUAL_MODE_READ_CQL:

      return formatControlProfilCqlSupport.find(code);



    default:
      throw new ModeGestionAPIUnkownException("FormatControlProfilSupportFacade/find/Mode API inconnu");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<FormatControlProfil> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return formatControlProfilSupport.findAll();

    case MODE_API.DATASTAX:
      return formatControlProfilCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:

      return formatControlProfilSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:

      return formatControlProfilCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("FormatControlProfilSupportFacade/findAll/Mode API inconnu");
    }

  }

  /**
   * {@inheritDoc}
   * 
   * @throws FormatControlProfilNotFoundException
   */
  @Override
  public void delete(final String id) throws FormatControlProfilNotFoundException {


      switch (ModeGestionAPI.getModeApiCf(cfName)) {

      case MODE_API.HECTOR:
        formatControlProfilSupport.delete(id, clockSupport.currentCLock());
        break;

      case MODE_API.DATASTAX:
      formatControlProfilCqlSupport.delete(id, clockSupport.currentCLock());
        break;

      case MODE_API.DUAL_MODE_READ_THRIFT:
      case MODE_API.DUAL_MODE_READ_CQL:
        formatControlProfilSupport.delete(id, clockSupport.currentCLock());
      formatControlProfilCqlSupport.delete(id, clockSupport.currentCLock());
        break;

      default:
        throw new ModeGestionAPIUnkownException("FormatControlProfilSupportFacade/delete/Mode API inconnu");
      }




  }
}


