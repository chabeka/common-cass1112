/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeControleSupport;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceRuntimeException;

/**
 * Implémentation du support {@link EcdeControleSupport}
 * 
 */
@Component
public class EcdeControleSupportImpl implements EcdeControleSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EcdeControleSupportImpl.class);

   private static final String PREFIXE_TRC = "checkEcdeWrite()";
   
   @Autowired
   private SAEControleSupportService controlService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkEcdeWrite(final File sommaireFile)
         throws CaptureMasseEcdeWriteFileException {

      LOGGER.debug("{} - Début", PREFIXE_TRC);

      final File parentFile = sommaireFile.getParentFile();
     // boolean ecdePermission = false;

      // Implementation pour windows
      controlService.checkEcdeWrite(parentFile);
      
    /*  if (parentFile.canWrite()) {
         try {
            final File tmpfile = File.createTempFile("bulkFlagPermission",
                  ".tmp", parentFile);

            if (tmpfile.isFile() && tmpfile.exists()) {
               ecdePermission = tmpfile.delete();
            }

         } catch (IOException e) {
            throw new CaptureMasseRuntimeException(e);
         }
      }

      if (!ecdePermission) {
         throw new CaptureMasseEcdeWriteFileException(sommaireFile
               .getAbsolutePath());
      }

      LOGGER.debug("{} - Le répertoire de traitement ECDE est {}", PREFIXE_TRC,
            parentFile.getAbsoluteFile());

      LOGGER.debug("{} - Sortie", PREFIXE_TRC);*/
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void checkHash(File sommaire, String hash, String typeHash)
         throws CaptureMasseSommaireTypeHashException,
         CaptureMasseSommaireHashException {

      if ("SHA-1".equals(typeHash)) {

         // récupération du contenu pour le calcul du HASH
         byte[] content;
         try {
            content = FileUtils.readFileToByteArray(sommaire);
         } catch (IOException e) {
            throw new SAECaptureServiceRuntimeException(e);
         }
         // calcul du Hash
         String hashCode = DigestUtils.shaHex(content);

         // comparaison avec la valeur attendu
         if (!StringUtils.equalsIgnoreCase(hashCode, hash.trim())) {
            throw new CaptureMasseSommaireHashException(hash, hashCode,
                  typeHash);
         }
      } else {
         throw new CaptureMasseSommaireTypeHashException(typeHash);
      }
   }

}
