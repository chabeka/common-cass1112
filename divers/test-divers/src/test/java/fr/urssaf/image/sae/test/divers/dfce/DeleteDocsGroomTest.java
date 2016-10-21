package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
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
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class DeleteDocsGroomTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDocsGroomTest.class);

   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   @Test
   //@Ignore
   public void countDocsGroom() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineRH = 0;
   
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
         
         BaseCategory categoryRH = base.getBaseCategory("drh");
         
         List<Criterion> metaRH = doc.getCriterions(categoryRH);
         if (metaRH.size() == 1 && Boolean.TRUE.equals(metaRH.get(0).getWord())) {
            nbDocsDomaineRH++;
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine RH", new Object[] {nbDocsDomaineRH});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void countDocsGroom2015() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineRH = 0;
   
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "drh:true AND (cop:UR917 OR cop:UR827 OR cop:UR790 OR cop:CER69 OR cop:UR340 OR cop:UR600 OR cop:UR490 OR cop:UR110 OR cop:UR642 OR cop:UR692 OR cop:UR880 OR cop:UR660 OR cop:UR300 OR cop:UR140 OR cop:UR870 OR cop:UR650 OR cop:UR630 OR cop:UR670 OR cop:UR230 OR cop:UR530 OR cop:UR240 OR cop:UR260 OR cop:UR170 OR cop:UR160 OR cop:UR150 OR cop:CRF87 OR cop:UR740 OR cop:UR860 OR cop:UR680 OR cop:UR100 OR cop:UR520 OR cop:UR320 OR cop:UR460 OR cop:UR730 OR cop:UR090 OR cop:CER31 OR cop:UR190 OR cop:UR810 OR cop:UR580 OR cop:UR737 OR cop:UR547 OR cop:UR747) AND SM_CREATION_DATE:[20150101 TO "+ dateFin + "]";
      //String requeteLucene = "SM_ARCHIVAGE_DATE:[20150708 TO "+ dateFin + "]";
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
      
      List<String> codesOrga = new ArrayList<String>();
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryRH = base.getBaseCategory("drh");
         
         List<Criterion> metaRH = doc.getCriterions(categoryRH);
         if (metaRH.size() == 1 && Boolean.TRUE.equals(metaRH.get(0).getWord())) {
            nbDocsDomaineRH++;
            
            BaseCategory categoryCodeOrga = base.getBaseCategory("cop");
            List<Criterion> metaCodeOrga = doc.getCriterions(categoryCodeOrga);
            if (metaCodeOrga.size() == 1) {
               if (!codesOrga.contains(metaCodeOrga.get(0).getWord())) {
                  codesOrga.add((String) metaCodeOrga.get(0).getWord());
               }
            }
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine RH", new Object[] {nbDocsDomaineRH});
      
      LOGGER.debug("code Orga : {}", codesOrga);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void countDocsGroomByYearAndIdArchivage() throws IOException, SearchQueryParseException {
      
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
      Map<String, Long> countByYear = new TreeMap<String, Long>();
      Map<String, Long> countAllByYear = new TreeMap<String, Long>();
      long nbDocsTrouve = 0;
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "drh:true AND cop:* AND SM_CREATION_DATE:[20120101 TO 20160517] ";
      
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
         
         String year = "";
         if (doc.getCreationDate() != null) {
            year = formatter.format(doc.getCreationDate());
         }
         
         BaseCategory categoryNumeroIdArchivage = base.getBaseCategory("nid");
         
         List<Criterion> metaNumeroIdArchivage = doc.getCriterions(categoryNumeroIdArchivage);
         if (metaNumeroIdArchivage.size() == 1 && StringUtils.isNotEmpty(metaNumeroIdArchivage.get(0).getWordValue())) {
            if (!countByYear.containsKey(year)) {
               countByYear.put(year, Long.valueOf(1));
            } else {
               countByYear.put(year, countByYear.get(year).longValue() + 1);
            }
         } 
         
         if (!countAllByYear.containsKey(year)) {
            countAllByYear.put(year, Long.valueOf(1));
         } else {
            countAllByYear.put(year, countAllByYear.get(year).longValue() + 1);
         }
         
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      
      for (String year : countByYear.keySet()) {
         LOGGER.debug("{} : {} doc(s) avec le numéro d'id archivage sur {} ", new Object[] {year, countByYear.get(year), countAllByYear.get(year) });
      }
      
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void extraireDocsGroomToCSV() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineRH = 0;
      
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/docs-groom.csv")));
      
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
         
         BaseCategory categoryRH = base.getBaseCategory("drh");
         
         List<Criterion> metaRH = doc.getCriterions(categoryRH);
         if (metaRH.size() == 1 && Boolean.TRUE.equals(metaRH.get(0).getWord())) {
            nbDocsDomaineRH++;
            
            //out.write(doc.getUuid().toString() + ";\n");
            
            out.write(doc.getUuid().toString() + ";");
            out.write(doc.getType() + ";");
            out.write(doc.getCriterions("cse").get(0).getWord() + ";");
            out.write(doc.getCriterions("cop").get(0).getWord() + ";\n");
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine RH", new Object[] {nbDocsDomaineRH});
      
      out.close();
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void deleteDocsGroomFromCSV() throws IOException {
      
      DateTime debut = new DateTime();
      
      String lastUuid = null;
      long nbDocsSupprime = 0;
      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/docs-groom.csv")));
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
