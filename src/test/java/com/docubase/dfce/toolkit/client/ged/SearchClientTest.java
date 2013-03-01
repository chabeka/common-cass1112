package com.docubase.dfce.toolkit.client.ged;

import java.util.Date;

import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;

public class SearchClientTest extends AbstractTestBase {
   private final SearchService searchService = serviceProvider.getSearchService();

   @BeforeClass
   public static void setUp() {
      connect();
   }

   @AfterClass
   public static void tearDown() {
      disconnect();
   }

   @Test
   public void testFormatDate() {
      searchService.formatDate(new Date(), DateFormat.DATE);
   }
}
