/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Ecouteur pour la partie contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleDocumentListener extends AbstractControleDocumentsListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleDocumentListener.class);

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getLogMessage() {
      return "une erreur est survenue lors des controles des documents";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

}
