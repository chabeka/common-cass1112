package fr.urssaf.image.sae.metadata.referential.services;

import java.util.List;

import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceNotFoundException;
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
    * @throws MetadataReferenceNotFoundException
    *            la métadonnée n'est pas trouvée
    */
   void modify(MetadataReference value)
         throws MetadataReferenceNotFoundException;

   /**
    * Recherche d'une métadonnées
    * 
    * @param codeLong
    *           code long de la métadonnée recherchée
    * @return {@link MetadataReference}
    */
   MetadataReference find(String codeLong);

   /**
    * Recherche de toutes les métadonnées
    * 
    * @return List<{@link MetadataReference}>
    */
   List<MetadataReference> findAll();

   /**
    * Recherche de toutes les métadonnées recherchables
    * 
    * @return List<{@link MetadataReference}>
    */
   List<MetadataReference> findAllMetadatasRecherchables();

   /**
    * Recherche de toutes les métadonnées consultables
    * 
    * @return List<{@link MetadataReference}>
    */
   List<MetadataReference> findAllMetadatasConsultables();

}
