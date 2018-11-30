package fr.urssaf.image.sae.integration.ihmweb.saeservice.service;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.utils.SoapFaultUtils;

/**
 * Méthodes utilitaires pour les tests du service web SaeService
 */
@Service
public class SaeServiceTestService {

   
   // private static final Logger LOG = Logger.getLogger(SaeServiceTestUtils.class);
   
   @Autowired
   private ReferentielSoapFaultService refSoapFault;
   
   
   /**
    * Vérifie que la SoapFault passée en paramètre corresponde à la SoapFault
    * attendue dans le cas où l'authentification n'est pas présente dans le
    * message SOAP de requête.<br>
    * Met à jour l'objet resultatTest si une erreur est détecté
    * 
    * @param faultObtenue la SoapFault à vérifier
    * @param resultatTest le résultat des tests à mettre à jour
    */
   public final void checkSoapFaultAuthentificationNonPresente(
         AxisFault faultObtenue,
         ResultatTest resultatTest) {
      
      SoapFault faultAttendue = refSoapFault.findSoapFault("wsse_SecurityTokenUnavailable") ;
      
      checkSoapFault(faultObtenue,resultatTest,faultAttendue,null);
      
   }
   
   
   /**
    * Vérifie que la SoapFault obtenue est bien celle attendue
    * 
    * @param faultObtenue la SoapFault obtenue 
    * @param resultatTest l'objet de résultat des tests
    * @param faultAttendue la SoapFault attendue
    * @param argsMsgSoapFault les arguments pour le String.format du message de la SoapFault attendue
    */
   public static final void checkSoapFault(
         AxisFault faultObtenue,
         ResultatTest resultatTest,
         SoapFault faultAttendue,
         Object[] argsMsgSoapFault) {
      
      // Vérification de la SoapFault
      String erreur = SoapFaultUtils.checkSoapFault(
            faultObtenue, 
            faultAttendue,
            argsMsgSoapFault);
      
      // Bonne SoapFault ou pas ?
      if (StringUtils.isEmpty(erreur)) {
         
         // La SoapFault levée est bien celle attendue
         resultatTest.setStatus(TestStatusEnum.Succes);
         
         // Message de Log
         SoapFaultUtils.ajouteDansLogSoapFaultObtenueEstCelleAttendue(
               resultatTest.getLog(),
               faultObtenue);
         
      } else {
         
         // La SoapFault levée n'est pas celle attendue
         resultatTest.setStatus(TestStatusEnum.Echec);
         
         // Message de Log
         SoapFaultUtils.ajouteDansLogSoapFaultObtenuePasCelleAttendue(
               resultatTest.getLog(),
               erreur,
               faultObtenue,
               faultAttendue,
               null);
         
      }
      
   }
   
   
   /**
    * Log une exception non prévue dans l'objet resultatTest
    * 
    * @param exception l'exception non prévue
    * @param resultatTest l'objet resultatTest à mettre à jour
    */
   public static final void exceptionNonPrevue(
         Exception exception,
         ResultatTest resultatTest) {
      // Une erreur non prévue a été levée
      resultatTest.setStatus(TestStatusEnum.Echec);
      resultatTest.getLog().appendLogLn("Une erreur non prévue s'est produite : ");
      resultatTest.getLog().appendLogNewLine();
      resultatTest.getLog().appendLogLn(exception.toString());
   }
   
   
   /**
    * Renvoi un identifiant d'archivage "Exemple"<br>
    * <br>
    * Cela permet par exemple d'initialiser les IHM avec un
    * modèle d'identifiant d'archivage, pour rappeler à 
    * l'utilisateur le format de cet identifiant (UUID).
    * 
    * @return un identifiant d'archivage "Exemple"
    */
   public static final String getIdArchivageExemple() {
      return "F81D4FAE-7DEC-11D0-A765-00A0C91E6BF6";
   }
   
}
