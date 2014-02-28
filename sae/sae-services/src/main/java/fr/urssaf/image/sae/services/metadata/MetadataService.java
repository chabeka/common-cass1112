package fr.urssaf.image.sae.services.metadata;

import java.util.List;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Service permettant de réaliser des opérations sur les métadonnées.
 */
public interface MetadataService {

   /**
    * Renvoie la liste des métadonnées qui sont mis à disposition du client.
    * 
    * @return List<MetadataReference> : Liste des métadonnées mises à
    *         disposition du client
    */
   List<MetadataReference> getClientAvailableMetadata();
}
