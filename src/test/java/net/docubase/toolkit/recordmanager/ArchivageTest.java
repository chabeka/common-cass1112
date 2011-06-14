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
     * Ce teste permet de v�rifier la cr�ation d'une archive � partir
     * d'�v�nements filtr�s sur une date de debut et une date de fin, comme
     * d�crit ci-dessous :
     * 
     * <ol>
     * <li>On archive les �v�nements filtr�s sur une date de debut et de fin.</li>
     * <li>On atteste par une assertion que l'archive est bien cr��e gr�ce � son
     * identifiant</li>
     * <li>Pour finir, on supprime l'archive ainsi cr��e.</li>
     * </ol>
     */
    @Test
    public void testAddArchive() {

	String archiveId = null;
	try {

	    // Cr�er une archive
	    archiveId = ServiceProvider.getArchiveService().archive(
		    buildFilter());
	    // V�rifier que l'archive est bien cr��e
	    assertNotNull("Le staockage a �chou�", archiveId);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer l'archive cr��e
	    boolean isOk = ServiceProvider.getArchiveService().deleteArchive(
		    archiveId);
	    assertTrue("L''archive n''est pas supprim�e", isOk);
	}

    }

    /**
     * Ce teste permet de rechercher une archive par son identifiant, comme
     * d�crit ci-dessous :
     * 
     * <ol>
     * <li>On archive les �v�nements filtr�s sur une date de debut et de fin.</li>
     * <li>On atteste par une assertion que l'archive est bien cr��e gr�ce � son
     * identifiant</li>
     * <li>On recherche l'archive en question par son identifiant et on atteste
     * que l'archive existe</li>
     * <li>Pour finir, on supprime l'archive ainsi cr��e.</li>
     * </ol>
     */
    @Test
    public void testGetArchive() {

	String archiveId = null;
	try {
	    // Cr�er une archive
	    archiveId = ServiceProvider.getArchiveService().archive(
		    buildFilter());
	    // V�rifier que l'archive est bien cr��e
	    assertNotNull("Le staockage a �chou�", archiveId);

	    // Remonter l'archive cr��e
	    boolean archiveExists = ServiceProvider.getArchiveService()
		    .archiveExists(archiveId);
	    // V�rifier que l'archive existe
	    assertTrue("Aucune archive n''est trouv�e", archiveExists);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer l'archive cr��e

	    boolean isOk = ServiceProvider.getArchiveService().deleteArchive(
		    archiveId);
	    assertTrue("L''archive n''est pas supprim�e", isOk);
	}
    }

    /**
     * Ce teste permet de rechercher une liste d'archives en filtrant les
     * �v�nements, sur une date de debut et de fin, comme d�crit ci-dessous :
     * 
     * <ol>
     * <li>On insert un document dans la base, afin de g�n�rer un �v�nement de
     * type stockage li� au document</li>
     * <li>On cr�e une premi�re archive dont les �v�nements sont li�s � cet
     * document.</li>
     * <li>On cr�e une seconde archive dont les �v�nements sont filtr�s sur une
     * date de debut et de fin</li>
     * <li>On recherche les archive ainsi cr��es entre une date de debut et une
     * date de fin.</li>
     * <li>On atteste par une assertion que le nombre d'archives est bien 2</li>
     * <li>Pour finir, on supprime les archives ainsi cr��es, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testGetArchives() {

	Document document = null;
	List<String> archivesIds = null;
	try {

	    // cr�er un document pour g�n�rer un �v�nement de stockage
	    document = insertDocument(DOC, base);
	    UUID keyDoc = document.getUUID();
	    assertNotNull("Le staockage du document a �chou�", keyDoc);

	    // Cr�er une 1ere archive par rapport � l'uuid du document cr��
	    EventReadFilter filter = buildFilter();
	    filter.setDocumentUUID(keyDoc);
	    String archiveId = ServiceProvider.getArchiveService().archive(
		    filter);
	    assertNotNull("Le staockage a �chou�", archiveId);

	    // Cr�er une 2eme archive par rapport � une date de debut et de fin
	    // des �v�nements
	    filter = buildFilter();
	    archiveId = ServiceProvider.getArchiveService().archive(filter);
	    assertNotNull("L'archivage a �chou�", archiveId);

	    // V�rifier que le nombre d'archives ainsi cr��es est bien 2
	    archivesIds = ServiceProvider.getArchiveService().getArchivesIds(
		    buildFilter());
	    assertNotNull("les archives ne peuvent pas �tre nul", archivesIds);
	    assertEquals("Nombre d'archives trouv�e n'est pas conforme ", 2,
		    archivesIds.size());

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer les archives cr��es
	    for (String archiveId : archivesIds) {
		boolean isOk = ServiceProvider.getArchiveService()
			.deleteArchive(archiveId);
		assertTrue("L''archive n''est pas supprim�e", isOk);
	    }
	    // supprimer le document cr��
	    if (document != null)
		ServiceProvider.getStoreService().deleteDocument(document);
	}
    }

    /**
     * Ce teste permet de v�rifier qu'une archive est bien celle d'un document
     * donn�e, comme d�crit ci-dessous :
     * 
     * <ol>
     * <li>On insert un document dans la base, afin de g�n�rer un �v�nement de
     * type stockage li� au document</li>
     * <li>On cr�e une archive dont les �v�nements sont li�s � cet document.</li>
     * <li>On recherche l'archive ainsi cr��es par l'identifiant du document.</li>
     * <li>L'archive �tant au format json, onrecherche l'identifiant du document
     * dans l'archive trouv�e</li>
     * <li>On atteste par une assertion que l'archive est bien celle du document
     * </li>
     * <li>Pour finir, on supprime l'archive ainsi cr��es, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testCheckIntegrityOfArchiveAndDocument() {

	Document document = null;
	String archiveId = null;
	try {
	    // cr�er un document pour g�n�rer un �v�nement de stockage
	    document = insertDocument(DOC, base);
	    UUID keyDoc = document.getUUID();
	    assertNotNull("Le staockage a �chou�", keyDoc);

	    // Cr�er une archive par rapport � l'uuid du document cr��
	    EventReadFilter filter = buildFilter();
	    filter.setDocumentUUID(keyDoc);
	    archiveId = ServiceProvider.getArchiveService().archive(filter);
	    assertNotNull("Le staockage a �chou�", archiveId);

	    // Rechercher l'archive � partir de l'identifiant du document
	    archiveId = ServiceProvider.getArchiveService()
		    .getArchiveIdByDocumentUuid(keyDoc);
	    assertNotNull(
		    "Impossible de retouner l'archive par l'id du document",
		    archiveId);

	    // V�rifier que l'identifiant du document contenu du fichier
	    // d'archive est bien identique � celui du document d'origine
	    DataContainer archiveFile = ServiceProvider.getArchiveService()
		    .getArchiveFile(archiveId);

	    String documentId = findKeyDocFromFileContent(archiveFile
		    .getInputStream());
	    assertEquals("Archive non conforme ", keyDoc.toString(), documentId);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // Nettoyer l'archive cr��e
	    if (archiveId != null) {
		boolean isOk = ServiceProvider.getArchiveService()
			.deleteArchive(archiveId);
		assertTrue("L''archive n''est pas supprim�e", isOk);
	    }

	    // supprimer le document cr��
	    if (document != null)
		ServiceProvider.getStoreService().deleteDocument(document);
	}

    }

    /**
     * Ce teste permet de supprimer une archive � partir de son identifiant
     * donn�e, comme d�crit ci-dessous :
     * 
     * <ol>
     * <li>On cr�e une archive dont les �v�nements sont filtr�s entre une date
     * de debut et de fin</li>
     * <li>On supprime l'archive ainsi cr��es par son identifiant.</li>
     * <li>On atteste par une assertion que l'archive est bien supprimer, gr�ce
     * au code retour isOk</li>
     * <li>Pour finir, on supprime l'archive ainsi cr��es, ainsi que le
     * document.</li>
     * </ol>
     */
    @Test
    public void testDeleteArchive() {
	String archiveId = null;
	try {
	    // Cr�er une archive
	    archiveId = ServiceProvider.getArchiveService().archive(
		    buildFilter());
	    assertNotNull("Le staockage a �chou�", archiveId);

	    boolean isOk = ServiceProvider.getArchiveService().deleteArchive(
		    archiveId);
	    assertTrue("L''archive n''est pas supprim�e", isOk);
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
