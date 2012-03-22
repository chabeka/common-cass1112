/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;

/**
 * Implémentation du support {@link SommaireFormatValidationSupport}
 * 
 */
@Component
public class SommaireFormatValidationSupportImpl implements
      SommaireFormatValidationSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void validationSommaire(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {
      // FIXME FBON - Implémentation validationSommaire

   }

}
