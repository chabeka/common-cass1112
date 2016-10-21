package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
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
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
public class TmpDeleteDocsSicomorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(TmpDeleteDocsSicomorTest.class);

   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   @Test
   public void deleteDocsSicomor() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      //String codeRND = "6.1.3.1.1";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      //String requeteLucene = "cpt:true AND (sco:699 OR sco:750 OR sco:770 OR sco:330 OR sco:240 OR sco:400 OR sco:470 OR sco:641 OR sco:642 OR sco:720 OR sco:440 OR sco:530 OR sco:492 OR sco:850 OR sco:491 OR sco:170 OR sco:790 OR sco:860 OR sco:160) AND SM_DOCUMENT_TYPE:" + codeRND;
      int pasExecution = 1000;
      
      //LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      /*final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);*/
      
      final StoreService storeService = serviceProvider
      .getStoreService();

      ExecutorService executor = Executors.newFixedThreadPool(10);
      
      // pas d'iteration
      //searchQuery.setSearchLimit(pasExecution);
      
      //Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      String[] uuids = {"6A56AA66-94F0-40A4-BE73-B345F85A0C6C",
            "BB2E2846-A68B-4534-9AA7-CB9285A8DAF2",
            "421F8E72-251B-4E8F-B1FA-319EF2046383",
            "5ED2E86F-A3DF-40D8-B3F2-D6999BAC22F9",
            "EE35E6FD-515C-4211-8069-135C8698255A",
            "96FBD093-3590-46A4-907C-6F66FDC66E1D",
            "BD04818D-8603-4143-B0BC-D99307B23695",
            "374966DB-AD3F-4CE4-9E0E-117A3EB08FFE",
            "D730BB65-8368-4EA9-B688-BA6284BEC5A7",
            "346D810E-32C0-416E-BC8C-DC3D125C6FCE",
            "44628302-4CA3-4645-AF7B-47177DEE6147",
            "280D3971-428C-4B79-8DF3-324583D81695",
            "2A098D51-4F50-4ADF-9E36-AB3636FA7479",
            "30F9E9D8-814E-4AB2-9788-E4347DC72848",
            "157FD944-1422-4635-A90C-77770A8C131E" };
      
      // boucle de comptage
      //while (iterateur.hasNext()) {
      for (String uuid : uuids) {
         //Document doc = iterateur.next();
         nbDocsTrouve++;
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouvés", new Object[] {nbDocsTrouve});
         }
         
         //DeleterThread thread = new DeleterThread(storeService, doc.getUuid(), nbDocsTrouve, pasExecution);
         DeleterThread thread = new DeleterThread(storeService, UUID.fromString(uuid), nbDocsTrouve, pasExecution);
         executor.execute(thread);
      }
      
      executor.shutdown();
      while (!executor.isTerminated()) {
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
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
