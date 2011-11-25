package fr.urssaf.image.sae.webservices.soap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.xml.security.Init;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import fr.urssaf.image.sae.webservices.util.AxiomUtils;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;

/**
 * Classe tests unitaires pour tester les différents SoapFault<br>
 * Les message soap de test se trouvent dans le répertoire
 * <code>src/test/resources/soap/request</code>
 * 
 * 
 */
@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.TooManyMethods" })
public class PingSecureSoapTest {

   
   private static Configuration config;
   
   private MessageContext msgctx;

   private OperationClient opClient;

   private ServiceClient client;
   

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, AxisFault {
      config = new PropertiesConfiguration("sae-webservices-test.properties");
      Init.init();
   }


   @Before
   public void before() throws AxisFault {

      client = new ServiceClient();
      Options options = new Options();
      options.setAction("PingSecure");
      client.setTargetEPR(new EndpointReference(config
            .getString("urlServiceWeb")));

      msgctx = new MessageContext();
      opClient = client.createClient(ServiceClient.ANON_OUT_IN_OP);
      opClient.addMessageContext(msgctx);

   }

   @After
   public void after() throws AxisFault {
      client.cleanupTransport();
   }

   
   @Test
   public void pingSecure_success() throws AxisFault {

      SoapTestUtils.execute(
            "src/test/resources/soap/request/pingSecure_ok.xml",
            msgctx,
            opClient);

      Document response = AxiomUtils.loadDocumentResponse(client);

      assertEquals("test sur le message retour du ping",
            "Les services du SAE sécurisés par authentification sont en ligne",
            response.getElementsByTagNameNS("http://www.cirtil.fr/saeService",
                  "pingString").item(0).getTextContent());

   }

   
   @Test
   public void pingSecure_failure_sae_droitsInsuffisants() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_sae_DroitsInsuffisants.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "Les droits présents dans le vecteur d'identification sont insuffisants pour effectuer l'action demandée",
            "DroitsInsuffisants", 
            SoapTestUtils.SAE_NAMESPACE, 
            SoapTestUtils.SAE_PREFIX);

      }
   }

   @Test
   public void pingSecure_failure_vi_invalidAuthLevel() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_vi_InvalidAuthLevel.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "Le niveau d'authentification initial n'est pas conforme au contrat d'interopérabilité",
            "InvalidAuthLevel",
            SoapTestUtils.VI_NAMESPACE,
            SoapTestUtils.VI_PREFIX);
         
      }
   }

   @Test
   @Ignore("Ce test ne peut pas fonctionner actuellement car le référentiel des droits n'est pas encore développé")
   public void pingSecure_failure_vi_invalidPagm() {

      // TODO : Retirer le Ignore du test de la SoapFault vi:InvalidPagm dès que le référentiel des droits sera réalisé
      
      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_vi_InvalidPagm.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "Le ou les PAGM présents dans le VI sont invalides",
            "InvalidPagm", 
            SoapTestUtils.VI_NAMESPACE, 
            SoapTestUtils.VI_PREFIX);
         
      }
   }

   @Test
   public void pingSecure_failure_vi_invalidService() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_vi_InvalidService.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "Le service visé par le VI n'existe pas ou est invalide",
            "InvalidService", 
            SoapTestUtils.VI_NAMESPACE, 
            SoapTestUtils.VI_PREFIX);
         
      }
   }

   @Test
   public void pingSecure_failure_vi_invalidVI() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_vi_InvalidVI.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault, 
            "Le VI est invalide", 
            "InvalidVI",
            SoapTestUtils.VI_NAMESPACE,
            SoapTestUtils.VI_PREFIX);
         
      }
   }

   @Test
   public void pingSecure_failure_wsse_failedCheck() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_wsse_FailedCheck.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "La signature ou le chiffrement n'est pas valide",
            "FailedCheck", 
            SoapTestUtils.WSSE_NAMESPACE, 
            SoapTestUtils.WSSE_PREFIX);
         
      }
   }

   @Test
   public void pingSecure_failure_wsse_invalidSecurityToken() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_wsse_InvalidSecurityToken.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "Le jeton de sécurité fourni est invalide",
            "InvalidSecurityToken", 
            SoapTestUtils.WSSE_NAMESPACE,
            SoapTestUtils.WSSE_PREFIX);
         
      }
   }

   @Test
   public void pingSecure_failure_wsse_securityTokenUnavailable() {

      try {

         SoapTestUtils.execute(
               "src/test/resources/soap/request/pingSecure_SoapFault_wsse_SecurityTokenUnavailable.xml",
               msgctx,
               opClient);

         fail(SoapTestUtils.FAIL_MSG);
         
      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(
            fault,
            "La référence au jeton de sécurité est introuvable",
            "SecurityTokenUnavailable", 
            SoapTestUtils.WSSE_NAMESPACE,
            SoapTestUtils.WSSE_PREFIX);
         
      }
   }

}
