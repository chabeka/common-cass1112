package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;

/**
 * Centralisation des traitements de la régionalisation.
 * 
 * 
 */
public interface ProcessingService {

   /**
    * Réalise le traitement de la régionalisation
    * 
    * @param updateDatas
    *           flag indiquant si le traitement est réel ou tir à blanc.
    * @param source
    *           fichier source de données
    * @param lastRecord
    *           dernier enregistrement à traiter
    * @param firstRecord
    *           premier enregistrement à traiter
    * @param uuid
    *           identifiant unique du numéro de traitement
    * @param dirPath
    *           chemin du répertoire dans lequel seront réalisés les fichiers de
    *           suivi d'execution
    */
   void launchWithFile(boolean updateDatas, File source, String uuid,
         int firstRecord, int lastRecord, String dirPath);
}
