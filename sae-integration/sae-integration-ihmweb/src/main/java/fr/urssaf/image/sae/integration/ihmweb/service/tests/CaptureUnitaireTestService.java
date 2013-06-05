package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitairePJ;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitairePJResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaireResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceTypeUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplReponseAttendue;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;

/**
 * Service de tests de l'opération "archivageUnitaire" du service SaeService
 */
@Service
public class CaptureUnitaireTestService {

   @Autowired
   private EcdeService ecdeService;

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private CaptureUnitaireResultat appelWsOpCaptureUnitaire(
         String urlServiceWeb, ViStyle viStyle, ViFormulaire viParams,
         CaptureUnitaireFormulaire formulaire, WsTestListener wsListener) {

      // Initialise la valeur de retour
      CaptureUnitaireResultat result = new CaptureUnitaireResultat();

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log
      SaeServiceLogUtils.logAppelArchivageUnitaire(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // La valeur de retour d'une capture unitaire
         String idArchivage;

         // Selon le mode d'appel à la capture unitaire
         // Opération "archivageUnitaire" ou "archivageUnitairePJ"
         // Si "archivageUnitairePJ", avec URL ECDE ou contenu sans MTOM
         // ou contenu avec MTOM
         if (ModeArchivageUnitaireEnum.archivageUnitaire.equals(formulaire
               .getModeCapture())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "archivageUnitaire"

            // Construction du paramètre d'entrée de l'opération
            ArchivageUnitaire paramsService = SaeServiceObjectFactory
                  .buildArchivageUnitaireRequest(formulaire.getUrlEcde(),
                        formulaire.getMetadonnees());

            // Appel du service web
            ArchivageUnitaireResponse response = service
                  .archivageUnitaire(paramsService);

            // Appel du listener
            wsListener.onRetourWsSansErreur(resultatTest, service
                  ._getServiceClient().getServiceContext()
                  .getConfigurationContext(), formulaire.getParent());

            // Récupère l'identifiant d'archivage renvoyé
            idArchivage = SaeServiceTypeUtils.extractUuid(response
                  .getArchivageUnitaireResponse().getIdArchive());

         } else if (ModeArchivageUnitaireEnum.archivageUnitairePJUrlEcde
               .equals(formulaire.getModeCapture())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "archivageUnitairePJ" avec URL ECDE

            idArchivage = appelArchivageUnitairePJavecUrlEcde(service,
                  wsListener, resultatTest, formulaire);

         } else if (ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom
               .equals(formulaire.getModeCapture())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "archivageUnitairePJ" avec contenu sans MTOM

            idArchivage = appelArchivageUnitairePJavecContenu(service,
                  wsListener, resultatTest, formulaire);

         } else if (ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom
               .equals(formulaire.getModeCapture())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "archivageUnitairePJ" avec contenu avec MTOM

            activeMtom(service);

            idArchivage = appelArchivageUnitairePJavecContenu(service,
                  wsListener, resultatTest, formulaire);

         } else {
            throw new IntegrationRuntimeException(
                  "Le mode de capture unitaire '" + formulaire.getModeCapture()
                        + "' est inconnu");
         }

         // Log de la réponse obtenue
         log.appendLogNewLine();
         log
               .appendLogLn("Détails de la réponse obtenue de l'opération \"archivageUnitaire\" :");
         SaeServiceLogUtils.logResultatCaptureUnitaire(resultatTest,
               idArchivage);

         // Affecte l'identifiant d'archivage à l'objet de réponse de la méthode
         result.setIdArchivage(idArchivage);

         // Calcul du SHA-1 du fichier envoyé, et affectation à l'objet de
         // réponse
         String sha1 = ecdeService.sha1(formulaire.getUrlEcde());
         result.setSha1(sha1);
         log.appendLogNewLine();
         log
               .appendLogLn("Pour information, le SHA-1 du fichier envoyé à la capture est : ");
         log.appendLogLn(sha1);

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
    * Test libre de l'appel à l'opération "archivageUnitaire" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpCaptureUnitaireUrlEcdeTestLibre(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire) {

      appelWsOpCaptureUnitaireUrlEcdeTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "archivageUnitaire" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpCaptureUnitaireUrlEcdeTestLibre(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire,
         ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpCaptureUnitaire(urlServiceWeb, ViStyle.VI_OK, viParams,
            formulaire, testLibre);

   }

   /**
    * Test d'appel à l'opération "archivageUnitaire" du service web SaeService.<br>
    * <br>
    * On s'attend à obtenir une SoapFault.
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viStyle
    *           le type de VI à générer
    * @param idSoapFaultAttendu
    *           l'identifiant de la SoapFault attendu dans le référentiel des
    *           SoapFault
    * @param argsMsgSoapFault
    *           les arguments pour le String.format du message de la SoapFault
    *           attendue
    */
   public final void appelWsOpCaptureUnitaireSoapFault(String urlServiceWeb,
         CaptureUnitaireFormulaire formulaire, ViStyle viStyle,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpCaptureUnitaire(urlServiceWeb, viStyle, null, formulaire,
            listener);

   }
   
   /**
    * Test d'appel à l'opération "archivageUnitaire" du service web SaeService.<br>
    * <br>
    * On s'attend à obtenir une SoapFault.
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viStyle
    *           le type de VI à générer
    * @param viParams
    *           les paramètres du VI
    * @param idSoapFaultAttendu
    *           l'identifiant de la SoapFault attendu dans le référentiel des
    *           SoapFault
    * @param argsMsgSoapFault
    *           les arguments pour le String.format du message de la SoapFault
    *           attendue
    */
   public final void appelWsOpCaptureUnitaireSoapFault(String urlServiceWeb,
         CaptureUnitaireFormulaire formulaire, ViStyle viStyle, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpCaptureUnitaire(urlServiceWeb, viStyle, viParams, formulaire,
            listener);

   }

   /**
    * Test d'appel à l'opération "archivageUnitaire" du service web SaeService.<br>
    * On s'attend à récupérer une réponse sans erreur
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @return les informations résultant de la capture unitaire
    */
   public final CaptureUnitaireResultat appelWsOpCaptureUnitaireReponseAttendue(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      CaptureUnitaireResultat resultat = appelWsOpCaptureUnitaire(
            urlServiceWeb, ViStyle.VI_OK, null, formulaire, testAvecReponse);

      // On considère que le test est en succès si aucune erreur renvoyé
      ResultatTest resultatTest = formulaire.getResultats();
      if ((resultat != null)
            && (TestStatusEnum.NonLance.equals(resultatTest.getStatus()))) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      } else {
         resultatTest.setStatus(TestStatusEnum.Echec);
      }

      // Renvoie le résultat
      return resultat;

   }
   
   
   /**
    * Test d'appel à l'opération "archivageUnitaire" du service web SaeService.<br>
    * On s'attend à récupérer une réponse sans erreur
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @return les informations résultant de la capture unitaire
    */
   public final CaptureUnitaireResultat appelWsOpCaptureUnitaireReponseAttendue(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      CaptureUnitaireResultat resultat = appelWsOpCaptureUnitaire(
            urlServiceWeb, ViStyle.VI_OK, viParams, formulaire, testAvecReponse);

      // On considère que le test est en succès si aucune erreur renvoyé
      ResultatTest resultatTest = formulaire.getResultats();
      if ((resultat != null)
            && (TestStatusEnum.NonLance.equals(resultatTest.getStatus()))) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      } else {
         resultatTest.setStatus(TestStatusEnum.Echec);
      }

      // Renvoie le résultat
      return resultat;

   }

   private DataHandler buildDataHandler(String urlEcde) {

      String cheminFichier = ecdeService.convertUrlEcdeToPath(urlEcde);

      FileDataSource fileDataSource = new FileDataSource(cheminFichier);

      DataHandler dataHandler = new DataHandler(fileDataSource);

      return dataHandler;

   }

   private String appelArchivageUnitairePJ(SaeServiceStub service,
         ArchivageUnitairePJ paramsService, WsTestListener wsListener,
         ResultatTest resultatTest, CaptureUnitaireFormulaire formulaire)
         throws RemoteException {

      // Appel du service web
      ArchivageUnitairePJResponse response = service
            .archivageUnitairePJ(paramsService);

      // Appel du listener
      wsListener.onRetourWsSansErreur(resultatTest, service._getServiceClient()
            .getServiceContext().getConfigurationContext(), formulaire
            .getParent());

      // Récupère l'identifiant d'archivage renvoyé
      String idArchivage = SaeServiceTypeUtils.extractUuid(response
            .getArchivageUnitairePJResponse().getIdArchive());
      return idArchivage;

   }

   private String appelArchivageUnitairePJavecUrlEcde(SaeServiceStub service,
         WsTestListener wsListener, ResultatTest resultatTest,
         CaptureUnitaireFormulaire formulaire) throws RemoteException {

      // Construction du paramètre d'entrée de l'opération
      ArchivageUnitairePJ paramsService = SaeServiceObjectFactory
            .buildArchivageUnitairePJRequestAvecUrlEcde(
                  formulaire.getUrlEcde(), formulaire.getMetadonnees());

      // Appel du service web
      String idArchivage = appelArchivageUnitairePJ(service, paramsService,
            wsListener, resultatTest, formulaire);
      return idArchivage;

   }

   private String appelArchivageUnitairePJavecContenu(SaeServiceStub service,
         WsTestListener wsListener, ResultatTest resultatTest,
         CaptureUnitaireFormulaire formulaire) throws RemoteException {

      // Construction du paramètre d'entrée de l'opération
      ArchivageUnitairePJ paramsService = SaeServiceObjectFactory
            .buildArchivageUnitairePJRequestAvecContenu(
                  buildDataHandler(formulaire.getUrlEcde()), formulaire
                        .getNomFichier(), formulaire.getMetadonnees());

      // Appel du service web
      String idArchivage = appelArchivageUnitairePJ(service, paramsService,
            wsListener, resultatTest, formulaire);
      return idArchivage;

   }

   private void activeMtom(SaeServiceStub service) {

      service._getServiceClient().getOptions().setProperty(
            Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);

   }

}
