package com.docubase.dfce.toolkit.base;

import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;

import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.AbstractTestBase;

public class CategoryReferenceTest extends AbstractTestBase {
    private static String CATEGORY_CODE1;
    private static String CATEGORY_CODE2;
    private static String CATEGORY_CODE3;
    private static String CATEGORY_CODE4;

    @BeforeClass
    public static void setUp() {
	connect();
	CATEGORY_CODE1 = StringUtils.deleteWhitespace(StringUtils.replace(
		"CATEGORY_CODE1" + UUID.randomUUID(), "-", StringUtils.EMPTY));
	CATEGORY_CODE2 = StringUtils.deleteWhitespace(StringUtils.replace(
		"CATEGORY_CODE2" + UUID.randomUUID(), "-", StringUtils.EMPTY));
	CATEGORY_CODE3 = StringUtils.deleteWhitespace(StringUtils.replace(
		"CATEGORY_CODE3" + UUID.randomUUID(), "-", StringUtils.EMPTY));
	CATEGORY_CODE4 = StringUtils.deleteWhitespace(StringUtils.replace(
		"CATEGORY_CODE4" + UUID.randomUUID(), "-", StringUtils.EMPTY));
    }

    @AfterClass
    public static void tearDown() {
	disconnect();
    }

    /**
     * Ajout d'une nouvelle catégorie au système
     * 
     * @throws ObjectAlreadyExistsException
     * 
     */
    @Test
    public void testInsertCategory() throws ObjectAlreadyExistsException {
	boolean alreadyExists = serviceProvider
		.getStorageAdministrationService().createCategory(
			CATEGORY_CODE1, CategoryDataType.DATE);
	Assert.assertFalse(alreadyExists);
    }

    /**
     * Ajout d'une catégorie en double
     * 
     * @throws ObjectAlreadyExistsException
     * 
     */
    @Test
    public void testInsertCategoryAlreadyExists()
	    throws ObjectAlreadyExistsException {
	boolean alreadyExists = serviceProvider
		.getStorageAdministrationService().createCategory(
			CATEGORY_CODE2, CategoryDataType.DATE);
	Assert.assertFalse(alreadyExists);
	alreadyExists = serviceProvider.getStorageAdministrationService()
		.createCategory(CATEGORY_CODE2, CategoryDataType.DATE);
	Assert.assertTrue(alreadyExists);
    }

    /**
     * Test sur l'ensemble des catégories du système
     * 
     * @throws ObjectAlreadyExistsException
     * 
     */
    @Test
    public void testGetAllCategories() throws ObjectAlreadyExistsException {
	serviceProvider.getStorageAdministrationService().createCategory(
		CATEGORY_CODE3, CategoryDataType.INTEGER);
	Set<Category> allCategories = serviceProvider
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
     * @throws ObjectAlreadyExistsException
     * 
     */
    @Test
    public void testGetCategoryByCode() throws ObjectAlreadyExistsException {
	serviceProvider.getStorageAdministrationService().createCategory(
		CATEGORY_CODE4, CategoryDataType.INTEGER);
	Category category = serviceProvider.getStorageAdministrationService()
		.getCategory(CATEGORY_CODE4);
	Assert.assertNotNull(category);
	Assert.assertEquals(category.getName(), category.getName());
	Assert.assertEquals(category.getType(), category.getType());
    }
}
