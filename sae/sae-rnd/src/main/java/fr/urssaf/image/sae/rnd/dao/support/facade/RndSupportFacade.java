package fr.urssaf.image.sae.rnd.dao.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.rnd.dao.cql.IRndDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.RndCqlSupport;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Rnd"
 */

@Component
public class RndSupportFacade {

  @Autowired
  IRndDaoCql rndDaoCql;

  private final String cfName = Constantes.CF_RND;

  private final RndSupport rndSupport;

  private final RndCqlSupport rndCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée rnd
   */
  @Autowired
  public RndSupportFacade(final RndSupport rndSupport,
                          final RndCqlSupport rndCqlSupport,
                          final JobClockSupport clockSupport) {
    this.rndSupport = rndSupport;
    this.rndCqlSupport = rndCqlSupport;
    this.clockSupport = clockSupport;
  }


  public final void ajouterRnd(final TypeDocument typeDocument) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      rndSupport.ajouterRnd(typeDocument, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      rndCqlSupport.ajouterRnd(typeDocument);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      rndCqlSupport.ajouterRnd(typeDocument);
      rndSupport.ajouterRnd(typeDocument, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("RndSupportFacade/Create/Mode API inconnu");

    }
  }

  public final TypeDocument getRnd(final String code) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return rndSupport.getRnd(code);

    case MODE_API.DATASTAX:
      return rndCqlSupport.getRnd(code);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return rndSupport.getRnd(code);

    case MODE_API.DUAL_MODE_READ_CQL:
      return rndCqlSupport.getRnd(code);

    default:
      throw new ModeGestionAPIUnkownException("RndSupportFacade/Find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public List<TypeDocument> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return rndSupport.findAll();

    case MODE_API.DATASTAX:
      return rndCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return rndSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return rndCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("RndSupportFacade/findAll/Mode API inconnu");
    }
  }




}
