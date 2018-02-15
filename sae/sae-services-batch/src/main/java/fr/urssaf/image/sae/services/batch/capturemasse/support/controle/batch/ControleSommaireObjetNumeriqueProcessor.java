/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceRuntimeException;

/**
 * ItemProcessor pour le controle des fichiers de référence
 * 
 */
@Component
public class ControleSommaireObjetNumeriqueProcessor extends AbstractListener
      implements ItemProcessor<VirtualReferenceFile, VirtualReferenceFile> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleSommaireObjetNumeriqueProcessor.class);

   @Autowired
   private CaptureMasseControleSupport controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final VirtualReferenceFile process(VirtualReferenceFile item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      final String path = (String) getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.SOMMAIRE_FILE);

      final File sommaire = new File(path);

      final File ecdeDirectory = sommaire.getParentFile();

      controleSupport.controleFichier(item, ecdeDirectory);

      // calcul du hash
      InputStream stream = null;
      try {
         String filePath = ecdeDirectory.getAbsolutePath() + File.separator
               + "documents" + File.separator + item.getFilePath();
         stream = new FileInputStream(filePath);
         String hash = DigestUtils.shaHex(stream);

         item.setHash(hash);
         item.setTypeHash("SHA-1");

      } catch (Exception exception) {
         throw new SAECaptureServiceRuntimeException(exception);
      }

      LOGGER.debug("{} - fin", trcPrefix);

      return item;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
