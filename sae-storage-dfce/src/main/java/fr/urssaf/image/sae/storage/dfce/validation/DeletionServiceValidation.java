package fr.urssaf.image.sae.storage.dfce.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit des méthodes de validation des arguments des services de suppression
 * par aspect.
 * 
 * @author akenore, Rhofir
 * 
 */

@Aspect
public class DeletionServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "delete.code.message";

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DeletionServiceImpl#deleteStorageDocument(UUIDCriteria)
    * deleteStorageDocument}. <br>
    * 
    * @param uuidCriteria
    *           : le critère de recherche
    */
   @Before(value = "execution(void fr.urssaf.image.sae.storage.services.storagedocument..DeletionService.deleteStorageDocument(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(uuidCriteria)")
   public final void deleteStorageDocumentValidation(
         final UUIDCriteria uuidCriteria) {

      Validate.notNull(uuidCriteria, StorageMessageHandler.getMessage(CODE_ERROR,
            "deletion.from.uuid.criteria.required", "delete.impact",
            "delete.action"));
      Validate.notNull(uuidCriteria.getUuid(), StorageMessageHandler.getMessage(
            CODE_ERROR, "deletion.from.uuid.criteria.required",
            "delete.action", "delete.action"));

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
         Integer.parseInt(StorageMessageHandler.getMessage("max.lucene.results"));
      } catch (NumberFormatException e) {
         Validate.isTrue(true, StorageMessageHandler.getMessage(CODE_ERROR,
               "max.lucene.results.required", "max.lucene.results.impact",
               "max.lucene.results.action"));
      }

   }
}
