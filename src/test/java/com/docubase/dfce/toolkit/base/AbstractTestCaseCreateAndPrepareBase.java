package com.docubase.dfce.toolkit.base;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.user.User;
import net.docubase.toolkit.model.user.UserGroup;
import net.docubase.toolkit.model.user.UserPermission;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.TestUtils;

public abstract class AbstractTestCaseCreateAndPrepareBase extends
	AbstractTestBase {
    protected static Logger logger = Logger
	    .getLogger(AbstractTestCaseCreateAndPrepareBase.class);

    public static final String SIMPLE_USER_NAME = "SIMPLE_USER_NAME";
    public static final String SIMPLE_USER_PASSWORD = "SIMPLE_USER_PASSWORD";
    public static final String SIMPLE_USER_GROUP = "SIMPLE_USER_GROUP";

    public static final String BASEID = "RICHGED";

    protected static final String[] catNames = { "Catégorie zéro",
	    "Catégorie un", "Catégorie deux", "Cat booléenne", "Cat entière",
	    "Cat décimale", "Cat date", "Cat date et heure", "CatInteger8" };

    protected static BaseCategory category0;
    protected static BaseCategory category1;
    protected static BaseCategory category2;
    protected static BaseCategory categoryBoolean;
    protected static BaseCategory categoryInteger;
    protected static BaseCategory categoryDecimal;
    protected static BaseCategory categoryDate;
    protected static BaseCategory categoryDateTime;
    protected static BaseCategory categoryInteger2;
    protected static Base base;

    @BeforeClass
    public static void before() {
	connect();
	base = deleteAndCreateBaseThenStarts();

	createSimpleUser();
    }

    @AfterClass
    public static void after() {
	serviceProvider.getBaseAdministrationService().deleteBase(base);
	disconnect();
    }

    private static void createSimpleUser() {
	UserGroup userGroup = serviceProvider.getUserAdministrationService()
		.loadUserGroup("USER");

	if (userGroup == null) {
	    Set<UserPermission> permissions = new HashSet<UserPermission>();
	    permissions.add(UserPermission.GET_BASE);
	    permissions.add(UserPermission.DOCUMENT_STORE);
	    permissions.add(UserPermission.DOCUMENT_EXTRACT);
	    permissions.add(UserPermission.BASE_SEARCH);

	    try {
		userGroup = serviceProvider.getUserAdministrationService()
			.createUserGroup("USER", permissions);
	    } catch (ObjectAlreadyExistsException e) {
		throw new RuntimeException(e);
	    }
	}

	User simpleUser = serviceProvider.getUserAdministrationService()
		.loadUser(SIMPLE_USER_NAME);
	if (simpleUser == null) {
	    try {
		simpleUser = serviceProvider.getUserAdministrationService()
			.createUser(SIMPLE_USER_NAME, SIMPLE_USER_PASSWORD,
				"USER");
	    } catch (ObjectAlreadyExistsException e) {
		throw new RuntimeException(e);
	    }
	}
    }

    protected int searchLucene(String query, int searchLimit)
	    throws ExceededSearchLimitException, SearchQueryParseException {
	return searchLucene(query, searchLimit, null);

    }

    protected int searchLucene(String query, int searchLimit,
	    ChainedFilter chainedFilter) throws ExceededSearchLimitException,
	    SearchQueryParseException {

	SearchResult search = serviceProvider.getSearchService().search(query,
		searchLimit, base, chainedFilter);
	if (search == null) {
	    return 0;
	}
	List<Document> docs = search.getDocuments();
	return docs == null ? 0 : docs.size();
    }

    protected static Base deleteAndCreateBaseThenStarts() {
	Base base = serviceProvider.getBaseAdministrationService().getBase(
		BASEID);
	if (base != null) {
	    serviceProvider.getBaseAdministrationService().deleteBase(base);
	}
	base = createBase();

	serviceProvider.getBaseAdministrationService().startBase(base);

	base = serviceProvider.getBaseAdministrationService().getBase(BASEID);
	return base;
    }

    private static Base createBase() {
	// ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
	// StorageAdministrationService storageAdministrationService =
	// serviceProvider
	// .getStorageAdministrationService();

	// Base base = toolkitFactory.createBase(BASEID);

	category0 = createBaseCategory(catNames[0], CategoryDataType.STRING,
		true);
	category0.setMinimumValues((short) 1);
	category0.setMaximumValues((short) 1);
	category0.setSingle(true);

	category1 = createBaseCategory(catNames[1], CategoryDataType.STRING,
		true);
	category2 = createBaseCategory(catNames[2], CategoryDataType.STRING,
		true);
	categoryBoolean = createBaseCategory(catNames[3],
		CategoryDataType.BOOLEAN, true);
	categoryInteger = createBaseCategory(catNames[4],
		CategoryDataType.INTEGER, true);
	categoryDecimal = createBaseCategory(catNames[5],
		CategoryDataType.DOUBLE, true);
	categoryDate = createBaseCategory(catNames[6], CategoryDataType.DATE,
		true);
	categoryDateTime = createBaseCategory(catNames[7],
		CategoryDataType.DATETIME, true);
	categoryInteger2 = createBaseCategory(catNames[8],
		CategoryDataType.INTEGER, true);

	Base base = createBase(BASEID, category0, category1, category2,
		categoryBoolean, categoryDate, categoryDateTime,
		categoryDecimal, categoryInteger, categoryInteger2);

	base.setDescription("My-Ged-Is-Rich");

	// Category category0 =
	// storageAdministrationService.findOrCreateCategory(
	// catNames[0], CategoryDataType.STRING);
	// Category category1 =
	// storageAdministrationService.findOrCreateCategory(
	// catNames[1], CategoryDataType.STRING);
	// Category category2 =
	// storageAdministrationService.findOrCreateCategory(
	// catNames[2], CategoryDataType.STRING);
	// Category categoryBoolean = storageAdministrationService
	// .findOrCreateCategory(catNames[3], CategoryDataType.BOOLEAN);
	// Category categoryInteger = storageAdministrationService
	// .findOrCreateCategory(catNames[4], CategoryDataType.INTEGER);
	// Category categoryDecimal = storageAdministrationService
	// .findOrCreateCategory(catNames[5], CategoryDataType.DOUBLE);
	// Category categoryDate = storageAdministrationService
	// .findOrCreateCategory(catNames[6], CategoryDataType.DATE);
	// Category catInteger8 = storageAdministrationService
	// .findOrCreateCategory(catNames[8], CategoryDataType.INTEGER);
	//
	// category0 = toolkitFactory.createBaseCategory(category0, true);
	// category0.setMinimumValues((short) 1);
	// category0.setMaximumValues((short) 1);
	// category0.setSingle(true);
	// base.addBaseCategory(category0);
	//
	// BaseCategory baseCategory1 = toolkitFactory.createBaseCategory(
	// category1, true);
	// baseCategory1.setEnableDictionary(false);
	// base.addBaseCategory(baseCategory1);
	//
	// BaseCategory baseCategory2 = toolkitFactory.createBaseCategory(
	// category2, true);
	// baseCategory2.setMaximumValues((short) 10);
	// baseCategory2.setEnableDictionary(false);
	// base.addBaseCategory(baseCategory2);
	//
	// BaseCategory baseCategoryBoolean = toolkitFactory.createBaseCategory(
	// categoryBoolean, true);
	// baseCategoryBoolean.setMaximumValues((short) 10);
	// baseCategoryBoolean.setEnableDictionary(false);
	// base.addBaseCategory(baseCategoryBoolean);
	//
	// BaseCategory baseCategoryInteger = toolkitFactory.createBaseCategory(
	// categoryInteger, true);
	// baseCategoryInteger.setMaximumValues((short) 10);
	// baseCategoryInteger.setEnableDictionary(false);
	// base.addBaseCategory(baseCategoryInteger);
	//
	// BaseCategory baseCategoryDecimal = toolkitFactory.createBaseCategory(
	// categoryDecimal, true);
	// baseCategoryDecimal.setMaximumValues((short) 10);
	// baseCategoryDecimal.setEnableDictionary(false);
	// base.addBaseCategory(baseCategoryDecimal);
	//
	// BaseCategory baseCategoryDate = toolkitFactory.createBaseCategory(
	// categoryDate, true);
	// baseCategoryDate.setMaximumValues((short) 10);
	// baseCategoryDate.setEnableDictionary(false);
	// base.addBaseCategory(baseCategoryDate);
	//
	// BaseCategory baseCategory8 = toolkitFactory.createBaseCategory(
	// catInteger8, true);
	// baseCategory8.setMaximumValues((short) 10);
	// baseCategory8.setEnableDictionary(false);
	// base.addBaseCategory(baseCategory8);

	// try {
	// serviceProvider.getBaseAdministrationService().createBase(base);
	// } catch (ObjectAlreadyExistsException e) {
	// e.printStackTrace();
	// fail("base : " + base.getBaseId() + " already exists");
	// }

	/*
	 * On va, alors qu'il n'y a aucun document, modifier la base pour y
	 * ajouter la dernière catégorie
	 */
	// Category categoryDateHeure = storageAdministrationService
	// .findOrCreateCategory(catNames[7], CategoryDataType.DATETIME);
	//
	// BaseCategory baseCategoryDateHeure =
	// toolkitFactory.createBaseCategory(
	// categoryDateHeure, true);
	// baseCategoryDateHeure.setMaximumValues((short) 10);
	// baseCategoryDateHeure.setEnableDictionary(false);
	// base.addBaseCategory(baseCategoryDateHeure);
	//
	// serviceProvider.getBaseAdministrationService().updateBase(base);

	return base;
    }

    protected static Document storeDocument(Document document, File file)
	    throws TagControlException {
	InputStream in = null;

	try {
	    in = new FileInputStream(file);
	    return serviceProvider.getStoreService().storeDocument(document,
		    FilenameUtils.getBaseName(file.getName()),
		    FilenameUtils.getExtension(file.getName()), in);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }

    /**
     * Cette méthode insert le docuemnt de référence utiliser pour les tests.
     * 
     * @return the uUID
     * @throws CustomTagControlException
     * @throws IOException
     * @throws FileNotFoundException
     */
    protected static FileReference createFileReference() {

	File file = TestUtils.getFile("48pages.pdf");

	assertNotNull(file);
	InputStream inputStream = null;
	try {
	    inputStream = new FileInputStream(file);
	} catch (FileNotFoundException e) {
	    fail(e.getMessage());
	}

	StorageAdministrationService storageAdministrationService = serviceProvider
		.getStorageAdministrationService();

	FileReference fileReference = storageAdministrationService
		.createFileReference(FilenameUtils.getBaseName(file.getName()),
			FilenameUtils.getExtension(file.getName()), inputStream);

	assertNotNull(fileReference);

	logger.info("FileReference created stored.");

	return fileReference;
    }

    protected Document createTestDocument() {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(category0, "New Value 12" + UUID.randomUUID());
	return document;
    }

}
