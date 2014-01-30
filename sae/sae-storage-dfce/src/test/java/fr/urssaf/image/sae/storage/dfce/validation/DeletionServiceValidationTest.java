package fr.urssaf.image.sae.storage.dfce.validation;

import java.io.IOException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;

/**
 * Test les aspects pour la validation.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class DeletionServiceValidationTest {

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
    * {@link fr.urssaf.image.sae.storage.dfce.ValidationDeletionServiceValidation#deleteStorageDocumentValidation(fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void deleteStorageDocumentValidation() throws InsertionServiceEx,
         IOException, ParseException, DeletionServiceEx {
      // Initialisation des jeux de données UUID
      commonsServices.getDeletionService().deleteStorageDocument(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.validationDeletionServiceValidation#rollBackValidation(String)}
    * <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void rollBackValidation() throws DeletionServiceEx,
         SearchingServiceEx {
      commonsServices.getDeletionService().rollBack(null);
   }
}
