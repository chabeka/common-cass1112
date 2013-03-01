package com.docubase.dfce.toolkit.lifecycle;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
import net.docubase.toolkit.model.reference.LifeCycleRule;

import org.apache.commons.lang.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class LifeCycleJobTest extends AbstractTestCaseCreateAndPrepareBase {
   private static LifeCycleRule lifeCycleRule1Year;
   private static LifeCycleRule lifeCycleRule2Years;
   private final Calendar calendar = Calendar.getInstance();

   @BeforeClass
   public static void beforeClass() throws ObjectAlreadyExistsException {
      lifeCycleRule1Year = serviceProvider.getStorageAdministrationService().getLifeCycleRule(
            "lifeCycleRule1YearId");
      if (lifeCycleRule1Year == null) {
         lifeCycleRule1Year = serviceProvider.getStorageAdministrationService()
               .createNewLifeCycleRule("lifeCycleRule1YearId", 1, LifeCycleLengthUnit.YEAR);
      }

      lifeCycleRule2Years = serviceProvider.getStorageAdministrationService().getLifeCycleRule(
            "lifeCycleRule2YearsId");
      if (lifeCycleRule2Years == null) {
         lifeCycleRule2Years = serviceProvider.getStorageAdministrationService()
               .createNewLifeCycleRule("lifeCycleRule2YearsId", 2, LifeCycleLengthUnit.YEAR);
      }

   }

   @SuppressWarnings("static-access")
   private Document storeDocumentWithRuleAndDate(String documentType, Date referenceDate,
         Date finalDate) throws FrozenDocumentException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(category0, "category0" + UUID.randomUUID().toString());
      File file = TestUtils.getFile("doc1.pdf");
      document.setType(documentType);
      document.setLifeCycleReferenceDate(referenceDate);

      try {
         Document storedDocument = storeDocument(document, file);

         if (finalDate != null) {
            serviceProvider.getStoreService().updateDocumentFinalDate(storedDocument, finalDate);

            try {
               Date now = new Date();
               while (now.before(finalDate)) {
                  Thread.currentThread().sleep(250);
                  now = new Date();
               }
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
         }

         return storedDocument;
      } catch (TagControlException e) {
         throw new IllegalArgumentException(e);
      }
   }

   @Test
   public void testRunJob1Year() throws FrozenDocumentException {
      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, -1);
      Date referenceDate = calendar.getTime();
      DateUtils.addDays(referenceDate, -1);
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule1Year.getDocumentType(),
            referenceDate, null);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getStorageAdministrationService().runLifeCycleJob();

      assertNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

   }

   @Test
   public void testRunJob2YearsAfterOneYear() throws FrozenDocumentException {
      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, -1);
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule2Years.getDocumentType(),
            calendar.getTime(), null);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getStorageAdministrationService().runLifeCycleJob();

      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));
   }

   @Test
   public void testRunJobOnOverloadedFinalDate() throws FrozenDocumentException {
      Date referenceDate = new Date();
      Date finalDate = DateUtils.addSeconds(referenceDate, 4);

      Document document = storeDocumentWithRuleAndDate(lifeCycleRule2Years.getDocumentType(),
            referenceDate, finalDate);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getStorageAdministrationService().runLifeCycleJob();

      assertNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));
   }

}
