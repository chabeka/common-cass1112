package com.docubase.dfce.toolkit.jira;

import static junit.framework.Assert.*;

import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.model.search.SearchResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.TestUtils;

public class CTRL54_2Test extends AbstractTestBase {

    private static Logger logger = Logger.getLogger(CTRL54_2Test.class);

    private static BaseCategory key;
    private static BaseCategory id;
    private static BaseCategory name;

    private static Base base1;
    private static Base base2;

    private static String BASE1_ID = UUID.randomUUID().toString();
    private static String BASE2_ID = UUID.randomUUID().toString();

    @BeforeClass
    public static void setUp() {
	logger.info("setUp " + CTRL54_2Test.class.getCanonicalName());
	connect();
	key = createBaseCategory("key", CategoryDataType.STRING, true, false,
		(short) 1, (short) 1, true);
	id = createBaseCategory("id", CategoryDataType.STRING, true, false,
		(short) 1, (short) 10, false);
	name = createBaseCategory("name", CategoryDataType.STRING, true, false,
		(short) 1, (short) 10, false);

	base1 = createBase(BASE1_ID, key, id, name);
	base2 = createBase(BASE2_ID, key, id, name);

	storeDocuments(20, base1, false);
	storeDocuments(20, base2, true);

    }

    @AfterClass
    public static void tearDown() {
	serviceProvider.getBaseAdministrationService().deleteBase(base1);
	serviceProvider.getBaseAdministrationService().deleteBase(base2);
	disconnect();
	logger.info("tearDown " + CTRL54_2Test.class.getCanonicalName());
    }

    private static void storeDocuments(int nb, Base base, boolean virtual) {
	FileReference fileReference = null;
	if (virtual) {
	    fileReference = serviceProvider.getStorageAdministrationService()
		    .createFileReference(base.getBaseId(), "pdf",
			    TestUtils.getInputStream("doc1.pdf"));
	}
	for (int i = 0; i < nb; i++) {
	    Document document = ToolkitFactory.getInstance().createDocumentTag(
		    base);
	    document.setUuid(UUID.randomUUID());
	    document.setType("Bill");
	    document.setTitle("Title" + i);
	    document.addCriterion(key, String.valueOf(i));
	    document.addCriterion(id, "testDelete");
	    document.addCriterion(name,
		    "name" + StringUtils.leftPad(String.valueOf(i), 3, "0"));
	    if (virtual) {
		try {
		    serviceProvider.getStoreService().storeVirtualDocument(
			    document, fileReference, 1, 4);
		} catch (TagControlException e) {
		    logger.error("Failed to store virtual document", e);
		    fail();
		}
	    } else {
		try {
		    serviceProvider.getStoreService().storeDocument(document,
			    "originalFilename", "pdf",
			    TestUtils.getInputStream("doc1.pdf"));
		} catch (TagControlException e) {
		    logger.error("Failed to store document", e);
		    fail();
		}
	    }
	}

    }

    @Test
    public void testSimpleRange() throws ExceededSearchLimitException,
	    FrozenDocumentException, SearchQueryParseException {

	// Check that document have been properly stored in base1 and base2.
	SearchResult search = serviceProvider.getSearchService().search(
		name.getName() + ":[name000 TO name019]", 200, base1);
	assertEquals(20, search.getTotalHits());
    }

    @Test
    public void testSimpleStar() throws ExceededSearchLimitException,
	    FrozenDocumentException, SearchQueryParseException {

	// Check that document have been properly stored in base1 and base2.
	SearchResult search = serviceProvider.getSearchService().search(
		name.getName() + ":name*", 200, base1);
	assertEquals(20, search.getTotalHits());
    }

    @Test
    public void testDeleteAll() throws TagControlException,
	    ExceededSearchLimitException, FrozenDocumentException,
	    SearchQueryParseException {

	// Check that document have been properly stored in base1 and base2.
	SearchResult search = serviceProvider.getSearchService().search(
		name.getName() + ":[name000 TO name019]", 200, base1);
	assertEquals(20, search.getTotalHits());

	search = serviceProvider.getSearchService().search(
		name.getName() + ":[name000 TO name019]", 200, base2);
	assertEquals(20, search.getTotalHits());

	// Search all documents that share the same id value on every base
	search = serviceProvider.getSearchService().multiBaseSearch(
		id.getName() + ":testDelete", 100);
	assertEquals(40, search.getTotalHits());

	// We delete all documents.
	for (Document doc : search.getDocuments()) {
	    serviceProvider.getStoreService().deleteDocument(doc.getUuid());
	}

	// Check that all documents have been properly deleted
	search = serviceProvider.getSearchService().multiBaseSearch(
		name.getName() + ":[name000 TO name019]", 100);
	assertEquals(0, search.getTotalHits());
    }
}
