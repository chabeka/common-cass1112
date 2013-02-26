/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.JournalisationService;
import fr.urssaf.image.sae.trace.service.ParametersService;

/**
 * Classe d'implémentation de l'interface {@link JournalisationService}. Cette
 * classe est un singleton et peut être accessible via le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 * 
 */
@Service
public class JournalisationServiceImpl implements JournalisationService {

   @Autowired
   private ParametersService paramService;

   @Autowired
   private JournalEvtService journalService;

   /**
    * {@inheritDoc}
    */
   @Override
   public String exporterTraces(JournalisationType typeJournalisation,
         String repertoire, Date date) {

      String chemin, id, hash;

      try {
         if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {
            id = (String) paramService.loadParameter(
                  ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT)
                  .getValue();
            hash = (String) paramService.loadParameter(
                  ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT)
                  .getValue();
            chemin = journalService.export(date, repertoire, id, hash);

         } else {
            throw new TraceRuntimeException(
                  "type de journalisation non supporté");
         }

      } catch (ParameterNotFoundException exception) {
         throw new TraceRuntimeException(exception);
      }

      return chemin;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Date> recupererDates(JournalisationType typeJournalisation) {

      // pour l'instant, il n'y a qu'un seul journal
      ParameterType type = ParameterType.JOURNALISATION_EVT_DATE;
      Date lastDate = DateUtils.truncate(new Date(), Calendar.DATE);
      Date firstDate;

      try {
         Parameter parameter = paramService.loadParameter(type);
         firstDate = (Date) parameter.getValue();
         firstDate = DateUtils.truncate(firstDate, Calendar.DATE);

      } catch (ParameterNotFoundException exception) {
         // On commence au 01/01/2013
         Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.YEAR, 2013);
         calendar.set(Calendar.MONTH, 0); // les numéros de mois commencent à 0
         calendar.set(Calendar.DAY_OF_MONTH, 1);
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);
         firstDate = calendar.getTime();
      }

      List<Date> dates = new ArrayList<Date>();

      while (firstDate.before(lastDate)) {

         if (journalService.hasRecords(firstDate)) {
            dates.add(firstDate);
         }
         firstDate = DateUtils.addDays(firstDate, 1);
      }

      return dates;
   }

}
