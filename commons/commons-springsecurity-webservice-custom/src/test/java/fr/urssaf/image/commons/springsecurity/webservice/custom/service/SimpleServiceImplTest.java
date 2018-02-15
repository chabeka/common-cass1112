package fr.urssaf.image.commons.springsecurity.webservice.custom.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.springsecurity.webservice.custom.wssecurity.SecurityContextByRoleInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class SimpleServiceImplTest {

   @Autowired
   private SimpleServiceImpl service;

   @Autowired
   private SecurityContextByRoleInterceptor interceptor;

   private static final String SAML_USER = "ROLE_USER";

   private static final String SAML_ADMIN = "ROLE_ADMIN";

   @Test
   public void saveSuccess() {

      interceptor.setRoles(SAML_ADMIN);
      service.save();

   }

   @Test
   public void saveFailure() {

      interceptor.setRoles(SAML_USER);
      try {
         service.save();
         fail("doit lever une exception " + SOAPFaultException.class);
      } catch (SOAPFaultException e) {
         assertEquals("Access is denied", e.getMessage());

      }
   }

   @Test
   public void loadSuccess() {

      interceptor.setRoles(SAML_USER);
      service.load();

   }

   @Test
   public void loadFailure() {

      interceptor.setRoles(SAML_ADMIN);
      try {
         service.load();
         fail("doit lever une exception " + SOAPFaultException.class);
      } catch (SOAPFaultException e) {
         assertEquals("Access is denied", e.getMessage());

      }
   }

}
