package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.tests.dfcetest.helpers.DocubaseHelper;

public class StressNcotiTest extends AbstractNcotiTest {

   private static final int NB_DOCS = 10;

   /**
    * ID des domaines docubase. Mettre l'ID de l'AMF primaire en premier.
    */
   int[] domainIds = { 1 };

   /**
    * Association domainId => Base docubase
    */
   Map<Integer, Base> bases;

   /**
    * Documents insérés à chaque test
    */
   List<Document> docs = new ArrayList<Document>();

   String catAppliSourceFName;

   // CatHelper catHelper;

   @Before
   public void initObjects() throws Exception {
      bases = new HashMap<Integer, Base>();

      for (int domId : domainIds) {
         Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
         assertNotNull("La base " + BASE_ID + " est nulle", base);
         bases.put(domId, base);
      }
      catAppliSourceFName = bases.get(1).getBaseCategory(Categories.APPLI_SOURCE.toString()).getFormattedName();
   }

   @After
   public void deleteDocuments() throws Exception {
      for (Document doc : this.docs) {
         ServiceProvider.getStoreService().deleteDocument(doc.getUuid());
      }
   }

   /**
    * Insère N documents sur chaque Base créée à partir de domaines différents
    * (cf. setup). Au total cet helper insère X documents avec X = N x Nombre de
    * domaines.
    * <p>
    * Tous les documents insérés ont une application source générée de la
    * manière suivante :
    * <ul>
    * <li>préfixe commun aux X documents (paramètre <tt>commonPart</tt>).</li>
    * <li>suffixe commun à chaque lot de N document, i.e. commun à chaque Base.</li>
    * </ul>
    * <p>
    * 
    * @return Association Base => Nom de l'application source générée
    * @param commonPart
    *           Partie commune dans le nom de l'applisource.
    * @return
    * @throws Exception
    */
   private Map<Base, String> insertDocsForEachDomain(String commonPart, int numDocs)
         throws Exception {
      // Nom d'application généré pour chaque lot de document.
      String generatedApp;
      // A chaque base correspond une applisource du genre : commonPart + generatedApp
      Map<Base, String> appsSource = new HashMap<Base, String>();

      for (Base base : this.bases.values()) {
         generatedApp = ((Object) System.nanoTime()).toString();
         String appName = commonPart + generatedApp;
         this.docs.addAll(DocubaseHelper.insertManyDocs(numDocs, base, appName));
         appsSource.put(base, appName);
      }
      return appsSource;
   }

   private Map<Base, String> insertDocsForEachDomain(String commonPart) throws Exception {
      return insertDocsForEachDomain(commonPart, NB_DOCS);
   }

   @Test
   public void searchOnEachDomain() throws Exception {
      SearchResult result;
      String luceneQuery;
      
      // Ce n'est pas une chaine fixe pour éviter que d'ancien tests
      // ne remontent si la suppression en base à échouée.
      String commonPart = String.format("testsearchOnEachDomain%s_%s", 
            Thread.currentThread().getName(), System.nanoTime());

      Map<Base, String> appsSource = insertDocsForEachDomain(commonPart);

      // On teste que la recherche multi base donne les mêmes résultats sur tous les domaines
      for (Entry<Base, String> base2app : appsSource.entrySet()) {
         Base base = base2app.getKey();
         System.out.println(base);

         // On recherche la partie commune à tous les domaines, on doit donc
         // trouver un nombre de document égal à : NB_DOCS x Nombre de domaines
         luceneQuery = String.format("%s:%s*", catAppliSourceFName, commonPart);
         result = ServiceProvider.getSearchService().search(luceneQuery, 1000, base);

         assertNotNull("Aucun document trouvé", result.getDocuments());
         assertEquals(this.domainIds.length * NB_DOCS, result.getDocuments().size());

         // On recherche la partie spécifique au domaine en cours
         luceneQuery = String.format("%s:%s", catAppliSourceFName, base2app.getValue());
         result = ServiceProvider.getSearchService().search(luceneQuery, 1000, base);
         assertEquals(NB_DOCS, result.getDocuments().size());
      }
      
      Authentication.closeSession();
      Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, AMF_URL);
      initObjects();
      
      Base base = this.bases.get(1);
      System.out.println(base);

      // On recherche la partie commune à tous les domaines, on doit donc
      // trouver un nombre de document égal à : NB_DOCS x Nombre de domaines
      luceneQuery = String.format("%s:%s*", catAppliSourceFName, commonPart);
      result = ServiceProvider.getSearchService().search(luceneQuery, 1000, base);

      assertNotNull("Aucun document trouvé", result.getDocuments());
      assertEquals(this.domainIds.length * NB_DOCS, result.getDocuments().size());

