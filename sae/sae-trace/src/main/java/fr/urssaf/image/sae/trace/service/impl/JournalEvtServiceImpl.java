package fr.urssaf.image.sae.trace.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.support.AbstractTraceSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.service.support.TraceFileSupport;
import fr.urssaf.image.sae.trace.utils.StaxUtils;

/**
 * Classe d'implémentation du support {@link JournalEvtService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class JournalEvtServiceImpl extends
      AbstractTraceServiceImpl<TraceJournalEvt, TraceJournalEvtIndex> implements
      JournalEvtService {

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
    * @param support
    *           Support de la classe DAO TraceJournalEvtDao
    * @param clockSupport
    *           JobClockSupport
    * @param loggerSupport
    *           Support pour l'écriture des traces applicatives
    * @param traceFileSupport
    *           Classe de support pour la création des fichiers de traces
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

      List<TraceJournalEvtIndex> listTraces = getSupport().findByDate(date);

      String path = null;
      String sDate = DateFormatUtils.format(date, PATTERN_DATE);
      if (CollectionUtils.isNotEmpty(listTraces)) {

         LOGGER.info(
               "{} - Nombre de traces trouvées pour la journée du {} : {}",
               new Object[] {
                     trcPrefix,
                     new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                           .format(date), listTraces.size() });

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
               new Object[] {
                     trcPrefix,
                     new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                           .format(date) });
      }

      LOGGER.debug(FIN_LOG, trcPrefix);
      return path;
   }

   private void writeTraces(File file, List<TraceJournalEvtIndex> listTraces,
         String idJournalPrecedent, String hashJournalPrecedent, Date date) {

      String trcPrefix = "writeTraces()";

      FileOutputStream resultatsStream = null;
      final String resultatPath = file.getAbsolutePath();
      XMLEventWriter writer = null;

      try {
         resultatsStream = new FileOutputStream(resultatPath);
         writer = loadWriter(resultatsStream);
         XMLEventFactory eventFactory = XMLEventFactory.newInstance();
         LOGGER.debug("{} - Classe du XMLEventFactory: {}", trcPrefix,
               eventFactory.getClass().getName());
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

      String trcPrefix = "loadWriter()";

      final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      LOGGER.debug("{} - Classe du XMLOutputFactory: {}", trcPrefix,
            outputFactory.getClass().getName());

      try {
         final XMLEventWriter writer = outputFactory.createXMLEventWriter(
               resultatsStream, "UTF-8");
         LOGGER.debug("{} - Classe du XMLEventWriter: {}", trcPrefix, writer
               .getClass().getName());
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
   public final JobClockSupport getClockSupport() {
      return clockSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final LoggerSupport getLoggerSupport() {
      return loggerSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final AbstractTraceSupport<TraceJournalEvt, TraceJournalEvtIndex> getSupport() {
      return support;
   }

   /**
    * Récupération de la liste des traces par identifiant unique du document.
    * 
    * @param idDoc
    *           Identifiant du document
    * @return Liste des traces
    */
   public final List<TraceJournalEvtIndexDoc> getTraceJournalEvtByIdDoc(
         UUID idDoc) {
      return support.findByIdDoc(idDoc);
   }
}
