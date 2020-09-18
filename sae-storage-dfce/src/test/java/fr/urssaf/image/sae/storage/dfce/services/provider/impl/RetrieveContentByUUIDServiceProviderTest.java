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
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import org.junit.Assert;

/**
 * Classe permettant de test la récupération du contenue d'un document en base.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class RetrieveContentByUUIDServiceProviderTest {

   @Autowired
   private CommonsServices commonsServices;
   @Autowired
   private StorageDocumentService storageDocumentService;

   @Before
   public void init() throws ConnectionServiceEx, IOException, ParseException {
      commonsServices.initStorageDocumens();
   }

   // Ici on test la récupération du contenu du document
   @Test
   public final void retrieveContent() throws ConnectionServiceEx,
   RetrievalServiceEx, InsertionServiceEx, InsertionIdGedExistantEx {

      // On récupère la connexion
      //commonsServices.getServiceProvider().openConnexion();

      // on insert le document.
      final StorageDocument document = storageDocumentService.insertStorageDocument(
                                                                                    commonsServices.getStorageDocument());
      // on test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      final byte[] content = storageDocumentService.retrieveStorageDocumentContentByUUID(
                                                                                         new UUIDCriteria(document.getUuid(), null));
      // ici on vérifie qu'on a bien un contenu
      Assert.assertNotNull(content);
   }
}
