/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Classe d'implémentation du support {@link RegTechniqueService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class RegTechniqueServiceImpl implements RegTechniqueService {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";

   private final TraceRegTechniqueSupport support;

   private final JobClockSupport clockSupport;

   private final LoggerSupport loggerSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RegTechniqueServiceImpl.class);

   /**
    * Constructeur
    * 
    * @param support
    *           Support de la classe DAO TraceRegTechniqueDao
    * @param clockSupport
    *           JobClockSupport Cassandra
    * @param loggerSupport
    *           Support pour l'écriture des traces applicatives
    */
   @Autowired
   public RegTechniqueServiceImpl(TraceRegTechniqueSupport support,
         JobClockSupport clockSupport, LoggerSupport loggerSupport) {
      super();
      this.support = support;
      this.clockSupport = clockSupport;
      this.loggerSupport = loggerSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<TraceRegTechniqueIndex> lecture(Date dateDebut,
         Date dateFin, int limite, boolean reversed) {

      String prefix = "lecture()";
      LOGGER.debug(DEBUT_LOG, prefix);

      List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);

      List<TraceRegTechniqueIndex> value = null;
      List<TraceRegTechniqueIndex> list;
      if (reversed) {
         list = findReversedOrder(dates, limite);
      } else {
         list = findNormalOrder(dates, limite);
      }

      if (CollectionUtils.isNotEmpty(list)) {
         value = list;
      }

      LOGGER.debug(FIN_LOG, prefix);

      return value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final TraceRegTechnique lecture(UUID identifiant) {
      return support.find(identifiant);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purge(Date date) {
      String prefix = "purge()";
      LOGGER.debug(DEBUT_LOG, prefix);

      Date dateIndex = DateUtils.truncate(date, Calendar.DATE);

      loggerSupport.logPurgeJourneeDebut(LOGGER, prefix,
            PurgeType.PURGE_TECHNIQUE, DateRegUtils.getJournee(date));
      long nbTracesPurgees = support.delete(dateIndex, clockSupport
            .currentCLock());
      loggerSupport.logPurgeJourneeFin(LOGGER, prefix,
            PurgeType.PURGE_TECHNIQUE, DateRegUtils.getJournee(date),
            nbTracesPurgees);

      LOGGER.debug(FIN_LOG, prefix);

   }

   private List<TraceRegTechniqueIndex> findNormalOrder(List<Date> dates,
         int limite) {
      int index = 0;
      int countLeft = limite;
      List<TraceRegTechniqueIndex> result;
      List<TraceRegTechniqueIndex> values = new ArrayList<TraceRegTechniqueIndex>();
      Date currentDate, startDate, endDate;

      do {
         currentDate = dates.get(index);
         startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
         endDate = DateRegUtils.getEndDate(currentDate, dates
               .get(dates.size() - 1));

         result = support.findByDates(startDate, endDate, countLeft, false);

         if (CollectionUtils.isNotEmpty(result)) {
            values.addAll(result);
            countLeft = limite - values.size();
         }
         index++;
      } while (index < dates.size() && countLeft > 0
            && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

      return values;
   }

   private List<TraceRegTechniqueIndex> findReversedOrder(List<Date> dates,
         int limite) {

      int index = dates.size() - 1;
      int countLeft = limite;
      List<TraceRegTechniqueIndex> result;
      List<TraceRegTechniqueIndex> values = new ArrayList<TraceRegTechniqueIndex>();
      Date currentDate, startDate, endDate;

      do {
         currentDate = dates.get(index);
         startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
         endDate = DateRegUtils.getEndDate(currentDate, dates
               .get(dates.size() - 1));

         result = support.findByDates(startDate, endDate, countLeft, true);

         if (CollectionUtils.isNotEmpty(result)) {
            values.addAll(result);
            countLeft = limite - values.size();
         }
         index--;
      } while (index >= 0 && countLeft > 0
            && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

      return values;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean hasRecords(Date date) {

      Date beginDate = DateUtils.truncate(date, Calendar.DATE);
      Date endDate = DateUtils.addDays(beginDate, 1);
      endDate = DateUtils.addMilliseconds(endDate, -1);

      List<TraceRegTechniqueIndex> list = lecture(beginDate, endDate, 1, false);

      boolean hasRecords = CollectionUtils.isNotEmpty(list);

      return hasRecords;
   }

}
