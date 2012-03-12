package fr.urssaf.image.sae.services.batch;

import java.util.UUID;

import fr.urssaf.image.sae.services.batch.exception.JobInexistantException;

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
    * Ajoute un traitement de capture en masse dans la pile des traitements de
    * masse en attente
    * 
    * @param urlECDE
    *           URL Ecde du fichier sommaire.xml
    * @param uuid
    *           identifiant unique
    * @return identifiant du traitement dans la pile des travaux
    * 
    */
   long ajouterJobCaptureMasse(String urlECDE, UUID uuid);

   /**
    * Exécute un traitement de masse stocké dans la pile des traitements en
    * attente
    * 
    * @param idJob
    *           identifiant du traitement à lancer
    * @return code sortie du traitement, correspond à un code
    *         {@link org.springframework.batch.core.ExitStatus}
    * @throws JobInexistantException
    *            Exception levée si le job correspondant à l'idJob passé en
    *            paramètre n'existe pas
    */
   String lancerJob(long idJob) throws JobInexistantException;
}
