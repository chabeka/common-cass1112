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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.webservices.skeleton.SaeServiceSkeletonInterface;
import fr.urssaf.image.sae.webservices.util.Axis2Utils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-security-test.xml", "/applicationContext-sae-vi-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class PingSecureFailureTest {

   private static final String FAIL_MSG = "le test doit échouer";

   private static final String FAULT_CODE = "FaultCode incorrect";

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   private MessageContext ctx;

   @Autowired
   ModeApiCqlSupport modeApiCqlSupport;

   @Before
   public void before() {
      ctx = new MessageContext();
      MessageContext.setCurrentMessageContext(ctx);
      modeApiCqlSupport.initTables(MODE_API.DATASTAX);
   }

   @After
   public void after() {
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   private static void assertAxisFault_noVI(final AxisFault axisFault) {

      assertAxisFault(axisFault,
            "La référence au jeton de sécurité est introuvable",
            "SecurityTokenUnavailable", "wsse");
   }

   private static void assertAxisFault(final AxisFault axisFault, final String expectedMsg,
         final String expectedType, final String expectedPrefix) {

      assertEquals(FAULT_CODE, expectedMsg, axisFault.getMessage());
      assertEquals(FAULT_CODE, expectedType, axisFault.getFaultCode()
            .getLocalPart());
      assertEquals(FAULT_CODE, expectedPrefix, axisFault.getFaultCode()
            .getPrefix());
   }

   private void pingSecure_failure(final String soap) throws AxisFault {

      Axis2Utils.initMessageContext(ctx, soap);

      final PingSecureRequest request = new PingSecureRequest();

      skeleton.pingSecure(request);
   }

   @Test
   public void pingSecure_failure_noHeader() {

      try {
         pingSecure_failure("src/test/resources/request/pingsecure_failure_noHeader.xml");

         fail(FAIL_MSG);
      } catch (final AxisFault e) {

         assertAxisFault_noVI(e);

      }

   }

   @Test
   public void pingSecure_failure_noWSsecurity() {

      try {
         pingSecure_failure("src/test/resources/request/pingsecure_failure_noWSsecurity.xml");

         fail(FAIL_MSG);
      } catch (final AxisFault e) {

         assertAxisFault_noVI(e);

      }

   }

   @Test
   public void pingSecure_failure_noVI() {

      try {
         pingSecure_failure("src/test/resources/request/pingsecure_failure_noVI.xml");

         fail(FAIL_MSG);
      } catch (final AxisFault e) {

         assertAxisFault_noVI(e);

      }

   }

   @Test
   public void pingSecure_failure_sign() {

      try {
         pingSecure_failure("src/test/resources/request/pingsecure_failure_sign.xml");

         fail(FAIL_MSG);
      } catch (final AxisFault e) {
         assertAxisFault(e,
               "L'identifiant de l'organisme client présent dans le VI est invalide ou inconnu",
               "InvalidIssuer",
               "vi");

      }

   }

}
