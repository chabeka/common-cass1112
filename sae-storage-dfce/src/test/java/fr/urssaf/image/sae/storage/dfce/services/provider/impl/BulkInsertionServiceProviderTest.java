package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;

/**
 * Classe permettant de test l'insertion en masse des documents en base.
 * 
 * @author akenore, rhofir.
 * 
 */

public class BulkInsertionServiceProviderTest extends CommonsServicesProvider {

   /**
    * Ici on test l'insertion en masse.
    */
   @Test
   @Ignore("Pour eviter que la base soit corrompue")
   @SuppressWarnings("PMD.LongVariable")
   public final void bulkInsertion() throws ConnectionServiceEx,
         InsertionServiceEx {
      
      // On récupère la connexion
      getServiceProvider().openConnexion();
      // on insert en mass les documents.
      BulkInsertionResults bulkInsertionResults = getServiceProvider()
            .getStorageDocumentService().bulkInsertStorageDocument(
                  getStorageDocuments(), false);
      // on test ici si on a bien inséré les 7 documents.
      Assert.assertTrue(bulkInsertionResults.getUuids().size() > 1);
      
   }

}
