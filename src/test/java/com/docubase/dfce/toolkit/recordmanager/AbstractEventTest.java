package com.docubase.dfce.toolkit.recordmanager;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.EventReadFilter;
import net.docubase.toolkit.model.recordmanager.RMClientEvent;
import net.docubase.toolkit.service.ServiceProvider;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public abstract class AbstractEventTest extends AbstractTestCaseCreateAndPrepareBase {

   /** Numero de page */
   protected static int PAGE = 1;

   /** Valeur fictive de keyDoc */
   protected static final String KEY_DOC = "1630f480-8e14-48a3-b845-2be35d069df6";

   /**
    * Build the log's search filter
    * 
    * @return the logs
    */
   protected EventReadFilter buildFilter() {
      Calendar startDate = GregorianCalendar.getInstance();
      startDate.set(Calendar.HOUR_OF_DAY, 9);
      Calendar endDate = GregorianCalendar.getInstance();
      endDate.set(Calendar.HOUR_OF_DAY, 19);

      EventReadFilter eventReadFilter = ToolkitFactory.getInstance().createEventReadFilter(
            startDate.getTime(), endDate.getTime());
      return eventReadFilter;
   }

   /**
    * Construire des �v�nements et les retourner
    * 
    * @return Liste d'�v�nements construits
    */
   protected List<RMClientEvent> buildEventLogList() {

      List<RMClientEvent> events = new ArrayList<RMClientEvent>();
      RMClientEvent evtLog = ToolkitFactory.getInstance().createRMClientEvent();
      evtLog.setActorLogin("_ADMIN");
      evtLog.setContextName("test");
      evtLog.setEventDate(Calendar.getInstance().getTime());
      evtLog.setSystemDate(Calendar.getInstance().getTime());
      evtLog.setEventTypeName("Stockage");
      evtLog.setObjectId("0@JUNIT.1");
      evtLog.setObjectType("TESTSTORE");
      events.add(evtLog);

      evtLog = ToolkitFactory.getInstance().createRMClientEvent();
      evtLog.setActorLogin("_ADMIN");
      evtLog.setContextName("test");
      evtLog.setEventDate(Calendar.getInstance().getTime());
      evtLog.setSystemDate(Calendar.getInstance().getTime());
      evtLog.setEventTypeName("Consultation");
      evtLog.setObjectId("0@JUNIT.1");
      evtLog.setObjectType("TESTSTORE");
      events.add(evtLog);

      return events;
   }

   /**
    * Construire des �v�nements et les retourner
    * 
    * @return Liste d'�v�nements construits
    */
   protected RMClientEvent buildDocEventLog() {

      RMClientEvent evtLog = ToolkitFactory.getInstance().createRMClientEvent();
      evtLog.setDocumentUuid(KEY_DOC);
      evtLog.setActorLogin("_ADMIN");
      evtLog.setContextName("test");
      evtLog.setEventDate(Calendar.getInstance().getTime());
      evtLog.setSystemDate(Calendar.getInstance().getTime());
      evtLog.setEventTypeName("Stockage");
      evtLog.setObjectId("0@JUNIT.1");
      evtLog.setObjectType("TESTSTORE");
      return evtLog;
   }

   /**
    * Stocker un document dans une base donn�e
    * 
    * @param baseId
    * @param filename
    * @return
    * @throws IOException
    */
   protected Document storeDocument(String baseId, String filename) throws IOException,
         TagControlException {

      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.setCreationDate(Calendar.getInstance().getTime());
      document.setType("pdf");
      document.addCriterion(base.getBaseCategory(catNames[0]), "FileRef");

      File file = getFile(filename, ArchiveClientTest.class);
      Document stored = ServiceProvider.getStoreService().storeDocument(document,
            new FileInputStream(file));

      return stored;
   }

   /**
    * Retourner la date d'�v�nement du dernier de la liste d'�v�nements,
    * recherch� sur la base d'un filtre donn�
    * 
    * @param filter
    *           de recherche d'�v�nements
    * @throws Exception
    *            en cas d'erreur
    */
   protected Date findEventLog(EventReadFilter filter) {

      final ArrayDeque<RMClientEvent> events = new ArrayDeque<RMClientEvent>();
      List<RMClientEvent> result = ServiceProvider.getRecordManagerService()
            .getEventLogList(filter);

      if (!result.isEmpty()) {
         events.addAll(result);
         System.out.format("\n*** PAGE N� %d \n", PAGE++);
      }
      assertTrue("La liste des �v�nements ne doit pas �tre vide", events.size() > 0);

      for (RMClientEvent event : events) {
         print(event);
      }

      return result.isEmpty() ? null : ServiceProvider.getRecordManagerService().peekLast(events)
            .getEventDate();
   }

   /**
    * Afficher les donn�es d'un �v�nement
    * 
    * @param event
    *           �v�nement � afficher
    */
   protected void print(RMClientEvent event) {
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
    *           libell�
    * @param value
    *           valeur
    */
   protected void format(String name, String value) {
      System.out.format("%s ==> %s \n", name, value != null ? value : "");
   }

}
