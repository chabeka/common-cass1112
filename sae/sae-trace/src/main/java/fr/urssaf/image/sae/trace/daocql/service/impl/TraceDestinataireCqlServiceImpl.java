/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.trace.commons.TraceDestinataireEnum;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;
import fr.urssaf.image.sae.trace.daocql.service.ITraceDestinataireCqlService;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;

/**
 * TODO (AC75095028) Description du type
 */
@Service
public class TraceDestinataireCqlServiceImpl implements ITraceDestinataireCqlService {

  @Autowired
  ITraceDestinataireCqlDao destinatairecqldao;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCodeEvenementByTypeTrace(final String typeTrace) {

    final List<TraceDestinataire> listeTraceDestinataire = destinatairecqldao.findAllWithMapper();
    final List<String> listeCodeEvenement = new ArrayList<String>();

    for (final TraceDestinataire traceDestinataire : listeTraceDestinataire) {

      if (traceDestinataire.getDestinataires().containsKey(typeTrace)) {
        listeCodeEvenement.add(traceDestinataire.getCodeEvt());
      }
    }

    return listeCodeEvenement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(final TraceDestinataire trace, final long clock) {
    saveOrUpdate(trace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String code, final long clock) {
    Assert.notNull(code, "le code ne peut etre null");
    destinatairecqldao.deleteById(code);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<TraceDestinataire> findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return destinatairecqldao.findWithMapperById(code);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(final TraceDestinataire trace) {
    saveOrUpdate(trace);
  }

  /**
   * @param trace
   */
  private void saveOrUpdate(final TraceDestinataire trace) {
    Assert.notNull(trace, "l'objet traceDestinataire ne peut etre null");

    boolean isValidCode = true;
    String errorKey = "";

    if (MapUtils.isNotEmpty(trace.getDestinataires())) {
      for (final String key : trace.getDestinataires().keySet()) {
        if (!EnumUtils.isValidEnum(TraceDestinataireEnum.class, key)) {
          errorKey = key;
          isValidCode = false;
        }
        break;
      }
    }

    if (isValidCode) {
      Map<String, List<String>> destinatairesFromDB;

      // recuperation de l'objet ayant le meme codeevt dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<TraceDestinataire> traceOpt = destinatairecqldao.findWithMapperById(trace.getCodeEvt());
      if (traceOpt.isPresent()) {
        final TraceDestinataire traceFromBD = traceOpt.get();
        destinatairesFromDB = traceFromBD.getDestinataires();

        // nouveau destinataires à ajouter
        final Map<String, List<String>> newDestinataires = trace.getDestinataires();
        if (MapUtils.isNotEmpty(newDestinataires)) {
          for (final String key : newDestinataires.keySet()) {
            final List<String> desti = newDestinataires.get(key);
            // On ecrase l'existant avec le nouveau s'il y en un avec la meme clé "key"
            destinatairesFromDB.put(key, desti);
          }
        }
        traceFromBD.setDestinataires(destinatairesFromDB);
        destinatairecqldao.save(traceFromBD);
      } else {
        destinatairecqldao.save(trace);
      }
    } else {
      throw new TraceRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TraceDestinataire> findAll() {
    return destinatairecqldao.findAllWithMapper();
  }

}
