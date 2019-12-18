/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.supportcql;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;
import fr.urssaf.image.sae.trace.daocql.impl.TraceJournalEvtCqlDaoImpl;
import fr.urssaf.image.sae.trace.daocql.impl.TraceJournalEvtIndexCqlDaoImpl;
import fr.urssaf.image.sae.trace.daocql.impl.TraceJournalEvtIndexDocDaoCqlImpl;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Support des classe DAO: <br>
 * {@link TraceJournalEvtCqlDaoImpl}<br>
 * {@link TraceJournalEvtIndexCqlDaoImpl}<br>
 * {@link TraceJournalEvtIndexDocDaoCqlImpl}
 */
@Component
public class TraceJournalEvtCqlSupport extends GenericAbstractTraceCqlSupport<TraceJournalEvtCql, TraceJournalEvtIndexCql> {

  private static final String JOURNAL_EVT_NAME = "journal des événements";

  // private static final String DATE_FORMAT = "yyyyMMdd";

  private final ITraceJournalEvtCqlDao tracejdao;

  private final ITraceJournalEvtIndexCqlDao indexjDao;

  private final ITraceJournalEvtIndexDocCqlDao indexjDocDao;

  private final TimeUUIDEtTimestampSupport timeUUIDSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(TraceJournalEvtCqlSupport.class);

  /**
   * @param tracejdao
   *          Service DAO de la famille de colonnes "TraceJournalEvt"
   * @param indexjDao
   *          Service DAO de la famille de colonnes "TraceJournalEvtIndex"
   * @param indexjDocDao
   *          Service DAO de la famille de colonnes "TraceJournalEvtIndexDoc"
   * @param timeUUIDSupport
   *          Utilitaires pour créer des TimeUUID
   */
  @Autowired
  public TraceJournalEvtCqlSupport(final ITraceJournalEvtCqlDao dao,
                                   final ITraceJournalEvtIndexCqlDao indexDao,
                                   final ITraceJournalEvtIndexDocCqlDao indexDocDao,
                                   final TimeUUIDEtTimestampSupport timeUUIDSupport) {
    super();
    tracejdao = dao;
    indexjDao = indexDao;
    indexjDocDao = indexDocDao;
    this.timeUUIDSupport = timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final IGenericDAO<TraceJournalEvtCql, UUID> getDao() {
    return tracejdao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final TraceJournalEvtIndexCql getIndexFromTrace(final TraceJournalEvtCql trace) {
    final TraceJournalEvtIndexCql index = new TraceJournalEvtIndexCql(trace);
    // date en string sous la forme de YYYYMMJJ sans les heures et les secondes
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    index.setIdentifiantIndex(journee);
    return index;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final String getRegistreName() {
    return JOURNAL_EVT_NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<TraceJournalEvtIndexCql> getIterator(final Date date) {

    final String journee = DateRegUtils.getJournee(date);
    /*
     * try {
     * dateJ = dateFormat.parse(journee);
     * }
     * catch (final ParseException e) {
     * LOGGER.error("Le parsin de la date fournie est impossible");
     * e.printStackTrace();
     * }
     */
    return indexjDao.IterableFindById(journee);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  TraceJournalEvtCql createNewInstance(final UUID idTrace, final Date timestamp) {
    return new TraceJournalEvtCql(idTrace, timestamp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  TimeUUIDEtTimestampSupport getTimeUuidSupport() {
    return timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  Logger getLogger() {
    return LOGGER;
  }

  /**
   * Ajout d’un index sur l’identifiant de document
   *
   * @param trace
   *          Trace du journal des événements
   * @param idDoc
   *          Identifiant du document
   * @param clock
   *          Horloge de création
   */
  public final void addIndexDoc(final TraceJournalEvtCql trace, final String idDoc, final long clock) {
    final TraceJournalEvtIndexDocCql traceJournal = new TraceJournalEvtIndexDocCql(trace);
    traceJournal.setIdentifiantIndex(java.util.UUID.fromString(idDoc));
    indexjDocDao.saveWithMapper(traceJournal);
  }

  /**
   * Recherche des traces du journal des événements par identifiant du document
   *
   * @param idDoc
   *          Identifiant du document
   * @return La liste des évènements trouvé La méthode renvoi "null" si aucun
   *         évènement trouvé.
   */
  public final List<TraceJournalEvtIndexDocCql> findByIdDoc(final UUID idDoc) {

    List<TraceJournalEvtIndexDocCql> traces = null;
    final Iterator<TraceJournalEvtIndexDocCql> iterator = indexjDocDao.IterableFindById(idDoc);

    if (iterator.hasNext()) {
      traces = new ArrayList<>();
      while (iterator.hasNext()) {
        final TraceJournalEvtIndexDocCql trace = iterator.next();
        traces.add(trace);
      }
    }
    return traces;
  }


  /**
   * Suppression d'index du document
   *
   * @param idDoc
   *          Identifiant du document
   * @param clock
   *          Horloge de création
   */
  public final void deleteIndexDoc(final UUID idDoc, final long clock) {
    indexjDocDao.deleteById(idDoc);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IGenericDAO<TraceJournalEvtIndexCql, String> getIndexDao() {
    return indexjDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  UUID getTraceId(final TraceJournalEvtIndexCql trace) {
    return trace.getIdentifiant();
  }

}
