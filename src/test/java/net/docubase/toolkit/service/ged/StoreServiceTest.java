package net.docubase.toolkit.service.ged;

import static org.junit.Assert.*;

import java.util.UUID;

import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.TestUtils;

@Ignore
public class StoreServiceTest extends AbstractTestBase {

    private final static String BASE_ID = StoreServiceTest.class
	    .getCanonicalName();
    private static Base base;
    private static BaseCategory key;
    private static BaseCategory testId;
    private static StoreService serviceUnderTest;

    @BeforeClass
    public static void setUp() {
	connect();

	key = createBaseCategory("key", CategoryDataType.STRING, true, false,
		1, 1, true);
	testId = createBaseCategory(StoreServiceTest.class.getCanonicalName(),
		CategoryDataType.STRING, true, false, 1, 1, false);

	base = createBase(BASE_ID, key, testId);
	serviceUnderTest = serviceProvider.getStoreService();

    }

    @AfterClass
    public static void tearDown() {
	serviceProvider.getBaseAdministrationService().deleteBase(base);
	disconnect();
    }

    @Before
    public void beforeTest() {

    }

    @After
    public void afterTest() {

    }

    @Test
    public void testDeleteDocument_ByDocument()
	    throws ExceededSearchLimitException, FrozenDocumentException,
	    SearchQueryParseException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(key, UUID.randomUUID().toString());
	document.addCriterion(testId, "testDeleteDocument_ByDocument");
	Document storeDocument = storeDocument(document,
		TestUtils.getDefaultFile(), true);

	serviceUnderTest.deleteDocument(storeDocument);

	assertNull(serviceProvider.getSearchService().getDocumentByUUID(base,
		storeDocument.getUuid()));
	SearchResult search = serviceProvider.getSearchService().search(
		testId.getFormattedName() + ":testDeleteDocument_ByDocument",
		100, base);
	assertEquals(0, search.getTotalHits());
    }

    @Test
    public void testDeleteDocument_ByUUID()
	    throws ExceededSearchLimitException, FrozenDocumentException,
	    SearchQueryParseException {
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(key, UUID.randomUUID().toString());
	document.addCriterion(testId, "testDeleteDocument_ByUUID");
	Document storeDocument = storeDocument(document,
		TestUtils.getDefaultFile(), true);

	serviceUnderTest.deleteDocument(storeDocument.getUuid());

	assertNull(serviceProvider.getSearchService().getDocumentByUUID(base,
		storeDocument.getUuid()));
	SearchResult search = serviceProvider.getSearchService().search(
		testId.getFormattedName() + ":testDeleteDocument_ByUUID", 100,
		base);
	assertEquals(0, search.getTotalHits());
    }

    @Test
    public void testUpdateDocument() {
	fail("Not yet implemented");
    }

    @Test
    public void testUpdateFileVersion() {
	fail("Not yet implemented");
    }

    @Test
    public void testStoreDocument() {
	fail("Not yet implemented");
    }

    @Test
    public void testStoreDocument_WithHash() {

    }

    @Test
    public void testStoreVirtualDocument() {
	fail("Not yet implemented");
    }

    @Test
    public void testUpdateVirtualDocument() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetDocumentFile() {
	fail("Not yet implemented");
    }

    @Test
    public void testInjectDocuments() {
	fail("Not yet implemented");
    }

    @Test
    public void testValueExists() {
	fail("Not yet implemented");
    }

    @Test
    public void testUpdateDocumentType() {
	fail("Not yet implemented");
    }

    @Test
    public void testUpdateDocumentFinalDate() {
	fail("Not yet implemented");
    }

    @Test
    public void testUpdateDocumentLifeCycleReferenceDate() {
	fail("Not yet implemented");
    }

    @Test
    public void testUnfreezeDocument() {
	fail("Not yet implemented");
    }

    @Test
    public void testFreezeDocument() {
	fail("Not yet implemented");
    }

    @Test
    public void testIsFrozen() {
	fail("Not yet implemented");
    }

}
