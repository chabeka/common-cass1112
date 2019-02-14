package fr.urssaf.image.sae.services.metadata.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.services.metadata.MetadataService;

/**
 * Classe d'implémentation du service {@link MetadataService}. Cette classe est
 * un singleton et peut être accédée via l'annotation @Autowired
 */
@Service
@Qualifier("metadataService")
public class MetadataServiceImpl implements MetadataService {

   /**
    * Liste des métadonnées mise à disposition des clients.
    */
   private final List<MetadataReference> clientAvailableMetadata = new ArrayList<MetadataReference>();

   /**
    * Constructeur
    * 
    * @param saeMetaDataService
    *           service permettant de réaliser mes opérations sur les
    *           métadonnées.
    */
   @Autowired
   public MetadataServiceImpl(final SaeMetaDataService saeMetaDataService) {
      super();
      final List<MetadataReference> allMetadatas = saeMetaDataService.findAll();
      initClientAvailableMetadata(allMetadatas);
   }

   /**
    * Methode permettant d'initialiser la liste des métadonnées mise à
    * disposition des clients
    * 
    * @param allMetadatas
    *           liste de toutes les métadonnées
    */
   private void initClientAvailableMetadata(final List<MetadataReference> allMetadatas) {
      for (MetadataReference metadata : allMetadatas) {
         if (metadata.isClientAvailable()) {
            clientAvailableMetadata.add(metadata);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataReference> getClientAvailableMetadata() {
      return clientAvailableMetadata;
   }

}
