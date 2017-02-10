package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.axis2.AxisFault;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ConsultationResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeDefinition;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationMTOM;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationMTOMResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectExtractor;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplReponseAttendue;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.utils.TestsMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.utils.ChecksumUtils;
import fr.urssaf.image.sae.integration.ihmweb.utils.TestUtils;

/**
 * Service de test de l'opération "consultation" du service web SaeService
 */
@SuppressWarnings( { "PMD.CyclomaticComplexity", "PMD.NPathComplexity",
      "PMD.ExcessiveMethodLength" })
@Service
public class ConsultationTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private TestsMetadonneesService testMetaService;

   @Autowired
   private ReferentielMetadonneesService referentielMetadonneesService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private ConsultationResultat appelWsOpConsultation(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         ConsultationFormulaire formulaire, WsTestListener wsListener) {

      // Initialise le résultat
      ConsultationResultat result = null;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelConsultation(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // Selon s'il faut appeler le service avec ou sans MTOM
         if (ModeConsultationEnum.AncienServiceSansMtom.equals(formulaire
               .getModeConsult())) {

            // Ancien service sans MTOM

            // Construction du paramètre d'entrée de l'opération
            Consultation paramsService = SaeServiceObjectFactory
                  .buildConsultationRequest(formulaire.getIdArchivage(),
                        formulaire.getCodeMetadonnees());

            // Appel du service web
            ConsultationResponse response = service.consultation(paramsService);

            // Transtypage de l'objet de la couche ws vers l'objet du modèle
            result = fromConsultationAncienService(response);

         } else {

            // Nouveau service sans MTOM

            // Construction du paramètre d'entrée de l'opération
            ConsultationMTOM paramsService = SaeServiceObjectFactory
                  .buildConsultationMTOMRequest(formulaire.getIdArchivage(),
                        formulaire.getCodeMetadonnees());

            // Appel du service web
            ConsultationMTOMResponse response = service
                  .consultationMTOM(paramsService);

            // Transtypage de l'objet de la couche ws vers l'objet du modèle
            result = fromConsultationNouveauService(response);

         }

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log
               .appendLogLn("Détails de la réponse obtenue de l'opération \"consultation\" :");
         SaeServiceLogUtils.logResultatConsultation(resultatTest, result);

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
    * Test libre de l'appel à l'opération "consultation" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpConsultationTestLibre(String urlServiceWeb,
         ConsultationFormulaire formulaire) {

      appelWsOpConsultationTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "consultation" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpConsultationTestLibre(String urlServiceWeb,
         ConsultationFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpConsultation(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,
            testLibre);

   }

   /**
    * Test d'appel à l'opération "consultation" du service web SaeService.<br>
    * On s'attend à récupérer une SoapFault
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
   public final void appelWsOpConsultationSoapFault(String urlServiceWeb,
         ConsultationFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpConsultation(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire, testLibre);

   }
   
   
   /**
    * Test d'appel à l'opération "consultation" du service web SaeService.<br>
    * On s'attend à récupérer une SoapFault
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
   public final void appelWsOpConsultationSoapFault(String urlServiceWeb,
         ConsultationFormulaire formulaire, ViStyle viStyle,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener testAuth = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpConsultation(urlServiceWeb, viStyle, null, formulaire, testAuth);

   }
   
   
   /**
    * Test d'appel à l'opération "consultation" du service web SaeService.<br>
    * On s'attend à récupérer une SoapFault
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viStyle
    *           le type de VI à générer
    * @param viParams
    *           les informations nécessaires pour générer le VI          
    * @param idSoapFaultAttendu
    *           l'identifiant de la SoapFault attendu dans le référentiel des
    *           SoapFault
    * @param argsMsgSoapFault
    *           les arguments pour le String.format du message de la SoapFault
    *           attendue
    */
   public final void appelWsOpConsultationSoapFault(String urlServiceWeb,
         ConsultationFormulaire formulaire, ViStyle viStyle, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener testAuth = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpConsultation(urlServiceWeb, viStyle, viParams, formulaire, testAuth);

   }

   private void wsVerifieRetour(ConsultationFormulaire formulaire,
         ConsultationResultat response, String sha1attendu,
         CodeMetadonneeList codesMetasAttendues,
         List<MetadonneeValeur> metaAttendues) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();
      ResultatTestLog log = resultatTest.getLog();

      // Initialise un flag pour suivre l'état du test
      boolean testKo = false;

      // Vérification du contenu (SHA-1) si fourni en paramètre + contenu base
      // 64
      if (sha1attendu != null) {
         boolean testKoTemp = wsVerifSha1(response.getContenu(), sha1attendu,
               log);
         if (testKoTemp) {
            testKo = true;
         }
      }

      // Vérification des métadonnes
      testKo = wsVerifieRetourMetaDemandees(resultatTest, codesMetasAttendues,
            metaAttendues, response.getMetadonnees());

      // Etat du test
      if (testKo) {
         resultatTest.setStatus(TestStatusEnum.Echec);
      } else {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   /**
    * Test d'appel à l'opération "consultation" du service web SaeService.<br>
    * On s'attend à récupérer une réponse correcte
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param sha1attendu
    *           le SHA-1 attendu, ou null si pas de SHA-1
    * @return la réponse de l'opération "consultation", ou null si une exception
    *         s'est produite
    */
   public final ConsultationResultat appelWsOpConsultationReponseCorrecteAttendue(
         String urlServiceWeb, ConsultationFormulaire formulaire,
         String sha1attendu) {

      return appelWsOpConsultationReponseCorrecteAttendue(urlServiceWeb,
            formulaire, sha1attendu, null, null);

   }

   /**
    * Test d'appel à l'opération "consultation" du service web SaeService.<br>
    * On s'attend à récupérer une réponse correcte
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param sha1attendu
    *           le SHA-1 attendu, ou null si pas de SHA-1
    * @param codesMetasAttendues
    *           la liste des codes des métadonnées attendues
    * @param metaAttendues
    *           la liste des paires clés/valeur des métadonnées attendus peut
    *           être un sous-ensemble de la liste codeMetasAttendus, dans le cas
    *           par exemple où certaines valeurs ne sont pas prédictibles.
    * @return la réponse de l'opération "consultation", ou null si une exception
    *         s'est produite
    */
   public final ConsultationResultat appelWsOpConsultationReponseCorrecteAttendue(
         String urlServiceWeb, ConsultationFormulaire formulaire,
         String sha1attendu, CodeMetadonneeList codesMetasAttendues,
         List<MetadonneeValeur> metaAttendues) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      ConsultationResultat response = appelWsOpConsultation(urlServiceWeb,
            ViStyle.VI_OK, null, formulaire, testAvecReponse);

      // On vérifie le résultat obtenu (si le test n'a pas échoué dans les
      // étapes préalables)
      ResultatTest resultatTest = formulaire.getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         // Vérifie que l'on ait reçu une réponse
         if (response == null) {

            // Echec bizarre
            resultatTest.setStatus(TestStatusEnum.Echec);
            ResultatTestLog log = resultatTest.getLog();
            log
                  .appendLogLn("On s'attendait à une réponse sans erreur du service web, hors on a obtenu une réponse null");

         } else {

            // Vérification de la réponse par rapport aux paramètres envoyés au
            // service web
            wsVerifieRetour(formulaire, response, sha1attendu,
                  codesMetasAttendues, metaAttendues);

         }

      }

      // Renvoie la réponse du service web
      return response;

   }

   private ConsultationResultat fromConsultationAncienService(
         ConsultationResponse response) {

      // Création de l'objet résultat
      ConsultationResultat result = new ConsultationResultat();

      // Extrait le contenu (DataHandler)
      DataHandler contenu = response.getConsultationResponse()
            .getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();
      result.setContenu(contenu);

      // Extrait les métadonnées
      ListeMetadonneeType metadonnees = response.getConsultationResponse()
            .getMetadonnees();
      result.setMetadonnees(metadonnees);

      // Renvoie du résultat
      return result;

   }

   private ConsultationResultat fromConsultationNouveauService(
         ConsultationMTOMResponse response) {

      // Création de l'objet résultat
      ConsultationResultat result = new ConsultationResultat();

      // Extrait le contenu (DataHandler)
      DataHandler contenu = response.getConsultationMTOMResponse().getContenu();
      result.setContenu(contenu);

      // Extrait les métadonnées
      ListeMetadonneeType metadonnees = response.getConsultationMTOMResponse()
            .getMetadonnees();
      result.setMetadonnees(metadonnees);

      // Renvoie du résultat
      return result;

   }

   private boolean wsVerifieRetourMetaDemandees(ResultatTest resultatTest,
         CodeMetadonneeList codesMetaAttendues,
         List<MetadonneeValeur> metaAttendues, ListeMetadonneeType metaObtenues) {

      boolean testKo = false;

      ResultatTestLog log = resultatTest.getLog();

      if (codesMetaAttendues == null) {

         // Rétro-compatibilité
         String erreur = testMetaService
               .verifieMetadonneesConsulteeParDefaut(metaObtenues);
         if (StringUtils.isNotBlank(erreur)) {
            testKo = true;
            log.appendLogNewLine();
            log.appendLogLn(erreur);
         }

      } else {

         // Vérifie que les codes de métadonnées demandées sont bien
         // consultables
         // C'est une sorte de "sur-vérification"
         log.appendLogNewLine();
         testMetaService.areMetadonneesConsultables(codesMetaAttendues,
               resultatTest);

         // Vérifie que les métadonnées obtenues sont bien celles demandées

         // D'abord, pour les besoins du service utilisé plus tard, on construit
         // la
         // liste des MetadonneeDefinition correspondant aux codes longs
         // attendus
         List<MetadonneeDefinition> metadonneesDefinitions = referentielMetadonneesService
               .construitListeMetadonnee(codesMetaAttendues);

         // 1) Vérifie que les métadonnnées retournées font bien partie de la
         // liste demandées
         String messageErreur1 = testMetaService
               .verifieMetasUniquementDansListeAutorisee(metaObtenues,
                     metadonneesDefinitions);

         // 2) Vérifie que toutes les métadonnées demandées font bien partie de
         // la liste
         // des métadonnées renvoyées

         String messageErreur2 = testMetaService.verifieMetasToutesPresentes(
               metaObtenues, metadonneesDefinitions);

         // Bilan des erreurs des vérifications 1) et 2)
         String messageErreurAll = TestUtils.concatMessagesErreurs(
               messageErreur1, messageErreur2);
         if (StringUtils.isNotBlank(messageErreurAll)) {

            testKo = true;

            log.appendLogNewLine();
            log.appendLogLn("Erreur sur les métadonnées");
            log.appendLogLn(messageErreurAll);

         }

         // La suite des tests ne se fait que si les tests 1) et 2) sont OK
         if (!testKo) {

            boolean testKoTemp;

            if (CollectionUtils.isNotEmpty(metaAttendues)) {

               MetadonneeValeurList metaObtenues2 = SaeServiceObjectExtractor
                     .extraitMetadonnees(metaObtenues);

               for (MetadonneeValeur metaValeur : metaAttendues) {
                  testKoTemp = testMetaService.verifiePresenceEtValeurAvecLog(
                        resultatTest, metaObtenues2, metaValeur.getCode(),
                        metaValeur.getValeur());
                  if (testKoTemp) {
                     testKo = true;
                  }
               }

            }

         }

      }

      // Renvoie du résultat
      return testKo;

   }

   private boolean wsVerifSha1(DataHandler contenu, String sha1attendu,
         ResultatTestLog log) {

      boolean testKo = false;

      // Calcul du SHA-1
      String sha1obtenu = null;
      try {
         sha1obtenu = ChecksumUtils.sha1(contenu.getInputStream());
      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      }

      // Comparaison des SHA-1
      if (!StringUtils.equalsIgnoreCase(sha1obtenu, sha1attendu)) {
         testKo = true;
         log
               .appendLogLn("Le SHA-1 du contenu de l'archive renvoyée ("
                     + sha1obtenu
                     + ") n'est pas identique au SHA-1 du document que l'on attendait ("
                     + sha1attendu + ")");
      }

      return testKo;

   }

}
