/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * implémentation du support {@link RollbackSupport}
 * 
 */
@Component
public class RollbackSupportImpl implements RollbackSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackSupportImpl.class);

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   private static final String PREFIXE_TRC = "deleteStorageDocument()";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void rollback(final UUID identifiant) {

      try {
         serviceProvider.getStorageDocumentService().deleteStorageDocument(
               identifiant);
      } catch (DeletionServiceEx deleteExcept) {
         LOGGER
               .debug(
                     "{} - Une exception a été levée lors de la suppression du document : {}",
                     PREFIXE_TRC, deleteExcept.getMessage());
         throw new CaptureMasseRuntimeException("SAE-ST-DEL001", deleteExcept);
      }

   }

}
