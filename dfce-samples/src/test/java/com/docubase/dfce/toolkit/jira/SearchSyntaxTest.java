package com.docubase.dfce.toolkit.jira;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.search.SearchResult;

import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.TestUtils;

/**
 * Teste l'extraction d'un document en contexte mono et multi thread.
 * 
 */
public class SearchSyntaxTest extends AbstractTestBase {
   private static final String DOC1_NAME = "doc1";
   private static final String DOC2_NAME = "doc2";
   public static final String APR = "apr";
   public static final String DEN = "den";
   public static final String RND = "rnd";

   public static final String BASE_NAME = SearchSyntaxTest.class.getSimpleName();

   public static Base searchSyntaxTestBase;
   private static Category categoryAPR;
   private static Category categoryDEN;
   private static Category categoryRND;

   @BeforeClass
   public static void prepareTestData() throws ObjectAlreadyExistsException, FileNotFoundException,
         TagControlException {
      serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);

      categoryAPR = serviceProvider.getStorageAdministrationService().findOrCreateCategory(APR,
            CategoryDataType.STRING);
      categoryDEN = serviceProvider.getStorageAdministrationService().findOrCreateCategory(DEN,
            CategoryDataType.STRING);
      categoryRND = serviceProvider.getStorageAdministrationService().findOrCreateCategory(RND,
            CategoryDataType.STRING);

      Base newBase = serviceProvider.getBaseAdministrationService().getBase(BASE_NAME);
      if (newBase != null) {
         serviceProvider.getBaseAdministrationService().deleteBase(newBase);
      }

      newBase = ToolkitFactory.getInstance().createBase(SearchSyntaxTest.class.getSimpleName());

      BaseCategory baseCategoryAPR = ToolkitFactory.getInstance().createBaseCategory(categoryAPR,
            true);
      BaseCategory baseCategoryDEN = ToolkitFactory.getInstance().createBaseCategory(categoryDEN,
            true);
      BaseCategory baseCategoryRND = ToolkitFactory.getInstance().createBaseCategory(categoryRND,
            true);

      newBase.addBaseCategory(baseCategoryAPR);
      newBase.addBaseCategory(baseCategoryDEN);
      newBase.addBaseCategory(baseCategoryRND);

      searchSyntaxTestBase = serviceProvider.getBaseAdministrationService().createBase(newBase);

