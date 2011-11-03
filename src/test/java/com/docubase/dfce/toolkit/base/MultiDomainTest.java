package com.docubase.dfce.toolkit.base;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import junit.framework.TestCase;
import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.TestUtils;

public class MultiDomainTest extends AbstractTestBase {
    private static final Map<String, String> catValues = new HashMap<String, String>();

    private static final Logger LOGGER = Logger
	    .getLogger(MultiDomainTest.class);

    private static final String URL = AbstractTestBase.SERVICE_URL;
    private static final String URL2 = AbstractTestBase.SERVICE_URL_2;

    private static final String CATA = "MBCode Fournisseur";
    private static final String CATB = "MBNo Serie";
    private static final String CATC = "Prix Vente";
    private static final String CATD = "Facultatif";

    private static final String BASE2 = "BASE 2";
    private static final String BASE3 = "BASE 3";

    /** Pour pouvoir les effacer plus rapidement */
    private static List<Document> storedDocs;

    static {
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");
    }

    @BeforeClass
    public static void setUp() {

	storedDocs = new ArrayList<Document>();

	/*
	 * base1 et 2 ont les 2 CATA, CATB et CATC mais pas dans le même ordre
	 * (on verra que celà n'a pas d'importance) base2 est la seule à avoir
	 * CATD.
	 */
	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL);
	createBase(BASE2, "Base no 1 - dom1", new String[] { CATA, CATC, CATB });
	serviceProvider.disconnect();

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL2);
	createBase(BASE3, "Base no 2 - dom1", new String[] { CATA, CATB, CATC,
		CATD });
	serviceProvider.disconnect();
    }

    /**
     * @see TestCase#tearDown()
     */
    @AfterClass
    public static void tearDown() {
	try {
	    if (serviceProvider.isSessionActive()) {
		serviceProvider.disconnect();
	    }
	    serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL);
	    for (Document document : storedDocs) {
		serviceProvider.getStoreService().deleteDocument(
			document.getUuid());
	    }
	    Base base2 = serviceProvider.getBaseAdministrationService()
		    .getBase(BASE2);
	    Base base3 = serviceProvider.getBaseAdministrationService()
		    .getBase(BASE3);
	    deleteBase(base2);
	    deleteBase(base3);
	} catch (Exception e) {
	    LOGGER.error(e);
	} finally {
	    if (serviceProvider.isSessionActive()) {
		serviceProvider.disconnect();
	    }
	}

    }

    protected static Base createBase(String baseId, String description,
	    String[] catNames) {

	Base base = serviceProvider.getBaseAdministrationService().getBase(
		baseId);

	if (base != null) {
	    deleteBase(base);
	}
	base = ToolkitFactory.getInstance().createBase(baseId);

	for (int i = 0; i < catNames.length; i++) {
	    Category category = serviceProvider
		    .getStorageAdministrationService().findOrCreateCategory(
			    catNames[i], CategoryDataType.STRING);

	    BaseCategory baseCategory = ToolkitFactory.getInstance()
		    .createBaseCategory(category, true);
	    baseCategory.setMaximumValues((short) 10);
	    baseCategory.setEnableDictionary(false);

	    base.addBaseCategory(baseCategory);
	}

	try {
	    serviceProvider.getBaseAdministrationService().createBase(base);
	} catch (ObjectAlreadyExistsException e) {
	    e.printStackTrace();
	    fail("base : " + base.getBaseId() + " already exists");
	}
	serviceProvider.getBaseAdministrationService().startBase(base);

	return base;
    }

    @Test
    public void testDifferentDomains() throws Exception {

	Map<String, String> catValues = new HashMap<String, String>();
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base2 = serviceProvider.getBaseAdministrationService().getBase(
		BASE2);
	storeDoc(base2, "docA", catValues);
	serviceProvider.disconnect();

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL2);
	Base base3 = serviceProvider.getBaseAdministrationService().getBase(
		BASE2);
	storeDoc(base3, "docB", catValues);

	List<Document> docs = null;
	docs = searchMulti(base3, base3.getBaseCategory(CATA)
		.getFormattedName() + ":" + catValues.get(CATA), 1000, null);

	assertNotNull(docs);
	assertEquals(2, docs.size());

	/*
	 * On extrait les fichiers de manière "classique"
	 */
	for (Document doc : docs) {
	    System.out.println("Extraction du document " + doc.getTitle());

	    // On réussit bien à extraire le fichier
	    InputStream documentFile = serviceProvider.getStoreService()
		    .getDocumentFile(doc);
	    assertNotNull(documentFile);
	    documentFile.close();
	}
	serviceProvider.disconnect();

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL);
	base2 = serviceProvider.getBaseAdministrationService().getBase(BASE2);
	/*
	 * On recherche à nouveau
	 */
	docs = searchMulti(base2, base2.getBaseCategory(CATA)
		.getFormattedName() + ":" + catValues.get(CATA), 1000, null);
	assertNotNull(docs);
	assertEquals(2, docs.size());

	for (Document doc : docs) {
	    System.out.println("Extraction du document " + doc.getTitle());

	    // On réussit bien à extraire le fichier
	    InputStream documentFile = serviceProvider.getStoreService()
		    .getDocumentFile(doc);
	    assertNotNull(documentFile);
	    documentFile.close();
	}
	serviceProvider.disconnect();
    }

    @Test
    public void testDocumentByUUIDDifferentDomains() {
	Map<String, String> catValues = new HashMap<String, String>();
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base2 = serviceProvider.getBaseAdministrationService().getBase(
		BASE2);
	Document storeDoc = storeDoc(base2, "docA", catValues);

	UUID uuid = storeDoc.getUuid();
	Document documentByUUID = serviceProvider.getSearchService()
		.getDocumentByUUID(base2, uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());
	serviceProvider.disconnect();

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL2);
	base2 = serviceProvider.getBaseAdministrationService().getBase(BASE2);
	documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(
		base2, uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());

	serviceProvider.disconnect();
    }

    @Test
    public void testDocumentByUUIDDifferentDomainsMultibase() {
	Map<String, String> catValues = new HashMap<String, String>();
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base2 = serviceProvider.getBaseAdministrationService().getBase(
		BASE2);
	Document storeDoc = storeDoc(base2, "docA", catValues);

	UUID uuid = storeDoc.getUuid();
	Document documentByUUID = serviceProvider.getSearchService()
		.getDocumentByUUIDMultiBase(uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());
	serviceProvider.disconnect();

	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, URL2);
	documentByUUID = serviceProvider.getSearchService()
		.getDocumentByUUIDMultiBase(uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());

	serviceProvider.disconnect();
    }

    private static Document storeDoc(Base target, String title,
	    Map<String, String> catValues) {
	Document document = ToolkitFactory.getInstance().createDocumentTag(
		target);

	document.setTitle(title);

	Set<BaseCategory> baseCategories = target.getBaseCategories();
	for (BaseCategory baseCategory : baseCategories) {
	    System.out.println("baseCategory : " + baseCategory.getName());
	}

	for (Map.Entry<String, String> ent : catValues.entrySet()) {
	    System.out.println(ent.getKey());
	    BaseCategory baseCategory = target.getBaseCategory(ent.getKey());
	    document.addCriterion(baseCategory, ent.getValue());
	}

	File newDoc = TestUtils.getFile("doc1.pdf");

	Document storeDoc = storeDocument(document, newDoc, true);
	if (document != null) {
	    storedDocs.add(storeDoc);
	}
	return storeDoc;
    }

    private static List<Document> searchMulti(Base target, String queryTxt,
	    int limit, Integer nbExpectedResults)
	    throws ExceededSearchLimitException, SearchQueryParseException {
	return searchMulti(target, queryTxt, limit, nbExpectedResults, null);
    }

    private static List<Document> searchMulti(Base target, String queryTxt,
	    int limit, Integer nbExpectedResults, ChainedFilter chainedFilter)
	    throws ExceededSearchLimitException, SearchQueryParseException {
	SearchResult searchResult = serviceProvider.getSearchService()
		.multiBaseSearch(queryTxt, limit, chainedFilter);
	if (nbExpectedResults != null) {
	    assertEquals((int) nbExpectedResults, searchResult.getTotalHits());
	}
	return searchResult.getDocuments();
    }

}
