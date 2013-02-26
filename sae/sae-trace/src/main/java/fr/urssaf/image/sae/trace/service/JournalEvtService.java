/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;

/**
 * Services du journal des événements du SAE
 * 
 */
public interface JournalEvtService extends RegService<TraceJournalEvt> {

   /**
    * Revoie une liste de traces du journal des événements sur une plage de date
    * 
    * @param dateDebut
    *           date de début de la plage de temps
    * @param dateFin
    *           date de fin de la plage de temps
    * @param limite
    *           nombre de traces maximum à récupérer
    * @param reversed
    *           booleen indiquant si l'ordre décroissant doit etre appliqué<br>
    *           <ul>
    *           <li>true : ordre décroissant</li>
    *           <li>false : ordre croissant</li>
    *           </ul>
    * @return une liste de traces du journal des événements du SAE contenues
    *         dans l'index
    */
   List<TraceJournalEvtIndex> lecture(Date dateDebut, Date dateFin, int limite,
         boolean reversed);

   /**
    * Exporte l'ensemble des traces du jour donné sous format XML
    * 
    * @param date
    *           date pour laquelle réaliser l'export
    * @param repertoire
    *           répertoire dans lequel créer le fichier de journalisation
    * @param idJournalPrecedent
    *           identifiant unique correspondant au journal précédent
    * @param hashJournalPrecedent
    *           hash du journal précédent
    * @return le chemin vers le fichier contenant les traces
    */
   String export(Date date, String repertoire, String idJournalPrecedent,
         String hashJournalPrecedent);

}
