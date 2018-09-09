/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.verification;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;

/**
 * Ce service permet de réaliser les vérifications finales, une fois le job fini
 * 
 */
public interface VerificationSupport {

   /**
    * vérifie que les différents fichiers existent, et que les logs sont bien
    * présent, le cas échéant et créé tous les éléments manquants le cas
    * échéant.
    * 
    * @param sommaireURL
    *           url du fichier ECDE
    * @param nbreDocs
    *           nombre de documents du fichier sommaire.xml
    * @param nbreStockes
    *           nombre de documents qui ont été intégrés
    * @param batchModeTraitement
    *           Mode de traitement pour le batch
    * @param logPresent
    *           flag indiquant si un message a destination de la prod a déjà été
    *           produit
    * @param erreurs
    *           liste des erreurs à l'origine de l'arrêt
    * @param idTraitement
    *           identifiant du traitement
    * @param listeDocsIntegres
    *           Liste des documents intégrés
    * @param typeJob
    *           Le type du job qui demande le check
    */
   void checkFinTraitement(URI sommaireURL, Integer nbreDocs,
         Integer nbreStockes, String batchModeTraitement, boolean logPresent,
         List<Throwable> erreurs, UUID idTraitement,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> listeDocsIntegres,
         TYPES_JOB typeJob);

}
