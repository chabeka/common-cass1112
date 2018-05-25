/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.supportcql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.IGenericIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class TraceJournalEvtCqlSupport extends GenericAbstractTraceCqlSupport<TraceJournalEvtCql, TraceJournalEvtIndexCql> {

  private static final String JOURNAL_EVT_NAME = "journal des événements";

  private static final String DATE_FORMAT = "yyyyMMdd";

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
    this.tracejdao = dao;
    this.indexjDao = indexDao;
    this.indexjDocDao = indexDocDao;
    this.timeUUIDSupport = timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final IGenericDAO<TraceJournalEvtCql, UUID> getDao() {
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
   * @return the dateFormat
   */

  @Override
  public String getDateFormat() {
    return DATE_FORMAT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<TraceJournalEvtIndexCql> getIterator(final Date date) {
    final Date dateJ = null;
    final DateFormat dateFormat = new SimpleDateFormat(getDateFormat());
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
  Iterator<TraceJournalEvtIndexCql> getIterator(final Date dateStar, final Date dateEnd, final boolean reversed, final Integer limit) {
    final String jourStar = DateRegUtils.getJournee(dateStar);
    final String journEnd = DateRegUtils.getJournee(dateEnd);
    return indexjDao.findByDateInterval(jourStar, journEnd, reversed, limit);
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
    traceJournal.setIdentifiantIndex(idDoc);
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
    final Iterator<TraceJournalEvtIndexDocCql> iterator = indexjDocDao.IterableFindById(idDoc.toString());
    if (iterator.hasNext()) {
      traces = new ArrayList<TraceJournalEvtIndexDocCql>();
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
    indexjDocDao.deleteById(idDoc.toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  IGenericIndexCqlDao<TraceJournalEvtIndexCql, String> getIndexDao() {
    return indexjDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getIndexId(final TraceJournalEvtIndexCql trace) {
    return trace.getIdentifiantIndex();
  }

}
