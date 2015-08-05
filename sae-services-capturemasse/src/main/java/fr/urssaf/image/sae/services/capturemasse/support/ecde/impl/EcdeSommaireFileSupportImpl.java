/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.impl;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;

/**
 * Implémentation du support {@link EcdeSommaireFileSupport}
 * 
 */
@Component
public class EcdeSommaireFileSupportImpl implements EcdeSommaireFileSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EcdeSommaireFileSupportImpl.class);

   private static final String PREFIXE_TRC = "convertURLtoFile()";

   @Autowired
   private EcdeServices ecdeServices;

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final File convertURLtoFile(final URI sommaireURI)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException {

      // Traces debug - entrée méthode

      LOGGER.debug("{} - Début", PREFIXE_TRC);
      LOGGER.debug("{} - Début des vérifications sur "
            + "l'URL ECDE envoyée au service de capture de masse", PREFIXE_TRC);
      // Fin des traces debug - entrée méthode

      File fileEcde = null;
      try {
         fileEcde = ecdeServices.convertSommaireToFile(sommaireURI);
      } catch (EcdeBadURLException e1) {
         throw new CaptureMasseSommaireEcdeURLException(sommaireURI.toString());
      } catch (EcdeBadURLFormatException e1) {
         throw new CaptureMasseSommaireEcdeURLException(sommaireURI.toString());
      }

      if (!fileEcde.exists()) {
         throw new CaptureMasseSommaireFileNotFoundException(sommaireURI
               .toString());
      }

      // Traces debug - entrée méthode
      LOGGER.debug("{} - Fin", PREFIXE_TRC);

      return fileEcde;
   }

}
