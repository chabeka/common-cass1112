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
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.Base.DocumentCreationDateConfiguration;
import net.docubase.toolkit.model.base.Base.DocumentOverlayFormConfiguration;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultiDomainTest extends AbstractBaseTestCase {
    private static final Map<String, String> catValues = new HashMap<String, String>();

    private static final Logger LOGGER = Logger
	    .getLogger(MultiDomainTest.class);

    private static final String URL = AbstractBaseTestCase.SERVICE_URL;
    private static final String URL2 = AbstractBaseTestCase.SERVICE_URL;

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
	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	createBase(BASE2, "Base no 1 - dom1", new String[] { CATA, CATC, CATB });
	Authentication.closeSession();

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL2);
	createBase(BASE3, "Base no 2 - dom1", new String[] { CATA, CATB, CATC,
		CATD });
	Authentication.closeSession();
    }

    /**
     * @see TestCase#tearDown()
     */
    @AfterClass
    public static void tearDown() {
	try {
	    if (Authentication.isSessionActive()) {
		Authentication.closeSession();
	    }
	    Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	    for (Document document : storedDocs) {
		ServiceProvider.getStoreService().deleteDocument(
			document.getUuid());
	    }
	    Base base2 = ServiceProvider.getBaseAdministrationService()
		    .getBase(BASE2);
	    Base base3 = ServiceProvider.getBaseAdministrationService()
		    .getBase(BASE3);
	    deleteBase(base2);
	    deleteBase(base3);
	} catch (Exception e) {
	    LOGGER.error(e);
	} finally {
	    if (Authentication.isSessionActive()) {
		Authentication.closeSession();
	    }
	}

    }

    protected static Base createBase(String baseId, String description,
	    String[] catNames) {

	Base base = ServiceProvider.getBaseAdministrationService().getBase(
		baseId);

	if (base != null) {
	    deleteBase(base);
	}
	base = ToolkitFactory.getInstance().createBase(baseId);

	// Déclare une date de création disponible mais optionnell
	base.setDocumentCreationDateConfiguration(DocumentCreationDateConfiguration.OPTIONAL);
	// Pas de fond de page
	base.setDocumentOverlayFormConfiguration(DocumentOverlayFormConfiguration.NONE);
	// Pas de groupe de document
	base.setDocumentOwnerDefault(Base.DocumentOwnerType.PUBLIC);
	// Le propriétaire d'un document n'est pas modifiable à postériori de
	// son injection
	base.setDocumentOwnerModify(false);

	for (int i = 0; i < catNames.length; i++) {
	    Category category = ServiceProvider
		    .getStorageAdministrationService().findOrCreateCategory(
			    catNames[i], CategoryDataType.STRING);

	    BaseCategory baseCategory = ToolkitFactory.getInstance()
		    .createBaseCategory(category, true);
	    baseCategory.setMaximumValues((short) 10);
	    baseCategory.setEnableDictionary(false);

	    base.addBaseCategory(baseCategory);
	}

	try {
	    ServiceProvider.getBaseAdministrationService().createBase(base);
	} catch (ObjectAlreadyExistsException e) {
	    e.printStackTrace();
	    fail("base : " + base.getBaseId() + " already exists");
	}
	ServiceProvider.getBaseAdministrationService().startBase(base);

	return base;
    }

    @Test
    public void testDifferentDomains() throws Exception {

	Map<String, String> catValues = new HashMap<String, String>();
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base2 = ServiceProvider.getBaseAdministrationService().getBase(
		BASE2);
	storeDoc(base2, "docA", catValues);
	Authentication.closeSession();

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL2);
	Base base3 = ServiceProvider.getBaseAdministrationService().getBase(
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
	    InputStream documentFile = ServiceProvider.getStoreService()
		    .getDocumentFile(doc);
	    assertNotNull(documentFile);
	    documentFile.close();
	}
	Authentication.closeSession();

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	base2 = ServiceProvider.getBaseAdministrationService().getBase(BASE2);
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
	    InputStream documentFile = ServiceProvider.getStoreService()
		    .getDocumentFile(doc);
	    assertNotNull(documentFile);
	    documentFile.close();
	}
	Authentication.closeSession();
    }

    @Test
    public void testDocumentByUUIDDifferentDomains() {
	Map<String, String> catValues = new HashMap<String, String>();
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base2 = ServiceProvider.getBaseAdministrationService().getBase(
		BASE2);
	Document storeDoc = storeDoc(base2, "docA", catValues);

	UUID uuid = storeDoc.getUuid();
	Document documentByUUID = ServiceProvider.getSearchService()
		.getDocumentByUUID(base2, uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());
	Authentication.closeSession();

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL2);
	base2 = ServiceProvider.getBaseAdministrationService().getBase(BASE2);
	documentByUUID = ServiceProvider.getSearchService().getDocumentByUUID(
		base2, uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());

	Authentication.closeSession();
    }

    @Test
    public void testDocumentByUUIDDifferentDomainsMultibase() {
	Map<String, String> catValues = new HashMap<String, String>();
	catValues.put(CATA, "DifferentDomains");
	catValues.put(CATB, "EE74");
	catValues.put(CATC, "50000Euros");

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base2 = ServiceProvider.getBaseAdministrationService().getBase(
		BASE2);
	Document storeDoc = storeDoc(base2, "docA", catValues);

	UUID uuid = storeDoc.getUuid();
	Document documentByUUID = ServiceProvider.getSearchService()
		.getDocumentByUUIDMultiBase(uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());
	Authentication.closeSession();

	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL2);
	documentByUUID = ServiceProvider.getSearchService()
		.getDocumentByUUIDMultiBase(uuid);
	assertNotNull(documentByUUID);
	assertEquals(uuid, documentByUUID.getUuid());

	Authentication.closeSession();
    }

    private static Document storeDoc(Base target, String title,
	    Map<String, String> catValues) {
	Document document = ToolkitFactory.getInstance().createDocumentTag(
		target);

	document.setTitle(title);
	document.setType("PDF");

	Set<BaseCategory> baseCategories = target.getBaseCategories();
	for (BaseCategory baseCategory : baseCategories) {
	    System.out.println("baseCategory : " + baseCategory.getName());
	}

	for (Map.Entry<String, String> ent : catValues.entrySet()) {
	    System.out.println(ent.getKey());
	    BaseCategory baseCategory = target.getBaseCategory(ent.getKey());
	    document.addCriterion(baseCategory, ent.getValue());
	}

	File newDoc = getFile("doc1.pdf", AbstractBaseTestCase.class); // le

	Document storeDoc = storeDoc(document, newDoc, true);
	if (document != null) {
	    storedDocs.add(storeDoc);
	}
	return storeDoc;
    }

    private static List<Document> searchMulti(Base target, String queryTxt,
	    int limit, Integer nbExpectedResults)
	    throws ExceededSearchLimitException {
	return searchMulti(target, queryTxt, limit, nbExpectedResults, null);
    }

    private static List<Document> searchMulti(Base target, String queryTxt,
	    int limit, Integer nbExpectedResults, ChainedFilter chainedFilter)
	    throws ExceededSearchLimitException {
	SearchResult searchResult = ServiceProvider.getSearchService()
		.multiBaseSearch(queryTxt, limit, chainedFilter);
	if (nbExpectedResults != null) {
	    assertEquals((int) nbExpectedResults, searchResult.getTotalHits());
	}
	return searchResult.getDocuments();
    }

}
