/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;

/**
 * Services de l'historique des événements
 * 
 */
public interface HistEvenementService {

   /**
    * Renvoie une liste de traces sur une plage de temps
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
   List<String> lecture(Date dateDebut, Date dateFin, int limite,
         boolean reversed);

}
