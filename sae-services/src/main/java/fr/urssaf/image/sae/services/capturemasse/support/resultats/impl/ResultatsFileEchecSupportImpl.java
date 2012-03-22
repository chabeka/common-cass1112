/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;

/**
 * Implémentation du support {@link ResultatsFileEchecSupport}
 * 
 */
@Component
public class ResultatsFileEchecSupportImpl implements ResultatsFileEchecSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeResultatsFile(File ecdeDirectory, File sommaireFile,
         CaptureMasseSommaireDocumentException erreur) {
      // FIXME FBON - Implémentation writeResultatsFile

   }

}
