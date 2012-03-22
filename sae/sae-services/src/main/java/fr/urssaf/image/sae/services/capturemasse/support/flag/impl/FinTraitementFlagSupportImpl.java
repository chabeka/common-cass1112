/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport;

/**
 * Implémentation du support {@link FinTraitementFlagSupport}
 * 
 */
@Component
public class FinTraitementFlagSupportImpl implements FinTraitementFlagSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeFinTraitementFlag(final File ecdeDirectory) {

      final String path = ecdeDirectory.getAbsolutePath() + File.separator
            + "fin_traitement.flag";

      final File file = new File(path);

      try {
         file.createNewFile();
      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);
      }

   }

}
