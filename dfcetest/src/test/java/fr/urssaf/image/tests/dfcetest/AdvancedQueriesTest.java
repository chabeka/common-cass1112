/**
 * 
 */
package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.exception.ged.SearchQueryParseException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

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
      
      base = sp.getBaseAdministrationService().getBase(BASE_ID);
      docGen = new DocGen(base);      
      
      appliSourceCategory = base.getBaseCategory(Categories.APPLI_SOURCE.toString());
      appliSourceFName = appliSourceCategory.getFormattedName();
      
      integerCategory = base.getBaseCategory(Categories.INTEGER.toString());
      intergerFName = integerCategory.getFormattedName(); 
      
      boolCategory = base.getBaseCategory(Categories.BOOLEAN.toString());
      boolFName = boolCategory.getFormattedName(); 
   }
   
   @After
   public void teardown() throws FrozenDocumentException {
      for (Document doc : DocGen.storedDocuments) {
         sp.getStoreService().deleteDocument(doc.getUuid());
      }
   }
   
   /**
    * Requête booléenne de recherche simple : c1=x AND c2=y
    */
   @Test
   public void AQ1() throws Exception {
      // Document A
      String titleA = docGen.setRandomTitle("AQ1A").getTitle();
      docGen.put(Categories.INTEGER, 10);
      Document docA = docGen.store();
      
      // Document B
      docGen.setRandomTitle("AQ1B").store();
      
      String lucene = String.format("%s:%s", intergerFName, 10);
      SearchResult result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(2, result.getDocuments().size());
      
      lucene = String.format("%s:%s", appliSourceFName, titleA);
      result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());

      // SUT
      lucene = String.format("%s:%s AND %s:%s", intergerFName, 10, appliSourceFName, titleA);
      result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(docA, result.getDocuments().get(0));      
   }
   
   /**
    * Requête c1>date
    */
   @Test
   public void date_superieur() throws Exception {
      // Document A
      String titleA = docGen.setRandomTitle("date_superieur").getTitle();
      docGen.put(Categories.DATE, new Date(2012, 1, 1));
      Document docA = docGen.store();

   
      String lucene = "Date>2011-12-31";
      SearchResult result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(docA, result.getDocuments().get(0));      
   }   
   
   /**
    * Requête de type (c1=x OR c2=y) AND (c3 = z)
    */
   @Test
   public void AQ2() throws Exception {
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
      SearchResult result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(2, result.getDocuments().size());
      
      lucene = String.format("%s:%s", appliSourceFName, titleA);
      result = sp.getSearchService().search(lucene, 100, base, null);
      Document d = result.getDocuments().get(0);
      assertEquals(1, result.getDocuments().size());      

      lucene = String.format("%s:%s OR %s:%s", intergerFName, 10, appliSourceFName, titleA);
      result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(2, result.getDocuments().size()); 
      
      // SUT
      lucene = String.format("(%s:%s OR %s:%s) AND %s:%s", 
            intergerFName, 10, appliSourceFName, titleA, boolFName, true);
      result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size()); 
      assertDocumentEquals(docB, result.getDocuments().get(0));
      
      // SUT
      lucene = String.format("(%s:%s OR %s:%s) AND %s:%s", 
            intergerFName, 10, appliSourceFName, titleA, boolFName, false);
      result = sp.getSearchService().search(lucene, 100, base, null);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(docA, result.getDocuments().get(0));
   }

   /**
    * Requête contenant un joker * avec une valeur de catégorie contenant un underscore. 
    * Le underscore est important car il est mal géré par Docubase.
    */
   @Test
   public void AQ4()  throws Exception {
      String firstPartTitle = DocubaseHelper.randomAlphaNum(10); 
      Document injectedDoc = docGen.setRandomTitle(firstPartTitle).store();      
      log.debug("AQ4, Doc inséré -> " + injectedDoc.getUuid().toString());
      
      Document docbyUUID = sp.getSearchService().getDocumentByUUIDMultiBase(injectedDoc.getUuid());
      assertDocumentEquals(injectedDoc, docbyUUID);
      
      String lucene = appliSourceFName + ":" + docGen.getTitle();
      SearchResult result = sp.getSearchService().search(lucene, 100, base);
      assertEquals(1, result.getDocuments().size());
      assertDocumentEquals(injectedDoc, result.getDocuments().get(0));
      
      // SUT
      lucene = String.format("%s:%s*", appliSourceFName, firstPartTitle);
      result = sp.getSearchService().search(lucene, 100, base);
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
   public void AQ5()  throws Exception {
      int nbDocsA = 17;
      String appliSourceA = docGen.setRandomTitle("AQ5A").getTitle();
      docGen.storeMany(nbDocsA);

      int nbDocsB = 13;
      String appliSourceB = docGen.setRandomTitle("AQ5B").getTitle();
      docGen.storeMany(nbDocsB);      
      String lucene = String.format("%s:%s AND %s:%s", 
            appliSourceFName, appliSourceA, appliSourceFName, appliSourceB);
      // On fixe une limite de recherche plus grande pour voir si on ne ramène pas plus de
      // résultats que prévu
      int searchLimit = 2 * (nbDocsA + nbDocsB);
      // SUT
      SearchResult result = sp.getSearchService().search(lucene, searchLimit, base, null);
      assertEquals(0, result.getDocuments().size());
   }
   
   /**
    * Teste qu'une valeur peut contenir des espaces.
    */
   @Test
   public void spaces() throws Exception {
      String appliSourceB = docGen.setRandomTitle("un titre ").getTitle();
      docGen.store();
      String lucene = String.format("%s:\"%s\"", appliSourceFName, appliSourceB);
      System.out.println(lucene);
      // SUT
      SearchResult result = sp.getSearchService().search(lucene, 10, base, null);
      assertEquals(1, result.getDocuments().size());
   }
   
   /**
    * Teste le caractère joker mono caractère : "?"
    * Lève une exception depuis la version 1.0.0 alpha de Docubase car non supporté
    */
   @Test(expected=SearchQueryParseException.class)
   public void wildcard_mono() throws Exception {
      docGen.setTitle("azerty");
      docGen.store();
      String lucene = String.format("%s:az?rty", appliSourceFName);
      // SUT
      SearchResult result = sp.getSearchService().search(lucene, 10, base, null);
      assertEquals(1, result.getDocuments().size());
      assertEquals(docGen.getTitle(), result.getDocuments().get(0).getFirstCriterion(appliSourceCategory).getWord());
   }   
   
   
   /**
    * Teste qu'un wildcard est interdit en tant que premier caractère
    */
   @Test(expected=SearchQueryParseException.class)
   public void wildcard_first() throws Exception {
      docGen.setTitle("azerty");
      docGen.store();
      //String lucene = String.format("%s:az?rty", appliSourceFName);
      String lucene = String.format("%s:?????", appliSourceFName);
      // SUT
      SearchResult result = sp.getSearchService().search(lucene, 10, base, null);
      assertEquals(1, result.getDocuments().size());
      assertEquals(docGen.getTitle(), result.getDocuments().get(0).getFirstCriterion(appliSourceCategory).getWord());
   }     
   
   /**
    * Peut-on valoriser une catégorie par une chaine vide ? 
    * Réponse : Non => TagControlException
    * @throws Exception
    */
   @Test(expected=TagControlException.class)
   public void emptyStringMetadata() throws Exception {
      docGen.setTitle("");
      docGen.store();
   }  
}
