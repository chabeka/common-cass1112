/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
public class ServicesValidationTest {

   @Autowired
   private TraitementService service;

   @Autowired
   private DocumentService documentService;

   /* UPDATE DOCUMENT */
   @Test
   public final void testUpdateDocumentInputFilePathObligatoire() {
      try {
         service.updateDocuments(null, null, 0, 0);
         Assert.fail("une erreur IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre fichier d'entrée doit être renseigné", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException est attendue");
      }
   }

   @Test
   public final void testUpdateDocumentPropertiesFilePathObligatoire() {
      try {
         service.updateDocuments("fichier", null, 0, 0);
         Assert.fail("une erreur IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre fichier de propriétés doit être renseigné",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("le paramètre fichier de propriétés doit être renseigné");
      }
   }

   /* WRITE CODES ORG */
   @Test(expected = IllegalArgumentException.class)
   public final void testWriteOrgsInputFilePathObligatoire() {
      service.writeCodesOrganismes(null);
   }

   /* WRITE DOCUMENTS */
   @Test
   public final void testWriteDocumentOutputFilePathObligatoire() {
      try {
         service.writeDocUuidsToUpdate(null, null);

         Assert.fail("une erreur IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre fichier de sortie doit être renseigné", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException est attendue");
      }
   }

   @Test
   public final void testWriteDocumentPropertiesFilePathObligatoire() {
      try {
         service.writeDocUuidsToUpdate("fichier", null);

         Assert.fail("une erreur IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre fichier de propriétés doit être renseigné",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException est attendue");
      }
   }

   /* GET DOCUMENT */
   @Test(expected = IllegalArgumentException.class)
   public final void testGetDocumentUuidObligatoire() {
      documentService.getDocument(null);
   }

   /* UPDATE DOCUMENT */
   @Test(expected = IllegalArgumentException.class)
   public final void testupdateDocumentObligatoire() throws DfceException {
      documentService.updateDocument(null);
   }
}
