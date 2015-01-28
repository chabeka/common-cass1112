package fr.urssaf.image.sae.storage.dfce.validation;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Fournit des méthodes de validation des arguments des services de suppression
 * par aspect.
 */
@Aspect
public class UpdateServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "delete.code.message";

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DeletionServiceImpl#deleteStorageDocument(java.util.UUID)
    * ) deleteStorageDocument}. <br>
    * 
    * @param uuid
    *           : le critère de recherche
    * @param modifMetas
    *           Liste des métas à modifier
    * @param delMetas
    *           Liste des métas à modifer
    */
   @Before(value = "execution(void fr.urssaf.image.sae.storage.services.storagedocument..UpdateService.updateStorageDocument(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(uuid, modifMetas, delMetas)")
   public final void deleteStorageDocumentValidation(final UUID uuid,
         List<StorageMetadata> modifMetas, List<StorageMetadata> delMetas) {

      Validate.notNull(uuid, StorageMessageHandler.getMessage(CODE_ERROR,
            "update.uuid.required", "update.impact", "update.action"));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DeletionServiceImpl#rollBack(String)
    * rollBack}. <br>
    * 
    * @param processId
    *           : L'identifiant du traitement
    */
   @Before(value = "execution(void fr.urssaf.image.sae.storage.services.storagedocument..DeletionService.rollBack(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(processId)")
   public final void rollBackValidation(final String processId) {
      Validate.notNull(processId, StorageMessageHandler.getMessage(CODE_ERROR,
            "rollback.processId.required", "rollback.processId.impact",
            "rollback.processId.action"));
      try {
         Integer.parseInt(StorageMessageHandler
               .getMessage("max.lucene.results"));
      } catch (NumberFormatException e) {
         Validate.isTrue(true, StorageMessageHandler.getMessage(CODE_ERROR,
               "max.lucene.results.required", "max.lucene.results.impact",
               "max.lucene.results.action"));
      }

   }
}
