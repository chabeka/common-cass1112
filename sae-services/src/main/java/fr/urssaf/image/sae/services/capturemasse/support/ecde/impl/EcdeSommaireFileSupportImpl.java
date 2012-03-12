/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.impl;

import java.io.File;
import java.net.URI;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;

/**
 * Implémentation du support {@link EcdeSommaireFileSupport}
 * 
 */
@Component
public class EcdeSommaireFileSupportImpl implements EcdeSommaireFileSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final File convertURLtoFile(URI sommaireURL)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException {
      // FIXME FBON - Implémentation convertURLtoFile
      return null;
   }

}
