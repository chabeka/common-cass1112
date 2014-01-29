package fr.urssaf.image.sae.storage.dfce.validation;

import java.util.ArrayList;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;


/**
 * Test les aspects pour la validation.
 */
public class SearchingServiceValidationTest extends
      StorageServices {
   /**
    * {@link fr.urssaf.image.sae.storage.dfce.SearchingServiceValidation#searchStorageDocumentByUUIDCriteriaValidation(UUIDCriteria)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void searchStorageDocumentByUUIDCriteriaValidation()
         throws SearchingServiceEx {
      getSearchingService().searchStorageDocumentByUUIDCriteria(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.SearchingServiceValidation#searchStorageDocumentByLuceneCriteriaValidation(String)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void searchStorageDocumentByLuceneCriteriaValidation()
         throws SearchingServiceEx, QueryParseServiceEx {
      getSearchingService().searchStorageDocumentByLuceneCriteria(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.SearchingServiceValidation#searchStorageDocumentByLuceneCriteriaValidation(String)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void searchStorageDocumentByLuceneQueryValidation()
         throws SearchingServiceEx, QueryParseServiceEx {
      LuceneCriteria luceneCriteria = new LuceneCriteria(null, 10,
            new ArrayList<StorageMetadata>());
      getSearchingService().searchStorageDocumentByLuceneCriteria(
            luceneCriteria);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.SearchingServiceValidation#searchStorageDocumentByLuceneCriteriaValidation(String)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void searchStorageDocumentByLuceneLimitValidation()
         throws SearchingServiceEx, QueryParseServiceEx {
      LuceneCriteria luceneCriteria = new LuceneCriteria(String.format("%s:%s",
            "act", "1"), 0, new ArrayList<StorageMetadata>());
      getSearchingService().searchStorageDocumentByLuceneCriteria(
            luceneCriteria);
   }
}
