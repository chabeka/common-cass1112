package fr.urssaf.image.sae.webservices.service;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageResponse;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;

public interface WSDeblocageService {

   
   /**
    * Service réalisant le <b>Déblocage de job </b>
    * 
    * @param request
    *           Un objet qui contient l'uuid du job 
    * @param callerIP
    *            adresse IP de l'appelant
    * @return un objet de type {@link DeblocageResponse}.
    * de confirmation du transfert.
    * 
    * @throws DeblocageAxisFault
    *            Une exception est levée lors du déblocage.
    * @throws JobInexistantException 
    */
   @PreAuthorize("hasRole('deblocage')")
   DeblocageResponse deblocage(Deblocage request, String callerIP)
         throws DeblocageAxisFault, JobInexistantException;
}
