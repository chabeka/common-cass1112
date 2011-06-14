package net.docubase.toolkit.recordmanager;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import net.docubase.am.common.io.util.DataContainer;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.EventReadFilter;
import net.docubase.toolkit.service.ServiceProvider;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class ArchivageTest extends AbstractEventTest {
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

	String archiveId = null;
	try {

	    // Créer une archive
	    archiveId = ServiceProvider.getArchiveService().archive(
		    buildFilter());
	    // Vérifier que l'archive est bien créée
	    assertNotNull("Le staockage a échoué", archiveId);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer l'archive créée
	    boolean isOk = ServiceProvider.getArchiveService().deleteArchive(
		    archiveId);
	    assertTrue("L''archive n''est pas supprimée", isOk);
	}

    }

    /**
     * Ce teste permet de rechercher une archive par son identifiant, comme
     * décrit ci-dessous :
     * 
     * <ol>
     * <li>On archive les évènements filtrés sur une date de debut et de fin.</li>
     * <li>On atteste par une assertion que l'archive est bien créée grâce à son
     * identifiant</li>
     * <li>On recherche l'archive en question par son identifiant et on atteste
     * que l'archive existe</li>
     * <li>Pour finir, on supprime l'archive ainsi créée.</li>
     * </ol>
     */
    @Test
    public void testGetArchive() {

	String archiveId = null;
	try {
	    // Créer une archive
	    archiveId = ServiceProvider.getArchiveService().archive(
		    buildFilter());
	    // Vérifier que l'archive est bien créée
	    assertNotNull("Le staockage a échoué", archiveId);

	    // Remonter l'archive créée
	    boolean archiveExists = ServiceProvider.getArchiveService()
		    .archiveExists(archiveId);
	    // Vérifier que l'archive existe
	    assertTrue("Aucune archive n''est trouvée", archiveExists);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer l'archive créée

	    boolean isOk = ServiceProvider.getArchiveService().deleteArchive(
		    archiveId);
	    assertTrue("L''archive n''est pas supprimée", isOk);
	}
    }

    /**
     * Ce teste permet de rechercher une liste d'archives en filtrant les
     * évènements, sur une date de debut et de fin, comme décrit ci-dessous :
     * 
     * <ol>
     * <li>On insert un document dans la base, afin de générer un évènement de
     * type stockage lié au document</li>
     * <li>On crée une première archive dont les évènements sont liés à cet
     * document.</li>
     * <li>On crée une seconde archive dont les évènements sont filtrés sur une
     * date de debut et de fin</li>
     * <li>On recherche les archive ainsi créées entre une date de debut et une
     * date de fin.</li>
     * <li>On atteste par une assertion que le nombre d'archives est bien 2</li>
     * <li>Pour finir, on supprime les archives ainsi créées, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testGetArchives() {

	Document document = null;
	List<String> archivesIds = null;
	try {

	    // créer un document pour générer un évènement de stockage
	    document = insertDocument(DOC, base);
	    UUID keyDoc = document.getUUID();
	    assertNotNull("Le staockage du document a échoué", keyDoc);

	    // Créer une 1ere archive par rapport à l'uuid du document créé
	    EventReadFilter filter = buildFilter();
	    filter.setDocumentUUID(keyDoc);
	    String archiveId = ServiceProvider.getArchiveService().archive(
		    filter);
	    assertNotNull("Le staockage a échoué", archiveId);

	    // Créer une 2eme archive par rapport à une date de debut et de fin
	    // des évènements
	    filter = buildFilter();
	    archiveId = ServiceProvider.getArchiveService().archive(filter);
	    assertNotNull("L'archivage a échoué", archiveId);

	    // Vérifier que le nombre d'archives ainsi créées est bien 2
	    archivesIds = ServiceProvider.getArchiveService().getArchivesIds(
		    buildFilter());
	    assertNotNull("les archives ne peuvent pas être nul", archivesIds);
	    assertEquals("Nombre d'archives trouvée n'est pas conforme ", 2,
		    archivesIds.size());

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer les archives créées
	    for (String archiveId : archivesIds) {
		boolean isOk = ServiceProvider.getArchiveService()
			.deleteArchive(archiveId);
		assertTrue("L''archive n''est pas supprimée", isOk);
	    }
	    // supprimer le document créé
	    if (document != null)
		ServiceProvider.getStoreService().deleteDocument(document);
	}
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
     * <li>L'archive étant au format json, onrecherche l'identifiant du document
     * dans l'archive trouvée</li>
     * <li>On atteste par une assertion que l'archive est bien celle du document
     * </li>
     * <li>Pour finir, on supprime l'archive ainsi créées, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testCheckIntegrityOfArchiveAndDocument() {

	Document document = null;
	String archiveId = null;
	try {
	    // créer un document pour générer un évènement de stockage
	    document = insertDocument(DOC, base);
	    UUID keyDoc = document.getUUID();
	    assertNotNull("Le staockage a échoué", keyDoc);

	    // Créer une archive par rapport à l'uuid du document créé
	    EventReadFilter filter = buildFilter();
	    filter.setDocumentUUID(keyDoc);
	    archiveId = ServiceProvider.getArchiveService().archive(filter);
	    assertNotNull("Le staockage a échoué", archiveId);

	    // Rechercher l'archive à partir de l'identifiant du document
	    archiveId = ServiceProvider.getArchiveService()
		    .getArchiveIdByDocumentUuid(keyDoc);
	    assertNotNull(
		    "Impossible de retouner l'archive par l'id du document",
		    archiveId);

	    // Vérifier que l'identifiant du document contenu du fichier
	    // d'archive est bien identique à celui du document d'origine
	    DataContainer archiveFile = ServiceProvider.getArchiveService()
		    .getArchiveFile(archiveId);

	    String documentId = findKeyDocFromFileContent(archiveFile
		    .getInputStream());
	    assertEquals("Archive non conforme ", keyDoc.toString(), documentId);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer l'archive créée
	    if (archiveId != null) {
		boolean isOk = ServiceProvider.getArchiveService()
			.deleteArchive(archiveId);
		assertTrue("L''archive n''est pas supprimée", isOk);
	    }

	    // supprimer le document créé
	    if (document != null)
		ServiceProvider.getStoreService().deleteDocument(document);
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
     * <li>On atteste par une assertion que l'archive est bien supprimer, grâce
     * au code retour isOk</li>
     * <li>Pour finir, on supprime l'archive ainsi créées, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testDeleteArchive() {
	String archiveId = null;
	try {
	    // Créer une archive
	    archiveId = ServiceProvider.getArchiveService().archive(
		    buildFilter());
	    assertNotNull("Le staockage a échoué", archiveId);

	    boolean isOk = ServiceProvider.getArchiveService().deleteArchive(
		    archiveId);
	    assertTrue("L''archive n''est pas supprimée", isOk);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Trouver l'identifiant d'un document dans le contenu d'un fichier
     * d'archive
     * 
     * @param fStream
     *            flux du contenu d'un fichier d'archive
     * @return Identifiant du document dans le contenu du fichier d'archive
     * @throws IOException
     *             en cas d'erreur
     */
    private String findKeyDocFromFileContent(InputStream fStream)
	    throws IOException {

	String documentId = null;
	DataInputStream dInput = new DataInputStream(fStream);
	while (dInput.available() != 0) {
	    String in = dInput.readLine();
	    JSONObject json = null;
	    try {
		json = (JSONObject) new JSONParser().parse(in);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    documentId = (String) json.get("DocumentId");
	}
	dInput.close();

	return documentId;
    }

}
