package com.docubase.dfce.toolkit.jobs;

import java.util.Date;

import net.docubase.toolkit.service.administration.JobAdministrationService;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

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
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentIntegrityCheckJobIT extends AbstractTestCaseCreateAndPrepareBase {

   @Test
   public void testDocumentIntegrityCheckJob() throws NoSuchJobException,
         JobInstanceAlreadyExistsException, JobParametersInvalidException,
         NoSuchJobExecutionException, JobInstanceAlreadyCompleteException, JobRestartException,
         NoSuchJobInstanceException, JobParametersNotFoundException,
         JobExecutionAlreadyRunningException, UnexpectedJobExecutionException {

      JobAdministrationService jobService = serviceProvider.getJobAdministrationService();

      String formattedDate = serviceProvider.getSearchService().formatDate(new Date(),
            DateFormat.DATETIME);

      String parameters = "start.date(date)=" + formattedDate + ",step.size(long)=" + 5;

      jobService.start(JobUtils.DOCUMENTS_INTEGRITY_CHECK, parameters);
   }
}
