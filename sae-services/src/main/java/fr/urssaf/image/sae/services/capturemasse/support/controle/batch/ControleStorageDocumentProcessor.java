/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import org.springframework.batch.item.ItemProcessor;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 * 
 */
public class ControleStorageDocumentProcessor implements
      ItemProcessor<SAEDocument, SAEDocument> {

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEDocument process(SAEDocument item) throws Exception {
      // FIXME FBON - Implémentation ControleStorageDocumentProcessor
      return null;
   }

}
