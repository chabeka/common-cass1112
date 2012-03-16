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
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL77Test extends AbstractCRTLTest {
   private static final String SRN_VALUE = "39";
   private Date CREATION_DATE;
   private Date ARCHIVAGE_DATE;
   private Date MODIFICATION_DATE;

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
         document.setTitle("My Document");
         document = serviceProvider.getStoreService().updateDocument(document);
      } catch (TagControlException e) {
         throw new RuntimeException(e);
      } catch (FrozenDocumentException e) {
         throw new RuntimeException(e);
      }

      ARCHIVAGE_DATE = document.getArchivageDate();
      CREATION_DATE = document.getCreationDate();
      MODIFICATION_DATE = document.getModificationDate();
   }

   @Test
   public void testQueryARCHIVAGE_DATE() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String archivageDateAsString = searchService.formatDate(ARCHIVAGE_DATE, DateFormat.DATETIME);

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_ARCHIVAGE_DATE + ":" + archivageDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }

   @Test
   public void testQueryCREATION_DATE() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String creationDateAsString = searchService.formatDate(CREATION_DATE, DateFormat.DATETIME);

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_CREATION_DATE + ":" + creationDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }

   @Test
   public void testQueryMODIFICATION_DATE() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchService searchService = serviceProvider.getSearchService();
      String modificationDateAsString = searchService.formatDate(MODIFICATION_DATE,
            DateFormat.DATETIME);

      SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(
            SystemFieldName.SM_MODIFICATION_DATE + ":" + modificationDateAsString, base);

      SearchResult searchResult = searchService.search(searchQuery);

      assertEquals(1, searchResult.getDocuments().size());
   }

}
