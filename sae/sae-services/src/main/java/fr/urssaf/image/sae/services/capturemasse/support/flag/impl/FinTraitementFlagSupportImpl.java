/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport;

/**
 * Implémentation du support {@link FinTraitementFlagSupport}
 * 
 */
@Component
public class FinTraitementFlagSupportImpl implements FinTraitementFlagSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(FinTraitementFlagSupportImpl.class);

   private static final String PREFIXE_TRC = "writeFinTraitementFlag()";

   private static final String FIN_FLAG = "fin_traitement.flag";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeFinTraitementFlag(final File ecdeDirectory) {

      LOGGER.debug("{} - Début de création du fichier ({})", PREFIXE_TRC,
            FIN_FLAG);

      final String path = ecdeDirectory.getAbsolutePath() + File.separator
            + FIN_FLAG;

      final File file = new File(path);

      try {
         file.createNewFile();
      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      LOGGER.debug("{} - Fin de création du fichier ({})", PREFIXE_TRC,
            FIN_FLAG);

   }

}
