package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import org.junit.Assert;

/**
 * Classe permettant de test la récupération des métadonnées d'un document en
 * base.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class RetrieveMetadatasByUUIDServiceProviderTest {

   @Autowired
   private CommonsServices commonsServices;
   @Autowired
   private StorageDocumentService storageDocumentService;

   @Before
   public void init() throws ConnectionServiceEx, IOException, ParseException {
      commonsServices.initStorageDocumens();
   }

   // Ici on test la récupération du document
   @Test
   public final void retrieveMetadatas() throws ConnectionServiceEx,
   RetrievalServiceEx, InsertionServiceEx, InsertionIdGedExistantEx {
      // on insert le document.
      final StorageDocument document = storageDocumentService.insertStorageDocument(
                                                                                    commonsServices.getStorageDocument());
      // on test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      final List<StorageMetadata> metadatas = storageDocumentService.retrieveStorageDocumentMetaDatasByUUID(
                                                                                                            new UUIDCriteria(document.getUuid(), null));
      // ici on vérifie qu'on a bien des métadonnées
      Assert.assertTrue(metadatas.size() > 3);
   }
}
