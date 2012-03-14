package com.docubase.dfce.toolkit.jira;

import java.io.InputStream;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.Assert;
import org.junit.Test;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CTRL64Test extends AbstractCRTLTest {
    private void storeDocuments() {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(APR, "WATT");
	document.addCriterion(NCE, "169996144948345571");

	InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
	try {
	    serviceProvider.getStoreService().storeDocument(document, "doc1",
		    "pdf", inputStream);
	} catch (TagControlException e) {
	    throw new RuntimeException(e);
	}

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	document.addCriterion(APR, "WATT");
	document.addCriterion(NCE, "169996144948345573");

	inputStream = TestUtils.getInputStream("doc1.pdf");
	try {
	    serviceProvider.getStoreService().storeDocument(document, "doc1",
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
    public void testTermAndNonIndexed() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	SearchResult searchResult = serviceProvider.getSearchService().search(
		"nce:169996144948345571 AND apr:WATT", 10, base);
	Assert.assertEquals(1, searchResult.getTotalHits());
    }

    @Test
    public void testStarredAndNonIndexed() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	SearchResult searchResult = serviceProvider.getSearchService().search(
		"nce:1* AND apr:WATT", 10, base);
	Assert.assertEquals(2, searchResult.getTotalHits());
    }

    @Test
    public void testRangeAndNonIndexed() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	SearchResult searchResult = serviceProvider.getSearchService().search(
		"nce:[169996144948345570 TO 169996144948345572] AND apr:WATT",
		10, base);
	Assert.assertEquals(1, searchResult.getTotalHits());
    }

}
