/**
 *
 */
package fr.urssaf.image.sae.trace.service.implcql;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.impl.TraceRegTechniqueDaoImpl;
import fr.urssaf.image.sae.trace.daocql.impl.TraceRegTechniqueIndexCqlDaoImpl;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.RegTechniqueServiceCql;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

/**
 * Classe d'implémentation du support {@link RegTechniqueService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Service
public class RegTechniqueCqlServiceImpl implements RegTechniqueServiceCql {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegTechniqueCqlServiceImpl.class);

  private final TraceRegTechniqueCqlSupport support;

  private final LoggerSupport loggerSupport;

  private final JobClockSupport clockSupport;

  /**
   * Constructeur
   *
   * @param support
   *           Support de la classe DAO TraceRegTechniqueDao
   * @param clockSupport
   *           JobClockSupport Cassandra
   * @param loggerSupport
   *           Support pour l'écriture des traces applicatives
   */
  @Autowired
  public RegTechniqueCqlServiceImpl(final TraceRegTechniqueCqlSupport support, final JobClockSupport clockSupport, final LoggerSupport loggerSupport) {
    super();
    this.support = support;
    this.clockSupport = clockSupport;
    this.loggerSupport = loggerSupport;
  }

  public RegTechniqueCqlServiceImpl(final CassandraCQLClientFactory ccf, final JobClockSupport clockSupport) {

    final ITraceRegTechniqueCqlDao dao = new TraceRegTechniqueDaoImpl(ccf);
    dao.setCcf(ccf);
    final ITraceRegTechniqueIndexCqlDao indexDao = new TraceRegTechniqueIndexCqlDaoImpl(ccf);
    indexDao.setCcf(ccf);
    final TimeUUIDEtTimestampSupport timeUUIDSupport = new TimeUUIDEtTimestampSupport();

    final TraceRegTechniqueCqlSupport support = new TraceRegTechniqueCqlSupport(dao, indexDao, timeUUIDSupport);
    this.support = support;
    this.clockSupport = clockSupport;
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
  public GenericAbstractTraceCqlSupport<TraceRegTechniqueCql, TraceRegTechniqueIndexCql> getSupport() {
    return support;
  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.trace.service.RegTechniqueServiceCql#lecture(java.util.UUID)
   */
  @Override
  public TraceRegTechniqueCql lecture(final UUID identifiant) {
    final Optional<TraceRegTechniqueCql> traceOpt = support.find(identifiant);
    return traceOpt.orElse(null);
  }

  public JobClockSupport getClockSupport() {
    return clockSupport;
  }

}
