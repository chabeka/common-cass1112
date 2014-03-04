package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;

/**
 * Service web de traitement des métadonnées.
 */
public interface WSMetadataService {

   /**
    * Renvoie la liste des métadonnées mises à disposition du client.
    * 
    * @return une instance de {@link RecuperationMetadonneesResponse}
    * @throws ErreurInterneAxisFault
    *            Une erreur est survenue lors de la récupération des métadonnées
    */
   RecuperationMetadonneesResponse recupererMetadonnees() throws ErreurInterneAxisFault;
}
