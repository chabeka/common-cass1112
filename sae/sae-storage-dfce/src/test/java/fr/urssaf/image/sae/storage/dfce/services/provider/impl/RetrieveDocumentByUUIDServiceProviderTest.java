package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe permettant de test la récupération d'un document en base.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class RetrieveDocumentByUUIDServiceProviderTest {

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

   // Ici on test la récupération du document
   @Test
   public final void retrieveDocument() throws ConnectionServiceEx,
         RetrievalServiceEx, InsertionServiceEx {

      // On récupère la connexion
      commonsServices.getServiceProvider().openConnexion();
      // on insert le document.
      StorageDocument document = commonsServices.getServiceProvider()
            .getStorageDocumentService().insertStorageDocument(
                  commonsServices.getStorageDocument());
      // on test ici si on a un UUID
      Assert.assertNotNull(document.getUuid());
      StorageDocument storageDocument = commonsServices.getServiceProvider()
            .getStorageDocumentService().retrieveStorageDocumentByUUID(
                  new UUIDCriteria(document.getUuid(), null));
      // ici on vérifie qu'on a bien un contenu
      Assert.assertNotNull(storageDocument.getContent());
      // ici on vérifie qu'on a bien des métadonnées
      Assert.assertTrue(storageDocument.getMetadatas().size() > 3);

   }
}
