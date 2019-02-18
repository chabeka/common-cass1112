package sae.client.demo.utils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * Méthodes utilitaires pour les tests
 */
public final class TestUtils {

   private TestUtils() {
      // Constructeur privé
   }

   /**
    * Fait un sysout d'informations sur la SOAP Fault passée en paramètre
    *
    * @param fault
    *           l'AxisFault dont on veut afficher les infos
    */
   public static void sysoutSoapFault(final SOAPFaultException fault) {

      System.out.println("Une SOAPFaultException a été levée");

      if (isSoapFault(fault)) {
         final Iterator subcodes = fault.getFault().getFaultSubcodes();
         while (subcodes.hasNext()) {
            final QName subcode = (QName) subcodes.next();
            fault.getFault().getFaultSubcodes().next();
            System.out.println("SoapFault Code namespace : " + subcode.getNamespaceURI());
            System.out.println("SoapFault Code préfixe : " + subcode.getPrefix());
            System.out.println("SoapFault Code partie locale : " + subcode.getLocalPart());
            System.out.println("SoapFault Message : " + fault.getMessage());
         }

      } else {

         System.out.println("Message : " + fault.getMessage());

      }

   }

   /**
    * Regarde si l'exception AxisFault passé en paramètre est une "SoapFault"
    *
    * @param fault
    *           l'AxisFault
    * @return true si l'AxisFault est une "SoapFault", false dans le cas contraire
    */
   private static boolean isSoapFault(final SOAPFaultException fault) {
      return (fault.getFault().getFaultCode() != null);
   }

   /**
    * Vérifie une SoapFault par rapport à un attendu
    *
    * @param fault
    *           l'AxisFault à vérifier
    * @param codeNamespaceAttendu
    *           le namespace du code que l'on est censé obtenir
    * @param codePrefixeAttendu
    *           le préfixe du code que l'on est censé obtenir
    * @param codePartieLocaleAttendu
    *           la partie locale du code que l'on est censé obtenir
    * @param messageAttendu
    *           le message que l'on est censé obtenir
    */
   public static void assertSoapFault(
                                      final SOAPFaultException fault,
                                      final String codeNamespaceAttendu,
                                      final String codePrefixeAttendu,
                                      final String codePartieLocaleAttendu,
                                      final String messageAttendu) {

      final Iterator subcodes = fault.getFault().getFaultSubcodes();
      QName subcode = null;
      while (subcodes.hasNext()) {
         subcode = (QName) subcodes.next();
      }

      // Vérifie que c'est bien une SoapFault
      assertTrue(
                 "L'AxisFault obtenue n'est pas une SoapFault (" + fault.getMessage() + ")",
                 isSoapFault(fault));

      // Vérifie le code de la SoapFault
      if (subcode != null) {
         // Le namespace
         assertEquals(
                      "Le namespace du code de la SoapFault est incorrect",
                      codeNamespaceAttendu,
                      subcode.getNamespaceURI());
         // Le préfixe
         assertEquals(
                      "Le préfixe du code de la SoapFault est incorrect",
                      codePrefixeAttendu,
                      subcode.getPrefix());
         // La partie locale
         assertEquals(
                      "La partie locale du code de la SoapFault est incorrecte",
                      codePartieLocaleAttendu,
                      subcode.getLocalPart());
      } else {
         // Le namespace
         assertEquals(
                      "Le namespace du code de la SoapFault est incorrect",
                      codeNamespaceAttendu,
                      subcode.getNamespaceURI());
         // Le préfixe
         assertEquals(
                      "Le préfixe du code de la SoapFault est incorrect",
                      codePrefixeAttendu,
                      subcode.getPrefix());
         // La partie locale
         assertEquals(
                      "La partie locale du code de la SoapFault est incorrecte",
                      codePartieLocaleAttendu,
                      subcode.getLocalPart());
      }

      // Vérifie le message de la SoapFault
      assertEquals(
                   "Le message de la SoapFault est incorrecte",
                   messageAttendu,
                   fault.getMessage());

   }

}
