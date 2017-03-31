/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;

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
    * @param sommaireFile
    *           fichier sommaire.xml
    */
   void writeResultatsFile(File ecdeDirectory,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs,
         int initDocCount, boolean restitutionUuids, File sommaireFile);

   /**
    * Service permettant l'écriture d'un fichier resultats.xml dans l'ECDE pour
    * les captures de masse de documents virtuels en mode tout ou rien ayant
    * réussi
    * 
    * @param ecdeDirectory
    *           répertoire ECDE de traitement pour une capture de masse
    * @param integDocs
    *           liste des documents virtuels persistés dans DFCE
    * @param initDocCount
    *           nombre de documents initial
    * @param restitutionUuids
    *           indicateur permettant de savoir s'il faut restituer les
    *           identifiants des documents intégrés
    * @param sommaireFile
    *           fichier sommaire.xml
    */
   void writeVirtualResultatsFile(File ecdeDirectory,
         ConcurrentLinkedQueue<CaptureMasseVirtualDocument> integDocs,
         int initDocCount, boolean restitutionUuids, File sommaireFile);

}
