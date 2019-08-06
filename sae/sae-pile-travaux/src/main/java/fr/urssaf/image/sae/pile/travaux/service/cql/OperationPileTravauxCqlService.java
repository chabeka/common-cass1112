/**
 * 
 */
package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.util.Date;

/**
 * Service centralisant les traitements sur la pile des travaux
 * 
 */
public interface OperationPileTravauxCqlService {

   /**
    * Purge la pile des travaux et l'historique associé (Suppression des jobs
    * dont la date de création est inférieur ou égale à la date max, sans tenir
    * compte des heures)
    * 
    * @param dateMax
    *           Date maximum pour laquelle supprimer les travaux
    */
   void purger(Date dateMax);

}