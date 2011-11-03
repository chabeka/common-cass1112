package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.ChainedFilter.ChainedFilterOperator;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;
import net.docubase.toolkit.service.ged.SearchService.MetaData;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

@RunWith(JUnit4.class)
public class RichGedTest extends AbstractTestCaseCreateAndPrepareBase {
    public final static Double pi = Double.valueOf("3.1415926535");

    private static final String[] catNames = { "Catégorie zéro",
	    "Catégorie un", "Catégorie deux", "Cat booléenne", "Cat entière",
	    "Cat décimale", "Cat date", "Cat date et heure" };

    private static ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

    private void control(Document doc, File newDoc, String c0)
	    throws IOException {
	// L'instance de doc à ce stade contient le documentInformation
	assertNotNull(doc);

	// getCriterionList renvoie la liste des catégories C0, C1, etc...
	List<Criterion> criterionList = doc.getCriterions(base
		.getBaseCategory(catNames[0]));
	assertEquals(1, criterionList.size());
	assertEquals(c0, criterionList.get(0).getWord());

	// Une autre vérification sur le tag : les 3 valeurs décimales sur C5
	// On va juste chercher si l'une d'entre elle est Pi.
	List<Criterion> c5s = doc.getCriterions(base
		.getBaseCategory(catNames[5]));
	assertEquals(3, c5s.size());
	boolean foundPi = false;
	for (int i = 0; i < c5s.size() && !foundPi; i++) {
	    foundPi = c5s.get(i).getWord().equals(pi);
	}
	assertTrue(foundPi);

	// Cet appel, la 1ère fois (lazy loading) va extraire le document.
	InputStream documentFile = serviceProvider.getStoreService()
		.getDocumentFile(doc);
	// le document extrait (dans une zone temporaire) doit avoir la même
	// taille que le document utilisé avant injection.

	assertEquals(DigestUtils.shaHex(new FileInputStream(newDoc)),
		DigestUtils.shaHex(documentFile));
	documentFile.close();
    }

    @Test
    public void testEscapeCharacterParenthesis() throws TagControlException,
	    FileNotFoundException, ExceededSearchLimitException,
	    SearchQueryParseException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(catNames[0], UUID.randomUUID().toString());
	document.addCriterion(catNames[1], "(test)");

	File file = TestUtils.getFile("doc1.pdf");
	InputStream inputStream = new FileInputStream(file);
	document = serviceProvider.getStoreService().storeDocument(document,
		"doc1", "pdf", inputStream);

