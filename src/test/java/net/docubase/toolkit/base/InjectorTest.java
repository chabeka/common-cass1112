package net.docubase.toolkit.base;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.List;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.recordmanager.AbstractEventTest;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;

public class InjectorTest extends AbstractEventTest {
   /** le fichier lot à injecter */
   private static final String XML_BATCH = "lot_valide.xml";

   /**
    * Test d'injection en masse de documents
    */
   @Test
   public void testInjectBatch() {
      File file = getFile(XML_BATCH, InjectorTest.class);
      List<Document> documens = ServiceProvider.getStoreService().injectDocuments(
            file.getAbsolutePath());

      assertEquals("Tous les documents n'ont pas été injectés.", 3, documens.size());

      for (Document doc : documens) {
         ServiceProvider.getStoreService().deleteDocument(doc);
      }
   }
}
