/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Ecouteur de la partie persistance des documents virtuels
 * 
 */
@Component
public class VirtualStockageListener
      extends
      AbstractStockageListener<VirtualStorageDocument, CaptureMasseVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualStockageListener.class);

   @Autowired
   private InsertionPoolThreadVirtualExecutor executor;

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(
         final JAXBElement<UntypedVirtualDocument> untypedType) {

      incrementCount();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ConcurrentLinkedQueue<UUID> getIntegratedDocuments() {

      final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> list = executor
            .getIntegratedDocuments();

      final ConcurrentLinkedQueue<UUID> listUuid = new ConcurrentLinkedQueue<UUID>();
      if (CollectionUtils.isNotEmpty(list)) {
         for (CaptureMasseVirtualDocument document : list) {
            listUuid.add(document.getUuid());
         }
      }

      return listUuid;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final AbstractPoolThreadExecutor<VirtualStorageDocument, CaptureMasseVirtualDocument> getExecutor() {
      return executor;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }
}
