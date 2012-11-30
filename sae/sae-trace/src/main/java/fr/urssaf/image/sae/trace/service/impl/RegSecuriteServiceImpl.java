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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
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

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<TraceRegSecuriteIndex> lecture(Date dateDebut,
         Date dateFin, int limite, boolean reversed) {

      int sizeMax = limite;
      Date endDate = dateFin;
      List<TraceRegSecuriteIndex> list = new ArrayList<TraceRegSecuriteIndex>(
            limite);
      List<TraceRegSecuriteIndex> value = null;
      Date startDate = DateRegUtils.getFirstDate(dateDebut, dateFin);
      List<TraceRegSecuriteIndex> result;

      do {
         result = support.findByDates(startDate, endDate, sizeMax, reversed);

         if (result != null) {
            list.addAll(result);
         }
         sizeMax = limite - list.size();

         endDate = startDate;
         startDate = DateRegUtils.getStartDate(startDate, dateDebut);

      } while (startDate.compareTo(dateDebut) >= 0 && sizeMax > 0);

      if (CollectionUtils.isNotEmpty(list)) {
         value = list;
      }

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

      Date date = DateUtils.truncate(dateDebut, Calendar.DATE);
      Date endDate = DateUtils.truncate(dateFin, Calendar.DATE);

      do {

         support.delete(date, clockSupport.currentCLock());
         date = DateUtils.addDays(date, 1);

      } while (date.compareTo(endDate) <= 0);

   }

}
