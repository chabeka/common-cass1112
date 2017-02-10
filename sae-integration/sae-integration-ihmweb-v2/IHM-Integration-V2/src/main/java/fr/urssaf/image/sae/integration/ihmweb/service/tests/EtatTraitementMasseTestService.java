package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.EtatTraitementMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EtatTraitementsMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EtatTraitementsMasseResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.TraitementMasseType;
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
 * Service de test de l'opération "etatTraitementMasse" du service web SaeService
 */
@Service
@SuppressWarnings("PMD.ExcessiveImports")
public class EtatTraitementMasseTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private TestsMetadonneesService testMetaService;

   @Autowired
   private ReferentielMetadonneesService referentielMetadonneesService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private EtatTraitementsMasseResponse appelWsOpEtatTraitementMasse(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         EtatTraitementMasseFormulaire formulaire, WsTestListener wsListener) {

      // Initialise le résultat
      EtatTraitementsMasseResponse response = null;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelEtatTraitementMasseSimple(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {
         // Construction du paramètre d'entrée de l'opération
         EtatTraitementsMasse paramsService = SaeServiceObjectFactory.buildEtatTraitementMasseRequest(formulaire.getRequeteListeUUID());
         // Appel du service web
         response = service.etatTraitementsMasse(paramsService);
         
         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogNewLine();
         TraitementMasseType[] listeJobs = response
               .getEtatTraitementsMasseResponse().getTraitementsMasse()
               .getTraitementMasse();

         log.appendLogLn("Détails des traitements demandés");
         log.appendLogLn("");

         for (TraitementMasseType job : listeJobs) {
            log.appendLogLn("Id Job : " + job.getIdJob());
            log.appendLogLn("Etat : " + job.getEtat());
            log.appendLogLn("Type : " + job.getType());
            log.appendLogLn("Date de création : " + job.getDateCreation());
            log.appendLogLn("Date de réservation : " + job.getDateReservation());
            log.appendLogLn("Date de début : " + job.getDateDebut());
            log.appendLogLn("Date de fin : " + job.getDateFin());
            log.appendLogLn("Nombre de documents : " + job.getNombreDocuments());
            log.appendLogLn("Message : " + job.getMessage());
            log.appendLogLn("");
         }         
         
         SaeServiceLogUtils.logResultatEtatTraitementMasse(log);
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
    * Test libre de l'appel à l'opération "etatTraitementMasse" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpEtatTraitementMasseTestLibre(String urlServiceWeb,
         EtatTraitementMasseFormulaire formulaire) {

      appelWsOpEtatTraitementMasseTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "EtatTraitementMasse" du service web SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final void appelWsOpEtatTraitementMasseTestLibre(String urlServiceWeb,
         EtatTraitementMasseFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpEtatTraitementMasse(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,testLibre);

   }

   /**
    * Test d'appel à l'opération "etatTraitementMasse" du service web SaeService.<br>
    * On s'attend à récupérer une réponse correcte
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final EtatTraitementsMasseResponse appelWsOpEtatTraitementMasseReponseCorrecteAttendue(
         String urlServiceWeb, EtatTraitementMasseFormulaire formulaire,
         Integer nbResultatsAttendus) {

      return appelWsOpEtatTraitementMasseReponseCorrecteAttendue(urlServiceWeb,
            formulaire, nbResultatsAttendus, null);

   }

   /**
    * Test d'appel à l'opération "etatTraitementMasse" du service web SaeService.<br>
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
    *           à effectuer sur la etatTraitementMasse
    * @param viParams
    *           les paramètres du VI
    * @return la réponse de l'opération "etatTraitementMasse", ou null si une exception
    *         s'est produite
    */
   public final EtatTraitementsMasseResponse appelWsOpEtatTraitementMasseReponseCorrecteAttendue(
         String urlServiceWeb, EtatTraitementMasseFormulaire formulaire,
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
      EtatTraitementsMasseResponse response = appelWsOpEtatTraitementMasse(urlServiceWeb,
            ViStyle.VI_OK, viParams, formulaire, testAvecReponse);
      return response;

   }


   /**
    * Test d'appel à l'opération "etatTraitementMasse" du service web SaeService.<br>
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
   public final void appelWsOpEtatTraitementMasseSoapFault(String urlServiceWeb,
         EtatTraitementMasseFormulaire formulaire, ViStyle viStyle,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpEtatTraitementMasse(urlServiceWeb, viStyle, null, formulaire, listener);
   }
   
   
   /**
    * Test d'appel à l'opération "etatTraitementMasse" du service web SaeService.<br>
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
   public final void appelWsOpEtatTraitementMasseSoapFault(String urlServiceWeb,
         EtatTraitementMasseFormulaire formulaire, ViStyle viStyle, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener listener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpEtatTraitementMasse(urlServiceWeb, viStyle, viParams, formulaire, listener);

   }

}
