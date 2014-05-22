package fr.urssaf.image.sae.trace.dao.support;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.query.SliceQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceJournalEvtDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceJournalEvtIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
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

   private final TimeUUIDEtTimestampSupport timeUUIDSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceJournalEvtSupport.class);

   /**
    * @param dao
    *           Service DAO de la famille de colonnes "TraceJournalEvt"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceJournalEvtIndex"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceJournalEvtSupport(TraceJournalEvtDao dao,
         TraceJournalEvtIndexDao indexDao,
         TimeUUIDEtTimestampSupport timeUUIDSupport) {
      super();
      this.dao = dao;
      this.indexDao = indexDao;
      this.timeUUIDSupport = timeUUIDSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void completeCreateTrace(
         ColumnFamilyUpdater<UUID, String> updater, TraceJournalEvt trace,
         long clock) {

      getDao().writeColumnContext(updater, trace.getContexte(), clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceJournalEvtDao getDao() {
      return dao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceJournalEvtIndexDao getIndexDao() {
      return indexDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceJournalEvtIndex getIndexFromTrace(TraceJournalEvt trace) {
      return new TraceJournalEvtIndex(trace);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getRegistreName() {
      return JOURNAL_EVT_NAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceJournalEvtIndex> getIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery) {
      return new TraceJournalEvtIndexIterator(sliceQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceJournalEvtIndex> getIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery,
         UUID startUuid, UUID endUuid, boolean reversed) {
      return new TraceJournalEvtIndexIterator(sliceQuery, startUuid, endUuid,
            reversed);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void completeTraceFromResult(TraceJournalEvt trace,
         ColumnFamilyResult<UUID, String> result) {
      trace.setContexte(result.getString(TraceJournalEvtDao.COL_CONTEXT));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceJournalEvt createNewInstance(UUID idTrace, Date timestamp) {
      return new TraceJournalEvt(idTrace, timestamp);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TimeUUIDEtTimestampSupport getTimeUuidSupport() {
      return timeUUIDSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Logger getLogger() {
      return LOGGER;
   }
}
