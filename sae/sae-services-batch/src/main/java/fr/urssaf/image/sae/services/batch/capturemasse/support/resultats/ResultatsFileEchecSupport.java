/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;

/**
 * Support pour l'écriture des fichiers resultats.xml en cs d'échec lors du
 * traitement de capture de masse
 * 
 */
public interface ResultatsFileEchecSupport {

   /**
    * Service permettant d'écrire un fichier resultats.xml dans l'ECDE pour les
    * traitements de capture de masse sur l'archivage d'un document
    * 
    * @param ecdeDirectory
    *           Répertoire ECDE de traitement pour une capture de masse
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml de la capture de masse
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
    * les traitements de capture de masse de documents virtuels en mode tout ou
    * rien en erreur
    * 
    * @param ecdeDirectory
    *           Répertoire ECDE de traitement pour une capture de masse
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml de la capture de masse
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
