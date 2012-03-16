package com.docubase.dfce.toolkit.jira;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Date;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Test;

import com.docubase.dfce.commons.indexation.SystemFieldName;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL76Test extends AbstractCRTLTest {
   private static final String SRN_VALUE = "39";
   private static final Date CREATION_DATE;
   private Date ARCHIVAGE_DATE;

   static {
      CREATION_DATE = new Date();
   }

   @Override
   public void before() {
      super.before();
      storeDocument();
   }

   private void storeDocument() {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(SRN, SRN_VALUE);

      document.setCreationDate(CREATION_DATE);

      InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
      try {
         document = serviceProvider.getStoreService().storeDocument(document, "doc1", "pdf",
               inputStream);
      } catch (TagControlException e) {
         throw new RuntimeException(e);
      }

      ARCHIVAGE_DATE = document.getArchivageDate();
   }

   @Test
   public void testQueryDatetime_Day() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String archivageDateAsString = searchService.formatDate(ARCHIVAGE_DATE, DateFormat.DATE);
      assertEquals(8, archivageDateAsString.length());

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_ARCHIVAGE_DATE + ":" + archivageDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }

   @Test
   public void testQueryDatetime_Millisecond() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String archivageDateAsString = searchService.formatDate(ARCHIVAGE_DATE, DateFormat.DATETIME);
      assertEquals(17, archivageDateAsString.length());

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_ARCHIVAGE_DATE + ":" + archivageDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }

   @Test
   public void testQueryDatetime_Datetime_Minute() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String archivageDateAsString = searchService.formatDate(ARCHIVAGE_DATE, DateFormat.DATETIME);
      assertEquals(17, archivageDateAsString.length());
      archivageDateAsString = archivageDateAsString.substring(0, 12);
      assertEquals(12, archivageDateAsString.length());

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_ARCHIVAGE_DATE + ":" + archivageDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }

   @Test
   public void testQueryDatetime_Datetime_13Characters() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String archivageDateAsString = searchService.formatDate(ARCHIVAGE_DATE, DateFormat.DATETIME);
      assertEquals(17, archivageDateAsString.length());
      archivageDateAsString = archivageDateAsString.substring(0, 13);
      assertEquals(13, archivageDateAsString.length());

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_ARCHIVAGE_DATE + ":" + archivageDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }
}
