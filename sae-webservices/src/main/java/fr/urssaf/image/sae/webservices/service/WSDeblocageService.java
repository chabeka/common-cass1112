package fr.urssaf.image.sae.webservices.service;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageResponse;
import fr.cirtil.www.saeservice.TransfertMasseResponse;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;

public interface WSDeblocageService {

   
   /**
    * Service réalisant le <b>Déblocage de job </b>
    * 
    * @param request
    *           Un objet qui contient l'uuid du job et le codeTraitement 
    * @param callerIP
    *            adresse IP de l'appelant
    * @return un objet de type {@link TransfertMasseResponse}.
    * de confirmation du transfert.
    * 
    * @throws TransfertAxisFault
    *            Une exception est levée lors du transfert.
    * @throws JobInexistantException 
    */
   @PreAuthorize("hasRole('deblocage')")
   DeblocageResponse deblocage(Deblocage request, String callerIP)
         throws DeblocageAxisFault, JobInexistantException;
}
