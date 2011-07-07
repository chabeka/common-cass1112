package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;
import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class RichQueriesTest extends AbstractTestCaseCreateAndPrepareBase {
    private static BaseCategory c0;
    private static BaseCategory c1;
    private static BaseCategory c2;
    private static BaseCategory c4;

    @BeforeClass
    public static void setupAll() {
	System.out.println("****** testCompleteQueryScenarii ******");
	Document tag;

	c0 = base.getBaseCategory(catNames[0]);
	c1 = base.getBaseCategory(catNames[1]);
	c2 = base.getBaseCategory(catNames[2]);
	c4 = base.getBaseCategory(catNames[4]);

	for (int i = 0; i < 50; i++) {
	    tag = ToolkitFactory.getInstance().createDocumentTag(base);

	    // C0 unique
	    tag.addCriterion(c0, "testfilter" + i);

	    // C1 soit Enfant, soit Adulte
	    String c1Val = null;
	    if (i < 10) { // Les enfants d'abord
		c1Val = "enfant";
	    } else {
		c1Val = "adulte";
	    }
	    tag.addCriterion(c1, c1Val);

	    // C2. 2 valeurs, une qui varie très peu, une qui est unique.
	    tag.addCriterion(c2, "personne" + i);
	    tag.addCriterion(c2, i % 2 == 0 ? "masculin" : "feminin");
	    tag.addCriterion(c4, 10);

	    // stockage
	    storeDoc(tag, getFile("doc1.pdf", RichQueriesTest.class), true);
	}

	ServiceProvider.getStorageAdministrationService()
		.updateAllIndexesUsageCount();
    }

    @Test
    public void testSimpleQuery() throws ExceededSearchLimitException {
	String query = c1.getFormattedName() + ":adulte";
	assertEquals(40, searchLucene(query, 1000, null));
    }

    @Test(expected = RuntimeException.class)
    public void testLeadingWildcardQuery() throws ExceededSearchLimitException {
	String query = c1.getFormattedName() + ":*";
	searchLucene(query, 1000, null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyQuery() throws ExceededSearchLimitException {
	String query = c1.getFormattedName() + ":";
	searchLucene(query, 1000, null);
    }

    @Test
    public void testCompositeQuery() throws ExceededSearchLimitException {
	String query = "(" + c1.getFormattedName() + ":adulte" + " AND "
		+ c2.getFormattedName() + ":masculin" + ") OR ("
		+ c1.getFormattedName() + ":enfant" + " AND "
		+ c2.getFormattedName() + ":feminin)";
	assertEquals(25, searchLucene(query, 1000, null));
    }

    @Test
    public void testRange() throws ExceededSearchLimitException {
	String query = c1.getFormattedName() + ":adulte" + " AND "
		+ c2.getFormattedName() + ":[feminin TO masculin]";
	assertEquals(40, searchLucene(query, 1000, null));
    }

    @Test(expected = ExceededSearchLimitException.class)
    public void testExceededSearchLimit() throws ExceededSearchLimitException {
	String query = c1.getFormattedName() + ":adulte";
	ServiceProvider.getSearchService().search(query, 100, 0, base, null,
		50000);
    }

    @Test
    public void testAnd() throws ExceededSearchLimitException {
	String query = c1.getFormattedName() + ":adulte" + " AND "
		+ c4.getFormattedName() + ":10";
	assertEquals(40, searchLucene(query, 1000, null));
    }
}
