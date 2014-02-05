package fr.urssaf.image.sae.documents.executable.service;

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
    */
   void identifierValiderFichiers(FormatValidationParametres parametres);
}
