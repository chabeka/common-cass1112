package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionMasseResponse;
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
 * Service de test de l'opération "suppressionMasse" du service web SaeService
 */
@Service
@SuppressWarnings("PMD.ExcessiveImports")
public class SuppressionMasseTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private TestsMetadonneesService testMetaService;

   @Autowired
   private ReferentielMetadonneesService referentielMetadonneesService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private SuppressionMasseResponse appelWsOpSuppressionMasse(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         SuppressionMasseFormulaire formulaire, WsTestListener wsListener) {

      // Initialise le résultat
      SuppressionMasseResponse response = null;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelSuppressionMasseSimple(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {
 //        System.out.println("formulaire.getRequeteLucene() : " +formulaire.getRequeteLucene());
         // Construction du paramètre d'entrée de l'opération
         SuppressionMasse paramsService = SaeServiceObjectFactory
               .buildSuppressionMasseRequest(formulaire.getRequeteLucene());

         // Appel du service web
         response = service.suppressionMasse(paramsService);
         
           // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogNewLine();
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"SuppressionMasse\" :");
         log.appendLogLn(response.getSuppressionMasseResponse().getUuid());
         SaeServiceLogUtils.logResultatSuppressionMasse(log, response.getSuppressionMasseResponse());
      } catch (AxisFault fault) {
         // Appel du listener
         wsListener.onSoapFault(resultatTest, fault, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
    } 
         catch (RemoteException e) {
         // Appel du listener
         wsListener.onRemoteException(resultatTest, e, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
     }

      // Ajoute le timestamp en 1ère ligne du log
      log.insertTimestamp();

      // Renvoi du résultat
      return response;

   }

   /**
    * Test libre de l'appel à l'opération "suppressionMasse" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpSuppressionMasseTestLibre(String urlServiceWeb,
         SuppressionMasseFormulaire formulaire) {

      appelWsOpSuppressionMasseTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "SuppressionMasse" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpSuppressionMasseTestLibre(String urlServiceWeb,
         SuppressionMasseFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpSuppressionMasse(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,testLibre);

   }

   /**
    * Test d'appel à l'opération "suppressionMasse" du service web SaeService.<br>
    * On s'attend à récupérer une réponse correcte
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final SuppressionMasseResponse appelWsOpSuppressionMasseReponseCorrecteAttendue(
         String urlServiceWeb, SuppressionMasseFormulaire formulaire,
         Integer nbResultatsAttendus) {

      return appelWsOpSuppressionMasseReponseCorrecteAttendue(urlServiceWeb,
            formulaire, nbResultatsAttendus, null);

   }

   /**
    * Test d'appel à l'opération "suppressionMasse" du service web SaeService.<br>
    * On s'attend à récupérer une réponse correcte
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param nbResultatsAttendus
    *           le nombre de résultats attendus, ou null si pas de vérif
    * @param flagResultatsTronquesAttendu
    *           le flag attendu, ou null si pas de verif
    * @param triDesResultatsDansAffichageLog
    *           à effectuer sur la suppressionMasse
    * @param viParams
    *           les paramètres du VI
    * @return la réponse de l'opération "suppressionMasse", ou null si une exception
    *         s'est produite
    */
   public final SuppressionMasseResponse appelWsOpSuppressionMasseReponseCorrecteAttendue(
         String urlServiceWeb, SuppressionMasseFormulaire formulaire,
         Integer nbResultatsAttendus, ViFormulaire viParams) {


      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();
      
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.setStatus(TestStatusEnum.Echec);
      ResultatTestLog log = resultatTest.getLog();
      log
            .appendLogLn("TEST");

      // Appel de la méthode "générique" de test
      SuppressionMasseResponse response = appelWsOpSuppressionMasse(urlServiceWeb,
            ViStyle.VI_OK, viParams, formulaire, testAvecReponse);
//
//      // On vérifie le résultat obtenu (si le test n'a pas échoué dans les
//      // étapes préalables)
//      ResultatTest resultatTest = formulaire.getResultats();
//      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
//
//         // Vérifie que l'on ait reçu une réponse
//         if (response == null) {
//
//            // Echec bizarre
//            resultatTest.setStatus(TestStatusEnum.Echec);
//            ResultatTestLog log = resultatTest.getLog();
//            log
//                  .appendLogLn("On s'attendait à une réponse sans erreur du service web, hors on a obtenu une réponse null");
//
//         } else {
//
//            // Vérification de la réponse par rapport aux paramètres envoyés au
//            // service web
//            wsVerifieRetour(resultatTest, response, nbResultatsAttendus);
//
//         }
//
//      }

      // Renvoie la réponse du service web
//      SuppressionMasseResponse response = new SuppressionMasseResponse();
      return response;

   }

   private void wsVerifieRetour(ResultatTest resultatTest,
         SuppressionMasseResponse response, Integer nbResultatsAttendus) {

//      // Initialise
//      ResultatTestLog log = resultatTest.getLog();
//
//      if ((response != null) && (response.getSuppressionMasseResponse() != null)) {
//
//         // Récupère le retour de la suppressionMasse
//         boolean resultatTronque = response.getSuppressionMasseResponse()
//               .getResultatTronque();
////         ResultatSuppressionMasseType[] resultats = null;
//         if (response.getSuppressionMasseResponse().getResultats() != null) {
//        
////            resultats = response.getSuppressionMasseResponse().getResultats()
////                  .getResultat();
//         }
//
//         // Calcul le nombre de résultats
////         int nbResultatsObtenus;
////         if (resultats == null) {
////            nbResultatsObtenus = 0;
////         } else {
////            nbResultatsObtenus = resultats.length;
////         }
//
//         // Vérifie le nombre de résultats attendus, si demandé
//         if ((nbResultatsAttendus != null)
//               && (nbResultatsObtenus != nbResultatsAttendus.intValue())) {
//
//            resultatTest.setStatus(TestStatusEnum.Echec);
//
//            log.appendLogNewLine();
//            log.appendLog("Erreur : on s'attendait à obtenir ");
//            log.appendLog(Integer.toString(nbResultatsAttendus));
//            log.appendLog(" résultat(s) de suppressionMasse, alors que l'on en a obtenu ");
//            log.appendLog(Integer.toString(nbResultatsObtenus));
//            log.appendLogLn(".");
//
//         }
//
//         // Vérifie le flag tronqué, si demandé
//         if ((flagResultatsTronquesAttendu != null)
//               && (resultatTronque != flagResultatsTronquesAttendu
//                     .booleanValue())) {
//
//            resultatTest.setStatus(TestStatusEnum.Echec);
//
//            log.appendLogNewLine();
//            log
//                  .appendLog("Erreur : on s'attendait à obtenir le flag de résultat tronqué à ");
//            log.appendLog(Boolean.toString(flagResultatsTronquesAttendu));
//            log.appendLog(" alors qu'on a obtenu ");
//            log.appendLog(Boolean.toString(resultatTronque));
//            log.appendLogLn(".");
//
//         }

         // Vérifie les métadonnées
         // Soit :
         // - on vérifie que l'on a obtenu, par défaut, les métadonnées dites
         // "consultées par défaut"
         // - ou on vérifie que l'on a obtenu les métadonnées demandées
//         if (resultats != null) {
//            if (CollectionUtils.isNotEmpty(codesMetaAttendues)) {
//
//               wsVerifieRetourMetaDemandees(resultatTest, codesMetaAttendues,
//                     resultats);
//
//            } else {
//
//               wsVerifieRetourMetaParDefaut(resultatTest, resultats);
//
//            }
//         } else 
            
//         if ((nbResultatsAttendus == null || nbResultatsAttendus == 0) && resultats == null) {
//            log.appendLogLn("Aucun résultat attendu, aucun résultat retourné");
//            resultatTest.setStatus(TestStatusEnum.Succes);
//         }
//
//         
//      }

   }

//   private void wsVerifieRetourMetaDemandees(ResultatTest resultatTest,
//         CodeMetadonneeList codesMetaAttendues,
//         ResultatSuppressionMasseType[] resultats) {
//
//      ResultatTestLog log = resultatTest.getLog();
//
//      // Vérifie que les codes de métadonnées demandées sont bien consultables
//      // C'est une sorte de "sur-vérification"
//      log.appendLogNewLine();
//      testMetaService.areMetadonneesConsultables(codesMetaAttendues,
//            resultatTest);
//
//      // Vérifie que les métadonnées obtenues sont bien celles demandées
//
//      // D'abord, pour les besoins du service utilisé plus tard, on construit la
//      // liste des MetadonneeDefinition correspondant aux codes longs attendus
//      List<MetadonneeDefinition> metadonneesDefinitions = referentielMetadonneesService
//            .construitListeMetadonnee(codesMetaAttendues);
//
//      // Puis on boucle sur les résultats de la suppressionMasse, afin de contrôler
//      // chaque résultat
//      String messageErreur1;
//      String messageErreur2;
//      String messageErreurAll;
//      for (int i = 0; i < resultats.length; i++) {
//
//         // 1) Vérifie que les métadonnnées retournées font bien partie de la
//         // liste demandées
//
//         messageErreur1 = testMetaService
//               .verifieMetasUniquementDansListeAutorisee(resultats[i]
//                     .getMetadonnees(), metadonneesDefinitions);
//
//         // 2) Vérifie que toutes les métadonnées demandées font bien partie de
//         // la liste
//         // des métadonnées renvoyées
//
//         messageErreur2 = testMetaService.verifieMetasToutesPresentes(
//               resultats[i].getMetadonnees(), metadonneesDefinitions);
//
//         // Bilan des erreurs
//         messageErreurAll = TestUtils.concatMessagesErreurs(messageErreur1,
//               messageErreur2);
//         if (StringUtils.isNotBlank(messageErreurAll)) {
//
//            resultatTest.setStatus(TestStatusEnum.Echec);
//
//            log.appendLogNewLine();
//            log.appendLogLn("Erreur sur les métadonnées du résultat #"
//                  + (i + 1));
//            log.appendLogLn(messageErreurAll);
//
//         }
//
//      }
//
//   }
//
//   private void wsVerifieRetourMetaParDefaut(ResultatTest resultatTest,
//         ResultatSuppressionMasseType[] resultats) {
//
//      ResultatTestLog log = resultatTest.getLog();
//
//      String messageErreur;
//      for (int i = 0; i < resultats.length; i++) {
//
//         messageErreur = testMetaService
//               .verifieMetadonneesConsulteeParDefaut(resultats[i]
//                     .getMetadonnees());
//
//         if (StringUtils.isNotBlank(messageErreur)) {
//
//            resultatTest.setStatus(TestStatusEnum.Echec);
//
//            log.appendLogNewLine();
//            log.appendLogLn("Erreur sur les métadonnées du résultat #"
//                  + (i + 1));
//            log.appendLogLn(messageErreur);
//
//         }
//
//      }
//
//   }

   /**
    * Test d'appel à l'opération "suppressionMasse" du service web SaeService.<br>
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
   public final void appelWsOpSuppressionMasseSoapFault(String urlServiceWeb,
         SuppressionMasseFormulaire formulaire, ViStyle viStyle,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpSuppressionMasse(urlServiceWeb, viStyle, null, formulaire, listener);
   }
   
   
   /**
    * Test d'appel à l'opération "suppressionMasse" du service web SaeService.<br>
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
    *             Formulaire contenant les parametres du VI
    * @param idSoapFaultAttendu
    *           l'identifiant de la SoapFault attendu dans le référentiel des
    *           SoapFault
    * @param argsMsgSoapFault
    *           les arguments pour le String.format du message de la SoapFault
    *           attendue
    */
   public final void appelWsOpSuppressionMasseSoapFault(String urlServiceWeb,
         SuppressionMasseFormulaire formulaire, ViStyle viStyle, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpSuppressionMasse(urlServiceWeb, viStyle, viParams, formulaire, listener);

   }

   /**
    * Vérifie qu'un résultat de suppressionMasse contienne bien les métadonnées
    * fournies en paramètres, avec les valeurs fournies.
    * 
    * @param resultatSuppressionMasse
    *           l'objet "résultat de suppressionMasse"
    * @param numeroResultatSuppressionMasse
    *           le numéro du résultat de suppressionMasse, pour le log
    * @param resultatTest
    *           l'objet contenant le résultat du test en cours
    * @param valeursAttendues
    *           la liste des métadonnées attendues (codes + valeurs)
    */
//   public final void verifieResultatSuppressionMasse(
//         ResultatSuppressionMasseType resultatSuppressionMasse,
//         String numeroResultatSuppressionMasse, ResultatTest resultatTest,
//         MetadonneeValeurList valeursAttendues) {
//
//      StringBuffer sbErreurs = new StringBuffer();
//
//      MetadonneeValeurList metadonnees = SaeServiceObjectExtractor
//            .extraitMetadonnees(resultatSuppressionMasse);
//
//      for (MetadonneeValeur valeurAttendue : valeursAttendues) {
//         verifiePresenceEtValeur(resultatTest, metadonnees,
//               numeroResultatSuppressionMasse, sbErreurs, valeurAttendue.getCode(),
//               valeurAttendue.getValeur());
//      }
//
//      if (sbErreurs.length() > 0) {
//         resultatTest.setStatus(TestStatusEnum.Echec);
//         resultatTest.getLog().appendLogLn(sbErreurs.toString());
//      }
//
//   }
//
//   private void verifiePresenceEtValeur(ResultatTest resultatTest,
//         MetadonneeValeurList metadonnees, String numeroResultatSuppressionMasse,
//         StringBuffer sbErreurs, String code, String valeurAttendue) {
//
//      String messageErreur = testMetaService.verifiePresenceEtValeur(
//            resultatTest, metadonnees, code, valeurAttendue);
//
//      if (StringUtils.isNotBlank(messageErreur)) {
//
//         if (sbErreurs.length() == 0) {
//            sbErreurs.append("Erreur sur le résultat #");
//            sbErreurs.append(numeroResultatSuppressionMasse);
//            sbErreurs.append("\r\n");
//         }
//
//         sbErreurs.append(messageErreur);
//         sbErreurs.append("\r\n");
//
//      }
//
//   }

}
