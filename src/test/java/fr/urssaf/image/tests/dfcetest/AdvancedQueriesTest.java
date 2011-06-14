/**
 * 
 */
package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.tests.dfcetest.helpers.DocGen;
import fr.urssaf.image.tests.dfcetest.helpers.DocubaseHelper;

/**
 * Teste les requêtes Lucene complexes
 * 
 */
public class AdvancedQueriesTest extends AbstractNcotiTest {
   
   private Base base;
   private BaseCategory appliSourceCategory;
   private String appliSourceFName;
   private String intergerFName;
   private String boolFName;
   private BaseCategory integerCategory;
   private BaseCategory boolCategory;
   private DocGen docGen;
   private List<Document> docs;
   
   @Before
   public void createContext() {
      docs = new ArrayList<Document>();
      
      base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
      docGen = new DocGen(base);      
      
      appliSourceCategory = base.getBaseCategory(Categories.APPLI_SOURCE.toString());
      appliSourceFName = appliSourceCategory.getFormattedName();
      
      integerCategory = base.getBaseCategory(Categories.INTEGER.toString());
      intergerFName = integerCategory.getFormattedName(); 
      
      boolCategory = base.getBaseCategory(Categories.BOOLEAN.toString());
      boolFName = boolCategory.getFormattedName(); 
   }
   
   @After
   public void teardown() {
      for (Document doc : DocGen.storedDocuments) {
         ServiceProvider.getStoreService().deleteDocument(doc);
      }
   }
   
   /**
    * Requête booléenne de recherche simple : c1=x AND c2=y
    */
   @Test
   public void AQ1() {
      // Document A
      String titleA = docGen.setRandomTitle("AQ1A").getTitle();
      docGen.put(Categories.INTEGER, 10);
      Document docA = docGen.store();
      
      // Document B
      docGen.setRandomTitle("AQ1B").store();
      
      String lucene = String.format("%s:%s", intergerFName, 10);
      SearchResult result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(2, result.getDocuments().size());
      
      lucene = String.format("%s:%s", appliSourceFName, titleA);
      result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());

      // SUT
      //lucene = String.format("%s:%s AND %s:%s", intergerFName, 10, appliSourceFName, titleA);
      // TODO : enlever la clause OR lorsque Docubase aura corrigé CRTL-45
      lucene = String.format("%s:%s AND %s:%s OR 1:1", intergerFName, 10, appliSourceFName, titleA);
      result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(docA, result.getDocuments().get(0));      
   }
   
   /**
    * Requête de type (c1=x OR c2=y) AND (c3 = z)
    */
   @Test
   public void AQ2() {
      // Document A
      String titleA = docGen.setRandomTitle("AQ2A").getTitle();
      docGen.put(Categories.INTEGER, 10);
      docGen.put(Categories.BOOLEAN, false);
      Document docA = docGen.store();
      
      // Document B
      docGen.setRandomTitle("AQ2B");
      docGen.put(Categories.BOOLEAN, true);
      Document docB = docGen.store();                
      
      String lucene = String.format("%s:%s", intergerFName, 10);
      SearchResult result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(2, result.getDocuments().size());
      
      lucene = String.format("%s:%s", appliSourceFName, titleA);
      result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());      

      lucene = String.format("%s:%s OR %s:%s", intergerFName, 10, appliSourceFName, titleA);
      result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(2, result.getDocuments().size()); 
      
      // SUT
      lucene = String.format("(%s:%s OR %s:%s) AND %s:%s", 
            intergerFName, 10, appliSourceFName, titleA, boolFName, true);
      result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size()); 
      assertDocumentEquals(docB, result.getDocuments().get(0));
      
      // SUT
      lucene = String.format("(%s:%s OR %s:%s) AND %s:%s", 
            intergerFName, 10, appliSourceFName, titleA, boolFName, false);
      result = ServiceProvider.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(docA, result.getDocuments().get(0));
   }
   

   /**
    * Requête de type (c1=x OR c2=y) AND (c3 = z)
    */
   @Test @Ignore("TODO")
   public void AQ3() {
      
   }

   /**
    * Requête contenant un joker * avec une valeur de catégorie contenant un underscore. 
    * Le underscore est important car il est mal géré par Docubase.
    */
   @Test
   public void AQ4() {
      String firstPartTitle = DocubaseHelper.randomAlphaNum(10); 
      Document injectedDoc = docGen.setRandomTitle(firstPartTitle).store();      
      log.debug("AQ4, Doc inséré -> " + injectedDoc.getUUID().toString());
      
      Document docbyUUID = ServiceProvider.getSearchService().getDocumentByUUIDMultiBase(injectedDoc.getUUID());
      assertDocumentEquals(injectedDoc, docbyUUID);
      
      String lucene = appliSourceFName + ":" + docGen.getTitle();
      SearchResult result = ServiceProvider.getSearchService().search(lucene, 100, base);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(injectedDoc, result.getDocuments().get(0));
      
      // SUT
      lucene = String.format("%s:%s*", appliSourceFName, firstPartTitle);
      result = ServiceProvider.getSearchService().search(lucene, 100, base);
      assertEquals(1, result.getDocuments().size());
      
      // On obtient le bon nombre de documents mais est-ce ceux que l'on a inséré ?
      assertDocumentEquals(injectedDoc, result.getDocuments().get(0));
   }
   
   
   /**
    * Requête booléenne absurde : c1=x AND c1=y
    * Cela permet de tester la stabilité de Docubase et l'exactitude 
    * de l'implémentation ensembliste.
    */
   @Test
   public void AQ5() {
      int nbDocsA = 17;
      String appliSourceA = docGen.setRandomTitle("AQ5A").getTitle();
      docGen.storeMany(nbDocsA);

      int nbDocsB = 13;
      String appliSourceB = docGen.setRandomTitle("AQ5B").getTitle();
      docGen.storeMany(nbDocsB);
      // TODO : enlever la clause OR lorsque Docubase aura corrigé CRTL-45      
      String lucene = String.format("%s:%s AND %s:%s OR 1:1", 
            appliSourceFName, appliSourceA, appliSourceFName, appliSourceB);
      // On fixe une limite de recherche plus grande pour voir si on ne ramène pas plus de
      // résultats que prévu
      int searchLimit = 2 * (nbDocsA + nbDocsB);
      // SUT
      SearchResult result = ServiceProvider.getSearchService().search(lucene, searchLimit, base, null);
      assertEquals(0, result.getDocuments().size());
   }
   
   /**
    * Teste qu'une valeur peut contenir des espaces.
    */
   @Test
   public void spaces() {
      String appliSourceB = docGen.setRandomTitle("un titre ").getTitle();
      docGen.store();
      String lucene = String.format("%s:\"%s\"", appliSourceFName, appliSourceB);
      System.out.println(lucene);
      // SUT
      SearchResult result = ServiceProvider.getSearchService().search(lucene, 10, base, null);
      assertEquals(1, result.getDocuments().size());
   }
   
   /**
    * Teste le caractère joker mono caractère : "?"
    */
   @Test
   public void wildcard_mono() {
      docGen.setTitle("azerty");
      docGen.store();
      //String lucene = String.format("%s:az?rty", appliSourceFName);
      String lucene = String.format("%s:?????", appliSourceFName);
      // SUT
      SearchResult result = ServiceProvider.getSearchService().search(lucene, 10, base, null);
      assertEquals(1, result.getDocuments().size());
      assertEquals(docGen.getTitle(), result.getDocuments().get(0).getFirstCriterion(appliSourceCategory).getWord());
   }   
}
