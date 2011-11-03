package com.docubase.dfce.toolkit.base;

import static junit.framework.Assert.*;

import java.util.List;

import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.Test;

import com.docubase.dfce.toolkit.TestUtils;

public class IndexCompositeTest extends AbstractTestCaseCreateAndPrepareBase {

    @Test(expected = IllegalArgumentException.class)
    public void testAddComposite_ListNull() {
	serviceProvider.getStorageAdministrationService()
		.findOrCreateCompositeIndex((Category) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddComposite_NotEnoughCategories() {
	Category category = serviceProvider.getStorageAdministrationService()
		.getCategory(catNames[0]);
	serviceProvider.getStorageAdministrationService()
		.findOrCreateCompositeIndex(category);
    }

    @Test
    public void testAddComposite_WithOneCategoryNotString() {
	Category category0 = serviceProvider.getStorageAdministrationService()
		.getCategory(catNames[0]);
	Category booleanCategory = serviceProvider
		.getStorageAdministrationService().getCategory(catNames[3]);
	serviceProvider.getStorageAdministrationService()
		.findOrCreateCompositeIndex(category0, booleanCategory);
    }

    @Test
    public void testAddComposite() {
	Category category0 = serviceProvider.getStorageAdministrationService()
		.getCategory(catNames[0]);
	Category category2 = serviceProvider.getStorageAdministrationService()
		.getCategory(catNames[2]);
	CompositeIndex compositeIndex = serviceProvider
		.getStorageAdministrationService().findOrCreateCompositeIndex(
			category0, category2);
	assertNotNull(compositeIndex);
	List<Category> categories = compositeIndex.getCategories();
	assertEquals(2, categories.size());
	assertEquals(categories.get(0).getName(), catNames[0]);
	assertEquals(categories.get(1).getName(), catNames[2]);
    }

    @Test
    public void testCompositeSearch() throws ExceededSearchLimitException,
	    SearchQueryParseException {

	ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
	BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
	BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);

	/*
	 * On va stocker 100 documents, avec C0 qui change à chaque fois et C1
	 * qui contient le nom du test.
	 */
	for (int i = 0; i < 20; i++) {
	    Document document = toolkitFactory.createDocumentTag(base);
	    // C0 unique
	    document.addCriterion(baseCategory0, "TestOffset" + i);
	    // C1 unique
	    document.addCriterion(baseCategory1, "TestComposite" + i);
	    // C2 unique
	    document.addCriterion(baseCategory2, "Joker" + i);

	    // stockage
	    storeDocument(document, TestUtils.getFile("doc1.pdf"), true);
	}

	Category category1 = serviceProvider.getStorageAdministrationService()
		.getCategory(catNames[1]);
	Category category2 = serviceProvider.getStorageAdministrationService()
		.getCategory(catNames[2]);
	CompositeIndex compositeIndex = serviceProvider
		.getStorageAdministrationService().findOrCreateCompositeIndex(
			category1, category2);

	assertNotNull(compositeIndex);

	String query = category1.getFormattedName()
		+ category2.getFormattedName() + ":TestComposite10Joker10";

	SearchResult search = serviceProvider.getSearchService().search(query,
		10000, base);
	assertEquals(1, search.getTotalHits());

    }

}
