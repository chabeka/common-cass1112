package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.junit.Test;

import com.docubase.dfce.exception.IllegalEventsPurgeException;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class ClearEventsTest extends AbstractTestCaseCreateAndPrepareBase {
    private ArchiveService archiveService = serviceProvider.getArchiveService();
    private RecordManagerService recordManagerService = serviceProvider
	    .getRecordManagerService();

    Calendar calendar = Calendar.getInstance();

    // trying to run system events clearing job on events that haven't
    // been archived yet -> Exception
    @Test(expected = IllegalEventsPurgeException.class)
    public void testClearSystemEventsNotArchived() throws InterruptedException,
	    IllegalEventsPurgeException {
	RMSystemEvent rmSystemEvent = ToolkitFactory.getInstance()
		.createRMSystemEvent();
	rmSystemEvent.setEventDescription("eventDescription");
	rmSystemEvent.setUsername("username");
	rmSystemEvent = recordManagerService
		.createCustomSystemEventLog(rmSystemEvent);
	Thread.sleep(2000);
	calendar = Calendar.getInstance();
	calendar.add(Calendar.SECOND, -1);
	archiveService.clearSystemEventsTo(calendar.getTime());
    }

    // trying to run documents events clearing job on events that haven't
    // been archived yet -> Exception
    @Test(expected = IllegalEventsPurgeException.class)
    public void testClearDocumentEventsNotArchived()
	    throws InterruptedException, IllegalEventsPurgeException {
	Thread.sleep(1000);
	RMDocEvent rmDocEvent = ToolkitFactory.getInstance().createRMDocEvent();
	rmDocEvent.setEventType(DocEventLogType.DELETE_DOCUMENT);
	rmDocEvent.setUsername("username");
	rmDocEvent.setDocUUID(UUID.randomUUID());
	rmDocEvent = recordManagerService
		.createCustomDocumentEventLog(rmDocEvent);

	Thread.sleep(1000);
	archiveService.clearDocumentEventsTo(rmDocEvent.getEventDate());
    }

    @SuppressWarnings("static-access")
    @Test
    public void testClearSystemEvents() throws InterruptedException,
	    IllegalEventsPurgeException {
	Thread.currentThread().sleep(1000);
	// archiving system events
	archiveService.createNextSystemLogsArchive();
	Date lastSucessfulRunDate = archiveService
		.getLastSucessfulSystemLogsArchiveRunDate();

	// clearing system events to last archive date
	archiveService.clearSystemEventsTo(lastSucessfulRunDate);

	// no events left in the date range (date0 to last run)
	List<RMSystemEvent> systemEventLogsByDates = recordManagerService
		.getSystemEventLogsByDates(new Date(0), lastSucessfulRunDate);
	assertTrue(systemEventLogsByDates.isEmpty());
    }

    @Test
    public void testClearDocumentEvents() throws InterruptedException,
	    IllegalEventsPurgeException {
	Thread.sleep(1000);

	// archiving document events
	archiveService.createNextDocumentLogsArchive();
	Date lastSucessfulRunDate = archiveService
		.getLastSucessfulDocumentLogsArchiveRunDate();

	// clearing document events to last archive date
	archiveService.clearDocumentEventsTo(lastSucessfulRunDate);

	// no events left in the date range (date0 to last run)
	List<RMDocEvent> documentEventLogsByDates = recordManagerService
		.getDocumentEventLogsByDates(new Date(0), lastSucessfulRunDate);
	assertTrue(documentEventLogsByDates.isEmpty());

    }

    @Test
    public void testClearNotAllSystemEvents() throws InterruptedException,
	    IllegalEventsPurgeException {
	Thread.sleep(1000);
	// archiving documents events
	archiveService.createNextSystemLogsArchive();

	// creating custom system event
	RMSystemEvent rmSystemEvent = ToolkitFactory.getInstance()
		.createRMSystemEvent();
	String randomName = UUID.randomUUID().toString();
	rmSystemEvent.setUsername(randomName);
	recordManagerService.createCustomSystemEventLog(rmSystemEvent);

	Date lastSucessfulRunDate = archiveService
		.getLastSucessfulSystemLogsArchiveRunDate();
	calendar.setTime(lastSucessfulRunDate);
	calendar.add(Calendar.SECOND, -1);
	lastSucessfulRunDate = calendar.getTime();

	// clearing event to job's last success (previous to custom event)
	archiveService.clearSystemEventsTo(lastSucessfulRunDate);
	List<RMSystemEvent> systemEventLogsByDates = recordManagerService
		.getSystemEventLogsByDates(lastSucessfulRunDate, new Date());

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
	    IllegalEventsPurgeException {
	Thread.sleep(1000);
	// archiving documents events
	archiveService.createNextDocumentLogsArchive();

	// creating custom document event
	RMDocEvent rmDocEvent = ToolkitFactory.getInstance().createRMDocEvent();
	String randomName = UUID.randomUUID().toString();
	rmDocEvent.setUsername(randomName);
	rmDocEvent.setDocUUID(UUID.randomUUID());
	recordManagerService.createCustomDocumentEventLog(rmDocEvent);

	Date lastSucessfulRunDate = archiveService
		.getLastSucessfulDocumentLogsArchiveRunDate();
	calendar.setTime(lastSucessfulRunDate);
	calendar.add(Calendar.SECOND, -1);
	lastSucessfulRunDate = calendar.getTime();

	// clearing event to job's last success (previous to custom event)
	archiveService.clearDocumentEventsTo(lastSucessfulRunDate);
	List<RMDocEvent> documentEventLogsByDates = recordManagerService
		.getDocumentEventLogsByDates(lastSucessfulRunDate, new Date());

	boolean eventFound = false;
	for (RMDocEvent rmDocEventIter : documentEventLogsByDates) {
	    if (randomName.equals(rmDocEventIter.getUsername())) {
		eventFound = true;
	    }
	}
	// event is created, events have only been cleared to
	// lastSucessfulRunDate
	assertTrue(eventFound);
    }
}
