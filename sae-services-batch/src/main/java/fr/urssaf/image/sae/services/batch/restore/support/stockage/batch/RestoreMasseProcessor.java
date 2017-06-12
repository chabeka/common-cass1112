package fr.urssaf.image.sae.services.batch.restore.support.stockage.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Item Processor pour vérifier que le document n'est pas gelé, et pour alimenter la 
 * metadonnée 'IdentifiantRestoreMasseInterne'.
 *
 */
@Component
public class RestoreMasseProcessor implements
      ItemProcessor<StorageDocument, StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RestoreMasseProcessor.class);

   /**
    * Identifiant de traitement de restore de masse.
    */
   private String uuid;

   /**
    * initialisation avant le début du Step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {
      // recupere l'identifiant de restore de masse
      this.uuid = stepExecution.getJobParameters().getString(
            Constantes.ID_TRAITEMENT_RESTORE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument process(StorageDocument item) throws Exception {

      LOGGER.debug("Traitement du document : {}", item.getUuid().toString());

      LOGGER.debug("Enrichissement de l'identifiant de restore pour le document : {}", 
            item.getUuid().toString());
      // dans le cas ou le document n'est pas gele
      // on va enrichir l'identifiant de restore de masse
      item.getMetadatas().add(
            new StorageMetadata(Constantes.CODE_COURT_META_ID_RESTORE,
                  uuid));
      item.setProcessId(uuid);
         
      return item;
   }
}