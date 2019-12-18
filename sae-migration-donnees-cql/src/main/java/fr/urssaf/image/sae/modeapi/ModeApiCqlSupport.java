/**
 *AC75095351
 */
package fr.urssaf.image.sae.modeapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.commons.utils.Constantes;

/**
 * Support de la classe DAO {@link IModeAPIDaoCql}
 */
@Service
public class ModeApiCqlSupport {

  @Autowired
  IModeAPIDaoCql modeApiDaoCql;

  final static String[] LIST_CF_NAME = {Constantes.CF_TRACE_DESTINATAIRE, Constantes.CF_TRACE_JOURNAL_EVT,
                                        Constantes.CF_TRACE_JOURNAL_EVT_INDEX, Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC,
                                        Constantes.CF_TRACE_REG_EXPLOITATION, Constantes.CF_TRACE_REG_EXPLOITATION_INDEX,
                                        Constantes.CF_TRACE_REG_SECURITE, Constantes.CF_TRACE_REG_SECURITE_INDEX,
                                        Constantes.CF_TRACE_REG_TECHNIQUE, Constantes.CF_TRACE_REG_TECHNIQUE_INDEX,
                                        Constantes.CF_JOBEXECUTION, Constantes.CF_JOBEXECUTIONS, Constantes.CF_JOBEXECUTIONS_RUNNING,
                                        Constantes.CF_JOB_HISTORY, Constantes.CF_JOBINSTANCE,
                                        Constantes.CF_JOBINSTANCES_BY_NAME, Constantes.CF_JOBINSTANCE_TO_JOBEXECUTION,
                                        Constantes.CF_JOB_REQUEST, Constantes.CF_JOBS_QUEUE, Constantes.CF_JOBSTEP,
                                        Constantes.CF_JOBSTEPS, Constantes.CF_DROIT_ACTION_UNITAIRE,
                                        Constantes.CF_DROIT_CONTRAT_SERVICE, Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL,
                                        Constantes.CF_DROIT_PAGM, Constantes.CF_DROIT_PAGMA, Constantes.CF_DROIT_PAGMP,
                                        Constantes.CF_DROIT_PAGMF, Constantes.CF_DROIT_PRMD, Constantes.CF_RND,
                                        Constantes.CF_CORRESPONDANCES_RND, Constantes.CF_METADATA,
                                        Constantes.CF_DICTIONARY, Constantes.CF_REFERENTIEL_FORMAT,
                                        Constantes.CF_PARAMETERS
  };

  public ModeApiCqlSupport(final IModeAPIDaoCql modeApiDaoCql) {
    this.modeApiDaoCql = modeApiDaoCql;
  }

  /**
   * Création d'un modeAPI
   *
   * @param action
   *          unitaire
   *          modeAPI à créer
   */
  public void create(final ModeAPI modeAPI) {
    saveOrUpdate(modeAPI);
  }

  /**
   * Méthode de suppression d'une modeAPI
   *
   * @param code
   *          identifiant de la modeAPI
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    modeApiDaoCql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de modeAPI en
   * fonction du code fourni
   *
   * @param code
   *          code de l'modeAPI
   * @return l'enregistrement de l'modeAPI correspondante
   */
  public final ModeAPI find(final String code) {

    return findById(code);

  }

  /**
   * * Recherche et retourne l'enregistrement de modeAPI en
   * fonction du code fourni en utilisant le mapper
   * 
   * @param code
   * @return l'enregistrement de l'modeAPI correspondante
   */
  public ModeAPI findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return modeApiDaoCql.findWithMapperById(code).orElse(null);

  }

  /**
   * Sauvegarde d'un modeAPI
   * 
   * @param modeAPI
   */
  private void saveOrUpdate(final ModeAPI modeAPI) {
    Assert.notNull(modeAPI, "l'objet modeAPI ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "";



    if (isValidCode) {

      // recuperation de l'objet ayant le meme cfname dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<ModeAPI> modeAPIOpt = modeApiDaoCql.findWithMapperById(modeAPI.getCfname());
      if (modeAPIOpt.isPresent()) {
        final ModeAPI modeAPIFromBD = modeAPIOpt.get();
        modeAPIFromBD.setMode(modeAPI.getMode());
        modeApiDaoCql.saveWithMapper(modeAPIFromBD);
      } else {
        modeApiDaoCql.saveWithMapper(modeAPI);
      }
    } else {
      throw new ModeAPIRuntimeException(
                                        "Impossible de créer l'enregistrement demandé. " + "La clé "
                                            + errorKey + " n'est pas supportée");
    }

  }

  /**
   * Retourne la liste de tous les modeAPI
   */
  public List<ModeAPI> findAll() {
    final Iterator<ModeAPI> it = modeApiDaoCql.findAllWithMapper();
    final List<ModeAPI> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * Retourne la liste de tous les modeAPI
   */
  public List<ModeAPI> findAll(final int max) {
    final int i = 0;
    final Iterator<ModeAPI> it = modeApiDaoCql.findAllWithMapper();
    final List<ModeAPI> list = new ArrayList<>();
    while (it.hasNext() && i < max) {
      list.add(it.next());
    }
    return list;
  }

  public void initTables(final String mode_API) {
    ModeAPI modeAPI;
    // On créé ou on update tous les modeAPI
    for (final String cfName : LIST_CF_NAME) {
      modeAPI = new ModeAPI();
      modeAPI.setCfname(cfName);
      modeAPI.setMode(mode_API);
      saveOrUpdate(modeAPI);
    }
  }
}
