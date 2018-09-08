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
    * Récupération des codes evenements par type de traces
    * 
    * @param typeTrace
    *           Trace à créer
    * @return Liste des codes Evenements
    */
   List<String> getCodeEvenementByTypeTrace(String typeTrace);

}
