package fr.urssaf.image.sae.services.batch.transfert.support.resultats;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;

/**
 * Interface de support en cas d'echec durant le transfert de masse
 * 
 *
 */
public interface ResultatsFileEchecTransfertSupport {

   /**
    * Service permettant d'écrire un fichier resultats.xml dans l'ECDE pour les
    * traitements de transfert de masse 
    * 
    * @param ecdeDirectory
    *           Répertoire ECDE de traitement pour une capture de masse
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml du transfert de masse
    * @param erreur
    *           objet contenant toutes les erreurs
    * @param totalDocuments
    *           nombre de documents total
    * @param nbDocumentsIntegres
    *           nombre de documents intégréslors du traitement
    * @param batchModeTraitement
    *           Mode de traitement du batch
    * @param listIntDocs
    *           Liste des documents intégrés
    */
   void writeResultatsFile(File ecdeDirectory, File sommaireFile,
         CaptureMasseErreur erreur, int totalDocuments,
         int nbDocumentsIntegres, String batchModeTraitement,
         ConcurrentLinkedQueue<?> listIntDocs);

   /**
    * Service permettant l'écriture d'un fichier resultats.xml dans l'ECDE pour
    * les traitements de transfert de masse de documents virtuels en mode tout ou
    * rien en erreur
    * 
    * @param ecdeDirectory
    *           Répertoire ECDE de traitement pour un transfert de masse
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml du transfert de masse
    * @param erreur
    *           objet contenant toutes les erreurs
    * @param totalDocuments
    *           nombre de documents au total
    * @param nbDocumentsIntegres
    *           nombre de documents intégréslors du traitement
    * @param batchModeTraitement
    *           Mode de traitement du batch
    * @param listIntDocs
    *           Liste des documents intégrés
    */
   void writeVirtualResultatsFile(File ecdeDirectory, File sommaireFile,
         CaptureMasseErreur erreur, int totalDocuments,
         int nbDocumentsIntegres, String batchModeTraitement,
         ConcurrentLinkedQueue<?> listIntDocs);
}
