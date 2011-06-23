package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.Base.DocumentCreationDateConfiguration;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.ChainedFilter.ChainedFilterOperator;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;
import net.docubase.toolkit.service.ged.SearchService.MetaData;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.docubase.dfce.toolkit.base.AbstractBaseTestCase;

@RunWith(JUnit4.class)
public class RichGedTest extends AbstractBaseTestCase {
   // private Logger logger = Logger.getLogger(RichGedTest.class);

   /* Nom de la base GED pour ce test */
   private static final String BASEID = "RICHGED";

   private static final String URL = "http://cer69-ds4int:8080/dfce-webapp/toolkit/";

   public final static Double pi = Double.valueOf("3.1415926535");

   private static final String[] catNames = { "Cat�gorie z�ro", "Cat�gorie un", "Cat�gorie deux",
         "Cat bool�enne", "Cat enti�re", "Cat d�cimale", "Cat date", "Cat date et heure" };

   private static ToolkitFactory toolkitFactory;

   @BeforeClass
   public static void before() {
      Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
      base = deleteAndCreateBaseThenStarts();
      toolkitFactory = ToolkitFactory.getInstance();
   }

   @AfterClass
   public static void after() {
      deleteBase(base);
      Authentication.closeSession();
   }

   protected static Base deleteAndCreateBaseThenStarts() {
      Base base = ServiceProvider.getBaseAdministrationService().getBase(BASEID);
      if (base != null) {
         deleteBase(base);
      }
      base = createBase();

      ServiceProvider.getBaseAdministrationService().startBase(base);

      return base;
   }

   protected static File getFile(String fileName) {
      Class<?> clazz = RichGedTest.class;
      return getFile(fileName, clazz);
   }

   private static Base createBase() {
      ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
      StorageAdministrationService storageAdministrationService = ServiceProvider
            .getStorageAdministrationService();

      Base base = toolkitFactory.createBase(BASEID);

      base.setDescription("My-Ged-Is-Rich");

      // D�clare une date de cr�ation disponible mais optionnell
      base.setDocumentCreationDateConfiguration(DocumentCreationDateConfiguration.OPTIONAL);
      // Pas de fond de page
      base.setDocumentOverlayFormConfiguration(Base.DocumentOverlayFormConfiguration.NONE);
      // Pas de groupe de document
      base.setDocumentOwnerDefault(Base.DocumentOwnerType.PUBLIC);
      // Le propri�taire d'un document n'est pas modifiable � post�riori de
      // son injection
      base.setDocumentOwnerModify(false);
      /*
       * Masque de titre. C'est encore maintenu en DS4, m�me si maintenant on
       * peut remonter les valeurs de cat�gorie dans les listes de solution sans
       * avoir besoin de cet artifice.
       */
      base.setDocumentTitleMask("C0+\" < \"+C1");
      // taille maximum d'un titre
      base.setDocumentTitleMaxSize(255);
      // Impossible de modifier un titre � post�riori
      base.setDocumentTitleModify(false);
      base.setDocumentTitleSeparator(">");

      Category category0 = storageAdministrationService.findOrCreateCategory(catNames[0],
            CategoryDataType.STRING);
      Category category1 = storageAdministrationService.findOrCreateCategory(catNames[1],
            CategoryDataType.STRING);
      Category category2 = storageAdministrationService.findOrCreateCategory(catNames[2],
            CategoryDataType.STRING);
      Category categoryBoolean = storageAdministrationService.findOrCreateCategory(catNames[3],
            CategoryDataType.BOOLEAN);
      Category categoryInteger = storageAdministrationService.findOrCreateCategory(catNames[4],
            CategoryDataType.INTEGER);
      Category categoryDecimal = storageAdministrationService.findOrCreateCategory(catNames[5],
            CategoryDataType.DOUBLE);
      Category categoryDate = storageAdministrationService.findOrCreateCategory(catNames[6],
            CategoryDataType.DATE);

      BaseCategory baseCategory0 = toolkitFactory.createBaseCategory(category0, true);
      baseCategory0.setMinimumValues((short) 1);
      baseCategory0.setSingle(true);
      base.addBaseCategory(baseCategory0);

      BaseCategory baseCategory1 = toolkitFactory.createBaseCategory(category1, true);
      baseCategory1.setEnableDictionary(false);
      base.addBaseCategory(baseCategory1);

      BaseCategory baseCategory2 = toolkitFactory.createBaseCategory(category2, true);
      baseCategory2.setMaximumValues((short) 10);
      baseCategory2.setEnableDictionary(false);
      base.addBaseCategory(baseCategory2);

      BaseCategory baseCategoryBoolean = toolkitFactory.createBaseCategory(categoryBoolean, true);
      baseCategoryBoolean.setMaximumValues((short) 10);
      baseCategoryBoolean.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryBoolean);

