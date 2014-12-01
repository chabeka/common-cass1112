/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.List;

/**
 * Services de gestion de trace destinataires
 * 
 */
public interface TraceDestinaireService {

   /**
    * Ajouter des traces dans le système de traçabilité du SAE
    * 
    * @param trace
    *           Trace à créer
    */
   List<String> getCodeEvenementByTypeTrace(String typeTrace);

}
