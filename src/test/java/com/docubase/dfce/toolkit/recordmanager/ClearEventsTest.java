package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class ClearEventsTest extends AbstractTestCaseCreateAndPrepareBase {
    private ArchiveService archiveService = ServiceProvider.getArchiveService();
    private RecordManagerService recordManagerService = ServiceProvider
	    .getRecordManagerService();

    @Test(expected = UnsupportedOperationException.class)
    public void testClearSystemEventsNotArchived() {
	RMSystemEvent rmSystemEvent = ToolkitFactory.getInstance()
		.createRMSystemEvent();
	rmSystemEvent.setEventDescription("eventDescription");
	rmSystemEvent.setUsername("username");
	rmSystemEvent = recordManagerService
		.createCustomSystemEventLog(rmSystemEvent);

	archiveService.clearSystemEventsTo(rmSystemEvent.getEventDate());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClearDocumentEventsNotArchived() {
	RMDocEvent rmDocEvent = ToolkitFactory.getInstance().createRMDocEvent();
	rmDocEvent.setEventType(DocEventLogType.DELETE_DOCUMENT);
	rmDocEvent.setUsername("username");
	rmDocEvent.setDocUUID(UUID.randomUUID());
	rmDocEvent = recordManagerService
		.createCustomDocumentEventLog(rmDocEvent);

	archiveService.clearDocumentEventsTo(rmDocEvent.getEventDate());
    }

    @Test
    public void testClearSystemEvents() {
	archiveService.createNextSystemLogsArchive();
	Date lastSucessfulRunDate = archiveService
		.getLastSucessfulSystemLogsArchiveRunDate();
	archiveService.clearSystemEventsTo(lastSucessfulRunDate);
	List<RMSystemEvent> systemEventLogsByDates = recordManagerService
		.getSystemEventLogsByDates(new Date(0), lastSucessfulRunDate);
	assertTrue("size  == " + systemEventLogsByDates.size(),
		systemEventLogsByDates.isEmpty());
    }

    @Test
    public void testClearDocumentEvents() {
	archiveService.createNextDocumentLogsArchive();
	Date lastSucessfulRunDate = archiveService
		.getLastSucessfulDocumentLogsArchiveRunDate();
	archiveService.clearDocumentEventsTo(lastSucessfulRunDate);
	List<RMDocEvent> documentEventLogsByDates = recordManagerService
		.getDocumentEventLogsByDates(new Date(0), lastSucessfulRunDate);
	assertTrue(documentEventLogsByDates.isEmpty());

    }
}
