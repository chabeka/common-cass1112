package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FilenameUtils;
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
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;

/**
 * Classe permettant de test l'insertion d'un document en base.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class InsertionServiceProviderTest {

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

   // Ici on insert le document.
   @Test
   public final void insertion() throws ConnectionServiceEx, InsertionServiceEx {
      int insertOcuurences = 10;
      // On récupère la connexion
      commonsServices.getServiceProvider().openConnexion();
      for (int ocuurrence = 0; ocuurrence < insertOcuurences; ocuurrence++)
      // on insert le document.
      {
         // on test ici si on a un UUID
         Assert.assertNotNull("UUID ne doit pas être null : ", commonsServices
               .getServiceProvider().getStorageDocumentService()
               .insertStorageDocument(commonsServices.getStorageDocument()));
      }

   }

   @Test
   public void insertionDocumentVirtuel() throws ConnectionServiceEx,
         InsertionServiceEx {
      StorageDocument storageDocument = commonsServices.getStorageDocument();

      // On récupère la connexion
      commonsServices.getServiceProvider().openConnexion();

      try {
         VirtualStorageReference reference = new VirtualStorageReference();
         reference.setFilePath(storageDocument.getFilePath());

         StorageReferenceFile storedRef = commonsServices.getServiceProvider()
               .getStorageDocumentService().insertStorageReference(reference);

         Assert.assertNotNull("le fichier de référence doit etre non null",
               storedRef);

         VirtualStorageDocument document = new VirtualStorageDocument();
         document.setMetadatas(storageDocument.getMetadatas());
         document.setFileName(FilenameUtils.getBaseName(storageDocument
               .getFileName())
               + "_1_1");
         document.setReferenceFile(storedRef);
         document.setStartPage(1);
         document.setEndPage(1);

         UUID uuid = commonsServices.getServiceProvider()
               .getStorageDocumentService().insertVirtualStorageDocument(
                     document);
         Assert.assertNotNull(
               "l'identifiant unique du document virtuel doit etre non null",
               uuid);

      } finally {
         commonsServices.getServiceProvider().closeConnexion();
      }

   }
}
