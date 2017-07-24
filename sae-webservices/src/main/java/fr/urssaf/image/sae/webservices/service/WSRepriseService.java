package fr.urssaf.image.sae.webservices.service;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.cirtil.www.saeservice.Reprise;
import fr.cirtil.www.saeservice.RepriseResponse;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.webservices.exception.RepriseAxisFault;

public interface WSRepriseService {

   
   /**
    * Service réalisant la <b>Reprise de traitement de masse en erreur </b>
    * 
    * @param request
    *           Un objet qui contient l'uuid du job en erreur
    * @param callerIP
    *            adresse IP de l'appelant
    * @return un objet de type {@link RepriseResponse}.
    * 
    * @throws RepriseAxisFault
    *            Une exception est levée lors du transfert.
    * @throws JobInexistantException 
    */
   @PreAuthorize("hasRole('reprise_masse')")
   RepriseResponse reprise(Reprise request, String callerIP)
         throws RepriseAxisFault, JobInexistantException;
}
