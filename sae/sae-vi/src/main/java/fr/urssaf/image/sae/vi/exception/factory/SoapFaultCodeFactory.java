package fr.urssaf.image.sae.vi.exception.factory;

import javax.xml.namespace.QName;

/**
 * Factory des SoapFaultCode dans les messages SOAP
 * 
 * 
 */
public final class SoapFaultCodeFactory {
   
   private static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
   private static final String WSSE_PREFIX = "wsse";
   
   
   private SoapFaultCodeFactory(){
      
   }

   /**
    * ex : "wsse:InvalidSecurityToken"
    * 
    * @param localPart
    *           code de l'erreur
    * @return soapFaultCode pour WS-Security
    */
   public static QName createWsseSoapFaultCode(String localPart) {

      return createSoapFaultCode(WSSE_NS, localPart, WSSE_PREFIX);
   }

   /**
    * ex: "vi:InvalidIssuer"
    * 
    * @param localPart
    *           code de l'erreur
    * @return soapFaultCode pour VI
    */
   public static QName createVISoapFaultCode(String localPart) {

      return createSoapFaultCode("urn:iops:vi:faultcodes", localPart, "vi");

   }

   /**
    * instanciation de {@link QName#QName(String, String, String)}
    * 
    * 
    * @param namespaceURI
    *           Namespace URI of the <code>QName</code>
    * @param localPart
    *           local part of the <code>QName</code>
    * @param prefix
    *           prefix of the <code>QName</code>
    * @return instance de QName
    */
   public static QName createSoapFaultCode(String namespaceURI,
         String localPart, String prefix) {

      return new QName(namespaceURI, localPart, prefix);

   }
}
