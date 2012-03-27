/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * ItemProcessor renseignant la métadonnée IdTraitementMasseInterne pour le
 * StorageDocument
 */
@Component
public class EnrichissementStorageDocumentProcessor implements
      ItemProcessor<StorageDocument, StorageDocument> {

   @Autowired
   private EnrichissementStorageDocumentSupport support;

   private String uuid;

   /**
    * initialisation avant le début du Step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {
      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      this.uuid = context.getString(Constantes.ID_TRAITEMENT);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument process(StorageDocument item) throws Exception {

      return support.enrichirDocument(item, uuid);

   }

}
