/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.impl;

import java.util.UUID;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;

/**
 * implémentation du support {@link RollbackSupport}
 * 
 */
@Component
public class RollbackSupportImpl implements RollbackSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackSupportImpl.class);

   @Autowired
   private DFCEServicesManager servicesManager;

   private static final String PREFIXE_TRC = "deleteStorageDocument()";
   /**
    * {@inheritDoc}
    */
   @Override
   public final void rollback(final UUID identifiant) {

      final ServiceProvider dfceService = servicesManager.getDFCEService();

      // Traces debug - entrée méthode
      
      LOGGER.debug("{} - Début", PREFIXE_TRC);
      // Fin des traces debug - entrée méthode
      try {
         LOGGER.debug("{} - UUID à supprimer : {}", PREFIXE_TRC, identifiant);
         dfceService.getStoreService().deleteDocument(identifiant);
         LOGGER.debug("{} - Sortie", PREFIXE_TRC);
      } catch (FrozenDocumentException frozenExcept) {
         LOGGER
               .debug(
                     "{} - Une exception a été levée lors de la suppression du document : {}",
                     PREFIXE_TRC, frozenExcept.getMessage());
         throw new CaptureMasseRuntimeException("SAE-ST-DEL001", frozenExcept);
      }
   }

}
