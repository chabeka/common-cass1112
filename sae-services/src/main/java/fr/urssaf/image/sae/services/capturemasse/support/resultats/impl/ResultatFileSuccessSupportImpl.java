/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.impl;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport;

/**
 * Implémentation du support {@link ResultatFileSuccessSupport}
 * 
 */
@Component
public class ResultatFileSuccessSupportImpl implements
      ResultatFileSuccessSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeResultatsFile(File ecdeDirectory,
         List<CaptureMasseIntegratedDocument> integratedDocuments,
         int documentsCount) {
      // FIXME FBON - Implémentation writeResultatsFile

   }

}