      createDocument(searchSyntaxTestBase, "GED", "MR YALCIN KEREM", "2.2.3.2.2", DOC1_NAME);
      createDocument(searchSyntaxTestBase, "ADELAIDE", "MR ALBERT CAMUS", "2.2.3.2.1", DOC2_NAME);
   }

   private static void createDocument(Base base, String apr, String den, String rnd, String docName)
         throws TagControlException, FileNotFoundException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(categoryAPR.getName(), apr);
      document.addCriterion(categoryDEN.getName(), den);
      document.addCriterion(categoryRND.getName(), rnd);
      File file = TestUtils.getFile("doc1.pdf");

      serviceProvider.getStoreService().storeDocument(document, docName, "pdf",
            new FileInputStream(file));
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testQMark() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:G?", 100, searchSyntaxTestBase);
   }

   // Pas de probl�me, la valeur est entre ""
   @Test
   public void testQuotedQMark() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("apr:\"G?\"", 100,
            searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // Pas de probl�me, le '?' est enchapp�, mais la requ�te ne donne pas de
   // r�sultat (il faudrait que l'index ait la valeur G?)
   @Test
   public void testEscapedQMark() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("apr:G\\?", 100,
            searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // Pas de probl�me le '.' n'est pas un caract�re prot�g�
   @Test
   public void testRND() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("rnd:2.2.3.2.2", 100,
            searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testMiddleQMark() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:G?D", 100, searchSyntaxTestBase);
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testMiddleQMarkAndJoker() throws ExceededSearchLimitException,
         SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:G?*", 100, searchSyntaxTestBase);
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testLeadingQMark() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:?ED", 100, searchSyntaxTestBase);
   }

   // la terme est bien entres "", mais la requ�te ne donne pas de r�sultat (il
   // faudrait que l'index ait la valeur G*)
   @Test
   public void testQuotedJoker() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:\"G*\"", 100, searchSyntaxTestBase);
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testMiddleQuestionMark() throws ExceededSearchLimitException,
         SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:G?D", 100, searchSyntaxTestBase);
   }

   // '*' non autoris� en d�but de terme
   @Test(expected = SearchQueryParseException.class)
   public void testLeadingJoker() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:*ED", 100, searchSyntaxTestBase);
   }

   // la requ�te est syntaxiquement correcte mais il n'y as pas de r�sultat
   @Test
   public void testEndingJoker() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("apr:TeSt*", 100,
            searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // test en version minuscule
   @Test
   public void testLowerCase() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("apr:ged", 100,
            searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // test en version minuscule avec joker
   @Test
   public void testLowerCaseWithJoker() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("apr:ge*", 100,
            searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // requete non autoris�e parce que uniquement '*'
   @Test(expected = SearchQueryParseException.class)
   public void testOnlyJoker() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr:*", 100, searchSyntaxTestBase);
   }

   // requ�te multi-terme, le moteur prend den:MR mais YALCIN KEREM ne sont pas
   // pr�fix�s -> erreur de syntaxe
   @Test(expected = SearchQueryParseException.class)
   public void testUnquotedMultiTerm() throws ExceededSearchLimitException,
         SearchQueryParseException {
      serviceProvider.getSearchService().search("den : MR YALCIN KEREM", 100, searchSyntaxTestBase);
   }

   // requete multi-terme entre "", pas de probl�me
   @Test
   public void testQuotedMultiTerm() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den : \"MR YALCIN KEREM\"", 100, searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // les ' ' sont enchapp�s, pas de probl�me
   @Test
   public void testEscapedMultiTerm() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den : MR\\ YALCIN\\ KEREM", 100, searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // les ' ' sont enchapp�s, pas de probl�me, version '*'
   @Test
   public void testEscapedMultiTermWithJoker() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den : MR\\ YALCIN\\ KE*", 100, searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testMultiTermLeadingQMark() throws ExceededSearchLimitException,
         SearchQueryParseException {
      serviceProvider.getSearchService().search("den:?MR YALCIN KEREM", 100, searchSyntaxTestBase);
   }

   // non autoris� parce qu'il y a un '?'
   @Test(expected = SearchQueryParseException.class)
   public void testMultiTermEndingQMark() throws ExceededSearchLimitException,
         SearchQueryParseException {
      serviceProvider.getSearchService().search("den:MR YALCIN KEREM ?", 100, searchSyntaxTestBase);
   }

   // le terme est entres "", mais la valeur de l'index cherch� est incorrecte
   // cherch� : "MR *YALCIN KEREM", index� : "MR YALCIN KEREM"
   @Test
   public void testQuotedMultiTermWithJoker() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den:\"MR *YALCIN KEREM\"", 100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // non support� car le '*' n'est pas � la fin
   @Test(expected = SearchQueryParseException.class)
   public void testMultiTermWithJokerEscapedSpaces() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den:MR\\ *YALCIN\\ KEREM", 100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // le terme est entres "", mais la valeur de l'index cherch� est incorrecte
   // cherch� : "MR YALCIN KEREM? ", index� : "MR YALCIN KEREM"
   @Test
   public void testQuotedMultiTermWithQMark() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den:\"MR YALCIN KEREM? \"", 100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // le terme est entres "", mais la valeur de l'index cherch� est incorrecte
   // cherch� : "\\MR YALCIN KEREM? \\", index� : "MR YALCIN KEREM"
   @Test
   public void testQuotedMultiTermWithBSlash() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "den:\"\\\\MR YALCIN KEREM? \\\\\"", 100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // requ�te multi-terme, le moteur prend den:MR mais *YALCIN KEREM ne sont
   // pas pr�fix�s -> erreur de syntaxe
   @Test(expected = SearchQueryParseException.class)
   public void testUnquotedMultiTermWithJoker() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("den:MR *YALCIN KEREM",
            100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // le terme est entres "", mais la valeur de l'index cherch� est incorrecte
   // cherch� : "2.2.?.2.2", index� : "2.2.3.2.2"
   @Test
   public void testQuotedQMarkRND() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search("rnd:\"2.2.?.2.2\"",
            100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // ici le and est en minuscules, il n'est est consid�r� comme un terme, mais
   // il n'est pas pr�fix�, d'o� l'erreur de syntaxe
   @Test(expected = SearchQueryParseException.class)
   public void testLowerCaseAnd() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("apr : ged and rnd:2.2.3.2.2", 100,
            searchSyntaxTestBase);
   }

   // le probl�me est r�solu en passant le and en majuscules
   @Test
   public void testUpperCaseAnd() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "apr : ged AND rnd:2.2.3.2.2", 100, searchSyntaxTestBase);
      assertEquals(1, searchResult.getTotalHits());
      assertEquals(DOC1_NAME, searchResult.getDocuments().get(0).getFilename());
   }

   // ici le and est en minuscules, il n'est est consid�r� comme un terme, mais
   // il n'est pas pr�fix�, d'o� l'erreur de syntaxe
   @Test(expected = SearchQueryParseException.class)
   public void testLowerCaseAndWithQMark() throws ExceededSearchLimitException,
         SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "apr : ged and rnd:2.2.3.2.?", 100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // seul le second terme est pr�fix� d'un '+', il ne tien pas compte du
   // premier
   @Test
   public void testPlus() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "rnd:2.2.3.2.2 OR + rnd:2.2.3.2.1", 100, searchSyntaxTestBase);
      assertEquals(2, searchResult.getTotalHits());
   }

   // ici, on pr�fixe les deux termes d'un '+', aucun document ne comporte les
   // deux indexes -> pas de r�sultats
   @Test
   public void testPlusAndPlus() throws ExceededSearchLimitException, SearchQueryParseException {
      SearchResult searchResult = serviceProvider.getSearchService().search(
            "+rnd:2.2.3.2.2 + rnd:2.2.3.2.1", 100, searchSyntaxTestBase);
      assertEquals(0, searchResult.getTotalHits());
   }

   // requete multiterme non autoris�e
   @Test(expected = SearchQueryParseException.class)
   public void testPlusMultiTerm() throws ExceededSearchLimitException, SearchQueryParseException {
      serviceProvider.getSearchService().search("rnd:2.2.3.2.2 + 2.2.3.2.1", 100,
            searchSyntaxTestBase);
   }
}
