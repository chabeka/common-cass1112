package net.docubase.toolkit.base;

import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.Authentication;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CategoryReferenceTest extends AbstractBaseTestCase {
    private static final String CATEGORY_CODE1 = "CATEGORY_CODE1"
	    + UUID.randomUUID();
    private static final String CATEGORY_CODE2 = "CATEGORY_CODE2"
	    + UUID.randomUUID();
    private static final String CATEGORY_CODE3 = "CATEGORY_CODE3"
	    + UUID.randomUUID();
    private static final String CATEGORY_CODE4 = "CATEGORY_CODE4"
	    + UUID.randomUUID();
    private static final String ADM_LOGIN = "_ADMIN";
    private static final String ADM_PASSWORD = "DOCUBASE";
    private static final String HOSTNAME = "cer69-ds4int";
    private static final Integer PORT = Integer.valueOf(4020);
    private static final Integer DOMAIN_ID = Integer.valueOf(1);

    @BeforeClass
    public static void beforeAll() throws Exception {
	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, HOSTNAME, PORT,
		DOMAIN_ID);
    }

    @AfterClass
    public static void afterAll() throws Exception {
	Authentication.closeSession();
    }

    /**
     * Ajout d'une nouvelle catégorie au système
     * 
     */
    @Test
    public void testInsertCategory() {
	boolean alreadyExists = ServiceProvider
		.getStorageAdministrationService().createCategory(
			CATEGORY_CODE1, CategoryDataType.DATE);
	Assert.assertFalse(alreadyExists);
    }

    /**
     * Ajout d'une catégorie en double
     * 
     */
    @Test
    public void testInsertCategoryAlreadyExists() {
	boolean alreadyExists = ServiceProvider
		.getStorageAdministrationService().createCategory(
			CATEGORY_CODE2, CategoryDataType.DATE);
	Assert.assertFalse(alreadyExists);
	alreadyExists = ServiceProvider.getStorageAdministrationService()
		.createCategory(CATEGORY_CODE2, CategoryDataType.DATE);
	Assert.assertTrue(alreadyExists);
    }

    /**
     * Test sur l'ensemble des catégories du système
     * 
     */
    @Test
    public void testGetAllCategories() {
	ServiceProvider.getStorageAdministrationService().createCategory(
		CATEGORY_CODE3, CategoryDataType.INTEGER);
	Set<Category> allCategories = ServiceProvider
		.getStorageAdministrationService().getAllCategories();

	Category categoryFound = null;
	for (Category category : allCategories) {
	    if (CATEGORY_CODE3.equals(category.getName())) {
		categoryFound = category;
	    }
	}

	Assert.assertNotNull(categoryFound);
	Assert.assertEquals(CategoryDataType.INTEGER, categoryFound.getType());
    }

    /**
     * Test de recherche d'une catégorie par son code
     * 
     */
    @Test
    public void testGetCategoryByCode() {
	ServiceProvider.getStorageAdministrationService().createCategory(
		CATEGORY_CODE4, CategoryDataType.INTEGER);
	Category category = ServiceProvider.getStorageAdministrationService()
		.getCategory(CATEGORY_CODE4);
	Assert.assertNotNull(category);
	Assert.assertEquals(category.getName(), category.getName());
	Assert.assertEquals(category.getType(), category.getType());
    }
}
