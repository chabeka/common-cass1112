package fr.urssaf.image.sae.services.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe abstraite contenant les attributs communs de toutes les
 * implementations:
 * <ul>
 * <li>{@link fr.urssaf.image.sae.services.document.SAESearchService Recherche}
 * : Implementation de recherche,</li>
 * <li>{@link fr.urssaf.image.sae.services.consultation.SAEConsultationService
 * Consultation} : Implementation de la consultation.</li>
 * </ul>
 */
public abstract class AbstractSAEServices {

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   /**
    * @return La façade de services Storage DFCE.
    */
   public final StorageDocumentService getStorageDocumentService() {
      return storageDocumentService;
   }

   /**
    * @param storageServiceProvider
    *           : La façade de services Storage DFCE.
    */
   public final void setStorageServiceProvider(final StorageDocumentService storageDocumentService) {
      this.storageDocumentService = storageDocumentService;
   }

}
