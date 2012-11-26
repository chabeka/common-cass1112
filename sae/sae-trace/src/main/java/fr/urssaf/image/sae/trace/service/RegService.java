/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.UUID;

/**
 * Services communs aux registres
 * 
 * @param <T>
 *           Type de trace contenue dans le registre
 */
public interface RegService<T> {

   /**
    * Renvoie une trace dans un registre à partir de son identifiant
    * 
    * @param identifiant
    *           identifiant de la trace
    * @return Trace correspondant à l'identifiant
    */
   T lecture(UUID identifiant);

   /**
    * Purge les traces d'un registre sur une plage de temps
    * 
    * @param dateDebut
    *           date de début de la plage de temps
    * @param dateFin
    *           date de fin de la plage de temps
    */
   void purge(Date dateDebut, Date dateFin);

}
