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

import net.docubase.am.client.exception.LuceneSearchLimitException;
import net.docubase.rheatoolkit.RheaToolkitException;
import net.docubase.rheatoolkit.base.CategoryDescription;
import net.docubase.rheatoolkit.exception.RheaToolkitSystemElementNotExistException;
import net.docubase.rheatoolkit.session.base.CompleteQuery;
import net.docubase.rheatoolkit.session.base.Document;
import net.docubase.rheatoolkit.session.base.SearchResult;
import net.docubase.rheatoolkit.session.base.UserBase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StressNcotiTest extends AbstractNcotiTest {

   private static final int NB_DOCS = 10;
   private static final String ADM_LOGIN = "_ADMIN";
   private static final String ADM_PASSWORD = "DOCUBASE";

   /**
    * ID des domaines docubase.
    * Mettre l'ID de l'AMF primaire en premier.
    */
   int[] domainIds = {1, 2};
   
   /**
    * Association domainId => Base docubase
    */
   Map<Integer, UserBase> bases;  
   
   /**
    * Documents insérés à chaque test
    */
   List<Document> docs = new ArrayList<Document>(); 
   
   String catAppliSourceFName; 
   
   CatHelper catHelper;

   @Before
   public void initObjects() throws Exception {
      this.bases = new HashMap<Integer, UserBase>();
      
      for (int domId: this.domainIds) {
         this.bases.put(domId, DocubaseHelper.getBase(domId));
      }
      this.catHelper = catFactory(this.domainIds[0]);
      this.catAppliSourceFName = this.catHelper.get(Categories.APPLI_SOURCE).getFormattedName();
   }
   
   @After
   public void deleteDocuments() throws Exception {
      for (Document doc: this.docs) {
         doc.delete();
      }
   }
   
   static class CatHelper {
      UserBase base;
      public CatHelper(int domainId) throws RheaToolkitException {
         this.base = DocubaseHelper.getBase(domainId);
      }
      public CategoryDescription get(Categories cat) throws RheaToolkitException {
         return base.getBaseDefinition().getIndex().getCategory(cat.toString());
      }
   }   
   
   CatHelper catFactory(int domainId) throws RheaToolkitException {
      return new CatHelper(domainId);
   }
   
   /**
    * Insère N documents sur chaque UserBase créée à partir de domaines différents (cf. setup). 
    * Au total cet helper insère X documents avec X = N x Nombre de domaines. 
    * <p>
    * Tous les documents insérés ont une application source générée de la manière suivante :
    * <ul>
    *    <li>préfixe commun aux X documents (paramètre <tt>commonPart</tt>).</li>
    *    <li>suffixe commun à chaque lot de N document, i.e. commun à chaque UserBase.</li>
    * </ul>
    * <p>   
    * @return Association UserBase => Nom de l'application source générée
    * @param commonPart Partie commune dans le nom de l'applisource.
    * @return
    * @throws Exception
    */
   private Map<UserBase, String> insertDocsForEachDomain(String commonPart, int numDocs) throws Exception {
      /**
       * Nom d'application généré pour chaque lot de document.
       */
      String generatedApp;
      /**
       * A chaque base correspond à une applisource du genre :
       * commonPart + generatedApp
       */
      Map<UserBase, String> appsSource = new HashMap<UserBase, String>();
     
      for (UserBase base: this.bases.values()) {
         generatedApp = ((Object) System.nanoTime()).toString();
         String appName = commonPart + generatedApp;
         this.docs.addAll(DocubaseHelper.insertManyDocs(numDocs, base, appName));
         appsSource.put(base, appName);
      }   
      
      return appsSource;
   }
   
   private Map<UserBase, String> insertDocsForEachDomain(String commonPart) throws Exception {
      return insertDocsForEachDomain(commonPart, NB_DOCS);
   }
     
   @Test
   public void searchOnEachDomain() throws Exception {
      SearchResult result;
      String luceneQuery;
      CompleteQuery query;
      // Ce n'est pas une chaine fixe pour éviter que d'ancien tests 
      // ne remontent si la suppression en base à échouée.
      String commonPart = String.format("testsearchOnEachDomain%s_%s", 
            Thread.currentThread().getName(), System.nanoTime());      
      
      Map<UserBase, String> appsSource = insertDocsForEachDomain(commonPart);
      
      // On teste que la recherche multi base donne les mêmes résultats sur tous les domaines
      for(Entry<UserBase, String> base2app: appsSource.entrySet()) {
         UserBase base = base2app.getKey();
         
         // On recherche la partie commune à tous les domaines, on doit donc trouver
         // un nombre de document égal à : NB_DOCS x Nombre de domaines 
         luceneQuery = String.format("%s:%s*", catAppliSourceFName, commonPart);
         query = new CompleteQuery(luceneQuery, 100, true);
         result = base.search(query);
         
         /*
         // DEBUG
         if (result.getSolutions() == null) {
            System.out.println("Aucun document trouvé sur le domaine " + base.getBaseDefinition().getDescription().getDomain().getId());
            continue;
         }*/
         assertNotNull("Aucun document trouvé", result.getSolutions()); 
         assertEquals(this.domainIds.length*NB_DOCS, result.getSolutions().size());
         
         // On recherche la partie spécifique au domaine en cours
         luceneQuery = String.format("%s:%s", catAppliSourceFName, base2app.getValue());
         query = new CompleteQuery(luceneQuery, 100, true);
         result = base.search(query);
         assertEquals(NB_DOCS, result.getSolutions().size());
      }

   }
   
   @Test
   public void getDocumentByUuidOnEachDomain() throws Exception {
      // Ce n'est pas une chaine fixe pour éviter que d'ancien tests 
      // ne remontent si la suppression en base à échouée.
      String commonPart = "test_getDocumentByUuid" + System.nanoTime();
      
      Map<UserBase, String> appsSource = insertDocsForEachDomain(commonPart);
      
      // On teste que la recherche par UUID donne les mêmes résultats sur tous les domaines
      for (Document doc : this.docs) {
         //System.out.println("Document domaine : " + doc.getDocumentInformation().getDomainId());
         
         // On interroge chaque domaine avec l'UUID du doc.
         // On doit trouver le document quelque soit le domaine interrogé.
         for (UserBase base : appsSource.keySet()) {
            //System.out.println("Recherche sur Domaine : " + base.getBaseDefinition().getDescription().getDomain().getId());
            try {
               Document docFound = base.getDocumentByUUID(doc.getDocumentInformation().getUUID());
               assertEquals(doc.getDocumentInformation().getNumDoc(),
                            docFound.getDocumentInformation().getNumDoc());
               //System.out.println("Trouvé");
            } catch (RheaToolkitSystemElementNotExistException e) {
               String msg = String.format("Le document %s, inséré via le domaine %d n'a pas été trouvé via le domaine %d",
                     doc.getDocumentInformation().getUUID(),
                     doc.getDocumentInformation().getDomainId(),
                     base.getBaseDefinition().getDescription().getDomain().getId());
//               System.out.println(msg);
               fail(msg);
            }
         }
      }
   }

   @Test
   public void delete() throws Exception {
      String commonPart = String.format("testDelete%s_%s", 
            Thread.currentThread().getName(), System.nanoTime());
      this.docs.clear();
      insertDocsForEachDomain(commonPart);
      assertEquals("Le nombre de document inséré est incorrrect", 
            this.domainIds.length * NB_DOCS, this.docs.size());
      
      for (Document doc: this.docs) {
         // SUT
         doc.delete();
      }
      
      SearchResult result;
      String luceneQuery;
      CompleteQuery query;
      
      for (UserBase base: this.bases.values()) {
         luceneQuery = String.format("%s:%s*", catAppliSourceFName, commonPart);
         query = new CompleteQuery(luceneQuery, 100, true);
         result = base.search(query);
         assertNull("Les documents sont censés avoir été supprimés", result.getSolutions());
      }
      // Pour ne pas faire planter le teardown
      this.docs.clear();
   }
   

   void print(String... strings) {
      StringBuffer concat = new StringBuffer();
      
      for (int i=0; i<strings.length; i++) {
         concat.append(strings[i] + "\n");
      }
      System.out.println(concat);
   }

   @Test(expected=LuceneSearchLimitException.class)
   public void maxLimit() throws Exception {
      UserBase base = (UserBase) this.bases.values().toArray()[0];
      int limit = 1 + net.docubase.am.network.service.common.lucene.CompleteQuery.MAX_SEARCH_LIMIT;
      String luceneQuery = String.format("%s:toto", catAppliSourceFName);
      CompleteQuery query = new CompleteQuery(luceneQuery, limit, true);
      base.search(query);
   }
   
   /**
    * TODO : vérifier que l'on teste bien ce cas dans les tests de RheaHA.
    * 
    * On se connecte à un document manager X puis on lance une requête sur un
    * document manager Y. Une exception est levée car il y a un problème sur Y,
    * on exécute alors la requête sur un autre document manager, Z.
    * @throws Exception 
    */
   /*@Test
   public void failOver() throws Exception {
      List<Document> docsFound = null;
      
      String commonPart = "testDelete_" + System.nanoTime();
      insertDocsForEachDomain(commonPart);
      
      // On arrête le premier document manager à être requêté
      //shutdownDocumentManager(this.domainIds[0]);
      
      for (Entry<Integer, UserBase> entry: this.bases.entrySet()) {
         int domId = entry.getKey();
         try {
            print("Requête sur document manager n°" + domId);
            String luceneQuery = String.format("%s:%s*", catAppliSourceFName, commonPart);
            docsFound = entry.getValue().searchDocuments(luceneQuery, 100);
            break;
         } catch (RheaToolkitSessionLicenseException e) {
            // il faut changer d'AMF primaire si c'est possible
            print("Trop de connexion, il faut changer d'AMF primaire");
         } catch (RheaToolkitException e) {
            // TODO : lorsque docubase typera + précisemment le dépassement de la limite, 
            // il faudra catcher l'exception appropriée
            if (e.getMessage().indexOf("discriminante") != -1) {
               print("La requête n'est pas assez discriminante");
               break; // pas la peine d'exécuter la requête sur un autre document manager
            }
         } catch (Exception e) {
            print(e.toString(), "testFailOver: skipping domain " + domId);
         }
      }
      if (docsFound != null) {
         print("docs.size = " + docsFound.size());
      }
      //restartDocumentManager(this.domainIds[0]);
   }*/
   
   @Test @Ignore
   public void http() throws IOException {
      shutdownDocumentManager(1);
   }
   
   protected void shutdownDocumentManager(int domId, boolean restart) throws IOException {
      final String server = "http://cer69-ds4int:8000/amfconfig/do/"; 
      String restartAction = "";
      HttpClient client = new HttpClient();
      
      // On se logge en admin
      PostMethod postLogon = new PostMethod(server + "logon");
      NameValuePair[] data = {
            new NameValuePair("username", ADM_LOGIN),
            new NameValuePair("password", ADM_PASSWORD)
      };
      postLogon.setRequestBody(data);
      assertEquals(302, client.executeMethod(postLogon));
      print(postLogon.getResponseBodyAsString());
      
      // On arrête le document manager
      if (restart) {
         restartAction = "reload=1&"; 
      }
      HttpMethod getShutdown = new GetMethod(server + "adm/shutdownFramework?" + restartAction + "id=" + domId);
      assertEquals(200, client.executeMethod(getShutdown));
      postLogon.releaseConnection();
      getShutdown.releaseConnection();
   }
   
   protected void shutdownDocumentManager(int domId) throws IOException {
      shutdownDocumentManager(domId, false);
   }
   
   protected void restartDocumentManager(int domId) throws IOException {
      shutdownDocumentManager(domId, true);
   }   
   
   
   // TODO : à faire
   @Test @Ignore
   public void injectionBatch() throws RheaToolkitException {
      //String xmlPath = "/appl/ds4/lot_valide.xml";
      //String xmlPath = "lot_valide.xml";
      //List<Document> docs = InjectorFactory.inject(xmlPath, this.session.getSessionId());
      //System.out.println(docs.size());
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
      
      for (int i=0; i<= 10; i++) {
         searcher.start();
         deleter.start();
      }
   }
}
