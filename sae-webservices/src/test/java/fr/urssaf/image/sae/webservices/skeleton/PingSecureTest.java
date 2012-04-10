package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.urssaf.image.sae.webservices.security.ActionsUnitaires;
import fr.urssaf.image.sae.webservices.security.spring.AuthenticationContext;
import fr.urssaf.image.sae.webservices.security.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.util.Axis2Utils;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-security-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class PingSecureTest {

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

   private PingSecureRequest createPingSecureRequest(String filePath) {

      Axis2Utils.initMessageContext(ctx, filePath);

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return PingSecureRequest.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Test
   public void pingSecure() throws AxisFault {

      PingSecureRequest request = createPingSecureRequest("src/test/resources/request/pingsecure_success.xml");

      PingSecureResponse response = skeleton.pingSecure(request);

      assertEquals("Test du ping",
            "Les services du SAE sécurisés par authentification sont en ligne",
            response.getPingString());

      AuthenticationToken authentification = AuthenticationContext
            .getAuthenticationToken();

      ActionsUnitaires actionUnitaires = authentification.getDetails();
      List<String> actions = actionUnitaires.getListeActionsUniquement();

      assertEquals("le nombre d'actions unitaires est incorrect", 1, actions
            .size());
      assertEquals("role inattendue", "ROLE_TOUS", actions.get(0));

      List<String> perimetres = actionUnitaires
            .getPerimetresDonnees("ROLE_TOUS");
      assertEquals("le nombre de perimetre de données est incorrect", 1,
            perimetres.size());
      assertEquals("role inattendue", "FULL", perimetres.get(0));

   }

}
