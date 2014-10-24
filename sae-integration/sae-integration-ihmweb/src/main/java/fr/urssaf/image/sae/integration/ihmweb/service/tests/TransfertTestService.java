package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Transfert;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplReponseAttendue;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;

/**
 * Service de tests de l'opération "transfert" du service SaeService
 */
@Service
public class TransfertTestService {

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;
   
   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   private boolean appelWsOpTransfert(String urlServiceWeb, ViStyle viStyle,
         ViFormulaire viParams, TransfertFormulaire formulaire,
         WsTestListener wsListener) {

      boolean transfertOK = true;
      
      // Initialise la valeur de retour
      // CaptureUnitaireResultat result = new CaptureUnitaireResultat();

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log
      SaeServiceLogUtils.logAppelTransfert(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // Construction du paramètre d'entrée de l'opération
         Transfert paramsService = SaeServiceObjectFactory.buildTransfertRequest(formulaire.getIdDocument());

         // Appel du service web
         service.transfert(paramsService);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogNewLine();
         log.appendLogLn("Aucune erreur levée");

      } catch (AxisFault fault) {

         // Appel du listener
         wsListener.onSoapFault(resultatTest, fault, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
         
         transfertOK = false;

      } catch (RemoteException e) {

         // Appel du listener
         wsListener.onRemoteException(resultatTest, e, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         transfertOK = false;
      }

      // Ajoute le timestamp en 1ère ligne du log
      log.insertTimestamp();

      return transfertOK;
   }

   /**
    * Test libre de l'appel à l'opération "transfert" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpTransfertTestLibre(
         String urlServiceWeb, TransfertFormulaire formulaire) {

      appelWsOpTransfertTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "transfert" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpTransfertTestLibre(String urlServiceWeb,
         TransfertFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpTransfert(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,
            testLibre);

   }

   /**
    * Test avec reponse attendue de l'appel à l'opération "transfert" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpTransfertReponseAttendue(
         String urlServiceWeb, TransfertFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      boolean resultat = appelWsOpTransfert(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,
            testAvecReponse);

      // On considère que le test est en succès si aucune erreur renvoyé
      ResultatTest resultatTest = formulaire.getResultats();
      if (resultat) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      } 
   }

   /**
    * Test de l'appel à l'opération "transfert" du service web
    * SaeService.<br>
    * On s'attend à obtenir une SoapFault.
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    * @param idSoapFaultAttendu
    *           l'identifiant de la SoapFault attendu dans le référentiel des
    *           SoapFault
    * @param argsMsgSoapFault
    *           les arguments pour le String.format du message de la SoapFault
    *           attendue
    */
   public final void appelWsOpTransfertSoapFault(
         String urlServiceWeb, TransfertFormulaire formulaire, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      boolean resultat = appelWsOpTransfert(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,
            listener);
      
      // On considère que le test est en succès si aucune erreur renvoyé
      ResultatTest resultatTest = formulaire.getResultats();
      if (!resultat) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      } 
   }
}
