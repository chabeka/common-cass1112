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
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import junit.framework.Assert;

/**
 * Classe permettant de tester la recherche d'un document dans la
 * base.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class SearchDocumentByUUIDServiceProviderTest {

   @Autowired
   private CommonsServices commonsServices;
   @Autowired
   private StorageDocumentService storageDocumentService;

   @Before
   public void init() throws ConnectionServiceEx, IOException, ParseException {
      commonsServices.initStorageDocumens();
   }

   // Ici on test la recherche d'un document
   @Test
   public final void searchDocument() throws ConnectionServiceEx,
   SearchingServiceEx, InsertionServiceEx, InsertionIdGedExistantEx {
      // on insert le document.
      final StorageDocument document = storageDocumentService.insertStorageDocument(
                                                                                    commonsServices.getStorageDocument());
      // on test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      final StorageDocument storageDocument = storageDocumentService.searchStorageDocumentByUUIDCriteria(
                                                                                                         new UUIDCriteria(document.getUuid(), null));
      // ici on vérifie qu'on a bien un contenu
      Assert.assertNotNull(storageDocument.getContent());
      // ici on vérifie qu'on a bien des métadonnées
      Assert.assertTrue(storageDocument.getMetadatas().size() > 3);
   }

}
