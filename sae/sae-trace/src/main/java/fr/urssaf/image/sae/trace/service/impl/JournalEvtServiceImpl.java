/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
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
import fr.urssaf.image.sae.trace.service.support.TraceFileSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.StaxUtils;

/**
 * Classe d'implémentation du support {@link JournalEvtService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class JournalEvtServiceImpl implements JournalEvtService {

   private static final String FIN_LOG = "{} - Fin";
   private static final String DEBUT_LOG = "{} - Début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(JournalEvtServiceImpl.class);

   private static final String PATTERN_DATE = "yyyyMMdd";
   private static final String INDENTATION = "    ";
   private static final String ERREUR_FLUX = "erreur de fermeture du flux ";

   private final TraceJournalEvtSupport support;

   private final JobClockSupport clockSupport;

   private final LoggerSupport loggerSupport;

   private final TraceFileSupport traceFileSupport;

   
   /**
    * @param support Support de la classe DAO TraceJournalEvtDao
    * @param clockSupport JobClockSupport
    * @param loggerSupport Support pour l'écriture des traces applicatives
    * @param traceFileSupport Classe de support pour la création des fichiers de traces
    */
   @Autowired
   public JournalEvtServiceImpl(TraceJournalEvtSupport support,
         JobClockSupport clockSupport, LoggerSupport loggerSupport,
         TraceFileSupport traceFileSupport) {
      super();
      this.support = support;
      this.clockSupport = clockSupport;
      this.loggerSupport = loggerSupport;
      this.traceFileSupport = traceFileSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String export(Date date, String repertoire,
         String idJournalPrecedent, String hashJournalPrecedent) {

      String trcPrefix = "export()";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      List<TraceJournalEvtIndex> listTraces = support.findByDate(date);

      String path = null;
      String sDate = DateFormatUtils.format(date, PATTERN_DATE);
      if (CollectionUtils.isNotEmpty(listTraces)) {

         LOGGER.info(
               "{} - Nombre de traces trouvées pour la journée du {} : {}",
               new Object[] { trcPrefix,
                     new SimpleDateFormat("yyyy-MM-dd").format(date),
                     listTraces.size() });

         File file, directory;
         directory = new File(repertoire);
         try {
            file = File.createTempFile(sDate, ".xml", directory);

         } catch (IOException exception) {
            throw new TraceRuntimeException(exception);
         }

         path = file.getAbsolutePath();
         writeTraces(file, listTraces, idJournalPrecedent,
               hashJournalPrecedent, date);

      } else {
         LOGGER.info("{} - Aucune trace trouvée pour la journée du {}",
               new Object[] { trcPrefix,
                     new SimpleDateFormat("yyyy-MM-dd").format(date) });
      }

      LOGGER.debug(FIN_LOG, trcPrefix);
      return path;
   }

   private void writeTraces(File file, List<TraceJournalEvtIndex> listTraces,
         String idJournalPrecedent, String hashJournalPrecedent, Date date) {

      FileOutputStream resultatsStream = null;
      final String resultatPath = file.getAbsolutePath();
      XMLEventWriter writer = null;

      try {
         resultatsStream = new FileOutputStream(resultatPath);
         writer = loadWriter(resultatsStream);
         XMLEventFactory eventFactory = XMLEventFactory.newInstance();
         StaxUtils staxUtils = new StaxUtils(eventFactory, writer);

         traceFileSupport.ecrireEntete(staxUtils);
         traceFileSupport.ecrireInfosJournalPrecedent(staxUtils,
               idJournalPrecedent, hashJournalPrecedent);
         traceFileSupport.ecrireDate(staxUtils, date);

         traceFileSupport.ecrireBaliseDebutTraces(staxUtils);

         TraceJournalEvt trace;
         if (CollectionUtils.isNotEmpty(listTraces)) {
            for (TraceJournalEvtIndex evt : listTraces) {
               trace = support.find(evt.getIdentifiant());
               traceFileSupport.ecrireTrace(staxUtils, trace);
            }
         }

         traceFileSupport.ecrireBalisesFin(staxUtils);

      } catch (FileNotFoundException e) {
         throw new TraceRuntimeException(e);

      } catch (XMLStreamException exception) {
         throw new TraceRuntimeException(exception);

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
         final XMLEventWriter writer = outputFactory.createXMLEventWriter(
               resultatsStream, "UTF-8");
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
   public final List<TraceJournalEvtIndex> lecture(Date dateDebut,
         Date dateFin, int limite, boolean reversed) {
      String prefix = "lecture()";
      LOGGER.debug(DEBUT_LOG, prefix);

      List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);
      LOGGER.debug("{} - Liste des dates à regarder : {}", prefix, dates);

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
   public final TraceJournalEvt lecture(UUID identifiant) {
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
            PurgeType.PURGE_EVT, DateRegUtils.getJournee(date));
      long nbTracesPurgees = support.delete(dateIndex, clockSupport
            .currentCLock());
      loggerSupport.logPurgeJourneeFin(LOGGER, prefix,
            PurgeType.PURGE_EVT, DateRegUtils.getJournee(date),
            nbTracesPurgees);

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
   public final boolean hasRecords(Date date) {

      String trcPrefix = "hasRecords()";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      Date beginDate = DateUtils.truncate(date, Calendar.DATE);
      Date endDate = DateUtils.addDays(beginDate, 1);
      endDate = DateUtils.addMilliseconds(endDate, -1);

      List<TraceJournalEvtIndex> list = lecture(beginDate, endDate, 1, false);

      boolean hasRecords = CollectionUtils.isNotEmpty(list);

      if (!hasRecords) {
         LOGGER.info("{} - Aucune trace trouvée pour la journée du {}",
               new Object[] { trcPrefix,
                     new SimpleDateFormat("yyyy-MM-dd").format(date) });
      }

      LOGGER.debug(FIN_LOG, trcPrefix);
      return hasRecords;
   }

}
