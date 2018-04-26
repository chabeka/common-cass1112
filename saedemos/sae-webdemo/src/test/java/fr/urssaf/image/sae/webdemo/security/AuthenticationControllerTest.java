package fr.urssaf.image.sae.webdemo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.CharSetUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import fr.urssaf.image.sae.webdemo.ControllerTestSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext/applicationContext-security-test.xml")
@SuppressWarnings("PMD")
public class AuthenticationControllerTest extends
      ControllerTestSupport<AuthenticationController> {

   static final String SAML_FIELD = "SAMLResponse";

   static final String RELAY_FIELD = "RelayState";

   private static String SAML_VALUE;

   static final String RELAY_VALUE = "/accueil.html";

   @Autowired
   private AuthenticationController servlet;

   @Before
   public void init() throws IOException {

      SAML_VALUE = FileUtils.readFileToString(new File(
            "src/test/resources/saml/ctd_rights.txt"), "UTF-8");
   }

   @Test
   public void post() throws IOException {

      this.initPost();

      this.initParameter(SAML_FIELD, SAML_VALUE);
      assertAuthenticate();

      String SAML_VALUE_DECODE = FileUtils.readFileToString(new File(
            "src/test/resources/saml/ctd_rights.xml"), "UTF-8");

      assertEquals(SAML_VALUE_DECODE, this.getAttributeSession(SAML_FIELD));
      assertEquals(RELAY_VALUE, this.getAttributeSession(RELAY_FIELD));
   }

   @Test
   public void post_failure() {

      this.initPost();
      this.initParameter(SAML_FIELD, null);
      assertAuthenticate();

      assertNull(this.getAttributeSession(SAML_FIELD));
      assertEquals(RELAY_VALUE, this.getAttributeSession(RELAY_FIELD));
   }

   @Test
   public void get() {

      this.initGet();

      this.initParameter(SAML_FIELD, SAML_VALUE);
      assertAuthenticate();

      assertNull(this.getAttributeSession(SAML_FIELD));
      assertNull(this.getAttributeSession(RELAY_FIELD));
   }

   private void assertAuthenticate() {

      this.initParameter(RELAY_FIELD, RELAY_VALUE);

      ModelAndView view = this.handle(servlet);

      assertEquals(200, this.getStatut());
      assertEquals("redirect:authenticate_check.html", view.getViewName());
   }
   
   
   @Test
   public void encodeBase64() throws IOException {
      
      String jeton = FileUtils.readFileToString(new File(
         "src/test/resources/saml/ctd_0_right.xml"), "UTF-8");
      
      String jetonBase64 = Base64.encodeBase64String(jeton.getBytes());
      
      System.out.println(jetonBase64);
      
      
   }

}
