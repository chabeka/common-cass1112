package com.docubase.dfce.toolkit;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.caucho.hessian.client.HessianConnectionException;
import com.docubase.dfce.toolkit.base.AbstractBaseTestCase;

@RunWith(JUnit4.class)
public class SecurityTest {
   @Test(expected = RuntimeException.class)
   public void testWrongLogin() {
      Authentication.openSession(AbstractBaseTestCase.ADM_LOGIN + "wrongLogin",
            AbstractBaseTestCase.ADM_PASSWORD, AbstractBaseTestCase.SERVICE_URL);
   }

   @Test(expected = RuntimeException.class)
   public void testWrongPassword() {
      Authentication.openSession(AbstractBaseTestCase.ADM_LOGIN, AbstractBaseTestCase.ADM_PASSWORD
            + "wrongPassword", AbstractBaseTestCase.SERVICE_URL);
   }

   @Test(expected = RuntimeException.class)
   public void testMalformedURL() {
      Authentication.openSession(AbstractBaseTestCase.ADM_LOGIN, AbstractBaseTestCase.ADM_PASSWORD,
            "malformed" + AbstractBaseTestCase.SERVICE_URL);
   }

   @Test(expected = HessianConnectionException.class)
   public void testRoleUserOperationAdmin() {
      Authentication.openSession(AbstractBaseTestCase.SIMPLE_USER_NAME,
            AbstractBaseTestCase.SIMPLE_USER_PASSWORD, AbstractBaseTestCase.SERVICE_URL);
      ServiceProvider.getBaseAdministrationService().getAllBases();
   }

   @Test
   public void testRoleAdminOperationAdmin() {
      Authentication.openSession(AbstractBaseTestCase.ADM_LOGIN, AbstractBaseTestCase.ADM_PASSWORD,
            AbstractBaseTestCase.SERVICE_URL);
      List<Base> allBases = ServiceProvider.getBaseAdministrationService().getAllBases();
      assertNotNull(allBases);
   }

   @Test
   public void testRoleUserOperationUser() {
      Authentication.openSession(AbstractBaseTestCase.SIMPLE_USER_NAME,
            AbstractBaseTestCase.SIMPLE_USER_PASSWORD, AbstractBaseTestCase.SERVICE_URL);
      String formatDate = ServiceProvider.getSearchService()
            .formatDate(new Date(), DateFormat.DATE);
      assertNotNull(formatDate);
   }
}
