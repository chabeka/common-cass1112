package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.SuppressionMasse;
import fr.cirtil.www.saeservice.SuppressionMasseResponse;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.SuppressionAxisFault;

/**
 * Service web de capture en masse du SAE
 * 
 * 
 */
public interface WSSuppressionMasseService {

   /**
    * Service pour l'opération <b>Suppression en masse</b>
    * 
    * @param request
    *           Un objet qui contient la requête pour déterminer les documents à
    *           supprimer
    * @param callerIP
    *           adresse IP de l'appelant
    * @return une objet de type {@link SuppressionMasseResponse}.
    * @throws CaptureAxisFault
    *            Une exception est levée lors de l'archivage en masse.
    */
   SuppressionMasseResponse suppressionEnMasse(SuppressionMasse request,
         String callerIP) throws SuppressionAxisFault;


}
