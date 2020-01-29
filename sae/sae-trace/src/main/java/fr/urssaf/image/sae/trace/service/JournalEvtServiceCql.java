/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

/**
 * Services du journal des événements du SAE
 */
public interface JournalEvtServiceCql {

   String export(Date date, String repertoire, String idJournalPrecedent,
                 String hashJournalPrecedent);

   /**
    * Renvoie une trace dans un registre à partir de son identifiant
    *
    * @param identifiant
    *           identifiant de la trace
    * @return Trace correspondant à l'identifiant
    */
   TraceJournalEvtCql lecture(UUID identifiant);

   /**
    * @return le support permettant la réalisation des opérations
    */
   GenericAbstractTraceCqlSupport<TraceJournalEvtCql, TraceJournalEvtIndexCql> getSupport();

   /**
    * @return le support de log
    */
   LoggerSupport getLoggerSupport();

   /**
    * @return le logger de la classe concernée
    */
   Logger getLogger();

  JobClockSupport getClockSupport();
}
