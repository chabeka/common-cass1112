/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.impl;

import java.net.URI;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

/**
 * Implémentation du service de capture de masse du SAE
 * 
 */
@Service
public class SAECaptureMasseServiceImpl implements SAECaptureMasseService {

   // /**
   // * Executable du traitement de capture de masse
   // */
   // @Autowired
   // private JobLauncher jobLauncher;
   //
   // /**
   // * Job de la capture de masse
   // */
   // @Autowired
   // @Qualifier("capture_masse")
   // private Job job;

   /**
    * {@inheritDoc}
    */
   @Override
   public void captureMasse(URI sommaireURL, UUID idTraitement)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException,
         CaptureMasseEcdeWriteFileException {

      // FIXME FBON - Implémentation captureMasse

   }

}
