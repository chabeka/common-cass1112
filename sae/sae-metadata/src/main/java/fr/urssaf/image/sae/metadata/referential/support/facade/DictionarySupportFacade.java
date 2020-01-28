package fr.urssaf.image.sae.metadata.referential.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.DictionaryCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Dictionary"
 */

@Component
public class DictionarySupportFacade {

  @Autowired
  IDictionaryDaoCql dictionaryDaoCql;

  private final String cfName = Constantes.CF_DICTIONARY;

  private final DictionarySupport dictionarySupport;

  private final DictionaryCqlSupport dictionaryCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée dictionary
   */
  @Autowired
  public DictionarySupportFacade(final DictionarySupport dictionarySupport,
                                 final DictionaryCqlSupport dictionaryCqlSupport,
                                 final JobClockSupport clockSupport) {
    this.dictionarySupport = dictionarySupport;
    this.dictionaryCqlSupport = dictionaryCqlSupport;
    this.clockSupport = clockSupport;
  }


  public final void addElement(final String identifiant, final String value) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      dictionarySupport.addElement(identifiant, value, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      dictionaryCqlSupport.addElement(identifiant, value);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      dictionaryCqlSupport.addElement(identifiant, value);
      dictionarySupport.addElement(identifiant, value, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("DictionarySupportFacade/Create/Mode API inconnu");

    }
  }

  public final Dictionary find(final String identifiant) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return dictionarySupport.find(identifiant);

    case MODE_API.DATASTAX:
      return dictionaryCqlSupport.find(identifiant);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return dictionarySupport.find(identifiant);

    case MODE_API.DUAL_MODE_READ_CQL:
      return dictionaryCqlSupport.find(identifiant);

    default:
      throw new ModeGestionAPIUnkownException("DictionarySupportFacade/Find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public List<Dictionary> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return dictionarySupport.findAll();

    case MODE_API.DATASTAX:
      return dictionaryCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return dictionarySupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return dictionaryCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("DictionarySupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public void deleteElement(final String identifiant, final String value) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      dictionarySupport.deleteElement(identifiant, value, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      dictionaryCqlSupport.deleteElement(identifiant, value);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      dictionarySupport.deleteElement(identifiant, value, clockSupport.currentCLock());
      dictionaryCqlSupport.deleteElement(identifiant, value);
      break;

    default:
      throw new ModeGestionAPIUnkownException("DictionarySupportFacade/delete/Mode API inconnu");
    }
  }


}
