/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.impl;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * implémentation du support {@link RollbackSupport}
 * 
 */
@Component
public class RollbackSupportImpl implements RollbackSupport {

   private final StorageServiceProvider serviceProvider;

   private final InterruptionTraitementConfig config;

   private final InterruptionTraitementMasseSupport support;

   private static final String CATCH = "AvoidCatchingThrowable";

   /**
    * 
    * @param serviceProvider
    *           ensemble des services de DFCE
    * @param config
    *           configuration de l'interruption programmée du traitement de
    *           capture en masse
    * @param support
    *           service d'interruption programmée des traitements
    */
   @Autowired
   public RollbackSupportImpl(
         @Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider,
         @Qualifier("interruption_capture_masse") InterruptionTraitementConfig config,
         InterruptionTraitementMasseSupport support) {

      this.serviceProvider = serviceProvider;
      this.config = config;
      this.support = support;
   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings(CATCH)
   @Override
   public final void rollback(final UUID identifiant)
         throws InterruptionTraitementException, DeletionServiceEx {

      interruptionTraitement();

      try {
         serviceProvider.getStorageDocumentService().deleteStorageDocument(
               identifiant);

         /* récupération des erreurs de DFCE */
      } catch (Throwable throwable) {

         throw new DeletionServiceEx("SAE-ST-DEL001", throwable.getMessage(),
               throwable);
      }

   }

   private void interruptionTraitement() throws InterruptionTraitementException {

      DateTime currentDate = new DateTime();

      support.interruption(currentDate, config);

   }

}
