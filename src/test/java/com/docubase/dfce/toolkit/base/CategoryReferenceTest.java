package com.docubase.dfce.toolkit.base;

import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CategoryReferenceTest extends AbstractBaseTestCase {
   private static final String CATEGORY_CODE1 = "CATEGORY_CODE1" + UUID.randomUUID();
   private static final String CATEGORY_CODE2 = "CATEGORY_CODE2" + UUID.randomUUID();
   private static final String CATEGORY_CODE3 = "CATEGORY_CODE3" + UUID.randomUUID();
   private static final String CATEGORY_CODE4 = "CATEGORY_CODE4" + UUID.randomUUID();
   private static final String URL = "http://cer69-ds4int:8080/dfce-webapp/toolkit/";

   @BeforeClass
   public static void beforeAll() {
      Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
   }

   @AfterClass
   public static void afterAll() {
      Authentication.closeSession();
   }

   /**
    * Ajout d'une nouvelle cat�gorie au syst�me
    * 
    * @throws ObjectAlreadyExistsException
    * 
    */
   @Test
   public void testInsertCategory() throws ObjectAlreadyExistsException {
      boolean alreadyExists = ServiceProvider.getStorageAdministrationService().createCategory(
            CATEGORY_CODE1, CategoryDataType.DATE);
      Assert.assertFalse(alreadyExists);
   }

   /**
    * Ajout d'une cat�gorie en double
    * 
    * @throws ObjectAlreadyExistsException
    * 
    */
   @Test
   public void testInsertCategoryAlreadyExists() throws ObjectAlreadyExistsException {
      boolean alreadyExists = ServiceProvider.getStorageAdministrationService().createCategory(
            CATEGORY_CODE2, CategoryDataType.DATE);
      Assert.assertFalse(alreadyExists);
      alreadyExists = ServiceProvider.getStorageAdministrationService().createCategory(
            CATEGORY_CODE2, CategoryDataType.DATE);
      Assert.assertTrue(alreadyExists);
   }

   /**
    * Test sur l'ensemble des cat�gories du syst�me
    * 
    * @throws ObjectAlreadyExistsException
    * 
    */
   @Test
   public void testGetAllCategories() throws ObjectAlreadyExistsException {
      ServiceProvider.getStorageAdministrationService().createCategory(CATEGORY_CODE3,
            CategoryDataType.INTEGER);
      Set<Category> allCategories = ServiceProvider.getStorageAdministrationService()
            .getAllCategories();

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
    * Test de recherche d'une cat�gorie par son code
    * 
    * @throws ObjectAlreadyExistsException
    * 
    */
   @Test
   public void testGetCategoryByCode() throws ObjectAlreadyExistsException {
      ServiceProvider.getStorageAdministrationService().createCategory(CATEGORY_CODE4,
            CategoryDataType.INTEGER);
      Category category = ServiceProvider.getStorageAdministrationService().getCategory(
            CATEGORY_CODE4);
      Assert.assertNotNull(category);
      Assert.assertEquals(category.getName(), category.getName());
      Assert.assertEquals(category.getType(), category.getType());
   }
}
