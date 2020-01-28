package fr.urssaf.image.sae.rnd.dao.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.CorrespondancesRndCqlSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Rnd"
 */

@Component
public class CorrespondancesRndSupportFacade {

  @Autowired
  ICorrespondancesDaoCql correspondancesDaoCql;

  private final String cfName = Constantes.CF_CORRESPONDANCES_RND;

  private final CorrespondancesRndSupport correspondancesRndSupport;

  private final CorrespondancesRndCqlSupport correspondancesRndCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée rnd
   */
  @Autowired
  public CorrespondancesRndSupportFacade(final CorrespondancesRndSupport correspondancesRndSupport,
                                         final CorrespondancesRndCqlSupport correspondancesRndCqlSupport,
                                         final JobClockSupport clockSupport) {
    this.correspondancesRndSupport = correspondancesRndSupport;
    this.correspondancesRndCqlSupport = correspondancesRndCqlSupport;
    this.clockSupport = clockSupport;
  }


  public final void ajouterCorrespondance(final Correspondance correspondance) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      correspondancesRndSupport.ajouterCorrespondance(correspondance, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      correspondancesRndCqlSupport.ajouterCorrespondance(correspondance);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      correspondancesRndCqlSupport.ajouterCorrespondance(correspondance);
      correspondancesRndSupport.ajouterCorrespondance(correspondance, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("CorrespondancesRndSupportFacade/Create/Mode API inconnu");

    }
  }

  public final Correspondance getCorrespondance(final String codeTemporaire, final String version) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return correspondancesRndSupport.find(codeTemporaire, version);

    case MODE_API.DATASTAX:
      return correspondancesRndCqlSupport.find(codeTemporaire, version);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return correspondancesRndSupport.find(codeTemporaire, version);

    case MODE_API.DUAL_MODE_READ_CQL:
      return correspondancesRndCqlSupport.find(codeTemporaire, version);

    default:
      throw new ModeGestionAPIUnkownException("CorrespondancesRndSupportFacade/Find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public List<Correspondance> getAllCorrespondances() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return correspondancesRndSupport.getAllCorrespondances();

    case MODE_API.DATASTAX:
      return correspondancesRndCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return correspondancesRndSupport.getAllCorrespondances();

    case MODE_API.DUAL_MODE_READ_CQL:
      return correspondancesRndCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("CorrespondancesRndSupportFacade/findAll/Mode API inconnu");
    }
  }




}
