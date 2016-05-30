package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.RestoreMasse;
import fr.cirtil.www.saeservice.RestoreMasseResponse;
import fr.urssaf.image.sae.webservices.exception.RestoreAxisFault;

/**
 * Service web de restore en masse du SAE
 * 
 * 
 */
public interface WSRestoreMasseService {

   /**
    * Service pour l'opération <b>Restore en masse</b>
    * 
    * @param request
    *           Un objet qui contient l'UUID du traitement de masse ayant
    *           supprimer les documents
    * @param callerIP
    *           adresse IP de l'appelant
    * @return une objet de type {@link RestoreMasseResponse}.
    * @throws RestoreAxisFault
    *            Une exception est levée lors de la restore de masse.
    */
   RestoreMasseResponse restoreEnMasse(RestoreMasse request, String callerIP)
         throws RestoreAxisFault;

}
