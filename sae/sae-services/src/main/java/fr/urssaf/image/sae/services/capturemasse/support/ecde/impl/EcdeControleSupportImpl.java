/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeControleSupport;

/**
 * Implémentation du support {@link EcdeControleSupport}
 * 
 */
@Component
public class EcdeControleSupportImpl implements EcdeControleSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EcdeControleSupportImpl.class);
   
   private static final String PREFIXE_TRC = "checkEcdeWrite()";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkEcdeWrite(final File sommaireFile)
         throws CaptureMasseEcdeWriteFileException {

      LOGGER.debug("{} - Début", PREFIXE_TRC);

      final File parentFile = sommaireFile.getParentFile();
      boolean ecdePermission = false;

      // Implementation pour windows
      if (parentFile.canWrite()) {
         try {
            final File tmpfile = File.createTempFile("bulkFlagPermission",
                  ".tmp", parentFile);

            if (tmpfile.isFile() && tmpfile.exists()) {
               ecdePermission = tmpfile.delete();
            }

         } catch (IOException e) {
            LOGGER.debug("{} - Erreur IO sur le répertoire "
                  + parentFile.getAbsolutePath(), PREFIXE_TRC);
         }
      }

      if (!ecdePermission) {
         throw new CaptureMasseEcdeWriteFileException(sommaireFile
               .getAbsolutePath());
      }

      LOGGER.debug("{} - Le répertoire de traitement ECDE est {}", PREFIXE_TRC,
            parentFile.getAbsoluteFile());

      LOGGER.debug("{} - Sortie", PREFIXE_TRC);
   }

}
