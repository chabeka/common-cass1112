package com.docubase.dfce.toolkit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This class provide a default template implementation for integration testing
 * at the toolkit level.
 * <p>
 * This class load configuration parameters from "test.properties".
 * 
 */
@RunWith(JUnit4.class)
public abstract class AbstractTestBase {

    /** The logger. */
    private static Logger logger = Logger.getLogger(AbstractTestBase.class);

    /** The Constant LOGIN. */
    public static final String ADM_LOGIN = "_ADMIN";

    /** The Constant PASSWORD. */
    public static final String ADM_PASSWORD = "DOCUBASE";

    /** The service URL 1. */
    public static String SERVICE_URL;

    /** The service URL 2. */
    public static String SERVICE_URL_2;

    /** The service provider. */
    public static ServiceProvider serviceProvider = ServiceProvider
	    .newServiceProvider();

    /** The Constant toolkitFactory. */
    public static final ToolkitFactory toolkitFactory = ToolkitFactory
	    .getInstance();

    /**
     * Load configuration.
     */
    static {
	// Load test configuration.
	Configuration config;
	try {
	    config = new PropertiesConfiguration("test.properties");
	    SERVICE_URL = config.getString("test.server.1.url");
	    SERVICE_URL_2 = config.getString("test.server.2.url");
	} catch (ConfigurationException e) {
	    logger.error("Fail to load configuration from test.properties", e);
	    fail();
	}
	logger.info("Configuration loaded from test.properties");
    }

    /**
     * Connect.
     */
    protected static void connect() {
	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
	logger.info("ServiceProvider connected.");
    }

    /**
     * Disconnect.
     */
    protected static void disconnect() {
	serviceProvider.disconnect();
	logger.info("ServiceProvider disconnected.");
    }

    /**
     * Creates the base.
     * 
     * @param baseId
     *            the base id
     * @param baseCategories
     *            the base categories
     * @return the base
     */
    protected static Base createBase(String baseId,
	    BaseCategory... baseCategories) {
	Base base = toolkitFactory.createBase(baseId);
	for (BaseCategory baseCategory : baseCategories) {
	    base.addBaseCategory(baseCategory);
	}
	try {
	    base = serviceProvider.getBaseAdministrationService().createBase(
		    base);
	} catch (ObjectAlreadyExistsException e) {
	    logger.error("Failed to create base with baseId:" + baseId, e);
	    fail();
	}
	logger.info("Base " + baseId + " created with categories:"
		+ baseCategories);
	return base;
    }

    protected static BaseCategory createBaseCategory(String name,
	    CategoryDataType type, boolean indexed) {
	Category category = serviceProvider.getStorageAdministrationService()
		.findOrCreateCategory(name, type);
	BaseCategory baseCategory = toolkitFactory.createBaseCategory(category,
		indexed);
	return baseCategory;
    }

    /**
     * Creates the base category.
     * 
     * @param name
     *            the name
     * @param type
     *            the type
     * @param indexed
     *            the indexed
     * @param dictionnary
     *            the dictionnary
     * @param min
     *            the min
     * @param max
     *            the max
     * @param single
     *            the single
     * @return the base category
     */
    protected static BaseCategory createBaseCategory(String name,
	    CategoryDataType type, boolean indexed, boolean dictionnary,
	    int min, int max, boolean single) {
	Category category = serviceProvider.getStorageAdministrationService()
		.findOrCreateCategory(name, type);
	BaseCategory baseCategory = toolkitFactory.createBaseCategory(category,
		indexed);
	baseCategory.setEnableDictionary(dictionnary);
	baseCategory.setMinimumValues((short) min);
	baseCategory.setMaximumValues((short) max);
	baseCategory.setSingle(single);
	return baseCategory;
    }

    // Refaire les methodes ci-dessous

    /**
     * Store the given document associated with the given file.
     * 
     * @param document
     *            the document.
     * @param file
     *            the file.
     * @param expectStore
     *            set to true, to check that the returned document is not null.
     * @return the stored document.
     */
    protected static Document storeDocument(Document document, File file,
	    boolean expectStore) {
	Document stored = null;
	FileInputStream in = null;
	try {
	    in = new FileInputStream(file);
	    stored = serviceProvider.getStoreService().storeDocument(document,
		    FilenameUtils.getBaseName(file.getName()),
		    FilenameUtils.getExtension(file.getName()), in);
	    Assert.assertEquals(expectStore, stored != null);

	} catch (IOException e) {
	    Assert.assertFalse(expectStore);
	} catch (TagControlException e) {
	    Assert.assertFalse(expectStore);
	} finally {
	    IOUtils.closeQuietly(in);
	}
	return stored;
    }

    /**
     * Génére une date de création. Date du jour moins 2 heures.
     * 
     * @return the date
     */
    protected static Date generateCreationDate() {
	return new DateTime(new Date()).minusHours(2).toDate();
    }

    protected static void deleteBase(Base base) {
	serviceProvider.getBaseAdministrationService().stopBase(base);
	serviceProvider.getBaseAdministrationService().deleteBase(base);
    }

}
