package fr.urssaf.image.sae.integration.ihmweb.utils;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;


/**
 * Méthodes utilitaires pour la manipulation des SoapFault dans le
 * cadre des tests d'intégration
 */
public final class SoapFaultUtils {

   
   private SoapFaultUtils() {
      
   }
   
   
   /**
    * Vérifie du code et du message d'une SoapFault par rapport à un attendu
    * 
    * @param faultObtenue la SoapFault que l'on veut vérifier
    * @param faultAttendue la SoapFault attendue
    * @param argsMsgSoapFault les arguments pour le String.format du message de la SoapFault attendue
    * @return un message d'erreur si la SoapFault ne correspond pas à l'attendu, ou null
    * dans le cas contraire.
    */
   @SuppressWarnings("PMD.OnlyOneReturn")
   public static String checkSoapFault(
         AxisFault faultObtenue,
         SoapFault faultAttendue,
         Object[] argsMsgSoapFault) {
      
      String attendu;
      String obtenu;
      
      // Vérifie le message de la Soap Fault
      attendu = String.format(faultAttendue.getMessage(),argsMsgSoapFault);
      obtenu = faultObtenue.getReason();
            
      if (!attendu.equals(obtenu)) {
         return String.format(
               "Le message de la SoapFault (\"%s\") n'est pas celui attendu (\"%s\")", 
               obtenu,
               attendu);
      }
      
      // Vérifie le code de la Soap Fault
      
      // Le namespace
      attendu = faultAttendue.getCode().getNamespaceURI();
      obtenu = faultObtenue.getFaultCode().getNamespaceURI();
      if (!attendu.equals(obtenu)) {
         return String.format(
               "Le namespace du code de la SoapFault (\"%s\") n'est pas celui attendu (\"%s\")", 
               obtenu,
               attendu);
      }
      
      // Le préfixe
      attendu = faultAttendue.getCode().getPrefix();
      obtenu = faultObtenue.getFaultCode().getPrefix();
      if (!attendu.equals(obtenu)) {
         return String.format(
               "Le préfixe du code de la SoapFault (\"%s\") n'est pas celui attendu (\"%s\")", 
               obtenu,
               attendu);
      }
      
      // La partie locale
      attendu = faultAttendue.getCode().getLocalPart();
      obtenu = faultObtenue.getFaultCode().getLocalPart();
      if (!attendu.equals(obtenu)) {
         return String.format(
               "La partie locale de la SoapFault (\"%s\") n'est pas celle attendue (\"%s\")", 
               obtenu,
               attendu);
    }
      
      // Si on arrive jusque là, c'est que la SoapFault est bien celle attendu
      return null;
      
   }
   
   
   /**
    * Ajout dans le log d'un résultat de test un message indiquant que la
    * SoapFault obtenue n'est pas celle attendue.<br>
    * Ecrit dans le log la définition de l'attendu et de l'obtenu.
    * 
    * @param log les logs à enrichir
    * @param erreur le message d'erreur spécifique à rajouter au log
    * @param faultObtenue la SoapFault obtenue
    * @param faultAttendue la SoapFault attendue
    * @param argsMsgSoapFault les arguments pour le String.format du message de la SoapFault attendue
    */
   public static void ajouteDansLogSoapFaultObtenuePasCelleAttendue(
         ResultatTestLog log,
         String erreur,
         AxisFault faultObtenue,
         SoapFault faultAttendue,
         Object[] argsMsgSoapFault) {
      
      log.appendLogLn("La SoapFault renvoyée n'est pas celle attendue");
      log.appendLogLn(erreur);
      
      log.appendLogNewLine();
      log.appendLogLn("SoapFault renvoyée :");
      LogUtils.logSoapFault(log,faultObtenue);
      
      
      log.appendLogNewLine();
      log.appendLogLn("SoapFault attendue :");
      LogUtils.logSoapFault(
            log,
            faultAttendue.getCode(),
            String.format(faultAttendue.getMessage(),argsMsgSoapFault));
      
   }
   
   
   /**
    * Ajout dans le log d'un résultat de test un message indiquant que la
    * SoapFault obtenue est bien celle que l'on attendait.<br>
    * Ecrit dans le log la définition de l'attendu/obtenu.
    * 
    * @param log le log à enrichir
    * @param faultObtenue la SoapFault obtenue
    */
   public static void ajouteDansLogSoapFaultObtenueEstCelleAttendue(
         ResultatTestLog log,
         AxisFault faultObtenue) {
      
      log.appendLogLn("On a bien récupéré la SoapFault attendue :");
      
      log.appendLogNewLine();
      LogUtils.logSoapFault(log, faultObtenue);
      
   }
   
}
