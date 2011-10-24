package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

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
}
