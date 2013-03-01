package com.docubase.dfce.toolkit.lifecycle;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;
import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;

import org.junit.Test;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class LifeCycleStorageTest extends AbstractTestCaseCreateAndPrepareBase {

   private File createTestFile() {
      return TestUtils.getFile("doc1.pdf");
   }

   @Test
   public void testStoreDocumentWithNoRule() throws FileNotFoundException, TagControlException {
      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));

      assertEquals("DEFAULT_DOCUMENT_TYPE", document.getType());
      assertEquals(document.getCreationDate(), document.getLifeCycleReferenceDate());
      assertNull(document.getFinalDate());
   }

   @Test
   public void testStoreDocumentWithSpecifiedRule() throws FileNotFoundException,
         TagControlException, ObjectAlreadyExistsException {
      String documentType = "documentType" + UUID.randomUUID();
      serviceProvider.getStorageAdministrationService().createNewLifeCycleRule(documentType, 12,
            LifeCycleLengthUnit.YEAR);

      Calendar calendar = Calendar.getInstance();
      calendar.set(2010, 12, 31);
      Date lifeCycleReferenceDate = calendar.getTime();

      Document document = createTestDocument();

      document.setLifeCycleReferenceDate(lifeCycleReferenceDate);
      document.setType(documentType);

      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));

      assertEquals(documentType, document.getType());
      assertEquals(lifeCycleReferenceDate, document.getLifeCycleReferenceDate());
      assertNull(document.getFinalDate());
   }

   @Test
   public void testStoreDocumentWithFinalDate() throws FileNotFoundException, TagControlException {
      Document document = createTestDocument();
      ((DocumentImpl) document).setFinalDate(new Date());

      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));

      assertNull(document.getFinalDate());
   }

   @Test
   public void testUpdateDocumentTryningFinalDate() throws FileNotFoundException,
         TagControlException, FrozenDocumentException {
      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));
      assertNull(document.getFinalDate());

      ((DocumentImpl) document).setFinalDate(new Date());
      assertNotNull(document.getFinalDate());

      document = serviceProvider.getStoreService().updateDocument(document);

      assertNull(document.getFinalDate());
   }

   @Test
   public void testUpdateDocumentTryningLifeCycleReferenceDate() throws FileNotFoundException,
         TagControlException, FrozenDocumentException {
      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));
      Date initialReferenceDate = document.getLifeCycleReferenceDate();

      document.setLifeCycleReferenceDate(new Date());
      assertNotSame(initialReferenceDate, document.getLifeCycleReferenceDate());

      document = serviceProvider.getStoreService().updateDocument(document);

      assertEquals(initialReferenceDate, document.getLifeCycleReferenceDate());
   }

   @Test
   public void testUpdateDocumentTryningLifeCycleReferenceDocumentType()
         throws FileNotFoundException, TagControlException, ObjectAlreadyExistsException,
         FrozenDocumentException {
      String documentType = "documentType" + UUID.randomUUID();
      serviceProvider.getStorageAdministrationService().createNewLifeCycleRule(documentType, 12,
            LifeCycleLengthUnit.YEAR);

      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));
      String initialDocumentType = document.getType();
      document.setType(documentType);

      document = serviceProvider.getStoreService().updateDocument(document);

      assertEquals(initialDocumentType, document.getType());
   }

   @Test
   public void testUpdateDocumentFinalDate() throws FileNotFoundException, TagControlException,
         FrozenDocumentException {
      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));
      assertNull(document.getFinalDate());

      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, 1);
      Date newFinalDate = calendar.getTime();
      serviceProvider.getStoreService().updateDocumentFinalDate(document, newFinalDate);

      document = serviceProvider.getSearchService().getDocumentByUUID(base, document.getUuid());

      assertEquals(newFinalDate, document.getFinalDate());
   }

   @Test
   public void testUpdateDocumentLifeCycleReferenceDate() throws TagControlException,
         FileNotFoundException, FrozenDocumentException {
      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));

      Date newLifeCycleReferenceDate = new Date();
      serviceProvider.getStoreService().updateDocumentLifeCycleReferenceDate(document,
            newLifeCycleReferenceDate);

      document = serviceProvider.getSearchService().getDocumentByUUID(base, document.getUuid());

      assertEquals(newLifeCycleReferenceDate, document.getLifeCycleReferenceDate());
   }

   @Test
   public void testUpdateDocumentLifeCycleReferenceDocumentType()
         throws ObjectAlreadyExistsException, FileNotFoundException, TagControlException,
         FrozenDocumentException {
      String documentType = "documentType" + UUID.randomUUID();
      serviceProvider.getStorageAdministrationService().createNewLifeCycleRule(documentType, 12,
            LifeCycleLengthUnit.YEAR);

      Document document = createTestDocument();
      File file = createTestFile();
      document = serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf",
            new FileInputStream(file));

      assertEquals("DEFAULT_DOCUMENT_TYPE", document.getType());

      serviceProvider.getStoreService().updateDocumentType(document, documentType);

      document = serviceProvider.getSearchService().getDocumentByUUID(base, document.getUuid());

      assertEquals(documentType, document.getType());
   }
}