      BaseCategory baseCategoryInteger = toolkitFactory.createBaseCategory(categoryInteger, true);
      baseCategoryInteger.setMaximumValues((short) 10);
      baseCategoryInteger.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryInteger);

      BaseCategory baseCategoryDecimal = toolkitFactory.createBaseCategory(categoryDecimal, true);
      baseCategoryDecimal.setMaximumValues((short) 10);
      baseCategoryDecimal.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryDecimal);

      BaseCategory baseCategoryDate = toolkitFactory.createBaseCategory(categoryDate, true);
      baseCategoryDate.setMaximumValues((short) 10);
      baseCategoryDate.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryDate);

      try {
         ServiceProvider.getBaseAdministrationService().createBase(base);
      } catch (ObjectAlreadyExistsException e) {
         e.printStackTrace();
         fail("base : " + base.getBaseId() + " already exists");
      }

      /*
       * On va, alors qu'il n'y a aucun document, modifier la base pour y
       * ajouter la derni�re cat�gorie
       */
      Category categoryDateHeure = storageAdministrationService.findOrCreateCategory(catNames[7],
            CategoryDataType.DATETIME);

      BaseCategory baseCategoryDateHeure = toolkitFactory.createBaseCategory(categoryDateHeure,
            true);
      baseCategoryDateHeure.setMaximumValues((short) 10);
      baseCategoryDateHeure.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryDateHeure);

      ServiceProvider.getBaseAdministrationService().updateBase(base);

      return base;
   }

   private void control(Document doc, File newDoc, String c0) throws IOException {
      // L'instance de doc � ce stade contient le documentInformation
      assertNotNull(doc);

      // getCriterionList renvoie la liste des cat�gories C0, C1, etc...
      List<Criterion> criterionList = doc.getCriteria(base.getBaseCategory(catNames[0]));
      assertEquals(1, criterionList.size());
      assertEquals(c0, criterionList.get(0).getWord());

      // Une autre v�rification sur le tag : les 3 valeurs d�cimales sur C5
      // On va juste chercher si l'une d'entre elle est Pi.
      List<Criterion> c5s = doc.getCriteria(base.getBaseCategory(catNames[5]));
      assertEquals(3, c5s.size());
      boolean foundPi = false;
      for (int i = 0; i < c5s.size() && !foundPi; i++) {
         foundPi = c5s.get(i).getWord().equals(pi);
      }
      assertTrue(foundPi);

      // Cet appel, la 1�re fois (lazy loading) va extraire le document.
      InputStream documentFile = ServiceProvider.getStoreService().getDocumentFile(doc);
      // le document extrait (dans une zone temporaire) doit avoir la m�me
      // taille que le document utilis� avant injection.

      assertEquals(DigestUtils.shaHex(new FileInputStream(newDoc)),
            DigestUtils.shaHex(documentFile));
      documentFile.close();
   }

   /**
    * On va retrouver les documents stock�s avec des requ�tes GRC portant sur
    * les cat�gories typ�es. Puis avec les requ�tes Lucene
    * 
    * @throws ExceededSearchLimitException
    * 
    * @throws IOException
    */
   @Test
   public void testTyped() throws ExceededSearchLimitException {
      ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

      BaseCategory c0 = base.getBaseCategory(catNames[0]);
      BaseCategory cBoolean = base.getBaseCategory(catNames[3]);

      Document document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(c0, "MyBooleanTest");
      document.addCriterion(cBoolean, true);

      storeDoc(document, getFile("doc1.pdf"), true);

      /*
       * On injecte le nom du champs filtr� par formatFieldName dans la requ�te
       */
      String c3FormattedName = cBoolean.getFormattedName();
      assertEquals(1, searchLucene(c3FormattedName + ":true", 5));
      assertEquals(0, searchLucene(c3FormattedName + ":false", 5));

      // Integer
      BaseCategory baseCategoryInteger = base.getBaseCategory(catNames[4]);
      document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(c0, "MyIntegerTest");
      document.addCriterion(baseCategoryInteger, 10);

      storeDoc(document, getFile("doc1.pdf"), true);

      String c4FormattedName = baseCategoryInteger.getFormattedName();
      assertEquals(1, searchLucene(c4FormattedName + ":10", 5));
      assertEquals(0, searchLucene(c4FormattedName + ":11", 5));

      // Decimal
      BaseCategory decimalBaseCategory = base.getBaseCategory(catNames[5]);
      document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(c0, "MyDecimalTest");
      document.addCriterion(decimalBaseCategory, 3.14);

      storeDoc(document, getFile("doc1.pdf"), true);

      String c5FName = decimalBaseCategory.getFormattedName();
      assertEquals(1, searchLucene(c5FName + ":3.14", 5));
      assertEquals(0, searchLucene(c5FName + ":3.1459", 5));

      // Date et DateHeure
      Date currDate = new Date();
      String strDate = ServiceProvider.getSearchService().formatDate(currDate, DateFormat.DATE);
      String strDateTime = ServiceProvider.getSearchService().formatDate(currDate,
            DateFormat.DATETIME);
      BaseCategory dateBaseCategory = base.getBaseCategory(catNames[6]);

      document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(c0, "MyDateTest");
      document.addCriterion(dateBaseCategory, currDate);

      storeDoc(document, getFile("doc1.pdf"), true);

      String c6FName = dateBaseCategory.getFormattedName();
      assertEquals(1, searchLucene(c6FName + ":" + strDate, 5));
      assertEquals(0, searchLucene(c6FName + ":1975-01-01", 5));

      BaseCategory dateTimeBaseCategory = base.getBaseCategory(catNames[7]);
      document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(c0, "MyDateTimeTest");
      document.addCriterion(dateTimeBaseCategory, currDate);
      storeDoc(document, getFile("doc1.pdf"), true);

      /*
       * Requ�tes un peu plus compliqu�es
       */
      String lucene = null;

      /*
       * En Lucene sans le strDateTime
       */
      lucene = "(" + c3FormattedName + ":true OR " + c4FormattedName + ":10" + " OR " + c5FName
            + ":3.14 OR " + c6FName + ":" + strDate + ")";

      assertEquals(4, searchLucene(lucene, 10));

      String c0FName = c0.getFormattedName();
      lucene = c0FName + ":My*";
      assertEquals(5, ServiceProvider.getSearchService().search(lucene, 100, base).getTotalHits());

      lucene = c0FName + ":My*Test";
      assertEquals(5, searchLucene(lucene, 10));

      /*
       * Nouveaut� ici : on recherche "Cat 7 - DateHeure" qui doit �tre �gale �
       * par exemple "1975-01-01 08-32". Le nom de la cat�gorie doit tjs �tre
       * prot�g�. Mais on doit aussi mettre des "" autour du terme recherch�.
       * Pour obtenir des requ�tes du type cat�7�-�dateheure:"1975-01-01 08-32"
       * 
       * Il faut donc prot�ger le caract�re "
       * 
       * Nouveaut�: on peut directement r�cup�rer le nom formatt�.
       */
      String c7Name = dateTimeBaseCategory.getFormattedName();
      assertEquals(0, searchLucene(c7Name + ":\"1975-01-01 08-32\"", 5));
      assertEquals(1, searchLucene(c7Name + ":\"" + strDateTime + "\"", 5));
   }

   /**
    * On stocke un document en pr�cisant un UUID, et on v�rifie que l'on le
    * r�cup�re bien dans la liste de solution (DocumentInformation) et dans le
    * tag.
    * 
    * @throws ExceededSearchLimitException
    * 
    * @throws IOException
    */
   @Test
   public void testProvidedUUID() throws ExceededSearchLimitException {
      ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

      BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);

      /*
       * On fournit explicitement un UUID
       */
      UUID uuidFourni = UUID.randomUUID();
      Document document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(baseCategory0, "UUIDFourni");
      document.setUuid(uuidFourni);
      storeDoc(document, getFile("doc1.pdf"), true);

      /*
       * On recherche le document par sa cat�gorie C0:UUIDFourni
       */
      List<Document> docs = ServiceProvider.getSearchService()
            .search(baseCategory0.getFormattedName() + ":UUIDFourni", 5, base).getDocuments();

      assertTrue(docs != null && docs.size() == 1);
      Document doc = docs.get(0);
      assertEquals(uuidFourni.toString(), doc.getUuid().toString());

      /*
       * On recherche �galement par cet UUIDFourni Comme c'est un champs
       * statique du tag c'est dans LucRef.
       */
      docs = ServiceProvider.getSearchService()
            .search(MetaData.UUID.getFormattedName() + ":" + uuidFourni.toString(), 5, base)
            .getDocuments();
      assertTrue(docs != null && docs.size() == 1);
      doc = docs.get(0);
   }

   /**
    * On stocke un document sans pr�ciser d'UUID et on v�rifie qu'il n'y en a
    * bien eu un g�n�r� et qu'il est r�cup�r� � l'identique dans le
    * DocumentInformation et le Tag
    * 
    * @throws ExceededSearchLimitException
    * 
    * @throws IOException
    */
   @Test
   public void testNoProvidedUUID() throws ExceededSearchLimitException {
      ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

      BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);

      /*
       * On fournit explicitement un UUID
       */
      Document document = toolkitFactory.createDocumentTag(base);
      document.addCriterion(baseCategory0, "UUIDNonFourni");
      storeDoc(document, getFile("doc1.pdf"), true);

      /*
       * On recherche le document
       */
      List<Document> docs = ServiceProvider.getSearchService()
            .search(baseCategory0.getFormattedName() + ":UUIDNonFourni", 5, base).getDocuments();
      assertTrue(docs != null && docs.size() == 1);
      Document doc = docs.get(0);
      /*
       * On v�rifie dans le documentInformation, dans Tag, et on compare
       */
      assertNotNull(doc.getUuid());
   }

   /**
    * Ce test montre des requ�tes cons�cutives en jouant sur l'offsetInIndex de
    * d�part. Ainsi on peut en plusieurs requ�tes successives peut couteuses
    * remonter une grosse liste de solution en faisant varier cet offsetInIndex
    * de d�part.
    * 
    * @throws ExceededSearchLimitException
    * 
    * @throws IOException
    */
   @Test
   public void testSearchWithOffset() throws ExceededSearchLimitException {
      BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
      BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);

      /*
       * On va stocker 100 documents, avec C0 qui change � chaque fois et C1 qui
       * contient le nom du test.
       */
      for (int i = 0; i < 100; i++) {
         Document document = toolkitFactory.createDocumentTag(base);
         // C0 unique
         document.addCriterion(baseCategory0, "TestOffset" + i);
         // C1 ne bouge pas
         document.addCriterion(baseCategory1, "TestOffset");

         // stockage
         storeDoc(document, getFile("doc1.pdf"), true);
      }

      /*
       * On a donc en th�orie 100 documents avec C1=TestOffset.
       * 
       * Si on demande 100 documents, alors apr�s avoir effectu� la requ�te,
       * l'AMF va lire sur l'index Lucene 100 tags.
       * 
       * On peut d�cider d'en demander seulement 10 par exemple (c'est � dire
       * avoir une liste de solution sur les 10 premiers), et de pouvoir en
       * demander 10 de plus.
       * 
       * Ensuite, on d�cide d'extraire ou non selon ce que nous dit notre
       * morceaux de liste de solution.
       * 
       * On souhaite donc dire
       * "je veux les 10 premiers �l�ments de la liste de solution",
       * "je veux les 10 suivants", "je veux les 10 suivants".
       * 
       * A chaque fois, Lucene restituera des "documents Lucene" dans le m�me
       * ordre. L'AMF choisira de ne lire (depuis l'index Lucene) qu'entre
       * offsetInIndex et offsetInIndex + limit.
       * 
       * L'API toolkit fourni une recherche avec offsetInIndex + limit.
       */
      Set<UUID> numDocMet = new HashSet<UUID>();
      String queryText = baseCategory1.getFormattedName() + ":TestOffset";
      for (int i = 0; i < 10; i++) {
         int offset = i * 10;

         /*
          * L'objet r�sult contient une liste de Solution remont�e (ici ce sera
          * 10 maximum) et le nombre th�orique de documents v�rifiant la requ�te
          * (ici ce sera tjs 100)
          */
         SearchResult searchResult = ServiceProvider.getSearchService().search(queryText, 10,
               offset, base, null);
         assertEquals(searchResult.getTotalHits(), 100);
         assertNotNull(searchResult.getDocuments());
         assertEquals(10, searchResult.getDocuments().size());
         for (Document document : searchResult.getDocuments()) {
            // On se rend compte que l'on ne rencontre chaque document
            // qu'une seule fois
            // dans toutes les it�rations de recherche.
            assertTrue(numDocMet.add(document.getUuid()));
         }
      }
      assertEquals(100, numDocMet.size());
   }

   /**
    * Ce test montre l'utilisation des FilterTerm (en mode AND) et aussi la
    * requete CompleteQuery qui expose le maximum de choses
    * 
    * @throws ExceededSearchLimitException
    * 
    * @throws IOException
    */
   @Test
   public void testCompleteQueryScenarii() throws ExceededSearchLimitException {
      System.out.println("****** testCompleteQueryScenarii ******");
      BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
      BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
      BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);
      BaseCategory baseCategoryAge = base.getBaseCategory(catNames[4]);

      /*
       * On va stocker n documents, avec C0 qui change � chaque fois, et C1 qui
       * prend peu de valeurs. On joue aussi avec C2 multivalu�;)
       */

      for (int i = 0; i < 50; i++) {
         Document document = toolkitFactory.createDocumentTag(base);

         // C0 unique
         document.addCriterion(baseCategory0, "testfilter" + i);

         // C1 soit Enfant, soit Adulte
         String c1Val = null;
         if (i < 10) { // Les enfants d'abord
            c1Val = "enfant";
            document.addCriterion(baseCategoryAge, i % 2 == 0 ? "3" : "7");
         } else {
            c1Val = "adulte";
            document.addCriterion(baseCategoryAge, i % 2 == 0 ? "23" : "47");
         }
         document.addCriterion(baseCategory1, c1Val);

         // C2. 2 valeurs, une qui varie tr�s peu, une qui est unique.
         document.addCriterion(baseCategory2, "personne" + i);
         document.addCriterion(baseCategory2, i % 2 == 0 ? "masculin" : "feminin");

         // stockage
         storeDoc(document, getFile("doc1.pdf"), true);
      }

      // Recherche sans filtre pour v�rifier le nombre de documents stock�s
      // assertEquals( 50, searchLucene(c0.getFormattedName()+":testfilter*",
      // 1000));

      /*
       * On veut tout les adultes de sexe masculin. Cel� doit repr�senter 20
       * personnes.
       * 
       * On a plusieurs fa�ons de chercher. On va consid�rer ici qu'il n'y a que
       * les documents pr�c�demment inject�s (on n'applique plus TestFilter* sur
       * C0) On va chercher � la fois sur C1 et C2.
       * 
       * 1/ On recherche C1=Adulte ET C2=Masculin sans utiliser les filtres.
       * 
       * 2/ On recherche C1=Adulte et filtre sur C2.
       * 
       * 3/ On recherche C2=Masculin et filtre sur C1.
       * 
       * 4/ On recherche avec filtre C1 et C2 (on doit donc avec une requ�te
       * lambda : C0:TestFilter* par exemple)
       */
      // 1/
      String query = baseCategoryAge.getFormattedName() + ":3";
      assertEquals(5, searchLucene(query, 1000, null));

      // 2/
      query = baseCategory1.getFormattedName() + ":adulte AND " + baseCategory2.getFormattedName()
            + ":masculin";
      // query = "_bUUID:" + base.getDescription().getUUID().toString();
      ChainedFilter chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addTermFilter(baseCategory2.getFormattedName(), "masculin");
      assertEquals(20, searchLucene(query, 1000, chainedFilter));

      // 3/
      query = baseCategory2.getFormattedName() + ":masculin";
      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
            ChainedFilterOperator.AND);
      // assertEquals( 20, searchLucene(query, 1000, new
      // FilterTerm(c1.getFormattedName(), "adulte" )));
      assertEquals(20, searchLucene(query, 1000, chainedFilter));

      // 4/
      query = baseCategory0.getFormattedName() + ":testfilter*";
      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
            ChainedFilterOperator.AND).addTermFilter(baseCategory2.getFormattedName(), "masculin",
            ChainedFilterOperator.AND);
      // assertEquals( 20, searchLucene(query, 1000,
      // new FilterTerm(c1.getFormattedName(), "adulte" ),
      // new FilterTerm(c2.getFormattedName(), "masculin" )
      // )
      // );

      assertEquals(20, searchLucene(query, 1000, chainedFilter));

      // 5/
      query = baseCategory1.getFormattedName() + ":enfant";
      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addIntRangeFilter(baseCategoryAge.getFormattedName(), 0, 5, true, true,
            ChainedFilterOperator.AND);
      // assertEquals( 5, searchLucene(query, 1000, new
      // FilterTerm(age.getFormattedName(), "[ 0 TO 5 ]" )));
      assertEquals(5, searchLucene(query, 1000, chainedFilter));

      // 6/
      query = baseCategory1.getFormattedName() + ":adulte";
      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addIntRangeFilter(baseCategoryAge.getFormattedName(), 23, 30, true, true,
            ChainedFilterOperator.AND);
      // assertEquals( 20, searchLucene(query, 1000, new
      // FilterTerm(age.getFormattedName(), "[ 23 TO 30 ]" )));

      assertEquals(20, searchLucene(query, 1000, chainedFilter));
      /*
       * On va utiliser la requ�te COMPLETE Dans un premier temps pour refaire
       * la meme chose qu'en 4/
       */
      SearchResult result = null;
      query = baseCategory0.getFormattedName() + ":testfilter*";
      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
            ChainedFilterOperator.AND).addTermFilter(baseCategory2.getFormattedName(), "masculin",
            ChainedFilterOperator.AND);

      // complete.addChainedFilter(chainedFilter2);

      // complete.createFilterRoot(NodeType.AND);
      // OperatorNode root = complete.getFilterRoot();
      // root.addFilterTerm(c1.getFormattedName(), "adulte" );
      // root.addFilterTerm(c2.getFormattedName(), "masculin" );

      /*
       * On peut afficher la requ�te
       */
      System.out.println("requ�te \"les adultes de sexe masculin\" :\n" + query);

      result = ServiceProvider.getSearchService().search(query, 1000, base, chainedFilter);
      assertNotNull(result.getDocuments());
      assertEquals(20, result.getDocuments().size());

      /*
       * Ensuite on va faire un OU adulte OU masculin doit renvoyer 40 + 5 : 45
       */
      query = baseCategory0.getFormattedName() + ":testfilter*";

      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter.addTermFilter(baseCategory1.getFormattedName(), "adulte",
            ChainedFilterOperator.OR).addTermFilter(baseCategory2.getFormattedName(), "masculin",
            ChainedFilterOperator.OR);

      System.out.println("requ�te \"les adultes ou les individus de sexe masculin\" :\n" + query);

      result = ServiceProvider.getSearchService().search(query, 1000, base, chainedFilter);
      assertNotNull(result.getDocuments());
      assertEquals(45, result.getDocuments().size());

      /*
       * On va faire une requ�te plus complexe
       * "les femmes et les enfants d'abord" et "la personne 49" et
       * "la personne 48".
       * 
       * c1!=adulte or c2=feminin or c2=personne48 or c2=personne49
       * 
       * Root: OR Fils simples: c2=feminin, c2=personne48, c2=personne49 Fils :
       * Operator NAND avec un fils c1=adulte
       * 
       * 48 + 49. Hors eux il y a 38 adultes donc 19 femmes. Il y a 10 enfants.
       * 31.
       */
      query = baseCategory0.getFormattedName() + ":testfilter*";

      chainedFilter = ToolkitFactory.getInstance().createChainedFilter();
      chainedFilter
            .addTermFilter(baseCategory2.getFormattedName(), "feminin", ChainedFilterOperator.OR)
            .addTermFilter(baseCategory2.getFormattedName(), "personne48", ChainedFilterOperator.OR)
            .addTermFilter(baseCategory2.getFormattedName(), "personne49", ChainedFilterOperator.OR)
            .addTermFilter(baseCategory1.getFormattedName(), "enfant", ChainedFilterOperator.ANDNOT);

      // On doit maintenant conserver les r�f�rences quand on va plus loin que
      // le root.
      // OperatorNode sub = root.addEmptyNode(NodeType.ANDNOT) ;
      // sub.addFilterTerm(c1.getFormattedName(), "adulte");
      System.out
            .println("Requ�te \"les femmes et les enfants d'abord ainsi que les passagers 48 et 49 qui sont des VIPs\" :\n "
                  + query);

      result = ServiceProvider.getSearchService().search(query, 1000, base, chainedFilter);
      assertNotNull(result.getDocuments());
      assertEquals(21, result.getDocuments().size());

   }

   /**
    * Ce test montre la r�cup�ration de l'index m�tier sans passer par
    * l'extraction de tag. On stocke un document, et on r�cup�re les cat�gories
    * directement depuis la liste de solution.
    * 
    * @throws ExceededSearchLimitException
    * 
    * @throws IOException
    */
   @Test
   public void testGetCategoriesWithoutExtract() throws ExceededSearchLimitException {

      BaseCategory baseCategory0 = base.getBaseCategory(catNames[0]);
      BaseCategory baseCategory1 = base.getBaseCategory(catNames[1]);
      BaseCategory baseCategory2 = base.getBaseCategory(catNames[2]);

      Document document = toolkitFactory.createDocumentTag(base);

      String c0Val = "testGetCategoriesWithoutExtract1";
      String c1Val = "Bien sur nous sommes d'accord";
      String c2Val = "J�rome Kerviel doit prendre cher!";

      document.addCriterion(baseCategory0, c0Val);
      document.addCriterion(baseCategory1, c1Val);
      document.addCriterion(baseCategory2, c2Val);

      storeDoc(document, getFile("doc1.pdf"), true);

      SearchResult searchResult = ServiceProvider.getSearchService().search(
            baseCategory0.getFormattedName() + ":" + c0Val, 10, base);
      List<Document> docs = searchResult.getDocuments();
      assertNotNull(docs);
      assertEquals(1, docs.size());

      Document doc = docs.get(0);
      List<Criterion> crits = doc.getCriteria(baseCategory0);
      assertNotNull(crits);
      assertEquals(1, crits.size());
      assertEquals(c0Val, crits.get(0).getWord());

      crits = doc.getCriteria(baseCategory1);
      assertNotNull(crits);
      assertEquals(1, crits.size());
      assertEquals(c1Val, crits.get(0).getWord());

      Criterion critC2 = doc.getFirstCriterion(baseCategory2);
      assertEquals(c2Val, critC2.getWord());
   }

   /**
    * Test du stockage d'un document avec note et cat�gories mono/multivalu�es
    * et typ�es. Les recherches sont r�alis�es ici � la fois dans l'index GRC et
    * l'index LUCENE
    * 
    * @throws IOException
    * @throws ExceededSearchLimitException
    * @throws CustomTagControlException
    */
   @Test
   public void testStoreAndReturnDoc() throws IOException, TagControlException,
         ExceededSearchLimitException {
      // assertTrue("La base " + BASEID + " n'est pas d�marr�e.",
      // base.isStarted());

      // voir l'astuce pour r�cup�rer le classPath au runtime pour localiser
      // les fichiers
      File newDoc = getFile("doc1.pdf");

      assertTrue(newDoc.exists());

      // On d�finit le Tag du futur document, li� � la base uBase.
      Document document = toolkitFactory.createDocumentTag(base);

      // On dit que l'on veut mettre "Identifier" en valeur d'identifiant de
      // la 1�re cat�gorie (d'indice 0)
      String c0 = "Identifier";

      document.addCriterion(base.getBaseCategory(catNames[0]), c0);

      // C1
      document.addCriterion(base.getBaseCategory(catNames[1]), "C1 val");

      // Plusieurs valeurs pour C2
      BaseCategory c2 = base.getBaseCategory(catNames[2]);
      for (int i = 1; i < 5; i++) {
         document.addCriterion(c2, "C2 val" + i);
      }
      document.setType("PDF");

      /*
       * Valeurs typ�es BOOLEAN, INTEGER, DECIMAL, DATE, DATETIME.
       * 
       * Les types sont nativement stock�s dans ces formats. Par contre, ce sont
       * des repr�sentations chaines qui sont v�hicul�es jusqu'au serveur.
       */

      // booleen "true" ou "false"

      // 2 fa�ons, 1 en mettant "true", une en mettant le type primitif
      if (false)
         document.addCriterion(base.getBaseCategory(catNames[3]), "true");
      // tag.addCriterion(uBase.getBaseDefinition().getIndex().getCategory(catNames[3),
      // true, true);

      // integer : 2 fa�ons
      if (false)
         document.addCriterion(base.getBaseCategory(catNames[4]), "-10");
      // tag.addCriterion(uBase.getBaseDefinition().getIndex().getCategory(catNames[4),
      // true, -10);

      // decimal : 30 digits maximums, dont 10 sur la partie d�cimale.

      if (false)
         document.addCriterion(base.getBaseCategory(catNames[5]), "-1.54");
      document.addCriterion(base.getBaseCategory(catNames[5]), -1.54);
      document.addCriterion(base.getBaseCategory(catNames[5]), pi); //

      // ce sera 0.0 qui sera stock�.
      document.addCriterion(base.getBaseCategory(catNames[5]), "0.0");

      Calendar cal = Calendar.getInstance();
      cal.set(2011, 11, 01);
      // date.
      document.addCriterion(base.getBaseCategory(catNames[6]), cal.getTime());

      // date et heure
      // tag.addCriterion(uBase.getBaseDefinition().getIndex().getCategory(catNames[7),
      // true, new Date());

      // Date de cr�ation du document (� priori avant son entr�e dans la GED,
      // on retranche une heure)
      cal = Calendar.getInstance();
      cal.setTimeInMillis(System.currentTimeMillis());
      cal.add(Calendar.HOUR, -1);
      document.setCreationDate(cal.getTime());

      Document stored = ServiceProvider.getStoreService().storeDocument(document,
            new FileInputStream(newDoc));

      // On v�rifie que le document a pass� le controle.
      assertNotNull(stored);

      UUID archiveUUID = stored.getUuid();
      assertNotNull(archiveUUID);
      /*
       * Recherche sur UUID
       */

      Document documentByUUID = ServiceProvider.getSearchService().getDocumentByUUID(base,
            archiveUUID);
      System.out.println(documentByUUID.getUuid());

      /*
       * On recherche maintenant dans Lucene L'approche est diff�rente.
       * 
       * Tout d'abord on n'exprime plus les requ�tes sur les cat�gories en
       * pr�cisant le num�ro de la cat�gorie, mais son nom (ce qui facilitera
       * pour le futur les requ�tes multibase)
       * 
       * On peut retrouver une cat�gorie par son nom, par son id...
       */
      String c0Name = base.getBaseCategory(catNames[0]).getName();
      // On peut comparer...
      String c0NameBis = base.getBaseCategory(catNames[0]).getName();
      assertEquals(c0NameBis, c0Name);

      /*
       * Ensuite on construit la requ�te
       */
      String query = c0Name + ":" + c0;

      /*
       * Ici on a donc une requ�te du genre "Cat 0:Identifier". Cette requete ne
       * peut pas fonctionner (lucene n'aime pas les espaces dans les noms de
       * champs => Erreurs de syntaxe au moment de parser la requ�te) En fait on
       * stocke les noms de champs en casse basse et en rempla�ant les espaces
       * par des char(255). Cel� est fait par la m�thode suivante, pour devenir
       * "cat�0:Identifier" (vous pouvez v�rifier ce n'est pas un espace entre
       * cat et 0 dans le commentaire pr�c�dent)
       */
      query = base.getBaseCategory(catNames[0]).getFormattedName() + ":" + c0;

      // Nouveaut� on peut l'�crire directement
      query = base.getBaseCategory(catNames[0]).getFormattedName() + ":" + c0;

      /*
       * 
       * Ce qui est fondamental : on doit pr�ciser une limite dans le nombre de
       * r�sultats � remonter. Le serveur interdira les valeurs au del� de
       * LuceneUtils.SEARCH_LIMIT (10 000) ;
       */
      SearchResult searchResult = ServiceProvider.getSearchService().search(query, 5, base);
      List<Document> docs = searchResult.getDocuments();
      assertTrue(docs != null && docs.size() == 1);
      System.out.println(docs.get(0).getUuid());
      System.out.println(docs.get(0).getDigest());
      // on fait les contr�les
      control(docs.get(0), newDoc, c0);

      // On essaye de stocker un autre document avec C0=Identifier, ce qui est
      // normalement impossible.
      document = toolkitFactory.createDocumentTag(base);

      document.addCriterion(base.getBaseCategory(catNames[0]), c0);
      // Cel� ne doit pas fonctionner (false en dernier param�tre)
      File file = new File("/unknownFile.pdf");
      storeDoc(document, file, false);

      // On le v�rifie aussi dans lucene
      assertEquals(1,
            searchLucene(base.getBaseCategory(catNames[0]).getFormattedName() + ":" + c0, 5));

   }
}
