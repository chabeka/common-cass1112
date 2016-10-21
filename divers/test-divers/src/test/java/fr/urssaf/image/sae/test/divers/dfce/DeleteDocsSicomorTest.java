package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecordManagerService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class DeleteDocsSicomorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDocsSicomorTest.class);

   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   @Test
   //@Ignore
   public void countDocsSicomor() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      String[] codesRnd = { "6.1.3.1.1",
            "6.1.3.2.1",
            "6.1.3.4.2",
            "6.1.3.6.1",
            "6.1.4.1.1",
            "6.1.4.1.8",
            "8.2.1.4.3",
            "8.1.2.1.2",
            "8.A.X.X.X",
            "6.1.3.1.2"
      };
      List<String> listeCodeRnd = Arrays.asList(codesRnd);
   
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      //String requeteLucene = "SM_ARCHIVAGE_DATE:[20150601 TO "+ dateFin + "]";
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         BaseCategory categorySiteComptable = base.getBaseCategory("sco");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            if (!listeCodeRnd.contains(doc.getType())) {
               LOGGER.debug("Code RND non reconnu : {} pour le doc {}", new Object[] {doc.getType(), doc.getUuid().toString()});
            }
            
            List<Criterion> metaSiteComptable = doc.getCriterions(categorySiteComptable);
            if (metaSiteComptable.size() == 1 && StringUtils.isEmpty((String) metaSiteComptable.get(0).getWord())) {
               LOGGER.debug("Site comptable vide pour le doc {}", new Object[] {doc.getUuid().toString()});
            }
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void extraireDocsSicomorToCSV() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/docs-sicomor.csv")));
      
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      //String requeteLucene = "SM_ARCHIVAGE_DATE:[20150101 TO "+ dateFin + "]";
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
            + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
            + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            out.write(doc.getUuid().toString() + ";\n");
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      
      out.close();
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void verifDocsSicomorFromCSV() throws IOException {
      
      long nbDocs = 0;
      List<UUID> idsJournauxTotal = new ArrayList<UUID>();
      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/docs-sicomor.csv")));
         int pasExecution = 100;
         
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/verif-docs-sicomor.csv")));
         
         LOGGER.debug("Ouverture de la connexion à DFCE");
         ServiceProvider serviceProvider = dfceConnectionService.openConnection();
         
         RecordManagerService recordManagerService = serviceProvider.getRecordManagerService();
         
         out.write("Id doc;Nb events doc;Nb journaux docs\n");
         
         // boucle de verif
         String line = in.readLine();
         while (line != null) {
            String uuid = line.split(";")[0];
            //LOGGER.debug("{}", new Object[] {uuid});
            
            // recupere la liste des evenements liees au doc
            List<RMDocEvent> liste = recordManagerService.getDocumentEventLogsByUUID(UUID.fromString(uuid));
            List<UUID> idsJournaux = new ArrayList<UUID>();
            for (RMDocEvent event : liste) {
               if (event.getArchiveUUID() != null && !idsJournaux.contains(event.getArchiveUUID())) {
                  idsJournaux.add(event.getArchiveUUID());
                  if (!idsJournauxTotal.contains(event.getArchiveUUID())) {
                     idsJournauxTotal.add(event.getArchiveUUID());
                  }
               }
            }
            
            out.write(uuid + ";" + liste.size() + ";" + idsJournaux.size() + "\n");
            
            if (nbDocs % pasExecution == 0) {
               LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocs});
            }
            
            nbDocs++;
            line = in.readLine();
         }
         
         LOGGER.debug("{} journaux au total pour {} docs", new Object[] {idsJournauxTotal.size(), nbDocs});
         
         in.close();
         out.close();
         
         LOGGER.debug("Fermeture de la connexion à DFCE");
         serviceProvider.disconnect();
      } catch (RuntimeException ex) {
         throw ex;
      }
   }
   
   @Test
   @Ignore
   public void verifExistingDocsSicomorFromCSV() throws IOException {
      
      long nbDocs = 0;
      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/docs-sicomor.csv")));
         int pasExecution = 1000;
         
         LOGGER.debug("Ouverture de la connexion à DFCE");
         ServiceProvider serviceProvider = dfceConnectionService.openConnection();
         
         SearchService searchService = serviceProvider.getSearchService();
         final Base base = serviceProvider.getBaseAdministrationService()
               .getBase(dfceConnection.getBaseName());
         
         // boucle de verif
         String line = in.readLine();
         while (line != null) {
            String uuid = line.split(";")[0];
            //LOGGER.debug("{}", new Object[] {uuid});
            
            Document doc = searchService.getDocumentByUUID(base, UUID.fromString(uuid));
            
            if (doc != null) {
               LOGGER.debug("Le doc {} existe en GNT et en GNS", new Object[] {uuid});
            }
            
            if (nbDocs % pasExecution == 0) {
               LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocs});
            }
            
            nbDocs++;
            line = in.readLine();
         }
         
         in.close();
         
         LOGGER.debug("Fermeture de la connexion à DFCE");
         serviceProvider.disconnect();
      } catch (RuntimeException ex) {
         throw ex;
      }
   }
   
   @Test
   @Ignore
   public void isDocsFrozen() {
      String[] uuids = {
            "9b026c45-b59c-41ae-8184-8e96dea380a8",
            "2ea8dd3e-b001-4ec2-8239-7e80b6212bcc",
            "80128fef-41e9-4361-aaed-ce4226a06e64",
            "e64d4a38-6a04-476a-aaef-475342288cce",
            "b0ab83c3-7956-4bc1-8d23-6bc0bf30a9db",
            "3f8b933c-ff36-425e-a085-064b94c43a5b",
            "4fb24c55-fbe0-4d21-aa31-44b3f012af7e",
            "5680050c-dc4c-4d86-81cf-b379abbb3b5e",
            "9c3fcf0b-3c50-4e6e-be83-cd9f28eca074",
            "1778a9bd-d417-4c3f-9ec6-686d7fc58eb0",
            "bf3d5d6e-c23a-4c5a-acf1-afc865bd50bf",
            "b1d4b209-2915-4bd9-a182-8d764a0f67fe",
            "7461747a-5724-4b5d-836e-8299f86c8a14",
            "4994e94b-3e2c-4ac1-95b5-de332a1368ec",
            "97e869cc-166d-4ea7-ad8b-18c32173ba63",
            "7f805979-f807-4d44-b20b-c4500fd22b77",
            "9901cf31-ac64-4f1c-8137-67230f043cf5",
            "dcc63bce-8a4f-4b94-9186-7ebef663eaad",
            "c3698962-b7c6-43a2-b285-469cccfa8920"
      };
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      
      for (String uuid : uuids) {
         
         Document doc = searchService.getDocumentByUUID(base, UUID.fromString(uuid));
         
         
         
         if (doc != null && doc.getFrozen().booleanValue() == true) {
            LOGGER.debug("Le doc {} est gelé", uuid);
         }
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void getSiteComptable() {
      String[] uuids = {
            "9b026c45-b59c-41ae-8184-8e96dea380a8",
            "2ea8dd3e-b001-4ec2-8239-7e80b6212bcc",
            "80128fef-41e9-4361-aaed-ce4226a06e64",
            "e64d4a38-6a04-476a-aaef-475342288cce",
            "b0ab83c3-7956-4bc1-8d23-6bc0bf30a9db",
            "3f8b933c-ff36-425e-a085-064b94c43a5b",
            "4fb24c55-fbe0-4d21-aa31-44b3f012af7e",
            "5680050c-dc4c-4d86-81cf-b379abbb3b5e",
            "9c3fcf0b-3c50-4e6e-be83-cd9f28eca074",
            "1778a9bd-d417-4c3f-9ec6-686d7fc58eb0",
            "bf3d5d6e-c23a-4c5a-acf1-afc865bd50bf",
            "b1d4b209-2915-4bd9-a182-8d764a0f67fe",
            "7461747a-5724-4b5d-836e-8299f86c8a14",
            "4994e94b-3e2c-4ac1-95b5-de332a1368ec",
            "97e869cc-166d-4ea7-ad8b-18c32173ba63",
            "7f805979-f807-4d44-b20b-c4500fd22b77",
            "9901cf31-ac64-4f1c-8137-67230f043cf5",
            "dcc63bce-8a4f-4b94-9186-7ebef663eaad",
            "c3698962-b7c6-43a2-b285-469cccfa8920"
      };
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      
      Map<String, List<String>> groupBySiteCompta = new HashMap<String, List<String>>();
      
      for (String uuid : uuids) {
         
         Document doc = searchService.getDocumentByUUID(base, UUID.fromString(uuid));
         
         if (!doc.getCriterions("sco").isEmpty()) {
            String siteCompta = (String) doc.getCriterions("sco").get(0).getWord();
            if (!groupBySiteCompta.containsKey(siteCompta)) {
               groupBySiteCompta.put(siteCompta, new ArrayList<String>());
            }
            groupBySiteCompta.get(siteCompta).add(uuid);
         }
      }
      
      for (String siteCompta : groupBySiteCompta.keySet()) {
         LOGGER.debug("Docs pour le site comptable {} : {}", new Object[] { siteCompta, groupBySiteCompta.get(siteCompta).toArray() });
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void verifDocsSicomorWithExport() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      long nbDocsImporter = 0;
      
      String cheminImport = "c:/tmp/EXPORT_GNS_20150616_092557/";
      List<String> docsSicomor = new ArrayList<String>();
      
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20150701 TO "+ dateFin + "]";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            File file = new File(cheminImport + doc.getUuid().toString());
            
            if (file.exists()) {
               nbDocsImporter++;
               docsSicomor.add(doc.getUuid().toString());
            } 
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      LOGGER.debug("{} docs importés", new Object[] {nbDocsImporter});
      
      long nbDocNonImporte = 0;
      File rep = new File(cheminImport);
      for (String fichier : rep.list()) {
         if (!docsSicomor.contains(fichier)) {
            nbDocNonImporte++;
         }
      }
      LOGGER.debug("{} docs non importés", new Object[] {nbDocNonImporte});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void countDocsSicomorBugNumeroFacture() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      long nbDocsBugNumeroFacture = 0;
   
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20150101 TO "+ dateFin + "]";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            BaseCategory categoryNumFactSic = base.getBaseCategory("nfs");
            List<Criterion> metaNumFactSic = doc.getCriterions(categoryNumFactSic);
            
            BaseCategory categoryNumFactFour = base.getBaseCategory("nff");
            List<Criterion> metaNumFactFour = doc.getCriterions(categoryNumFactFour);
            
            if (metaNumFactSic.size() == 1 && metaNumFactFour.size() == 1 && StringUtils.isNotEmpty((String) metaNumFactSic.get(0).getWord()) && StringUtils.isEmpty((String) metaNumFactFour.get(0).getWord())) {
               nbDocsBugNumeroFacture++;
            }
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      LOGGER.debug("{} docs avec le bug sur les numeros de facture", new Object[] {nbDocsBugNumeroFacture});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void countDocsSicomorBugNumeroLot() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      String numeroLotRecherche = "201509110702";
      long nbDocsNumLot = 0;
   
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         BaseCategory categoryNumeroLot = base.getBaseCategory("nlo");
         
         BaseCategory categorySiteComptable = base.getBaseCategory("sco");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            List<Criterion> metaNumeroLot = doc.getCriterions(categoryNumeroLot);
            if (metaNumeroLot.size() == 1 && metaNumeroLot.get(0).getWord() != null && numeroLotRecherche.equals(metaNumeroLot.get(0).getWord())) {
               nbDocsNumLot++;
               
               List<Criterion> metaSiteComptable = doc.getCriterions(categorySiteComptable);
               
               LOGGER.debug("{} : (site : {}, code rnd : {})", new Object[] {doc.getUuid(), metaSiteComptable.get(0).getWord(), doc.getType() });
            }
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      LOGGER.debug("{} docs pour le numero de lot {}", new Object[] {nbDocsNumLot, numeroLotRecherche});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void countDocsSicomorBySiteCompta() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      long nbDocsDomaineComptableForSite = 0;
      boolean verifExistance = false;
      String[] sitesComptable = { 
            "319", // CIRSO
            "090", "820", "650", "320", "460", "120", "810", "310", // MIPY
            "470", "240", "330", "641", "642", "400", "720", // AQUITAINE
            "440", "530", "527", "492", "850", "491", // PAYS DE LA LOIRE
            "030", "430", "630", "150", "997", // AUVERGNE
            "699", // CIRTIL
            "421", "381", "071", "692", "380", "260", "999", "422", "730", "740", "691", "010", // RHONE-ALPES
            "750", "117", "770", // IDF
            "761", "763", "762", "270", // HAUTE NORMANDIE
            "230", "870", "190" // LIMOUSIN
      };
      List<String> listeSiteCompta = Arrays.asList(sitesComptable);
   
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         BaseCategory categorySiteComptable = base.getBaseCategory("sco");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
//            if (verifExistance) {
//               if (serviceProvider.getSearchService().getDocumentByUUID(base, doc.getUuid()) != null) {
//                  nbDocsDomaineComptable++;
//               }
//            } else {
//               nbDocsDomaineComptable++;
//            }
            
            List<Criterion> metaSiteComptable = doc.getCriterions(categorySiteComptable);
            if (metaSiteComptable.size() == 1 && StringUtils.isNotEmpty((String) metaSiteComptable.get(0).getWord()) 
                  && listeSiteCompta.contains((String) metaSiteComptable.get(0).getWord())) {
               /*if (verifExistance) {
                  if (serviceProvider.getSearchService().getDocumentByUUID(base, doc.getUuid()) != null) {
                     nbDocsDomaineComptableForSite++;
                  }
               } else {
                  nbDocsDomaineComptableForSite++;
               }*/
               nbDocsDomaineComptableForSite++;
            } 
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      LOGGER.debug("{} docs dans le domaine comptable pour les sites demandes", new Object[] {nbDocsDomaineComptableForSite});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void deleteDocsSicomorFromCSV() throws IOException {
      
      DateTime debut = new DateTime();
      
      String lastUuid = null;
      long nbDocsSupprime = 0;
      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/docs-sicomor.csv")));
         int pasExecution = 100;
         
         LOGGER.debug("Ouverture de la connexion à DFCE");
         ServiceProvider serviceProvider = dfceConnectionService.openConnection();
         
         final StoreService storeService = serviceProvider
               .getStoreService();
         
         ExecutorService executor = Executors.newFixedThreadPool(10);
         
         // boucle de suppression
         String line = in.readLine();
         while (line != null) {
            nbDocsSupprime++;
            String uuid = line.split(";")[0];
            //LOGGER.debug("{}", new Object[] {uuid});
            
            DeleterThread thread = new DeleterThread(storeService, UUID.fromString(uuid), nbDocsSupprime, pasExecution);
            executor.execute(thread);
            
            lastUuid = uuid;
            line = in.readLine();
         }
         
         executor.shutdown();
         while (!executor.isTerminated()) {
         }
         
         in.close();
         
         DateTime fin = new DateTime();
         Duration duree = new Duration(debut, fin);
         LOGGER.debug("Duree : {} jours, {} heures et {} minutes", new Long[] { duree.getStandardDays(), duree.getStandardHours(), duree.getStandardMinutes()});         
         
         LOGGER.debug("Fermeture de la connexion à DFCE");
         serviceProvider.disconnect();
      } catch (RuntimeException ex) {
         LOGGER.error("Erreur lors de la suppression ({} nb docs supprimés, dernier : {}) : {}", new Object[] {Long.toString(nbDocsSupprime), lastUuid, ex.getMessage()});
         throw ex;
      }
   }
   
   class DeleterThread implements Runnable {

      private UUID doc;
      
      private StoreService storeService;
      
      private long compteur;
      
      private long pasExecution;

      public DeleterThread(StoreService storeService, UUID doc, long compteur, long pasExecution){
         this.storeService=storeService;
         this.doc=doc;
         this.compteur=compteur;
         this.pasExecution=pasExecution;
      }

      @Override
      public void run() {
         try {
            storeService.deleteDocument(doc);
            if (compteur % pasExecution == 0) {
               LOGGER.debug("En cours : {} docs supprimés", new Object[] {compteur});
            }
         } catch (FrozenDocumentException e) {
            LOGGER.error("Impossible de supprimer le document {} : {}", new Object[] {doc, e.getMessage()});
         } 
      }
   }
}
