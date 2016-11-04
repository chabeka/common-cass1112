package fr.urssaf.image.sae.services.documentExistant;

import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;

public interface SAEDocumentExistantService {

   boolean documentExistant(UUID idGed) throws  SearchingServiceEx, ConnectionServiceEx;

}
