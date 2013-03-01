package example.com.docubase.dfce.toolkit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.user.User;
import net.docubase.toolkit.model.user.UserPermission;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;
import net.docubase.toolkit.service.administration.UserAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

@RunWith(JUnit4.class)
public class ToolkitExampleTest {
   private static final String TEST_BASE_ID = "TestBase";

   /** The logger. */
   private static Logger logger = Logger.getLogger(ToolkitExampleTest.class);

   private ServiceProvider serviceProvider;

   private Base testBase;

   public static final String CIVILITY = "civility";
   public static final String NAME = "name";
   public static final String NB_NOVELS = "nb_novels";
   public static final String BIRTHDATE = "birthdate";

   private final ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

   @Before
   public void before() throws ObjectAlreadyExistsException {

      String url = "";
      // Load test configuration.
      Configuration config;
      try {
         config = new PropertiesConfiguration("test.properties");
         url = config.getString("test.server.1.url");
      } catch (ConfigurationException e) {
         logger.error("Fail to load configuration from test.properties", e);
         fail();
      }
      logger.info("Configuration loaded from test.properties");

      serviceProvider = ServiceProvider.newServiceProvider();
      serviceProvider.connect("_ADMIN", "DOCUBASE", url);

      createUser();
      createCategories();
      createTestBase();
      creatingCiviltiesDictionnary();
   }

   private void createUser() throws ObjectAlreadyExistsException {
      User user = serviceProvider.getUserAdministrationService().loadUser("User");
      if (user == null) {
         serviceProvider.getUserAdministrationService().createUser("User", "Password", "ADMIN");
      }
   }

   @Test
   public void testCreateUserGroupAndUser() throws ObjectAlreadyExistsException {
      UserAdministrationService userAdminService = serviceProvider.getUserAdministrationService();

      if (userAdminService.loadUser("MyUser") != null) {
         // ce test ne passe qu'une fois, la seconde fois le user existe d�j�
         return;
      }

      // creating permissions GET_BASE AND BASE_SEARCH
      Set<UserPermission> permissions = new HashSet<UserPermission>();
      permissions.add(UserPermission.GET_BASE);
      permissions.add(UserPermission.BASE_SEARCH);

      // creating bases set
      Set<String> restrictedBaseIds = new HashSet<String>();
      restrictedBaseIds.add("TestBase");

      // creating group "MyGroup" with permissions and restricted bases
      userAdminService.createUserGroup("MyGroup", permissions, restrictedBaseIds);

      // creating user "MyUser" belonging to the group "MyGroup"
      userAdminService.createUser("MyUser", "myPassword", "MyGroup");

   }

   @After
   public void after() {
      if (serviceProvider != null) {
         serviceProvider.disconnect();
      }
   }

