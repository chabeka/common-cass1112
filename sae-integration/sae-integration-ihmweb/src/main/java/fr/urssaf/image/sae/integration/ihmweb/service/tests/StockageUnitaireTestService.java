package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.StockageUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.StockageUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeStockageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.StockageUnitaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.StockageUnitaireResponse;
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
 * Service de tests de l'opération "stockageUnitaire" du service SaeService
 */
@Service
public class StockageUnitaireTestService {

   @Autowired
   private EcdeService ecdeService;

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private StockageUnitaireResultat appelWsOpStockageUnitaire(
         String urlServiceWeb, ViStyle viStyle, ViFormulaire viParams,
         StockageUnitaireFormulaire formulaire, WsTestListener wsListener) {

      // Initialise la valeur de retour
      StockageUnitaireResultat result = new StockageUnitaireResultat();

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log
      SaeServiceLogUtils.logAppelStockageUnitaire(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // La valeur de retour d'une capture unitaire
         String idArchivage;
         
         System.out.println("ModeStockage : "+formulaire.getModeStockage());
         
         // Selon le mode d'appel au stockage unitaire
         // stockageUnitaire avec UrlEcde : stockageUnitaireAvecUrlEcde       
         // stockageUnitaire avec contenu avec MTOM : stockageUnitaireAvecContenuAvecMTOM
         // stockageUnitaire avec contenu sans MTOM  : stockageUnitaireAvecContenuSansMTOM         
         
         if (ModeStockageUnitaireEnum.stockageUnitaireAvecUrlEcde.equals(formulaire
               .getModeStockage())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "stockageUnitaireAvecUrlEcde"

            // Construction du paramètre d'entrée de l'opération
            StockageUnitaire paramsService = SaeServiceObjectFactory
                  .buildStockageUnitaireRequest(formulaire.getUrlEcde(),
                        formulaire.getUrlEcdeOrig(),formulaire.getMetadonnees());

            // Appel du service web
            StockageUnitaireResponse response = service
                  .stockageUnitaire(paramsService);

            // Appel du listener
            wsListener.onRetourWsSansErreur(resultatTest, service
                  ._getServiceClient().getServiceContext()
                  .getConfigurationContext(), formulaire.getParent());

            // Récupère l'identifiant d'archivage renvoyé
            idArchivage = SaeServiceTypeUtils.extractUuid(response
                  .getStockageUnitaireResponse().getIdGed());
            

         } else if (ModeStockageUnitaireEnum.stockageUnitaireAvecContenuSansMTOM
               .equals(formulaire.getModeStockage())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "stockageUnitaireAvecContenuSansMTOM" avec contenu sans MTOM

            // Construction du paramètre d'entrée de l'opération
            DataHandler contenuNomFichier = buildDataHandler(formulaire.getUrlEcde());

            // Fichier au format d'origine à rattacher
            DataHandler contenuNomFichierOrig = buildDataHandler(formulaire.getUrlEcdeOrig());
            InputStream is_contenuNomFichierOrig = null;
            InputStream is_contenuNomFichier = null;

            try {
               is_contenuNomFichier = contenuNomFichier.getInputStream();
            } catch (IOException e) {
                   e.printStackTrace();
            }
            
            try {
               is_contenuNomFichierOrig = contenuNomFichierOrig.getInputStream();
            } catch (IOException e) {
                   e.printStackTrace();
            }
            
            StockageUnitaire paramsService = SaeServiceObjectFactory
            .buildStockageUnitaireRequestavecContenu(
                  formulaire.getUrlEcde(),
                  is_contenuNomFichier,
                  formulaire.getUrlEcdeOrig(),
                  is_contenuNomFichierOrig,
                  formulaire.getMetadonnees());
            
            // Appel du service web
            StockageUnitaireResponse response = service
                  .stockageUnitaire(paramsService);

            // Appel du listener
            wsListener.onRetourWsSansErreur(resultatTest, service
                  ._getServiceClient().getServiceContext()
                  .getConfigurationContext(), formulaire.getParent());

            // Récupère l'identifiant d'archivage renvoyé
            idArchivage = SaeServiceTypeUtils.extractUuid(response
                  .getStockageUnitaireResponse().getIdGed());
  
         } else if (ModeStockageUnitaireEnum.stockageUnitaireAvecContenuAvecMTOM
               .equals(formulaire.getModeStockage())) {

            // Mode d'appel à la capture unitaire :
            // => Opération "stockageUnitaireAvecContenuAvecMTOM" avec contenu avec MTOM

            activeMtom(service);

            // Construction du paramètre d'entrée de l'opération
            DataHandler contenuNomFichier = buildDataHandler(formulaire.getUrlEcde());

            // Fichier au format d'origine à rattacher
            DataHandler contenuNomFichierOrig = buildDataHandler(formulaire.getUrlEcdeOrig());
            InputStream is_contenuNomFichierOrig = null;
            InputStream is_contenuNomFichier = null;

            try {
               is_contenuNomFichier = contenuNomFichier.getInputStream();
            } catch (IOException e) {
                   e.printStackTrace();
            }
            
            try {
               is_contenuNomFichierOrig = contenuNomFichierOrig.getInputStream();
            } catch (IOException e) {
                   e.printStackTrace();
            }
            
            StockageUnitaire paramsService = SaeServiceObjectFactory
            .buildStockageUnitaireRequestavecContenu(
                  formulaire.getUrlEcde(),
                  is_contenuNomFichier,
                  formulaire.getUrlEcdeOrig(),
                  is_contenuNomFichierOrig,
                  formulaire.getMetadonnees());
            
            // Appel du service web
            StockageUnitaireResponse response = service
                  .stockageUnitaire(paramsService);

            // Appel du listener
            wsListener.onRetourWsSansErreur(resultatTest, service
                  ._getServiceClient().getServiceContext()
                  .getConfigurationContext(), formulaire.getParent());

            // Récupère l'identifiant d'archivage renvoyé
            idArchivage = SaeServiceTypeUtils.extractUuid(response
                  .getStockageUnitaireResponse().getIdGed());
            
         } else {
            throw new IntegrationRuntimeException(
                  "Le mode de capture unitaire '" + formulaire.getModeStockage()
                        + "' est inconnu");
         }

         // Log de la réponse obtenue
         log.appendLogNewLine();
         log
               .appendLogLn("Détails de la réponse obtenue de l'opération \"stockageUnitaire\" :");
         SaeServiceLogUtils.logResultatStockageUnitaire(resultatTest,
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
    * Test libre de l'appel à l'opération "stockageUnitaire" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @return StockageUnitaireResultat
    *            résultat du stockageUnitaire
    */
//   public final void appelWsOpStockageUnitaireUrlEcdeTestLibre(
//         String urlServiceWeb, StockageUnitaireFormulaire formulaire) {
   public final StockageUnitaireResultat appelWsOpStockageUnitaireUrlEcdeTestLibre(
            String urlServiceWeb, StockageUnitaireFormulaire formulaire) {
          
      // Initialise la valeur de retour
      StockageUnitaireResultat result = new StockageUnitaireResultat();
  
      result = appelWsOpStockageUnitaireUrlEcdeTestLibre(urlServiceWeb, formulaire, null);
      
      return result;
   }


   /**
    * Test libre de l'appel à l'opération "stockageUnitaire" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    *  @return StockageUnitaireResultat
    *           résultat de stockage
    */
//   public final void appelWsOpStockageUnitaireUrlEcdeTestLibre(
//         String urlServiceWeb, StockageUnitaireFormulaire formulaire,
//         ViFormulaire viParams) {
      public final StockageUnitaireResultat appelWsOpStockageUnitaireUrlEcdeTestLibre(
            String urlServiceWeb, StockageUnitaireFormulaire formulaire,
            ViFormulaire viParams) {

      // Initialise la valeur de retour
      StockageUnitaireResultat result = new StockageUnitaireResultat();
      
      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      result = appelWsOpStockageUnitaire(urlServiceWeb, ViStyle.VI_OK, viParams,
            formulaire, testLibre);
      
      return result;

   }

   /**
    * Test d'appel à l'opération "stockageUnitaire" du service web SaeService.<br>
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
   public final void appelWsOpStockageUnitaireSoapFault(String urlServiceWeb,
         StockageUnitaireFormulaire formulaire, ViStyle viStyle,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpStockageUnitaire(urlServiceWeb, viStyle, null, formulaire,
            listener);

   }
   
   /**
    * Test d'appel à l'opération "stockageUnitaire" du service web SaeService.<br>
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
   public final void appelWsOpStockageUnitaireSoapFault(String urlServiceWeb,
         StockageUnitaireFormulaire formulaire, ViStyle viStyle, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpStockageUnitaire(urlServiceWeb, viStyle, viParams, formulaire,
            listener);

   }

   /**
    * Test d'appel à l'opération "stockageUnitaire" du service web SaeService.<br>
    * On s'attend à récupérer une réponse sans erreur
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @return les informations résultant de la capture unitaire
    */
   public final StockageUnitaireResultat appelWsOpStockageUnitaireReponseAttendue(
         String urlServiceWeb, StockageUnitaireFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      StockageUnitaireResultat resultat = appelWsOpStockageUnitaire(
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
    * Test d'appel à l'opération "stockageUnitaire" du service web SaeService.<br>
    * On s'attend à récupérer une réponse sans erreur
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @return les informations résultant de la capture unitaire
    */
   public final StockageUnitaireResultat appelWsOpStockageUnitaireReponseAttendue(
         String urlServiceWeb, StockageUnitaireFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      StockageUnitaireResultat resultat = appelWsOpStockageUnitaire(
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

   private void activeMtom(SaeServiceStub service) {

      service._getServiceClient().getOptions().setProperty(
            Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);

   }

}
