package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.docubase.toolkit.Authentication;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.linkedin.util.concurrent.ThreadPerTaskExecutor;

import fr.urssaf.image.tests.dfcetest.helpers.DocubaseHelper;

/**
 * Teste l'extraction d'un document en contexte mono et multi thread.
 * 
 */
@RunWith(Parameterized.class)
public class ExtractionTest extends AbstractNcotiTest {
   
   private int nbThread;
   
   @Parameters
   public static List<Integer[]> numbersOfThreads() {
      return Arrays.asList(new Integer[][] {
            {1, 0}, // mono thread, mais exécuté dans un thread différent du main
            {3, 0}, 
            {20, 0},
            {50, 0},
      });
   }
   
   public ExtractionTest(int input, int expected) {
      this.nbThread = input;
      // pas besoin d'expected dans cette classe
   }
   
   /**
    * Extraction réalisée dans le main thread.
    * @throws Exception
    */
   @Test
   public void monoSearchExtraction() throws Exception {
      final String appliSource = "test_multiExtraction" + System.nanoTime();
      Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
      DocubaseHelper.storeOneDocWithRandomCategories(base, appliSource);
      
      BaseCategory cat = base.getBaseCategory(Categories.APPLI_SOURCE.toString());
      String fname = cat.getFormattedName();
      String luceneQuery = String.format("%s:%s", fname, appliSource);
      SearchResult result = ServiceProvider.getSearchService().search(luceneQuery, 10, base);
      
      File extractedDoc = null;
      if (result != null && result.getDocuments() != null) {
         assertEquals("Un seul document devrait être trouvé", 1, result.getDocuments().size());
         Document doc = result.getDocuments().get(0);
         extractedDoc = ServiceProvider.getStoreService().getDocumentFile(doc).getAbsoluteFile();
      } else {
         fail("Impossible de trouver le document dont l'appli source est " + appliSource);
      }
      log.debug("{}, fichier : {}", Thread.currentThread().getName(), extractedDoc.getAbsolutePath());
   }
   
   /**
    * Teste que l'extraction des documents est bien thread safe.
    * <p>
    * On crée une tâche d'extraction par thread puis on teste que
    * l'on a bien autant de documents que de threads.
    * </p>  
    * @throws Exception
    */
   @Test
   public void multiSearchExtraction() throws Exception {
      final String appliSource = "test_multiExtraction" + System.nanoTime();
      final Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
      DocubaseHelper.storeOneDocWithRandomCategories(base, appliSource);
      
      List<Future<File>> futures = new ArrayList<Future<File>>();
      Set<String> files = new HashSet<String>();

      for (int i = 0; i < nbThread; i++) {
         Future<File> future = ThreadPerTaskExecutor.execute(new SearchThenExtractTask(appliSource));
         futures.add(future);
      }

      for (Future<File> future : futures) {
         File extractedDoc = future.get();
         assertNotNull(extractedDoc);
         files.add(extractedDoc.getAbsolutePath());
      }
      assertEquals("L'extraction n'est pas thread safe.", nbThread, files.size());
   }
   
   class SearchThenExtractTask implements Callable<File> {
      private String docAppliSource;
      
      public SearchThenExtractTask(String docAppliSource) {
         this.docAppliSource = docAppliSource;
      }
      
      public File call() throws Exception {
         Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, AMF_HOST, AMF_PORT, DOMAIN_ID);
         Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
         BaseCategory cat = base.getBaseCategory(Categories.APPLI_SOURCE.toString());
         String catFName = cat.getFormattedName();          
         
         File extractedDoc = null;
         String luceneQuery = String.format("%s:%s", catFName, docAppliSource);
         SearchResult result = ServiceProvider.getSearchService().search(luceneQuery, 10, base);
         
         if (result != null && result.getDocuments() != null) {
            assertEquals("Un seul document devrait être trouvé", 1, result.getDocuments().size());
            Document doc = result.getDocuments().get(0);
            extractedDoc = ServiceProvider.getStoreService().getDocumentFile(doc).getAbsoluteFile();
         } else {
            fail("Impossible de trouver le document dont l'appli source est " + docAppliSource);
         }
         log.debug("{}, fichier : {}", Thread.currentThread().getName(), extractedDoc.getAbsolutePath());
         Authentication.closeSession();
         return extractedDoc;
      } 
   }
}
