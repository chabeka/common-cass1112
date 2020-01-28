package fr.urssaf.image.sae.format.referentiel.dao.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.dao.support.cql.ReferentielFormatCqlSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * AC75095351
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "ReferentielFormat"
 */

@Component
public class ReferentielFormatSupportFacade {


  private final String cfName = Constantes.CF_REFERENTIEL_FORMAT;

  private final ReferentielFormatSupport referentielFormatSupport;

  private final ReferentielFormatCqlSupport referentielFormatCqlSupport;

  private final JobClockSupport clockSupport;


  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée formatFichier
   */
  @Autowired
  public ReferentielFormatSupportFacade(final ReferentielFormatSupport referentielFormatSupport,
                                        final ReferentielFormatCqlSupport referentielFormatCqlSupport,
                                        final JobClockSupport clockSupport) {
    this.referentielFormatSupport = referentielFormatSupport;
    this.referentielFormatCqlSupport = referentielFormatCqlSupport;
    this.clockSupport = clockSupport;

  }


  public final void create(final FormatFichier formatFichier) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      referentielFormatSupport.create(formatFichier, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      referentielFormatCqlSupport.create(formatFichier);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      referentielFormatCqlSupport.create(formatFichier);
      referentielFormatSupport.create(formatFichier, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("FormatFichierSupportFacade/Create/Mode API inconnu");

    }
  }

  public final FormatFichier find(final String idFormat) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return referentielFormatSupport.find(idFormat);

    case MODE_API.DATASTAX:
      return referentielFormatCqlSupport.find(idFormat);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return referentielFormatSupport.find(idFormat);

    case MODE_API.DUAL_MODE_READ_CQL:
      return referentielFormatCqlSupport.find(idFormat);

    default:
      throw new ModeGestionAPIUnkownException("FormatFichierSupportFacade/Find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public List<FormatFichier> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return referentielFormatSupport.findAll();

    case MODE_API.DATASTAX:
      return referentielFormatCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return referentielFormatSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return referentielFormatCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("FormatFichierSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws UnknownFormatException
   */

  public void delete(final String idFormat) throws UnknownFormatException {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      referentielFormatSupport.delete(idFormat, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      referentielFormatCqlSupport.delete(idFormat);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      referentielFormatSupport.delete(idFormat,  clockSupport.currentCLock());
      referentielFormatCqlSupport.delete(idFormat);
      break;

    default:
      throw new ModeGestionAPIUnkownException("FormatFichierSupportFacade/delete/Mode API inconnu");
    }
  }


}
