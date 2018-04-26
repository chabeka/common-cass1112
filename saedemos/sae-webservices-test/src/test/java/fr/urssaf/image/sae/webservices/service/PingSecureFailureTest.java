package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.util.SaeServiceStubUtils;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class PingSecureFailureTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(PingSecureFailureTest.class);


   private String getSoapFaultInfos(AxisFault fault) {
      StringBuilder messageFailure = new StringBuilder();
      messageFailure.append("SoapFault :");
      messageFailure.append("\r\n" + "FaultCode = "
            + fault.getFaultCode().getPrefix() + ":"
            + fault.getFaultCode().getLocalPart());
      messageFailure.append("\r\n" + "FaultString = " + fault.getMessage());
      return messageFailure.toString();
   }

   /**
    * Test unitaire de la SoapFault wsse:SecurityTokenUnavailable<br>
    * <br>
    * Cas de test : On consomme le service pingSecure sans mettre de VI dans le
    * message SOAP<br>
    * <br>
    * Résultat attendu : levée d'une SoapFault avec les données suivantes :<br>
    * <ul>
    * <li>FaultCode : wsse:SecurityTokenUnavailable</li>
    * <li>FaultString : La référence au jeton de sécurité est introuvable</li>
    * </ul>
    * 
    * @throws RemoteException
    */
   @Test
   public void pingSecure_failure_SecurityTokenUnavailable()
         throws RemoteException {

      // On truande le service PingSecureService en lui collant un stub
      // qui ne possède pas la mécanique pour inclure le VI
      PingSecureService service = new PingSecureService(
            SaeServiceStubUtils.getServiceStub());
      
      try {
         service.pingSecure();
         fail("le test doit échouer");
      } catch (AxisFault fault) {

         // On trace
         LOG.debug(getSoapFaultInfos(fault));

         SoapTestUtils.assertAxisFault(fault,
               "La référence au jeton de sécurité est introuvable",
               "SecurityTokenUnavailable", SoapTestUtils.WSSE_NAMESPACE,
               SoapTestUtils.WSSE_PREFIX);

      }
   }
}
