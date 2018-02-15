/**
 * 
 */
package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertResponse;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;

/**
 * Service web de transfert
 * 
 */
public interface WSTransfertService {

   /**
    * Service réalisant le transfert d'un document
    * 
    * @param request
    *           Objet contenant l'identifiant unique du document à transférer
    *           
    * @return une instance de {@link TransfertResponse} contenant un message 
    * de confirmation du transfert.
    * 
    * @throws TransfertAxisFault
    *            Une exception est levée lors du transfert.
    */
   TransfertResponse transfert(Transfert request)
         throws TransfertAxisFault;

}
