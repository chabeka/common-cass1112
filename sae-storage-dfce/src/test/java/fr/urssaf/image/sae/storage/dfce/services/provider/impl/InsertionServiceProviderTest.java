package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;

/**
 * Classe permettant de test l'insertion d'un document en base.
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class InsertionServiceProviderTest extends CommonsServicesProvider {
   // Ici on insert le document.
   @Test
   public final void insertion() throws ConnectionServiceEx, InsertionServiceEx {
      int insertOcuurences = 10;
      // On récupère la connexion
      getServiceProvider().openConnexion();
      for (int ocuurrence = 0; ocuurrence < insertOcuurences; ocuurrence++)
      // on insert le document.
      {
         // on test ici si on a un UUID
         Assert.assertNotNull("UUID ne doit pas être null : ",
               getServiceProvider().getStorageDocumentService()
                     .insertStorageDocument(getStorageDocument()));
      }

   }

   @Test
   public void insertionDocumentVirtuel() throws ConnectionServiceEx,
         InsertionServiceEx {
      StorageDocument storageDocument = getStorageDocument();

      // On récupère la connexion
      getServiceProvider().openConnexion();

      try {
         VirtualStorageReference reference = new VirtualStorageReference();
         reference.setFilePath(storageDocument.getFilePath());

         StorageReferenceFile storedRef = getServiceProvider()
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

         UUID uuid = getServiceProvider().getStorageDocumentService()
               .insertVirtualStorageDocument(document);
         Assert.assertNotNull(
               "l'identifiant unique du document virtuel doit etre non null",
               uuid);

      } finally {
         getServiceProvider().closeConnexion();
      }

   }
}
