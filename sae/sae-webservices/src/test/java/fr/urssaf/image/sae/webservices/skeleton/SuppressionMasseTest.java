package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.SuppressionMasse;
import fr.cirtil.www.saeservice.SuppressionMasseResponse;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings({ "PMD.MethodNamingConventions" })
public class SuppressionMasseTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Before
   public void before() {

      MockHttpServletResponse response = new MockHttpServletResponse();
      response.setContentType("text/html");

      MessageContext ctx = new MessageContext();
      ctx.setProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE, response);

      MessageContext.setCurrentMessageContext(ctx);

      // indispensable car c'est ainsi que l'identifiant du traitement est
      // calculé
      MDC.put(BuildOrClearMDCAspect.LOG_CONTEXTE, UUID.randomUUID().toString());

      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("TU");
      extrait.setIdUtilisateur("login_test");
      SaeDroits droits = new SaeDroits();
   
      extrait.setSaeDroits(droits);

      Authentication authentication = new TestingAuthenticationToken(extrait,
            "password_test", new String[] { "ROLE_TOUS" });
      

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }


   @After
   public void after() {

      SecurityContextHolder.getContext().setAuthentication(null);
   }

   private SuppressionMasse createSuppressionMasse(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return SuppressionMasse.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Test
   public void suppressionMasse_success() throws AxisFault {

      SuppressionMasse request = createSuppressionMasse("src/test/resources/request/suppressionMasse_success.xml");

      SuppressionMasseResponse response = skeleton.suppressionMasseSecure(request,
            "127.0.0.1");

      assertNotNull("Test de suppression de masse",
            response.getSuppressionMasseResponse());


   }

}
