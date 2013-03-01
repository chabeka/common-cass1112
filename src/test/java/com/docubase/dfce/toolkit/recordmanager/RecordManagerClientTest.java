package com.docubase.dfce.toolkit.recordmanager;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.joda.time.DateTime;
import org.junit.Test;

public class RecordManagerClientTest extends AbstractEventTest {

   private RecordManagerService recordManagerService = serviceProvider.getRecordManagerService();

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

      RMSystemEvent createdEvent = recordManagerService
            .createCustomSystemEventLog((buildSystemEventsLogList().get(0)));

      // retourner la liste d'�v�nements filtr�s
      List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(new DateTime(
            createdEvent.getEventDate()).minusMillis(1).toDate(),
            new DateTime(createdEvent.getEventDate()).plusMillis(1).toDate());

      // Trouver l'�v�nement enregistr� dans la liste retourn�e
      boolean found = false;
      for (RMSystemEvent evt : events) {
         if ("EVENT1".equals(evt.getEventDescription())) {
            found = true;
         }
      }

      // Attester que l'�v�nement est bien enregistr�
      assertTrue("L'�v�nement n'est pas enregistr�", found);
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
      List<RMSystemEvent> systemEventsLogList = buildSystemEventsLogList();
      assertEquals(2, systemEventsLogList.size());

      RMSystemEvent createdEvent1 = recordManagerService
            .createCustomSystemEventLog(systemEventsLogList.get(0));
      RMSystemEvent createdEvent2 = recordManagerService
            .createCustomSystemEventLog(systemEventsLogList.get(1));

      // retourner la liste d'�v�nements
      List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(new DateTime(
            createdEvent1.getEventDate()).minusMillis(1).toDate(),
            new DateTime(createdEvent2.getEventDate()).plusMillis(1).toDate());

      // Trouver l'�v�nement enregistr� dans la liste retourn�e
      boolean foundA = false;
      boolean foundB = false;
      for (RMSystemEvent evt : events) {
         if ("EVENT1".equals(evt.getEventDescription()))
            foundA = true;

         if ("EVENT2".equals(evt.getEventDescription()))
            foundB = true;
      }

      // Attester que l'�v�nement de Stockage est bien enregistr�
      assertTrue("L'�v�nement de EVENT1 n'est pas enregistr�", foundA);

      // Attester que l'�v�nement de Stockage est bien enregistr�
      assertTrue("L'�v�nement de EVENT2 n'est pas enregistr�", foundB);
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

      // retourner la liste d'�v�nements filtr�s
      List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(beginDate,
            endDate);

      // Attester que cette liste est non vide
      assertTrue("La liste des �v�nements ne doit pas �tre vide",
            (!events.isEmpty() && events.size() > 1));
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
      RMDocEvent docEventLog = buildDocEventLog();

      recordManagerService.createCustomDocumentEventLog(docEventLog);

      List<RMDocEvent> events = serviceProvider.getRecordManagerService()
            .getDocumentEventLogsByUUID(KEY_DOC);

      boolean found = false;
      for (RMDocEvent evt : events) {
         found = true;
         if (!KEY_DOC.toString().equals(evt.getDocUUID().toString())) {
            found = false;
            break;
         }
      }

      // Attester que l'�v�nement est bien enregistr�
      assertTrue("L'�v�nement n'est pas enregistr�", found);
   }
}
