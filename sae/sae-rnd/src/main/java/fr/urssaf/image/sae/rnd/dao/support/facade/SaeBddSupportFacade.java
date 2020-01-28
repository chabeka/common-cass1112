package fr.urssaf.image.sae.rnd.dao.support.facade;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.SaeBddCqlSupport;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "SaeBdd"
 */

@Component
public class SaeBddSupportFacade {

  private final String cfName = Constantes.CF_RND;

  private final SaeBddSupport saeBddSupport;

  private final SaeBddCqlSupport saeBddCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée saeBdd
   */
  @Autowired
  public SaeBddSupportFacade(final SaeBddSupport saeBddSupport,
                             final SaeBddCqlSupport saeBddCqlSupport,
                             final JobClockSupport clockSupport) {
    this.saeBddSupport = saeBddSupport;
    this.saeBddCqlSupport = saeBddCqlSupport;
    this.clockSupport = clockSupport;
  }


  public final VersionRnd getVersionRnd() throws SaeBddRuntimeException {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return saeBddSupport.getVersionRnd();

    case MODE_API.DATASTAX:
      return saeBddCqlSupport.getVersionRnd();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return saeBddSupport.getVersionRnd();

    case MODE_API.DUAL_MODE_READ_CQL:
      return saeBddCqlSupport.getVersionRnd();

    default:
      throw new ModeGestionAPIUnkownException("SaeBddSupportFacade/getVersionRnd/Mode API inconnu");
    }
  }

  public final void updateVersionRnd(final VersionRnd versionRnd) throws SaeBddRuntimeException {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      saeBddSupport.updateVersionRnd(versionRnd);
      break;

    case MODE_API.DATASTAX:
      saeBddCqlSupport.updateVersionRnd(versionRnd);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      saeBddCqlSupport.updateVersionRnd(versionRnd);
      saeBddSupport.updateVersionRnd(versionRnd);
      break;

    default:
      throw new ModeGestionAPIUnkownException("SaeBddSupportFacade/updateVersionRnd/Mode API inconnu");

    }
  }

  public final void updateRnd(final List<TypeDocument> listeTypeDocs) throws SaeBddRuntimeException {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      saeBddSupport.updateRnd(listeTypeDocs);
      break;

    case MODE_API.DATASTAX:
      saeBddCqlSupport.updateRnd(listeTypeDocs);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      saeBddCqlSupport.updateRnd(listeTypeDocs);
      saeBddSupport.updateRnd(listeTypeDocs);
      break;

    default:
      throw new ModeGestionAPIUnkownException("SaeBddSupportFacade/updateRnd/Mode API inconnu");

    }
  }

  public final void updateCorrespondances(
                                          final Map<String, String> listeCorrespondances, final String version)
                                              throws SaeBddRuntimeException {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      saeBddSupport.updateCorrespondances(listeCorrespondances, version);
      break;

    case MODE_API.DATASTAX:
      saeBddCqlSupport.updateCorrespondances(listeCorrespondances, version);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      saeBddCqlSupport.updateCorrespondances(listeCorrespondances, version);
      saeBddSupport.updateCorrespondances(listeCorrespondances, version);
      break;

    default:
      throw new ModeGestionAPIUnkownException("SaeBddSupportFacade/updateCorrespondances/Mode API inconnu");

    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws SaeBddRuntimeException
   */

  public List<Correspondance> getAllCorrespondances() throws SaeBddRuntimeException {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return saeBddSupport.getAllCorrespondances();

    case MODE_API.DATASTAX:
      return saeBddCqlSupport.getAllCorrespondances();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return saeBddSupport.getAllCorrespondances();

    case MODE_API.DUAL_MODE_READ_CQL:
      return saeBddCqlSupport.getAllCorrespondances();

    default:
      throw new ModeGestionAPIUnkownException("SaeBddSupportFacade/getAllCorrespondances/Mode API inconnu");
    }
  }

  public final void startMajCorrespondance(final Correspondance correspondance) throws SaeBddRuntimeException {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      saeBddSupport.startMajCorrespondance(correspondance);
      break;

    case MODE_API.DATASTAX:
      saeBddCqlSupport.startMajCorrespondance(correspondance);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      saeBddCqlSupport.startMajCorrespondance(correspondance);
      saeBddSupport.startMajCorrespondance(correspondance);
      break;

    default:
      throw new ModeGestionAPIUnkownException("SaeBddSupportFacade/startMajCorrespondance/Mode API inconnu");

    }
  }


}
