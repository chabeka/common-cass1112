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