   @Test
   public void testArchivageAndSearch() throws FileNotFoundException, TagControlException,
         ExceededSearchLimitException, SearchQueryParseException {
      StoreService storeService = serviceProvider.getStoreService();
      SearchService searchService = serviceProvider.getSearchService();

      // creating document for Albert Camus' file
      Document documentCamus = toolkitFactory.createDocumentTag(testBase);

      // adding metadata
      documentCamus.addCriterion(NAME, "Albert Camus");
      documentCamus.addCriterion(CIVILITY, "MR");
      documentCamus.addCriterion(NB_NOVELS, 18);
      documentCamus.addCriterion(BIRTHDATE, new GregorianCalendar(7, 10, 1913).getTime());

      // storing Camus' file
      File fileCamus = TestUtils.getFile("example/camus.pdf");
      documentCamus = storeService.storeDocument(documentCamus, "camus", "pdf",
            new FileInputStream(fileCamus));

      // cr�ating document for Andr� Breton's file
      Document documentBreton = toolkitFactory.createDocumentTag(testBase);

      // adding metadata to document
      documentBreton.addCriterion(NAME, "Andr� Breton");
      documentBreton.addCriterion(CIVILITY, "MR");
      documentBreton.addCriterion(NB_NOVELS, 28);
      documentBreton.addCriterion(BIRTHDATE, new GregorianCalendar(19, 1, 1896).getTime());

      // storing Breton's file
      File fileBreton = TestUtils.getFile("example/breton.pdf");
      documentBreton = storeService.storeDocument(documentBreton, "breton", "pdf",
            new FileInputStream(fileBreton));

      // WRONG DICTIONNARY
      // cr�ating document for Andr� Breton's file
      Document documentBeauvoir = toolkitFactory.createDocumentTag(testBase);

      // adding metadata to document
      documentBeauvoir.addCriterion(NAME, "Simone de Beauvoir");
      documentBeauvoir.addCriterion(CIVILITY, "Mlle");
      documentBeauvoir.addCriterion(NB_NOVELS, 50);
      documentBeauvoir.addCriterion(BIRTHDATE, new GregorianCalendar(1, 6, 1804).getTime());

      // storing Beauvoir's file
      File fileBeauvoir = TestUtils.getFile("example/beauvoir.pdf");
      boolean tagControlException = false;
      try {
         storeService.storeDocument(documentBeauvoir, "beauvoir", "pdf", new FileInputStream(
               fileBeauvoir));
      } catch (TagControlException e) {
         tagControlException = true;
      }
      assertTrue(tagControlException);

      Document documentByUUID;
      SearchResult searchResult;
      // SEARCH

      // UUID Search
      documentByUUID = searchService.getDocumentByUUID(testBase, documentBreton.getUuid());
      assertNotNull(documentByUUID);

      // UUID Search multi-bases
      documentByUUID = searchService.getDocumentByUUIDMultiBase(documentCamus.getUuid());
      assertNotNull(documentByUUID);

      // NAME Search
      searchResult = searchService.search("name:Albert\\ Camus", 10, testBase);
      assertEquals(1, searchResult.getTotalHits());
      Document onlyDocument = searchResult.getDocuments().get(0);
      assertEquals(18, onlyDocument.getCriterions(NB_NOVELS).get(0).getWord());

      // AND Search
      searchResult = searchService.search("name:Albert\\ Camus AND nb_novels:18", 10, testBase);
      assertEquals(1, searchResult.getTotalHits());

      // OR Search
      searchResult = searchService.search("name:Albert\\ Camus OR nb_novels:28", 10, testBase);
      assertEquals(2, searchResult.getTotalHits());

      // Joker Search
      searchResult = searchService.search("name:a*", 10, testBase);
      assertEquals(2, searchResult.getTotalHits());

      // Range Search
      searchResult = searchService.search("nb_novels:[1 TO 40]", 10, testBase);
      assertEquals(2, searchResult.getTotalHits());

      // Range Search and Filter
      ChainedFilter filter = toolkitFactory.createChainedFilter();
      filter.addTermFilter("name", "Albert Camus");
      searchResult = searchService.search("nb_novels:[1 TO 40]", 10, testBase, filter);
      assertEquals(1, searchResult.getTotalHits());
   }

   @Test
   public void testConnectIsServerUpDisconnect() {
      // connecting to DFCE
      ServiceProvider serviceProvider = ServiceProvider.newServiceProvider();
      serviceProvider.connect("User", "Password",
            "http://cer69imageint9.cer69.recouv:8080/dfce-webapp/toolkit/");

      // testing if server up
      assertTrue(serviceProvider.isServerUp());

      // disconnecting from DFCE
      serviceProvider.disconnect();
   }

   @Test
   public void testVirtualDocument() throws FileNotFoundException, TagControlException {
      StoreService storeService = serviceProvider.getStoreService();
      StorageAdministrationService adminService = serviceProvider.getStorageAdministrationService();

      // creating XXthCentury file reference
      File fileSand = TestUtils.getFile("example/XXthCentury.pdf");
      FileReference fileReference = adminService.createFileReference("XXthCentury", "pdf",
            new FileInputStream(fileSand));

      // cr�ating Beauvoir document
      Document documentBeauvoir = toolkitFactory.createDocumentTag(testBase);
      documentBeauvoir.addCriterion(NAME, "Simone de Beauvoir");
      documentBeauvoir.addCriterion(CIVILITY, "MRS");
      documentBeauvoir.addCriterion(NB_NOVELS, 25);
      documentBeauvoir.addCriterion(BIRTHDATE, new GregorianCalendar(9, 0, 1908).getTime());

      // storing virtual document, Beauvoir's page 4 to 7
      documentBeauvoir = storeService.storeVirtualDocument(documentBeauvoir, fileReference, 1, 3);

      // cr�ating Sartre Document document
      Document documentSartre = toolkitFactory.createDocumentTag(testBase);
      documentSartre.addCriterion(NAME, "Jean-Paul Sartre");
      documentSartre.addCriterion(CIVILITY, "MS");
      documentSartre.addCriterion(NB_NOVELS, 10);
      documentSartre.addCriterion(BIRTHDATE, new GregorianCalendar(21, 5, 1905).getTime());

      // storing virtual document, Sartre's page 4 to 7
      documentSartre = storeService.storeVirtualDocument(documentSartre, fileReference, 4, 7);
   }

