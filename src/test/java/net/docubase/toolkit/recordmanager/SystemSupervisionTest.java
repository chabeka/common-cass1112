package net.docubase.toolkit.recordmanager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.EventReadFilter;
import net.docubase.toolkit.model.recordmanager.RMClientEvent;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;

public class SystemSupervisionTest extends AbstractEventTest {
    /** Numero de page */
    private static int PAGE = 1;

    /** Nom du contexte dans le cadre du test */
    private static final String CONTEXT_NAME = "TEST";

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
	try {
	    // Nettoyage des évènement
	    cleanEventsLog();

	    // Retourner les évènements
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogList(buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'évènement de la suppression",
		    1, events.size());

	    // Enregistrer un évènement
	    ServiceProvider.getRecordManagerService().addEventLog(
		    buildEventLogList().get(0));

	    // Retourner les évènements
	    events = ServiceProvider.getRecordManagerService().getEventLogList(
		    buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'évènement de la suppression et d'ajout",
		    2, events.size());

	    // Trouver l'évènement enregistré dans la liste retourner
	    boolean found = false;
	    for (RMClientEvent evt : events) {
		if (CONTEXT_NAME.equals(evt.getContextName()))
		    found = true;
	    }

	    // Attester que l'évènement est bien enregistré
	    assertTrue("L'évènement n'est pas enregistré", found);

	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(e.getMessage(), false);
	} finally {
	    // Nettoyer les évènements enregistré
	    cleanEventsLog();
	}
    }

    /**
     * Ce teste permet de vérifier l'enregistrement en batch de plusieurs
     * évènements comme décrit ci-dessous :
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
	try {

	    // Nettoyage des évènement
	    cleanEventsLog();

	    // Retourner les évènements
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogList(buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'évènement de la suppression",
		    1, events.size());

	    // Enregistrer une liste d'évènements
	    ServiceProvider.getRecordManagerService().addEventsLog(
		    buildEventLogList());

	    // Retourner retourner une liste d'évènements, filtrée sur une date
	    // de debut et une date de fin
	    events = ServiceProvider.getRecordManagerService().getEventLogList(
		    buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'évènement de la suppression et des 2 ajouts",
		    3, events.size());

	    // Vérifier que les évènements enregistrés sont bin dans la liste
	    boolean[] found = new boolean[] { false, false };
	    for (RMClientEvent evt : events) {
		if (CONTEXT_NAME.equals(evt.getContextName())
			&& "Stockage".equals(evt.getEventTypeName()))
		    found[0] = true;
		if (CONTEXT_NAME.equals(evt.getContextName())
			&& "Consultation".equals(evt.getEventTypeName()))
		    found[1] = true;
	    }

	    // Attester par les assertions que les évènements sont trouvés
	    assertTrue(
		    "L'évènement de context TEST et de Type Stockage n'est pas enregistré",
		    found[0]);
	    assertTrue(
		    "L'évènement de context TEST et de Type Consultation n'est pas enregistré",
		    found[1]);

	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(e.getMessage(), false);
	} finally {
	    // Nettoyage des évènement
	    cleanEventsLog();
	}
    }

    /**
     * Ce teste permet de vérifier la recherche d'évènements comme décrit
     * ci-dessous :
     * 
     * <ol>
     * <li>On cherche une liste d'évènements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On atteste que la liste remontée est non vide</li>
     * <li>On affiche les évènements trouvés</li>
     * </ol>
     */
    @Test
    public void testGetEventLogList() {
	try {
	    // Rechercher les évènements, en filtrant sur une date de debut et
	    // une date fin
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogList(buildFilter());

	    // Attester que cette liste est non vide
	    assertTrue("La liste des évènements ne doit pas être vide",
		    !events.isEmpty());

	    // Afficher la liste d'évènement trouvés
	    for (RMClientEvent event : events) {
		print(event);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Ce teste permet de vérifier la pagination après la recherche
     * d'évènements, comme décrit ci-dessous :
     * 
     * <ol>
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
    public void testPaginationGetEventLogList() {
	try {
	    PAGE = 1;
	    // Créer un filtre avec une date de debut, une date de fin et on un
	    // nombre d'items à retourner
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

	} catch (Exception e) {
	    assertTrue(false);
	    e.printStackTrace();
	}
    }

    /**
     * Ce teste permet d'exporter dans un fichier csv, une liste d'évènements,
     * comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On crée un filtre avec une date de debut et une date de fin</li>
     * <li>On remonte les évènements sur la base du cet filtre</li>
     * <li>On exporte les évènements sur la base du même filtre.</li>
     * <li>On lit le fichier export en comptant les lignes.</li>
     * <li>On comparer le nombre de lignes exportées et le nombre de lignes
     * remontées</li>
     * </ol>
     */
    @Test
    public void testExportEventLog() {
	File tmpFile = null;
	BufferedOutputStream bufferedOutputStream = null;
	try {

	    // Construire un filtre basé sur une date de debut et une date de
	    // fin
	    EventReadFilter readFilter = buildFilter();

	    // Remonter les évènements sur la base du même filtre
	    List<RMClientEvent> eventLogList = ServiceProvider
		    .getRecordManagerService().getEventLogList(readFilter);

	    // Créer un fichier temporaire csv
	    tmpFile = File.createTempFile("eventslog", ".csv");
	    FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
	    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
	    BufferedReader bufferedReader = new BufferedReader(new FileReader(
		    tmpFile));

	    // Exporter les évènements sur la base du même filtre, dana le
	    // fichier temporaire
	    ServiceProvider.getRecordManagerService().exportLogsList(
		    readFilter, bufferedOutputStream, "csv");

	    // Lire le fichier exporté et compter le nombre de ligne
	    int nbLine = 0;
	    while (bufferedReader.readLine() != null) {
		nbLine++;
	    }

	    // Comparer le nombre de lignes exportées et le nombre de lignes
	    // remontées
	    assertEquals(eventLogList.size(), nbLine);

	} catch (Exception ex) {
	    assertTrue(false);
	    ex.printStackTrace();
	} finally {
	    if (tmpFile != null) {
		tmpFile.delete();
	    }
	    if (bufferedOutputStream != null) {
		try {
		    bufferedOutputStream.close();
		} catch (IOException e) {
		    // quiet
		}
	    }
	}
    }

    /**
     * Ce teste permet de vérifier la traçabilité d'évènements liés à un
     * document :
     * 
     * <ol>
     * <li>On crée un document dans la base GED et on recupère son identifiant
     * UUID</li>
     * <li>On recherche les évènements liés à cet document, à partir de son
     * identifiant UUID.</li>
     * <li>On atteste qu'il y a au moins un évènement lié à ce document.</li>
     * </ol>
     */
    @Test
    public void testStoreDocAndGetEventsByKeyDoc() {

	Document docPdf = null;

	try {
	    // créer un document dans la base GED et on recupère son identifiant
	    // UUID
	    docPdf = insertDocument(DOC, base);
	    UUID keyDoc = docPdf.getUUID();

	    // Rechercher les évènements liés à cet document, à partir de son
	    // identifiant UUID
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogListByKeyDoc(
			    keyDoc.toString(), 100);

	    // Vérifier qu'il existe des évènements qui sont liés à cet document
	    assertNotNull("Aucun evenement trouvé", events);
	    assertTrue("La liste doit contenir au moins un évènement",
		    events.size() > 0);

	    // Afficher la liste d'évènements
	    for (RMClientEvent event : events) {
		print(event);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(false);
	} finally {
	    // supprimer le document
	    if (docPdf != null) {
		ServiceProvider.getStoreService().deleteDocument(docPdf);
	    }
	}
    }

    /**
     * Ce teste permet de vérifier la suppression d'évènements filtrés par dates
     * :
     * 
     * <ol>
     * <li>On crée un filtre avec une date de debut et une date de fin</li>
     * <li>On supprime les évènements sur la base de ce filtre.</li>
     * <li>On recherche les évènements sur la base de ce même filtre.</li>
     * <li>On atteste que tous les évènements sont supprimés à part celui de la
     * suppression.</li>
     * </ol>
     */
    @Test
    public void testDeleteEventLogList() {

	try {
	    // Construire un filtre avec une date de debut et une date de fin
	    EventReadFilter filter = buildFilter();

	    // Supprimer les évènements sur la base de ce filtre.
	    ServiceProvider.getRecordManagerService().deleteEventsLog(filter);

	    // Rechercher les évènements sur la base de ce même filtre.
	    List<RMClientEvent> result = ServiceProvider
		    .getRecordManagerService().getEventLogList(filter);

	    // Attester que Le nombre d'évènement trouvé est bien 1, c-a-d celui
	    // de l'évènement de la suppression
	    assertTrue("La liste des évènements ne doit pas être vide",
		    result.size() == 1);
	} catch (Exception e) {
	    assertTrue(false);
	    e.printStackTrace();
	}
    }

    /**
     * Retourner la date d'évènement du dernier de la liste d'évènements,
     * recherché sur la base d'un filtre donné
     * 
     * @param filter
     *            de recherche d'évènements
     * @throws Exception
     *             en cas d'erreur
     */
    private Date findEventLog(EventReadFilter filter) throws Exception {

	final ArrayDeque<RMClientEvent> events = new ArrayDeque<RMClientEvent>();
	List<RMClientEvent> result = ServiceProvider.getRecordManagerService()
		.getEventLogList(filter);

	if (!result.isEmpty()) {
	    events.addAll(result);
	    System.out.format("\n*** PAGE N° %d \n", PAGE++);
	}
	assertTrue("La liste des évènements ne doit pas être vide",
		events.size() > 0);

	for (RMClientEvent event : events) {
	    print(event);
	}

	return result.isEmpty() ? null : ServiceProvider
		.getRecordManagerService().peekLast(events).getEventDate();
    }

    /**
     * Nettoyage des évènements
     */
    private void cleanEventsLog() {
	ServiceProvider.getRecordManagerService()
		.deleteEventsLog(buildFilter());
    }

    /**
     * Construire des évènements et les retourner
     * 
     * @return Liste d'évènements construits
     */
    private List<RMClientEvent> buildEventLogList() {

	List<RMClientEvent> events = new ArrayList<RMClientEvent>();
	RMClientEvent evtLog = ToolkitFactory.getInstance()
		.createRMClientEvent();
	evtLog.setActorLogin("_ADMIN");
	evtLog.setContextName(CONTEXT_NAME);
	evtLog.setEventDate(Calendar.getInstance().getTime());
	evtLog.setSystemDate(Calendar.getInstance().getTime());
	evtLog.setEventTypeName("Stockage");
	evtLog.setObjectId("0@JUNIT.1");
	evtLog.setObjectType("TESTSTORE");
	events.add(evtLog);

	evtLog = ToolkitFactory.getInstance().createRMClientEvent();
	evtLog.setActorLogin("_ADMIN");
	evtLog.setContextName(CONTEXT_NAME);
	evtLog.setEventDate(Calendar.getInstance().getTime());
	evtLog.setSystemDate(Calendar.getInstance().getTime());
	evtLog.setEventTypeName("Consultation");
	evtLog.setObjectId("0@JUNIT.1");
	evtLog.setObjectType("TESTSTORE");
	events.add(evtLog);

	return events;
    }

    /**
     * Afficher les données d'un évènement
     * 
     * @param event
     *            évènement à afficher
     */
    private void print(RMClientEvent event) {
	System.out.println("\n");
	format("EventDate", event.getEventDateFormatUTC());
	format("ActorLogin", event.getActorLogin());
	format("EventTypeName", event.getEventTypeName());
	format("ContextName", event.getContextName());
	format("ObjectId", event.getObjectId());
	format("ObjectType", event.getObjectType());
	format("SystemDate", event.getSystemDateFormatUTC());
	format("Arg1", event.getArg1());
	format("Arg2", event.getArg2());
	format("Digest", event.getDigest());
	format("DigestAlgorithm", event.getDigestAlgorithm());
	format("DocumentUuid", event.getDocumentUuid());
	format("ArchiveUuid", event.getArchiveId());
    }

    /**
     * Afficher un libellé et sa valeur
     * 
     * @param name
     *            libellé
     * @param value
     *            valeur
     */
    private void format(String name, String value) {
	System.out.format("%s ==> %s \n", name, value != null ? value : "");
    }
}
