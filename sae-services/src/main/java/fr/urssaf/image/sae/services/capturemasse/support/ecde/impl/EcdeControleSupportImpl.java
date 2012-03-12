/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeControleSupport;

/**
 * Implémentation du support {@link EcdeControleSupport}
 * 
 */
@Component
public class EcdeControleSupportImpl implements EcdeControleSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkEcdeWrite(File sommaireFile)
         throws CaptureMasseEcdeWriteFileException {
      // FIXME FBON - implémentation checkEcdeWrite

   }

}
