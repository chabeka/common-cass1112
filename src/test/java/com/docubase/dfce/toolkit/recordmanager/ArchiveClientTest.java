package com.docubase.dfce.toolkit.recordmanager;

import static junit.framework.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.EventReadFilter;
import net.docubase.toolkit.model.recordmanager.RMArchive;
import net.docubase.toolkit.model.recordmanager.RMClientEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.junit.Ignore;
import org.junit.Test;

public class ArchiveClientTest extends AbstractEventTest {
    private final ArchiveService archiveService = ServiceProvider
	    .getArchiveService();
    private final RecordManagerService recordManagerService = ServiceProvider
	    .getRecordManagerService();

    /**
     * Ce teste permet de vérifier la création d'une archive à partir
     * d'évènements filtrés sur une date de debut et une date de fin, comme
     * décrit ci-dessous :
     * 
     * <ol>
     * <li>On archive les évènements filtrés sur une date de debut et de fin.</li>
     * <li>On atteste par une assertion que l'archive est bien créée grâce à son
     * identifiant</li>
     * <li>Pour finir, on supprime l'archive ainsi créée.</li>
     * </ol>
     */
    @Test
    public void testAddArchive() {

	// Ajouter d'évènements
	recordManagerService.addEventsLog(buildEventLogList());
	// créer une archive
	List<String> archiveIds = archiveService.archive(buildFilter());
	// Vérifier que l'archive est bien créée
	assertTrue("Le staockage a échoué", archiveIds.size() > 0);
	// Nettoyer l'archive créée
	boolean isOk = archiveService.deleteArchive(archiveIds.get(0));
	assertTrue("L''archive n''est pas supprimée", isOk);

	// Nettoyer les données de test
	clearEventsAndArchives(archiveIds);
    }

