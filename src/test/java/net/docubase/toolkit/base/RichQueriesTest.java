package net.docubase.toolkit.base;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.BeforeClass;
import org.junit.Test;

public class RichQueriesTest extends AbstractTestCaseCreateAndPrepareBase {
   private static BaseCategory c0;
   private static BaseCategory c1;
   private static BaseCategory c2;

   @BeforeClass
   public static void setupAll() throws IOException {
      System.out.println("****** testCompleteQueryScenarii ******");
      Document tag;

      c0 = base.getBaseCategory(catNames[0]);
      c1 = base.getBaseCategory(catNames[1]);
      c2 = base.getBaseCategory(catNames[2]);

      for (int i = 0; i < 50; i++) {
         tag = ToolkitFactory.getInstance().createDocumentTag(base);

         // C0 unique
         tag.addCriterion(c0, "testfilter" + i);

         // C1 soit Enfant, soit Adulte
         String c1Val = null;
         if (i < 10) { // Les enfants d'abord
            c1Val = "enfant";
         } else {
            c1Val = "adulte";
         }
         tag.addCriterion(c1, c1Val);

         // C2. 2 valeurs, une qui varie très peu, une qui est unique.
         tag.addCriterion(c2, "personne" + i);
         tag.addCriterion(c2, i % 2 == 0 ? "masculin" : "feminin");

         // stockage
         storeDoc(tag, getFile("doc1.pdf", RichQueriesTest.class), null, true);
      }

      ServiceProvider.getStorageAdministrationService().updateAllIndexesUsageCount();
   }

   @Test
   public void testSimpleQuery() {
      String query = c1.getFormattedName() + ":adulte";
      assertEquals(40, searchLucene(query, 1000, null));
   }

   @Test
   public void testCompositeQuery() {
      String query = "(" + c1.getFormattedName() + ":adulte" + " AND " + c2.getFormattedName()
            + ":masculin" + ") OR (" + c1.getFormattedName() + ":enfant" + " AND "
            + c2.getFormattedName() + ":feminin)";
      assertEquals(25, searchLucene(query, 1000, null));
   }

   @Test
   public void testRange() {
      String query = c1.getFormattedName() + ":adulte" + " AND " + c2.getFormattedName()
            + ":[feminin TO masculin]";
      assertEquals(40, searchLucene(query, 1000, null));
   }
}
