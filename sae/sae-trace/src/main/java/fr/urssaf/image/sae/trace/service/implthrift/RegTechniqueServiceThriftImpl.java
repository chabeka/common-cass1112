package fr.urssaf.image.sae.trace.service.implthrift;

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
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.RegTechniqueServiceThrift;
import fr.urssaf.image.sae.trace.service.impl.RegTechniqueServiceImpl;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Classe d'implémentation du support {@link RegTechniqueService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class RegTechniqueServiceThriftImpl implements RegTechniqueServiceThrift {

  private final TraceRegTechniqueSupport traceRegTechniqueSupport;

   private final JobClockSupport clockSupport;

   private final LoggerSupport loggerSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RegTechniqueServiceImpl.class);

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
