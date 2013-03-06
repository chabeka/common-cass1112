package com.docubase.dfce.toolkit.lifecycle;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
import net.docubase.toolkit.model.reference.LifeCycleRule;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.commons.jobs.JobUtils;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class LifeCycleJobIT extends AbstractTestCaseCreateAndPrepareBase {
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
            calendar.setTime(new Date());
            calendar.add(Calendar.MILLISECOND, 2000);

            serviceProvider.getStoreService().updateDocumentFinalDate(storedDocument,
                  calendar.getTime());

            try {
               Thread.sleep(2000);
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
   public void testRunJob1Year() throws FrozenDocumentException, NoSuchJobException,
         JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException,
         JobParametersInvalidException {
      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, -1);
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule1Year.getDocumentType(),
            calendar.getTime(), null);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      assertNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));
   }

   @Test
   public void testRunJob1Year_increase() throws FrozenDocumentException, NoSuchJobException,
         JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException,
         JobParametersInvalidException, ObjectAlreadyExistsException {
      String documentType = "TEST_LIFCE_CYCLE_RULE_UPDATE_INCREASE";

      LifeCycleRule lifeCycleRule = serviceProvider.getStorageAdministrationService()
            .getLifeCycleRule(documentType);
      if (lifeCycleRule == null) {
         lifeCycleRule = serviceProvider.getStorageAdministrationService().createNewLifeCycleRule(
               documentType, 2, LifeCycleLengthUnit.YEAR);
      } else {
         lifeCycleRule = serviceProvider.getStorageAdministrationService().updateLifeCycleRule(
               documentType, 2, LifeCycleLengthUnit.YEAR);
      }

      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, -1);
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule.getDocumentType(),
            calendar.getTime(), null);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getStorageAdministrationService().updateLifeCycleRule(
            lifeCycleRule.getDocumentType(), 5, LifeCycleLengthUnit.YEAR);

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      // le document n'a pas �t� supprim�
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

   }

   @Test
   public void testRunJob1Year_reduce() throws FrozenDocumentException, NoSuchJobException,
         JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException,
         JobParametersInvalidException, ObjectAlreadyExistsException {

      String documentType = "TEST_LIFCE_CYCLE_RULE_UPDATE_REDUCE";
      LifeCycleRule lifeCycleRule = serviceProvider.getStorageAdministrationService()
            .getLifeCycleRule(documentType);
      if (lifeCycleRule == null) {
         lifeCycleRule = serviceProvider.getStorageAdministrationService().createNewLifeCycleRule(
               documentType, 2, LifeCycleLengthUnit.YEAR);
      } else {
         lifeCycleRule = serviceProvider.getStorageAdministrationService().updateLifeCycleRule(
               documentType, 2, LifeCycleLengthUnit.YEAR);
      }

      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, -1);
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule.getDocumentType(),
            calendar.getTime(), null);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getStorageAdministrationService().updateLifeCycleRule(
            lifeCycleRule.getDocumentType(), 1, LifeCycleLengthUnit.MONTH);

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      // le document a �t� supprim�
      assertNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));
   }

   @Test
   public void testRunJob2YearsAfterOneYear() throws FrozenDocumentException, NoSuchJobException,
         JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException,
         JobParametersInvalidException {
      calendar.setTime(new Date());
      calendar.add(Calendar.YEAR, -1);
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule2Years.getDocumentType(),
            calendar.getTime(), null);
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));
   }

   @Test
   public void testRunJobOnOverloadedFinalDate() throws FrozenDocumentException,
         NoSuchJobException, JobParametersNotFoundException, JobRestartException,
         JobExecutionAlreadyRunningException, JobInstanceAlreadyCompleteException,
         UnexpectedJobExecutionException, JobParametersInvalidException {
      Document document = storeDocumentWithRuleAndDate(lifeCycleRule2Years.getDocumentType(),
            new Date(), new Date());
      UUID documentUUID = document.getUuid();
      assertNotNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));

      serviceProvider.getJobAdministrationService().startNextInstance(JobUtils.LIFE_CYCLE_JOB);

      assertNull(serviceProvider.getSearchService().getDocumentByUUID(base, documentUUID));
   }

}
