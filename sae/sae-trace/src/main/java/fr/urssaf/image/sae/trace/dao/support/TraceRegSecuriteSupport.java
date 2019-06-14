package fr.urssaf.image.sae.trace.dao.support;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

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
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Support de la classe DAO {@link TraceRegSecuriteDao}
 */
@Component
public class TraceRegSecuriteSupport extends AbstractTraceSupport<TraceRegSecurite, TraceRegSecuriteIndex> {

   private static final String REG_SECURITE_NAME = "registre de sécurité";

   private final TraceRegSecuriteDao dao;

   private final TraceRegSecuriteIndexDao indexDao;

   private final TimeUUIDEtTimestampSupport timeUUIDSupport;

   private static final Logger LOGGER = LoggerFactory.getLogger(TraceRegSecuriteSupport.class);

   /**
    * @param dao
    *           Service DAO de la famille de colonnes "TraceRegSecurite"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceRegSecuriteIndex"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceRegSecuriteSupport(final TraceRegSecuriteDao dao, final TraceRegSecuriteIndexDao indexDao,
                                  final TimeUUIDEtTimestampSupport timeUUIDSupport) {
      super();
      this.dao = dao;
      this.indexDao = indexDao;
      this.timeUUIDSupport = timeUUIDSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void completeCreateTrace(final ColumnFamilyUpdater<UUID, String> updater, final TraceRegSecurite trace, final long clock) {

      if (trace.getInfos() != null) {
         getDao().writeColumnInfos(updater, trace.getInfos(), clock);
      }

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
   protected TraceRegSecuriteIndex getIndexFromTrace(final TraceRegSecurite trace) {
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
   protected Iterator<TraceRegSecuriteIndex> getIterator(final SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery) {
      return new TraceRegSecuriteIndexIterator(sliceQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceRegSecuriteIndex> getIterator(final SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery,
                                                         final UUID startUuid, final UUID endUuid, final boolean reversed) {
      return new TraceRegSecuriteIndexIterator(sliceQuery, startUuid, endUuid, reversed);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void completeTraceFromResult(final TraceRegSecurite trace, final ColumnFamilyResult<UUID, String> result) {
      final byte[] bValue = result.getByteArray(TraceRegTechniqueDao.COL_INFOS);
      if (bValue != null) {
         trace.setInfos(MapSerializer.get().fromBytes(bValue));
      }
      trace.setContexte(result.getString(TraceRegTechniqueDao.COL_CONTEXTE));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TraceRegSecurite createNewInstance(final UUID idTrace, final Date timestamp) {
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
