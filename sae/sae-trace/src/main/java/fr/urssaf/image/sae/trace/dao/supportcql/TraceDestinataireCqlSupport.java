/**
 *
 */
package fr.urssaf.image.sae.trace.dao.supportcql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.commons.TraceDestinataireEnum;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;
import fr.urssaf.image.sae.trace.daocql.impl.TraceDestinataireCqlDaoImpl;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;

/**
 * Support de la classe DAO {@link TraceDestinataireDao}
 */
@Service
public class TraceDestinataireCqlSupport {

  private static final int LIFE_DURATION = 10;

  @Autowired
  ITraceDestinataireCqlDao destinatairecqldao;

  private LoadingCache<String, TraceDestinataire> traces;

  /**
   * Constructeur
   *
   * @param dao
   *           DAO des traces destinataires
   */

  public TraceDestinataireCqlSupport() {
    traces = CacheBuilder.newBuilder()
        .expireAfterWrite(LIFE_DURATION,
                          TimeUnit.MINUTES)
        .build(
               new CacheLoader<String, TraceDestinataire>() {

                 @Override
                 public TraceDestinataire load(final String identifiant) {
                   return findById(identifiant);
                 }

               });
  }

   public TraceDestinataireCqlSupport(final CassandraCQLClientFactory ccf) {

		final ITraceDestinataireCqlDao dao = new TraceDestinataireCqlDaoImpl();
		dao.setCcf(ccf);
		destinatairecqldao = dao;
  }
  /**
   * Création d'une trace destinataire
   *
   * @param trace
   *           trace à créer
   * @param clock
   *           horloge de la création
   */
  public void create(final TraceDestinataire trace, final long clock) {
    saveOrUpdate(trace);
  }

  /**
   * Méthode de suppression d'une trace destinataire
   *
   * @param code
   *           identifiant de la trace
   * @param clock
   *           horloge de suppression
   */
  public void delete(final String code, final long clock) {
    Assert.notNull(code, "le code ne peut etre null");
    destinatairecqldao.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de la trace destinataire en
   * fonction du code fourni
   *
   * @param code
   *           code de la trace destinataire
   * @return l'enregistrement de la trace destinataire correspondante
   */
  public final TraceDestinataire find(final String code) {
    try {
      return traces.getUnchecked(code);

    }
    catch (final InvalidCacheLoadException exception) {
      throw new TraceRuntimeException(exception);
    }
  }

  /**
   * {@inheritDoc}
   */
  public TraceDestinataire findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return destinatairecqldao.findWithMapperById(code).orElse(null);

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
        destinatairecqldao.saveWithMapper(traceFromBD);
      } else {
        destinatairecqldao.saveWithMapper(trace);
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
  public List<TraceDestinataire> findAll() {
    final Iterator<TraceDestinataire> it = destinatairecqldao.findAllWithMapper();
    final List<TraceDestinataire> list = new ArrayList<>();
    while (it.hasNext()) {

      list.add(it.next());

    }
    return list;
  }
}
