package sae.client.demo.webservice;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.apache.axis2.AxisFault;


/**
 * Méthodes utilitaires pour les tests
 */
public class TestUtils {

   
   /**
    * Fait un sysout d'informations sur l'AxisFault passée en paramètre
    * 
    * @param fault l'AxisFault dont on veut afficher les infos
    */
   public static void sysoutAxisFault(AxisFault fault) {
      
      System.out.println("Une AxisFault a été levée");
      
      if (isSoapFault(fault)) {
         
         System.out.println("SoapFault Code namespace : " + fault.getFaultCode().getNamespaceURI());
         System.out.println("SoapFault Code préfixe : " + fault.getFaultCode().getPrefix());
         System.out.println("SoapFault Code partie locale : " + fault.getFaultCode().getLocalPart());
         System.out.println("SoapFault Message : " + fault.getReason());
         
      } else {
         
         System.out.println("Message : " + fault.getMessage());
         
      }
      
   }
   
   /**
    * Regarde si l'exception AxisFault passé en paramètre est une "SoapFault"
    * 
    * @param fault l'AxisFault
    * @return true si l'AxisFault est une "SoapFault", false dans le cas contraire
    */
   private static boolean isSoapFault(AxisFault fault) {
      return (fault.getFaultCode()!=null) && (fault.getReason()!=null);
   }
   
   
   
   /**
    * Vérifie une SoapFault par rapport à un attendu
    * 
    * @param fault l'AxisFault à vérifier
    * @param codeNamespaceAttendu le namespace du code que l'on est censé obtenir
    * @param codePrefixeAttendu le préxixe du code que l'on est censé obtenir
    * @param codePartieLocaleAttendu la partie locale du code que l'on est censé obtenir
    * @param messageAttendu le message que l'on est censé obtenir
    */
   public static void assertSoapFault(
         AxisFault fault,
         String codeNamespaceAttendu,
         String codePrefixeAttendu,
         String codePartieLocaleAttendu,
         String messageAttendu) {
      
      // Vérifie que c'est bien une SoapFault
      assertTrue(
            "L'AxisFault obtenue n'est pas une SoapFault (" + fault.getMessage() + ")",
            isSoapFault(fault));
      

      // Vérifie le code de la SoapFault      
      
      // Le namespace
      assertEquals(
            "Le namespace du code de la SoapFault est incorrect",
            codeNamespaceAttendu,
            fault.getFaultCode().getNamespaceURI());
      // Le préfixe
      assertEquals(
            "Le préfixe du code de la SoapFault est incorrect",
            codePrefixeAttendu,
            fault.getFaultCode().getPrefix());
      // La partie locale
      assertEquals(
            "La partie locale du code de la SoapFault est incorrecte",
            codePartieLocaleAttendu,
            fault.getFaultCode().getLocalPart());
      
      // Vérifie le message de la SoapFault
      assertEquals(
            "Le message de la SoapFault est incorrecte",
            messageAttendu,
            fault.getReason());
      
   }
   
   
}
