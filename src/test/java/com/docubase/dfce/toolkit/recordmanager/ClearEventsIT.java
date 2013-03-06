package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.administration.JobAdministrationService;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.commons.jobs.JobUtils;
import com.docubase.dfce.exception.IllegalEventsPurgeException;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class ClearEventsIT extends AbstractTestCaseCreateAndPrepareBase {
   private final ArchiveService archiveService = serviceProvider.getArchiveService();
   private final RecordManagerService recordManagerService = serviceProvider
         .getRecordManagerService();
   private final JobAdministrationService jobAdministrationService = serviceProvider
         .getJobAdministrationService();
   DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

   Calendar calendar = Calendar.getInstance();

   // trying to run system events clearing job on events that haven't
   // been archived yet -> Exception
   @Test
   public void testClearSystemEventsNotArchived() throws InterruptedException,
         IllegalEventsPurgeException, NoSuchJobException, JobInstanceAlreadyExistsException,
         JobParametersInvalidException, NoSuchJobExecutionException {
      RMSystemEvent rmSystemEvent = ToolkitFactory.getInstance().createRMSystemEvent();
      rmSystemEvent.setEventDescription("eventDescription");
      rmSystemEvent.setUsername("username");
      rmSystemEvent = recordManagerService.createCustomSystemEventLog(rmSystemEvent);
      Thread.sleep(2000);
      calendar = Calendar.getInstance();
      calendar.add(Calendar.SECOND, -1);
      String parameters = "end.date(date)=" + dateFormat.format(calendar.getTime())
            + ",type=SYSTEM";
      Long executionId = jobAdministrationService.start(JobUtils.CLEAR_EVENT_JOB, parameters);

      String summary = jobAdministrationService.getSummary(executionId);

      assertNotNull(summary);
      assertTrue(summary.contains("status=FAILED"));
   }

   // trying to run documents events clearing job on events that haven't
   // been archived yet -> Exception
   @Test
   public void testClearDocumentEventsNotArchived() throws InterruptedException,
         IllegalEventsPurgeException, NoSuchJobException, JobInstanceAlreadyExistsException,
         JobParametersInvalidException, NoSuchJobExecutionException {
      Thread.sleep(1000);
      RMDocEvent rmDocEvent = ToolkitFactory.getInstance().createRMDocEvent();
      rmDocEvent.setEventType(DocEventLogType.DELETE_DOCUMENT);
      rmDocEvent.setUsername("username");
      rmDocEvent.setDocUUID(UUID.randomUUID());
      rmDocEvent.setDocVersion("0.0.0");
      rmDocEvent = recordManagerService.createCustomDocumentEventLog(rmDocEvent);

      Thread.sleep(1000);

      String parameters = "end.date(date)=" + dateFormat.format(rmDocEvent.getEventDate())
            + ",type=DOCUMENT";
      Long executionId = jobAdministrationService.start(JobUtils.CLEAR_EVENT_JOB, parameters);

      String summary = jobAdministrationService.getSummary(executionId);

      assertNotNull(summary);
      assertTrue(summary.contains("status=FAILED"));
   }

   @SuppressWarnings("static-access")
   @Test
   public void testClearSystemEvents() throws InterruptedException, IllegalEventsPurgeException,
         UnexpectedJobExecutionException, NoSuchJobException, JobParametersNotFoundException,
         JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobInstanceAlreadyExistsException {
      Thread.currentThread().sleep(2000);
      System.out.println("Start testClearSystemEvents ... ");
      // archiving system events

      jobAdministrationService.startNextInstance(JobUtils.SYSTEM_LOGS_ARCHIVE_JOB);
      Date lastSucessfulRunDate = archiveService.getLastSucessfulSystemLogsArchiveRunDate();
      System.out.println("lastSucessfulRunDate:" + lastSucessfulRunDate);
      UUID lastSystemLogsArchiveUUID = archiveService.getLastSystemLogsArchiveUUID();
      System.out.println("Last archive UUID: " + lastSystemLogsArchiveUUID);
      // clearing system events to last archive date
      String parameters = "end.date(date)=" + dateFormat.format(lastSucessfulRunDate)
            + ",type=SYSTEM";
      jobAdministrationService.start(JobUtils.CLEAR_EVENT_JOB, parameters);

      // no events left in the date range (date0 to last run)
      List<RMSystemEvent> systemEventLogsByDates = recordManagerService.getSystemEventLogsByDates(
            new Date(0), lastSucessfulRunDate);
      System.out.println(systemEventLogsByDates.size());
      for (RMSystemEvent rmSystemEvent : systemEventLogsByDates) {
         System.out.println("Events: " + rmSystemEvent.toString() + " date: "
               + rmSystemEvent.getEventDate() + " archiveUUID: " + rmSystemEvent.getArchiveUUID());
      }
      assertTrue(systemEventLogsByDates.isEmpty());
      System.out.println("... end testClearSystemEvents");
   }

   @Test
   public void testClearDocumentEvents() throws InterruptedException, IllegalEventsPurgeException,
         UnexpectedJobExecutionException, NoSuchJobException, JobParametersNotFoundException,
         JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobInstanceAlreadyExistsException {
      Thread.sleep(1000);

      // archiving document events
      jobAdministrationService.startNextInstance(JobUtils.DOCUMENT_LOGS_ARCHIVE_JOB);

      Date lastSucessfulRunDate = archiveService.getLastSucessfulDocumentLogsArchiveRunDate();

      // clearing document events to last archive date
      // archiveService.clearDocumentEventsTo(lastSucessfulRunDate);
      String parameters = "end.date(date)=" + dateFormat.format(lastSucessfulRunDate)
            + ",type=DOCUMENT";
      jobAdministrationService.start(JobUtils.CLEAR_EVENT_JOB, parameters);

      // no events left in the date range (date0 to last run)
      List<RMDocEvent> documentEventLogsByDates = recordManagerService.getDocumentEventLogsByDates(
            new Date(0), lastSucessfulRunDate);
      assertTrue(documentEventLogsByDates.isEmpty());

   }

   @Test
   public void testClearNotAllSystemEvents() throws InterruptedException,
         IllegalEventsPurgeException, UnexpectedJobExecutionException, NoSuchJobException,
         JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobInstanceAlreadyExistsException {
      Thread.sleep(1000);

      System.out.println("Start testClearNotAllSystemEvents ...");
      // archiving system events
      jobAdministrationService.startNextInstance(JobUtils.SYSTEM_LOGS_ARCHIVE_JOB);

      // creating custom system event
      RMSystemEvent rmSystemEvent = ToolkitFactory.getInstance().createRMSystemEvent();
      String randomName = UUID.randomUUID().toString();
      rmSystemEvent.setUsername(randomName);
      recordManagerService.createCustomSystemEventLog(rmSystemEvent);

      Date lastSucessfulRunDate = archiveService.getLastSucessfulSystemLogsArchiveRunDate();
      // calendar.setTime(lastSucessfulRunDate);
      // calendar.add(Calendar.SECOND, -1);
      // lastSucessfulRunDate = calendar.getTime();

      // clearing event to job's last success (previous to custom event)
      String parameters = "end.date(date)=" + dateFormat.format(lastSucessfulRunDate)
            + ",type=SYSTEM";
      jobAdministrationService.start(JobUtils.CLEAR_EVENT_JOB, parameters);

      List<RMSystemEvent> systemEventLogsByDates = recordManagerService.getSystemEventLogsByDates(
            lastSucessfulRunDate, new Date());

      boolean eventFound = false;
      for (RMSystemEvent rmSystemEventIter : systemEventLogsByDates) {
         if (randomName.equals(rmSystemEventIter.getUsername())) {
            eventFound = true;
         }
      }

      // custom event hasn't been cleared
      assertTrue(eventFound);
   }

   @Test
   public void testClearNotAllDocumentEvents() throws InterruptedException,
         IllegalEventsPurgeException, UnexpectedJobExecutionException, NoSuchJobException,
         JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException,
         JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobInstanceAlreadyExistsException {
      Thread.sleep(1000);
      // archiving documents events
      jobAdministrationService.startNextInstance(JobUtils.DOCUMENT_LOGS_ARCHIVE_JOB);

      // creating custom document event
      RMDocEvent rmDocEvent = ToolkitFactory.getInstance().createRMDocEvent();
      String randomName = UUID.randomUUID().toString();
      rmDocEvent.setUsername(randomName);
      rmDocEvent.setDocUUID(UUID.randomUUID());
      rmDocEvent.setDocVersion("0.0.0");
      recordManagerService.createCustomDocumentEventLog(rmDocEvent);

      Date lastSucessfulRunDate = archiveService.getLastSucessfulDocumentLogsArchiveRunDate();

      System.out.println("lastSucessfulRunDate: " + lastSucessfulRunDate);
      calendar.setTime(lastSucessfulRunDate);
      calendar.add(Calendar.SECOND, -1);
      lastSucessfulRunDate = calendar.getTime();
      System.out.println("lastSucessfulRunDate -1s: " + lastSucessfulRunDate);
      // clearing event to job's last success (previous to custom event)
      String parameters = "end.date(date)=" + dateFormat.format(lastSucessfulRunDate)
            + ",type=DOCUMENT";
      jobAdministrationService.start(JobUtils.CLEAR_EVENT_JOB, parameters);

      List<RMDocEvent> documentEventLogsByDates = recordManagerService.getDocumentEventLogsByDates(
            lastSucessfulRunDate, new Date());

      boolean eventFound = false;
      for (RMDocEvent rmDocEventIter : documentEventLogsByDates) {
         if (randomName.equals(rmDocEventIter.getUsername())) {
            eventFound = true;
         }
      }
      // event is created, events have only been cleared to
      // lastSucessfulRunDate
      assertTrue(eventFound);
      System.out.println("... end testClearNotAllSystemEvents");
   }
}
