/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.trace.model.DfceTraceDoc;

/**
 * Service du journal de cycle de vie des archives
 * 
 */
public interface CycleVieService {

   /**
    * Recherche et renvoie la liste des traces du cycle de vie des archives
    * comprises dans l'intervalle des dates donné
    * 
    * @param dateDebut
    *           date de début de la plage de temps
    * @param dateFin
    *           date de fin de la plage de temps
    * @param limite
    *           Nombre de traces maximum à récupérer
    * @param reversed
    *           booleen indiquant si l'ordre décroissant doit etre appliqué<br>
    *           <ul>
    *           <li>true : ordre décroissant</li>
    *           <li>false : ordre croissant</li>
    *           </ul>
    * @return une liste de traces de l'historique des événements
    */
   List<DfceTraceDoc> lecture(Date dateDebut, Date dateFin, int limite,
         boolean reversed);

   /**
    * Recherche et renvoie les traces pour le document dont l'UUID est passé en
    * paramètre
    * 
    * @param docUuid
    *           Identifiant unique du document
    * @return Liste des traces DFCE concernant le document
    */
   List<DfceTraceDoc> lectureParDocument(UUID docUuid);

}
