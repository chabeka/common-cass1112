package fr.urssaf.image.sae.documents.executable.service;

import fr.urssaf.image.sae.documents.executable.model.AddMetadatasParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;

/**
 * Service centralisant les traitements sur les documents.
 */
public interface TraitementService {

   /**
    * Réalise l'identification et / ou la validation des fichiers.
    * 
    * @param parametres
    *           Les paramètres concernant le traitement
    * @return int nombre de documents traités
    */
   int identifierValiderFichiers(FormatValidationParametres parametres);
   
   
   /**
    * Réalise l’ajout de métadonnées à un ensemble de documents
    * 
    * @param parametres : Les paramètres concernant le traitement
    */
   void addMetadatasToDocuments(AddMetadatasParametres parametres);
   
   /**
    * Réalise l’ajout de métadonnées à un ensemble de documents
    * 
    * @param parametres : Les paramètres concernant le traitement
    */
   void addMetadatasToDocumentsFromCSV(AddMetadatasParametres parametres);
}
