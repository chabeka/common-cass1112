/**
 *
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.support.AbstractTraceSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegSecuriteServiceThrift;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Classe d'implémentation du support {@link RegSecuriteService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Service
public class RegSecuriteThriftServiceImpl implements RegSecuriteServiceThrift {

  private final TraceRegSecuriteSupport support;

  private final JobClockSupport clockSupport;

  private final LoggerSupport loggerSupport;

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(RegSecuriteThriftServiceImpl.class);

  /**
   * @param support
   *          Support de la classe DAO TraceRegSecuriteDao
   * @param clockSupport
   *          JobClockSupport Cassandra
   * @param loggerSupport
   *          Support pour l'écriture des traces applicatives
   */
  @Autowired
  public RegSecuriteThriftServiceImpl(final TraceRegSecuriteSupport support,
                                      final JobClockSupport clockSupport, final LoggerSupport loggerSupport) {
    super();
    this.support = support;
    this.clockSupport = clockSupport;
    this.loggerSupport = loggerSupport;
  }

  /**
   * {@inheritDoc}
   */
  public JobClockSupport getClockSupport() {
    return clockSupport;
  }

  /**
   * {@inheritDoc}
   */
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * {@inheritDoc}
   */
  public LoggerSupport getLoggerSupport() {
    return loggerSupport;
  }

  /**
   * {@inheritDoc}
   */
  public AbstractTraceSupport<TraceRegSecurite, TraceRegSecuriteIndex> getSupport() {
    return support;
  }

  public TraceRegSecurite lecture(final UUID identifiant) {
    return getSupport().find(identifiant);
  }

}
