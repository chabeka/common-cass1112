package fr.urssaf.image.sae.storage.dfce.validation;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

/**
 * Test les aspects pour la validation.
 * 
 * 
 */
public class InsertionServiceValidationTest extends StorageServices {
   /**
    * {@link fr.urssaf.image.sae.storage.dfce.InsertionServiceValidation#insertStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void insertStorageDocumentValidation() throws InsertionServiceEx {
      // Initialisation des jeux de données UUID
      getInsertionService().insertStorageDocument(null);
   }

}
