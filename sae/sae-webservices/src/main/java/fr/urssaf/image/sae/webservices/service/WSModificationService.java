/**
 * 
 */
package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationResponse;
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;

/**
 * Service web de modification
 * 
 */
public interface WSModificationService {

   /**
    * Service réalisant la modification d'un document
    * 
    * @param request
    *           objet contenant l'identifiant unique d'un document ainsi que les
    *           métadonnées à modifier
    * @return une instance de {@link ModificationResponse}
    * @throws ModificationAxisFault
    *            Une exception est levée lors de la modification
    */
   ModificationResponse modification(Modification request)
         throws ModificationAxisFault;

}
