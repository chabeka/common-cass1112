/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;

/**
 * Services du journal des événements du SAE
 */
public interface JournalEvtService {

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

  /**
   * Renvoie une trace dans un registre à partir de son identifiant
   *
   * @param identifiant
   *          identifiant de la trace
   * @return Trace correspondant à l'identifiant
   */
  TraceJournalEvt lecture(UUID identifiant);

  /**
   * Renvoie une liste de traces sur une plage de temps
   *
   * @param dateDebut
   *          date de début de la plage de temps
   * @param dateFin
   *          date de fin de la plage de temps
   * @param limite
   *          Nombre de traces maximum à récupérer
   * @param reversed
   *          booleen indiquant si l'ordre décroissant doit etre appliqué<br>
   *          <ul>
   *          <li>true : ordre décroissant</li>
   *          <li>false : ordre croissant</li>
   *          </ul>
   * @return une liste de traces contenues dans l'index
   */
  List<TraceJournalEvtIndex> lecture(Date dateDebut, Date dateFin, int limite, boolean reversed);

  /**
   * Purge les traces d'un registre sur une plage de temps
   *
   * @param date
   *          date à laquelle réaliser la purge
   */
  void purge(Date date);

  /**
   * Renvoie un indicateur de présence d'enregistrements pour la date donnée
   *
   * @param date
   *          date pour laquelle vérifier la présence d'enregistrements
   * @return un indicateur de présence de données
   */
  boolean hasRecords(Date date);

}
