/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;

/**
 * ItemProcessor permettant de transformer un objet {@link VirtualReferenceFile}
 * vers un objet {@link VirtualStorageReference}
 * 
 */
@Component
public class ConvertVirtualReferenceFileProcessor implements
      ItemProcessor<VirtualReferenceFile, VirtualStorageReference> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertVirtualReferenceFileProcessor.class);

   private StepExecution stepExecution;

   /**
    * réalisé avant le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VirtualStorageReference process(VirtualReferenceFile item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      String path = (String) stepExecution.getJobExecution()
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

}
