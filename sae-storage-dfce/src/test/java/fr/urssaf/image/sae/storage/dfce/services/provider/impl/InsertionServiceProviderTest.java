package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
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
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import junit.framework.Assert;

/**
 * Classe permettant de test l'insertion d'un document en base.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class InsertionServiceProviderTest {

   @Autowired
   private CommonsServices commonsServices;

   @Autowired
   private StorageDocumentService storageDocumentService;

   @Before
   public void init() throws ConnectionServiceEx, IOException, ParseException {
      commonsServices.initStorageDocumens();
   }

   // Ici on insert le document.
   @Test
   public final void insertion() throws ConnectionServiceEx, InsertionServiceEx, InsertionIdGedExistantEx {
      final int insertOcuurences = 10;
      // On récupère la connexion
      for (int ocuurrence = 0; ocuurrence < insertOcuurences; ocuurrence++)
         // on insert le document.
      {
         // on test ici si on a un UUID
         Assert.assertNotNull("UUID ne doit pas être null : ", storageDocumentService.insertStorageDocument(commonsServices.getStorageDocument()));
      }

   }

   @Test
   public void insertionDocumentVirtuel() throws ConnectionServiceEx,
   InsertionServiceEx {
      final StorageDocument storageDocument = commonsServices.getStorageDocument();

      final VirtualStorageReference reference = new VirtualStorageReference();
      reference.setFilePath(storageDocument.getFilePath());

      final StorageReferenceFile storedRef = storageDocumentService.insertStorageReference(reference);

      Assert.assertNotNull("le fichier de référence doit etre non null",
                           storedRef);

      final VirtualStorageDocument document = new VirtualStorageDocument();
      document.setMetadatas(storageDocument.getMetadatas());
      document.setFileName(FilenameUtils.getBaseName(storageDocument
                                                     .getFileName())
                           + "_1_1");
      document.setReferenceFile(storedRef);
      document.setStartPage(1);
      document.setEndPage(1);

      final UUID uuid = storageDocumentService.insertVirtualStorageDocument(
                                                                            document);
      Assert.assertNotNull(
                           "l'identifiant unique du document virtuel doit etre non null",
                           uuid);

   }
}
