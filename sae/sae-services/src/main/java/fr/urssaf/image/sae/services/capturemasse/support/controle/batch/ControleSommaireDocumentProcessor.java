/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import java.io.File;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport;

/**
 * Item processor pour le contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleSommaireDocumentProcessor extends AbstractListener
      implements ItemProcessor<UntypedDocument, UntypedDocument> {

   @Autowired
   private CaptureMasseControleSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument process(final UntypedDocument item)
         throws Exception {

      final String path = (String) getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.SOMMAIRE_FILE);

      final File sommaire = new File(path);

      final File ecdeDirectory = sommaire.getParentFile();

      support.controleSAEDocument(item, ecdeDirectory);

      return item;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
