/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;

/**
 * Suppoprt pour l'écriture des fichiers resultats.xml en cs d'échec lors du
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
    *           exception sur un des documents du sommaire à archiver
    * @param totalDocuments
    *           nombre de documents traités au total
    */
   void writeResultatsFile(File ecdeDirectory, File sommaireFile,
         CaptureMasseSommaireDocumentException erreur, int totalDocuments);

}
