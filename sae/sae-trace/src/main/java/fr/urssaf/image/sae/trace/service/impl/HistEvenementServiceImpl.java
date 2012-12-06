/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.support.HistEvenementSupport;
import fr.urssaf.image.sae.trace.service.HistEvenementService;

/**
 * Classe d'implémentation du support {@link HistEvenementService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class HistEvenementServiceImpl implements HistEvenementService {

   @Autowired
   private HistEvenementSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<String> lecture(Date dateDebut, Date dateFin, int limite,
         boolean reversed) {

      Date startDate = getGmtDate(dateDebut);
      startDate = DateUtils.truncate(startDate, Calendar.DATE);
      Date endDate = getGmtDate(dateFin);
      endDate = DateUtils.addDays(endDate, 1);
      endDate = DateUtils.truncate(endDate, Calendar.DATE);

      return support.findByDates(startDate, endDate, limite, reversed);
   }

   private Date getGmtDate(Date date) {
      long value = DateTimeZone.getDefault().convertLocalToUTC(date.getTime(),
            true);

      return new Date(value);

   }
}
