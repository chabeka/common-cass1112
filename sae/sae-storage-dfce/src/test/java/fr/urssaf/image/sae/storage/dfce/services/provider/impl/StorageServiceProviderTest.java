package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;

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
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe permettant de tester la validation par aspect du paramètre du service
 * d'insertion.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class StorageServiceProviderTest {

   @Autowired
   private CommonsServices commonsServices;
   @Autowired
   private StorageDocumentService storageDocumentService;

   @Before
   public void init() throws ConnectionServiceEx, IOException, ParseException {
      commonsServices.initStorageDocumens();
   }

   /**
    * Test de validation par aspect du paramètre du service d'insertion.
    * @throws InsertionIdGedExistantEx
    */
   @Test(expected = IllegalArgumentException.class)
   public final void storageServiceProvider() throws ConnectionServiceEx,
   InsertionServiceEx, InsertionIdGedExistantEx {
      storageDocumentService.insertStorageDocument(null);
   }

}
