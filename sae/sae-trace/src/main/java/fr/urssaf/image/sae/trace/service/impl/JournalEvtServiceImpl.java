/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Classe d'implémentation du support {@link JournalEvtService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class JournalEvtServiceImpl implements JournalEvtService {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(JournalEvtServiceImpl.class);

   private static final String PATTERN_DATE = "yyyyMMdd";
   private static final String INDENTATION = "    ";
   private static final String ERREUR_FLUX = "erreur de fermeture du flux ";

   @Autowired
   private TraceJournalEvtSupport support;

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private LoggerSupport loggerSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public String export(Date date, String repertoire,
         String idJournalPrecedent, String hashJournalPrecedent) {

      List<TraceJournalEvtIndex> listTraces = support.findByDate(date);

      String path = null;
      if (CollectionUtils.isNotEmpty(listTraces)) {

         File file, directory;
         directory = new File(repertoire);
         try {
            file = File.createTempFile(DateFormatUtils.format(date,
                  PATTERN_DATE), ".xml", directory);

         } catch (IOException exception) {
            throw new TraceRuntimeException(exception);
         }

         writeTraces(file, listTraces);

      }

      return path;
   }

   /**
    * @param file
    * @param listTraces
    */
   private void writeTraces(File file, List<TraceJournalEvtIndex> listTraces) {

      FileOutputStream resultatsStream = null;
      final String resultatPath = file.getAbsolutePath();
      XMLEventWriter writer = null;

      try {
         resultatsStream = new FileOutputStream(resultatPath);
         writer = loadWriter(resultatsStream);
         
         

      } catch (FileNotFoundException e) {
         throw new TraceRuntimeException(e);

      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (XMLStreamException e) {
               LOGGER.debug(ERREUR_FLUX + resultatPath);
            }
         }

         if (resultatsStream != null) {
            try {
               resultatsStream.close();
            } catch (IOException e) {
               LOGGER.debug(ERREUR_FLUX + resultatPath);
            }
         }
      }

   }

   /**
    * créé le writer pour le fichier résultats.xml
    * 
    * @param resultatsStream
    * @return
    */
   private XMLEventWriter loadWriter(final FileOutputStream resultatsStream) {

      final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

      try {
         final XMLEventWriter writer = outputFactory
               .createXMLEventWriter(resultatsStream);
         IndentingXMLEventWriter iWriter = new IndentingXMLEventWriter(writer);
         iWriter.setIndent(INDENTATION);
         return iWriter;

      } catch (XMLStreamException e) {
         throw new TraceRuntimeException(e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<TraceJournalEvtIndex> lecture(Date dateDebut, Date dateFin,
         int limite, boolean reversed) {
      String prefix = "lecture()";
      LOGGER.debug(DEBUT_LOG, prefix);

      List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);

      List<TraceJournalEvtIndex> value = null;
      List<TraceJournalEvtIndex> list;
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
   public TraceJournalEvt lecture(UUID identifiant) {
      return support.find(identifiant);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void purge(Date date) {
      String prefix = "purge()";
      LOGGER.debug(DEBUT_LOG, prefix);

      Date dateIndex = DateUtils.truncate(date, Calendar.DATE);

      loggerSupport.logPurgeJourneeDebut(LOGGER, prefix,
            PurgeType.PURGE_EXPLOITATION, DateRegUtils.getJournee(date));
      long nbTracesPurgees = support.delete(dateIndex, clockSupport
            .currentCLock());
      loggerSupport.logPurgeJourneeFin(LOGGER, prefix,
            PurgeType.PURGE_EXPLOITATION, DateRegUtils.getJournee(date),
            nbTracesPurgees);
      date = DateUtils.addDays(date, 1);

      LOGGER.debug(FIN_LOG, prefix);

   }

   private List<TraceJournalEvtIndex> findNormalOrder(List<Date> dates,
         int limite) {
      int index = 0;
      int countLeft = limite;
      List<TraceJournalEvtIndex> result;
      List<TraceJournalEvtIndex> values = new ArrayList<TraceJournalEvtIndex>();
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

   private List<TraceJournalEvtIndex> findReversedOrder(List<Date> dates,
         int limite) {

      int index = dates.size() - 1;
      int countLeft = limite;
      List<TraceJournalEvtIndex> result;
      List<TraceJournalEvtIndex> values = new ArrayList<TraceJournalEvtIndex>();
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
   public boolean hasRecords(Date date) {

      Date beginDate = DateUtils.truncate(date, Calendar.DATE);
      Date endDate = DateUtils.addDays(beginDate, 1);
      endDate = DateUtils.addMilliseconds(endDate, -1);

      List<TraceJournalEvtIndex> list = lecture(beginDate, endDate, 1, false);

      boolean hasRecords = CollectionUtils.isNotEmpty(list);

      return hasRecords;
   }

}
