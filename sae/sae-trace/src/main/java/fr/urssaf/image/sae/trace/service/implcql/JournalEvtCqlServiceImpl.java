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
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;
import fr.urssaf.image.sae.trace.daocql.impl.TraceJournalEvtCqlDaoImpl;
import fr.urssaf.image.sae.trace.daocql.impl.TraceJournalEvtIndexCqlDaoImpl;
import fr.urssaf.image.sae.trace.daocql.impl.TraceJournalEvtIndexDocDaoCqlImpl;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.service.JournalEvtServiceCql;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.service.support.TraceFileSupport;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.TraceUtils;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class JournalEvtCqlServiceImpl implements JournalEvtServiceCql {

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(JournalEvtCqlServiceImpl.class);

  private static final String PATTERN_DATE = "yyyyMMdd";

  private final TraceJournalEvtCqlSupport supportcql;

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
  public JournalEvtCqlServiceImpl(final TraceJournalEvtCqlSupport support, final JobClockSupport clockSupport, final LoggerSupport loggerSupport,
                                  final TraceFileSupport traceFileSupport) {
    super();
    supportcql = support;
    this.loggerSupport = loggerSupport;
    this.traceFileSupport = traceFileSupport;
  }

  public JournalEvtCqlServiceImpl(final CassandraCQLClientFactory ccf) {

    final ITraceJournalEvtCqlDao dao = new TraceJournalEvtCqlDaoImpl(ccf);
    // dao.setCcf(ccf);
    final ITraceJournalEvtIndexCqlDao indexdao = new TraceJournalEvtIndexCqlDaoImpl(ccf);
    // indexdao.setCcf(ccf);
    final ITraceJournalEvtIndexDocCqlDao indexdocdao = new TraceJournalEvtIndexDocDaoCqlImpl(ccf);
    // indexdocdao.setCcf(ccf);

    final TimeUUIDEtTimestampSupport timeUUIDSupport = new TimeUUIDEtTimestampSupport();
    final TraceJournalEvtCqlSupport support = new TraceJournalEvtCqlSupport(dao, indexdao, indexdocdao, timeUUIDSupport);
    supportcql = support;
    loggerSupport = new LoggerSupport();
    traceFileSupport = new TraceFileSupport();
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
                                listTraces.size() });

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
                                .format(date) });
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
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * Récupération de la liste des traces par identifiant unique du document.
   *
   * @param idDoc
   *           Identifiant du document
   * @return Liste des traces
   */
  public final List<TraceJournalEvtIndexDocCql> getTraceJournalEvtByIdDoc(
                                                                          final UUID idDoc) {
    return supportcql.findByIdDoc(idDoc);
  }

  @Override
  public TraceJournalEvtCql lecture(final UUID identifiant) {
    final Optional<TraceJournalEvtCql> traceOpt = supportcql.find(identifiant);
    return traceOpt.orElse(null);
  }

}
