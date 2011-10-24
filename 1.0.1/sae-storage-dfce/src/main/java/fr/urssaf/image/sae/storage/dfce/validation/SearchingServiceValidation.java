package fr.urssaf.image.sae.storage.dfce.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit des méthodes de validation des arguments des services de recherche
 * par aspect.
 * 
 * @author akenore
 * 
 */
@Aspect
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SearchingServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "search.code.message";

   /**
    * Valide l'argument de la méthode  {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.SearchingServiceImpl#searchStorageDocumentByLuceneCriteria(LuceneCriteria) searchStorageDocumentByLuceneCriteria}.
    * 
    * 
    * @param luceneCriteria
    *           : Le critère lucene de recherche.
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocuments  fr.urssaf.image.sae.storage.services.storagedocument..SearchingService.searchStorageDocumentByLuceneCriteria(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(luceneCriteria)")
   public final void searchStorageDocumentByLuceneCriteriaValidation(
         final LuceneCriteria luceneCriteria) {
      Validate.notNull(luceneCriteria, StorageMessageHandler.getMessage(CODE_ERROR,
            "search.by.lucene.criteria.required", "search.impact",
            "search.lucene.action"));
      Validate.notNull(luceneCriteria.getLuceneQuery(), StorageMessageHandler
            .getMessage(CODE_ERROR, "search.by.lucene.query.required",
                  "search.impact", "search.lucene.action"));
      Validate.notNull(luceneCriteria.getLuceneQuery(), StorageMessageHandler
            .getMessage(CODE_ERROR, "search.by.lucene.query.required",
                  "search.impact", "search.lucene.action"));
      if (luceneCriteria.getLimit() <= 0) {
         Validate.isTrue(false, StorageMessageHandler.getMessage(CODE_ERROR,
               "max.lucene.results.required", "max.lucene.results.impact",
               "max.lucene.results.action"));
      }
   }

   /**
    * Valide l'argument de la méthode {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.SearchingServiceImpl#searchStorageDocumentByUUIDCriteria(UUIDCriteria) searchStorageDocumentByUUIDCriteria}
    * 
    * @param uUIDCriteria
    *           : Le critère uuid.
    * 
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocument  fr.urssaf.image.sae.storage.services.storagedocument..SearchingService.searchStorageDocumentByUUIDCriteria(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(uUIDCriteria)")
   public final void searchStorageDocumentByUUIDCriteriaValidation(
         final UUIDCriteria uUIDCriteria) {
      Validate.notNull(uUIDCriteria, StorageMessageHandler.getMessage(CODE_ERROR,
            "search.uuid.required", "search.impact", "search.uuid.action"));

      Validate.notNull(uUIDCriteria.getUuid(), StorageMessageHandler.getMessage(
            CODE_ERROR, "search.uuid.action", "search.impact",
            "search.uuid.action"));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.SearchingServiceImpl#searchMetaDatasByUUIDCriteria(UUIDCriteria)
    * searchMetaDatasByUUIDCriteria}.
    * 
    * @param uUIDCriteria
    *           : Le critère uuid.
    * 
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocument  fr.urssaf.image.sae.storage.services.storagedocument..SearchingService.searchMetaDatasByUUIDCriteria(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(uUIDCriteria)")
   public final void searchMetaDatasByUUIDCriteriaValidation(
         final UUIDCriteria uUIDCriteria) {
      Validate.notNull(uUIDCriteria, StorageMessageHandler.getMessage(CODE_ERROR,
            "search.uuid.required", "search.impact", "search.uuid.action"));

      Validate.notNull(uUIDCriteria.getUuid(), StorageMessageHandler.getMessage(
            CODE_ERROR, "search.uuid.action", "search.impact",
            "search.uuid.action"));

   }
}
