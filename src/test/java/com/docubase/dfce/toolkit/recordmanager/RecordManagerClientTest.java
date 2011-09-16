package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.joda.time.DateTime;
import org.junit.Test;

public class RecordManagerClientTest extends AbstractEventTest {

    private RecordManagerService recordManagerService = ServiceProvider
	    .getRecordManagerService();

    /**
     * Ce teste permet de vérifier l'enregistrement d'un seul évènement comme
     * décrit ci-dessous :
     * 
     * <ol>
     * <li>On enregistre un évènement.</li>
     * <li>On retourne une liste d'évènements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On cherche dans cette liste, l'évènement enregistré et on atteste
     * qu'il est bien enregistré</li>
     * <li>Pour finir, on supprime tous les évènements entre une date de debut
     * et une date de fin.</li>
     * </ol>
     */
    @Test
    public void testAddEventLog() {
	// Ajouter un évènement

	RMSystemEvent createdEvent = recordManagerService
		.createCustomSystemEventLog((buildSystemEventsLogList().get(0)));

	// retourner la liste d'évènements filtrés
	List<RMSystemEvent> events = recordManagerService
		.getSystemEventLogsByDates(
			new DateTime(createdEvent.getEventDate())
				.minusMillis(1).toDate(), new DateTime(
				createdEvent.getEventDate()).plusMillis(1)
				.toDate());

	// Trouver l'évènement enregistré dans la liste retournée
	boolean found = false;
	for (RMSystemEvent evt : events) {
	    System.out.println("evt.getKey() = " + evt.getKey());
	    if ("EVENT1".equals(evt.getEventDescription())) {
		found = true;
	    }
	}

	// Attester que l'évènement est bien enregistré
	assertTrue("L'évènement n'est pas enregistré", found);
    }

    /**
     * Ce teste permet de vérifier l'enregistrement de plusieurs évènements
     * comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On enregistre une liste d'évènements.</li>
     * <li>On retourne une liste d'évènements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On cherche dans cette liste, les évènements enregistrés et on atteste
     * qu'ils sont bien enregistrés</li>
     * <li>Pour finir, on supprime tous les évènements entre une date de debut
     * et une date de fin.</li>
     * </ol>
     */
    @Test
    public void testAddEventLogList() {
	// Ajouter un évènement
	List<RMSystemEvent> systemEventsLogList = buildSystemEventsLogList();
	assertEquals(2, systemEventsLogList.size());

	RMSystemEvent createdEvent1 = recordManagerService
		.createCustomSystemEventLog(systemEventsLogList.get(0));
	RMSystemEvent createdEvent2 = recordManagerService
		.createCustomSystemEventLog(systemEventsLogList.get(1));

	// retourner la liste d'évènements
	List<RMSystemEvent> events = recordManagerService
		.getSystemEventLogsByDates(
			new DateTime(createdEvent1.getEventDate()).minusMillis(
				1).toDate(),
			new DateTime(createdEvent2.getEventDate())
				.plusMillis(1).toDate());

	// Trouver l'évènement enregistré dans la liste retournée
	boolean foundA = false;
	boolean foundB = false;
	for (RMSystemEvent evt : events) {
	    if ("EVENT1".equals(evt.getEventDescription()))
		foundA = true;

	    if ("EVENT2".equals(evt.getEventDescription()))
		foundB = true;
	}

	// Attester que l'évènement de Stockage est bien enregistré
	assertTrue("L'évènement de EVENT1 n'est pas enregistré", foundA);

	// Attester que l'évènement de Stockage est bien enregistré
	assertTrue("L'évènement de EVENT2 n'est pas enregistré", foundB);
    }

    /**
     * Ce teste permet de vérifier la recherche d'évènements comme décrit
     * ci-dessous :
     * 
     * <ol>
     * <li>On enregistre une liste de 2 évènements.</li>
     * <li>On cherche une liste d'évènements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On atteste que la liste remontée est non vide</li>
     * <li>On affiche les évènements trouvés</li>
     * </ol>
     */
    @Test
    public void testGetEventLogList() {

	// Ajouter un évènement
	List<RMSystemEvent> systemEventsLogList = buildSystemEventsLogList();
	Date maxEventDate = new Date(0);
	for (RMSystemEvent rmSystemEvent : systemEventsLogList) {
	    RMSystemEvent createdEvent = recordManagerService
		    .createCustomSystemEventLog(rmSystemEvent);
	    if (maxEventDate.before(createdEvent.getEventDate())) {
		maxEventDate = createdEvent.getEventDate();
	    }
	}
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(maxEventDate);
	calendar.add(Calendar.MILLISECOND, -1);
	beginDate = calendar.getTime();
	calendar.add(Calendar.MILLISECOND, 2);
	endDate = calendar.getTime();

	// retourner la liste d'évènements filtrés
	List<RMSystemEvent> events = recordManagerService
		.getSystemEventLogsByDates(beginDate, endDate);

	// Attester que cette liste est non vide
	assertTrue("La liste des évènements ne doit pas être vide",
		(!events.isEmpty() && events.size() > 1));
    }

    /**
     * Ce teste permet de vérifier la traçabilité d'évènements liés à un
     * document :
     * 
     * <ol>
     * <li>On crée un document dans la base GED, mais nous admettons que le
     * document existe</li>
     * <li>On enregistre évènement pour ce document.</li>
     * <li>On recherche les évènements liés à cet document, à partir de son
     * identifiant UUID.</li>
     * <li>On atteste que tous les évènements sont liés à ce document.</li>
     * </ol>
     */
    @Test
    public void testGetEventLogsByKeyDoc() {
	// Ajouter un évènement de stockage de document
	RMDocEvent docEventLog = buildDocEventLog();

	recordManagerService.createCustomDocumentEventLog(docEventLog);

	List<RMDocEvent> events = ServiceProvider.getRecordManagerService()
		.getDocumentEventLogsByUUID(KEY_DOC);

	boolean found = false;
	for (RMDocEvent evt : events) {
	    found = true;
	    if (!KEY_DOC.toString().equals(evt.getDocUUID().toString())) {
		found = false;
		break;
	    }
	}

	// Attester que l'évènement est bien enregistré
	assertTrue("L'évènement n'est pas enregistré", found);
    }
}
