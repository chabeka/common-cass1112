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

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.JournalisationService;
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

            ident = paramService.getJournalisationEvtIdJournPrec();

            hash = paramService.getJournalisationEvtHashJournPrec();

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
      Date lastDate = DateUtils.truncate(new Date(), Calendar.DATE);
      Date firstDate;

      try {
         firstDate = paramService.getJournalisationEvtDate();
         firstDate = DateUtils.truncate(firstDate, Calendar.DATE);
         // la date stockée est la dernière date traitée. La première date à
         // traiter est donc à J+1
         firstDate = DateUtils.addDays(firstDate, 1);

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
