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
    * @param idTraitement
    *           identifiant unique du numéro de traitement
    * @param firstRecord
    *           premier enregistrement à traiter
    * @param lastRecord
    *           dernier enregistrement à traiter
    * @param dirPath
    *           chemin du répertoire dans lequel seront réalisés les fichiers de
    *           suivi d'execution
    * @return le nombre de documents mis à jour
    * 
    */
   int launchWithFile(boolean updateDatas, File source, String idTraitement,
         int firstRecord, int lastRecord, String dirPath);
}
