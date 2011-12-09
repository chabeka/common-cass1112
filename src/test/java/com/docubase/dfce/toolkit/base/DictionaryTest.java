package com.docubase.dfce.toolkit.base;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.TestUtils;

public class DictionaryTest extends AbstractTestCaseCreateAndPrepareBase {
    private static BaseCategory baseCategory;
    private File newDoc;
    private Document document;

    @BeforeClass
    public static void setupAll() {
	Category category = serviceProvider.getStorageAdministrationService()
		.findOrCreateCategory("CategoryWithDictionary",
			CategoryDataType.INTEGER);

	baseCategory = ToolkitFactory.getInstance().createBaseCategory(
		category, true);
	baseCategory.setMaximumValues((short) 10);
	baseCategory.setEnableDictionary(true);

	serviceProvider.getBaseAdministrationService().stopBase(base);
	base.addBaseCategory(baseCategory);
	serviceProvider.getBaseAdministrationService().updateBase(base);
	serviceProvider.getBaseAdministrationService().startBase(base);
    }

    @Before
    public void setupEach() {
	serviceProvider.getStorageAdministrationService().addDictionaryTerm(
		baseCategory, "10");

	newDoc = TestUtils.getFile("doc1.pdf");
	Assert.assertTrue(newDoc.exists());

	document = ToolkitFactory.getInstance().createDocumentTag(base);
	String identifier = "Identifier" + UUID.randomUUID() + base.getBaseId();
	document.addCriterion(category0, identifier);

	// Date de création du document (à priori avant son entrée dans la
	// GED, on retranche une heure)
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis(System.currentTimeMillis());
	cal.add(Calendar.HOUR, -1);
	document.setCreationDate(cal.getTime());
    }

    @Test(expected = TagControlException.class)
    public void testStoreDocWrongEntry() throws TagControlException {
	document.addCriterion(baseCategory, 11);

	storeDocument(document, newDoc);
    }

    @Test
    public void testStoreDocCorrectEntry() {
	document.addCriterion(baseCategory, 10);
	Document stored;
	try {
	    stored = storeDocument(document, newDoc);
	} catch (TagControlException e) {
	    throw new RuntimeException(e);
	}

	// 10 est dans le dictionaire, le document est donc stocké
	Assert.assertNotNull(stored);
    }

    @Test(expected = TagControlException.class)
    public void testRemoveEntry() throws TagControlException {
	serviceProvider.getStorageAdministrationService().removeDictionaryTerm(
		baseCategory, "10");

	document.addCriterion(baseCategory, 10);
	storeDocument(document, newDoc);
    }

    @Test
    public void testGetAllEntries() {
	List<String> allEntries = serviceProvider
		.getStorageAdministrationService().getAllDictonaryTerms(
			baseCategory);
	Assert.assertEquals(1, allEntries.size());
	Assert.assertTrue(allEntries.contains("10"));
    }
}
