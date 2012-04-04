/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.impl;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * implémentation du support {@link RollbackSupport}
 * 
 */
@Component
public class RollbackSupportImpl implements RollbackSupport {

   private final StorageServiceProvider serviceProvider;

   private final InterruptionTraitementConfig interruptionConfig;

   private final InterruptionTraitementMasseSupport interruptionSupport;

   /**
    * 
    * @param serviceProvider
    *           ensemble des services de DFCE
    * @param interruptionConfig
    *           configuration de l'interruption programmée du traitement de
    *           capture en masse
    * @param interruptionSupport
    *           service d'interruption programmée des traitements
    */
   @Autowired
   public RollbackSupportImpl(
         @Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider,
         @Qualifier("interruption_capture_masse") InterruptionTraitementConfig interruptionConfig,
         InterruptionTraitementMasseSupport interruptionSupport) {

      this.serviceProvider = serviceProvider;
      this.interruptionConfig = interruptionConfig;
      this.interruptionSupport = interruptionSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void rollback(final UUID identifiant)
         throws InterruptionTraitementException, DeletionServiceEx {

      interruptionTraitement();
      serviceProvider.getStorageDocumentService().deleteStorageDocument(
            identifiant);

   }

   private void interruptionTraitement() throws InterruptionTraitementException {

      DateTime currentDate = new DateTime();

      interruptionSupport.interruption(currentDate, interruptionConfig);

   }

}
