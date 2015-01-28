package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

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

/**
 * Support de la classe DAO {@link TraceJournalEvtDao}
 * 
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
    *           Service DAO de la famille de colonnes "TraceJournalEvt"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceJournalEvtIndex"
    * @param indexDocDao
    *           Service DAO de la famille de colonnes "TraceJournalEvtIndexDoc"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceJournalEvtSupport(TraceJournalEvtDao dao,
         TraceJournalEvtIndexDao indexDao,
         TraceJournalEvtIndexDocDao indexDocDao,
         TimeUUIDEtTimestampSupport timeUUIDSupport) {
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
         ColumnFamilyUpdater<UUID, String> updater, TraceJournalEvt trace,
         long clock) {

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
   protected final TraceJournalEvtIndex getIndexFromTrace(TraceJournalEvt trace) {
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
         SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery) {
      return new TraceJournalEvtIndexIterator(sliceQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Iterator<TraceJournalEvtIndex> getIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery,
         UUID startUuid, UUID endUuid, boolean reversed) {
      return new TraceJournalEvtIndexIterator(sliceQuery, startUuid, endUuid,
            reversed);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void completeTraceFromResult(TraceJournalEvt trace,
         ColumnFamilyResult<UUID, String> result) {
      trace.setContexte(result.getString(TraceJournalEvtDao.COL_CONTEXT));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TraceJournalEvt createNewInstance(UUID idTrace,
         Date timestamp) {
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
    * 
    * Ajout d’un index sur l’identifiant de document
    * 
    * @param trace
    *           Trace du journal des événements
    * @param idDoc
    *           Identifiant du document
    * @param clock
    *           Horloge de création
    */
   public final void addIndexDoc(TraceJournalEvt trace, String idDoc, long clock) {
      TraceJournalEvtIndexDoc traceJournal = new TraceJournalEvtIndexDoc(trace);
      ColumnFamilyUpdater<String, UUID> updater = indexDocDao
            .createUpdater(idDoc);
      indexDocDao.writeColumn(updater, traceJournal.getIdentifiant(),
            traceJournal, clock);
      indexDocDao.update(updater);
   }

   /**
    * Recherche des traces du journal des événements par identifiant du document
    * 
    * @param idDoc
    *           Identifiant du document
    * 
    * @return La liste des évènements trouvé La méthode renvoi "null" si aucun
    *         évènement trouvé.
    */
   public final List<TraceJournalEvtIndexDoc> findByIdDoc(UUID idDoc) {
      SliceQuery<String, UUID, TraceJournalEvtIndexDoc> sQuery = indexDocDao
            .createSliceQuery();
      sQuery.setKey(idDoc.toString());

      List<TraceJournalEvtIndexDoc> traces = null;
      Iterator<TraceJournalEvtIndexDoc> iterator = new TraceJournalEvtIndexDocIterator(
            sQuery);

      if (iterator.hasNext()) {
         traces = new ArrayList<TraceJournalEvtIndexDoc>();
         while (iterator.hasNext()) {
            TraceJournalEvtIndexDoc trace = iterator.next();
            traces.add(trace);
         }
      }
      return traces;
   }

   /**
    * Suppression d'index du document
    * 
    * @param idDoc
    *           Identifiant du document
    * @param clock
    *           Horloge de création
    */
   public final void deleteIndexDoc(UUID idDoc, long clock) {
      Mutator<String> mutator = indexDocDao.createMutator();
      indexDocDao.deleteIndex(mutator, idDoc.toString(), clock);
      mutator.execute();
   }
}
