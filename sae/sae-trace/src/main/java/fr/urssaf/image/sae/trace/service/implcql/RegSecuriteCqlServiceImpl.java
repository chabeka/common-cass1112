/**
 *
 */
package fr.urssaf.image.sae.trace.service.implcql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegSecuriteCqlSupport;
import fr.urssaf.image.sae.trace.service.IRegSecuriteServiceCql;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Classe d'implémentation du support {@link RegSecuriteService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Service
public class RegSecuriteCqlServiceImpl extends
                                       AbstractTraceServiceCqlImpl<TraceRegSecuriteCql, TraceRegSecuriteIndexCql>
                                       implements IRegSecuriteServiceCql {

  private final TraceRegSecuriteCqlSupport support;

  private final JobClockSupport clockSupport;

  private final LoggerSupport loggerSupport;

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(RegSecuriteCqlServiceImpl.class);

  /**
   * @param support
   *          Support de la classe DAO TraceRegSecuriteDao
   * @param clockSupport
   *          JobClockSupport Cassandra
   * @param loggerSupport
   *          Support pour l'écriture des traces applicatives
   */
  @Autowired
  public RegSecuriteCqlServiceImpl(final TraceRegSecuriteCqlSupport support,
                                   final JobClockSupport clockSupport, final LoggerSupport loggerSupport) {
    super();
    this.support = support;
    this.clockSupport = clockSupport;
    this.loggerSupport = loggerSupport;
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

}
