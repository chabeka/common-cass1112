package fr.urssaf.image.sae.services.batch.transfert.support.resultats;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;

/**
 * Support pour l'écriture des fichiers resultats.xml pour les traitements de
 * transfert de masse
 * 
 */
public interface ResultatFileSuccessTransfertSupport {
   /**
    * Service permettant d'écrire un fichier resultats.xml dans l'ECDE pour les
    * traitements de transfert de masse en mode 'tout ou rien' ayant réussi
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
    * @param modeBatch
    *           le mode d'execution du batch (TOUT_OU_RIEN ou PARTIEL)
    */
   void writeResultatsFile(File ecdeDirectory,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs,
         int initDocCount, boolean restitutionUuids, File sommaireFile,
         String modeBatch);

   /**
    * Service permettant l'écriture d'un fichier resultats.xml dans l'ECDE pour
    * les transferts de masse de documents virtuels en mode tout ou rien ayant
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