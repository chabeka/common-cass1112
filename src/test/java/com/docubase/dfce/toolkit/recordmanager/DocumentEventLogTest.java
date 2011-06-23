package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.RMClientEvent;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentEventLogTest extends AbstractTestCaseCreateAndPrepareBase {
   private File file;
   private InputStream inputStream;

   @Before
   public void setUpEach() throws FileNotFoundException {
      file = getFile("48Pages.pdf", DocumentEventLogTest.class);
      inputStream = new FileInputStream(file);
   }

   @Test
   public void testStoreEventLog() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      document.addCriterion(baseCategory, UUID.randomUUID().toString());
      document.setType("pdf");

      document = ServiceProvider.getStoreService().storeDocument(document, inputStream);

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(document.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(1, nbEvents);
      assertEquals("storeDocument", eventLogList.get(nbEvents - 1).getEventTypeName());
   }

   @Test
   public void testStoreEventLogSimpleUser() throws TagControlException {
      Authentication.openSession(SIMPLE_USER_NAME, SIMPLE_USER_PASSWORD, SERVICE_URL);
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      document.addCriterion(baseCategory, UUID.randomUUID().toString());
      document.setType("pdf");

      document = ServiceProvider.getStoreService().storeDocument(document, inputStream);

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(document.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(1, nbEvents);
      assertEquals("storeDocument", eventLogList.get(nbEvents - 1).getEventTypeName());
      assertEquals(SIMPLE_USER_NAME, eventLogList.get(nbEvents - 1).getActorLogin());
      Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
   }

   @Test
   public void testStoreVirtualEventLog() throws TagControlException {
      Document refDocument = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      refDocument.addCriterion(baseCategory, UUID.randomUUID().toString());
      refDocument.setType("pdf");

      refDocument = ServiceProvider.getStoreService().storeDocument(refDocument, inputStream);

      Document virtualDocument = ToolkitFactory.getInstance().createDocumentTag(base);
      virtualDocument.addCriterion(baseCategory, UUID.randomUUID().toString());
      virtualDocument = ServiceProvider.getStoreService().storeVirtualDocument(virtualDocument,
            refDocument, 1, 10);

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(virtualDocument.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(1, nbEvents);
      assertEquals("storeVirtualDocument", eventLogList.get(nbEvents - 1).getEventTypeName());

   }

   @Test
   public void testUpdateEventLog() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      document.addCriterion(baseCategory, UUID.randomUUID().toString());
      document.setType("pdf");

      document = ServiceProvider.getStoreService().storeDocument(document, inputStream);

      document = ServiceProvider.getStoreService().updateDocument(document);

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(document.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(2, nbEvents);
      assertEquals("updateDocument", eventLogList.get(nbEvents - 1).getEventTypeName());
   }

   @Test
   public void testUpdateVirtualEventLog() throws TagControlException {
      Document refDocument = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      refDocument.addCriterion(baseCategory, UUID.randomUUID().toString());
      refDocument.setType("pdf");

      refDocument = ServiceProvider.getStoreService().storeDocument(refDocument, inputStream);

      Document virtualDocument = ToolkitFactory.getInstance().createDocumentTag(base);
      virtualDocument.addCriterion(baseCategory, UUID.randomUUID().toString());
      virtualDocument = ServiceProvider.getStoreService().storeVirtualDocument(virtualDocument,
            refDocument, 1, 10);

      ServiceProvider.getStoreService().updateVirtualDocument(virtualDocument, 1, 2);

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(virtualDocument.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(2, nbEvents);
      assertEquals("updateVirtualDocument", eventLogList.get(nbEvents - 1).getEventTypeName());
   }

   @Test
   public void testDeleteEventLog() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      document.addCriterion(baseCategory, UUID.randomUUID().toString());
      document.setType("pdf");

      document = ServiceProvider.getStoreService().storeDocument(document, inputStream);

      ServiceProvider.getStoreService().deleteDocument(document.getUuid());

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(document.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(2, nbEvents);
      assertEquals("deleteDocument", eventLogList.get(nbEvents - 1).getEventTypeName());
   }

   @Test
   public void testExtractEventLog() throws TagControlException, IOException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
      document.addCriterion(baseCategory, UUID.randomUUID().toString());
      document.setType("pdf");

      document = ServiceProvider.getStoreService().storeDocument(document, inputStream);

      InputStream documentFile = ServiceProvider.getStoreService().getDocumentFile(document);
      documentFile.close();

      List<RMClientEvent> eventLogList = ServiceProvider.getRecordManagerService()
            .getEventLogListByKeyDoc(document.getUuid().toString(), Integer.MAX_VALUE);

      assertNotNull(eventLogList);
      int nbEvents = eventLogList.size();
      assertEquals(2, nbEvents);
      assertEquals("extract", eventLogList.get(nbEvents - 1).getEventTypeName());
   }
}
