/**
 * 
 */
package fr.urssaf.image.sae.jobs.executable.service;

/**
 * Service centralisant les traitements sur les jobs
 * 
 */
public interface TraitementService {

   /**
    * Purge les jobs et leur historique
    */
   void purger();
   
}
