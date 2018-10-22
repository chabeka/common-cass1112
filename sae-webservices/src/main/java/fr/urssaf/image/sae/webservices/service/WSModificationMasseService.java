/**
 * 
 */
package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.ModificationMasse;
import fr.cirtil.www.saeservice.ModificationMasseResponse;
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;

/**
 * Service web de modification
 * 
 */
public interface WSModificationMasseService {

   /**
    * Service réalisant la modification d'un document
    * 
    * @param request
    *           objet contenant l'identifiant unique d'un document ainsi que les
    *           métadonnées à modifier
    * @param callerIP
    *           adresse IP de l'appelant
    * @return une instance de {@link ModificationMasseResponse}
    * @throws ModificationAxisFault
    *            Une exception est levée lors de la modification
    */
   ModificationMasseResponse modificationMasse(ModificationMasse request,
         String callerIP)
         throws ModificationAxisFault;

}
