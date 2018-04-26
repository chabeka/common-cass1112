/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Service d'indicateurs
 * 
 */
public interface StatusService {

   /**
    * renvoie un indicateur permettant de savoir si une purge est en cours
    * d'exécution
    * 
    * @param typePurge
    *           Purge dont il faut vérifier le statut
    * @return un indicateur d'exécution de la purge :<br />
    *         <ul>
    *         <li>true si la purge est en cours</li>
    *         <li>false sinon</li>
    *         </ul>
    */
   boolean isPurgeRunning(PurgeType typePurge);

   /**
    * renvoie un indicateur permettant de savoir si une journalisation est en
    * cours d'exécution
    * 
    * @param typeJournalisation
    *           Journalisation dont il faut vérifier le statut
    * @return un indicateur d'exécution de la journalisation :<br />
    *         <ul>
    *         <li>true si la purge est en cours</li>
    *         <li>false sinon</li>
    *         </ul>
    */
   boolean isJournalisationRunning(JournalisationType typeJournalisation);

   /**
    * Met à jour l'indicateur de traitement de la purge
    * 
    * @param typePurge
    *           Purge dont il faut mettre à jour le statut
    * @param value
    *           valeur du statut
    */
   void updatePurgeStatus(PurgeType typePurge, Boolean value);
   
   /**
    * Met à jour l'indicateur de traitement de la journalisation
    * 
    * @param typeJournalisation
    *           Journalisation dont il faut mettre à jour le statut
    * @param value
    *           valeur du statut
    */
   void updateJournalisationStatus(JournalisationType typeJournalisation, Boolean value);
}
