package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class RichQueriesIT extends AbstractTestCaseCreateAndPrepareBase {
   private static BaseCategory c0;
   private static BaseCategory c1;
   private static BaseCategory c2;
   private static BaseCategory c4;

   @BeforeClass
   public static void setupAll() throws NoSuchJobException, JobExecutionAlreadyRunningException,
         JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobParametersNotFoundException, UnexpectedJobExecutionException {
      System.out.println("****** testCompleteQueryScenarii ******");
      Document tag;

      c0 = base.getBaseCategory(catNames[0]);
      c1 = base.getBaseCategory(catNames[1]);
      c2 = base.getBaseCategory(catNames[2]);
      c4 = base.getBaseCategory(catNames[4]);

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

         // C2. 2 valeurs, une qui varie tr�s peu, une qui est unique.
         tag.addCriterion(c2, "personne" + i);
         tag.addCriterion(c2, i % 2 == 0 ? "masculin" : "feminin");
         tag.addCriterion(c4, 10);

         // stockage
         storeDocument(tag, TestUtils.getFile("doc1.pdf"), true);
      }

      serviceProvider.getStorageAdministrationService().updateAllIndexesUsageCount();
   }

   @Test
   public void testSimpleQuery() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c1.getName() + ":adulte";
      assertEquals(40, searchLucene(query, 1000, null));
   }

   @Test(expected = SearchQueryParseException.class)
   public void testLeadingWildcardQuery() throws ExceededSearchLimitException,
         SearchQueryParseException {
      String query = c1.getName() + ":*";
      searchLucene(query, 1000, null);
   }

   @Test(expected = SearchQueryParseException.class)
   public void testEmptyQuery() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c1.getName() + ":";
      searchLucene(query, 1000, null);
   }

   @Test
   public void testCompositeQuery() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = "(" + c1.getName() + ":adulte" + " AND " + c2.getName() + ":masculin"
            + ") OR (" + c1.getName() + ":enfant" + " AND " + c2.getName() + ":feminin)";
      assertEquals(25, searchLucene(query, 1000, null));
   }

   @Test
   public void testRange() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c1.getName() + ":adulte" + " AND " + c2.getName() + ":[feminin TO masculin]";
      assertEquals(40, searchLucene(query, 1000, null));
   }

   @Test(expected = ExceededSearchLimitException.class)
   public void testExceededSearchLimit() throws ExceededSearchLimitException,
         SearchQueryParseException {
      String query = c1.getName() + ":adulte";
      serviceProvider.getSearchService().search(query, 100, 0, base, null, 5);
   }

   @Test
   public void testAnd() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c1.getName() + ":adulte" + " AND " + c4.getName() + ":10";
      assertEquals(40, searchLucene(query, 1000, null));
   }
}
