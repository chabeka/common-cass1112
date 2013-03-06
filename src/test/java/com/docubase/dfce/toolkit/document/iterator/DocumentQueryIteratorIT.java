package com.docubase.dfce.toolkit.document.iterator;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentQueryIteratorIT extends AbstractTestCaseCreateAndPrepareBase {
   private static BaseCategory c0;
   private static BaseCategory c1;
   private static BaseCategory c2;
   private static BaseCategory c4;

   @BeforeClass
   public static void setupAll() throws NoSuchJobException, JobExecutionAlreadyRunningException,
         JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobParametersNotFoundException, UnexpectedJobExecutionException {
      Document document;

      c0 = base.getBaseCategory(catNames[0]);
      c1 = base.getBaseCategory(catNames[1]);
      c2 = base.getBaseCategory(catNames[2]);
      c4 = base.getBaseCategory(catNames[4]);

      for (int i = 0; i < 50; i++) {
         document = ToolkitFactory.getInstance().createDocumentTag(base);

         // C0 unique
         document.addCriterion(c0, "testfilter" + i);

         // C1 soit Enfant, soit Adulte
         String c1Val = null;
         if (i < 10) { // Les enfants d'abord
            c1Val = "enfant";
         } else {
            c1Val = "adulte";
         }
         document.addCriterion(c1, c1Val);

         // C2. 2 valeurs, une qui varie tr�s peu, une qui est unique.
         document.addCriterion(c2, "personne" + i);
         document.addCriterion(c2, i % 2 == 0 ? "masculin" : "feminin");
         document.addCriterion(c4, 10);

         // stockage
         storeDocument(document, TestUtils.getFile("doc1.pdf"), true);
      }

      serviceProvider.getStorageAdministrationService().updateAllIndexesUsageCount();
   }

   @Test
   public void testCreateDocumentIterator_Monobase_C0_Joker() {
      SearchQuery query = ToolkitFactory.getInstance().createMonobaseQuery(
            c0.getName() + ":testfilter*", base);
      query.setSearchLimit(7);
      Iterator<Document> iterator = serviceProvider.getSearchService()
            .createDocumentIterator(query);

      Set<String> c0Values = new HashSet<String>();

      int count = 0;
      while (iterator.hasNext()) {
         Document document = iterator.next();
         c0Values.add(document.getSingleCriterion(c0.getName()).getWord().toString());
         count++;
      }

      assertEquals(50, c0Values.size());
   }

   @Test
   public void testCreateDocumentIterator_Monobase_C1() {
      SearchQuery query = ToolkitFactory.getInstance().createMonobaseQuery(
            c1.getName() + ":adulte", base);
      query.setSearchLimit(7);
      Iterator<Document> iterator = serviceProvider.getSearchService()
            .createDocumentIterator(query);

      Set<String> c0Values = new HashSet<String>();

      int count = 0;
      while (iterator.hasNext()) {
         Document document = iterator.next();
         c0Values.add(document.getSingleCriterion(c0.getName()).getWord().toString());
         count++;
      }

      assertEquals(40, c0Values.size());
   }

}
