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
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.AbstractTraceSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.service.RegTechniqueServiceThrift;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * @author AC75007648
 */
@Service
public class RegTechniqueServiceThriftImpl implements RegTechniqueServiceThrift {

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(JournalEvtServiceThriftImpl.class);

  private final TraceRegTechniqueSupport traceRegTechniqueSupport;

  private final JobClockSupport clockSupport;

  private final LoggerSupport loggerSupport;

  @Autowired
  public RegTechniqueServiceThriftImpl(final TraceRegTechniqueSupport traceRegTechniqueSupport,
                                       final JobClockSupport clockSupport, final LoggerSupport loggerSupport) {
    super();
    this.traceRegTechniqueSupport = traceRegTechniqueSupport;
    this.clockSupport = clockSupport;
    this.loggerSupport = loggerSupport;
  }

  @Override
  public AbstractTraceSupport<TraceRegTechnique, TraceRegTechniqueIndex> getSupport() {
    return traceRegTechniqueSupport;
  }

  @Override
  public LoggerSupport getLoggerSupport() {
    return loggerSupport;
  }

  @Override
  public JobClockSupport getClockSupport() {
    return clockSupport;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  @Override
  public TraceRegTechnique lecture(final UUID identifiant) {
    return getSupport().find(identifiant);
  }

}
