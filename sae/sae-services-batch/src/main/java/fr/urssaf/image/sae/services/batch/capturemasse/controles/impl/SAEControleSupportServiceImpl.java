/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.controles.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.util.WriteUtils;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;

/**
 * Implémentation du support {@link SAEControleSupportService}
 * 
 */
@Component
public class SAEControleSupportServiceImpl implements SAEControleSupportService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAEControleSupportServiceImpl.class);

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

      ecdePermission = checkEcde(parentFile);

      if (!ecdePermission) {
         throw new CaptureMasseEcdeWriteFileException(sommaireFile
               .getAbsolutePath());
      }

      LOGGER.debug("{} - Le répertoire de traitement ECDE est {}", PREFIXE_TRC,
            parentFile.getAbsoluteFile());

      LOGGER.debug("{} - Sortie", PREFIXE_TRC);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void checkHash(File sommaire, String hash, String typeHash)
         throws CaptureMasseSommaireTypeHashException,
         CaptureMasseSommaireHashException {
      if (Constantes.ALGO_HASH.contains(typeHash)) {
    	  int indexAlgo = Constantes.ALGO_HASH.indexOf(typeHash);
         // récupération du contenu pour le calcul du HASH
         FileInputStream content;
         try {
            content = FileUtils.openInputStream(sommaire);
         } catch (IOException e) {
            throw new CaptureMasseRuntimeException(e);
         }
         // calcul du Hash
         //String hashCode = StringUtils.EMPTY;
         String hashCalculated = StringUtils.EMPTY;
         try {
            //hashCode = DigestUtils.shaHex(content);
            hashCalculated = HashUtils.hashHex(content, Constantes.ALGO_HASH.get(indexAlgo));
         } catch (NoSuchAlgorithmException | IOException e) {
            throw new CaptureMasseSommaireHashException(hash, hashCalculated,
                  typeHash);
         } finally {
            try {
               content.close();
            } catch (IOException e) {
               LOGGER.debug("impossible de fermer le flux du fichier sommaire.xml");
            }
         }

         // comparaison avec la valeur attendu
         if (!StringUtils.equalsIgnoreCase(hashCalculated, hash.trim())) {
            throw new CaptureMasseSommaireHashException(hash, hashCalculated,
                  typeHash);
         }
      } else {
         throw new CaptureMasseSommaireTypeHashException(typeHash);
      }
   }

   private boolean checkEcde(File parentFile) {

      boolean ecdePermission = false;

      if (parentFile.canWrite()) {
         try {
            final File tmpfile = File.createTempFile("bulkFlagPermission",
                  ".tmp", parentFile);

            WriteUtils.writeFile(tmpfile, null, null);

            if (tmpfile.isFile() && tmpfile.exists()) {
               ecdePermission = tmpfile.delete();
            }

         } catch (IOException e) {
            LOGGER
                  .info("impossible de réaliser la création du fichier, droits insuffisants");
         }
      }

      return ecdePermission;
   }

}
