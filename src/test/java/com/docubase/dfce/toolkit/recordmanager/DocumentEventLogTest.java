package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.reference.FileReference;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentEventLogTest extends AbstractTestCaseCreateAndPrepareBase {
    private File file;
    private InputStream inputStream;

    @Before
    public void setUpEach() throws FileNotFoundException {
	file = TestUtils.getFile("48Pages.pdf");
	inputStream = new FileInputStream(file);
    }

    @Test
    public void testStoreEventLog() throws TagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());

	document = serviceProvider.getStoreService().storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()), inputStream);

	List<RMDocEvent> eventLogList = serviceProvider
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
	serviceProvider.connect(SIMPLE_USER_NAME, SIMPLE_USER_PASSWORD,
		SERVICE_URL);
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());

	document = serviceProvider.getStoreService().storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()), inputStream);

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
	List<RMDocEvent> eventLogList = serviceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(1, nbEvents);
	assertEquals(DocEventLogType.CREATE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());
	assertEquals(SIMPLE_USER_NAME, eventLogList.get(nbEvents - 1)
		.getUsername());
    }

    @Test
    public void testStoreVirtualEventLog() throws TagControlException {
	FileReference fileReference = createFileReference();

	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);

	Document virtualDocument = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	virtualDocument
		.addCriterion(baseCategory, UUID.randomUUID().toString());
	virtualDocument = serviceProvider.getStoreService()
		.storeVirtualDocument(virtualDocument, fileReference, 1, 10);

	List<RMDocEvent> eventLogList = serviceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			virtualDocument.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(1, nbEvents);
	assertEquals(DocEventLogType.CREATE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());

    }

    @Test
    public void testUpdateEventLog() throws TagControlException,
	    FrozenDocumentException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());

	document = serviceProvider.getStoreService().storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()), inputStream);

	document = serviceProvider.getStoreService().updateDocument(document);

	List<RMDocEvent> eventLogList = serviceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.MODIFY_METADATA,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testUpdateVirtualEventLog() throws TagControlException,
	    FrozenDocumentException {
	FileReference fileReference = createFileReference();

	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);

	Document virtualDocument = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	virtualDocument
		.addCriterion(baseCategory, UUID.randomUUID().toString());
	virtualDocument = serviceProvider.getStoreService()
		.storeVirtualDocument(virtualDocument, fileReference, 1, 10);

	serviceProvider.getStoreService().updateVirtualDocument(
		virtualDocument, 1, 2);

	List<RMDocEvent> eventLogList = serviceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			virtualDocument.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.MODIFY_METADATA,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testDeleteEventLog() throws TagControlException,
	    FrozenDocumentException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());

	document = serviceProvider.getStoreService().storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()), inputStream);

	serviceProvider.getStoreService().deleteDocument(document.getUuid());

	List<RMDocEvent> eventLogList = serviceProvider
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

	document = serviceProvider.getStoreService().storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()), inputStream);

	InputStream documentFile = serviceProvider.getStoreService()
		.getDocumentFile(document);
	documentFile.close();

	List<RMDocEvent> eventLogList = serviceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.RESTITUTE_DOCUMENT,
		eventLogList.get(nbEvents - 1).getEventType());
    }

    @Test
    public void testUpdateFinalDate() throws TagControlException,
	    FrozenDocumentException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, UUID.randomUUID().toString());

	document = serviceProvider.getStoreService().storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()), inputStream);

	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.YEAR, 1);

	serviceProvider.getStoreService().updateDocumentFinalDate(document,
		calendar.getTime());

	List<RMDocEvent> eventLogList = serviceProvider
		.getRecordManagerService().getDocumentEventLogsByUUID(
			document.getUuid());

	assertNotNull(eventLogList);
	int nbEvents = eventLogList.size();
	assertEquals(2, nbEvents);
	assertEquals(DocEventLogType.MODIFY_CONSERVATION_PERIOD, eventLogList
		.get(nbEvents - 1).getEventType());
    }
}
