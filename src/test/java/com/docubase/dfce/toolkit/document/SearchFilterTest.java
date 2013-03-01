package com.docubase.dfce.toolkit.document;

import static junit.framework.Assert.*;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.ChainedFilter.ChainedFilterOperator;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class SearchFilterTest extends AbstractTestCaseCreateAndPrepareBase {

   private static BaseCategory key;
   private static BaseCategory company;
   private static BaseCategory owner;
   private static BaseCategory id;
   private static BaseCategory number;

   /** UUID du document de r�f�ence pour les tests. */
   private static FileReference fileReference;

   @BeforeClass
   public static final void beforeClass() throws TagControlException {
      fileReference = createFileReference();
      insertDocument();
   }

   private static void insertDocument() throws TagControlException {
      Document tag = null;

      key = base.getBaseCategory(catNames[0]);
      company = base.getBaseCategory(catNames[1]);
      owner = base.getBaseCategory(catNames[2]);
      id = base.getBaseCategory(catNames[4]);
      number = base.getBaseCategory(catNames[8]);

      for (int i = 0; i < 200; i++) {
         tag = ToolkitFactory.getInstance().createDocumentTag(base);
         // Key is unique.
         tag.addCriterion(key, "key" + i);

         String c1Val = null;
         if (i < 100) {
            c1Val = "SPAM";
            tag.addCriterion(id, i % 2 == 0 ? "3" : "7");
         } else {
            c1Val = "AT&T";
            tag.addCriterion(id, i % 2 == 0 ? "23" : "47");
         }
         tag.addCriterion(company, c1Val);

         tag.addCriterion(owner, "number" + i);
         tag.addCriterion(owner, i % 2 == 0 ? "bob" : "sue");
         tag.addCriterion(number, i);

         // stockage
         insertVirtualDocument(tag, fileReference, 1, 5);
      }

   }

   /**
    * Insert un document virtuel.
    * 
    * @param fileReference
    *           uuid du document de r�f�rence
    * @param startPage
    *           premi�re page
    * @param endPage
    *           derni�re page
    * @param name
    *           nom du document virtuel.
    * 
    * @return le doucument virtuel.
    */
   private static Document insertVirtualDocument(Document document, FileReference fileReference,
         int startPage, int endPage) throws TagControlException {

      Document stored = serviceProvider.getStoreService().storeVirtualDocument(document,
            fileReference, startPage, endPage);

      assertNotNull(stored);
      return stored;
   }

   @Test
   public void testFilterAnd() throws ExceededSearchLimitException, SearchQueryParseException {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter()
            .addTermFilter(company.getName(), "spam", ChainedFilterOperator.AND)
            .addTermFilter(owner.getName(), "bob", ChainedFilterOperator.AND);
      SearchResult result = serviceProvider.getSearchService().search(key.getName() + ":key*",
            1000, base, chainedFilter);

      assertEquals(50, result.getTotalHits());
   }

   @Test
   public void testFilterOr() throws ExceededSearchLimitException, SearchQueryParseException {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter()
            .addTermFilter(company.getName(), "spam", ChainedFilterOperator.OR)
            .addTermFilter(company.getName(), "at&t", ChainedFilterOperator.OR);
      SearchResult result = serviceProvider.getSearchService().search(key.getName() + ":key*",
            1000, base, chainedFilter);
      assertEquals(200, result.getTotalHits());

   }

   @Test
   public void testFilterAndNot() throws ExceededSearchLimitException, SearchQueryParseException {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter()
            .addTermFilter(owner.getName(), "bob", ChainedFilterOperator.ANDNOT);

      SearchResult result = serviceProvider.getSearchService().search(key.getName() + ":key*",
            1000, base, chainedFilter);
      assertEquals(100, result.getTotalHits());
   }

   @Test
   public void testNumericRangeFilterExclusive() throws ExceededSearchLimitException,
         SearchQueryParseException {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter()
            .addIntRangeFilter(id.getName(), 7, 47, false, false);
      SearchResult result = serviceProvider.getSearchService().search(key.getName() + ":key*",
            1000, base, chainedFilter);
      assertEquals(50, result.getTotalHits());

   }

   @Test
   public void testNumericRangeFilterInclusive() throws ExceededSearchLimitException,
         SearchQueryParseException {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter()
            .addIntRangeFilter(id.getName(), 7, 47, true, true);
      SearchResult result = serviceProvider.getSearchService().search(key.getName() + ":key*",
            1000, base, chainedFilter);
      assertEquals(150, result.getTotalHits());

   }

   @Test
   public void testMultipleFilter() throws ExceededSearchLimitException, SearchQueryParseException {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter()
            .addTermFilter(company.getName(), "spam", ChainedFilterOperator.AND)
            .addTermFilter(owner.getName(), "bob", ChainedFilterOperator.AND)
            .addTermFilter(owner.getName(), "sue", ChainedFilterOperator.OR);
      SearchResult result = serviceProvider.getSearchService().search(
            number.getName() + ":[1 TO 10]", 1000, base, chainedFilter);

      assertEquals(10, result.getTotalHits());

   }

   @Test
   public void testNumericRangeQuery() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult result = serviceProvider.getSearchService().search(
            owner.getName() + ":bob AND " + number.getName() + ":[1 TO 20]", 1000, base);
      assertEquals(10, result.getTotalHits());
   }
}
