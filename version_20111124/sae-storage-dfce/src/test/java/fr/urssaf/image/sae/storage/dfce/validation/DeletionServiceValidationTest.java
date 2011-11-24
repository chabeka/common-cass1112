package fr.urssaf.image.sae.storage.dfce.validation;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;

/**
 * Test les aspects pour la validation.
 * 
 * @author rhofir, kenore.
 * 
 */
public class DeletionServiceValidationTest extends
      StorageServices {
   /**
    * {@link fr.urssaf.image.sae.storage.dfce.ValidationDeletionServiceValidation#deleteStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void deleteStorageDocumentValidation() throws InsertionServiceEx,
         IOException, ParseException, DeletionServiceEx {
      // Initialisation des jeux de données UUID
      getDeletionService().deleteStorageDocument(null);
   }
   /**
    * {@link fr.urssaf.image.sae.storage.dfce.validationDeletionServiceValidation#rollBackValidation(String)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void rollBackValidation() throws DeletionServiceEx, SearchingServiceEx {
      getDeletionService().rollBack(null);
   }
}
