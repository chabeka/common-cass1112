package com.docubase.dfce.toolkit.recordmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public abstract class AbstractEventTest extends
	AbstractTestCaseCreateAndPrepareBase {

    /** Numero de page */
    protected static int PAGE = 1;

    /** Valeur fictive de keyDoc */
    protected static final UUID KEY_DOC = UUID.randomUUID();

    protected Date beginDate;

    protected Date endDate;

    @Before
    public void beforeEach() {
	Calendar calendar = GregorianCalendar.getInstance();
	endDate = new Date();
	calendar.setTime(endDate);
	calendar.add(Calendar.MINUTE, -5);
	beginDate = calendar.getTime();
    }

    /**
     * Construire des évènements et les retourner
     * 
     * @return Liste d'évènements construits
     */
    protected List<RMSystemEvent> buildSystemEventsLogList() {

	List<RMSystemEvent> events = new ArrayList<RMSystemEvent>();
	RMSystemEvent evtLog = ToolkitFactory.getInstance()
		.createRMSystemEvent();
	evtLog.setUsername("_ADMIN");
	evtLog.setEventDescription("EVENT1");
	events.add(evtLog);

	evtLog = ToolkitFactory.getInstance().createRMSystemEvent();
	evtLog.setUsername("_ADMIN");
	evtLog.setEventDescription("EVENT2");
	events.add(evtLog);

	return events;
    }

    /**
     * Construire des évènements et les retourner
     * 
     * @return Liste d'évènements construits
     */
    protected RMDocEvent buildDocEventLog() {

	RMDocEvent evtLog = ToolkitFactory.getInstance().createRMDocEvent();
	evtLog.setDocUUID(KEY_DOC);
	evtLog.setUsername("_ADMIN");
	evtLog.setEventType(DocEventLogType.CREATE_DOCUMENT);
	return evtLog;
    }

    /**
     * Stocker un document dans une base donnée
     * 
     * @param baseId
     * @param filename
     * @return
     * @throws IOException
     */
    protected Document storeDocument(String baseId, String filename)
	    throws IOException, TagControlException {

	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.setCreationDate(Calendar.getInstance().getTime());
	document.addCriterion(base.getBaseCategory(catNames[0]), "FileRef");

	File file = TestUtils.getFile(filename);
	Document stored = serviceProvider.getStoreService().storeDocument(
		document, FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()),
		new FileInputStream(file));

	return stored;
    }

    /**
     * Afficher un libellé et sa valeur
     * 
     * @param name
     *            libellé
     * @param value
     *            valeur
     */
    protected void format(String name, String value) {
	System.out.format("%s ==> %s \n", name, value != null ? value : "");
    }

}
