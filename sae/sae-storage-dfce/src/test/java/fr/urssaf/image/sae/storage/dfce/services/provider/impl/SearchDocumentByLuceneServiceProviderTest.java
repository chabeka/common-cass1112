package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
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
public class SearchDocumentByLuceneServiceProviderTest extends
      CommonsServicesProvider {

   // Ici on test la recherche d'un document
   @Test
	@Ignore
     public final void searchDocument() throws ConnectionServiceEx,
         SearchingServiceEx, InsertionServiceEx, QueryParseServiceEx {
     
      // On récupère la connexion
      getServiceProvider().openConnexion();
      // On insert le document.
      StorageDocument document= getServiceProvider().getStorageDocumentService()
            .insertStorageDocument(getStorageDocument());
      // On test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      final String lucene = String.format("%s:%s", "apr", "GED");
      StorageDocuments strDocuments = getServiceProvider()
            .getStorageDocumentService().searchStorageDocumentByLuceneCriteria(
                  new LuceneCriteria(lucene, 10, null));
      // ici on vérifie qu'on a bien des documents
      Assert.assertNotNull(strDocuments.getAllStorageDocuments());
      // ici on vérifie que le nombre de document est bien supérieur à 1
      Assert.assertTrue(strDocuments.getAllStorageDocuments().size() > 1);
   }
}
