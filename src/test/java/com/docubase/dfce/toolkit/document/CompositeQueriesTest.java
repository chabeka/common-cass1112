package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class CompositeQueriesTest extends AbstractTestCaseCreateAndPrepareBase {
    @BeforeClass
    public static void setup() throws TagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(category0, "1");
	document.addCriterion(category1, "c11");
	document.addCriterion(category2, "c21");
	document.addCriterion(categoryInteger, 1);
	InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
	serviceProvider.getStoreService().storeDocument(document, "doc1",
		"pdf", inputStream);

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	document.addCriterion(category0, "2");
	document.addCriterion(category1, "c12");
	document.addCriterion(category2, "c22");
	document.addCriterion(categoryInteger, 2);
	inputStream = TestUtils.getInputStream("doc1.pdf");
	serviceProvider.getStoreService().storeDocument(document, "doc2",
		"pdf", inputStream);

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	document.addCriterion(category0, "3");
	document.addCriterion(category1, "c11");
	document.addCriterion(category2, "c22");
	document.addCriterion(categoryInteger, 3);
	inputStream = TestUtils.getInputStream("doc1.pdf");
	serviceProvider.getStoreService().storeDocument(document, "doc3",
		"pdf", inputStream);

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	document.addCriterion(category0, "4");
	document.addCriterion(category1, "c14");
	document.addCriterion(category2, "c24");
	document.addCriterion(categoryInteger, 4);
	inputStream = TestUtils.getInputStream("doc1.pdf");
	serviceProvider.getStoreService().storeDocument(document, "doc4",
		"pdf", inputStream);
    }

    @Test
    public void testSubQueriesAndRange() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	String query = "(" + category1.getFormattedName() + ":" + "c11"
		+ " AND " + category2.getFormattedName() + ":" + "c21"
		+ ") OR (" + category1.getFormattedName() + ":" + "c12"
		+ " AND " + category2.getFormattedName() + ":" + "c*" + ")";

	SearchResult searchResult = serviceProvider.getSearchService().search(
		query, 500, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(2, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc2"));
    }

    @Test
    public void testDeepQuery() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	String c0 = category0.getFormattedName();
	String c1 = category1.getFormattedName();
	String c2 = category2.getFormattedName();
	String cI = categoryInteger.getFormattedName();

	String c0Term = c0 + ":4";
	String c1Term = c1 + ":c11 OR " + c1 + ":c12";
	String c2Term = c2 + ":c21 OR " + c2 + ":c22";
	String cITerm = cI + ":1 OR " + cI + ":2";

	String query = c0Term + " OR ((" + cITerm + " AND (" + c2Term
		+ ")) AND (" + c1Term + "))";

	SearchResult searchResult = serviceProvider.getSearchService().search(
		query, 500, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(3, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc2"));
	assertTrue(fileNames.contains("doc4"));
    }

    @Test
    public void testDeepQueryANDNot() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	String c0 = category0.getFormattedName();
	String c1 = category1.getFormattedName();
	String c2 = category2.getFormattedName();
	String cI = categoryInteger.getFormattedName();

	String c0Term = c0 + ":4";
	String c1Term = c1 + ":c11 OR " + c1 + ":c12";
	String c2Term = c2 + ":c21 OR " + c2 + ":c22";
	String cITerm = cI + ":1 AND NOT " + cI + ":2";

	String query = c0Term + " OR ((" + cITerm + " AND (" + c2Term
		+ ")) AND (" + c1Term + "))";

	SearchResult searchResult = serviceProvider.getSearchService().search(
		query, 500, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(2, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc4"));
    }

    @Test
    public void testDeepQueryORNot() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	String c0 = category0.getFormattedName();
	String c1 = category1.getFormattedName();
	String c2 = category2.getFormattedName();
	String cI = categoryInteger.getFormattedName();

	String c0Term = c0 + ":4";
	String c1Term = c1 + ":c11 OR " + c1 + ":c12";
	String c2Term = c2 + ":c21 OR " + c2 + ":c22";
	String cITerm = cI + ":1 OR NOT " + cI + ":2";

	String query = c0Term + " OR ((" + cITerm + " AND (" + c2Term
		+ ")) AND (" + c1Term + "))";

	SearchResult searchResult = serviceProvider.getSearchService().search(
		query, 500, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(3, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc3"));
	assertTrue(fileNames.contains("doc4"));
    }
}
