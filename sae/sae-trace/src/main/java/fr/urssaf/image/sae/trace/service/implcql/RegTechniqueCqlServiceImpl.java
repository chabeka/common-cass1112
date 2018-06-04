/**
 *
 */
package fr.urssaf.image.sae.trace.service.implcql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.service.RegTechniqueServiceCql;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Classe d'implémentation du support {@link RegTechniqueService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Service
public class RegTechniqueCqlServiceImpl extends
                                        AbstractTraceServiceCqlImpl<TraceRegTechniqueCql, TraceRegTechniqueIndexCql>
                                        implements RegTechniqueServiceCql {

  private final TraceRegTechniqueCqlSupport support;

  private final JobClockSupport clockSupport;

  private final LoggerSupport loggerSupport;

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(RegTechniqueCqlServiceImpl.class);

  /**
   * Constructeur
   *
   * @param support
   *          Support de la classe DAO TraceRegTechniqueDao
   * @param clockSupport
   *          JobClockSupport Cassandra
   * @param loggerSupport
   *          Support pour l'écriture des traces applicatives
   */
  @Autowired
  public RegTechniqueCqlServiceImpl(final TraceRegTechniqueCqlSupport support,
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
  public GenericAbstractTraceCqlSupport<TraceRegTechniqueCql, TraceRegTechniqueIndexCql> getSupport() {
    return support;
  }
}
