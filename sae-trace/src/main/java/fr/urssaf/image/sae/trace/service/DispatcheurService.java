/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Services du dispatcheur de la trace
 * 
 */
public interface DispatcheurService {

   /**
    * Ajouter des traces dans le système de traçabilité du SAE
    * 
    * @param trace
    *           Trace à créer
    */
   void ajouterTrace(TraceToCreate trace);

}