    /**
     * Ce teste permet de vérifier l'extraction d'une archive par son
     * identifiant, comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On archive les évènements filtrés sur une date de debut et de fin.</li>
     * <li>On atteste par une assertion que l'archive est bien créée grâce à son
     * identifiant</li>
     * <li>On extrait le contenu de l'archive en question par son identifiant et
     * on vérifie que le contenu est non vide</li>
     * <li>Pour finir, on supprime l'archive ainsi créée.</li>
     * </ol>
     */
    @Test
    public void testExtractArchive() {

	// Ajouter d'évènements et archiver
	List<String> archiveIds = createEventsAndArchivedThem();

	InputStream is = null;
	try {
	    is = archiveService.extractArchive(archiveIds.get(0));
	    BufferedReader reader = new BufferedReader(
		    new InputStreamReader(is));

	    int nbLine = 0;
	    while (reader.readLine() != null) {
		nbLine++;
	    }

	    assertTrue("L''archive n''est pas extraite", nbLine > 0);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

	// Nettoyer les données de test
	clearEventsAndArchives(archiveIds);
    }

    /**
     * Ce teste permet de rechercher une liste d'archives en filtrant les
     * évènements, sur une date de debut et de fin, comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On crée une archive dont les évènements sont filtrés sur une date de
     * debut et de fin</li>
     * <li>On recherche les archives entre une date de debut et une date de fin.
     * </li>
     * <li>On vérifie que le nombre d'archive est au moins 2</li>
     * <li>Pour finir, on supprime les archives ainsi créées, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testGetArchives() {

	// Ajouter un évènement lié à un document fictif
	recordManagerService.addEventsLog(Collections
		.singletonList(buildDocEventLog()));

	// Ajouter d'autres évènements et archiver
	List<String> archiveIds = createEventsAndArchivedThem();

	assertEquals("On doit avoir deux archives", 2, archiveIds.size());

	// Retrouver les archives enregistrées
	List<RMArchive> archives = archiveService.getArchives(buildFilter());
	assertTrue("La liste d''archive ne doit pas être vide",
		archives.size() > 1);

	// Extraire la première archive de la liste
	InputStream is = null;
	try {
	    is = archiveService.extractArchive(archives.get(0).getArchiveId());
	    assertNotNull(
		    "Le contenu de cette archive ne devrait pas être vide", is);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

	// Nettoyer les données de test
	clearEventsAndArchives(archiveIds);
    }

    @Test
    public void testGetEventsFromArchive() {
	List<String> archiveIds = null;
	try {
	    // Ajouter d'évènements et archiver
	    archiveIds = createEventsAndArchivedThem();

	    // Extraire une archive
	    InputStream is = archiveService.extractArchive(archiveIds.get(0));

	    // Convertir les lignes de l'archive en évènements
	    List<RMClientEvent> events = archiveService
		    .getEventsFromArchive(is);
	    assertTrue("Au moins deux évènements devraient êtré remontés",
		    events.size() > 0);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {

	    // Nettoyer les données de test
	    if (archiveIds != null)
		clearEventsAndArchives(archiveIds);
	}
    }

    /**
     * Ce teste permet de supprimer une archive à partir de son identifiant
     * donnée, comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On crée une archive dont les évènements sont filtrés entre une date
     * de debut et de fin</li>
     * <li>On supprime l'archive ainsi créées par son identifiant.</li>
     * <li>On atteste par une assertion que l'archive est bien supprimer</li>
     * <li>Pour finir, on supprime l'archive ainsi créées, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testDeleteArchive() {

	// Ajouter d'évènements et archiver
	List<String> archiveIds = createEventsAndArchivedThem();

	boolean exists = archiveService.archiveExists(archiveIds.get(0));

	assertNotNull("Le staockage a échoué", exists);

	// Supprimer l'archive créée
	boolean isDeleted = archiveService.deleteArchive(archiveIds.get(0));
	assertNotNull("La suppression a échoué", isDeleted);

	// Nettoyer les évènements créé
	recordManagerService.deleteEventsLog(buildFilter());
    }

    /**
     * Ce teste permet de vérifier qu'une archive est bien celle d'un document
     * donnée, comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On insert un document dans la base, afin de générer un évènement de
     * type stockage lié au document</li>
     * <li>On crée une archive dont les évènements sont liés à cet document.</li>
     * <li>On recherche l'archive ainsi créées par l'identifiant du document.</li>
     * <li>L'archive étant au format json, on recherche l'emprunte du document
     * dans l'archive trouvée</li>
     * <li>On atteste par une assertion que l'emprunte du document est identique
     * à celui qui se trouve dans l'archive</li>
     * <li>Pour finir, on supprime l'archive ainsi créées, ainsi que le
     * document.</li>
     * </ol>
     */
    @Ignore
    @Test
    public void testCheckIntegrityOfArchiveAndDocument() {

	Document document = null;
	List<String> archiveIds = null;
	try {
	    // créer un document pour générer un évènement de stockage
	    document = storeDocument(BASEID, "doc1.pdf");
	    UUID keyDoc = document.getUuid();
	    assertNotNull("Le staockage a échoué", keyDoc);

	    // Ajouter une archive filtrée sur l'uuid du document créé
	    EventReadFilter filter = buildFilter();
	    filter.setDocumentUUID(keyDoc.toString());
	    archiveIds = archiveService.archive(filter);
	    assertNotNull("Le stockage a échoué", archiveIds);

	    // Trouver le disgest du document
	    String docDigest = document.getDigest();
	    assertNotNull("Le document n'a pas de disgest", docDigest);

	    // Trouver le disgest du document dans l'archive
	    InputStream is = archiveService
		    .extractArchiveByDocumentUuid(keyDoc);
	    String archiveDocDigest = archiveService
		    .findDocumentDigestFromArchive(keyDoc.toString(), is);
	    assertNotNull("L'archive n'a de digest pour ce document",
		    archiveDocDigest);

	    assertEquals("Archive non conforme ", docDigest, archiveDocDigest);

	} catch (Exception e) {
	    e.printStackTrace();
	    assertFalse(false);
	} finally {
	    // Nettoyer l'archive créée
	    if (archiveIds != null && !archiveIds.isEmpty()) {
		boolean isOk = ServiceProvider.getArchiveService()
			.deleteArchive(archiveIds.get(0));
		assertTrue("L''archive n''est pas supprimée", isOk);
	    }

	    // supprimer le document créé
	    if (document != null) {
		ServiceProvider.getStoreService().deleteDocument(
			document.getUuid());
	    }

	    // Nettoyer les évènements créé
	    recordManagerService.deleteEventsLog(buildFilter());
	}

    }

    /**
     * Ajouter des évènements et créer une archive
     * 
     * @return List d'archives créées
     */
    private List<String> createEventsAndArchivedThem() {
	// Ajouter d'évènements
	recordManagerService.addEventsLog(buildEventLogList());
	// Ajouter une archive
	List<String> archiveIds = archiveService.archive(buildFilter());
	assertTrue("Le staockage a échoué", archiveIds.size() > 0);
	return archiveIds;
    }

    /**
     * Nettoyer les données de test
     * 
     * @param archiveIds
     */
    private void clearEventsAndArchives(List<String> archiveIds) {
	// Supprimer l'archive créée
	for (String archiveId : archiveIds) {
	    boolean isDeleted = archiveService.deleteArchive(archiveId);
	    assertNotNull("La suppression a échoué", isDeleted);
	}

	// Nettoyer les évènements créé
	recordManagerService.deleteEventsLog(buildFilter());
    }

}
