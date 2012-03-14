package com.docubase.dfce.toolkit.base;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.commons.indexation.SystemFieldName;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.TestUtils;

public class MultibaseTest extends AbstractTestBase {
    private static final String CATA = "code_fournisseur";
    private static final String CATB = "no_serie";
    private static final String CATC = "prix";
    private static final String CATD = "facultatif";

    private static Base base1;
    private static Base base2;

    private static final String BASE1 = "BASE 1";
    private static final String BASE2 = "BASE 2";

    private static List<Document> storedDocs;
    private static Map<String, String> catValues;

    @BeforeClass
    public static void setUp() {
	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
	storedDocs = new ArrayList<Document>();

	/*
	 * base1 et 2 ont les 2 CATA, CATB et CATC mais pas dans le même ordre
	 * (on verra que celà n'a pas d'importance) base2 est la seule à avoir
	 * CATD.
	 */
	base1 = createBase(BASE1, "Base no 1 - dom1", new String[] { CATA,
		CATC, CATB });
	base2 = createBase(BASE2, "Base no 2 - dom1", new String[] { CATA,
		CATB, CATC, CATD });

	/*
	 * On stocke le "même" doc sur BASE1 et BASE2.
	 */
	catValues = new HashMap<String, String>();
	catValues.put(CATA, "Docubase");
	catValues.put(CATB, "EE35");
	catValues.put(CATC, "500Euros");

	storeDoc(base1, "doc1", catValues);
	storeDoc(base2, "doc2", catValues);
    }

    /**
     * @throws FrozenDocumentException
     * @see TestCase#tearDown()
     */
    @AfterClass
    public static void tearDown() throws FrozenDocumentException {
	for (Document document : storedDocs) {
	    serviceProvider.getStoreService()
		    .deleteDocument(document.getUuid());
	}
	deleteBase(base1);
	deleteBase(base2);

	serviceProvider.disconnect();
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
    public void testSimple() throws ExceededSearchLimitException, IOException,
	    SearchQueryParseException {

	/*
	 * On vérifie qu'on les trouve individuellement en requête monobase.
	 */
	Category categoryA = serviceProvider.getStorageAdministrationService()
		.getCategory(CATA);
	String query1 = categoryA.getName() + ":" + catValues.get(CATA);
	searchMono(base1, query1, 10, 1);

	String query2 = categoryA.getName() + ":" + catValues.get(CATA);
	searchMono(base2, query2, 10, 1);

	/*
	 * En requête multibase, on doit en trouver 2. (sauf si il y a une autre
	 * base dans le système) Et ce que l'on interroge la base1 ou la base2
	 */
	List<Document> docs = null;
	String query = query2;
	docs = searchMulti(base1, query, 1000, 2);
	assertEquals(2, docs.size());
	docs = searchMulti(base2, query, 1000, 2);
	assertEquals(2, docs.size());

	/*
	 * Meme recherche en passant par base1
	 */

	/*
	 * docs est ici alors une solution 'multibase'. On va chercher à
	 * extraire les documents
	 */
	Set<String> baseNames = new HashSet<String>();
	for (int i = 0; i < docs.size(); i++) {
	    Document doc = docs.get(i);
	    System.out.println("doc " + doc.getTitle() + " est dans la base: "
		    + doc.getBaseId());
	    // On vérifie bien que dans cette liste de solutions de 2 document,
	    // les 2 viennent d'une base différente
	    assertTrue(baseNames.add(doc.getBaseId()));

	    // On réussit bien à extraire le fichier
	    InputStream documentFile = serviceProvider.getStoreService()
		    .getDocumentFile(doc);

	    assertNotNull(documentFile);
	    documentFile.close();
	}

	/*
	 * On remarque que l'on peut revenir en requête monobase en précisant
	 * explicitement la base dans les filtres
	 */

	ChainedFilter chainedFilter = ToolkitFactory
		.getInstance()
		.createChainedFilter()
		.addTermFilter(SystemFieldName.SM_BASE_UUID.name(),
			base1.getUuid().toString());
	docs = searchMulti(base1, query, 50, 1, chainedFilter);
    }

    @Test(expected = ExceededSearchLimitException.class)
    public void testExceededSearchLimit() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	Category category = serviceProvider.getStorageAdministrationService()
		.getCategory(CATA);
	String query = category.getName() + ":Docubase";
	serviceProvider.getSearchService().multiBaseSearch(query, 100, null, 1);
    }

    private static void storeDoc(Base target, String title,
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
    }

    private static List<Document> searchMono(Base target, String queryTxt,
	    int limit, Integer nbExpectedResults, ChainedFilter chainedFilter)
	    throws ExceededSearchLimitException, SearchQueryParseException {
	SearchResult searchResult = serviceProvider.getSearchService().search(
		queryTxt, limit, target, chainedFilter);
	if (nbExpectedResults != null) {
	    assertEquals((int) nbExpectedResults, searchResult.getTotalHits());
	}
	return searchResult.getDocuments();
    }

    private static List<Document> searchMono(Base target, String queryTxt,
	    int limit, Integer nbExpectedResults)
	    throws ExceededSearchLimitException, SearchQueryParseException {
	return searchMono(target, queryTxt, limit, nbExpectedResults, null);
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
