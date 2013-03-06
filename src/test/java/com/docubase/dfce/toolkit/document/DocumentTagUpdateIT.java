package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentTagUpdateIT extends AbstractTestCaseCreateAndPrepareBase {

   /** The file. */
   private static File file;

   @BeforeClass
   public static void beforeClass() {
      file = TestUtils.getFile("48pages.pdf");
   }

   private final ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

   private BaseCategory baseCategory0;

   private BaseCategory baseCategoryInteger;

   private Document storedDocument;

   private BaseCategory baseCategoryBoolean;

   @Before
   public void beforeEach() throws FileNotFoundException, TagControlException {
      baseCategory0 = base.getBaseCategory(catNames[0]);
      baseCategoryInteger = base.getBaseCategory(catNames[4]);
      baseCategoryBoolean = base.getBaseCategory(catNames[3]);

      Document document = toolkitFactory.createDocumentTag(base);
      String c0Value = "UNIQUE_ID" + UUID.randomUUID();
      document.addCriterion(baseCategory0, c0Value);
      document.addCriterion(baseCategoryInteger, 12);
      document.addCriterion(baseCategoryInteger, 212);

      storedDocument = serviceProvider.getStoreService().storeDocument(document,
            FilenameUtils.getBaseName(file.getName()), FilenameUtils.getExtension(file.getName()),
            new FileInputStream(file));

      List<Criterion> category0Criterions = storedDocument.getCriterions(baseCategory0);
      List<Criterion> categoryIntegercriterions = storedDocument.getCriterions(baseCategoryInteger);
      assertEquals(1, category0Criterions.size());
      assertEquals(c0Value, category0Criterions.get(0).getWord());
      assertEquals(2, categoryIntegercriterions.size());
      assertEquals(12, categoryIntegercriterions.get(0).getWord());
      assertEquals(212, categoryIntegercriterions.get(1).getWord());

   }

   @Test
   public void testUpdateCriterionValue() throws TagControlException, FrozenDocumentException {
      Document documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(base,
            storedDocument.getUuid());

      List<Criterion> categoryIntegercriterions = storedDocument.getCriterions(baseCategoryInteger);
      categoryIntegercriterions.get(0).setWord(512);
      serviceProvider.getStoreService().updateDocument(storedDocument);

      documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(base,
            storedDocument.getUuid());

      categoryIntegercriterions = documentByUUID.getCriterions(baseCategoryInteger);
      assertEquals(2, categoryIntegercriterions.size());
      assertEquals(512, categoryIntegercriterions.get(0).getWord());
      assertEquals(212, categoryIntegercriterions.get(1).getWord());
   }

   @Test
   public void testDeleteCriterion() throws TagControlException, FrozenDocumentException {
      Document documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(base,
            storedDocument.getUuid());

      documentByUUID.deleteCriterion(baseCategoryInteger);
      serviceProvider.getStoreService().updateDocument(documentByUUID);

      documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(base,
            storedDocument.getUuid());
      List<Criterion> categoryIntegercriterions = documentByUUID.getCriterions(baseCategoryInteger);

      assertEquals(2, documentByUUID.getAllCriterions().size());
      assertEquals(1, categoryIntegercriterions.size());
      assertEquals(212, categoryIntegercriterions.get(0).getWord());
   }

   @Test
   public void testAddCriterion() throws TagControlException, FrozenDocumentException {
      Document documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(base,
            storedDocument.getUuid());

      documentByUUID.addCriterion(baseCategoryBoolean, Boolean.TRUE);
      serviceProvider.getStoreService().updateDocument(documentByUUID);

      documentByUUID = serviceProvider.getSearchService().getDocumentByUUID(base,
            storedDocument.getUuid());
      List<Criterion> categoryBooleanCriterions = documentByUUID.getCriterions(baseCategoryBoolean);

      assertEquals(4, documentByUUID.getAllCriterions().size());
      assertEquals(1, categoryBooleanCriterions.size());
      assertEquals(Boolean.TRUE, categoryBooleanCriterions.get(0).getWord());
   }
}
