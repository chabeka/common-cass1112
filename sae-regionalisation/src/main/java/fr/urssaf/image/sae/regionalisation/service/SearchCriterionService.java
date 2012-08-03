package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;

/**
 * Service sur les critères de recherche
 * 
 * 
 */
public interface SearchCriterionService {

   /**
    * Enregistre les critères de recherche à partir d'un fichier de type .cvs.<br>
    * 
    * @param searchCriterionCvs
    *           fichier cvs
    * @throws IOException
    *            exception levée lors de la lecture du fichier
    */
   void enregistrerSearchCriterion(File searchCriterionCvs) throws IOException;
}
