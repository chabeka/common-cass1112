package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.EtatTraitementsMasse;
import fr.cirtil.www.saeservice.EtatTraitementsMasseResponse;
import fr.urssaf.image.sae.webservices.exception.EtatTraitementsMasseAxisFault;

/**
 * Service web de capture en masse du SAE
 * 
 * 
 */
public interface WSEtatJobMasseService {

   /**
    * Service pour l'opération <b>Etat des traitements de masse</b>
    * 
    * @param request
    *           Un objet qui contient la liste des UUID dont on souhaite connaître l'état
    * @param callerIP
    *           adresse IP de l'appelant
    * @return une objet de type {@link EtatTraitementsMasseResponse}.
    * @throws EtatTraitementsMasseAxisFault
    *            Une exception est levée lors de la récupération de l'état des traitements de masse
    */
   EtatTraitementsMasseResponse etatJobMasse(EtatTraitementsMasse request,
         String callerIP) throws EtatTraitementsMasseAxisFault;


}
