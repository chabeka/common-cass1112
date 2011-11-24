package fr.urssaf.image.sae.webservices.util;

import static org.junit.Assert.assertEquals;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.context.MessageContext;


/**
 * Constantes et Méthodes utilitaires pour les tests unitaires des appels SOAP
 */
public final class SoapTestUtils {

   
   public static final String FAIL_MSG = "le test doit échouer";

   public static final String VI_PREFIX = "vi";

   public static final String VI_NAMESPACE = "urn:iops:vi:faultcodes";

   public static final String SAE_PREFIX = "sae";

   public static final String SAE_NAMESPACE = "urn:sae:faultcodes";

   public static final String WSSE_PREFIX = "wsse";

   public static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
   
   
   /**
    * Soap Fault Code : ServiceNonImplemente
    */
   public static final String FAULT_CODE_SNI = "ServiceNonImplemente";
   
   
   /**
    * Soap Fault Code : SecurityTokenUnavailable
    */
   public static final String FAULT_CODE_STU = "SecurityTokenUnavailable";
   
   
   
   /**
    * Constructeur privé
    */
   private SoapTestUtils() {
      
   }
   
   
   /**
    * Vérification d'une SoapFault attendue dans un test unitaire
    * 
    * @param fault l'exception récupérée côté client
    * @param message le message attendu
    * @param localPart le code attendu
    * @param namespaceURI le namespace du code attendu
    * @param prefix le préfixe du code attendu
    */
   public static void assertAxisFault(
         AxisFault fault, 
         String message,
         String localPart, 
         String namespaceURI, 
         String prefix) {

      // Vérification du code de la SoapFault
      
      String faultCodeAttendu = prefix + ":" + localPart;
      
      String faultCodeObtenu = 
         fault.getFaultCode().getPrefix() + 
         ":" + 
         fault.getFaultCode().getLocalPart();
      
      assertEquals(
            "Le code de la SoapFault est incorrect",
            faultCodeAttendu,
            faultCodeObtenu);
      
      // Vérification du message de la SoapFault
      assertEquals(
            "le message du soapFault est incorrect",
            message, 
            fault.getMessage());
      
      // Vérifications supplémentaires
      assertEquals("le prefix du soapFault", prefix, fault.getFaultCode()
            .getPrefix());
      assertEquals("le code du soapFault est incorrect", localPart, fault
            .getFaultCode().getLocalPart());
      assertEquals("le namespaceURI du soapFault", namespaceURI, fault
            .getFaultCode().getNamespaceURI());
      
   }
   
   
   /**
    * 
    * @param soapFile le fichier de requête SOAP
    * @param msgctx ...
    * @param opClient ...
    * @throws AxisFault en cas d'erreur
    */
   public static void execute(
         String soapFile,
         MessageContext msgctx,
         OperationClient opClient) throws AxisFault {

      SOAPEnvelope soapEnvelope = AxiomUtils.parse(soapFile);

      msgctx.setEnvelope(soapEnvelope);
      opClient.execute(true);

   }
   
}
