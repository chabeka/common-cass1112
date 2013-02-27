/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.JournalisationService;
import fr.urssaf.image.sae.trace.service.ParametersService;
import fr.urssaf.image.sae.trace.utils.XmlValidationUtils;

/**
 * Classe d'implémentation de l'interface {@link JournalisationService}. Cette
 * classe est un singleton et peut être accessible via le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 * 
 */
@Service
public class JournalisationServiceImpl implements JournalisationService {

   private static final String RESULTATS_XSD = "xsd/journal_sae.xsd";

   @Autowired
   private ParametersService paramService;

   @Autowired
   private JournalEvtService journalService;

   @Autowired
   private ApplicationContext applicationContext;

   /**
    * {@inheritDoc}
    */
   @Override
   public final String exporterTraces(JournalisationType typeJournalisation,
         String repertoire, Date date) {

      String chemin, ident, hash;

      try {
         if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {
            ident = (String) paramService.loadParameter(
                  ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT)
                  .getValue();
            hash = (String) paramService.loadParameter(
                  ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT)
                  .getValue();
            chemin = journalService.export(date, repertoire, ident, hash);

            checkFormat(chemin);

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
    * @param chemin
    */
   private void checkFormat(String chemin) {
      try {

         Resource sommaireXSD = applicationContext.getResource(RESULTATS_XSD);

         URL xsdSchema = sommaireXSD.getURL();

         XmlValidationUtils.parse(new File(chemin), xsdSchema);

      } catch (IOException exception) {
         throw new TraceRuntimeException(
               "Erreur lors de la validation XSD du fichier généré : " + chemin,
               exception);
      } catch (ParserConfigurationException exception) {
         throw new TraceRuntimeException(
               "Erreur lors de la validation XSD du fichier généré : " + chemin,
               exception);
      } catch (SAXException exception) {
         throw new TraceRuntimeException(
               "Erreur lors de la validation XSD du fichier généré : " + chemin,
               exception);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<Date> recupererDates(JournalisationType typeJournalisation) {

      // pour l'instant, il n'y a qu'un seul journal
      ParameterType type = ParameterType.JOURNALISATION_EVT_DATE;
      Date lastDate = DateUtils.truncate(new Date(), Calendar.DATE);
      Date firstDate;

      try {
         Parameter parameter = paramService.loadParameter(type);
         firstDate = (Date) parameter.getValue();
         firstDate = DateUtils.truncate(firstDate, Calendar.DATE);

      } catch (ParameterNotFoundException exception) {
         throw new TraceRuntimeException(exception);
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
