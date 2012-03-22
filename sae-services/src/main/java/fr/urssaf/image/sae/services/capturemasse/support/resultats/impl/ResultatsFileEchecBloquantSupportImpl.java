/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport;

/**
 * Implémentation du support {@link ResultatsFileEchecBloquantSupport}
 * 
 */
@Component
public class ResultatsFileEchecBloquantSupportImpl implements
      ResultatsFileEchecBloquantSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeResultatsFile(File ecdeDirectory,
         CaptureMasseSommaireFormatValidationException erreur) {
      // FIXME FBON - Implémentation writeResultatsFile

   }

}
