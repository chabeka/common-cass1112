/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Ecouteur pour la partie persistance des documents du fichier sommaire.xml
 * 
 */
@Component
public class StockageListener extends
      AbstractStockageListener<StorageDocument, CaptureMasseIntegratedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StockageListener.class);

   @Autowired
   private InsertionPoolThreadExecutor executor;

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(
         final JAXBElement<UntypedDocument> untypedType) {

      incrementCount();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ConcurrentLinkedQueue<UUID> getIntegratedDocuments() {

      final ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> list = executor
            .getIntegratedDocuments();

      final ConcurrentLinkedQueue<UUID> listUuid = new ConcurrentLinkedQueue<UUID>();
      if (CollectionUtils.isNotEmpty(list)) {
         for (CaptureMasseIntegratedDocument document : list) {
            listUuid.add(document.getIdentifiant());
         }
      }

      return listUuid;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final InsertionPoolThreadExecutor getExecutor() {
      return executor;
   }
}
