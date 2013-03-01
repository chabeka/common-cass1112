package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

import net.docubase.toolkit.model.document.Document;

import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentFreezeTest extends AbstractTestCaseCreateAndPrepareBase {

   private Document document;

   @Before
   public void beforeEach() {
      document = createDocument();
   }

   private Document createDocument() {
      Document document = createTestDocument();
      File file = TestUtils.getFile("doc1.pdf");
      try {
         return serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
               new FileInputStream(file));
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      } catch (TagControlException e) {
         throw new RuntimeException(e);
      }
   }

   @Test
   public void testFreezeDocument() {
      serviceProvider.getStoreService().freezeDocument(document);

      assertTrue(serviceProvider.getStoreService().isFrozen(document));
   }

   @Test(expected = FrozenDocumentException.class)
   public void testDeleteFrozenDocument() throws FrozenDocumentException {
      serviceProvider.getStoreService().freezeDocument(document);

      serviceProvider.getStoreService().deleteDocument(document.getUuid());
   }

   @Test
   public void testUpdateFrozenDocument() throws FrozenDocumentException {
      serviceProvider.getStoreService().freezeDocument(document);
      assertTrue(serviceProvider.getStoreService().isFrozen(document));
      serviceProvider.getStoreService().unfreezeDocument(document);
      serviceProvider.getStoreService().deleteDocument(document.getUuid());
   }

   @Test
   public void testUnfreezeDocument() throws FrozenDocumentException {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.SECOND, 1);
      Date finalDate = calendar.getTime();
      serviceProvider.getStoreService().updateDocumentFinalDate(document, finalDate);

      try {
         Thread.sleep(2000);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
      serviceProvider.getStoreService().freezeDocument(document);
      assertTrue(serviceProvider.getStoreService().isFrozen(document));
      serviceProvider.getStoreService().unfreezeDocument(document);
      assertFalse(serviceProvider.getStoreService().isFrozen(document));
   }

}
