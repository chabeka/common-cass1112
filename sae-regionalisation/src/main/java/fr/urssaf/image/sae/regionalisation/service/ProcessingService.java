package fr.urssaf.image.sae.regionalisation.service;

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
    * @param firstRecord
    *           numéro du premier enregistrement à traiter
    * @param processingCount
    *           nombre d'enregistrement à traiter
    */
   void launch(boolean updateDatas, int firstRecord, int processingCount);
}
