package fr.urssaf.image.sae.storage.dfce.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit des méthodes de validation des arguments des services de récupération
 * par aspect.
 * 
 * @author akenore
 * 
 */
@Aspect
public class RetrievalServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "retrieve.code.message";

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RetrievalServiceImpl#retrieveStorageDocumentByUUID(UUIDCriteria)
    * retrieveStorageDocumentByUUID}.
    * 
    * 
    * @param uUIDCriteria
    *           : Le critère UUID
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocument  fr.urssaf.image.sae.storage.services.storagedocument..RetrievalService.retrieveStorageDocumentByUUID(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(uUIDCriteria)")
   public final void retrieveStorageDocumentByUUIDValidation(
         final UUIDCriteria uUIDCriteria) {
      checkNotNull(uUIDCriteria, "retrieve.document.impact",
            "retrieve.document.action");
   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RetrievalServiceImpl#retrieveStorageDocumentContentByUUID(UUIDCriteria)
    * retrieveStorageDocumentContentByUUID}.
    * 
    * @param uUIDCriteria
    *           : Le critère UUID
    */
   @Before(value = "execution( byte[] fr.urssaf.image.sae.storage.services.storagedocument..RetrievalService.retrieveStorageDocumentContentByUUID(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked)  && args(uUIDCriteria)")
   public final void retrieveStorageDocumentContentByUUIDValidation(
         final UUIDCriteria uUIDCriteria) {
      checkNotNull(uUIDCriteria, "retrieve.document.content.impact",
            "retrieve.document.action");
   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RetrievalServiceImpl#retrieveStorageDocumentMetaDatasByUUID(UUIDCriteria)
    * retrieveStorageDocumentMetaDatasByUUID}.
    * 
    * @param uUIDCriteria
    *           : Le critère UUID
    */
   @Before(value = "execution(java.util.List<fr.urssaf.image.sae.storage.model..StorageMetadata> fr.urssaf.image.sae.storage..RetrievalServiceImpl.retrieveStorageDocumentMetaDatasByUUID(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(uUIDCriteria)")
   public final void retrieveStorageDocumentMetaDatasByUUIDValidation(
         final UUIDCriteria uUIDCriteria) {
      checkNotNull(uUIDCriteria, "retrieve.document.metadonnee.impact",
            "retrieve.document.action");
   }

   /**
    * Vérifie que l'{@link UUIDCriteria} est non null.
    * @param message
    *           : Le message
    * @param uUIDCriteria
    *           : Le critère UUID
    */
   private void checkNotNull(final UUIDCriteria uUIDCriteria,
         final String... message) {
      Validate.notNull(uUIDCriteria, StorageMessageHandler.getMessage(CODE_ERROR,
            "retrieve.from.uuid.criteria.required", message[0], message[1]));
      Validate.notNull(uUIDCriteria.getUuid(), StorageMessageHandler.getMessage(
            CODE_ERROR, "retrieve.from.uuid.criteria.required", message[0],
            message[1]));

   }
}
