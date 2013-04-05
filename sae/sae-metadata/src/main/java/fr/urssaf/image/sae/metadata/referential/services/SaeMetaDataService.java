package fr.urssaf.image.sae.metadata.referential.services;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Service permettant de réaliser mes opérations sur les métadonnées.
 */

public interface SaeMetaDataService {

   /**
    * Créé la métadonnée
    * 
    * @param value
    *           la médatonnée à créer
    */
   void create(MetadataReference value);

   /**
    * Modifie la métadonnée
    * 
    * @param value
    *           la métadonnée à modifier
    */
   void moify(MetadataReference value);

}
