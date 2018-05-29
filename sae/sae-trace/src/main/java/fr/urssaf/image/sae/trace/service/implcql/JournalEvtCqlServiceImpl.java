/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.service.implcql;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.daocql.service.ITraceJournalEvtCqlService;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.service.support.TraceFileSupport;
import fr.urssaf.image.sae.trace.utils.TraceUtils;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class JournalEvtCqlServiceImpl extends
                                      AbstractTraceServiceCqlImpl<TraceJournalEvtCql, TraceJournalEvtIndexCql>
                                      implements ITraceJournalEvtCqlService<TraceJournalEvtCql, TraceIndex> {

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(JournalEvtCqlServiceImpl.class);

  private static final String PATTERN_DATE = "yyyyMMdd";

  private static final String INDENTATION = "    ";

  private static final String ERREUR_FLUX = "erreur de fermeture du flux ";

  private final TraceJournalEvtCqlSupport supportcql;

  private final JobClockSupport clockSupport;

  private final LoggerSupport loggerSupport;

  private final TraceFileSupport traceFileSupport;

  /**
   * @param support
   *          Support de la classe DAO TraceJournalEvtDao
   * @param clockSupport
   *          JobClockSupport
   * @param loggerSupport
   *          Support pour l'écriture des traces applicatives
   * @param traceFileSupport
   *          Classe de support pour la création des fichiers de traces
   */
  @Autowired
  public JournalEvtCqlServiceImpl(final TraceJournalEvtCqlSupport support, final JobClockSupport clockSupport, final LoggerSupport loggerSupport,
                                  final TraceFileSupport traceFileSupport) {
    super();
    this.supportcql = support;
    this.clockSupport = clockSupport;
    this.loggerSupport = loggerSupport;
    this.traceFileSupport = traceFileSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String export(final Date date, final String repertoire, final String idJournalPrecedent, final String hashJournalPrecedent) {

    final String trcPrefix = "export()";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    final List<TraceJournalEvtIndexCql> listTraces = getSupport().findByDate(date, null);

    String path = null;
    final String sDate = DateFormatUtils.format(date, PATTERN_DATE);
    if (CollectionUtils.isNotEmpty(listTraces)) {

      LOGGER.info(
                  "{} - Nombre de traces trouvées pour la journée du {} : {}",
                  new Object[] {
                                trcPrefix,
                                new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                                                                                 .format(date),
                                listTraces.size()});

      File file, directory;
      directory = new File(repertoire);
      try {
        file = File.createTempFile(sDate, ".xml", directory);

      }
      catch (final IOException exception) {
        throw new TraceRuntimeException(exception);
      }

      path = file.getAbsolutePath();
      TraceUtils.writeTraces(file,
                             listTraces,
                             idJournalPrecedent,
                             hashJournalPrecedent,
                             date,
                             traceFileSupport,
                             supportcql);

    } else {
      LOGGER.info("{} - Aucune trace trouvée pour la journée du {}",
                  new Object[] {
                                trcPrefix,
                                new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                                                                                 .format(date)});
    }

    LOGGER.debug(FIN_LOG, trcPrefix);
    return path;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GenericAbstractTraceCqlSupport<TraceJournalEvtCql, TraceJournalEvtIndexCql> getSupport() {
    return supportcql;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LoggerSupport getLoggerSupport() {
    return loggerSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JobClockSupport getClockSupport() {
    return clockSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Logger getLogger() {
    return LOGGER;
  }
}
