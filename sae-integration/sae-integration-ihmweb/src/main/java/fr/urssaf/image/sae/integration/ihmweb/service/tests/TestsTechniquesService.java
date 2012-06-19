package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestTechnique;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.PingRequest;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.PingResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.PingSecureRequest;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.PingSecureResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.utils.LogUtils;
import fr.urssaf.image.sae.integration.ihmweb.utils.SoapFaultUtils;


/**
 * Services pour les tests dits "techniques"<br>
 * <br>
 * Cette classe est utilisée par le contrôleur Tests995Controller
 */
@Service
@SuppressWarnings("PMD.TooManyMethods")
public class TestsTechniquesService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;
   
   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;
   
   private void echecSoapFault(
         TestTechnique leTest,
         AxisFault fault) {
      leTest.setStatus(TestStatusEnum.Echec);
      leTest.getDetails().appendLogLn("On a obtenu une SoapFault :");
      leTest.getDetails().appendLogNewLine();
      LogUtils.logSoapFault(leTest.getDetails(), fault);
   }
   
   
   private void echecRemote(
         TestTechnique leTest,
         RemoteException exception) {
      leTest.setStatus(TestStatusEnum.Echec);
      leTest.getDetails().appendLogLn("Une exception non SoapFault a été levée :");
      leTest.getDetails().appendLogNewLine();
      leTest.getDetails().appendLogLn(exception.toString());
   }
   
   
   private void checkResultatString(
         TestTechnique leTest,
         String attendu,
         String obtenu) {
      
      // Vérifie que la réponse est conforme à l'attendu
      if (obtenu.equals(attendu)) {
         
         // Le test est en succès
         leTest.setStatus(TestStatusEnum.Succes);
         leTest.getDetails().appendLog("La réponse obtenue est correcte (\"" + obtenu + "\").");
         
      } else {
         
         // Le test est en échec
         leTest.setStatus(TestStatusEnum.Echec);
         leTest.getDetails().appendLog(
               "La réponse obtenue (\"" + obtenu + 
               "\") ne correspond pas à la réponse attendue (\"" + attendu + 
               "\")");
         
      }
      
   }
   
   
   
   /**
    * 001_Ping_ok<br>
    * <br>
    * Appel de l'opération "ping".<br>
    * On attend en retour la chaîne de caractères "Les services SAE sont en ligne".
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest001(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
      
         // Récupération du stub du service web avec le VI adéquat
         SaeServiceStub service = saeServiceStubUtils.getServiceStubSansVi(urlServiceWeb);
         
         // Construction des paramètres d'appel à l'opération
         PingRequest request = new PingRequest();
         
         // Appel du service web
         try {
            
            // Appel
            PingResponse response = service.ping(request);
            
            // Vérifie que la réponse est conforme à l'attendu
            String attendu = "Les services SAE sont en ligne";
            String obtenu = response.getPingString();
            checkResultatString(leTest,attendu,obtenu);
            
         } catch (AxisFault fault) {
            
            // Le test est en échec
            echecSoapFault(leTest,fault);
            
         } catch (RemoteException e) {
            
            // Le test est en échec
            echecRemote(leTest,e);
            
         }
         
      }
   }
   
   
   /**
    * 002_PingSecure_ok<br>
    * <br>
    * Appel de l'opération "pingSecure".<br>
    * On attend en retour la chaîne de caractères "Les services du SAE sécurisés par authentification sont en ligne".
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest002(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         // Récupération du stub du service web avec le VI adéquat
         SaeServiceStub service = saeServiceStubUtils.getServiceStubAvecViOk(urlServiceWeb);
         
         // Construction des paramètres d'appel à l'opération
         PingSecureRequest request = new PingSecureRequest();
         
         // Appel du service web
         try {
            
            // Appel
            PingSecureResponse response = service.pingSecure(request);
            
            // Vérifie que la réponse est conforme à l'attendu
            String attendu = "Les services du SAE sécurisés par authentification sont en ligne";
            String obtenu = response.getPingString();
            checkResultatString(leTest,attendu,obtenu);
            
         } catch (AxisFault fault) {
            
            // Le test est en échec
            echecSoapFault(leTest,fault);
            
         } catch (RemoteException e) {
            
            // Le test est en échec
            echecRemote(leTest,e);
            
         }
         
      }
      
   }
   
   
   /**
    * 003_PingSecure-SoapFault_wsse_SecurityTokenUnavailable
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest003(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_wsse_SecurityTokenUnavailable,
               refSoapFault.findSoapFault("wsse_SecurityTokenUnavailable"));
         
      }
      
   }
   
   
   private void pingSecureTestSoapfault(
         String urlServiceWeb,
         TestTechnique leTest,
         ViStyle viStyle,
         SoapFault soapFaultAttendu) {
    
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(urlServiceWeb, viStyle);
      
      PingSecureRequest request = new PingSecureRequest();

      try {
         
         // Appel de l'opération pingSecure
         service.pingSecure(request);
         
         // Echec du test, car on aurait du avoir une SoapFault
         leTest.setStatus(TestStatusEnum.Echec);
         leTest.getDetails().appendLogLn(
               "On aurait dû obtenir une SoapFault (" +
               soapFaultAttendu.codeToString() +
               ")");
         
      } catch (AxisFault faultObtenue) {
         
         // Vérifie la SoapFault
         String erreur = SoapFaultUtils.checkSoapFault(
               faultObtenue,
               soapFaultAttendu,
               null);
         
         // Bonne SoapFault ou pas ?
         if (StringUtils.isEmpty(erreur)) {
            
            // La SoapFault levée est bien celle attendue
            leTest.setStatus(TestStatusEnum.Succes);
            
            // Message de Log
//            leTest.getDetails().appendLogLn(
//                  "On a obtenu la SoapFault attendue (" + 
//                  soapFaultAttendu.codeToString() + ")");
            leTest.getDetails().appendLogLn(
                  "On a obtenu la SoapFault attendue (" + 
                  soapFaultAttendu.codeToString() + ", \"" +   
                  soapFaultAttendu.getMessage() + "\")");
            
         } else {
            
            // La SoapFault levée n'est pas celle attendue
            leTest.setStatus(TestStatusEnum.Echec);
            
            // Message de Log
            SoapFaultUtils.ajouteDansLogSoapFaultObtenuePasCelleAttendue(
                  leTest.getDetails(),
                  erreur,
                  faultObtenue,
                  soapFaultAttendu,
                  null);
            
         }

      } catch (RemoteException e) {
         
         // Le test est en échec
         echecRemote(leTest,e);
         
      }
      
   }
   
   
   /**
    * 004_PingSecure-SoapFault_wsse_InvalidSecurityToken
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest004(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_wsse_InvalidSecurityToken,
               refSoapFault.findSoapFault("wsse_InvalidSecurityToken"));
         
      }
      
   }
   
   
   /**
    * 005_PingSecure-SoapFault_wsse_FailedCheck
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest005(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_wsse_FailedCheck,
               refSoapFault.findSoapFault("wsse_FailedCheck"));
         
      }
      
   }
   
   
   /**
    * 006_PingSecure-SoapFault_vi_InvalidVI
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest006(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_vi_InvalidVI,
               refSoapFault.findSoapFault("vi_InvalidVI"));
         
      }
      
   }
   
   
   /**
    * 007_PingSecure-SoapFault_vi_InvalidService
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest007(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_vi_InvalidService,
               refSoapFault.findSoapFault("vi_InvalidService"));
         
      }
      
   }
   
   
   /**
    * 008_PingSecure-SoapFault_vi_InvalidAuthLevel
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest008(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_vi_InvalidAuthLevel,
               refSoapFault.findSoapFault("vi_InvalidAuthLevel"));
         
      }
      
   }
   
   
   /**
    * 010_PingSecure-SoapFault_sae_DroitsInsuffisants
    * 
    * @param urlServiceWeb l'URL du service web SaeService
    * @param leTest l'objet du cas de test
    */
   public final void casTest010(
         String urlServiceWeb,
         TestTechnique leTest) {
      
      if (leTest.isaLancer()) {
         
         pingSecureTestSoapfault(
               urlServiceWeb,
               leTest,
               ViStyle.VI_SF_sae_DroitsInsuffisants,
               refSoapFault.findSoapFault("sae_DroitsInsuffisants"));
         
      }
      
   }
   
}
