package com.docubase.dfce.toolkit.service.administration;

import static org.junit.Assert.*;

import java.util.List;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractBaseTestCase;

public class BaseAdministrationServiceTest extends AbstractBaseTestCase {

   private static final String URL = "http://cer69-ds4int:8080/dfce-webapp/toolkit/";

   @BeforeClass
   public static void setUp() {
      Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
   }

   @AfterClass
   public static void afterClass() {
      Authentication.closeSession();
   }

   @Test
   public void testGetAllBases() {
      Base newBase = ServiceProvider.getBaseAdministrationService().getBase("newBase");
      if (newBase != null) {
         ServiceProvider.getBaseAdministrationService().stopBase(newBase);
         ServiceProvider.getBaseAdministrationService().deleteBase(newBase);
      }

      List<Base> allBases = ServiceProvider.getBaseAdministrationService().getAllBases();
      int allBasesSize = allBases.size();

      newBase = ToolkitFactory.getInstance().createBase("newBase");

      Category category = ServiceProvider.getStorageAdministrationService().findOrCreateCategory(
            "newCategory", CategoryDataType.STRING);

      BaseCategory baseCategory = ToolkitFactory.getInstance().createBaseCategory(category, true);

      newBase.addBaseCategory(baseCategory);

      try {
         ServiceProvider.getBaseAdministrationService().createBase(newBase);
      } catch (ObjectAlreadyExistsException e) {
         e.printStackTrace();
         fail("base : " + base.getBaseId() + " already exists");
      }

      List<Base> allBasesAfterNewBase = ServiceProvider.getBaseAdministrationService()
            .getAllBases();
      int allBasesAfterNewBaseSize = allBasesAfterNewBase.size();

      assertEquals(allBasesSize + 1, allBasesAfterNewBaseSize);

      ServiceProvider.getBaseAdministrationService().deleteBase(newBase);
   }
}