	UUID insertedUUID = document.getUuid();
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);

	SearchResult searchResult = serviceProvider.getSearchService().search(
		baseCategory1.getFormattedName() + ":\\(test\\)", 10, base);
	assertEquals(1, searchResult.getTotalHits());
	Document document2 = searchResult.getDocuments().get(0);
	assertEquals(insertedUUID, document2.getUuid());
	assertEquals("(test)", document2.getSingleCriterion(catNames[1])
		.getWord());
    }

    @Test
    public void testEscapeCharacterFieldGrouping() throws TagControlException,
	    FileNotFoundException, ExceededSearchLimitException,
	    SearchQueryParseException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(catNames[0], UUID.randomUUID().toString());
	document.addCriterion(catNames[1], "testEscapeCharacterFieldGrouping1");
	document.addCriterion(catNames[1], "testEscapeCharacterFieldGrouping2");

	File file = TestUtils.getFile("doc1.pdf");
	InputStream inputStream = new FileInputStream(file);
	document = serviceProvider.getStoreService().storeDocument(document,
		"doc1", "pdf", inputStream);

	UUID insertedUUID = document.getUuid();
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);

	SearchResult searchResult = serviceProvider
		.getSearchService()
		.search(baseCategory1.getFormattedName()
			+ ":(+testEscapeCharacterFieldGrouping1 +testEscapeCharacterFieldGrouping2)",
			10, base);
	assertEquals(1, searchResult.getTotalHits());
	Document document2 = searchResult.getDocuments().get(0);
	assertEquals(insertedUUID, document2.getUuid());
    }

    @Test
    public void testEscapeCharacterWithJoker() throws TagControlException,
	    FileNotFoundException, ExceededSearchLimitException,
	    SearchQueryParseException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(catNames[0], UUID.randomUUID().toString());
	document.addCriterion(catNames[1], "+test&&");

	File file = TestUtils.getFile("doc1.pdf");
	InputStream inputStream = new FileInputStream(file);
	document = serviceProvider.getStoreService().storeDocument(document,
		"doc1", "pdf", inputStream);

	UUID insertedUUID = document.getUuid();
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);

	SearchResult searchResult = serviceProvider.getSearchService().search(
		baseCategory1.getFormattedName() + ":\\+test*", 10, base);
	assertEquals(1, searchResult.getTotalHits());
	Document document2 = searchResult.getDocuments().get(0);
	assertEquals(insertedUUID, document2.getUuid());
	assertEquals("+test&&", document2.getSingleCriterion(catNames[1])
		.getWord());
    }

    @Test
    public void testPlusAndMinusSearch() throws FileNotFoundException,
	    TagControlException, ExceededSearchLimitException,
	    SearchQueryParseException {
	Document document1 = ToolkitFactory.getInstance().createDocumentTag(
		base);
	document1.addCriterion(catNames[0], UUID.randomUUID().toString());
	document1.addCriterion(catNames[1], "testPlusAndMinusSearch");
	document1.addCriterion(catNames[2], "CAT2");

	File file = TestUtils.getFile("doc1.pdf");
	InputStream inputStream = new FileInputStream(file);
	document1 = serviceProvider.getStoreService().storeDocument(document1,
		"doc1", "pdf", inputStream);

	Document document2 = ToolkitFactory.getInstance().createDocumentTag(
		base);
	document2.addCriterion(catNames[0], UUID.randomUUID().toString());
	document2.addCriterion(catNames[1], "testPlusAndMinusSearch");
	document2.addCriterion(catNames[2], "WRONGCAT2");

	UUID insertedUUID = document1.getUuid();
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
	BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);

	SearchResult searchResult = serviceProvider.getSearchService().search(
		"+" + baseCategory1.getFormattedName()
			+ ":testPlusAndMinusSearch -"
			+ baseCategory2.getFormattedName() + ":WRONGCAT2", 10,
		base);
	assertEquals(1, searchResult.getTotalHits());
	assertEquals(insertedUUID, searchResult.getDocuments().get(0).getUuid());
    }

    @Test
    public void testNotSearch() throws FileNotFoundException,
	    TagControlException, ExceededSearchLimitException,
	    SearchQueryParseException {
	Document document1 = ToolkitFactory.getInstance().createDocumentTag(
		base);
	document1.addCriterion(catNames[0], UUID.randomUUID().toString());
	document1.addCriterion(catNames[1], "testNotSearch");
	document1.addCriterion(catNames[2], "CATNOT2");

	File file = TestUtils.getFile("doc1.pdf");
	InputStream inputStream = new FileInputStream(file);
	document1 = serviceProvider.getStoreService().storeDocument(document1,
		"doc1", "pdf", inputStream);

	Document document2 = ToolkitFactory.getInstance().createDocumentTag(
		base);
	document2.addCriterion(catNames[0], UUID.randomUUID().toString());
	document2.addCriterion(catNames[1], "testNotSearch");
	document2.addCriterion(catNames[2], "WRONGCATNOT2");

	UUID insertedUUID = document1.getUuid();
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
	BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);

	SearchResult searchResult = serviceProvider.getSearchService().search(
		baseCategory1.getFormattedName() + ":testNotSearch AND NOT "
			+ baseCategory2.getFormattedName() + ":WRONGCATNOT2",
		10, base);
	assertEquals(1, searchResult.getTotalHits());
	assertEquals(insertedUUID, searchResult.getDocuments().get(0).getUuid());
    }

    /**
     * On va retrouver les documents stockés avec des requêtes GRC portant sur
     * les catégories typées. Puis avec les requêtes Lucene
     * 
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * 
     * @throws IOException
     */
    @Test
    public void testTyped() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

	BaseCategory c0 = base.getBaseCategory(catNames[0]);
	BaseCategory cBoolean = base.getBaseCategory(catNames[3]);

	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(c0, "MyBooleanTest");
	document.addCriterion(cBoolean, true);

	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	/*
	 * On injecte le nom du champs filtré par formatFieldName dans la
	 * requête
	 */
	String c3FormattedName = cBoolean.getFormattedName();
	assertEquals(1, searchLucene(c3FormattedName + ":true", 5));
	assertEquals(0, searchLucene(c3FormattedName + ":false", 5));

	// Integer
	BaseCategory baseCategoryInteger = base.getBaseCategory(catNames[4]);
	document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(c0, "MyIntegerTest");
	document.addCriterion(baseCategoryInteger, 10);

	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	String c4FormattedName = baseCategoryInteger.getFormattedName();
	assertEquals(1, searchLucene(c4FormattedName + ":10", 5));
	assertEquals(0, searchLucene(c4FormattedName + ":11", 5));

	// Decimal
	BaseCategory decimalBaseCategory = base.getBaseCategory(catNames[5]);
	document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(c0, "MyDecimalTest");
	document.addCriterion(decimalBaseCategory, 3.14);

	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	String c5FName = decimalBaseCategory.getFormattedName();
	assertEquals(1, searchLucene(c5FName + ":3.14", 5));
	assertEquals(0, searchLucene(c5FName + ":3.1459", 5));

	// Date et DateHeure
	Date currDate = new Date();
	String strDate = serviceProvider.getSearchService().formatDate(
		currDate, DateFormat.DATE);
	String strDateTime = serviceProvider.getSearchService().formatDate(
		currDate, DateFormat.DATETIME);
	BaseCategory dateBaseCategory = base.getBaseCategory(catNames[6]);

	document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(c0, "MyDateTest");
	document.addCriterion(dateBaseCategory, currDate);

	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	String c6FName = dateBaseCategory.getFormattedName();
	assertEquals(1, searchLucene(c6FName + ":" + strDate, 5));
	assertEquals(0, searchLucene(c6FName + ":1975-01-01", 5));

	BaseCategory dateTimeBaseCategory = base.getBaseCategory(catNames[7]);
	document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(c0, "MyDateTimeTest");
	document.addCriterion(dateTimeBaseCategory, currDate);
	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	/*
	 * Requêtes un peu plus compliquées
	 */
	String lucene = null;

	/*
	 * En Lucene sans le strDateTime
	 */
	lucene = "(" + c3FormattedName + ":true OR " + c4FormattedName + ":10"
		+ " OR " + c5FName + ":3.14 OR " + c6FName + ":" + strDate
		+ ")";

	assertEquals(4, searchLucene(lucene, 10));

	String c0FName = c0.getFormattedName();
	lucene = c0FName + ":My*";
	assertEquals(5,
		serviceProvider.getSearchService().search(lucene, 100, base)
			.getTotalHits());

	// lucene = c0FName + ":My*Test";
	// assertEquals(5, searchLucene(lucene, 10));

	/*
	 * Nouveauté ici : on recherche "Cat 7 - DateHeure" qui doit être égale
	 * à par exemple "1975-01-01 08-32". Le nom de la catégorie doit tjs
	 * être protégé. Mais on doit aussi mettre des "" autour du terme
	 * recherché. Pour obtenir des requêtes du type
	 * cat 7 - dateheure:"1975-01-01 08-32"
	 * 
	 * Il faut donc protéger le caractère "
	 * 
	 * Nouveauté: on peut directement récupérer le nom formatté.
	 */
	String c7Name = dateTimeBaseCategory.getFormattedName();
	assertEquals(0, searchLucene(c7Name + ":\"1975-01-01 08-32\"", 5));
	assertEquals(1, searchLucene(c7Name + ":\"" + strDateTime + "\"", 5));
    }

    /**
     * On stocke un document en précisant un UUID, et on vérifie que l'on le
     * récupère bien dans la liste de solution (DocumentInformation) et dans le
     * tag.
     * 
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * 
     * @throws IOException
     */
    @Test
    public void testProvidedUUID() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

	BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);

	/*
	 * On fournit explicitement un UUID
	 */
	UUID uuidFourni = UUID.randomUUID();
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "UUIDFourni");
	document.setUuid(uuidFourni);
	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	/*
	 * On recherche le document par sa catégorie C0:UUIDFourni
	 */
	List<Document> docs = serviceProvider
		.getSearchService()
		.search(baseCategory0.getFormattedName() + ":UUIDFourni", 5,
			base).getDocuments();

	assertTrue(docs != null && docs.size() == 1);
	Document doc = docs.get(0);
	assertEquals(uuidFourni.toString(), doc.getUuid().toString());

	/*
	 * On recherche également par cet UUIDFourni Comme c'est un champs
	 * statique du tag c'est dans LucRef.
	 */
	docs = serviceProvider
		.getSearchService()
		.search(MetaData.UUID.getFormattedName() + ":"
			+ uuidFourni.toString(), 5, base).getDocuments();
	assertTrue(docs != null && docs.size() == 1);
	doc = docs.get(0);
    }

    /**
     * On stocke un document sans préciser d'UUID et on vérifie qu'il n'y en a
     * bien eu un généré et qu'il est récupéré à l'identique dans le
     * DocumentInformation et le Tag
     * 
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * 
     * @throws IOException
     */
    @Test
    public void testNoProvidedUUID() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

	BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);

	/*
	 * On fournit explicitement un UUID
	 */
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(baseCategory0, "UUIDNonFourni");
	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	/*
	 * On recherche le document
	 */
	List<Document> docs = serviceProvider
		.getSearchService()
		.search(baseCategory0.getFormattedName() + ":UUIDNonFourni", 5,
			base).getDocuments();
	assertTrue(docs != null && docs.size() == 1);
	Document doc = docs.get(0);
	/*
	 * On vérifie dans le documentInformation, dans Tag, et on compare
	 */
	assertNotNull(doc.getUuid());
    }

    /**
     * Ce test montre des requêtes consécutives en jouant sur l'offsetInIndex de
     * départ. Ainsi on peut en plusieurs requêtes successives peut couteuses
     * remonter une grosse liste de solution en faisant varier cet offsetInIndex
     * de départ.
     * 
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * 
     * @throws IOException
     */
    @Test
    public void testSearchWithOffset() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);

	/*
	 * On va stocker 100 documents, avec C0 qui change à chaque fois et C1
	 * qui contient le nom du test.
	 */
	for (int i = 0; i < 100; i++) {
	    Document document = toolkitFactory.createDocumentTag(base);
	    // C0 unique
	    document.addCriterion(baseCategory0, "TestOffset" + i);
	    // C1 ne bouge pas
	    document.addCriterion(baseCategory1, "TestOffset");

	    // stockage
	    storeDocument(document, TestUtils.getFile("doc1.pdf"), true);
	}

	/*
	 * On a donc en théorie 100 documents avec C1=TestOffset.
	 * 
	 * Si on demande 100 documents, alors après avoir effectué la requête,
	 * l'AMF va lire sur l'index Lucene 100 tags.
	 * 
	 * On peut décider d'en demander seulement 10 par exemple (c'est à dire
	 * avoir une liste de solution sur les 10 premiers), et de pouvoir en
	 * demander 10 de plus.
	 * 
	 * Ensuite, on décide d'extraire ou non selon ce que nous dit notre
	 * morceaux de liste de solution.
	 * 
	 * On souhaite donc dire
	 * "je veux les 10 premiers éléments de la liste de solution",
	 * "je veux les 10 suivants", "je veux les 10 suivants".
	 * 
	 * A chaque fois, Lucene restituera des "documents Lucene" dans le même
	 * ordre. L'AMF choisira de ne lire (depuis l'index Lucene) qu'entre
	 * offsetInIndex et offsetInIndex + limit.
	 * 
	 * L'API toolkit fourni une recherche avec offsetInIndex + limit.
	 */
	Set<UUID> numDocMet = new HashSet<UUID>();
	String queryText = baseCategory1.getFormattedName() + ":TestOffset";
	for (int i = 0; i < 10; i++) {
	    int offset = i * 10;

	    /*
	     * L'objet résult contient une liste de Solution remontée (ici ce
	     * sera 10 maximum) et le nombre théorique de documents vérifiant la
	     * requête (ici ce sera tjs 100)
	     */
	    SearchResult searchResult = serviceProvider.getSearchService()
		    .search(queryText, 10, offset, base, null);
	    assertEquals(searchResult.getTotalHits(), 100);
	    assertNotNull(searchResult.getDocuments());
	    assertEquals(10, searchResult.getDocuments().size());
	    for (Document document : searchResult.getDocuments()) {
		// On se rend compte que l'on ne rencontre chaque document
		// qu'une seule fois
		// dans toutes les itérations de recherche.
		assertTrue(numDocMet.add(document.getUuid()));
	    }
	}
	assertEquals(100, numDocMet.size());
    }

    /**
     * Ce test montre l'utilisation des FilterTerm (en mode AND) et aussi la
     * requete CompleteQuery qui expose le maximum de choses
     * 
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * 
     * @throws IOException
     */
    @Test
    public void testCompleteQueryScenarii()
	    throws ExceededSearchLimitException, SearchQueryParseException {
	System.out.println("****** testCompleteQueryScenarii ******");
	BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
	BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);
	BaseCategory baseCategoryAge = base.getBaseCategory(catNames[4]);

	/*
	 * On va stocker n documents, avec C0 qui change à chaque fois, et C1
	 * qui prend peu de valeurs. On joue aussi avec C2 multivalué;)
	 */

	for (int i = 0; i < 50; i++) {
	    Document document = toolkitFactory.createDocumentTag(base);

	    // C0 unique
	    document.addCriterion(baseCategory0, "testfilter" + i);

	    // C1 soit Enfant, soit Adulte
	    String c1Val = null;
	    if (i < 10) { // Les enfants d'abord
		c1Val = "enfant";
		document.addCriterion(baseCategoryAge, i % 2 == 0 ? "3" : "7");
	    } else {
		c1Val = "adulte";
		document.addCriterion(baseCategoryAge, i % 2 == 0 ? "23" : "47");
	    }
	    document.addCriterion(baseCategory1, c1Val);

	    // C2. 2 valeurs, une qui varie très peu, une qui est unique.
	    document.addCriterion(baseCategory2, "personne" + i);
	    document.addCriterion(baseCategory2, i % 2 == 0 ? "masculin"
		    : "feminin");

	    // stockage
	    storeDocument(document, TestUtils.getFile("doc1.pdf"), true);
	}

	// Recherche sans filtre pour vérifier le nombre de documents stockés
	// assertEquals( 50, searchLucene(c0.getFormattedName()+":testfilter*",
	// 1000));

	/*
	 * On veut tout les adultes de sexe masculin. Celà doit représenter 20
	 * personnes.
	 * 
	 * On a plusieurs façons de chercher. On va considérer ici qu'il n'y a
	 * que les documents précédemment injectés (on n'applique plus
	 * TestFilter* sur C0) On va chercher à la fois sur C1 et C2.
	 * 
	 * 1/ On recherche C1=Adulte ET C2=Masculin sans utiliser les filtres.
	 * 
	 * 2/ On recherche C1=Adulte et filtre sur C2.
	 * 
	 * 3/ On recherche C2=Masculin et filtre sur C1.
	 * 
	 * 4/ On recherche avec filtre C1 et C2 (on doit donc avec une requête
	 * lambda : C0:TestFilter* par exemple)
	 */
	// 1/
	String query = baseCategoryAge.getFormattedName() + ":3";
	assertEquals(5, searchLucene(query, 1000, null));

	// 2/
	query = baseCategory1.getFormattedName() + ":adulte AND "
		+ baseCategory2.getFormattedName() + ":masculin";
	// query = "_bUUID:" + base.getDescription().getUUID().toString();
	ChainedFilter chainedFilter = ToolkitFactory.getInstance()
		.createChainedFilter();
	chainedFilter.addTermFilter(baseCategory2.getFormattedName(),
		"masculin");
	assertEquals(20, searchLucene(query, 1000, chainedFilter));

	// 3/
	query = baseCategory2.getFormattedName() + ":masculin";
	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
		ChainedFilterOperator.AND);
	// assertEquals( 20, searchLucene(query, 1000, new
	// FilterTerm(c1.getFormattedName(), "adulte" )));
	assertEquals(20, searchLucene(query, 1000, chainedFilter));

	// 4/
	query = baseCategory0.getFormattedName() + ":testfilter*";
	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
		ChainedFilterOperator.AND).addTermFilter(
		baseCategory2.getFormattedName(), "masculin",
		ChainedFilterOperator.AND);
	// assertEquals( 20, searchLucene(query, 1000,
	// new FilterTerm(c1.getFormattedName(), "adulte" ),
	// new FilterTerm(c2.getFormattedName(), "masculin" )
	// )
	// );

	assertEquals(20, searchLucene(query, 1000, chainedFilter));

	// 5/
	query = baseCategory1.getFormattedName() + ":enfant";
	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter.addIntRangeFilter(baseCategoryAge.getFormattedName(), 0,
		5, true, true, ChainedFilterOperator.AND);
	// assertEquals( 5, searchLucene(query, 1000, new
	// FilterTerm(age.getFormattedName(), "[ 0 TO 5 ]" )));
	assertEquals(5, searchLucene(query, 1000, chainedFilter));

	// 6/
	query = baseCategory1.getFormattedName() + ":adulte";
	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter.addIntRangeFilter(baseCategoryAge.getFormattedName(), 23,
		30, true, true, ChainedFilterOperator.AND);
	// assertEquals( 20, searchLucene(query, 1000, new
	// FilterTerm(age.getFormattedName(), "[ 23 TO 30 ]" )));

	assertEquals(20, searchLucene(query, 1000, chainedFilter));
	/*
	 * On va utiliser la requête COMPLETE Dans un premier temps pour refaire
	 * la meme chose qu'en 4/
	 */
	SearchResult result = null;
	query = baseCategory0.getFormattedName() + ":testfilter*";
	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
		ChainedFilterOperator.AND).addTermFilter(
		baseCategory2.getFormattedName(), "masculin",
		ChainedFilterOperator.AND);

	// complete.addChainedFilter(chainedFilter2);

	// complete.createFilterRoot(NodeType.AND);
	// OperatorNode root = complete.getFilterRoot();
	// root.addFilterTerm(c1.getFormattedName(), "adulte" );
	// root.addFilterTerm(c2.getFormattedName(), "masculin" );

	/*
	 * On peut afficher la requête
	 */
	System.out.println("requête \"les adultes de sexe masculin\" :\n"
		+ query);

	result = serviceProvider.getSearchService().search(query, 1000, base,
		chainedFilter);
	assertNotNull(result.getDocuments());
	assertEquals(20, result.getDocuments().size());

	/*
	 * Ensuite on va faire un OU adulte OU masculin doit renvoyer 40 + 5 :
	 * 45
	 */
	query = baseCategory0.getFormattedName() + ":testfilter*";

	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
		ChainedFilterOperator.OR).addTermFilter(
		baseCategory2.getFormattedName(), "masculin",
		ChainedFilterOperator.OR);

	System.out
		.println("requête \"les adultes ou les individus de sexe masculin\" :\n"
			+ query);

	result = serviceProvider.getSearchService().search(query, 1000, base,
		chainedFilter);
	assertNotNull(result.getDocuments());
	assertEquals(45, result.getDocuments().size());

	/*
	 * On va faire une requête plus complexe
	 * "les femmes et les enfants d'abord" et "la personne 49" et
	 * "la personne 48".
	 * 
	 * c1!=adulte or c2=feminin or c2=personne48 or c2=personne49
	 * 
	 * Root: OR Fils simples: c2=feminin, c2=personne48, c2=personne49 Fils
	 * : Operator NAND avec un fils c1=adulte
	 * 
	 * 48 + 49. Hors eux il y a 38 adultes donc 19 femmes. Il y a 10
	 * enfants. 31.
	 */
	query = baseCategory0.getFormattedName() + ":testfilter*";

	chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
	chainedFilter
		.addTermFilter(baseCategory2.getFormattedName(), "feminin",
			ChainedFilterOperator.OR)
		.addTermFilter(baseCategory2.getFormattedName(), "personne48",
			ChainedFilterOperator.OR)
		.addTermFilter(baseCategory2.getFormattedName(), "personne49",
			ChainedFilterOperator.OR)
		.addTermFilter(baseCategory1.getFormattedName(), "enfant",
			ChainedFilterOperator.ANDNOT);

	// On doit maintenant conserver les références quand on va plus loin que
	// le root.
	// OperatorNode sub = root.addEmptyNode(NodeType.ANDNOT) ;
	// sub.addFilterTerm(c1.getFormattedName(), "adulte");
	System.out
		.println("Requête \"les femmes et les enfants d'abord ainsi que les passagers 48 et 49 qui sont des VIPs\" :\n "
			+ query);

	result = serviceProvider.getSearchService().search(query, 1000, base,
		chainedFilter);
	assertNotNull(result.getDocuments());
	assertEquals(21, result.getDocuments().size());

    }

    /**
     * Ce test montre la récupération de l'index métier sans passer par
     * l'extraction de tag. On stocke un document, et on récupère les catégories
     * directement depuis la liste de solution.
     * 
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * 
     * @throws IOException
     */
    @Test
    public void testGetCategoriesWithoutExtract()
	    throws ExceededSearchLimitException, SearchQueryParseException {

	BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
	BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
	BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);

	Document document = toolkitFactory.createDocumentTag(base);

	String c0Val = "testGetCategoriesWithoutExtract1";
	String c1Val = "Bien sur nous sommes d'accord";
	String c2Val = "Jérome Kerviel doit prendre cher!";

	document.addCriterion(baseCategory0, c0Val);
	document.addCriterion(baseCategory1, c1Val);
	document.addCriterion(baseCategory2, c2Val);

	storeDocument(document, TestUtils.getFile("doc1.pdf"), true);

	SearchResult searchResult = serviceProvider.getSearchService().search(
		baseCategory0.getFormattedName() + ":" + c0Val, 10, base);
	List<Document> docs = searchResult.getDocuments();
	assertNotNull(docs);
	assertEquals(1, docs.size());

	Document doc = docs.get(0);
	List<Criterion> crits = doc.getCriteria(baseCategory0);
	assertNotNull(crits);
	assertEquals(1, crits.size());
	assertEquals(c0Val, crits.get(0).getWord());

	crits = doc.getCriteria(baseCategory1);
	assertNotNull(crits);
	assertEquals(1, crits.size());
	assertEquals(c1Val, crits.get(0).getWord());

	Criterion critC2 = doc.getFirstCriterion(baseCategory2);
	assertEquals(c2Val, critC2.getWord());
    }

    /**
     * Test du stockage d'un document avec note et catégories mono/multivaluées
     * et typées. Les recherches sont réalisées ici à la fois dans l'index GRC
     * et l'index LUCENE
     * 
     * @throws IOException
     * @throws ExceededSearchLimitException
     * @throws SearchQueryParseException
     * @throws CustomTagControlException
     */
    @Test
    public void testStoreAndReturnDoc() throws IOException,
	    TagControlException, ExceededSearchLimitException,
	    SearchQueryParseException {
	// assertTrue("La base " + BASEID + " n'est pas démarrée.",
	// base.isStarted());

	// voir l'astuce pour récupérer le classPath au runtime pour localiser
	// les fichiers
	File newDoc = TestUtils.getFile("doc1.pdf");

	assertTrue(newDoc.exists());

	// On définit le Tag du futur document, lié à la base uBase.
	Document document = toolkitFactory.createDocumentTag(base);

	// On dit que l'on veut mettre "Identifier" en valeur d'identifiant de
	// la 1ère catégorie (d'indice 0)
	String c0 = "Identifier";

	document.addCriterion(base.getBaseCategory(catNames[0]), c0);

	// C1
	document.addCriterion(base.getBaseCategory(catNames[1]), "C1 val");

	// Plusieurs valeurs pour C2
	BaseCategory c2 = base.getBaseCategory(catNames[2]);
	for (int i = 1; i < 5; i++) {
	    document.addCriterion(c2, "C2 val" + i);
	}

	/*
	 * Valeurs typées BOOLEAN, INTEGER, DECIMAL, DATE, DATETIME.
	 * 
	 * Les types sont nativement stockés dans ces formats. Par contre, ce
	 * sont des représentations chaines qui sont véhiculées jusqu'au
	 * serveur.
	 */

	// booleen "true" ou "false"

	document.addCriterion(base.getBaseCategory(catNames[5]), -1.54);
	document.addCriterion(base.getBaseCategory(catNames[5]), pi); //

	// ce sera 0.0 qui sera stocké.
	document.addCriterion(base.getBaseCategory(catNames[5]), "0.0");

	Calendar cal = Calendar.getInstance();
	cal.set(2011, 11, 01);
	// date.
	document.addCriterion(base.getBaseCategory(catNames[6]), cal.getTime());

	// date et heure
	// tag.addCriterion(uBase.getBaseDefinition().getIndex().getCategory(catNames[7),
	// true, new Date());

	// Date de création du document (à priori avant son entrée dans la GED,
	// on retranche une heure)
	cal = Calendar.getInstance();
	cal.setTimeInMillis(System.currentTimeMillis());
	cal.add(Calendar.HOUR, -1);
	document.setCreationDate(cal.getTime());

	Document stored = serviceProvider.getStoreService().storeDocument(
		document, FilenameUtils.getBaseName(newDoc.getName()),
		FilenameUtils.getExtension(newDoc.getName()),
		new FileInputStream(newDoc));

	// On vérifie que le document a passé le controle.
	assertNotNull(stored);

	UUID archiveUUID = stored.getUuid();
	assertNotNull(archiveUUID);
	/*
	 * Recherche sur UUID
	 */

	Document documentByUUID = serviceProvider.getSearchService()
		.getDocumentByUUID(base, archiveUUID);
	System.out.println(documentByUUID.getUuid());

	/*
	 * On recherche maintenant dans Lucene L'approche est différente.
	 * 
	 * Tout d'abord on n'exprime plus les requêtes sur les catégories en
	 * précisant le numéro de la catégorie, mais son nom (ce qui facilitera
	 * pour le futur les requêtes multibase)
	 * 
	 * On peut retrouver une catégorie par son nom, par son id...
	 */
	String c0Name = base.getBaseCategory(catNames[0]).getName();
	// On peut comparer...
	String c0NameBis = base.getBaseCategory(catNames[0]).getName();
	assertEquals(c0NameBis, c0Name);

	/*
	 * Ensuite on construit la requête
	 */
	String query = c0Name + ":" + c0;

	/*
	 * Ici on a donc une requête du genre "Cat 0:Identifier". Cette requete
	 * ne peut pas fonctionner (lucene n'aime pas les espaces dans les noms
	 * de champs => Erreurs de syntaxe au moment de parser la requête) En
	 * fait on stocke les noms de champs en casse basse et en remplaçant les
	 * espaces par des char(255). Celà est fait par la méthode suivante,
	 * pour devenir "cat 0:Identifier" (vous pouvez vérifier ce n'est pas un
	 * espace entre cat et 0 dans le commentaire précédent)
	 */
	query = base.getBaseCategory(catNames[0]).getFormattedName() + ":" + c0;

	// Nouveauté on peut l'écrire directement
	query = base.getBaseCategory(catNames[0]).getFormattedName() + ":" + c0;

	/*
	 * 
	 * Ce qui est fondamental : on doit préciser une limite dans le nombre
	 * de résultats à remonter. Le serveur interdira les valeurs au delà de
	 * LuceneUtils.SEARCH_LIMIT (10 000) ;
	 */
	SearchResult searchResult = serviceProvider.getSearchService().search(
		query, 5, base);
	List<Document> docs = searchResult.getDocuments();
	assertTrue(docs != null && docs.size() == 1);
	System.out.println(docs.get(0).getUuid());
	System.out.println(docs.get(0).getDigest());
	// on fait les contrôles
	control(docs.get(0), newDoc, c0);

	// On essaye de stocker un autre document avec C0=Identifier, ce qui est
	// normalement impossible.
	document = toolkitFactory.createDocumentTag(base);

	document.addCriterion(base.getBaseCategory(catNames[0]), c0);
	// Celà ne doit pas fonctionner (false en dernier paramètre)
	File file = new File("/unknownFile.pdf");
	storeDocument(document, file, false);

	// On le vérifie aussi dans lucene
	assertEquals(
		1,
		searchLucene(base.getBaseCategory(catNames[0])
			.getFormattedName() + ":" + c0, 5));

    }
}
