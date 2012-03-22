package fr.urssaf.image.sae.webservices.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.urssaf.image.sae.webservices.skeleton.SaeServiceSkeletonInterface;
import fr.urssaf.image.sae.webservices.util.Axis2Utils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-security-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class PingSecureFailureTest {

   private static final String FAIL_MSG = "le test doit échouer";

   private static final String FAULT_CODE = "FaultCode incorrect";

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   private MessageContext ctx;

   @Before
   public void before() {
      ctx = new MessageContext();
      MessageContext.setCurrentMessageContext(ctx);
   }

   @After
   public void after() {
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   private static void assertAxisFault_noVI(AxisFault axisFault) {

      assertAxisFault(axisFault,
            "La référence au jeton de sécurité est introuvable",
            "SecurityTokenUnavailable", "wsse");
   }

   private static void assertAxisFault(AxisFault axisFault, String expectedMsg,
         String expectedType, String expectedPrefix) {

      assertEquals(FAULT_CODE, expectedMsg, axisFault.getMessage());
      assertEquals(FAULT_CODE, expectedType, axisFault.getFaultCode()
            .getLocalPart());
      assertEquals(FAULT_CODE, expectedPrefix, axisFault.getFaultCode()
            .getPrefix());
   }

   private void pingSecure_failure(String soap) throws AxisFault {

      Axis2Utils.initMessageContext(ctx, soap);

      PingSecureRequest request = new PingSecureRequest();

      skeleton.pingSecure(request);
   }

   @Test
   public void pingSecure_failure_noHeader() {

      try {
         this
               .pingSecure_failure("src/test/resources/request/pingsecure_failure_noHeader.xml");

         fail(FAIL_MSG);
      } catch (AxisFault e) {

         assertAxisFault_noVI(e);

      }

   }

   @Test
   public void pingSecure_failure_noWSsecurity() {

      try {
         this
               .pingSecure_failure("src/test/resources/request/pingsecure_failure_noWSsecurity.xml");

         fail(FAIL_MSG);
      } catch (AxisFault e) {

         assertAxisFault_noVI(e);

      }

   }

   @Test
   public void pingSecure_failure_noVI() {

      try {
         this
               .pingSecure_failure("src/test/resources/request/pingsecure_failure_noVI.xml");

         fail(FAIL_MSG);
      } catch (AxisFault e) {

         assertAxisFault_noVI(e);

      }

   }

   @Test
   public void pingSecure_failure_sign() {

      try {
         this
               .pingSecure_failure("src/test/resources/request/pingsecure_failure_sign.xml");

         fail(FAIL_MSG);
      } catch (AxisFault e) {

         assertAxisFault(e, "La signature ou le chiffrement n'est pas valide",
               "FailedCheck", "wsse");

      }

   }

   @Test
   public void pingSecure_failure_accessDenied() throws AxisFault {

      try {
         this
               .pingSecure_failure("src/test/resources/request/pingsecure_failure_accessDenied.xml");
         fail(FAIL_MSG);
      } catch (AxisFault e) {

         assertEquals(
               FAULT_CODE,
               "Les droits présents dans le vecteur d'identification sont insuffisants pour effectuer l'action demandée",
               e.getMessage());
         assertEquals(FAULT_CODE, "DroitsInsuffisants", e.getFaultCode()
               .getLocalPart());
         assertEquals(FAULT_CODE, "sae", e.getFaultCode().getPrefix());

         assertEquals(FAULT_CODE, "urn:sae:faultcodes", e.getFaultCode()
               .getNamespaceURI());

      }

   }

}
