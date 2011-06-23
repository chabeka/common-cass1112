package com.docubase.dfce.toolkit.client.ged;

import java.util.Date;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Test;

import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

public class SearchClientTest extends AbstractDFCEToolkitClientTest {
   private SearchService searchService = ServiceProvider.getSearchService();

   @Test
   public void testFormatDate() {
      searchService.formatDate(new Date(), DateFormat.DATE);
   }

}
