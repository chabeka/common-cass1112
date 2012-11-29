/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;

/**
 * Services du registre de sécurité
 * 
 */
public interface RegSecuriteService extends RegService<TraceRegSecurite> {

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
    * @return une liste de traces de sécurité contenues dans l'index
    */
   List<TraceRegSecuriteIndex> lecture(Date dateDebut, Date dateFin,
         int limite, boolean reversed);

}
