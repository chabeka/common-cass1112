/**
 * 
 */
package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.urssaf.image.sae.webservices.exception.SuppressionAxisFault;

/**
 * Service web de suppression
 * 
 */
public interface WSSuppressionService {

   /**
    * Service réalisant la suppression d'un document
    * 
    * @param request
    *           Objet contenant l'identifiant unique du document à supprimer
    * @return une instance de {@link SuppressionResponse}
    * @throws SuppressionAxisFault
    *            Une exception est levée lors de la suppression
    */
   SuppressionResponse suppression(Suppression request)
         throws SuppressionAxisFault;

}