   private void createCategories() throws ObjectAlreadyExistsException {
      StorageAdministrationService admService = serviceProvider.getStorageAdministrationService();
      // testing if 'NAME' category exists
      if (admService.getCategory(NAME) == null) {
         // creating 'NAME' category
         admService.createCategory(NAME, CategoryDataType.STRING);
      }
      // fetching or creating 'CIVILITY' category
      admService.findOrCreateCategory(CIVILITY, CategoryDataType.STRING);
      // fetching or creating 'NB_NOVELS' category
      admService.findOrCreateCategory(NB_NOVELS, CategoryDataType.INTEGER);
      // fetching or creating 'BIRTHDATE' category
      admService.findOrCreateCategory(BIRTHDATE, CategoryDataType.DATE);
   }

   private void createTestBase() throws ObjectAlreadyExistsException {
      StorageAdministrationService adminService = serviceProvider.getStorageAdministrationService();
      BaseAdministrationService baseAdminService = serviceProvider.getBaseAdministrationService();

      Base base = baseAdminService.getBase(TEST_BASE_ID);
      if (base != null) {
         baseAdminService.deleteBase(base);
      }

      Category categoryCivility = adminService.getCategory(CIVILITY);
      Category categoryName = adminService.getCategory(NAME);
      Category categoryNbNovels = adminService.getCategory(NB_NOVELS);
      Category categoryBirthdate = adminService.getCategory(BIRTHDATE);

      // "CIVILITY", not indexed, 0-1, dictionnary and not single
      BaseCategory baseCategoryCivility = toolkitFactory
            .createBaseCategory(categoryCivility, false);
      baseCategoryCivility.setMinimumValues(0);
      baseCategoryCivility.setMaximumValues(1);
      baseCategoryCivility.setEnableDictionary(true);
      baseCategoryCivility.setSingle(false);

      // "NAME", indexed, 1-2, no dictionnary and single
      BaseCategory baseCategoryName = toolkitFactory.createBaseCategory(categoryName, true);
      baseCategoryName.setEnableDictionary(false);
      baseCategoryName.setMinimumValues(1);
      baseCategoryName.setMaximumValues(2);
      baseCategoryName.setSingle(false);

      // "NB_NOVELS", indexed, 1, no dictionnary and not single
      BaseCategory baseCategoryNbNovels = toolkitFactory.createBaseCategory(categoryNbNovels, true);
      baseCategoryNbNovels.setEnableDictionary(false);
      baseCategoryNbNovels.setMinimumValues(1);
      baseCategoryNbNovels.setMaximumValues(1);
      baseCategoryNbNovels.setSingle(false);

      // "BIRTHDATE", not indexed, 0-1, no dictionnary and not single
      BaseCategory baseCategoryBirthdate = toolkitFactory.createBaseCategory(categoryBirthdate,
            false);
      baseCategoryBirthdate.setEnableDictionary(false);
      baseCategoryBirthdate.setMinimumValues(0);
      baseCategoryBirthdate.setMaximumValues(1);
      baseCategoryBirthdate.setSingle(false);

      // creating base instance
      base = toolkitFactory.createBase(TEST_BASE_ID);

      // adding baseCategories to the created Base
      base.addBaseCategory(baseCategoryCivility);
      base.addBaseCategory(baseCategoryName);
      base.addBaseCategory(baseCategoryNbNovels);
      base.addBaseCategory(baseCategoryBirthdate);

      // creating "TestBase" in the system
      testBase = baseAdminService.createBase(base);
   }

   private void creatingCiviltiesDictionnary() {
      BaseCategory baseCategoryCivility = testBase.getBaseCategory(CIVILITY);
      StorageAdministrationService adminService = serviceProvider.getStorageAdministrationService();

      // adding "MR", "MS" et "MRS" to CIVILITY's dictionnary
      adminService.addDictionaryTerm(baseCategoryCivility, "MR");
      adminService.addDictionaryTerm(baseCategoryCivility, "MS");
      adminService.addDictionaryTerm(baseCategoryCivility, "MRS");
   }
}
