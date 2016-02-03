package fr.urssaf.image.sae.services.batch;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;

/**
 * Service des traitements de masse.<br>
 * <ul>
 * <li>ajout d'un traitement de masse dans la pile des traitements en attente</li>
 * <li>exécution d'un traitement de masse</li>
 * </ul>
 * 
 * 
 */
public interface TraitementAsynchroneService {

   /**
    * Ajoute un traitement de masse dans la pile des traitements de
    * masse en attente
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à l'enregistrement d'un
    *           traitement de masse
    * 
    */
   //@PreAuthorize("hasRole('traitement_masse')")
   //@PreAuthorize("hasAnyRole('archivage_masse', 'suppression_masse', 'restore_masse')")
   void ajouterJob(TraitemetMasseParametres parametres);

   /**
    * Exécute un traitement de masse stocké dans la pile des traitements en
    * attente
    * 
    * @param idJob
    *           identifiant du traitement à lancer
    * @throws JobInexistantException
    *            Exception levée si le job correspondant à l'idJob passé en
    *            paramètre n'existe pas
    * @throws JobNonReserveException
    *            Exception levée si le job correspondant à l'idJob passé en
    *            paramètre n'a pas été réservé
    */
   void lancerJob(UUID idJob) throws JobInexistantException,
         JobNonReserveException;
}