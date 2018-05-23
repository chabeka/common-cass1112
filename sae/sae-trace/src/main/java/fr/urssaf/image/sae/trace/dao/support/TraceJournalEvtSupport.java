package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceJournalEvtDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDocDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceJournalEvtIndexDocIterator;
import fr.urssaf.image.sae.trace.dao.iterator.TraceJournalEvtIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Support de la classe DAO {@link TraceJournalEvtDao}
 */
@Component
public class TraceJournalEvtSupport extends
                                    AbstractTraceSupport<TraceJournalEvt, TraceJournalEvtIndex> {

  private static final String JOURNAL_EVT_NAME = "journal des événements";

  private final TraceJournalEvtDao dao;

  private final TraceJournalEvtIndexDao indexDao;

  private final TraceJournalEvtIndexDocDao indexDocDao;

  private final TimeUUIDEtTimestampSupport timeUUIDSupport;

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(TraceJournalEvtSupport.class);

  /**
   * @param dao
   *          Service DAO de la famille de colonnes "TraceJournalEvt"
   * @param indexDao
   *          Service DAO de la famille de colonnes "TraceJournalEvtIndex"
   * @param indexDocDao
   *          Service DAO de la famille de colonnes "TraceJournalEvtIndexDoc"
   * @param timeUUIDSupport
   *          Utilitaires pour créer des TimeUUID
   */
  @Autowired
  public TraceJournalEvtSupport(final TraceJournalEvtDao dao,
                                final TraceJournalEvtIndexDao indexDao,
                                final TraceJournalEvtIndexDocDao indexDocDao,
                                final TimeUUIDEtTimestampSupport timeUUIDSupport) {
    super();
    this.dao = dao;
    this.indexDao = indexDao;
    this.indexDocDao = indexDocDao;
    this.timeUUIDSupport = timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void completeCreateTrace(
                                           final ColumnFamilyUpdater<UUID, String> updater, final TraceJournalEvt trace,
                                           final long clock) {

    getDao().writeColumnContext(updater, trace.getContexte(), clock);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final TraceJournalEvtDao getDao() {
    return dao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final TraceJournalEvtIndexDao getIndexDao() {
    return indexDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final TraceJournalEvtIndex getIndexFromTrace(final TraceJournalEvt trace) {
    return new TraceJournalEvtIndex(trace);
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
  protected final Iterator<TraceJournalEvtIndex> getIterator(
                                                             final SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery) {
    return new TraceJournalEvtIndexIterator(sliceQuery);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final Iterator<TraceJournalEvtIndex> getIterator(
                                                             final SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery,
                                                             final UUID startUuid, final UUID endUuid, final boolean reversed) {
    return new TraceJournalEvtIndexIterator(sliceQuery,
                                            startUuid,
                                            endUuid,
                                            reversed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void completeTraceFromResult(final TraceJournalEvt trace,
                                               final ColumnFamilyResult<UUID, String> result) {
    trace.setContexte(result.getString(TraceJournalEvtDao.COL_CONTEXT));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final TraceJournalEvt createNewInstance(final UUID idTrace,
                                                    final Date timestamp) {
    return new TraceJournalEvt(idTrace, timestamp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final TimeUUIDEtTimestampSupport getTimeUuidSupport() {
    return timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final Logger getLogger() {
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
  public final void addIndexDoc(final TraceJournalEvt trace, final String idDoc, final long clock) {
    final TraceJournalEvtIndexDoc traceJournal = new TraceJournalEvtIndexDoc(trace);
    final ColumnFamilyUpdater<String, UUID> updater = indexDocDao
                                                                 .createUpdater(idDoc);
    indexDocDao.writeColumn(updater,
                            traceJournal.getIdentifiant(),
                            traceJournal,
                            clock);
    indexDocDao.update(updater);
  }

  /**
   * Recherche des traces du journal des événements par identifiant du document
   *
   * @param idDoc
   *          Identifiant du document
   * @return La liste des évènements trouvé La méthode renvoi "null" si aucun
   *         évènement trouvé.
   */
  public final List<TraceJournalEvtIndexDoc> findByIdDoc(final UUID idDoc) {
    final SliceQuery<String, UUID, TraceJournalEvtIndexDoc> sQuery = indexDocDao
                                                                                .createSliceQuery();
    sQuery.setKey(idDoc.toString());

    List<TraceJournalEvtIndexDoc> traces = null;
    final Iterator<TraceJournalEvtIndexDoc> iterator = new TraceJournalEvtIndexDocIterator(
                                                                                           sQuery);

    if (iterator.hasNext()) {
      traces = new ArrayList<TraceJournalEvtIndexDoc>();
      while (iterator.hasNext()) {
        final TraceJournalEvtIndexDoc trace = iterator.next();
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
    final Mutator<String> mutator = indexDocDao.createMutator();
    indexDocDao.deleteIndex(mutator, idDoc.toString(), clock);
    mutator.execute();
  }

}
