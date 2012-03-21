/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;

/**
 * Item processor pour le contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleSommaireDocumentProcessor implements
      ItemProcessor<UntypedDocument, UntypedDocument> {

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument process(UntypedDocument item) throws Exception {
      // FIXME FBON - Implémentation ControleSommaireDocumentProcessor
      return null;
   }

}
