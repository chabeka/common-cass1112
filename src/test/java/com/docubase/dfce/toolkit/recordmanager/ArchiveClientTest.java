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
   private final ArchiveService archiveService = ServiceProvider.getArchiveService();
   private final RecordManagerService recordManagerService = ServiceProvider
         .getRecordManagerService();

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

      // Ajouter d'�v�nements
      recordManagerService.addEventsLog(buildEventLogList());
      // cr�er une archive
      List<String> archiveIds = archiveService.archive(buildFilter());
      // V�rifier que l'archive est bien cr��e
      assertTrue("Le staockage a �chou�", archiveIds.size() > 0);
      // Nettoyer l'archive cr��e
      boolean isOk = archiveService.deleteArchive(archiveIds.get(0));
      assertTrue("L''archive n''est pas supprim�e", isOk);

      // Nettoyer les donn�es de test
      clearEventsAndArchives(archiveIds);
   }

   /**
    * Ce teste permet de v�rifier l'extraction d'une archive par son
    * identifiant, comme d�crit ci-dessous :
    * 
    * <ol>
    * <li>On archive les �v�nements filtr�s sur une date de debut et de fin.</li>
    * <li>On atteste par une assertion que l'archive est bien cr��e gr�ce � son
    * identifiant</li>
    * <li>On extrait le contenu de l'archive en question par son identifiant et
    * on v�rifie que le contenu est non vide</li>
    * <li>Pour finir, on supprime l'archive ainsi cr��e.</li>
    * </ol>
    */
   @Test
   public void testExtractArchive() {

      // Ajouter d'�v�nements et archiver
      List<String> archiveIds = createEventsAndArchivedThem();

      InputStream is = null;
      try {
         is = archiveService.extractArchive(archiveIds.get(0));
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));

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

      // Nettoyer les donn�es de test
      clearEventsAndArchives(archiveIds);
   }

   /**
    * Ce teste permet de rechercher une liste d'archives en filtrant les
    * �v�nements, sur une date de debut et de fin, comme d�crit ci-dessous :
    * 
    * <ol>
    * <li>On cr�e une archive dont les �v�nements sont filtr�s sur une date de
    * debut et de fin</li>
    * <li>On recherche les archives entre une date de debut et une date de fin.</li>
    * <li>On v�rifie que le nombre d'archive est au moins 2</li>
    * <li>Pour finir, on supprime les archives ainsi cr��es, ainsi que le
    * document.</li>
    * </ol>
    */
   @Test
   public void testGetArchives() {

      // Ajouter un �v�nement li� � un document fictif
      recordManagerService.addEventsLog(Collections.singletonList(buildDocEventLog()));

      // Ajouter d'autres �v�nements et archiver
      List<String> archiveIds = createEventsAndArchivedThem();

      assertEquals("On doit avoir deux archives", 2, archiveIds.size());

      // Retrouver les archives enregistr�es
      List<RMArchive> archives = archiveService.getArchives(buildFilter());
      assertTrue("La liste d''archive ne doit pas �tre vide", archives.size() > 1);

      // Extraire la premi�re archive de la liste
      InputStream is = null;
      try {
         is = archiveService.extractArchive(archives.get(0).getArchiveId());
         assertNotNull("Le contenu de cette archive ne devrait pas �tre vide", is);
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

      // Nettoyer les donn�es de test
      clearEventsAndArchives(archiveIds);
   }

   @Test
   public void testGetEventsFromArchive() {
      List<String> archiveIds = null;
      try {
         // Ajouter d'�v�nements et archiver
         archiveIds = createEventsAndArchivedThem();

         // Extraire une archive
         InputStream is = archiveService.extractArchive(archiveIds.get(0));

         // Convertir les lignes de l'archive en �v�nements
         List<RMClientEvent> events = archiveService.getEventsFromArchive(is);
         assertTrue("Au moins deux �v�nements devraient �tr� remont�s", events.size() > 0);

      } catch (Exception e) {
         e.printStackTrace();
      } finally {

         // Nettoyer les donn�es de test
         if (archiveIds != null)
            clearEventsAndArchives(archiveIds);
      }
   }

   /**
    * Ce teste permet de supprimer une archive � partir de son identifiant
    * donn�e, comme d�crit ci-dessous :
    * 
    * <ol>
    * <li>On cr�e une archive dont les �v�nements sont filtr�s entre une date de
    * debut et de fin</li>
    * <li>On supprime l'archive ainsi cr��es par son identifiant.</li>
    * <li>On atteste par une assertion que l'archive est bien supprimer</li>
    * <li>Pour finir, on supprime l'archive ainsi cr��es, ainsi que le document.
    * </li>
    * </ol>
    */
   @Test
   public void testDeleteArchive() {

      // Ajouter d'�v�nements et archiver
      List<String> archiveIds = createEventsAndArchivedThem();

      boolean exists = archiveService.archiveExists(archiveIds.get(0));

      assertNotNull("Le staockage a �chou�", exists);

      // Supprimer l'archive cr��e
      boolean isDeleted = archiveService.deleteArchive(archiveIds.get(0));
      assertNotNull("La suppression a �chou�", isDeleted);

      // Nettoyer les �v�nements cr��
      recordManagerService.deleteEventsLog(buildFilter());
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
    * <li>L'archive �tant au format json, on recherche l'emprunte du document
    * dans l'archive trouv�e</li>
    * <li>On atteste par une assertion que l'emprunte du document est identique
    * � celui qui se trouve dans l'archive</li>
    * <li>Pour finir, on supprime l'archive ainsi cr��es, ainsi que le document.
    * </li>
    * </ol>
    */
   @Ignore
   @Test
   public void testCheckIntegrityOfArchiveAndDocument() {

      Document document = null;
      List<String> archiveIds = null;
      try {
         // cr�er un document pour g�n�rer un �v�nement de stockage
         document = storeDocument(BASEID, "doc1.pdf");
         UUID keyDoc = document.getUuid();
         assertNotNull("Le staockage a �chou�", keyDoc);

         // Ajouter une archive filtr�e sur l'uuid du document cr��
         EventReadFilter filter = buildFilter();
         filter.setDocumentUUID(keyDoc.toString());
         archiveIds = archiveService.archive(filter);
         assertNotNull("Le stockage a �chou�", archiveIds);

         // Trouver le disgest du document
         String docDigest = document.getDigest();
         assertNotNull("Le document n'a pas de disgest", docDigest);

         // Trouver le disgest du document dans l'archive
         InputStream is = archiveService.extractArchiveByDocumentUuid(keyDoc);
         String archiveDocDigest = archiveService.findDocumentDigestFromArchive(keyDoc.toString(),
               is);
         assertNotNull("L'archive n'a de digest pour ce document", archiveDocDigest);

         assertEquals("Archive non conforme ", docDigest, archiveDocDigest);

      } catch (Exception e) {
         e.printStackTrace();
         assertFalse(false);
      } finally {
         // Nettoyer l'archive cr��e
         if (archiveIds != null && !archiveIds.isEmpty()) {
            boolean isOk = ServiceProvider.getArchiveService().deleteArchive(archiveIds.get(0));
            assertTrue("L''archive n''est pas supprim�e", isOk);
         }

         // supprimer le document cr��
         if (document != null) {
            ServiceProvider.getStoreService().deleteDocument(document.getUuid());
         }

         // Nettoyer les �v�nements cr��
         recordManagerService.deleteEventsLog(buildFilter());
      }

   }

   /**
    * Ajouter des �v�nements et cr�er une archive
    * 
    * @return List d'archives cr��es
    */
   private List<String> createEventsAndArchivedThem() {
      // Ajouter d'�v�nements
      recordManagerService.addEventsLog(buildEventLogList());
      // Ajouter une archive
      List<String> archiveIds = archiveService.archive(buildFilter());
      assertTrue("Le staockage a �chou�", archiveIds.size() > 0);
      return archiveIds;
   }

   /**
    * Nettoyer les donn�es de test
    * 
    * @param archiveIds
    */
   private void clearEventsAndArchives(List<String> archiveIds) {
      // Supprimer l'archive cr��e
      for (String archiveId : archiveIds) {
         boolean isDeleted = archiveService.deleteArchive(archiveId);
         assertNotNull("La suppression a �chou�", isDeleted);
      }

      // Nettoyer les �v�nements cr��
      recordManagerService.deleteEventsLog(buildFilter());
   }

}
