package fr.urssaf.image.sae.webservices.security.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import me.prettyprint.hector.api.exceptions.HectorException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Element;

import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.webservices.modele.WebServiceConfiguration;
import fr.urssaf.image.sae.webservices.security.SecurityService;
import fr.urssaf.image.sae.webservices.security.igc.IgcService;
import fr.urssaf.image.sae.webservices.util.Axis2Utils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-security-test.xml",
      "/applicationContext-sae-vi-test.xml" })
public class AuthenticateHandlerCassandraDownTest {

   private static final String FAIL_MSG = "le test doit échouer";

   private static final String FAULT_CODE = "FaultCode incorrect";

   private MessageContext ctx;

   @Autowired
   private WebServiceVIService service;

   @Autowired
   private IgcService igcService;

   @Autowired
   private WebServiceConfiguration configuration;

   @Before
   public void before() {
      ctx = new MessageContext();
      MessageContext.setCurrentMessageContext(ctx);
   }

   @Test
   public void loadCertifsAndCrlException() throws AxisFault {

      SecurityService security = new SecurityService(igcService, service,
            configuration) {

         @Override
         public void authentification(Element identification)
               throws VIInvalideException {
            HectorException exception = new HectorException("erreur base");
            UncheckedExecutionException uncheckedException = new UncheckedExecutionException(
                  exception);
            VIInvalideException viException = new VIInvalideException(
                  "erreur VI base", uncheckedException);
            throw viException;
         }

      };

      AuthenticateHandler handler = new AuthenticateHandler(security);

      try {
         Axis2Utils.initMessageContext(ctx,
               "src/test/resources/request/pingsecure_success.xml");

         handler.authenticate();

         fail(FAIL_MSG);
      } catch (AxisFault e) {

         assertEquals(FAULT_CODE,
               "La base de données est temporairement inaccessible", e
                     .getMessage());

      }

   }
}
