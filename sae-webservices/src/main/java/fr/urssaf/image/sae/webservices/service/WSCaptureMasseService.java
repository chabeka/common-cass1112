package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;

/**
 * Service web de capture en masse du SAE
 * 
 * 
 */
public interface WSCaptureMasseService {

   /**
    * Service pour l'opération <b>Archivage en masse</b>
    * 
    * @param request
    *           Un objet qui contient l'URI du sommaire.xml
    * @param callerIP
    *            adresse IP de l'appelant
    * @return une objet de type {@link ArchivageMasseResponse}.
    * @throws CaptureAxisFault
    *            Une exception est levée lors de l'archivage en masse.
    */
   ArchivageMasseResponse archivageEnMasse(ArchivageMasse request, String callerIP)
         throws CaptureAxisFault;
}
