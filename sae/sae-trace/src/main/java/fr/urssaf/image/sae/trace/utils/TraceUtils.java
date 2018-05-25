/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.service.support.TraceFileSupport;
import javanet.staxutils.IndentingXMLEventWriter;

/**
 * TODO (AC75095028) Description du type
 */
public class TraceUtils {

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(TraceUtils.class);

  private static final String PATTERN_DATE = "yyyyMMdd";

  private static final String INDENTATION = "    ";

  private static final String ERREUR_FLUX = "erreur de fermeture du flux ";

  public static void writeTraces(final File file, final List<TraceJournalEvtIndexCql> listTraces,
                                 final String idJournalPrecedent, final String hashJournalPrecedent, final Date date,
                                 final TraceFileSupport traceFileSupport, final TraceJournalEvtCqlSupport support) {

    final String trcPrefix = "writeTraces()";

    FileOutputStream resultatsStream = null;
    final String resultatPath = file.getAbsolutePath();
    XMLEventWriter writer = null;

    try {
      resultatsStream = new FileOutputStream(resultatPath);
      writer = loadWriter(resultatsStream);
      final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
      LOGGER.debug("{} - Classe du XMLEventFactory: {}",
                   trcPrefix,
                   eventFactory.getClass().getName());
      final StaxUtils staxUtils = new StaxUtils(eventFactory, writer);

      traceFileSupport.ecrireEntete(staxUtils);
      traceFileSupport.ecrireInfosJournalPrecedent(staxUtils,
                                                   idJournalPrecedent,
                                                   hashJournalPrecedent);
      traceFileSupport.ecrireDate(staxUtils, date);

      traceFileSupport.ecrireBaliseDebutTraces(staxUtils);

      Optional<TraceJournalEvtCql> trace;
      if (CollectionUtils.isNotEmpty(listTraces)) {
        for (final TraceJournalEvtIndexCql evt : listTraces) {
          trace = support.find(evt.getIdentifiant());
          if (trace.isPresent()) {
            traceFileSupport.ecrireTrace(staxUtils, trace.get());
          }
        }
      }

      traceFileSupport.ecrireBalisesFin(staxUtils);

    }
    catch (final FileNotFoundException e) {
      throw new TraceRuntimeException(e);

    }
    catch (final XMLStreamException exception) {
      throw new TraceRuntimeException(exception);

    }
    finally {
      if (writer != null) {
        try {
          writer.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug(ERREUR_FLUX + resultatPath);
        }
      }

      if (resultatsStream != null) {
        try {
          resultatsStream.close();
        }
        catch (final IOException e) {
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
  private static XMLEventWriter loadWriter(final FileOutputStream resultatsStream) {

    final String trcPrefix = "loadWriter()";

    final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    LOGGER.debug("{} - Classe du XMLOutputFactory: {}",
                 trcPrefix,
                 outputFactory.getClass().getName());

    try {
      final XMLEventWriter writer = outputFactory.createXMLEventWriter(
                                                                       resultatsStream, "UTF-8");
      LOGGER.debug("{} - Classe du XMLEventWriter: {}", trcPrefix, writer
                                                                         .getClass().getName());
      final IndentingXMLEventWriter iWriter = new IndentingXMLEventWriter(writer);
      iWriter.setIndent(INDENTATION);
      return iWriter;

    }
    catch (final XMLStreamException e) {
      throw new TraceRuntimeException(e);
    }
  }

  private static TraceJournalEvt getTraceFromTraceJournalEvtCql(final TraceJournalEvtCql tCql) {
    final TraceJournalEvt trace = new TraceJournalEvt();
    trace.setContexte(tCql.getContexte());
    trace.setCodeEvt(tCql.getCodeEvt());
    trace.setContratService(tCql.getContratService());
    trace.setPagms(tCql.getPagms());
    trace.setLogin(tCql.getLogin());
    return trace;
  }

}
