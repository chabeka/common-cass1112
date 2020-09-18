package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import org.junit.Assert;

/**
 * Classe permettant de tester la recherche d'un document dans la base.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-storage-dfce-test.xml"})
public class SearchPaginatedDocumentProviderTest {

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
   @Ignore
   public final void searchDocument() throws ConnectionServiceEx,
         SearchingServiceEx, InsertionServiceEx, QueryParseServiceEx, InsertionIdGedExistantEx {

      // On insert le document.
      final StorageDocument document = storageDocumentService.insertStorageDocument(
                                                                                    commonsServices.getStorageDocument());
      // On test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      final String lucene = String.format("%s:%s", "apr", "GED");
      final PaginatedLuceneCriteria paginatedLuceneCriteria = new PaginatedLuceneCriteria(
                                                                                          lucene,
                                                                                          10,
                                                                                          null,
                                                                                          null,
                                                                                          null);

      final PaginatedStorageDocuments strDocuments = storageDocumentService.searchPaginatedStorageDocuments(paginatedLuceneCriteria);

      // ici on vérifie qu'on a bien des documents
      Assert.assertNotNull(strDocuments.getAllStorageDocuments());
      // ici on vérifie que le nombre de document est bien supérieur à 1
      Assert.assertTrue(strDocuments.getAllStorageDocuments().size() > 1);
   }
}
