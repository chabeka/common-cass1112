package net.docubase.toolkit.base;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.UUID;

import net.docubase.rheatoolkit.RheaToolkitException;
import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.ICustomTagControl;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.ChainedFilter.ChainedFilterOperator;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearchFilterTest extends AbstractTestCaseCreateAndPrepareBase {

    private static BaseCategory key;
    private static BaseCategory company;
    private static BaseCategory owner;
    private static BaseCategory id;
    private static BaseCategory number;

    /** UUID du document de réféence pour les tests. */
    private static UUID referenceUUID;

    private static File file;

    @BeforeClass
    public static final void beforeClass() throws CustomTagControlException {
	file = getFile("48pages.pdf", VirtualDocumentTest.class);
	storeDocument();
	insertDocument();
    }

    private static void insertDocument() throws CustomTagControlException {
	Document tag = null;

	key = base.getBaseCategory(catNames[0]);
	company = base.getBaseCategory(catNames[1]);
	owner = base.getBaseCategory(catNames[2]);
	id = base.getBaseCategory(catNames[4]);
	number = base.getBaseCategory(catNames[8]);

	for (int i = 0; i < 200; i++) {
	    tag = ToolkitFactory.getInstance().createDocumentTag(base);
	    // Key is unique.
	    tag.addCriterion(key, "key" + i);

	    String c1Val = null;
	    if (i < 100) {
		c1Val = "SPAM";
		tag.addCriterion(id, i % 2 == 0 ? "3" : "7");
	    } else {
		c1Val = "AT&T";
		tag.addCriterion(id, i % 2 == 0 ? "23" : "47");
	    }
	    tag.addCriterion(company, c1Val);

	    tag.addCriterion(owner, "number" + i);
	    tag.addCriterion(owner, i % 2 == 0 ? "bob" : "sue");
	    tag.addCriterion(number, i);

	    // stockage
	    insertVirtualDocument(referenceUUID, tag, 1, 5, String.valueOf(i));
	}

    }

    private static Document storeDocument() throws CustomTagControlException {
	Document tag = ToolkitFactory.getInstance().createDocumentTag(base)
		.setCreationDate(generateCreationDate())
		.setDocFileName(file.getName()).setDocType("pdf");

	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	tag.addCriterion(baseCategory, "FileRef_" + System.currentTimeMillis());

	ICustomTagControl control = new MyTagControl(catNames);
	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		file, control);

	assertTrue(stored);
	referenceUUID = tag.getUUID();

	return tag;
    }

    /**
     * Insert un document virtuel.
     * 
     * @param referenceUUID
     *            uuid du document de référence
     * @param startPage
     *            première page
     * @param endPage
     *            dernière page
     * @param name
     *            nom du document virtuel.
     * 
     * @return le doucument virtuel.
     * @throws CustomTagControlException
     * @throws RheaToolkitException
     */
    private static Document insertVirtualDocument(UUID referenceUUID,
	    Document tag, int startPage, int endPage, String name)
	    throws CustomTagControlException {

	tag.setDocFileName(name).setDocType("pdf")
		.setVirtualReferenceUUID(referenceUUID)
		.setVirtualStartPage(startPage).setVirtualEndPage(endPage);

	ICustomTagControl control = new MyTagControl(catNames);
	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		control);

	assertTrue(stored);
	return tag;
    }

    @Test
    public void testFilterAnd() throws RheaToolkitException {
	ChainedFilter chainedFilter = ToolkitFactory.getInstance()
		.createChainedFilter()
		.addTermFilter(company.getFormattedName(), "spam")
		.addTermFilter(owner.getFormattedName(), "bob");
	SearchResult result = ServiceProvider.getSearchService().search(
		key.getFormattedName() + ":key*", 1000, base, chainedFilter);
	assertEquals(50, result.getTotalHits());
    }

    @Test
    public void testFilterOr() throws RheaToolkitException {
	ChainedFilter chainedFilter = ToolkitFactory
		.getInstance()
		.createChainedFilter()
		.addTermFilter(company.getFormattedName(), "spam",
			ChainedFilterOperator.OR)
		.addTermFilter(company.getFormattedName(), "at&t",
			ChainedFilterOperator.OR);
	SearchResult result = ServiceProvider.getSearchService().search(
		key.getFormattedName() + ":key*", 1000, base, chainedFilter);
	assertEquals(200, result.getTotalHits());

    }

    @Test
    public void testFilterAndNot() throws RheaToolkitException {
	ChainedFilter chainedFilter = ToolkitFactory
		.getInstance()
		.createChainedFilter()
		.addTermFilter(owner.getFormattedName(), "bob",
			ChainedFilterOperator.ANDNOT);

	SearchResult result = ServiceProvider.getSearchService().search(
		key.getFormattedName() + ":key*", 1000, base, chainedFilter);
	assertEquals(100, result.getTotalHits());
    }

    @Test
    public void testNumericRangeFilterExclusive() throws RheaToolkitException {
	ChainedFilter chainedFilter = ToolkitFactory.getInstance()
		.createChainedFilter()
		.addIntRangeFilter(id.getFormattedName(), 7, 47, false, false);
	SearchResult result = ServiceProvider.getSearchService().search(
		key.getFormattedName() + ":key*", 1000, base, chainedFilter);
	assertEquals(50, result.getTotalHits());

    }

    @Test
    public void testNumericRangeFilterInclusive() throws RheaToolkitException {
	ChainedFilter chainedFilter = ToolkitFactory.getInstance()
		.createChainedFilter()
		.addIntRangeFilter(id.getFormattedName(), 7, 47, true, true);
	SearchResult result = ServiceProvider.getSearchService().search(
		key.getFormattedName() + ":key*", 1000, base, chainedFilter);
	assertEquals(150, result.getTotalHits());

    }

    @Test
    public void testMultipleFilter() throws RheaToolkitException {
	ChainedFilter chainedFilter = ToolkitFactory
		.getInstance()
		.createChainedFilter()
		.addTermFilter(company.getFormattedName(), "spam",
			ChainedFilterOperator.AND)
		.addTermFilter(owner.getFormattedName(), "bob",
			ChainedFilterOperator.AND)
		.addTermFilter(owner.getFormattedName(), "sue",
			ChainedFilterOperator.OR);
	SearchResult result = ServiceProvider.getSearchService().search(
		number.getFormattedName() + ":[1 TO 10]", 1000, base,
		chainedFilter);

	assertEquals(10, result.getTotalHits());

    }

    @Test
    public void testNumericRangeQuery() throws RheaToolkitException {
	SearchResult result = ServiceProvider.getSearchService().search(
		owner.getFormattedName() + ":bob AND "
			+ number.getFormattedName() + ":[1 TO 20]", 1000, base);
	assertEquals(10, result.getTotalHits());
    }
}
