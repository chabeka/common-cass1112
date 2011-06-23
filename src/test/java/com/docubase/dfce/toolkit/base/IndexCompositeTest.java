package com.docubase.dfce.toolkit.base;

import static junit.framework.Assert.*;

import java.util.List;

import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;

public class IndexCompositeTest extends AbstractTestCaseCreateAndPrepareBase {

   @Test(expected = IllegalArgumentException.class)
   public void testAddComposite_ListNull() {
      ServiceProvider.getStorageAdministrationService().findOrCreateCompositeIndex((Category) null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddComposite_NotEnoughCategories() {
      Category category = ServiceProvider.getStorageAdministrationService()
            .getCategory(catNames[0]);
      ServiceProvider.getStorageAdministrationService().findOrCreateCompositeIndex(category);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddComposite_WithOneCategoryNotString() {
      Category category0 = ServiceProvider.getStorageAdministrationService().getCategory(
            catNames[0]);
      Category booleanCategory = ServiceProvider.getStorageAdministrationService().getCategory(
            catNames[3]);
      ServiceProvider.getStorageAdministrationService().findOrCreateCompositeIndex(category0,
            booleanCategory);
   }

   @Test
   public void testAddComposite() {
      Category category0 = ServiceProvider.getStorageAdministrationService().getCategory(
            catNames[0]);
      Category category2 = ServiceProvider.getStorageAdministrationService().getCategory(
            catNames[2]);
      CompositeIndex compositeIndex = ServiceProvider.getStorageAdministrationService()
            .findOrCreateCompositeIndex(category0, category2);
      assertNotNull(compositeIndex);
      List<Category> categories = compositeIndex.getCategories();
      assertEquals(2, categories.size());
      assertEquals(categories.get(0).getName(), catNames[0]);
      assertEquals(categories.get(1).getName(), catNames[2]);
   }

}
