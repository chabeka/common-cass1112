package fr.urssaf.image.sae.services.batch;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;

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
    * Ajoute un traitement de capture de masse dans la pile des traitements de
    * masse en attente
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à l'enregistrement d'un
    *           traitement de masse
    * 
    */
   @PreAuthorize("hasRole('archivage_masse')")
   void ajouterJobCaptureMasse(TraitemetMasseParametres parametres);

   /**
    * Ajoute un traitement de restore de masse dans la pile des traitements de
    * masse en attente
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à l'enregistrement d'un
    *           traitement de masse
    * 
    */
   @PreAuthorize("hasRole('restore_masse')")
   void ajouterJobRestoreMasse(TraitemetMasseParametres parametres);

   /**
    * Ajoute un traitement de suppression masse dans la pile des traitements de
    * masse en attente
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à l'enregistrement d'un
    *           traitement de masse
    * 
    */
   @PreAuthorize("hasRole('suppression_masse')")
   void ajouterJobSuppressionMasse(TraitemetMasseParametres parametres);
   

   /**
    * Ajoute un traitement de modification de masse dans la pile des traitements de
    * masse en attente
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à l'enregistrement d'un
    *           traitement de masse
    * 
    */
   @PreAuthorize("hasRole('modification_masse')")
   void ajouterJobModificationMasse(TraitemetMasseParametres parametres);
   
   /**
    * Ajoute un traitement de modification de masse dans la pile des traitements de
    * masse en attente
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à l'enregistrement d'un
    *           traitement de masse
    * 
    */
   @PreAuthorize("hasRole('transfert_masse')")
   void ajouterJobTransfertMasse(TraitemetMasseParametres parametres);
   
   /**
    * Ajoute un traitement de reprise dans la pile des traitements de masse en attente
    * @param parametres
    *             contient les paramètres nécessaires à l'enregistrement de reprise
    */
   @PreAuthorize("hasRole('reprise_masse')")
   void ajouterJobReprise(TraitemetMasseParametres parametres);
   
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

   /**
    * Récupère la liste des jobs demandés
    * 
    * @param listeUuid
    *           Liste des UUID des jobs que l'on souhaite récupérer
    * @return La liste des jobs correspondants aux UUID fournis en paramètre          
    */
   List<JobRequest> recupererJobs(List<UUID> listeUuid);
   
   
   /**
    * Exécute un traitement de reprise de masse stocké dans la pile des travaux en
    * attente
    * @param jobReprise Le traitement de reprise
    * @return un objet ExitTraitement résultat
    * @throws JobParameterTypeException est levée si l'uuid du job à reprendre n'est pas renseigné
    * @throws JobInexistantException si le job à reprendre n'existe pas en base
    */
   ExitTraitement lancerReprise(JobRequest jobReprise)
         throws JobParameterTypeException, JobInexistantException;
   
}