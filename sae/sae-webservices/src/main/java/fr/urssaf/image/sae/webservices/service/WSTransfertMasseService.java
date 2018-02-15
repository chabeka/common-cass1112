package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.TransfertMasse;
import fr.cirtil.www.saeservice.TransfertMasseResponse;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;

public interface WSTransfertMasseService {
   
   /**
    * Service réalisant le <b>Transfert en masse avec hash de documents </b>
    * 
    * @param request
    *           Un objet qui contient l'URI du sommaire.xml, le hash du fichier sommaire et l'algorithme utilisé pour générer le hash
    * @param callerIP
    *            adresse IP de l'appelant
    * @return un objet de type {@link TransfertMasseResponse}.
    * de confirmation du transfert.
    * 
    * @throws TransfertAxisFault
    *            Une exception est levée lors du transfert.
    */
   TransfertMasseResponse transfertEnMasse(TransfertMasse request, String callerIP)
         throws TransfertAxisFault;

}
