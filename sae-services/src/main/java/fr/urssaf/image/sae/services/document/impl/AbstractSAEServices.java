package fr.urssaf.image.sae.services.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Classe abstraite contenant les attributs communs de toutes les
 * implementations:
 * <ul>
 * <li>{@link fr.urssaf.image.sae.services.document.SAESearchService Recherche}
 * : Implementation de recherche,</li>
 * <li>{@link fr.urssaf.image.sae.services.consultation.SAEConsultationService
 * Consultation} : Implementation de la consultation.</li>
 * </ul>
 * 
 * @author akenore,rhofir.
 * 
 */
@SuppressWarnings( { "PMD.AbstractClassWithoutAbstractMethod",
      "PMD.LongVariable" })
public abstract class AbstractSAEServices {
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;

   /**
    * @return La façade de services Storage DFCE.
    */
   public final StorageServiceProvider getStorageServiceProvider() {
      return storageServiceProvider;
   }

   /**
    * @param storageServiceProvider
    *           : La façade de services Storage DFCE.
    */
   public final void setStorageServiceProvider(
         StorageServiceProvider storageServiceProvider) {
      this.storageServiceProvider = storageServiceProvider;
   }

}
