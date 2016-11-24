package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CopieResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Copie;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.CopieResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplReponseAttendue;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.utils.TestsMetadonneesService;

/**
 * Service de test de l'opération "consultation" du service web SaeService
 */
@SuppressWarnings( { "PMD.CyclomaticComplexity", "PMD.NPathComplexity",
"PMD.ExcessiveMethodLength" })
@Service
public class CopieTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private TestsMetadonneesService testMetaService;

   @Autowired
   private ReferentielMetadonneesService referentielMetadonneesService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private CopieResultat appelWsOpCopie(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams, CopieFormulaire formulaire,
         WsTestListener wsListener) {

      // Initialise le résultat
      CopieResultat result = null;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelCopie(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // Nouveau service sans MTOM

         // Construction du paramètre d'entrée de l'opération
         Copie paramsService = SaeServiceObjectFactory.buildCopieRequest(
               formulaire.getIdGed(), formulaire.getListeMetadonnees());

         // Appel du service web
         CopieResponse response = service.copie(paramsService);

         // Transtypage de l'objet de la couche ws vers l'objet du modèle
         result = fromCopieNouveauService(response);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"copie\" :");
         SaeServiceLogUtils.logResultatCopie(resultatTest, result);

      } catch (AxisFault fault) {

         // Appel du listener
         wsListener.onSoapFault(resultatTest, fault, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

      } catch (RemoteException e) {

         // Appel du listener
         wsListener.onRemoteException(resultatTest, e, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

      }

      // Ajoute le timestamp en 1ère ligne du log
      log.insertTimestamp();

      // Renvoie du résultat
      return result;

   }

   /**
    * Methode permettant de
    * 
    * @param response
    * @return
    */
   private CopieResultat fromCopieNouveauService(
         CopieResponse response) {
      return new CopieResultat(response.getCopieResponse().getIdGed());
   }

   /**
    * Test libre de l'appel à l'opération "copie" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final CopieResultat appelWsOpCopieTestLibre(String urlServiceWeb,
         CopieFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      return appelWsOpCopie(urlServiceWeb, ViStyle.VI_OK, null, formulaire, testLibre);

   }

   /**
    * Test libre de l'appel à l'opération "copie" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final CopieResultat appelWsOpCopie(String urlServiceWeb, CopieFormulaire formulaire) {
      return this.appelWsOpCopie(urlServiceWeb, formulaire, null, null, null);
   }

   /**
    * 
    * Test libre de l'appel à l'opération "copie" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param faultAttendue SOAP Fault attendue
    * @param argsMsgSoapFault Argument SOAP Fault
    */
   public final CopieResultat appelWsOpCopie(String urlServiceWeb,
         CopieFormulaire formulaire, SoapFault faultAttendue, Object[] argsMsgSoapFault, ViFormulaire viParams) {
      WsTestListener testListener = null;
      boolean isSoapFault = faultAttendue != null;
      if (!isSoapFault) {
         // Création de l'objet qui implémente l'interface WsTestListener
         // et qui attend obligatoirement une réponse.
         testListener = new WsTestListenerImplReponseAttendue();       
      } else {
         // Création de l'objet qui implémente l'interface WsTestListener
         // et qui attend obligatoirement une réponse.
         testListener = new WsTestListenerImplSoapFault(faultAttendue, argsMsgSoapFault);  
      }

      // Appel de la méthode "générique" de test
      CopieResultat resultat = appelWsOpCopie(urlServiceWeb, ViStyle.VI_OK,
            viParams, formulaire,
            testListener);

      ResultatTest resultatTest = formulaire.getResultats();

      if ((resultat != null)
            && (TestStatusEnum.NonLance.equals(resultatTest.getStatus()))) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      } else {
         resultatTest.setStatus(TestStatusEnum.Echec);
      }

      return resultat;
   }


   /**
    * Test libre de l'appel à l'opération "copie" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParamsForm
    *           les paramètres du VI
    */
   public final CopieResultat appelWsOpCopie(String urlServiceWeb,
         CopieFormulaire formulaire, ViFormulaire viParamsForm) {
      return this.appelWsOpCopie(urlServiceWeb, formulaire, null, null, viParamsForm);

   }

}
