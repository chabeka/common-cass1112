package fr.urssaf.image.sae.storage.dfce.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

/**
 * Test les aspects pour la validation.
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class InsertionServiceValidationTest {

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
    * {@link fr.urssaf.image.sae.storage.dfce.InsertionServiceValidation#insertStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)}
    * <br>
    * @throws InsertionIdGedExistantEx 
    */
   @Test(expected = IllegalArgumentException.class)
   public void insertStorageDocumentValidation() throws InsertionServiceEx, InsertionIdGedExistantEx {
      // Initialisation des jeux de données UUID
      commonsServices.getInsertionService().insertStorageDocument(null);
   }

}
