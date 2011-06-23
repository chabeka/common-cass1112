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

   private RecordManagerService recordManagerService = ServiceProvider.getRecordManagerService();

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
    * <li>Pour finir, on supprime tous les �v�nements entre une date de debut et
    * une date de fin.</li>
    * </ol>
    */
   @Test
   public void testAddEventLog() {
      // Ajouter un �v�nement
      recordManagerService.addEventLog(buildEventLogList().get(0));

      // retourner la liste d'�v�nements filtr�s
      List<RMClientEvent> events = recordManagerService.getEventLogList(buildFilter());

      // Trouver l'�v�nement enregistr� dans la liste retourn�e
      boolean found = false;
      for (RMClientEvent evt : events) {
         if ("Stockage".equals(evt.getEventTypeName()))
            found = true;
      }

      // Attester que l'�v�nement est bien enregistr�
      assertTrue("L'�v�nement n'est pas enregistr�", found);

      // Nettoyer les �v�nements
      recordManagerService.deleteEventsLog(buildFilter());
   }

   /**
    * Ce teste permet de v�rifier l'enregistrement de plusieurs �v�nements comme
    * d�crit ci-dessous :
    * 
    * <ol>
    * <li>On enregistre une liste d'�v�nements.</li>
    * <li>On retourne une liste d'�v�nements, en filtrant sur une date de debut
    * et une date de fin</li>
    * <li>On cherche dans cette liste, les �v�nements enregistr�s et on atteste
    * qu'ils sont bien enregistr�s</li>
    * <li>Pour finir, on supprime tous les �v�nements entre une date de debut et
    * une date de fin.</li>
    * </ol>
    */
   @Test
   public void testAddEventLogList() {
      // Ajouter un �v�nement
      recordManagerService.addEventsLog(buildEventLogList());

      // retourner la liste d'�v�nements filtr�s
      List<RMClientEvent> events = recordManagerService.getEventLogList(buildFilter());

      // Trouver l'�v�nement enregistr� dans la liste retourn�e
      boolean foundA = false;
      boolean foundB = false;
      for (RMClientEvent evt : events) {
         if ("Stockage".equals(evt.getEventTypeName()))
            foundA = true;

         if ("Consultation".equals(evt.getEventTypeName()))
            foundB = true;
      }

      // Attester que l'�v�nement de Stockage est bien enregistr�
      assertTrue("L'�v�nement de Stockage n'est pas enregistr�", foundA);

      // Attester que l'�v�nement de Stockage est bien enregistr�
      assertTrue("L'�v�nement de Consultation n'est pas enregistr�", foundB);

      // Nettoyer les �v�nements
      recordManagerService.deleteEventsLog(buildFilter());
   }

   /**
    * Ce teste permet de v�rifier la recherche d'�v�nements comme d�crit
    * ci-dessous :
    * 
    * <ol>
    * <li>On enregistre une liste de 2 �v�nements.</li>
    * <li>On cherche une liste d'�v�nements, en filtrant sur une date de debut
    * et une date de fin</li>
    * <li>On atteste que la liste remont�e est non vide</li>
    * <li>On affiche les �v�nements trouv�s</li>
    * </ol>
    */
   @Test
   public void testGetEventLogList() {

      // Ajouter un �v�nement
      recordManagerService.addEventsLog(buildEventLogList());

      // retourner la liste d'�v�nements filtr�s
      List<RMClientEvent> events = recordManagerService.getEventLogList(buildFilter());

      // Attester que cette liste est non vide
      assertTrue("La liste des �v�nements ne doit pas �tre vide",
            (!events.isEmpty() && events.size() > 1));

      // Nettoyer les �v�nements
      recordManagerService.deleteEventsLog(buildFilter());
   }

   /**
    * Ce teste permet de v�rifier la pagination apr�s la recherche d'�v�nements,
    * comme d�crit ci-dessous :
    * 
    * <ol>
    * <li>On enregistre une liste d'�v�nements.</li>
    * <li>On cr�e un filtre avec une date de debut, une date de fin et un nombre
    * d'items � retourner</li>
    * <li>On remonte la pr�mi�re page d'�v�nements � partir du filtre cr��, et
    * on recup�re la date d'�v�nement</li>
    * du dernier �l�ment de la liste de la page.</li>
    * <li>On remplace par cette date, la date de debut de la recherche dans le
    * filtre (EventStartDate).</li>
    * <li>On remonte alors la seconde page.</li>
    * </ol>
    * 
    * NB: Ainsi, on peut remonter toutes les pages en rep�tant la m�me demarche.
    */
   @Test
   public void testPagination() {
      PAGE = 1;

      // Ajouter un �v�nement
      recordManagerService.addEventsLog(buildEventLogList());

      // Cr�er un filtre avec une date de debut, une date de fin et on un
      // nombre d'items � retourner par page
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

      // Nettoyer les �v�nements
      recordManagerService.deleteEventsLog(buildFilter());
   }

   /**
    * Ce teste permet de v�rifier la tra�abilit� d'�v�nements li�s � un document
    * :
    * 
    * <ol>
    * <li>On cr�e un document dans la base GED, mais nous admettons que le
    * document existe</li>
    * <li>On enregistre �v�nement pour ce document.</li>
    * <li>On recherche les �v�nements li�s � cet document, � partir de son
    * identifiant UUID.</li>
    * <li>On atteste que tous les �v�nements sont li�s � ce document.</li>
    * </ol>
    */
   @Test
   public void testGetEventLogsByKeyDoc() {
      // Ajouter un �v�nement de stockage de document
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

      // Attester que l'�v�nement est bien enregistr�
      assertTrue("L'�v�nement n'est pas enregistr�", found);

      // Nettoyer les �v�nements
      recordManagerService.deleteEventsLog(buildFilter());
   }

   /**
    * Ce teste permet de v�rifier la suppression d'�v�nements filtr�s par dates
    * :
    * 
    * <ol>
    * <li>On enregistre une liste d'�v�nements.</li>
    * <li>On cr�e un filtre avec une date de debut et une date de fin</li>
    * <li>On supprime les �v�nements sur la base de ce filtre.</li>
    * <li>On recherche les �v�nements sur la base de ce m�me filtre.</li>
    * <li>On atteste que tous les �v�nements sont supprim�s � part celui de la
    * suppression.</li>
    * </ol>
    */
   @Test
   public void testDeleteEventLog() {

      // Ajouter un �v�nement
      recordManagerService.addEventsLog(buildEventLogList());

      recordManagerService.deleteEventsLog(buildFilter());

      // Rechercher les �v�nements sur la base de ce m�me filtre.
      List<RMClientEvent> events = ServiceProvider.getRecordManagerService().getEventLogList(
            buildFilter());

      // Attester que Le nombre d'�v�nement trouv� est bien 1, c-a-d celui
      // de l'�v�nement de la suppressiona
      assertEquals("La liste des �v�nements ne doit pas �tre vide", 2, events.size());
   }
}
