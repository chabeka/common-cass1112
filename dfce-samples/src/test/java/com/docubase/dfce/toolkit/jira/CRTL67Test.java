package com.docubase.dfce.toolkit.jira;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Test;

import com.docubase.dfce.commons.indexation.SystemFieldName;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL67Test extends AbstractCRTLTest {
   private static final String SRN_VALUE = "SRN_VALUE";
   private static final Date CREATION_DATE;
   private static final Date LIFE_CYCLE_REFERENCE_DATE;
   private Date documentArchivageDate;

   static {
      Calendar calendar = Calendar.getInstance();

      calendar.set(2009, 5, 20);
      CREATION_DATE = calendar.getTime();

      calendar.set(2011, 9, 13, 22, 10);
      LIFE_CYCLE_REFERENCE_DATE = calendar.getTime();

   }

   @Override
   public void before() {
      super.before();
      storeDocument();
   }

   private void storeDocument() {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(SRN, SRN_VALUE);

      document.setLifeCycleReferenceDate(LIFE_CYCLE_REFERENCE_DATE);
      document.setCreationDate(CREATION_DATE);

      // document.addCriterion(NCE, "169996144948345571");

      InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
      try {
         document = serviceProvider.getStoreService().storeDocument(document, "doc1", "pdf",
               inputStream);
      } catch (TagControlException e) {
         throw new RuntimeException(e);
      }

      documentArchivageDate = document.getArchivageDate();
   }

   @Test
   public void testArchivageDateDate() throws ExceededSearchLimitException,
         SearchQueryParseException {
      String formattedArchivageDate = serviceProvider.getSearchService().formatDate(
            documentArchivageDate, DateFormat.DATE);

      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_ARCHIVAGE_DATE + ":"
                  + formattedArchivageDate, 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testArchivageDateDateTime() throws ExceededSearchLimitException,
         SearchQueryParseException {

      String formattedArchivageDate = serviceProvider.getSearchService().formatDate(
            documentArchivageDate, DateFormat.DATETIME);

      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_ARCHIVAGE_DATE + ":"
                  + formattedArchivageDate, 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testArchivageDateRange() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_ARCHIVAGE_DATE
                  + ":[20000101 TO 20121231]", 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testLifeCycleReferenceDateDate() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_LIFE_CYCLE_REFERENCE_DATE
                  + ":20111013", 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testLifeCycleReferenceDateDateTime() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_LIFE_CYCLE_REFERENCE_DATE
                  + ":201110132010", 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testLifeCycleReferenceDateRange() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_LIFE_CYCLE_REFERENCE_DATE
                  + ":[20111013 TO 20111014]", 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testCreationDateDate() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService()
            .search("srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_CREATION_DATE + ":20090620",
                  10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testCreationDateDateTime() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService()
            .search("srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_CREATION_DATE + ":20090620",
                  10, base);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testCreationDateRange() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "srn:" + SRN_VALUE + " AND " + SystemFieldName.SM_CREATION_DATE
                  + ":[20090618 TO 20090621]", 10, base);
      assertEquals(1, searchResult.getTotalHits());
   }
}
