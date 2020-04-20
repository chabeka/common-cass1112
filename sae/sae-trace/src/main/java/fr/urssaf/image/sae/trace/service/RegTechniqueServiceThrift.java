/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.UUID;

import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.AbstractTraceSupport;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * @author AC75007648
 */
public interface RegTechniqueServiceThrift {

  /**
   * @return le support permettant la réalisation des opérations
   */
  AbstractTraceSupport<TraceRegTechnique, TraceRegTechniqueIndex> getSupport();

  /**
   * @return le support de log
   */
  LoggerSupport getLoggerSupport();

  /**
   * @return le support de timing des opérations
   */
  JobClockSupport getClockSupport();

  /**
   * @return le logger de la classe concernée
   */
  Logger getLogger();

  /**
   * Renvoie une trace dans un registre à partir de son identifiant
   *
   * @param identifiant
   *          identifiant de la trace
   * @return Trace correspondant à l'identifiant
   */
  TraceRegTechnique lecture(UUID identifiant);

}
