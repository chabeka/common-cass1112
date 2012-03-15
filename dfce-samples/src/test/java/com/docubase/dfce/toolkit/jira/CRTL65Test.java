package com.docubase.dfce.toolkit.jira;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Test;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL65Test extends AbstractCRTLTest {
    private static final String NCE_VALUE = "169996144948345571";

    private void storeDocuments() {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(NCE, NCE_VALUE);
	document.addCriterion(DATE, "20111017");

	InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
	try {
	    serviceProvider.getStoreService().storeDocument(document, "doc1",
		    "pdf", inputStream);
	} catch (TagControlException e) {
	    throw new RuntimeException(e);
	}

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	document.addCriterion(NCE, NCE_VALUE);
	document.addCriterion(DATE,
		new GregorianCalendar(2011, 9, 17).getTime());

	inputStream = TestUtils.getInputStream("doc1.pdf");
	try {
	    serviceProvider.getStoreService().storeDocument(document, "doc2",
		    "pdf", inputStream);
	} catch (TagControlException e) {
	    throw new RuntimeException(e);
	}

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	document.addCriterion(NCE, NCE_VALUE);

	Calendar calendar = Calendar.getInstance();
	calendar.set(2011, 9, 17);
	document.addCriterion(DATE, calendar.getTime());

	inputStream = TestUtils.getInputStream("doc1.pdf");
	try {
	    serviceProvider.getStoreService().storeDocument(document, "doc3",
		    "pdf", inputStream);
	} catch (TagControlException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void before() {
	super.before();
	storeDocuments();
    }

    @Test
    public void testDate() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	SearchResult searchResult = serviceProvider.getSearchService().search(
		NCE + ":" + NCE_VALUE + " AND " + DATE + ":20111017", 10, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(3, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc2"));
	assertTrue(fileNames.contains("doc3"));
    }

    @Test
    public void testDateFormattedDate() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	Calendar calendar = Calendar.getInstance();
	calendar.set(2011, 9, 17);
	String formattedDate = serviceProvider.getSearchService().formatDate(
		calendar.getTime(), DateFormat.DATE);

	SearchResult searchResult = serviceProvider.getSearchService().search(
		"nce:" + NCE_VALUE + " AND date:" + formattedDate, 10, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(3, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc2"));
	assertTrue(fileNames.contains("doc3"));
    }

    @Test
    public void testDateFormattedTimeZonedDate()
	    throws ExceededSearchLimitException, SearchQueryParseException {
	String formattedDate = serviceProvider.getSearchService().formatDate(
		new GregorianCalendar(2011, 9, 17).getTime(), DateFormat.DATE);
	SearchResult searchResult = serviceProvider.getSearchService().search(
		"nce:" + NCE_VALUE + " AND date:" + formattedDate, 10, base);
	List<Document> documents = searchResult.getDocuments();
	assertEquals(3, documents.size());
	Set<String> fileNames = new HashSet<String>();
	for (Document document : documents) {
	    fileNames.add(document.getFilename());
	}

	assertTrue(fileNames.contains("doc1"));
	assertTrue(fileNames.contains("doc2"));
	assertTrue(fileNames.contains("doc3"));
    }
}
