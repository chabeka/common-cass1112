/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;

/**
 * ItemProcessor permettant de transformer un objet {@link VirtualReferenceFile}
 * vers un objet {@link VirtualStorageReference}
 * 
 */
@Component
public class ConvertVirtualReferenceFileProcessor extends AbstractListener
      implements ItemProcessor<VirtualReferenceFile, VirtualStorageReference> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertVirtualReferenceFileProcessor.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public final VirtualStorageReference process(VirtualReferenceFile item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      String path = (String) getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.SOMMAIRE_FILE);
      File sommaire = new File(path);
      File ecdeDirectory = sommaire.getParentFile();
      String filePath = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + item.getFilePath();

      VirtualStorageReference reference = new VirtualStorageReference();
      reference.setFilePath(filePath);

      LOGGER.debug("{} - fin", trcPrefix);
      return reference;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
