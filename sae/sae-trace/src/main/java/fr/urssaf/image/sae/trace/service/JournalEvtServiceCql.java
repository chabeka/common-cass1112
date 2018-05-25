/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;

/**
 * Services du journal des événements du SAE
 */
public interface JournalEvtServiceCql extends RegService<TraceJournalEvtCql, TraceJournalEvtIndexCql> {

  /**
   * Exporte l'ensemble des traces du jour donné sous format XML
   *
   * @param date
   *          date pour laquelle réaliser l'export
   * @param repertoire
   *          répertoire dans lequel créer le fichier de journalisation
   * @param idJournalPrecedent
   *          identifiant unique correspondant au journal précédent
   * @param hashJournalPrecedent
   *          hash du journal précédent
   * @return le chemin vers le fichier contenant les traces
   */
  String export(Date date, String repertoire, String idJournalPrecedent,
                String hashJournalPrecedent);

}
