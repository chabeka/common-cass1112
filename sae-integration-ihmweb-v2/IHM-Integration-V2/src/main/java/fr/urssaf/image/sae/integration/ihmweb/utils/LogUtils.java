package fr.urssaf.image.sae.integration.ihmweb.utils;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;

/**
 * Méthodes utilitaires pour logguer des éléments dans le log du résultat d'un test
 */
public final class LogUtils {

   
   private LogUtils() {
      
   }
   
   /**
    * Log un code de SoapFault
    * @param log le log
    * @param faultCode le code de la SoapFault
    */
   public static void logSoapFaultCode(
         ResultatTestLog log,
         QName faultCode) {
      if (faultCode==null) {
         log.appendLogLn("Code Namespace: " + "SoapFaultCode non présent dans la SoapFault");
         log.appendLogLn("Code Préfixe: " + "SoapFaultCode non présent dans la SoapFault");
         log.appendLogLn("Code Partie locale: " + "SoapFaultCode non présent dans la SoapFault");
      } else {
         log.appendLogLn("Code Namespace: " + faultCode.getNamespaceURI());
         log.appendLogLn("Code Préfixe: " + faultCode.getPrefix());
         log.appendLogLn("Code Partie locale: " + faultCode.getLocalPart());
      }
   }
   
   
   /**
    * Log une SoapFault
    * 
    * @param log le log
    * @param fault la SoapFault
    */
   public static void logSoapFault(
         ResultatTestLog log,
         AxisFault fault) {
      
      log.appendLogLn("Message: " + fault.getReason());
      
      logSoapFaultCode(log,fault.getFaultCode());
      
      if (fault.getCause()!=null) {
         log.appendLogLn("Cause  :");
         log.appendLogLn(fault.getCause().toString());
      }
      
      if (fault.getDetail()!=null) {
         log.appendLogLn("Détails  :");
         log.appendLogLn(fault.getDetail().toString());
      }
      
   }
   
   
   /**
    * Log une SoapFault
    * 
    * @param log le log
    * @param faultCode le code de la SoapFault
    * @param faultMessage le message de la SoapFault
    */
   public static void logSoapFault(
         ResultatTestLog log,
         QName faultCode,
         String faultMessage) {
      
      log.appendLogLn("Message: " + faultMessage);
      logSoapFaultCode(log,faultCode);
      
   }
   
   
}
