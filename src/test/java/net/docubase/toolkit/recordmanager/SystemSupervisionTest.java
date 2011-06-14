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
     * Ce teste permet de v�rifier l'enregistrement d'un seul �v�nement comme
     * d�crit ci-dessous :
     * 
     * <ol>
     * <li>On enregistre un �v�nement.</li>
     * <li>On retourne une liste d'�v�nements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On cherche dans cette liste, l'�v�nement enregistr� et on atteste
     * qu'il est bien enregistr�</li>
     * <li>Pour finir, on supprime tous les �v�nements entre une date de debut
     * et une date de fin.</li>
     * </ol>
     */
    @Test
    public void testAddEventLog() {
	try {
	    // Nettoyage des �v�nement
	    cleanEventsLog();

	    // Retourner les �v�nements
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogList(buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'�v�nement de la suppression",
		    1, events.size());

	    // Enregistrer un �v�nement
	    ServiceProvider.getRecordManagerService().addEventLog(
		    buildEventLogList().get(0));

	    // Retourner les �v�nements
	    events = ServiceProvider.getRecordManagerService().getEventLogList(
		    buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'�v�nement de la suppression et d'ajout",
		    2, events.size());

	    // Trouver l'�v�nement enregistr� dans la liste retourner
	    boolean found = false;
	    for (RMClientEvent evt : events) {
		if (CONTEXT_NAME.equals(evt.getContextName()))
		    found = true;
	    }

	    // Attester que l'�v�nement est bien enregistr�
	    assertTrue("L'�v�nement n'est pas enregistr�", found);

	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(e.getMessage(), false);
	} finally {
	    // Nettoyer les �v�nements enregistr�
	    cleanEventsLog();
	}
    }

    /**
     * Ce teste permet de v�rifier l'enregistrement en batch de plusieurs
     * �v�nements comme d�crit ci-dessous :
     * 
     * <ol>
     * <li>On enregistre une liste d'�v�nements.</li>
     * <li>On retourne une liste d'�v�nements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On cherche dans cette liste, les �v�nements enregistr�s et on atteste
     * qu'ils sont bien enregistr�s</li>
     * <li>Pour finir, on supprime tous les �v�nements entre une date de debut
     * et une date de fin.</li>
     * </ol>
     */
    @Test
    public void testAddEventLogList() {
	try {

	    // Nettoyage des �v�nement
	    cleanEventsLog();

	    // Retourner les �v�nements
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogList(buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'�v�nement de la suppression",
		    1, events.size());

	    // Enregistrer une liste d'�v�nements
	    ServiceProvider.getRecordManagerService().addEventsLog(
		    buildEventLogList());

	    // Retourner retourner une liste d'�v�nements, filtr�e sur une date
	    // de debut et une date de fin
	    events = ServiceProvider.getRecordManagerService().getEventLogList(
		    buildFilter());
	    assertEquals(
		    "Pas normal, on devait avoir juste l'�v�nement de la suppression et des 2 ajouts",
		    3, events.size());

	    // V�rifier que les �v�nements enregistr�s sont bin dans la liste
	    boolean[] found = new boolean[] { false, false };
	    for (RMClientEvent evt : events) {
		if (CONTEXT_NAME.equals(evt.getContextName())
			&& "Stockage".equals(evt.getEventTypeName()))
		    found[0] = true;
		if (CONTEXT_NAME.equals(evt.getContextName())
			&& "Consultation".equals(evt.getEventTypeName()))
		    found[1] = true;
	    }

	    // Attester par les assertions que les �v�nements sont trouv�s
	    assertTrue(
		    "L'�v�nement de context TEST et de Type Stockage n'est pas enregistr�",
		    found[0]);
	    assertTrue(
		    "L'�v�nement de context TEST et de Type Consultation n'est pas enregistr�",
		    found[1]);

	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(e.getMessage(), false);
	} finally {
	    // Nettoyage des �v�nement
	    cleanEventsLog();
	}
    }

    /**
     * Ce teste permet de v�rifier la recherche d'�v�nements comme d�crit
     * ci-dessous :
     * 
     * <ol>
     * <li>On cherche une liste d'�v�nements, en filtrant sur une date de debut
     * et une date de fin</li>
     * <li>On atteste que la liste remont�e est non vide</li>
     * <li>On affiche les �v�nements trouv�s</li>
     * </ol>
     */
    @Test
    public void testGetEventLogList() {
	try {
	    // Rechercher les �v�nements, en filtrant sur une date de debut et
	    // une date fin
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogList(buildFilter());

	    // Attester que cette liste est non vide
	    assertTrue("La liste des �v�nements ne doit pas �tre vide",
		    !events.isEmpty());

	    // Afficher la liste d'�v�nement trouv�s
	    for (RMClientEvent event : events) {
		print(event);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Ce teste permet de v�rifier la pagination apr�s la recherche
     * d'�v�nements, comme d�crit ci-dessous :
     * 
     * <ol>
     * <li>On cr�e un filtre avec une date de debut, une date de fin et un
     * nombre d'items � retourner</li>
     * <li>On remonte la pr�mi�re page d'�v�nements � partir du filtre cr��, et
     * on recup�re la date d'�v�nement</li>
     * du dernier �l�ment de la liste de la page.</li>
     * <li>On remplace par cette date, la date de debut de la recherche dans le
     * filtre (EventStartDate).</li>
     * <li>On remonte alors la seconde page.</li>
     * </ol>
     * 
     * NB: Ainsi, on peut remonter toutes les pages en rep�tant la m�me
     * demarche.
     */
    @Test
    public void testPaginationGetEventLogList() {
	try {
	    PAGE = 1;
	    // Cr�er un filtre avec une date de debut, une date de fin et on un
	    // nombre d'items � retourner
	    EventReadFilter readFilter = buildFilter();
	    readFilter.setNbItems(5);

	    // Remonter la 1ere page et on recup�re la date d'�v�nement du
	    // dernier �l�ment de la liste de la page.
	    Date lastEventLog = findEventLog(readFilter);

	    // Modifier la date de debut du filtre par la date recup�r�e
	    readFilter.setEventStartDate(lastEventLog);
	    // Remonter la 2eme page et on recup�re la date d'�v�nement du
	    // dernier �l�ment de la liste de la page.
	    lastEventLog = findEventLog(readFilter);

	} catch (Exception e) {
	    assertTrue(false);
	    e.printStackTrace();
	}
    }

    /**
     * Ce teste permet d'exporter dans un fichier csv, une liste d'�v�nements,
     * comme d�crit ci-dessous :
     * 
     * <ol>
     * <li>On cr�e un filtre avec une date de debut et une date de fin</li>
     * <li>On remonte les �v�nements sur la base du cet filtre</li>
     * <li>On exporte les �v�nements sur la base du m�me filtre.</li>
     * <li>On lit le fichier export en comptant les lignes.</li>
     * <li>On comparer le nombre de lignes export�es et le nombre de lignes
     * remont�es</li>
     * </ol>
     */
    @Test
    public void testExportEventLog() {
	File tmpFile = null;
	BufferedOutputStream bufferedOutputStream = null;
	try {

	    // Construire un filtre bas� sur une date de debut et une date de
	    // fin
	    EventReadFilter readFilter = buildFilter();

	    // Remonter les �v�nements sur la base du m�me filtre
	    List<RMClientEvent> eventLogList = ServiceProvider
		    .getRecordManagerService().getEventLogList(readFilter);

	    // Cr�er un fichier temporaire csv
	    tmpFile = File.createTempFile("eventslog", ".csv");
	    FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
	    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
	    BufferedReader bufferedReader = new BufferedReader(new FileReader(
		    tmpFile));

	    // Exporter les �v�nements sur la base du m�me filtre, dana le
	    // fichier temporaire
	    ServiceProvider.getRecordManagerService().exportLogsList(
		    readFilter, bufferedOutputStream, "csv");

	    // Lire le fichier export� et compter le nombre de ligne
	    int nbLine = 0;
	    while (bufferedReader.readLine() != null) {
		nbLine++;
	    }

	    // Comparer le nombre de lignes export�es et le nombre de lignes
	    // remont�es
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
     * Ce teste permet de v�rifier la tra�abilit� d'�v�nements li�s � un
     * document :
     * 
     * <ol>
     * <li>On cr�e un document dans la base GED et on recup�re son identifiant
     * UUID</li>
     * <li>On recherche les �v�nements li�s � cet document, � partir de son
     * identifiant UUID.</li>
     * <li>On atteste qu'il y a au moins un �v�nement li� � ce document.</li>
     * </ol>
     */
    @Test
    public void testStoreDocAndGetEventsByKeyDoc() {

	Document docPdf = null;

	try {
	    // cr�er un document dans la base GED et on recup�re son identifiant
	    // UUID
	    docPdf = insertDocument(DOC, base);
	    UUID keyDoc = docPdf.getUUID();

	    // Rechercher les �v�nements li�s � cet document, � partir de son
	    // identifiant UUID
	    List<RMClientEvent> events = ServiceProvider
		    .getRecordManagerService().getEventLogListByKeyDoc(
			    keyDoc.toString(), 100);

	    // V�rifier qu'il existe des �v�nements qui sont li�s � cet document
	    assertNotNull("Aucun evenement trouv�", events);
	    assertTrue("La liste doit contenir au moins un �v�nement",
		    events.size() > 0);

	    // Afficher la liste d'�v�nements
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
     * Ce teste permet de v�rifier la suppression d'�v�nements filtr�s par dates
     * :
     * 
     * <ol>
     * <li>On cr�e un filtre avec une date de debut et une date de fin</li>
     * <li>On supprime les �v�nements sur la base de ce filtre.</li>
     * <li>On recherche les �v�nements sur la base de ce m�me filtre.</li>
     * <li>On atteste que tous les �v�nements sont supprim�s � part celui de la
     * suppression.</li>
     * </ol>
     */
    @Test
    public void testDeleteEventLogList() {

	try {
	    // Construire un filtre avec une date de debut et une date de fin
	    EventReadFilter filter = buildFilter();

	    // Supprimer les �v�nements sur la base de ce filtre.
	    ServiceProvider.getRecordManagerService().deleteEventsLog(filter);

	    // Rechercher les �v�nements sur la base de ce m�me filtre.
	    List<RMClientEvent> result = ServiceProvider
		    .getRecordManagerService().getEventLogList(filter);

	    // Attester que Le nombre d'�v�nement trouv� est bien 1, c-a-d celui
	    // de l'�v�nement de la suppression
	    assertTrue("La liste des �v�nements ne doit pas �tre vide",
		    result.size() == 1);
	} catch (Exception e) {
	    assertTrue(false);
	    e.printStackTrace();
	}
    }

    /**
     * Retourner la date d'�v�nement du dernier de la liste d'�v�nements,
     * recherch� sur la base d'un filtre donn�
     * 
     * @param filter
     *            de recherche d'�v�nements
     * @throws Exception
     *             en cas d'erreur
     */
    private Date findEventLog(EventReadFilter filter) throws Exception {

	final ArrayDeque<RMClientEvent> events = new ArrayDeque<RMClientEvent>();
	List<RMClientEvent> result = ServiceProvider.getRecordManagerService()
		.getEventLogList(filter);

	if (!result.isEmpty()) {
	    events.addAll(result);
	    System.out.format("\n*** PAGE N� %d \n", PAGE++);
	}
	assertTrue("La liste des �v�nements ne doit pas �tre vide",
		events.size() > 0);

	for (RMClientEvent event : events) {
	    print(event);
	}

	return result.isEmpty() ? null : ServiceProvider
		.getRecordManagerService().peekLast(events).getEventDate();
    }

    /**
     * Nettoyage des �v�nements
     */
    private void cleanEventsLog() {
	ServiceProvider.getRecordManagerService()
		.deleteEventsLog(buildFilter());
    }

    /**
     * Construire des �v�nements et les retourner
     * 
     * @return Liste d'�v�nements construits
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
     * Afficher les donn�es d'un �v�nement
     * 
     * @param event
     *            �v�nement � afficher
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
     * Afficher un libell� et sa valeur
     * 
     * @param name
     *            libell�
     * @param value
     *            valeur
     */
    private void format(String name, String value) {
	System.out.format("%s ==> %s \n", name, value != null ? value : "");
    }
}
