package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeDefinition;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Recherche;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
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
import fr.urssaf.image.sae.integration.ihmweb.utils.TestUtils;

/**
 * Service de test de l'opération "recherche" du service web SaeService
 */
@Service
@SuppressWarnings("PMD.ExcessiveImports")
public class RechercheTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private TestsMetadonneesService testMetaService;

   @Autowired
   private ReferentielMetadonneesService referentielMetadonneesService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private RechercheResponse appelWsOpRecherche(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         RechercheFormulaire formulaire, WsTestListener wsListener,
         TypeComparaison triDesResultatsDansAffichageLog) {

      // Initialise le résultat
      RechercheResponse response = null;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelRecherche(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // Construction du paramètre d'entrée de l'opération
         Recherche paramsService = SaeServiceObjectFactory
               .buildRechercheRequest(formulaire.getRequeteLucene(), formulaire
                     .getCodeMetadonnees());

         // Appel du service web
         response = service.recherche(paramsService);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogNewLine();
         log
               .appendLogLn("Détails de la réponse obtenue de l'opération \"recherche\" :");
         SaeServiceLogUtils.logResultatRecherche(log, response
               .getRechercheResponse(), triDesResultatsDansAffichageLog);

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

      // Renvoi du résultat
      return response;

   }

   /**
    * Test libre de l'appel à l'opération "recherche" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpRechercheTestLibre(String urlServiceWeb,
         RechercheFormulaire formulaire) {

      appelWsOpRechercheTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "recherche" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpRechercheTestLibre(String urlServiceWeb,
         RechercheFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpRecherche(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,
            testLibre, null);

   }

   /**
    * Test d'appel à l'opération "recherche" du service web SaeService.<br>
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
    *           à effectuer sur la recherche
    * @return la réponse de l'opération "recherche", ou null si une exception
    *         s'est produite
    */
   public final RechercheResponse appelWsOpRechercheReponseCorrecteAttendue(
         String urlServiceWeb, RechercheFormulaire formulaire,
         Integer nbResultatsAttendus, Boolean flagResultatsTronquesAttendu,
         TypeComparaison triDesResultatsDansAffichageLog) {

      return appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
            formulaire, nbResultatsAttendus, flagResultatsTronquesAttendu,
            triDesResultatsDansAffichageLog, null);

   }

   /**
    * Test d'appel à l'opération "recherche" du service web SaeService.<br>
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
    *           à effectuer sur la recherche
    * @param viParams
    *           les paramètres du VI
    * @return la réponse de l'opération "recherche", ou null si une exception
    *         s'est produite
    */
   public final RechercheResponse appelWsOpRechercheReponseCorrecteAttendue(
         String urlServiceWeb, RechercheFormulaire formulaire,
         Integer nbResultatsAttendus, Boolean flagResultatsTronquesAttendu,
         TypeComparaison triDesResultatsDansAffichageLog, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une réponse
      WsTestListener testAvecReponse = new WsTestListenerImplReponseAttendue();

      // Appel de la méthode "générique" de test
      RechercheResponse response = appelWsOpRecherche(urlServiceWeb,
            ViStyle.VI_OK, viParams, formulaire, testAvecReponse,
            triDesResultatsDansAffichageLog);

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
            wsVerifieRetour(resultatTest, response, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, formulaire.getCodeMetadonnees());

         }

      }

      // Renvoie la réponse du service web
      return response;

   }

   private void wsVerifieRetour(ResultatTest resultatTest,
         RechercheResponse response, Integer nbResultatsAttendus,
         Boolean flagResultatsTronquesAttendu,
         CodeMetadonneeList codesMetaAttendues) {

      // Initialise
      ResultatTestLog log = resultatTest.getLog();

      if ((response != null) && (response.getRechercheResponse() != null)) {

         // Récupère le retour de la recherche
         boolean resultatTronque = response.getRechercheResponse()
               .getResultatTronque();
         ResultatRechercheType[] resultats = null;
         if (response.getRechercheResponse().getResultats() != null) {
            resultats = response.getRechercheResponse().getResultats()
                  .getResultat();
         }

         // Calcul le nombre de résultats
         int nbResultatsObtenus;
         if (resultats == null) {
            nbResultatsObtenus = 0;
         } else {
            nbResultatsObtenus = resultats.length;
         }

         // Vérifie le nombre de résultats attendus, si demandé
         if ((nbResultatsAttendus != null)
               && (nbResultatsObtenus != nbResultatsAttendus.intValue())) {

            resultatTest.setStatus(TestStatusEnum.Echec);

            log.appendLogNewLine();
            log.appendLog("Erreur : on s'attendait à obtenir ");
            log.appendLog(Integer.toString(nbResultatsAttendus));
            log
                  .appendLog(" résultat(s) de recherche, alors que l'on en a obtenu ");
            log.appendLog(Integer.toString(nbResultatsObtenus));
            log.appendLogLn(".");

         }

         // Vérifie le flag tronqué, si demandé
         if ((flagResultatsTronquesAttendu != null)
               && (resultatTronque != flagResultatsTronquesAttendu
                     .booleanValue())) {

            resultatTest.setStatus(TestStatusEnum.Echec);

            log.appendLogNewLine();
            log
                  .appendLog("Erreur : on s'attendait à obtenir le flag de résultat tronqué à ");
            log.appendLog(Boolean.toString(flagResultatsTronquesAttendu));
            log.appendLog(" alors qu'on a obtenu ");
            log.appendLog(Boolean.toString(resultatTronque));
            log.appendLogLn(".");

         }

         // Vérifie les métadonnées
         // Soit :
         // - on vérifie que l'on a obtenu, par défaut, les métadonnées dites
         // "consultées par défaut"
         // - ou on vérifie que l'on a obtenu les métadonnées demandées
         if (resultats != null) {
            if (CollectionUtils.isNotEmpty(codesMetaAttendues)) {

               wsVerifieRetourMetaDemandees(resultatTest, codesMetaAttendues,
                     resultats);

            } else {

               wsVerifieRetourMetaParDefaut(resultatTest, resultats);

            }
         } else if (nbResultatsAttendus == 0 && resultats == null) {
            log.appendLogLn("Aucun résultat attendu, aucun résultat retourné");
            resultatTest.setStatus(TestStatusEnum.Succes);
         }

      }

   }

   private void wsVerifieRetourMetaDemandees(ResultatTest resultatTest,
         CodeMetadonneeList codesMetaAttendues,
         ResultatRechercheType[] resultats) {

      ResultatTestLog log = resultatTest.getLog();

      // Vérifie que les codes de métadonnées demandées sont bien consultables
      // C'est une sorte de "sur-vérification"
      log.appendLogNewLine();
      testMetaService.areMetadonneesConsultables(codesMetaAttendues,
            resultatTest);

      // Vérifie que les métadonnées obtenues sont bien celles demandées

      // D'abord, pour les besoins du service utilisé plus tard, on construit la
      // liste des MetadonneeDefinition correspondant aux codes longs attendus
      List<MetadonneeDefinition> metadonneesDefinitions = referentielMetadonneesService
            .construitListeMetadonnee(codesMetaAttendues);

      // Puis on boucle sur les résultats de la recherche, afin de contrôler
      // chaque résultat
      String messageErreur1;
      String messageErreur2;
      String messageErreurAll;
      for (int i = 0; i < resultats.length; i++) {

         // 1) Vérifie que les métadonnnées retournées font bien partie de la
         // liste demandées

         messageErreur1 = testMetaService
               .verifieMetasUniquementDansListeAutorisee(resultats[i]
                     .getMetadonnees(), metadonneesDefinitions);

         // 2) Vérifie que toutes les métadonnées demandées font bien partie de
         // la liste
         // des métadonnées renvoyées

         messageErreur2 = testMetaService.verifieMetasToutesPresentes(
               resultats[i].getMetadonnees(), metadonneesDefinitions);

         // Bilan des erreurs
         messageErreurAll = TestUtils.concatMessagesErreurs(messageErreur1,
               messageErreur2);
         if (StringUtils.isNotBlank(messageErreurAll)) {

            resultatTest.setStatus(TestStatusEnum.Echec);

            log.appendLogNewLine();
            log.appendLogLn("Erreur sur les métadonnées du résultat #"
                  + (i + 1));
            log.appendLogLn(messageErreurAll);

         }

      }

   }

   private void wsVerifieRetourMetaParDefaut(ResultatTest resultatTest,
         ResultatRechercheType[] resultats) {

      ResultatTestLog log = resultatTest.getLog();

      String messageErreur;
      for (int i = 0; i < resultats.length; i++) {

         messageErreur = testMetaService
               .verifieMetadonneesConsulteeParDefaut(resultats[i]
                     .getMetadonnees());

         if (StringUtils.isNotBlank(messageErreur)) {

            resultatTest.setStatus(TestStatusEnum.Echec);

            log.appendLogNewLine();
            log.appendLogLn("Erreur sur les métadonnées du résultat #"
                  + (i + 1));
            log.appendLogLn(messageErreur);

         }

      }

   }

   /**
    * Test d'appel à l'opération "recherche" du service web SaeService.<br>
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
   public final void appelWsOpRechercheSoapFault(String urlServiceWeb,
         RechercheFormulaire formulaire, ViStyle viStyle,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpRecherche(urlServiceWeb, viStyle, null, formulaire, listener,
            null);

   }

   /**
    * Vérifie qu'un résultat de recherche contienne bien les métadonnées
    * fournies en paramètres, avec les valeurs fournies.
    * 
    * @param resultatRecherche
    *           l'objet "résultat de recherche"
    * @param numeroResultatRecherche
    *           le numéro du résultat de recherche, pour le log
    * @param resultatTest
    *           l'objet contenant le résultat du test en cours
    * @param valeursAttendues
    *           la liste des métadonnées attendues (codes + valeurs)
    */
   public final void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche,
         String numeroResultatRecherche, ResultatTest resultatTest,
         MetadonneeValeurList valeursAttendues) {

      StringBuffer sbErreurs = new StringBuffer();

      MetadonneeValeurList metadonnees = SaeServiceObjectExtractor
            .extraitMetadonnees(resultatRecherche);

      for (MetadonneeValeur valeurAttendue : valeursAttendues) {
         verifiePresenceEtValeur(resultatTest, metadonnees,
               numeroResultatRecherche, sbErreurs, valeurAttendue.getCode(),
               valeurAttendue.getValeur());
      }

      if (sbErreurs.length() > 0) {
         resultatTest.setStatus(TestStatusEnum.Echec);
         resultatTest.getLog().appendLogLn(sbErreurs.toString());
      }

   }

   private void verifiePresenceEtValeur(ResultatTest resultatTest,
         MetadonneeValeurList metadonnees, String numeroResultatRecherche,
         StringBuffer sbErreurs, String code, String valeurAttendue) {

      String messageErreur = testMetaService.verifiePresenceEtValeur(
            resultatTest, metadonnees, code, valeurAttendue);

      if (StringUtils.isNotBlank(messageErreur)) {

         if (sbErreurs.length() == 0) {
            sbErreurs.append("Erreur sur le résultat #");
            sbErreurs.append(numeroResultatRecherche);
            sbErreurs.append("\r\n");
         }

         sbErreurs.append(messageErreur);
         sbErreurs.append("\r\n");

      }

   }

}
