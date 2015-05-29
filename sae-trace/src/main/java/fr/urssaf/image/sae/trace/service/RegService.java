/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.trace.dao.model.Trace;
import fr.urssaf.image.sae.trace.dao.model.TraceIndex;

/**
 * Services communs aux registres
 * 
 * @param <T>
 *           Type de trace contenue dans le registre
 * @param <I>
 *           Index des traces
 */
public interface RegService<T extends Trace, I extends TraceIndex> {

   /**
    * Renvoie une trace dans un registre à partir de son identifiant
    * 
    * @param identifiant
    *           identifiant de la trace
    * @return Trace correspondant à l'identifiant
    */
   T lecture(UUID identifiant);

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
    * @return une liste de traces contenues dans l'index
    */
   List<I> lecture(Date dateDebut, Date dateFin, int limite, boolean reversed);

   /**
    * Purge les traces d'un registre sur une plage de temps
    * 
    * @param date
    *           date à laquelle réaliser la purge
    */
   void purge(Date date);

   /**
    * Renvoie un indicateur de présence d'enregistrements pour la date donnée
    * 
    * @param date
    *           date pour laquelle vérifier la présence d'enregistrements
    * @return un indicateur de présence de données
    */
   boolean hasRecords(Date date);

}
