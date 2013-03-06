package com.docubase.dfce.toolkit.jira;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ged.SearchService;

import org.junit.Assert;
import org.junit.Test;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL95IT extends AbstractCRTLIT {
   private static final String SRT_VALUE = "123456";
   private static final String RND_VALUE1 = "2.3.1.1.12";
   private static final String RND_VALUE2 = "2.3.1.1.8";

   private static final Date CREATION_DATE;

   static {
      Calendar calendar = Calendar.getInstance();

      calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
      calendar.set(2009, 5, 20, 0, 0, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      CREATION_DATE = calendar.getTime();
   }

   @Override
   public void before() {
      super.before();
      storeDocuments();
   }

   private void storeDocuments() {
      Document document1 = ToolkitFactory.getInstance().createDocumentTag(base);
      document1.addCriterion(RND, RND_VALUE1);
      document1.addCriterion(SRT, SRT_VALUE);
      document1.setCreationDate(CREATION_DATE);

      InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
      try {
         document1 = serviceProvider.getStoreService().storeDocument(document1, "doc1", "pdf",
               inputStream);
      } catch (TagControlException e) {
         throw new RuntimeException(e);
      }

      Document document2 = ToolkitFactory.getInstance().createDocumentTag(base);
      document2.addCriterion(RND, RND_VALUE2);
      document2.addCriterion(SRT, SRT_VALUE);
      document2.setCreationDate(CREATION_DATE);

      inputStream = TestUtils.getInputStream("doc1.pdf");
      try {
         document2 = serviceProvider.getStoreService().storeDocument(document2, "doc1", "pdf",
               inputStream);
      } catch (TagControlException e) {
         throw new RuntimeException(e);
      }
   }

   @Test
   public void testParenthesis1() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            "(SRT:123456) AND ((RND:2.3.1.1.12) OR (RND:2.3.1.1.8))", base);
      SearchResult searchResult = searchService.search(searchQuery);

      Assert.assertEquals(2, searchResult.getTotalHits());
   }

   @Test
   public void testParenthesis2() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            "(((RND:2.3.1.1.12) OR (RND:2.3.1.1.8))) AND (SRT:123456)", base);
      SearchResult searchResult = searchService.search(searchQuery);

      Assert.assertEquals(2, searchResult.getTotalHits());
   }
}
