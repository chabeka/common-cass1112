package fr.urssaf.image.sae.storage.dfce.validation;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Test les aspects pour la validation.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class SearchingServiceValidationTest {

   @Autowired
   private CommonsServices commonsServices;

   @Before
   public void init() throws ConnectionServiceEx {
      commonsServices.initServicesParameters();
   }

   @After
   public void end() {
      commonsServices.closeServicesParameters();
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.SearchingServiceValidation#searchStorageDocumentByUUIDCriteriaValidation(UUIDCriteria)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void searchStorageDocumentByUUIDCriteriaValidation()
         throws SearchingServiceEx {
      commonsServices.getSearchingService()
            .searchStorageDocumentByUUIDCriteria(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.SearchingServiceValidation#searchStorageDocumentByLuceneCriteriaValidation(String)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void searchStorageDocumentByLuceneCriteriaValidation()
         throws SearchingServiceEx, QueryParseServiceEx {
      commonsServices.getSearchingService()
            .searchStorageDocumentByLuceneCriteria(null);
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
      commonsServices.getSearchingService()
            .searchStorageDocumentByLuceneCriteria(luceneCriteria);
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
      commonsServices.getSearchingService()
            .searchStorageDocumentByLuceneCriteria(luceneCriteria);
   }
}
