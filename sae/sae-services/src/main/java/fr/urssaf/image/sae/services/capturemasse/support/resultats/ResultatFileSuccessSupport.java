/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;

/**
 * Support pour l'écriture des fichiers resultats.xml pour les traitements de
 * capture de masse
 * 
 */
public interface ResultatFileSuccessSupport {

   /**
    * Service permettant d'écrire un fichier resultats.xml dans l'ECDE pour les
    * traitements de capture de masse en mode 'tout ou rien' ayant réussi
    * 
    * @param ecdeDirectory
    *           répertoire ECDE de traitement pour une capture de masse
    * @param integDocs
    *           liste des documents persistés dans DFCE
    * @param initDocCount
    *           nombre de documents initial
    * @param restitutionUuids
    *           booléen pour ajouter ou non la liste des documents intégrés avec
    *           leur UUID dans le resultat.xml
    * @param sommaireFile fichier sommaire.xml          
    */
   void writeResultatsFile(File ecdeDirectory,
         ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> integDocs, int initDocCount,
         boolean restitutionUuids, File sommaireFile);

}
