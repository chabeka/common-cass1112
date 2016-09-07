/**
 * 
 */
package fr.urssaf.image.sae.documents.executable.service;


/**
 * Service d'indicateurs
 * 
 */
public interface StatusPurgeService {

   /**
    * renvoie un indicateur permettant de savoir si une purge est en cours
    * d'exécution
    *
    * @return un indicateur d'exécution de la purge :<br />
    *         <ul>
    *         <li>true si la purge est en cours</li>
    *         <li>false sinon</li>
    *         </ul>
    */
   boolean isPurgeRunning();

   /**
    * Met à jour l'indicateur de traitement de la purge
    * 
    * @param value
    *           valeur du statut
    */
   void updatePurgeStatus(Boolean value);

}
