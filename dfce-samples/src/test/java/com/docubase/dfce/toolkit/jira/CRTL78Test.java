package com.docubase.dfce.toolkit.jira;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.docubase.dfce.commons.indexation.SystemFieldName;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL78Test extends AbstractCRTLTest {
    private static final String SRN_VALUE = "39";
    private static final Date CREATION_DATE;

    static {
	Calendar calendar = Calendar.getInstance();

	calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
	calendar.set(2009, 5, 20, 0, 0, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	CREATION_DATE = calendar.getTime();
    }

    @Override
    public void before() {
	super.before();
	storeDocument();
    }

    private void storeDocument() {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(SRN, SRN_VALUE);

	document.setCreationDate(CREATION_DATE);

	InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
	try {
	    document = serviceProvider.getStoreService().storeDocument(
		    document, "doc1", "pdf", inputStream);
	} catch (TagControlException e) {
	    throw new RuntimeException(e);
	}
    }

    @Test
    public void testExclusiveRange_lowerTerm()
	    throws ExceededSearchLimitException, SearchQueryParseException {
	SearchService searchService = serviceProvider.getSearchService();
	String formattedCreationDate = searchService.formatDate(CREATION_DATE,
		DateFormat.DATETIME);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(CREATION_DATE);
	calendar.add(Calendar.DAY_OF_YEAR, 1);
	String formattedEndDate = searchService.formatDate(calendar.getTime(),
		DateFormat.DATETIME);

	SearchQuery searchQueryInclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":["
				+ formattedCreationDate + " TO "
				+ formattedEndDate + "]", base);
	SearchResult searchResult = searchService.search(searchQueryInclusive);
	Assert.assertEquals(1, searchResult.getDocuments().size());

	SearchQuery searchQueryExclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":{"
				+ formattedCreationDate + " TO "
				+ formattedEndDate + "}", base);
	searchResult = searchService.search(searchQueryExclusive);

	Assert.assertEquals(0, searchResult.getDocuments().size());
    }

    @Test
    public void testExclusiveRange_upperTerm()
	    throws ExceededSearchLimitException, SearchQueryParseException {
	SearchService searchService = serviceProvider.getSearchService();
	String formattedCreationDate = searchService.formatDate(CREATION_DATE,
		DateFormat.DATETIME);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(CREATION_DATE);
	calendar.add(Calendar.DAY_OF_YEAR, -1);
	String formattedBeginDate = searchService.formatDate(
		calendar.getTime(), DateFormat.DATETIME);

	SearchQuery searchQueryInclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":["
				+ formattedBeginDate + " TO "
				+ formattedCreationDate + "]", base);
	SearchResult searchResult = searchService.search(searchQueryInclusive);
	Assert.assertEquals(1, searchResult.getDocuments().size());

	SearchQuery searchQueryExclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":{"
				+ formattedBeginDate + " TO "
				+ formattedCreationDate + "}", base);
	searchResult = searchService.search(searchQueryExclusive);

	Assert.assertEquals(0, searchResult.getDocuments().size());
    }

    @Test
    public void testExclusiveRange_DATEformatlowerTerm()
	    throws ExceededSearchLimitException, SearchQueryParseException {
	SearchService searchService = serviceProvider.getSearchService();
	String formattedCreationDate = searchService.formatDate(CREATION_DATE,
		DateFormat.DATE);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(CREATION_DATE);
	calendar.add(Calendar.DAY_OF_YEAR, 1);
	String formattedEndDate = searchService.formatDate(calendar.getTime(),
		DateFormat.DATE);

	SearchQuery searchQueryInclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":["
				+ formattedCreationDate + " TO "
				+ formattedEndDate + "]", base);
	SearchResult searchResult = searchService.search(searchQueryInclusive);
	Assert.assertEquals(1, searchResult.getDocuments().size());

	SearchQuery searchQueryExclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":{"
				+ formattedCreationDate + " TO "
				+ formattedEndDate + "}", base);
	searchResult = searchService.search(searchQueryExclusive);

	Assert.assertEquals(0, searchResult.getDocuments().size());
    }

    @Test
    public void testExclusiveRange_DATEformatupperTerm()
	    throws ExceededSearchLimitException, SearchQueryParseException {
	SearchService searchService = serviceProvider.getSearchService();
	String formattedCreationDate = searchService.formatDate(CREATION_DATE,
		DateFormat.DATE);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(CREATION_DATE);
	calendar.add(Calendar.DAY_OF_YEAR, -1);
	String formattedBeginDate = searchService.formatDate(
		calendar.getTime(), DateFormat.DATE);

	SearchQuery searchQueryInclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":["
				+ formattedBeginDate + " TO "
				+ formattedCreationDate + "]", base);
	SearchResult searchResult = searchService.search(searchQueryInclusive);
	Assert.assertEquals(1, searchResult.getDocuments().size());

	SearchQuery searchQueryExclusive = ToolkitFactory.getInstance()
		.createMonobaseQuery(
			"srn:39 AND " + SystemFieldName.SM_CREATION_DATE + ":{"
				+ formattedBeginDate + " TO "
				+ formattedCreationDate + "}", base);
	searchResult = searchService.search(searchQueryExclusive);

	Assert.assertEquals(0, searchResult.getDocuments().size());
    }
}
