package com.docubase.dfce.toolkit.jobs;

import static org.junit.Assert.*;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.NoSuchJobException;

import com.docubase.dfce.commons.jobs.JobUtils;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class IndexCategoriesJob extends AbstractTestCaseCreateAndPrepareBase {

   private UUID[] uuids;

   @Test
   public void testIndexCategoriesJob() throws NoSuchJobException,
         JobInstanceAlreadyExistsException, JobParametersInvalidException,
         ExceededSearchLimitException, TagControlException {
      // Disable index on category1
      BaseCategory baseCategory = base.getBaseCategory(category1.getName());
      baseCategory.setIndexed(false);
      Base updateBase = serviceProvider.getBaseAdministrationService().updateBase(base);
      assertFalse(updateBase.getBaseCategory(category1.getName()).isIndexed());

      // Inject 10 document.
      uuids = new UUID[10];

      for (int i = 0; i < 10; i++) {
         UUID randomUUID = UUID.randomUUID();
         Document document = toolkitFactory.createDocumentTag(base);
         document.addCriterion(category0, "value1" + i);
         document.addCriterion(category1, randomUUID.toString());
         storeDocument(document, TestUtils.getDefaultFile());
         uuids[i] = randomUUID;
      }

      // Try search on category1. Should fail as category1 is note indexed.
      for (UUID uuid : uuids) {
         SearchQuery searchQuery = toolkitFactory.createMonobaseQuery(category1.getName() + ":"
               + uuid, updateBase);

         SearchResult search;
         try {
            search = serviceProvider.getSearchService().search(searchQuery);
            assertEquals(0, search.getTotalHits());
         } catch (SearchQueryParseException e) {
            assertTrue(true);
         }
      }

      // Enable index on category1
      baseCategory = base.getBaseCategory(category1.getName());
      baseCategory.setIndexed(true);
      serviceProvider.getBaseAdministrationService().updateBase(base);

      // Run index Job on category1
      String parameters = "category.names=" + category1.getName() + ",timestamp="
            + System.currentTimeMillis();
      serviceProvider.getJobAdministrationService()
            .start(JobUtils.INDEX_CATEGORIES_JOB, parameters);

      // Search should succeed.
      for (UUID uuid : uuids) {
         SearchQuery searchQuery = toolkitFactory.createMonobaseQuery(category1.getName() + ":"
               + uuid, updateBase);

         SearchResult search;
         try {
            search = serviceProvider.getSearchService().search(searchQuery);
            assertEquals(1, search.getTotalHits());
         } catch (SearchQueryParseException e) {
            fail("Search should succeed");
         }
      }
   }

}
