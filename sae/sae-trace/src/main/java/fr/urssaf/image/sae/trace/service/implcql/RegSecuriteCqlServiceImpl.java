/**
 *
 */
package fr.urssaf.image.sae.trace.service.implcql;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegSecuriteCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.impl.TraceRegSecuriteCqlDaoImpl;
import fr.urssaf.image.sae.trace.daocql.impl.TraceRegSecuriteIndexCqlDaoImpl;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegSecuriteServiceCql;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

/**
 * Classe d'implémentation du support {@link RegSecuriteService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Component
public class RegSecuriteCqlServiceImpl implements RegSecuriteServiceCql {

  private final TraceRegSecuriteCqlSupport support;

  private final LoggerSupport loggerSupport;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RegSecuriteCqlServiceImpl.class);

  /**
   * @param support
   *           Support de la classe DAO TraceRegSecuriteDao
   * @param clockSupport
   *           JobClockSupport Cassandra
   * @param loggerSupport
   *           Support pour l'écriture des traces applicatives
   */
  @Autowired
  public RegSecuriteCqlServiceImpl(final TraceRegSecuriteCqlSupport support, final LoggerSupport loggerSupport) {
    super();
    this.support = support;
    this.loggerSupport = loggerSupport;
  }
  /**
   * update du bean dean le context courant de spring avec la nouvelle instance de {@link CassandraCQLClientFactory}
   * @param context
   */
  public RegSecuriteCqlServiceImpl(final CassandraCQLClientFactory ccf) {

    final ITraceRegSecuriteCqlDao dao = new TraceRegSecuriteCqlDaoImpl(ccf);
    dao.setCcf(ccf);
    final ITraceRegSecuriteIndexCqlDao indexDao = new TraceRegSecuriteIndexCqlDaoImpl(ccf);
    indexDao.setCcf(ccf);
    final TimeUUIDEtTimestampSupport timeUUIDSupport = new TimeUUIDEtTimestampSupport();

    final TraceRegSecuriteCqlSupport support = new TraceRegSecuriteCqlSupport(dao, indexDao, timeUUIDSupport);
    this.support = support;
    loggerSupport = new LoggerSupport();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Logger getLogger() {
    return LOGGER;
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
  public GenericAbstractTraceCqlSupport<TraceRegSecuriteCql, TraceRegSecuriteIndexCql> getSupport() {
    return support;
  }

  @Override
  public TraceRegSecuriteCql lecture(final UUID identifiant) {
    final Optional<TraceRegSecuriteCql> traceOpt = support.find(identifiant);
    return traceOpt.orElse(null);
  }

}
