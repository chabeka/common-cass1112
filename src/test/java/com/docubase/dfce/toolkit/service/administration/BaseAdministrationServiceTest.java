package com.docubase.dfce.toolkit.service.administration;

import static org.junit.Assert.*;

import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.AbstractTestBase;

public class BaseAdministrationServiceTest extends AbstractTestBase {

   @BeforeClass
   public static void setUp() {
      serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
   }

   @AfterClass
   public static void afterClass() {
      serviceProvider.disconnect();
   }

   @Test
   public void testGetAllBases() {
      Base newBase = serviceProvider.getBaseAdministrationService().getBase("newBase");
      if (newBase != null) {
         serviceProvider.getBaseAdministrationService().stopBase(newBase);
         serviceProvider.getBaseAdministrationService().deleteBase(newBase);
      }

      List<Base> allBases = serviceProvider.getBaseAdministrationService().getAllBases();
      int allBasesSize = allBases.size();

      newBase = ToolkitFactory.getInstance().createBase("newBase");

      Category category = serviceProvider.getStorageAdministrationService().findOrCreateCategory(
            "newCategory", CategoryDataType.STRING);

      BaseCategory baseCategory = ToolkitFactory.getInstance().createBaseCategory(category, true);

      newBase.addBaseCategory(baseCategory);

      try {
         serviceProvider.getBaseAdministrationService().createBase(newBase);
      } catch (ObjectAlreadyExistsException e) {
         e.printStackTrace();
         fail("base : " + newBase.getBaseId() + " already exists");
      }

      List<Base> allBasesAfterNewBase = serviceProvider.getBaseAdministrationService()
            .getAllBases();
      int allBasesAfterNewBaseSize = allBasesAfterNewBase.size();

      assertEquals(allBasesSize + 1, allBasesAfterNewBaseSize);

      serviceProvider.getBaseAdministrationService().deleteBase(newBase);
   }
}
