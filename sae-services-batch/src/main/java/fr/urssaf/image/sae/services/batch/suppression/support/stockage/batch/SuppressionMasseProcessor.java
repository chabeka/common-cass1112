package fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * Item Processor pour vérifier que le document n'est pas gelé, et pour enrichir les 
 * metadonnées 'IdentifiantSuppressionMasseInterne' et 'DateMiseEnCorbeille'.
 *
 */
@Component
public class SuppressionMasseProcessor implements
      ItemProcessor<StorageDocument, StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SuppressionMasseProcessor.class);

   /**
    * Identifiant de traitement de suppression de masse.
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
      // recupere l'identifiant de suppression de masse
      this.uuid = stepExecution.getJobParameters().getString(
            Constantes.ID_TRAITEMENT_SUPPRESSION);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument process(StorageDocument item) throws Exception {
      StorageDocument retour = null;
      LOGGER.debug("Traitement du document : {}", item.getUuid().toString());

      // recupere le flag indiquant si le document est gele ou non
      boolean estGele = false;
      Object value = StorageMetadataUtils.valueObjectMetadataFinder(
            item.getMetadatas(), StorageTechnicalMetadatas.GEL.getShortCode());
      if (value != null) {
         estGele = (Boolean) value;
      }
      LOGGER.debug("Le document est il gele ? : {}", estGele);

      // pour tous les documents non gele, on fait la mise a jour par le writer
      // sinon, les autres sont skippes
      if (!estGele) {
         retour = item;

         LOGGER.debug("Enrichissement de l'identifiant de suppression pour le document : {}", 
               item.getUuid().toString());
         // dans le cas ou le document n'est pas gele
         // on va enrichir l'identifiant de suppression de masse
         // et la date de mise a la corbeille
         item.getMetadatas().add(
               new StorageMetadata(Constantes.CODE_COURT_META_ID_SUPPRESSION,
                     uuid));
         item.getMetadatas().add(
               new StorageMetadata(Constantes.CODE_COURT_META_DATE_CORBEILLE,
                     new Date()));
         item.setProcessId(uuid);
      } else {
         LOGGER.debug("Le document {} a été ignoré car il est gelé", item.getUuid().toString());
      }

      return retour;
   }
}
