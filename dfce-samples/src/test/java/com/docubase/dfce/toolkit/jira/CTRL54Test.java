package com.docubase.dfce.toolkit.jira;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.service.ged.SearchService;

import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class CTRL54Test extends AbstractTestCaseCreateAndPrepareBase {
    private BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
    private ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
    private SearchService searchService = serviceProvider.getSearchService();
    private String c1Query = baseCategory1.getFormattedName() + ":index1";
    private InputStream inputStream;

    @Before
    public void beforeEach() throws FileNotFoundException {
	File file = getFile("doc1.pdf", CTRL54Test.class);
	inputStream = new FileInputStream(file);
    }

    @Test
    public void testDeindexDocument() throws ExceededSearchLimitException,
	    FrozenDocumentException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	document = storeDoc(document, getFile("doc1.pdf", CTRL54Test.class),
		true);
	assertNotNull(document.getUuid());

	assertNotNull(searchService.getDocumentByUUID(base, document.getUuid()));
	List<Document> docs = searchService.search(c1Query, 10, base)
		.getDocuments();
	assertEquals(1, docs.size());
	assertEquals(document.getUuid(), docs.get(0).getUuid());

	serviceProvider.getStoreService().deleteDocument(document.getUuid());
	docs = searchService.search(c1Query, 10, base).getDocuments();
	assertEquals(0, docs.size());

	Document documentByUUID = searchService.getDocumentByUUID(base,
		document.getUuid());
	assertNull(documentByUUID);
    }

    @Test
    public void testUpdateDocument() throws ExceededSearchLimitException,
	    FrozenDocumentException, TagControlException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	document = storeDoc(document, getFile("doc1.pdf", CTRL54Test.class),
		true);
	assertNotNull(document.getUuid());

	assertNotNull(searchService.getDocumentByUUID(base, document.getUuid()));
	List<Document> docs = searchService.search(c1Query, 10, base)
		.getDocuments();
	assertEquals(1, docs.size());
	assertEquals(document.getUuid(), docs.get(0).getUuid());

	document.getFirstCriterion(baseCategory1).setWord("index2");
	serviceProvider.getStoreService().updateDocument(document);
	docs = searchService.search(c1Query, 10, base).getDocuments();
	assertEquals(0, docs.size());

	Document documentByUUID = searchService.getDocumentByUUID(base,
		document.getUuid());
	assertNotNull(documentByUUID);

	serviceProvider.getStoreService().deleteDocument(
		documentByUUID.getUuid());
    }

    @Test
    public void testDeindexDocumentMultiBase()
	    throws ExceededSearchLimitException, FrozenDocumentException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	document = storeDoc(document, getFile("doc1.pdf", CTRL54Test.class),
		true);
	assertNotNull(document.getUuid());

	assertNotNull(searchService.getDocumentByUUIDMultiBase(document
		.getUuid()));
	List<Document> docs = searchService.multiBaseSearch(c1Query, 10)
		.getDocuments();
	assertEquals(1, docs.size());
	assertEquals(document.getUuid(), docs.get(0).getUuid());

	serviceProvider.getStoreService().deleteDocument(document.getUuid());
	docs = searchService.multiBaseSearch(c1Query, 10).getDocuments();
	assertEquals(0, docs.size());

	Document documentByUUID = searchService.getDocumentByUUID(base,
		document.getUuid());
	assertNull(documentByUUID);
    }

    @Test
    public void testDeindexOneOutOfTwo() throws ExceededSearchLimitException,
	    FrozenDocumentException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	document = storeDoc(document, getFile("doc1.pdf", CTRL54Test.class),
		true);

	Document document2 = toolkitFactory.createDocumentTag(base);
	document2.addCriterion(baseCategory0, "testDeindex2");
	document2.addCriterion(baseCategory1, "index1");
	document2 = storeDoc(document2, getFile("doc1.pdf", CTRL54Test.class),
		true);

	assertNotNull(searchService.getDocumentByUUID(base, document.getUuid()));
	assertNotNull(searchService
		.getDocumentByUUID(base, document2.getUuid()));
	assertNotSame(document.getUuid(), document2.getUuid());

	List<Document> documents = searchService.search(c1Query, 10, base)
		.getDocuments();

	assertEquals(2, documents.size());

	serviceProvider.getStoreService().deleteDocument(document.getUuid());

	documents = searchService.search(c1Query, 10, base).getDocuments();

	assertEquals(1, documents.size());
	assertEquals(document2.getUuid(), documents.get(0).getUuid());

	serviceProvider.getStoreService().deleteDocument(document2.getUuid());
    }

    @Test
    public void testDeindexVirtualDocument()
	    throws ExceededSearchLimitException, FrozenDocumentException,
	    TagControlException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");

	FileReference fileReference = serviceProvider
		.getStorageAdministrationService().createFileReference("doc1",
			"pdf", inputStream);
	document = serviceProvider.getStoreService().storeVirtualDocument(
		document, fileReference, 1, 1);

	assertNotNull(document.getUuid());

	assertNotNull(searchService.getDocumentByUUID(base, document.getUuid()));
	List<Document> docs = searchService.search(c1Query, 10, base)
		.getDocuments();
	assertEquals(1, docs.size());
	assertEquals(document.getUuid(), docs.get(0).getUuid());

	serviceProvider.getStoreService().deleteDocument(document.getUuid());
	docs = searchService.search(c1Query, 10, base).getDocuments();
	assertEquals(0, docs.size());

	Document documentByUUID = searchService.getDocumentByUUID(base,
		document.getUuid());
	assertNull(documentByUUID);
    }

    @Test
    public void testUpdateVirtualDocument()
	    throws ExceededSearchLimitException, FrozenDocumentException,
	    TagControlException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	FileReference fileReference = serviceProvider
		.getStorageAdministrationService().createFileReference("doc1",
			"pdf", inputStream);
	document = serviceProvider.getStoreService().storeVirtualDocument(
		document, fileReference, 1, 1);

	assertNotNull(document.getUuid());

	assertNotNull(searchService.getDocumentByUUID(base, document.getUuid()));
	List<Document> docs = searchService.search(c1Query, 10, base)
		.getDocuments();
	assertEquals(1, docs.size());
	assertEquals(document.getUuid(), docs.get(0).getUuid());

	document.getFirstCriterion(baseCategory1).setWord("index2");
	serviceProvider.getStoreService().updateVirtualDocument(document, 1, 1);
	docs = searchService.search(c1Query, 10, base).getDocuments();
	assertEquals(0, docs.size());

	Document documentByUUID = searchService.getDocumentByUUID(base,
		document.getUuid());
	assertNotNull(documentByUUID);

	serviceProvider.getStoreService().deleteDocument(
		documentByUUID.getUuid());
    }

    @Test
    public void testDeindexVirtualDocumentMultiBase()
	    throws ExceededSearchLimitException, FrozenDocumentException,
	    TagControlException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	FileReference fileReference = serviceProvider
		.getStorageAdministrationService().createFileReference("doc1",
			"pdf", inputStream);
	document = serviceProvider.getStoreService().storeVirtualDocument(
		document, fileReference, 1, 1);
	assertNotNull(document.getUuid());

	assertNotNull(searchService.getDocumentByUUIDMultiBase(document
		.getUuid()));
	List<Document> docs = searchService.multiBaseSearch(c1Query, 10)
		.getDocuments();
	assertEquals(1, docs.size());
	assertEquals(document.getUuid(), docs.get(0).getUuid());

	serviceProvider.getStoreService().deleteDocument(document.getUuid());
	docs = searchService.multiBaseSearch(c1Query, 10).getDocuments();
	assertEquals(0, docs.size());

	Document documentByUUID = searchService.getDocumentByUUID(base,
		document.getUuid());
	assertNull(documentByUUID);
    }

    @Test
    public void testDeindexOneOutOfTwoVirtuals()
	    throws ExceededSearchLimitException, FrozenDocumentException,
	    TagControlException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "testDeindex");
	document.addCriterion(baseCategory1, "index1");
	FileReference fileReference = serviceProvider
		.getStorageAdministrationService().createFileReference("doc1",
			"pdf", inputStream);
	document = serviceProvider.getStoreService().storeVirtualDocument(
		document, fileReference, 1, 1);

	Document document2 = toolkitFactory.createDocumentTag(base);
	document2.addCriterion(baseCategory0, "testDeindex2");
	document2.addCriterion(baseCategory1, "index1");
	FileReference fileReference2 = serviceProvider
		.getStorageAdministrationService().createFileReference("doc1",
			"pdf", inputStream);
	document2 = serviceProvider.getStoreService().storeVirtualDocument(
		document2, fileReference2, 1, 1);

	assertNotNull(searchService.getDocumentByUUID(base, document.getUuid()));
	assertNotNull(searchService
		.getDocumentByUUID(base, document2.getUuid()));
	assertNotSame(document.getUuid(), document2.getUuid());

	List<Document> documents = searchService.search(c1Query, 10, base)
		.getDocuments();

	assertEquals(2, documents.size());

	serviceProvider.getStoreService().deleteDocument(document.getUuid());

	documents = searchService.search(c1Query, 10, base).getDocuments();

	assertEquals(1, documents.size());
	assertEquals(document2.getUuid(), documents.get(0).getUuid());

	serviceProvider.getStoreService().deleteDocument(document2.getUuid());
    }
}
