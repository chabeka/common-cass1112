package fr.urssaf.image.sae.storage.dfce.validation;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Test les aspects pour la validation.
 * 
 * @author rhofir, kenore.
 * 
 */
public class InsertionServiceValidationTest extends
      StorageServices {
   /**
    * {@link fr.urssaf.image.sae.storage.dfce.InsertionServiceValidation#insertStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void insertStorageDocumentValidation() throws InsertionServiceEx {
      // Initialisation des jeux de données UUID
      getInsertionService().insertStorageDocument(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.InsertionServiceValidation#bulkInsertStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void bulkInsertStorageDocumentValidation() throws InsertionServiceEx {
      // Initialisation des jeux de données UUID
      getInsertionService().bulkInsertStorageDocument(null, true);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.InsertionServiceValidation#bulkInsertStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void bulkInsertStorageDocumentValidList() throws InsertionServiceEx {
      // Initialisation des jeux de données UUID
      StorageDocuments storageDocuments = new StorageDocuments();
      storageDocuments.setAllStorageDocuments(null);
      getInsertionService().bulkInsertStorageDocument(storageDocuments, true);
   }
}
