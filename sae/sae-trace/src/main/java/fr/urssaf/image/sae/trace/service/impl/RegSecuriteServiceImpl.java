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
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Classe d'implémentation du support {@link RegSecuriteService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class RegSecuriteServiceImpl implements RegSecuriteService {

   @Autowired
   private TraceRegSecuriteSupport support;

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private LoggerSupport loggerSupport;

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(RegSecuriteServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<TraceRegSecuriteIndex> lecture(Date dateDebut,
         Date dateFin, int limite, boolean reversed) {

      String prefix = "lecture()";
      LOGGER.debug(DEBUT_LOG, prefix);

      List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);

      List<TraceRegSecuriteIndex> value = null;
      List<TraceRegSecuriteIndex> list;
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
   public final TraceRegSecurite lecture(UUID identifiant) {
      return support.find(identifiant);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purge(Date dateDebut, Date dateFin) {

      String prefix = "purge()";
      LOGGER.debug(DEBUT_LOG, prefix);

      Date date = DateUtils.truncate(dateDebut, Calendar.DATE);
      Date endDate = DateUtils.truncate(dateFin, Calendar.DATE);

      do {

         loggerSupport.logPurgeJourneeDebut(LOGGER, prefix,
               PurgeType.PURGE_SECURITE, DateRegUtils.getJournee(date));
         long nbTracesPurgees = support.delete(date, clockSupport
               .currentCLock());
         loggerSupport.logPurgeJourneeFin(LOGGER, prefix,
               PurgeType.PURGE_SECURITE, DateRegUtils.getJournee(date),
               nbTracesPurgees);
         date = DateUtils.addDays(date, 1);

      } while (date.compareTo(endDate) <= 0);

      LOGGER.debug(FIN_LOG, prefix);

   }

   private List<TraceRegSecuriteIndex> findNormalOrder(List<Date> dates,
         int limite) {
      int index = 0;
      int countLeft = limite;
      List<TraceRegSecuriteIndex> result;
      List<TraceRegSecuriteIndex> values = new ArrayList<TraceRegSecuriteIndex>();
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

   private List<TraceRegSecuriteIndex> findReversedOrder(List<Date> dates,
         int limite) {

      int index = dates.size() - 1;
      int countLeft = limite;
      List<TraceRegSecuriteIndex> result;
      List<TraceRegSecuriteIndex> values = new ArrayList<TraceRegSecuriteIndex>();
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

}
