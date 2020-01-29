/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.UUID;

import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Services du registre de surveillance technique
 */
public interface RegTechniqueServiceCql {
  /**
   * @return le support permettant la réalisation des opérations
   */
  GenericAbstractTraceCqlSupport<TraceRegTechniqueCql, TraceRegTechniqueIndexCql> getSupport();

  /**
   * @return le support de log
   */
  LoggerSupport getLoggerSupport();

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
  TraceRegTechniqueCql lecture(UUID identifiant);

  JobClockSupport getClockSupport();
}
