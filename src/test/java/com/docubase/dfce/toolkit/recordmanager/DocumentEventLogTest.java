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
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
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
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());
	document.setType("pdf");

	document = ServiceProvider.getStoreService().storeDocument(document,
		inputStream);

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(1, nbEvents);
	assertEquals(DocEventLogType.CREATE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testStoreEventLogSimpleUser() throws TagControlException {
	Authentication.openSession(SIMPLE_USER_NAME, SIMPLE_USER_PASSWORD,
		SERVICE_URL);
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());
	document.setType("pdf");

	document = ServiceProvider.getStoreService().storeDocument(document,
		inputStream);

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(1, nbEvents);
	assertEquals(DocEventLogType.CREATE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());
	assertEquals(SIMPLE_USER_NAME, eventLogList.get(nbEvents - 1)
		.getUsername());
	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
    }

    @Test
    public void testStoreVirtualEventLog() throws TagControlException {
	Document refDocument = ToolkitFactory.getInstance().createDocumentTag(
		base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	refDocument.addCriterion(baseCategory, UUID.randomUUID().toString());
	refDocument.setType("pdf");

	refDocument = ServiceProvider.getStoreService().storeDocument(
		refDocument, inputStream);

	Document virtualDocument = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	virtualDocument
		.addCriterion(baseCategory, UUID.randomUUID().toString());
	virtualDocument = ServiceProvider.getStoreService()
		.storeVirtualDocument(virtualDocument, refDocument, 1, 10);

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			virtualDocument.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(1, nbEvents);
	assertEquals(DocEventLogType.CREATE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());

    }

    @Test
    public void testUpdateEventLog() throws TagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());
	document.setType("pdf");

	document = ServiceProvider.getStoreService().storeDocument(document,
		inputStream);

	document = ServiceProvider.getStoreService().updateDocument(document);

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.MODIFY_METADATA,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testUpdateVirtualEventLog() throws TagControlException {
	Document refDocument = ToolkitFactory.getInstance().createDocumentTag(
		base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	refDocument.addCriterion(baseCategory, UUID.randomUUID().toString());
	refDocument.setType("pdf");

	refDocument = ServiceProvider.getStoreService().storeDocument(
		refDocument, inputStream);

	Document virtualDocument = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	virtualDocument
		.addCriterion(baseCategory, UUID.randomUUID().toString());
	virtualDocument = ServiceProvider.getStoreService()
		.storeVirtualDocument(virtualDocument, refDocument, 1, 10);

	ServiceProvider.getStoreService().updateVirtualDocument(
		virtualDocument, 1, 2);

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			virtualDocument.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.MODIFY_METADATA,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testDeleteEventLog() throws TagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());
	document.setType("pdf");

	document = ServiceProvider.getStoreService().storeDocument(document,
		inputStream);

	ServiceProvider.getStoreService().deleteDocument(document.getUuid());

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.DELETE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testExtractEventLog() throws TagControlException, IOException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());
	document.setType("pdf");

	document = ServiceProvider.getStoreService().storeDocument(document,
		inputStream);

	InputStream documentFile = ServiceProvider.getStoreService()
		.getDocumentFile(document);
	documentFile.close();

	List<RMDocEvent> eventLogList = ServiceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.RESTITUTE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());
    }
}
