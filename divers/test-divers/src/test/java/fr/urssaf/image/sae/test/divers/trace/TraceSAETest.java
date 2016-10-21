package fr.urssaf.image.sae.test.divers.trace;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-local.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-prod.xml" })
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-prod-gnt.xml" })
public class TraceSAETest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(TraceSAETest.class);

   @Autowired
   private RegTechniqueService regTechniqueService;
   
   @Autowired
   private JournalEvtService journalEvtService;
   
   @Test
   public void getErreurSicomor() throws ParserConfigurationException, SAXException, IOException  {
      
      Date dateDebut = new GregorianCalendar(2015, 10, 12, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 10, 12, 23, 59, 59).getTime();
      String codeEvtRecherche = "WS_TRANSFERT|KO";
      
      Map<String, Integer> docs = new HashMap<String, Integer>();
      
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(dateDebut, dateFin, 20000, false);
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      
      for (TraceRegTechniqueIndex index : liste) {
         if (index.getContrat() != null && index.getContrat().equals("CS_SICOMOR")) {
            if (index.getCodeEvt().equals("WS_TRANSFERT|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
            } else if (index.getCodeEvt().equals("WS_SUPPRESSION|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
            } else if (index.getCodeEvt().equals("WS_CONSULTATION|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
            } else if (index.getCodeEvt().equals("WS_MODIFICATION|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
               
            }
         }
      }
      
      Iterator<Entry<String, Integer>> iterator = docs.entrySet().iterator();
      while (iterator.hasNext()) {
         Entry<String, Integer> entry = iterator.next();
         
         System.out.println(entry.getKey() + " : " + entry.getValue().toString());
      }
   }
   
   @Test
   public void checkErreurCauseByTransfert()  {
      
      Date dateDebut = new GregorianCalendar(2015, 11, 9, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 11, 9, 23, 59, 59).getTime();
      int limite = 20000;
      String cs = "CS_SICOMOR";
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
      
      Map<String, List<TraceRegTechnique>> groupByServeur = new TreeMap<String, List<TraceRegTechnique>>();
      
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(dateDebut, dateFin, limite, false);
      
      for (TraceRegTechniqueIndex index : liste) {
         if (index.getContrat() != null && index.getContrat().equals(cs)) {
            TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
            String saeServeurHostname = (String) trace.getInfos().get("saeServeurHostname");
            
            if (!groupByServeur.containsKey(saeServeurHostname)) {
               groupByServeur.put(saeServeurHostname, new ArrayList<TraceRegTechnique>());
            }
            groupByServeur.get(saeServeurHostname).add(trace);
            //System.out.println(index.getCodeEvt() + " " + saeServeurHostname + " " + formatter.format(index.getTimestamp()));
         }
      }
      
      Map<String, List<TraceJournalEvt>> groupJournalByServeur = new TreeMap<String, List<TraceJournalEvt>>();
      
      List<TraceJournalEvtIndex> journal = journalEvtService.lecture(dateDebut, dateFin, limite, false);
      for (TraceJournalEvtIndex index : journal) {
         if (index.getContratService() != null && index.getContratService().equals(cs) &&
               index.getCodeEvt() != null && index.getCodeEvt().equals("DFCE_TRANSFERT_DOC|OK")) {
            TraceJournalEvt trace = journalEvtService.lecture(index.getIdentifiant());
            String saeServeurHostname = (String) trace.getInfos().get("saeServeurHostname");
            
            if (!groupJournalByServeur.containsKey(saeServeurHostname)) {
               groupJournalByServeur.put(saeServeurHostname, new ArrayList<TraceJournalEvt>());
            }
            groupJournalByServeur.get(saeServeurHostname).add(trace);
            //System.out.println(index.getCodeEvt() + " " + saeServeurHostname + " " + formatter.format(index.getTimestamp()));
         }
      }
      
      for (String serveur : groupByServeur.keySet()) {
         // recupere la liste des trace du journal sae
         List<TraceJournalEvt> tracesSAE = groupJournalByServeur.get(serveur);
         // recupere la liste des erreurs du serveur
         for (TraceRegTechnique trace : groupByServeur.get(serveur)) {
            boolean causeByTransfert = false;
            TraceJournalEvt traceTransfert = null;
            for (TraceJournalEvt traceSAE : tracesSAE) {
               if (trace.getTimestamp().getTime() > traceSAE.getTimestamp().getTime()) {
                  // on calcule la difference entre les deux dates
                  long difference = trace.getTimestamp().getTime() - traceSAE.getTimestamp().getTime();
                  if (difference < 1000) {
                     // la difference est inferieur a la seconde
                     causeByTransfert = true;
                     traceTransfert = traceSAE;
                     break;
                  }
               }
            }
            if (causeByTransfert) {
               System.out.println("Erreur sur l'evenement de " + trace.getCodeEvt() + " et sur le serveur " + serveur + " à " + formatter.format(trace.getTimestamp()) 
                     + " probablement du au transfert sur le même serveur à " + formatter.format(traceTransfert.getTimestamp()));
            } else {
               System.out.println("Erreur autres : " + trace.getCodeEvt() + " sur le serveur " + serveur + " à " + formatter.format(trace.getTimestamp()));
            }
         }
      }
   }
   
   @Test
   public void checkErreurCauseByAlreadyLock() {
      
      Date dateDebut = new GregorianCalendar(2015, 11, 8, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 11, 8, 23, 59, 59).getTime();
      int limite = 20000;
      String cs = "CS_GROOM";
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
      
      Map<String, List<TraceRegTechnique>> groupByServeur = new TreeMap<String, List<TraceRegTechnique>>();
      
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(dateDebut, dateFin, limite, false);
      
      for (TraceRegTechniqueIndex index : liste) {
         if (index.getContrat() != null && index.getContrat().equals(cs) && (index.getCodeEvt().equals("WS_TRANSFERT|KO") || index.getCodeEvt().equals("WS_SUPPRESSION|KO"))) {
            TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
            String saeServeurHostname = (String) trace.getInfos().get("saeServeurHostname");
            
            if (!groupByServeur.containsKey(saeServeurHostname)) {
               groupByServeur.put(saeServeurHostname, new ArrayList<TraceRegTechnique>());
            }
            groupByServeur.get(saeServeurHostname).add(trace);
            //System.out.println(index.getCodeEvt() + " " + saeServeurHostname + " " + formatter.format(index.getTimestamp()));
         }
      }
      
      List<TraceJournalEvt> journalEvt = new ArrayList<TraceJournalEvt>();
      
      List<TraceJournalEvtIndex> journal = journalEvtService.lecture(dateDebut, dateFin, limite, false);
      for (TraceJournalEvtIndex index : journal) {
         if (index.getContratService() != null && index.getContratService().equals(cs) &&
               index.getCodeEvt() != null && (index.getCodeEvt().equals("DFCE_TRANSFERT_DOC|OK") || index.getCodeEvt().equals("DFCE_SUPPRESSION_DOC|OK"))) {
            TraceJournalEvt trace = journalEvtService.lecture(index.getIdentifiant());
            journalEvt.add(trace);
            //System.out.println(index.getCodeEvt() + " " + saeServeurHostname + " " + formatter.format(index.getTimestamp()));
         }
      }
      
      for (String serveur : groupByServeur.keySet()) {
         // recupere la liste des erreurs du serveur
         for (TraceRegTechnique trace : groupByServeur.get(serveur)) {
            boolean causeByAlreadyLock = false;
            TraceJournalEvt traceDelete = null;
            String serveurJournal = null;
            for (TraceJournalEvt traceSAE : journalEvt) {
               if (trace.getTimestamp().getTime() > traceSAE.getTimestamp().getTime()) {
                  // on calcule la difference entre les deux dates
                  long difference = trace.getTimestamp().getTime() - traceSAE.getTimestamp().getTime();
                  if (Math.abs(difference) < 1000) {
                     // la difference est inferieur a la seconde
                     causeByAlreadyLock = true;
                     traceDelete = traceSAE;
                     serveurJournal = (String) traceSAE.getInfos().get("saeServeurHostname");
                     break;
                  }
               }
            }
            if (causeByAlreadyLock) {
               System.out.println("Erreur sur l'evenement de " + trace.getCodeEvt() + " et sur le serveur " + serveur + " à " + formatter.format(trace.getTimestamp()) 
                     + " probablement du à l'evenement " + traceDelete.getCodeEvt() + " sur le serveur " + serveurJournal + " à " + formatter.format(traceDelete.getTimestamp()));
            } else {
               System.out.println("Erreur autres : " + trace.getCodeEvt() + " sur le serveur " + serveur + " à " + formatter.format(trace.getTimestamp()));
            }
         }
      }
   }
   
   @Test
   public void getErreurGroom() throws ParserConfigurationException, SAXException, IOException  {
      
      Date dateDebut = new GregorianCalendar(2015, 10, 12, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 10, 12, 23, 59, 59).getTime();
      String codeEvtRecherche = "WS_SUPPRESSION|KO";
      
      Map<String, Integer> docs = new HashMap<String, Integer>();
      
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(dateDebut, dateFin, 20000, false);
      if (liste == null) {
         liste = new ArrayList<TraceRegTechniqueIndex>();
      }
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      
      for (TraceRegTechniqueIndex index : liste) {
         if (index.getContrat() != null && index.getContrat().equals("CS_GROOM")) {
            if (index.getCodeEvt().equals("WS_TRANSFERT|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
            } else if (index.getCodeEvt().equals("WS_SUPPRESSION|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
            } else if (index.getCodeEvt().equals("WS_CONSULTATION|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
            } else if (index.getCodeEvt().equals("WS_MODIFICATION|KO") && codeEvtRecherche.equals(index.getCodeEvt())) {
               TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
               String request = (String) trace.getInfos().get("soapRequest");
               
               ByteArrayInputStream stream = new ByteArrayInputStream(request.getBytes());
               
               Document doc = builder.parse(stream);
               
               // enveloppe compose de header et de body, on recupere le body
               String idDoc = StringUtils.trim(doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getTextContent());
               if (docs.get(idDoc) == null) {
                  docs.put(idDoc, Integer.valueOf(1));
               } else {
                  docs.put(idDoc, Integer.valueOf(docs.get(idDoc) + 1));
               }
               
            }
         }
      }
      
      Iterator<Entry<String, Integer>> iterator = docs.entrySet().iterator();
      while (iterator.hasNext()) {
         Entry<String, Integer> entry = iterator.next();
         
         System.out.println(entry.getKey() + " : " + entry.getValue().toString());
      }
   }
   
   @Test
   public void getDocsEnErreurSicomor()   {
      
      Date dateDebut = new GregorianCalendar(2015, 10, 10, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 10, 10, 23, 59, 59).getTime();
      
      List<String> docs = new ArrayList<String>();
      
      List<TraceJournalEvtIndex> traces = journalEvtService.lecture(dateDebut, dateFin, 20000, false);
      for (TraceJournalEvtIndex index : traces) {
         if (index.getCodeEvt().equals("DFCE_DEPOT_DOC|OK") && index.getPagms().contains("PAGM_CIME_GNT_COMPTA")) {
            TraceJournalEvt trace = journalEvtService.lecture(index.getIdentifiant());
            String idDoc = (String) trace.getInfos().get("idDoc");
            String serveur = (String) trace.getInfos().get("saeServeurHostname");
            if (serveur.equals("hwi69gntappli1")) {
               docs.add(idDoc);
            }
         }
      }
      
      for (String idDoc : docs) {
         System.out.println(idDoc);
      }
   }
   
   @Test
   public void getDocsEnErreurGroom()   {
      
      Date dateDebut = new GregorianCalendar(2015, 10, 10, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 10, 10, 23, 59, 59).getTime();
      
      List<String> docs = new ArrayList<String>();
      
      List<TraceJournalEvtIndex> traces = journalEvtService.lecture(dateDebut, dateFin, 20000, false);
      for (TraceJournalEvtIndex index : traces) {
         if (index.getCodeEvt().equals("DFCE_DEPOT_DOC|OK") && index.getPagms().contains("PAGM_CIME_GNT_RH")) {
            TraceJournalEvt trace = journalEvtService.lecture(index.getIdentifiant());
            String idDoc = (String) trace.getInfos().get("idDoc");
            String serveur = (String) trace.getInfos().get("saeServeurHostname");
            if (serveur.equals("hwi69gntappli1")) {
               docs.add(idDoc);
            }
         }
      }
      
      for (String idDoc : docs) {
         System.out.println(idDoc);
      }
   }
   
   @Test
   public void getDocsOKSurCNP69()   {
      
      Date dateDebut = new GregorianCalendar(2015, 10, 20, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2015, 10, 20, 23, 59, 59).getTime();
      
      int nbDoc = 0;
      Map<String, Integer> byServer = new HashMap<String, Integer>();
      
      List<TraceJournalEvtIndex> traces = journalEvtService.lecture(dateDebut, dateFin, 20000, false);
      for (TraceJournalEvtIndex index : traces) {
         if (index.getCodeEvt().equals("DFCE_DEPOT_DOC|OK")) {
            TraceJournalEvt trace = journalEvtService.lecture(index.getIdentifiant());
            String serveur = (String) trace.getInfos().get("saeServeurHostname");
            if (serveur.startsWith("hwi69")) {
               if (byServer.containsKey(serveur)) {
                  byServer.put(serveur, byServer.get(serveur) + 1); 
               } else {
                  byServer.put(serveur, 1);
               }
               nbDoc++;
            }
         }
      }
      
      System.out.println("nb docs:  " + nbDoc);
      for (String serveur : byServer.keySet()) {
         System.out.println(serveur + ":  " + byServer.get(serveur));
      }
   }
   
   @Test
   public void checkErrorSicomor() {
      
      Date dateDebut = new GregorianCalendar(2016, 0, 11, 0, 0, 0).getTime();
      Date dateFin = new GregorianCalendar(2016, 0, 11, 23, 59, 59).getTime();
      int limite = 50000;
      String cs = "CS_SICOMOR";
      boolean verifErrorAlready = false;
      boolean verifErrorConsult = true;
      
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(dateDebut, dateFin, limite, false);
      Map<String, Long> compteurDocInexistant = new TreeMap<String, Long>();
      Map<String, Long> compteurAlreadyLock = new TreeMap<String, Long>();
      Map<UUID, Date> docsAVerifier = new TreeMap<UUID, Date>();
      Map<UUID, List<String>> docsEventsAVerifier = new TreeMap<UUID, List<String>>();
      long compteurAutres = 0;
      
      for (TraceRegTechniqueIndex index : liste) {
         if (index.getContrat() != null && index.getContrat().equals(cs) && (index.getCodeEvt().equals("WS_CONSULTATION|KO") || index.getCodeEvt().equals("WS_MODIFICATION|KO") || index.getCodeEvt().equals("WS_SUPPRESSION|KO") || index.getCodeEvt().equals("WS_TRANSFERT|KO"))) {
            TraceRegTechnique trace = regTechniqueService.lecture(index.getIdentifiant());
            String stacktrace = (String) trace.getStacktrace();
            if (stacktrace.contains("Il n'existe aucun document pour l'identifiant d'archivage")) {
               if (!compteurDocInexistant.containsKey(index.getCodeEvt())) {
                  compteurDocInexistant.put(index.getCodeEvt(), Long.valueOf(1));
               } else {
                  compteurDocInexistant.put(index.getCodeEvt(), compteurDocInexistant.get(index.getCodeEvt()) + 1);
               }
               if (verifErrorConsult) {
                  String request = (String) trace.getInfos().get("soapRequest");
                  if (request.indexOf("<ns1:idArchive>") > 0) {
                     int debut = request.indexOf("<ns1:idArchive>") + 15;
                     int fin = debut + 36;
                     String id = request.substring(debut, fin);
                     docsAVerifier.put(UUID.fromString(id), trace.getTimestamp());
                  }
               }
            } else if (stacktrace.contains("com.docubase.dfce.lock.exception.AlreadyLockedObjectException")) {
               if (!compteurAlreadyLock.containsKey(index.getCodeEvt())) {
                  compteurAlreadyLock.put(index.getCodeEvt(), Long.valueOf(1));
               } else {
                  compteurAlreadyLock.put(index.getCodeEvt(), compteurAlreadyLock.get(index.getCodeEvt()) + 1);
               }
               if (verifErrorAlready) {
                  String request = (String) trace.getInfos().get("soapRequest");
                  if (request.indexOf("<ns1:uuid>") > 0) {
                     int debut = request.indexOf("<ns1:uuid>") + 10;
                     int fin = debut + 36;
                     String id = request.substring(debut, fin);
                     docsAVerifier.put(UUID.fromString(id), trace.getTimestamp());
                     if (!docsEventsAVerifier.containsKey(UUID.fromString(id))) {
                        docsEventsAVerifier.put(UUID.fromString(id), new ArrayList<String>());
                     }
                     docsEventsAVerifier.get(UUID.fromString(id)).add(index.getCodeEvt() + "&&" + index.getTimestamp());
                  }
               }
            } else {
               compteurAutres++;
            }
         }
      }
      
      for (String codeEvent : compteurDocInexistant.keySet()) {
         System.out.println(compteurDocInexistant.get(codeEvent) + " appels du service de " + codeEvent + " pour des docs inexistant");
      }
      for (String codeEvent : compteurAlreadyLock.keySet()) {
         System.out.println(compteurAlreadyLock.get(codeEvent) + " appels du service de " + codeEvent + " avec l'erreur AlreadyLockObject");
      }
      if (compteurAutres > 0) {
         System.out.println(compteurAutres + " erreurs autres");
      }
      
      if (verifErrorConsult) {
         System.out.println(docsAVerifier.keySet().size() + " docs consultés en GNT alors qu'ils n'y sont plus");
         for (UUID idDoc : docsAVerifier.keySet()) {
            // trace les events
            System.out.println("  " + idDoc.toString());
         }
      }
      
      if (verifErrorAlready) {
         System.out.println(docsAVerifier.keySet().size() + " docs concernés par l'erreur AlreadyLockObject");
         for (UUID idDoc : docsAVerifier.keySet()) {
            // trace les events
            System.out.println("  " + idDoc.toString());
            for (String events : docsEventsAVerifier.get(idDoc)) {
               System.out.println("    " + events.split("&&")[0] + " à " + events.split("&&")[1]);
            }
         }
      }
   }
   
}
