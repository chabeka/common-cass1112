/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.verification;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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
    * @param logPresent
    *           flag indiquant si un message a destination de la prod a déjà été
    *           produit
    * @param erreurs
    *           liste des erreurs à l'origine de l'arrêt
    * @param idTraitement
    *           identifiant du traitement
    */
   void checkFinTraitement(URI sommaireURL, Integer nbreDocs,
         Integer nbreStockes, boolean logPresent, List<Throwable> erreurs,
         UUID idTraitement);

}
