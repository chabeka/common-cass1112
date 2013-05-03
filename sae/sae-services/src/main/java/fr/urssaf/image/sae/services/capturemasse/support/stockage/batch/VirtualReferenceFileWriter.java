/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.services.capturemasse.model.SaeListVirtualReferenceFile;

/**
 * ItemWriter des fichiers de référence dans une liste d'éléments
 * 
 */
@Component
public class VirtualReferenceFileWriter implements
      ItemWriter<VirtualReferenceFile> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualReferenceFileWriter.class);

   @Autowired
   private SaeListVirtualReferenceFile saeListVirtualReferenceFile;

   /**
    * {@inheritDoc}
    */
   @Override
   public void write(List<? extends VirtualReferenceFile> items)
         throws Exception {
      String trcPrefix = "write";
      LOGGER.debug("{} - début", trcPrefix);

      for (VirtualReferenceFile item : items) {
         saeListVirtualReferenceFile.add(item);
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

}
