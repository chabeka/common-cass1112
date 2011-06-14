package net.docubase.toolkit.base;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DictionaryTest extends AbstractTestCaseCreateAndPrepareBase {
   private static BaseCategory baseCategory;
   private File newDoc;
   private Document document;

   @BeforeClass
   public static void setupAll() {
      Category category = ServiceProvider.getStorageAdministrationService().findOrCreateCategory(
            "CategoryWithDictionary", CategoryDataType.INTEGER);

      baseCategory = ToolkitFactory.getInstance().createBaseCategory(category, true);
      baseCategory.setMaximumValues((short) 10);
      baseCategory.setEnableDictionary(true);

      ServiceProvider.getBaseAdministrationService().stopBase(base);
      base.addBaseCategory(baseCategory);
      ServiceProvider.getBaseAdministrationService().updateBase(base);
      ServiceProvider.getBaseAdministrationService().startBase(base);
   }

   @Before
   public void setupEach() {
      ServiceProvider.getStorageAdministrationService().addDictionaryTerm(baseCategory, "10");

      newDoc = getFile("doc1.pdf", DictionaryTest.class);
      Assert.assertTrue(newDoc.exists());

      document = ToolkitFactory.getInstance().createDocumentTag(base);
      String identifier = "Identifier" + UUID.randomUUID() + base.getBaseId();
      document.addCriterion(baseCategory0, identifier);

      // Date de création du document (à priori avant son entrée dans la
      // GED, on retranche une heure)
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(System.currentTimeMillis());
      cal.add(Calendar.HOUR, -1);
      document.setCreationDate(cal.getTime());
   }

   @Test
   public void testStoreDocWrongEntry() {
      document.addCriterion(baseCategory, 11);

      boolean stored = ServiceProvider.getStoreService().storeDocument(document, newDoc);

      // 11 n'est pas dans le dictionnaire, le document n'est donc pas stocké
      Assert.assertFalse(stored);
   }

   @Test
   public void testStoreDocCorrectEntry() {
      document.addCriterion(baseCategory, 10);
      boolean stored = ServiceProvider.getStoreService().storeDocument(document, newDoc);

      // 10 est dans le dictionaire, le document est donc stocké
      Assert.assertTrue(stored);
   }

   @Test
   public void testRemoveEntry() {
      ServiceProvider.getStorageAdministrationService().removeDictionaryTerm(baseCategory, "10");

      document.addCriterion(baseCategory, 10);
      boolean stored = ServiceProvider.getStoreService().storeDocument(document, newDoc);

      // 10 n'est plus dans le dictionaire, le document n'est donc pas stocké
      Assert.assertFalse(stored);
   }

   @Test
   public void testGetAllEntries() {
      List<String> allEntries = ServiceProvider.getStorageAdministrationService()
            .getAllDictonaryTerms(baseCategory);
      Assert.assertEquals(1, allEntries.size());
      Assert.assertTrue(allEntries.contains("10"));
   }
}
