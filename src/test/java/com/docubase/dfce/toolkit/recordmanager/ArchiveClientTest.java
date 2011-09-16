package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMLogArchiveReport;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.joda.time.DateTime;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class ArchiveClientTest extends AbstractTestCaseCreateAndPrepareBase {
    private final ArchiveService archiveService = ServiceProvider
	    .getArchiveService();
    private final RecordManagerService recordManagerService = ServiceProvider
	    .getRecordManagerService();

    private final SearchService searchService = ServiceProvider
	    .getSearchService();

    private final StoreService storeService = ServiceProvider.getStoreService();

    @Test
    public void runDocumentLogsArchiveJob() throws IOException {
	assertFalse(archiveService.isDocumentLogsArchiveRunning());

	RMDocEvent rmDocEvent = ToolkitFactory.getInstance().createRMDocEvent();
	UUID randomUUID = UUID.randomUUID();
	rmDocEvent.setDocUUID(randomUUID);
	rmDocEvent.setEventDescription("eventDescription");
	rmDocEvent.setEventType(DocEventLogType.CREATE_DOCUMENT);
	rmDocEvent.setUsername("username");
	recordManagerService.createCustomDocumentEventLog(rmDocEvent);

	Document documentTag = ToolkitFactory.getInstance().createDocumentTag(
		base);
	documentTag.addCriterion(baseCategory0, UUID.randomUUID().toString());
	File file = getFile("doc1.pdf", ArchiveClientTest.class);
	storeDoc(documentTag, file, true);

	archiveService.createNextDocumentLogsArchive();

	UUID lastDocumentLogsArchiveUUID = archiveService
		.getLastDocumentLogsArchiveUUID();
	Base archiveBase = archiveService.getLogsArchiveBase();

	Document archive = searchService.getDocumentByUUID(archiveBase,
		lastDocumentLogsArchiveUUID);

	InputStream documentFile = storeService.getDocumentFile(archive);

	BufferedReader bufferedReader = new BufferedReader(
		new InputStreamReader(documentFile));
	String readLine = bufferedReader.readLine();
	String eventLine = null;
	while (readLine != null) {
	    System.out.println(readLine);
	    if (readLine.contains(randomUUID.toString())
		    && readLine
			    .contains(DocEventLogType.CREATE_DOCUMENT.name())
		    && readLine.contains("username")) {
		eventLine = readLine;
	    }
	    readLine = bufferedReader.readLine();
	}
	assertNotNull(eventLine);
    }

    @Test
    public void runDocumentLogsArchiveJobChaining() throws IOException {
	assertFalse(archiveService.isDocumentLogsArchiveRunning());

	archiveService.createNextDocumentLogsArchive();
	archiveService.createNextDocumentLogsArchive();
	UUID archive1UUID = archiveService.getLastDocumentLogsArchiveUUID();
	archiveService.createNextDocumentLogsArchive();
	UUID archive2UUID = archiveService.getLastDocumentLogsArchiveUUID();

	Base archiveBase = archiveService.getLogsArchiveBase();
	Document archive1 = searchService.getDocumentByUUID(archiveBase,
		archive1UUID);
	Document archive2 = searchService.getDocumentByUUID(archiveBase,
		archive2UUID);

	InputStream archive1DocFile = storeService.getDocumentFile(archive1);
	InputStream archive2DocFile = storeService.getDocumentFile(archive2);

	List<String> archive1Lines = extractLines(archive1DocFile);
	List<String> archive2Lines = extractLines(archive2DocFile);

	Criterion previousUUIDCriterion = archive2
		.getSingleCriterion(ArchiveService.PREVIOUS_LOG_ARCHIVE_UUID);
	Object previousUUIDFromDocument = previousUUIDCriterion.getWord();

	Date archive1EndDate = (Date) archive1.getSingleCriterion(
		ArchiveService.LOG_ARCHIVE_END_DATE).getWord();

	Date archive2BeginDate = (Date) archive2.getSingleCriterion(
		ArchiveService.LOG_ARCHIVE_BEGIN_DATE).getWord();

	assertEquals(archive1UUID, previousUUIDFromDocument);

	assertEquals(archive1EndDate, archive2BeginDate);
	// last line of archive2 should contain archive 1 digest
	assertEquals(archive1.getDigest(),
		archive2Lines.get(archive2Lines.size() - 1));
	// last but on line of archive1 should be != then first archive2 line
	assertNotSame(archive1Lines.get(archive1Lines.size() - 2),
		archive2Lines.get(0));
    }

    private List<String> extractLines(InputStream inputStream) {
	List<String> allLines = new ArrayList<String>();
	BufferedReader bufferedReader = new BufferedReader(
		new InputStreamReader(inputStream));
	String readLine;
	try {
	    readLine = bufferedReader.readLine();
	    while (readLine != null) {
		allLines.add(readLine);
		readLine = bufferedReader.readLine();
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	return allLines;

    }

    @Test
    public void runSystemLogsArchiveJob() throws IOException {
	assertFalse(archiveService.isDocumentLogsArchiveRunning());

	RMSystemEvent rmSystemEvent = ToolkitFactory.getInstance()
		.createRMSystemEvent();
	rmSystemEvent.setEventDescription("eventDescription");
	rmSystemEvent.setUsername("username");
	recordManagerService.createCustomSystemEventLog(rmSystemEvent);

	archiveService.createNextSystemLogsArchive();

	UUID lastDocumentLogsArchiveUUID = archiveService
		.getLastSystemLogsArchiveUUID();
	Base archiveBase = archiveService.getLogsArchiveBase();

	Document archive = searchService.getDocumentByUUID(archiveBase,
		lastDocumentLogsArchiveUUID);

	InputStream documentFile = storeService.getDocumentFile(archive);

	BufferedReader bufferedReader = new BufferedReader(
		new InputStreamReader(documentFile));
	String eventLine = null;
	String readLine = bufferedReader.readLine();
	while (readLine != null) {
	    if (readLine.contains("eventDescription")
		    && readLine.contains("username")) {
		eventLine = readLine;
	    }
	    readLine = bufferedReader.readLine();
	}
	assertNotNull(eventLine);
    }

    @Test
    public void runArchiveLogChainingReport() {
	Date lastSucessfullRunDate = archiveService
		.getLastSucessfulDocumentLogsArchiveRunDate();
	if (lastSucessfullRunDate == null) {
	    archiveService.createNextDocumentLogsArchive();
	    lastSucessfullRunDate = archiveService
		    .getLastSucessfulDocumentLogsArchiveRunDate();
	}

	Date beginDate = lastSucessfullRunDate;

	archiveService.createNextDocumentLogsArchive();
	archiveService.createNextDocumentLogsArchive();
	archiveService.createNextDocumentLogsArchive();
	archiveService.createNextDocumentLogsArchive();

	Date endDate = new DateTime(
		archiveService.getLastSucessfulDocumentLogsArchiveRunDate())
		.plusMinutes(1).toDate();

	List<RMLogArchiveReport> chainingReport = archiveService
		.createDocumentLogArchiveChainingReport(beginDate, endDate);

	assertEquals(5, chainingReport.size());
	for (RMLogArchiveReport rmLogArchiveReport : chainingReport) {
	    assertEquals(rmLogArchiveReport.getReComputedDigest(),
		    rmLogArchiveReport.getDigest());
	}

    }
}
