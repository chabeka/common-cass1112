package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.DocumentExistant;
import fr.cirtil.www.saeservice.DocumentExistantResponse;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.webservices.exception.DocumentExistantAxisFault;

/**
 * (AC75095351) Interface du webservice WSDocumentExistantService permettant de savoir si un document existe dans la GED
 */
public interface WSDocumentExistantService {

  DocumentExistantResponse documentExistant(DocumentExistant request) throws DocumentExistantAxisFault, SearchingServiceEx, ConnectionServiceEx;
}
