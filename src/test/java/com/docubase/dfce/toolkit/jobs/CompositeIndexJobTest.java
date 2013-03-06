package com.docubase.dfce.toolkit.jobs;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.service.administration.JobAdministrationService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.commons.jobs.JobUtils;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class CompositeIndexJobTest extends AbstractTestCaseCreateAndPrepareBase {

   @Before
   public void beforeTest() throws TagControlException {

      for (int i = 0; i < 10; i++) {
         Document document = toolkitFactory.createDocumentTag(base);
         document.addCriterion(category0, "value1" + i);
         document.addCriterion(category1, "value2" + i);
         storeDocument(document, TestUtils.getDefaultFile());
      }

      CompositeIndex compositIndex = serviceProvider.getStorageAdministrationService()
            .findOrCreateCompositeIndex(category0.getCategory(), category1.getCategory());
   }

   @Test
   public void testCompositeIndexJob() throws NoSuchJobException,
         JobInstanceAlreadyExistsException, JobParametersInvalidException,
         NoSuchJobExecutionException, JobInstanceAlreadyCompleteException, JobRestartException,
         NoSuchJobInstanceException, JobParametersNotFoundException,
         JobExecutionAlreadyRunningException, UnexpectedJobExecutionException {

      JobAdministrationService jobService = serviceProvider.getJobAdministrationService();

      String parameters = "composite.names=" + catNames[0] + catNames[1] + ",timestamp="
            + System.currentTimeMillis();

      Long jobExecutionId = jobService.start(JobUtils.INDEX_COMPOSITES_JOB, parameters);

   }

}
