package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;

/**
 * Classe permettant de tester la recherche d'un document dans la base.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class SearchDocumentByLuceneServiceProviderTest {

   @Autowired
   private CommonsServices commonsServices;

   @Before
   public void init() throws ConnectionServiceEx, IOException, ParseException {
      commonsServices.initServicesParameters();
      commonsServices.initStorageDocumens();
   }

   @After
   public void end() {
      commonsServices.closeServicesParameters();
   }

   // Ici on test la recherche d'un document
   @Test
   @Ignore
   public final void searchDocument() throws ConnectionServiceEx,
         SearchingServiceEx, InsertionServiceEx, QueryParseServiceEx {

      // On récupère la connexion
      commonsServices.getServiceProvider().openConnexion();
      // On insert le document.
      StorageDocument document = commonsServices.getServiceProvider()
            .getStorageDocumentService().insertStorageDocument(
                  commonsServices.getStorageDocument());
      // On test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      final String lucene = String.format("%s:%s", "apr", "GED");
      StorageDocuments strDocuments = commonsServices.getServiceProvider()
            .getStorageDocumentService().searchStorageDocumentByLuceneCriteria(
                  new LuceneCriteria(lucene, 10, null));
      // ici on vérifie qu'on a bien des documents
      Assert.assertNotNull(strDocuments.getAllStorageDocuments());
      // ici on vérifie que le nombre de document est bien supérieur à 1
      Assert.assertTrue(strDocuments.getAllStorageDocuments().size() > 1);
   }
}
