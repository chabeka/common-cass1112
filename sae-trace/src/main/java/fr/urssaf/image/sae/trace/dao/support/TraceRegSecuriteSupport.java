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

import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteDao;
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegSecuriteIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

/**
 * Support de la classe DAO {@link TraceRegSecuriteDao}
 * 
 */
@Component
public class TraceRegSecuriteSupport extends
      AbstractTraceSupport<TraceRegSecurite, TraceRegSecuriteIndex> {

   private static final String REG_SECURITE_NAME = "registre de sécurité";

   private final TraceRegSecuriteDao dao;

   private final TraceRegSecuriteIndexDao indexDao;

   private final TimeUUIDEtTimestampSupport timeUUIDSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceRegSecuriteSupport.class);

   /**
    * @param dao
    *           Service DAO de la famille de colonnes "TraceRegSecurite"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceRegSecuriteIndex"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceRegSecuriteSupport(TraceRegSecuriteDao dao,
         TraceRegSecuriteIndexDao indexDao,
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
         ColumnFamilyUpdater<UUID, String> updater, TraceRegSecurite trace,
         long clock) {

      getDao().writeColumnContexte(updater, trace.getContexte(), clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegSecuriteDao getDao() {
      return dao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegSecuriteIndexDao getIndexDao() {
      return indexDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegSecuriteIndex getIndexFromTrace(TraceRegSecurite trace) {
      return new TraceRegSecuriteIndex(trace);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getRegistreName() {
      return REG_SECURITE_NAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceRegSecuriteIndex> getIterator(
         SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery) {
      return new TraceRegSecuriteIndexIterator(sliceQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceRegSecuriteIndex> getIterator(
         SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery,
         UUID startUuid, UUID endUuid, boolean reversed) {
      return new TraceRegSecuriteIndexIterator(sliceQuery, startUuid, endUuid,
            reversed);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void completeTraceFromResult(TraceRegSecurite trace,
         ColumnFamilyResult<UUID, String> result) {
      trace.setContexte(result.getString(TraceRegTechniqueDao.COL_CONTEXTE));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TraceRegSecurite createNewInstance(UUID idTrace, Date timestamp) {
      return new TraceRegSecurite(idTrace, timestamp);
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
