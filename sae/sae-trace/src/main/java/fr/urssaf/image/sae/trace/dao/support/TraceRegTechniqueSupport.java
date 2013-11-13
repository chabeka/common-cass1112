package fr.urssaf.image.sae.trace.dao.support;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegTechniqueIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

/**
 * Support de la classe DAO {@link TraceRegTechniqueDao}
 * 
 */
@Component
public class TraceRegTechniqueSupport extends
      AbstractTraceSupport<TraceRegTechnique, TraceRegTechniqueIndex> {

   private final TraceRegTechniqueDao dao;

   private final TraceRegTechniqueIndexDao indexDao;

   private final TimeUUIDEtTimestampSupport timeUUIDSupport;

   private static final String REG_TECHNIQUE_NAME = "registre de surveillance technique";
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceRegTechniqueSupport.class);

   /**
    * Constructeur
    * 
    * @param dao
    *           Service DAO de la famille de colonnes "TraceRegTechnique"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceRegTechniqueIndex"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceRegTechniqueSupport(TraceRegTechniqueDao dao,
         TraceRegTechniqueIndexDao indexDao,
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
         ColumnFamilyUpdater<UUID, String> updater, TraceRegTechnique trace,
         long clock) {

      getDao().writeColumnContexte(updater, trace.getContexte(), clock);
      if (StringUtils.isNotBlank(trace.getStacktrace())) {
         getDao().writeColumnStackTrace(updater, trace.getStacktrace(), clock);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegTechniqueDao getDao() {
      return dao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegTechniqueIndexDao getIndexDao() {
      return indexDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegTechniqueIndex getIndexFromTrace(TraceRegTechnique trace) {
      return new TraceRegTechniqueIndex(trace);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getRegistreName() {
      return REG_TECHNIQUE_NAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceRegTechniqueIndex> getIterator(
         SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery) {
      return new TraceRegTechniqueIndexIterator(sliceQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Iterator<TraceRegTechniqueIndex> getIterator(
         SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery,
         UUID startUuid, UUID endUuid, boolean reversed) {
      return new TraceRegTechniqueIndexIterator(sliceQuery, startUuid, endUuid,
            reversed);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void completeTraceFromResult(TraceRegTechnique trace,
         ColumnFamilyResult<UUID, String> result) {
      trace.setContexte(result.getString(TraceRegTechniqueDao.COL_CONTEXTE));
      trace
            .setStacktrace(result
                  .getString(TraceRegTechniqueDao.COL_STACKTRACE));

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected TraceRegTechnique createNewInstance(UUID idTrace, Date timestamp) {
      return new TraceRegTechnique(idTrace, timestamp);
   }

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