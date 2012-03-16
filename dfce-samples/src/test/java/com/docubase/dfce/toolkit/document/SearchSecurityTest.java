package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.user.User;
import net.docubase.toolkit.model.user.UserSearchFilter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class SearchSecurityTest extends AbstractTestCaseCreateAndPrepareBase {
   private static BaseCategory c0;
   private static BaseCategory c1;
   private static BaseCategory c2;
   private static BaseCategory c4;

   private static String ADULT_RESTRICTED_USERNAME = "ADULT_RESTRICTED_USERNAME";
   private static String ADULT_RESTRICTED_USER_PASSWORD = "ADULT_RESTRICTED_USER_PASSWORD";

   private static String MALE_ADULT_AND_ADO_RESTRICTED_USERNAME = "MALE_ADULT_AND_ADO_RESTRICTED_USERNAME";
   private static String MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD = "MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD";

   @BeforeClass
   public static void setupAll() throws NoSuchJobException, JobExecutionAlreadyRunningException,
         JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException,
         JobParametersNotFoundException, UnexpectedJobExecutionException {
      System.out.println("****** testCompleteQueryScenarii ******");
      Document tag;

      c0 = base.getBaseCategory(catNames[0]);
      c1 = base.getBaseCategory(catNames[1]);
      c2 = base.getBaseCategory(catNames[2]);
      c4 = base.getBaseCategory(catNames[4]);

      for (int i = 0; i < 50; i++) {
         tag = ToolkitFactory.getInstance().createDocumentTag(base);

         // C0 unique
         tag.addCriterion(c0, "testfilter" + i);

         // C1 soit Enfant, soit Adulte
         String c1Val = null;
         if (i < 10) { // Les enfants d'abord
            c1Val = "enfant";
         } else if (i < 20) {
            c1Val = "ado";
         } else {
            c1Val = "adulte";
         }
         tag.addCriterion(c1, c1Val);

         // C2. 2 valeurs, une qui varie tr�s peu, une qui est unique.
         tag.addCriterion(c2, "personne" + i);
         tag.addCriterion(c2, i % 2 == 0 ? "masculin" : "feminin");
         tag.addCriterion(c4, 10);

         // stockage
         storeDocument(tag, TestUtils.getFile("doc1.pdf"), true);
      }

      serviceProvider.getStorageAdministrationService().updateAllIndexesUsageCount();

      User user = serviceProvider.getUserAdministrationService()
            .loadUser(ADULT_RESTRICTED_USERNAME);
      if (user == null) {
         try {
            createAdultRestrictedUser();
         } catch (ObjectAlreadyExistsException e) {
            throw new RuntimeException(e);
         }
      }

      user = serviceProvider.getUserAdministrationService().loadUser(
            MALE_ADULT_AND_ADO_RESTRICTED_USERNAME);
      if (user == null) {
         try {
            createMaleAdultAndAdoRestrictedUser();
         } catch (ObjectAlreadyExistsException e) {
            throw new RuntimeException(e);
         }
      }
   }

   @Before
   public void beforeEach() {
      serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
   }

   private static void createAdultRestrictedUser() throws ObjectAlreadyExistsException {
      UserSearchFilter searchFilter = ToolkitFactory.getInstance().createSearchFilter(c1.getName(),
            "adulte");
      List<UserSearchFilter> filters = new ArrayList<UserSearchFilter>();
      filters.add(searchFilter);
      serviceProvider.getUserAdministrationService().createUser(ADULT_RESTRICTED_USERNAME,
            ADULT_RESTRICTED_USER_PASSWORD, filters, "ADMIN");
   }

   private static void createMaleAdultAndAdoRestrictedUser() throws ObjectAlreadyExistsException {
      UserSearchFilter searchFilterAdultAndAdo = ToolkitFactory.getInstance().createSearchFilter(
            c1.getName(), "adulte", "ado");
      UserSearchFilter searchFilterMale = ToolkitFactory.getInstance().createSearchFilter(
            c2.getName(), "masculin");
      List<UserSearchFilter> filters = new ArrayList<UserSearchFilter>();
      filters.add(searchFilterAdultAndAdo);
      filters.add(searchFilterMale);
      serviceProvider.getUserAdministrationService().createUser(
            MALE_ADULT_AND_ADO_RESTRICTED_USERNAME, MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD,
            filters, "ADMIN");
   }

   @Test
   public void testSimpleQuery() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c1.getName() + ":adulte OR " + c1.getName() + ":ado";
      assertEquals(40, searchLucene(query, 1000, null));

      serviceProvider.connect(ADULT_RESTRICTED_USERNAME, ADULT_RESTRICTED_USER_PASSWORD,
            SERVICE_URL);

      assertEquals(30, searchLucene(query, 1000, null));

      serviceProvider.connect(MALE_ADULT_AND_ADO_RESTRICTED_USERNAME,
            MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD, SERVICE_URL);

      assertEquals(20, searchLucene(query, 1000, null));
   }

   @Test
   public void testCompositeQuery() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = "(" + c1.getName() + ":adulte" + " AND " + c2.getName() + ":masculin"
            + ") OR (" + c1.getName() + ":enfant" + " AND " + c2.getName() + ":feminin)";
      assertEquals(20, searchLucene(query, 1000, null));

      serviceProvider.connect(ADULT_RESTRICTED_USERNAME, ADULT_RESTRICTED_USER_PASSWORD,
            SERVICE_URL);

      assertEquals(15, searchLucene(query, 1000, null));

      serviceProvider.connect(MALE_ADULT_AND_ADO_RESTRICTED_USERNAME,
            MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD, SERVICE_URL);

      assertEquals(15, searchLucene(query, 1000, null));
   }

   @Test
   public void testRange() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c2.getName() + ":masculin" + " AND " + c1.getName() + ":[adulte TO enfant]";
      assertEquals(20, searchLucene(query, 1000, null));

      serviceProvider.connect(ADULT_RESTRICTED_USERNAME, ADULT_RESTRICTED_USER_PASSWORD,
            SERVICE_URL);

      assertEquals(15, searchLucene(query, 1000, null));

      serviceProvider.connect(MALE_ADULT_AND_ADO_RESTRICTED_USERNAME,
            MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD, SERVICE_URL);

      assertEquals(15, searchLucene(query, 1000, null));
   }

   @Test
   public void testAnd() throws ExceededSearchLimitException, SearchQueryParseException {
      String query = c1.getName() + ":adulte" + " AND " + c4.getName() + ":10";
      assertEquals(30, searchLucene(query, 1000, null));

      serviceProvider.connect(ADULT_RESTRICTED_USERNAME, ADULT_RESTRICTED_USER_PASSWORD,
            SERVICE_URL);

      assertEquals(30, searchLucene(query, 1000, null));

      serviceProvider.connect(MALE_ADULT_AND_ADO_RESTRICTED_USERNAME,
            MALE_ADULT_AND_ADO_RESTRICTED_USER_PASSWORD, SERVICE_URL);

      assertEquals(15, searchLucene(query, 1000, null));

   }
}
