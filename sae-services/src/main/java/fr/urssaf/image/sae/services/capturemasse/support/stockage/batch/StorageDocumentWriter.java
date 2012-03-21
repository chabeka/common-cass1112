/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Item writer de l'écriture des documents dans DFCE
 * 
 */
@Component
public class StorageDocumentWriter implements ItemWriter<StorageDocument> {

   /**
    * {@inheritDoc}
    */
   @Override
   public void write(List<? extends StorageDocument> items) throws Exception {
      // FIXME FBON - Implémentation de StorageDocumentWriter
      
   }

   
   
}