      // On recherche la partie spécifique au domaine en cours
      /*luceneQuery = String.format("%s:%s", catAppliSourceFName, base2app.getValue());
      result = ServiceProvider.getSearchService().search(luceneQuery, 1000, base);
      assertEquals(NB_DOCS, result.getDocuments().size());*/
   }

   @Test
   public void getDocumentByUuidOnEachDomain() throws Exception {
      // Ce n'est pas une chaine fixe pour éviter que d'ancien tests
      // ne remontent si la suppression en base à échouée.
      String commonPart = "test_getDocumentByUuid" + System.nanoTime();

      Map<Base, String> appsSource = insertDocsForEachDomain(commonPart);

      // On teste que la recherche par UUID donne les mêmes résultats sur tous
      // les domaines
      for (Document doc : this.docs) {
         // System.out.println("Document domaine : " +
         // doc.getDocumentInformation().getDomainId());

         // On interroge chaque domaine avec l'UUID du doc.
         // On doit trouver le document quelque soit le domaine interrogé.
         for (Base base : appsSource.keySet()) {
            // System.out.println("Recherche sur Domaine : " +
            // base.getBaseDefinition().getDescription().getDomain().getId());
            try {
               //Document docFound = ServiceProvider.getSearchService().getDocumentByUUIDMultiBase(doc.getUuid());
               Document docFound = ServiceProvider.getSearchService().getDocumentByUUID(base, doc.getUuid());
               // Là ce serait pas de chance
               assertEquals("Les UUID sont différents", doc.getUuid(), docFound.getUuid());
               // System.out.println("Trouvé");
            } catch (Exception e) {
               String msg = String.format("Le document %s n'a pas été trouvé", doc.getUuid());
               // System.out.println(msg);
               fail(msg);
            }
         }
      }
   }

   @Test
   public void delete() throws Exception {
      String commonPart = String.format("testDelete%s_%s", Thread.currentThread().getName(), System.nanoTime());
      this.docs.clear();
      insertDocsForEachDomain(commonPart);
      assertEquals("Le nombre de document inséré est incorrrect", 
            this.domainIds.length * NB_DOCS, this.docs.size());

      for (Document doc : this.docs) {
         // SUT
         log.debug("Suppression du doc " + doc.getUuid().toString());
         ServiceProvider.getStoreService().deleteDocument(doc.getUuid());
      }
      SearchResult result;
      String luceneQuery;

      for (Base base : this.bases.values()) {
         luceneQuery = String.format("%s:%s*", catAppliSourceFName, commonPart);
         result = ServiceProvider.getSearchService().search(luceneQuery, 100, base);
         assertNull("Les documents sont censés avoir été supprimés", result.getDocuments());
      }
      // Pour ne pas faire planter le teardown
      this.docs.clear();
   }

   void print(String... strings) {
      StringBuffer concat = new StringBuffer();

      for (int i = 0; i < strings.length; i++) {
         concat.append(strings[i] + "\n");
      }
      System.out.println(concat);
   }

   @Test(expected = ExceededSearchLimitException.class)
   public void maxLimit() throws Exception {
      Base base = (Base) this.bases.values().toArray()[0];
      int limit = 100000;
      String luceneQuery = String.format("%s:toto", catAppliSourceFName);
      ServiceProvider.getSearchService().search(luceneQuery, limit, base);
   }

   @Test
   @Ignore("TODO : rendre la classe thread safe et tester plutôt recherches // insertions")
   public void searchAndDelete() {
      Runnable searchRunnable = new Runnable() {
         public void run() {
            try {
               StressNcotiTest.this.searchOnEachDomain();
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      };

      Runnable deleteRunnable = new Runnable() {
         public void run() {
            try {
               StressNcotiTest.this.delete();
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      };

      Thread searcher = new Thread(searchRunnable);
      Thread deleter = new Thread(deleteRunnable);

      for (int i = 0; i <= 10; i++) {
         searcher.start();
         deleter.start();
      }
   }
   
   @Test
   public void getBaseMultiThread() {
      Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
      System.out.println("getBaseMultiThread: " + base  + " " + Thread.currentThread().getName());
      
      Thread t = new Thread() {
         public void run() {
            System.out.println(Thread.currentThread().getName());
            Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
            System.out.println("getBaseMultiThread: " + base  + " " + Thread.currentThread().getName());
         };
      };
      t.start();
   }
   
   @Test
   public void getBaseMultiThreadWithAuth() {
      Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
      System.out.println("getBaseMultiThreadWithAuth: " + base  + " " + Thread.currentThread().getName());
      
      Thread t = new Thread() {
         public void run() {
            Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, AMF_URL);
            System.out.println(Thread.currentThread().getName());
            Base base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
            System.out.println("getBaseMultiThreadWithAuth: " + base  + " " + Thread.currentThread().getName());
            Authentication.closeSession();
         };
      };
      t.start();
   }
   
   @Test
   public void dummy() {
      // Permet de tester les before/after
   }
}
