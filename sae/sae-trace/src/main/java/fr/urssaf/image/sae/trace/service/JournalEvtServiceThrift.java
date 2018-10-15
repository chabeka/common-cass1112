/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.support.AbstractTraceSupport;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Services du journal des événements du SAE
 */
public interface JournalEvtServiceThrift {

  String export(Date date, String repertoire, String idJournalPrecedent,
                String hashJournalPrecedent);

  /**
   * @return le support permettant la réalisation des opérations
   */
  AbstractTraceSupport<TraceJournalEvt, TraceJournalEvtIndex> getSupport();

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
  TraceJournalEvt lecture(UUID identifiant);
}