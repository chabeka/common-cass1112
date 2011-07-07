package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import net.docubase.toolkit.model.recordmanager.EventReadFilter;
import net.docubase.toolkit.model.recordmanager.RMClientEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecordManagerService;

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
	recordManagerService.addEventLog(buildEventLogList().get(0));

	// retourner la liste d'évènements filtrés
	List<RMClientEvent> events = recordManagerService
		.getEventLogList(buildFilter());

	// Trouver l'évènement enregistré dans la liste retournée
	boolean found = false;
	for (RMClientEvent evt : events) {
	    if ("Stockage".equals(evt.getEventTypeName()))
		found = true;
	}

	// Attester que l'évènement est bien enregistré
	assertTrue("L'évènement n'est pas enregistré", found);

	// Nettoyer les évènements
	recordManagerService.deleteEventsLog(buildFilter());
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
	recordManagerService.addEventsLog(buildEventLogList());

	// retourner la liste d'évènements filtrés
	List<RMClientEvent> events = recordManagerService
		.getEventLogList(buildFilter());

	// Trouver l'évènement enregistré dans la liste retournée
	boolean foundA = false;
	boolean foundB = false;
	for (RMClientEvent evt : events) {
	    if ("Stockage".equals(evt.getEventTypeName()))
		foundA = true;

	    if ("Consultation".equals(evt.getEventTypeName()))
		foundB = true;
	}

	// Attester que l'évènement de Stockage est bien enregistré
	assertTrue("L'évènement de Stockage n'est pas enregistré", foundA);

	// Attester que l'évènement de Stockage est bien enregistré
	assertTrue("L'évènement de Consultation n'est pas enregistré", foundB);

	// Nettoyer les évènements
	recordManagerService.deleteEventsLog(buildFilter());
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
	recordManagerService.addEventsLog(buildEventLogList());

	// retourner la liste d'évènements filtrés
	List<RMClientEvent> events = recordManagerService
		.getEventLogList(buildFilter());

	// Attester que cette liste est non vide
	assertTrue("La liste des évènements ne doit pas être vide",
		(!events.isEmpty() && events.size() > 1));

	// Nettoyer les évènements
	recordManagerService.deleteEventsLog(buildFilter());
    }

    /**
     * Ce teste permet de vérifier la pagination après la recherche
     * d'évènements, comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On enregistre une liste d'évènements.</li>
     * <li>On crée un filtre avec une date de debut, une date de fin et un
     * nombre d'items à retourner</li>
     * <li>On remonte la prémière page d'évènements à partir du filtre créé, et
     * on recupère la date d'évènement</li>
     * du dernier élément de la liste de la page.</li>
     * <li>On remplace par cette date, la date de debut de la recherche dans le
     * filtre (EventStartDate).</li>
     * <li>On remonte alors la seconde page.</li>
     * </ol>
     * 
     * NB: Ainsi, on peut remonter toutes les pages en repétant la même
     * demarche.
     */
    @Test
    public void testPagination() {
	PAGE = 1;

	// Ajouter un évènement
	recordManagerService.addEventsLog(buildEventLogList());

	// Créer un filtre avec une date de debut, une date de fin et on un
	// nombre d'items à retourner par page
	EventReadFilter readFilter = buildFilter();
	readFilter.setNbItems(5);

	// Remonter la 1ere page et on recupère la date d'évènement du
	// dernier élément de la liste de la page.
	Date lastEventLog = findEventLog(readFilter);

	// Modifier la date de debut du filtre par la date recupérée
	readFilter.setEventStartDate(lastEventLog);
	// Remonter la 2eme page et on recupère la date d'évènement du
	// dernier élément de la liste de la page.
	lastEventLog = findEventLog(readFilter);

	// Nettoyer les évènements
	recordManagerService.deleteEventsLog(buildFilter());
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
	RMClientEvent event = buildEventLogList().get(0);
	event.setDocumentUuid(KEY_DOC);
	recordManagerService.addEventLog(event);

	List<RMClientEvent> events = ServiceProvider.getRecordManagerService()
		.getEventLogListByKeyDoc(KEY_DOC, 100);

	boolean found = false;
	for (RMClientEvent evt : events) {
	    found = true;
	    if (!KEY_DOC.equals(evt.getDocumentUuid())) {
		found = false;
		break;
	    }
	}

	// Attester que l'évènement est bien enregistré
	assertTrue("L'évènement n'est pas enregistré", found);

	// Nettoyer les évènements
	recordManagerService.deleteEventsLog(buildFilter());
    }

    /**
     * Ce teste permet de vérifier la suppression d'évènements filtrés par dates
     * :
     * 
     * <ol>
     * <li>On enregistre une liste d'évènements.</li>
     * <li>On crée un filtre avec une date de debut et une date de fin</li>
     * <li>On supprime les évènements sur la base de ce filtre.</li>
     * <li>On recherche les évènements sur la base de ce même filtre.</li>
     * <li>On atteste que tous les évènements sont supprimés à part celui de la
     * suppression.</li>
     * </ol>
     */
    @Test
    public void testDeleteEventLog() {

	// Ajouter un évènement
	recordManagerService.addEventsLog(buildEventLogList());

	recordManagerService.deleteEventsLog(buildFilter());

	// Rechercher les évènements sur la base de ce même filtre.
	List<RMClientEvent> events = ServiceProvider.getRecordManagerService()
		.getEventLogList(buildFilter());

	// Attester que Le nombre d'évènement trouvé est bien 1, c-a-d celui
	// de l'évènement de la suppressiona
	assertEquals("La liste des évènements ne doit pas être vide", 2,
		events.size());
    }
}
