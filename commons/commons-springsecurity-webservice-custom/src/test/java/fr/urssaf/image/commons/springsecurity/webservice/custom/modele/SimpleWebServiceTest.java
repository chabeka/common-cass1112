package fr.urssaf.image.commons.springsecurity.webservice.custom.modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.springsecurity.webservice.custom.wssecurity.SecurityContextByFileInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/applicationContext.xml",
      "/applicationContext-jaxws.xml" })
public class SimpleWebServiceTest {

   @Autowired
   private SimpleWebService service;

   @Autowired
   private SecurityContextByFileInterceptor interceptor;

   private static final String SAML_USER = "src/test/resources/saml/saml_role_user.xml";

   private static final String SAML_ADMIN = "src/test/resources/saml/saml_role_admin.xml";

   @Test
   public void saveSuccess() {

      interceptor.setSAMLFile(SAML_ADMIN);
      service.save("title", "text");

   }

   @Test
   public void saveFailure() {

      interceptor.setSAMLFile(SAML_USER);
      try {
         service.save("title", "text");
         fail("doit lever une exception " + SOAPFaultException.class);
      } catch (SOAPFaultException e) {
         assertEquals("Access is denied", e.getMessage());

      }
   }

   @Test
   public void loadSuccess() {
      interceptor.setSAMLFile(SAML_USER);
      service.load();

   }

   @Test
   public void loadFailure() {
      interceptor.setSAMLFile(SAML_ADMIN);
      try {
         service.load();
         fail("doit lever une exception " + SOAPFaultException.class);
      } catch (SOAPFaultException e) {
         assertEquals("Access is denied", e.getMessage());

      }
   }

}
