package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.axis2.AxisFault;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.Comptage;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedVirtualDocumentType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseAvecHash;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseAvecHashResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.dfce.DfceService;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;
import fr.urssaf.image.sae.integration.ihmweb.utils.ExceptionUtils;

/**
 * Service pour les tests de la fonctionnalité "Capture de masse".<br>
 * <br>
 * Cette fonctionnalité fait intervenir :<br>
 * <ul>
 * <li>le service web SaeService, et son opération "archivageMasse"</li>
 * <li>
 * l'ECDE, avec :
 * <ul>
 * <li>la présence ou non du fichier flag de fin de traitement</li>
 * <li>la lecture du fichier resultats.xml</li>
 * </ul>
 * </li>
 * </ul>
 */
@Service
public class CaptureMasseTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private EcdeService ecdeService;

   @Autowired
   private DfceService dfceService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private CaptureMasseResultat appelWsOpArchiMasse(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         CaptureMasseFormulaire formulaire, WsTestListener wsListener) {

      // Initialise le résultat
      CaptureMasseResultat result = null;

      // on supprime les fichiers de traitement précédents
      String urlEcde = formulaire.getUrlSommaire();
      try {
         deleteFileIfExists(getCheminFichierDebutFlag(urlEcde));
      } catch (Exception e1) {
         // nothing to do
      }

      try {
         deleteFileIfExists(getCheminFichierFlag(urlEcde));
      } catch (Exception e1) {
         // nothing to do
      }

      try {
         deleteFileIfExists(getCheminFichierResultatsXml(urlEcde));
      } catch (Exception e1) {
         // nothing to do
      }

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelArchivageMasse(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // Selon si l'on demande l'opération archivageMasse ou
         // archivageMasseAvecHash
         if (formulaire.getAvecHash()) {

            // Construction du paramètre d'entrée de l'opération
            ArchivageMasseAvecHash paramsService = SaeServiceObjectFactory
                  .buildArchivageMasseAvecHashRequest(formulaire
                        .getUrlSommaire(), formulaire.getHash(), formulaire
                        .getTypeHash());

            // Appel du service web
            ArchivageMasseAvecHashResponse response = service
                  .archivageMasseAvecHash(paramsService);

            // Construction de l'objet modèle de résultat
            result = fromCaptureMasseAvecHash(response);

         } else {

            // Construction du paramètre d'entrée de l'opération
            ArchivageMasse paramsService = SaeServiceObjectFactory
                  .buildArchivageMasseRequest(formulaire.getUrlSommaire());

            // Appel du service web
            ArchivageMasseResponse response = service
                  .archivageMasse(paramsService);

            // Construction de l'objet modèle de résultat
            result = fromCaptureMasseAncienService(response);

         }

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log
               .appendLogLn("Détails de la réponse obtenue de l'opération \"archivageMasse\" :");
         SaeServiceLogUtils.logResultatCaptureMasse(resultatTest, result);

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

      // Renvoie de l'objet résultat
      return result;

   }

   /**
    * Test libre de l'appel à l'opération "archivageMasse" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final CaptureMasseResultat appelWsOpArchiMasseTestLibre(
         String urlServiceWeb, CaptureMasseFormulaire formulaire) {

      return appelWsOpArchiMasseTestLibre(urlServiceWeb, formulaire, null);

   }

   /**
    * Test libre de l'appel à l'opération "archivageMasse" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final CaptureMasseResultat appelWsOpArchiMasseTestLibre(
         String urlServiceWeb, CaptureMasseFormulaire formulaire,
         ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      return appelWsOpArchiMasse(urlServiceWeb, ViStyle.VI_OK, viParams,
            formulaire, testLibre);

   }

   /**
    * Test d'appel à l'opération "archivageMasse" du service web SaeService.<br>
    * On vérifie que l'authentification applicative est activée sur l'opération
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpArchiMasseSoapFaultAuth(String urlServiceWeb,
         CaptureMasseFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault
            .findSoapFault("wsse_SecurityTokenUnavailable");
      WsTestListener testAuth = new WsTestListenerImplSoapFault(faultAttendue,
            null);

      // Appel de la méthode "générique" de test
      appelWsOpArchiMasse(urlServiceWeb,
            ViStyle.VI_SF_wsse_SecurityTokenUnavailable, null, formulaire,
            testAuth);

   }

   /**
    * appel de l'archivage de masse avec en attente aucune saop fault
    * 
    * @param urlWebService
    *           adresse du WS
    */
   public final CaptureMasseResultat appelWsOpArchiMasseOKAttendu(
         String urlWebService, CaptureMasseFormulaire formulaire) {

      // Fait la même chose que le test libre
      return appelWsOpArchiMasseTestLibre(urlWebService, formulaire);

   }

   /**
    * appel de l'archivage de masse avec en attente aucune saop fault
    * 
    * @param urlWebService
    *           adresse du WS
    * @param viParam
    *           Le paramètres du VI
    */
   public final CaptureMasseResultat appelWsOpArchiMasseOKAttendu(
         String urlWebService, CaptureMasseFormulaire formulaire,
         ViFormulaire viParams) {

      // Fait la même chose que le test libre
      return appelWsOpArchiMasseTestLibre(urlWebService, formulaire, viParams);

   }

   /**
    * appel de l'archivage de masse avec en attente une saop fault dont on
    * fournit le code
    * 
    * @param urlWebService
    *           adresse du WS
    * @param formulaire
    *           formulaire affiché
    * @param soapFault
    *           code erreur attendue
    * @param args
    *           arguments de la soapFault
    */
   public final void appelWsOpArchiMasseSoapFaultAttendue(String urlWebService,
         CaptureMasseFormulaire formulaire, String soapFault, String[] args) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)

      SoapFault faultAttendue = refSoapFault.findSoapFault(soapFault);

      WsTestListener testLibre = new WsTestListenerImplSoapFault(faultAttendue,
            args);

      // Appel de la méthode "générique" de test
      appelWsOpArchiMasse(urlWebService, ViStyle.VI_OK, null, formulaire,
            testLibre);

   }

   /**
    * appel de l'archivage de masse avec en attente une saop fault dont on
    * fournit le code
    * 
    * @param urlWebService
    *           adresse du WS
    * @param formulaire
    *           formulaire affiché
    * @param soapFault
    *           code erreur attendue
    * @param args
    *           arguments de la soapFault
    */
   public final void appelWsOpArchiMasseSoapFaultAttendue(String urlWebService,
         CaptureMasseFormulaire formulaire, ViFormulaire viParams,
         String soapFault, String[] args) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)

      SoapFault faultAttendue = refSoapFault.findSoapFault(soapFault);

      WsTestListener testLibre = new WsTestListenerImplSoapFault(faultAttendue,
            args);

      // Appel de la méthode "générique" de test
      appelWsOpArchiMasse(urlWebService, ViStyle.VI_OK, viParams, formulaire,
            testLibre);

   }

   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire
    */
   public final void regardeResultatsTdm(
         CaptureMasseResultatFormulaire formulaire) {

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      resultatTest.setStatus(TestStatusEnum.SansStatus);

      // Récupère l'URL ECDE du fichier sommaire.xml
      String urlEcdeSommaire = formulaire.getUrlSommaire();

      // Récupère le chemin complet du fichier flag
      String cheminFichierFlag = getCheminFichierFlag(urlEcdeSommaire);

      // On regarde si le fichier flag est présent ou non
      log.appendLog("Présence du fichier flag " + cheminFichierFlag + " : ");
      File file = new File(cheminFichierFlag);
      boolean fichierFlagPresent = file.exists();
      if (fichierFlagPresent) {
         log.appendLogLn("OUI");
         log
               .appendLogLn("=> Le traitement de masse est considéré comme terminé");
      } else {
         log.appendLogLn("NON");
         log
               .appendLogLn("=> Le traitement de masse n'est pas considéré comme terminé (pas encore commencé ou en cours)");
      }

      // Si le fichier flag est présent, on jette un coup d'oeil au
      // resultats.xml
      if (fichierFlagPresent) {

         // Récupère le chemin complet du fichier resultats.xml
         String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlEcdeSommaire);

         // On vérifie dans un premier temps que le fichier resultats existe
         log.appendLogNewLine();
         log.appendLog("Présence du fichier résultats "
               + cheminFichierResultatsXml + " : ");
         file = new File(cheminFichierResultatsXml);
         boolean fichierResultatsPresent = file.exists();
         if (fichierResultatsPresent) {

            // Le fichier resultats.xml est présent
            log.appendLogLn("OUI");
            log.appendLogNewLine();

            // Charge le fichier resultats.xml
            ResultatsType objResultatXml = ecdeService
                  .chargeResultatsXml(cheminFichierResultatsXml);

            String startFilePath = getCheminFichierDebutFlag(formulaire
                  .getUrlSommaire());

            // Affiche dans le log un résumé du fichier resultats.xml
            SaeServiceLogUtils.logResultatsXml(resultatTest, objResultatXml,
                  cheminFichierResultatsXml, startFilePath);

         } else {

            // Test en échec : le fichier flag est présent, mais pas le fichier
            // résultats
            resultatTest.setStatus(TestStatusEnum.Echec);
            log.appendLogLn("NON");
            log.appendLogNewLine();
            log
                  .appendLogLn("Erreur : le fichier flag est présent, on devrait trouver un fichier résultats");

         }

      }

   }

   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire
    */
   public final void testResultatsTdmReponseOKAttendue(
         CaptureMasseResultatFormulaire formulaire) {

      boolean fileExists = fileResultatExists(formulaire);

      if (fileExists) {

         String urlSommaire = formulaire.getUrlSommaire();
         String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlSommaire);
         ResultatTestLog log = formulaire.getResultats().getLog();
         ResultatsType objResultatXml = ecdeService
               .chargeResultatsXml(cheminFichierResultatsXml);
         ResultatTest resultatTest = formulaire.getResultats();

         String startFilePath = getCheminFichierDebutFlag(formulaire
               .getUrlSommaire());

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(resultatTest, objResultatXml,
               cheminFichierResultatsXml, startFilePath);

         if (objResultatXml.getErreurBloquanteTraitement() != null) {
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            log.appendLogLn("Erreur présente dans le fichier de résultat : "
                  + objResultatXml.getErreurBloquanteTraitement().getLibelle());
         } else if (objResultatXml.getNonIntegratedDocumentsCount() > 0) {
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            log.appendLogLn(objResultatXml.getNonIntegratedDocumentsCount()
                  + " documents non intégrés");

         } else {
            formulaire.getResultats().setStatus(TestStatusEnum.Succes);
         }
      }
   }

   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire
    * @param notIntegratedDocuments
    *           le nombre de documents non intégrés attendu
    * @param documentType
    *           erreur attendue pour le document donné
    * @param index
    *           index du document contenant l'erreur
    */
   public final void testResultatsTdmReponseKOAttendue(
         CaptureMasseResultatFormulaire formulaire, int notIntegratedDocuments,
         NonIntegratedDocumentType documentType, int index) {
      processReponseKoAttendue(formulaire, notIntegratedDocuments,
            documentType, index);
   }

   /**
    * Regarde les résultats d'un traitement de masse. Recherche l'erreur donnée
    * dans l'ensemble des résultats
    * 
    * @param formulaire
    *           le formulaire
    * @param notIntegratedDocuments
    *           le nombre de documents non intégrés attendu
    * @param documentType
    *           erreur attendue pour le document donné
    */
   public final void testResultatsTdmReponseKOAttendue(
         CaptureMasseResultatFormulaire formulaire, int notIntegratedDocuments,
         NonIntegratedDocumentType documentType) {

      processReponseKoAttendue(formulaire, notIntegratedDocuments,
            documentType, Integer.MIN_VALUE);
   }

   private void processReponseKoAttendue(
         CaptureMasseResultatFormulaire formulaire, int notIntegratedDocuments,
         NonIntegratedDocumentType documentType, int index) {
      boolean fileExists = fileResultatExists(formulaire);

      if (fileExists) {

         String urlSommaire = formulaire.getUrlSommaire();
         String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlSommaire);
         ResultatTestLog log = formulaire.getResultats().getLog();
         ResultatsType objResultatXml = ecdeService
               .chargeResultatsXml(cheminFichierResultatsXml);

         String startFilePath = getCheminFichierDebutFlag(formulaire
               .getUrlSommaire());

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(formulaire.getResultats(),
               objResultatXml, cheminFichierResultatsXml, startFilePath);

         if (objResultatXml.getNonIntegratedDocumentsCount() != notIntegratedDocuments) {
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            log.appendLogLn("Le nombre de documents non intégrés est de "
                  + objResultatXml.getNonIntegratedDocumentsCount()
                  + " alors que nous en attendions " + notIntegratedDocuments);

         } else if (objResultatXml.getNonIntegratedDocuments() == null
               || objResultatXml.getNonIntegratedDocuments()
                     .getNonIntegratedDocument() == null
               || objResultatXml.getNonIntegratedDocuments()
                     .getNonIntegratedDocument().size() < index) {
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            log
                  .appendLogLn("Aucun document non intégré listé ou index de document erroné");
         } else {
            if (Integer.MIN_VALUE != index) {
               findNonIntegratedDocument(formulaire, documentType,
                     objResultatXml.getNonIntegratedDocuments()
                           .getNonIntegratedDocument(), index);
            } else {
               findNonIntegratedDocument(formulaire, documentType.getErreurs()
                     .getErreur().get(0), objResultatXml
                     .getNonIntegratedDocuments().getNonIntegratedDocument());
            }
         }

      }
   }
   
   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire
    * @param notIntegratedVirtualDocuments
    *           le nombre de documents non intégrés attendu
    * @param documentType
    *           erreur attendue pour le document donné
    * @param indexNumVirtualDoc
    *           index du document contenant l'erreur
    * @param indexComposant
    *           index du composant dans le document contenant l'erreur
    */
   public final void testResultatsTdmReponseVirtualDocumentKOAttendue(
         CaptureMasseResultatFormulaire formulaire, int notIntegratedVirtualDocuments,
         NonIntegratedVirtualDocumentType documentType, int indexNumVirtualDoc, int indexComposant) {

      processReponseVirtualDocumentKoAttendue(formulaire, notIntegratedVirtualDocuments,
            documentType, indexNumVirtualDoc, indexComposant);
   }
   
   private void processReponseVirtualDocumentKoAttendue(
         CaptureMasseResultatFormulaire formulaire, int notIntegratedVirtualDocuments,
         NonIntegratedVirtualDocumentType documentType, int indexNumVirtualDoc, int indexComposant) {
      boolean fileExists = fileResultatExists(formulaire);

      if (fileExists) {

         String urlSommaire = formulaire.getUrlSommaire();
         String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlSommaire);
         ResultatTestLog log = formulaire.getResultats().getLog();
         ResultatsType objResultatXml = ecdeService
               .chargeResultatsXml(cheminFichierResultatsXml);

         String startFilePath = getCheminFichierDebutFlag(formulaire
               .getUrlSommaire());

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(formulaire.getResultats(),
               objResultatXml, cheminFichierResultatsXml, startFilePath);

         if (objResultatXml.getNonIntegratedVirtualDocumentsCount() != notIntegratedVirtualDocuments) {
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            log.appendLogLn("Le nombre de documents virtuels non intégrés est de "
                  + objResultatXml.getNonIntegratedDocumentsCount()
                  + " alors que nous en attendions " + notIntegratedVirtualDocuments);

         } else if (objResultatXml.getNonIntegratedVirtualDocuments() == null
               || objResultatXml.getNonIntegratedVirtualDocuments()
                     .getNonIntegratedVirtualDocument() == null
               || objResultatXml.getNonIntegratedVirtualDocuments()
                     .getNonIntegratedVirtualDocument().size() < indexNumVirtualDoc) {
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            log
                  .appendLogLn("Aucun document virtuel non intégré listé ou index de document erroné");
         } else {
            if (Integer.MIN_VALUE != indexNumVirtualDoc) {
               findNonIntegratedVirtualDocument(formulaire, documentType,
                     objResultatXml.getNonIntegratedVirtualDocuments()
                           .getNonIntegratedVirtualDocument(), indexNumVirtualDoc);
            } else {
               findNonIntegratedVirtualDocument(formulaire, documentType, 
                     objResultatXml.getNonIntegratedVirtualDocuments()
                           .getNonIntegratedVirtualDocument(), 0);
            }
         }

      }
   }

   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire le nombre de documents non intégrés attendu
    * @param waitedError
    *           erreur bloquante attendue
    */
   public final void testResultatsTdmReponseKOAttendue(
         CaptureMasseResultatFormulaire formulaire, ErreurType waitedError) {

      boolean fileExists = fileResultatExists(formulaire);

      if (fileExists) {

         String urlSommaire = formulaire.getUrlSommaire();
         String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlSommaire);
         ResultatTestLog log = formulaire.getResultats().getLog();
         ResultatsType objResultatXml = ecdeService
               .chargeResultatsXml(cheminFichierResultatsXml);

         String startFilePath = getCheminFichierDebutFlag(formulaire
               .getUrlSommaire());

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(formulaire.getResultats(),
               objResultatXml, cheminFichierResultatsXml, startFilePath);

         if (objResultatXml.getErreurBloquanteTraitement() == null
               || waitedError == null) {
            log
                  .appendLogLn("Impossible de comparer l'erreur attendue et l'erreur obtenue");
            if (waitedError == null) {
               log.appendLog("erreur attendue nulle ");
            }
            if (objResultatXml.getErreurBloquanteTraitement() == null) {
               log.appendLog("erreur obtenue nulle");
            }
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);

         } else {

            if (objResultatXml.getErreurBloquanteTraitement().getCode() == null
                  || objResultatXml.getErreurBloquanteTraitement().getLibelle() == null
                  || waitedError.getCode() == null
                  || waitedError.getLibelle() == null
                  || !waitedError.getCode()
                        .equalsIgnoreCase(
                              objResultatXml.getErreurBloquanteTraitement()
                                    .getCode())
                  || !waitedError.getLibelle().equalsIgnoreCase(
                        objResultatXml.getErreurBloquanteTraitement()
                              .getLibelle())) {
               log
                     .appendLogLn("L'erreur obtenue et l'erreur attendue sont différentes :");
               log.appendLogLn("attendue : code : " + waitedError.getCode()
                     + "  /  libellé : " + waitedError.getLibelle());
               log.appendLogLn("obtenue : code : "
                     + objResultatXml.getErreurBloquanteTraitement().getCode()
                     + "  /  libellé : "
                     + objResultatXml.getErreurBloquanteTraitement()
                           .getLibelle());
               formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            } else {
               log
                     .appendLogLn("L'erreur obtenue et l'erreur attendue sont identiques");
               formulaire.getResultats().setStatus(TestStatusEnum.Succes);
            }

         }

      }
   }

   /**
    * Vérifie que les différents fichiers existent, et log au fur et à mesure
    * 
    * @param formulaire
    *           : formulaire de résultat de capture de masse
    * 
    * @return un booleen définissant l'existence ou non du fichier sommaire.xml
    */
   private boolean fileResultatExists(CaptureMasseResultatFormulaire formulaire) {
      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      resultatTest.setStatus(TestStatusEnum.SansStatus);

      // Récupère l'URL ECDE du fichier sommaire.xml
      String urlEcdeSommaire = formulaire.getUrlSommaire();

      // Récupère le fichier de début de traitement
      String cheminFichierDebutFlag = getCheminFichierDebutFlag(urlEcdeSommaire);

      // Récupère le chemin complet du fichier flag
      String cheminFichierFlag = getCheminFichierFlag(urlEcdeSommaire);

      // test de la présence du fichier debut_traitement.flag
      boolean fileExists = testFileExists(cheminFichierDebutFlag, log,
            formulaire);

      if (fileExists) {

         // test de la présence du fichier fin_traitement.flag
         fileExists = testFileExists(cheminFichierFlag, log, formulaire);

         if (fileExists) {

            File startFile = new File(cheminFichierDebutFlag);
            File endFile = new File(cheminFichierFlag);

            long endDate = endFile.lastModified();
            long startDate = startFile.lastModified();
            long time = endDate - startDate;

            log.appendLogLn("temps de traitement = " + getDuration(time));

            String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlEcdeSommaire);
            fileExists = testFileExists(cheminFichierResultatsXml, log,
                  formulaire);
         }

      }

      return fileExists;
   }

   /**
    * Vérification qu'aucun fichier de traitement n'a été créé
    * 
    * @param captureMasseResultat
    *           formulaire
    * @param urlEcde
    *           url ecde du fichier sommaire.xml
    */
   public final void testResultatsTdmReponseAucunFichierAttendu(
         CaptureMasseResultatFormulaire captureMasseResultat, String urlEcde) {

      String startFlagFilePath = getCheminFichierDebutFlag(urlEcde);
      File startFlagFile = new File(startFlagFilePath);
      ResultatTestLog log = captureMasseResultat.getResultats().getLog();

      if (startFlagFile.exists()) {
         log.appendLogLn("Un traitement a été lancé sur l'URL " + urlEcde);
         captureMasseResultat.getResultats().setStatus(TestStatusEnum.Echec);
      } else {
         log.appendLogLn("Pas de fichier de traitement présent sur " + urlEcde);
         captureMasseResultat.getResultats().setStatus(TestStatusEnum.Succes);
      }

   }
   
   /**
    * Vérification qu'aucun fichier fin de traitement n'a été créé
    * 
    * @param captureMasseResultat
    *           formulaire
    * @param urlEcde
    *           url ecde du fichier sommaire.xml
    */
   public final void testResultatsTdmReponseEnCours(
         CaptureMasseResultatFormulaire captureMasseResultat, String urlEcde) {

      String startFlagFilePath = getCheminFichierDebutFlag(urlEcde);
      String endFlagFilePath = getCheminFichierFlag(urlEcde);
      ResultatTestLog log = captureMasseResultat.getResultats().getLog();
      
      // test de la présence du fichier debut_traitement.flag
      boolean fileExists = testFileExists(startFlagFilePath, log,
            captureMasseResultat);

      if (fileExists) {
         fileExists = testFileExists(endFlagFilePath, log,
               captureMasseResultat);
         if (!fileExists) {
            captureMasseResultat.getResultats().setStatus(TestStatusEnum.Succes);
         } else {
            log.appendLogLn("Un fichier de fin de traitement est présent sur " + urlEcde);
            captureMasseResultat.getResultats().setStatus(TestStatusEnum.Echec);
         }
         
      } else {
         log.appendLogLn("Pas de fichier de traitement présent sur " + urlEcde);
         captureMasseResultat.getResultats().setStatus(TestStatusEnum.Echec);
      }
   }

   /**
    * @param formulaire
    *           formulaire affiché
    * @param documentType
    *           erreur attendue
    * @param nonIntegratedDocument
    *           liste des documents non intégrée
    * @param index
    *           n° de document où doit se trouver l'erreur.
    */
   private void findNonIntegratedDocument(
         CaptureMasseResultatFormulaire formulaire,
         NonIntegratedDocumentType documentType,
         List<NonIntegratedDocumentType> nonIntegratedDocument, int index) {
      
      if (documentType == null
            || documentType.getObjetNumerique() == null
            || StringUtils.isBlank(documentType.getObjetNumerique()
                  .getCheminEtNomDuFichier())
            || documentType.getErreurs() == null
            || documentType.getErreurs().getErreur() == null
            || documentType.getErreurs().getErreur().isEmpty()) {
         formulaire.getResultats().getLog().appendLogLn(
               "L'objet d'erreur attendu est incomplet");
         formulaire.getResultats().setStatus(TestStatusEnum.Echec);

      } else {

         HashMap<String, String> mapErreurs = new HashMap<String, String>();
         for (ErreurType erreurType : documentType.getErreurs().getErreur()) {
            mapErreurs.put(erreurType.getCode(), erreurType.getLibelle());         
         }

         NonIntegratedDocumentType found = nonIntegratedDocument.get(index);

         if (found == null) {
            formulaire.getResultats().getLog().appendLogLn(
                  "Impossible de trouver l'erreur correspondant au fichier");
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);

         } else {
            boolean hasError = false;
            int i = 0;
            String label;
            List<ErreurType> listErreurs = found.getErreurs().getErreur();
            formulaire.getResultats().getLog().appendLogLn("");
            while (!hasError && i < listErreurs.size()) {
               label = mapErreurs.get(listErreurs.get(i).getCode());
               if (label == null
                     || !label
                           .equalsIgnoreCase(listErreurs.get(i).getLibelle())) {
                  hasError = true;
                  
               }
               i++;
            }

            if (hasError) {
               formulaire
                     .getResultats()
                     .getLog()
                     .appendLogLn(
                           "Impossible de trouver toutes les erreurs dans le document non intégré");

               formulaire.getResultats().getLog().appendLogLn(
                     "erreurs attendues : ");

               for (ErreurType erreurType : documentType.getErreurs()
                     .getErreur()) {
                  formulaire.getResultats().getLog().appendLogLn(
                        "code : " + erreurType.getCode() + "  /  libellé : "
                              + erreurType.getLibelle());

               }

               formulaire.getResultats().getLog().appendLogLn(
                     "erreurs obtenues : ");

               for (ErreurType erreurType : found.getErreurs().getErreur()) {
                  formulaire.getResultats().getLog().appendLogLn(
                        "code : " + erreurType.getCode() + "  /  libellé : "
                              + erreurType.getLibelle());

               }

               formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            } else {
               formulaire.getResultats().getLog().appendLogLn(
                     "Toutes les erreurs attendues ont été trouvées : A VERIFIER ");
               formulaire.getResultats().setStatus(TestStatusEnum.Succes);
           }

         }
      }
   }
   
   /**
    * @param formulaire
    *           formulaire affiché
    * @param documentType
    *           erreur attendue
    * @param nonIntegratedDocument
    *           liste des documents non intégrée
    * @param indexNumVirtualDoc
    *           n° de document où doit se trouver l'erreur.
    */
   private void findNonIntegratedVirtualDocument(
         CaptureMasseResultatFormulaire formulaire,
         NonIntegratedVirtualDocumentType documentType,
         List<NonIntegratedVirtualDocumentType> nonIntegratedVirtualDocument, int indexNumVirtualDoc) {

      if (documentType == null
            || documentType.getObjetNumerique() == null
            || StringUtils.isBlank(documentType.getObjetNumerique()
                  .getCheminEtNomDuFichier())
            || documentType.getErreurs() == null
            || documentType.getErreurs().getErreur() == null
            || documentType.getErreurs().getErreur().isEmpty()) {
         formulaire.getResultats().getLog().appendLogLn(
               "L'objet d'erreur attendu est incomplet");
         formulaire.getResultats().setStatus(TestStatusEnum.Echec);

      } else {

         HashMap<String, String> mapErreurs = new HashMap<String, String>();
         for (ErreurType erreurType : documentType.getErreurs().getErreur()) {
            mapErreurs.put(erreurType.getCode(), erreurType.getLibelle());
         }

         NonIntegratedVirtualDocumentType found = nonIntegratedVirtualDocument.get(indexNumVirtualDoc);

         if (found == null) {
            formulaire.getResultats().getLog().appendLogLn(
                  "Impossible de trouver l'erreur correspondant au fichier");
            formulaire.getResultats().setStatus(TestStatusEnum.Echec);

         } else {
            boolean hasError = false;
            int i = 0;
            String label;
            List<ErreurType> listErreurs = found.getErreurs().getErreur();
            while (!hasError && i < listErreurs.size()) {
               label = mapErreurs.get(listErreurs.get(i).getCode());
               if (label == null
                     || !label
                           .equalsIgnoreCase(listErreurs.get(i).getLibelle())) {
                  hasError = true;
               }

               i++;
            }

            if (hasError) {
               formulaire
                     .getResultats()
                     .getLog()
                     .appendLogLn(
                           "Impossible de trouver toutes les erreurs dans le document non intégré");

               formulaire.getResultats().getLog().appendLogLn(
                     "erreurs attendues : ");

               for (ErreurType erreurType : documentType.getErreurs()
                     .getErreur()) {
                  formulaire.getResultats().getLog().appendLogLn(
                        "code : " + erreurType.getCode() + "  /  libellé : "
                              + erreurType.getLibelle());
               }

               formulaire.getResultats().getLog().appendLogLn(
                     "erreurs obtenues : ");

               for (ErreurType erreurType : found.getErreurs().getErreur()) {
                  formulaire.getResultats().getLog().appendLogLn(
                        "code : " + erreurType.getCode() + "  /  libellé : "
                              + erreurType.getLibelle());
               }

               formulaire.getResultats().setStatus(TestStatusEnum.Echec);
            } else {
               formulaire.getResultats().getLog().appendLogLn(
                     "Toutes les erreurs attendues ont été trouvées");
               formulaire.getResultats().setStatus(TestStatusEnum.Succes);
            }
         }

      }

   }

   /**
    * teste la présence d'un fichier. Si le fichier n'est pas présent, on
    * positionne le status du formulaire à en échec. De toutes les manières, on
    * log le résultat
    * 
    * @param cheminFichierDebutFlag
    */
   private boolean testFileExists(String cheminFichierDebutFlag,
         ResultatTestLog log, CaptureMasseResultatFormulaire formulaire) {
      File file = new File(cheminFichierDebutFlag);
      boolean fichierFlagPresent = file.exists();
      log.appendLog("Présence du fichier "
            + new File(cheminFichierDebutFlag).getName() + " : ");
      if (fichierFlagPresent) {
         log.appendLogLn("OUI");
      } else {
         log.appendLogLn("NON");
         formulaire.getResultats().setStatus(TestStatusEnum.Echec);
      }

      return fichierFlagPresent;
   }

   /**
    * Recherche une erreur donnée dans l'ensemble du fichier de résultat
    * 
    * @param formulaire
    *           formulaire affiché
    * @param erreurType
    *           erreur recherchée
    * @param nonIntegratedDocument
    *           liste documents non intégrés
    */
   private void findNonIntegratedDocument(
         CaptureMasseResultatFormulaire formulaire, ErreurType erreurType,
         List<NonIntegratedDocumentType> nonIntegratedDocument) {

      boolean errorFound = false;
      int i = 0;
      int j = 0;
      NonIntegratedDocumentType currentNid;
      List<ErreurType> listErrors;
      ErreurType currentError = null;

      while (!errorFound && i < nonIntegratedDocument.size()) {

         currentNid = nonIntegratedDocument.get(i);

         if (currentNid.getErreurs() != null
               && currentNid.getErreurs().getErreur() != null) {
            j = 0;
            listErrors = currentNid.getErreurs().getErreur();

            while (!errorFound && j < listErrors.size()) {
               currentError = listErrors.get(j);

               if (currentError != null
                     && erreurType.getCode().equals(currentError.getCode())
                     && erreurType.getLibelle().equals(
                           currentError.getLibelle())) {
                  errorFound = true;
               }

               j++;

            }

         }

         i++;
      }

      ResultatTestLog log = formulaire.getResultats().getLog();
      if (!errorFound) {
         log.appendLogLn("- Aucun des enregistrements ne contient l'erreur souhaitée :");         
         log.appendLogLn("code : " + erreurType.getCode());
         log.appendLogLn("libellé : " + erreurType.getLibelle());
         log.appendLogLn("- Erreur trouvée dans le fichier resultats.xml : ");
         log.appendLogLn("code : " + currentError.getCode());
         log.appendLogLn("libellé : " + currentError.getLibelle());
         formulaire.getResultats().setStatus(TestStatusEnum.Echec);
      } else {
         log
               .appendLogLn("L'erreur a bien été retrouvée dans le fichier de résultat");
         log.appendLogLn("code : " + erreurType.getCode());
         log.appendLogLn("libellé : " + erreurType.getLibelle());
         formulaire.getResultats().setStatus(TestStatusEnum.Succes);
      }
   }

   /**
    * Supprime le fichier s'il existe
    * 
    * @param path
    *           : chemin complet du fichier
    */
   private void deleteFileIfExists(String path) {
      File file = new File(path);
      if (file.exists()) {
         file.delete();
      }
   }

   // private String getRepertoireTraitement(String repDocuments) {
   //      
   // // Le répertoire documents correspond à :
   // // => racineEcde/AAAAMMJJ/idTraitements/documents/
   // // On cherche à retirer le répertoire documents pour obtenir :
   // // => racineEcde/AAAAMMJJ/idTraitements/
   //      
   // String repDocumentsOk =
   // FilenameUtils.normalizeNoEndSeparator(repDocuments);
   // int idx = FilenameUtils.indexOfLastSeparator(repDocumentsOk);
   // String repTraitement = repDocumentsOk.substring(0,idx);
   // repTraitement = FilenameUtils.normalizeNoEndSeparator(repTraitement);
   //      
   // return repTraitement;
   //      
   // }

   // private String getRepertoireDocuments(String cheminFicSommaire) {
   //      
   // String repDocuments = FilenameUtils.getFullPath(cheminFicSommaire);
   // repDocuments = FilenameUtils.normalizeNoEndSeparator(repDocuments);
   // return repDocuments;
   //      
   // }

   private String getRepertoireTraitement(String cheminFicSommaire) {

      String repTraitement = FilenameUtils.getFullPath(cheminFicSommaire);

      return repTraitement;

   }

   private String getCheminFichierFlag(String urlEcdeSommaire) {

      String cheminFicSommaire = ecdeService
            .convertUrlEcdeToPath(urlEcdeSommaire);

      String repTraitement = getRepertoireTraitement(cheminFicSommaire);

      String cheminFichierFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_FLAG_TDM);

      return cheminFichierFlag;

   }

   private String getCheminFichierDebutFlag(String urlEcdeSommaire) {

      String cheminFicSommaire = ecdeService
            .convertUrlEcdeToPath(urlEcdeSommaire);

      String repTraitement = getRepertoireTraitement(cheminFicSommaire);

      String cheminFichierFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_DEB_FLAG_TDM);

      return cheminFichierFlag;

   }

   private String getCheminFichierResultatsXml(String urlEcdeSommaire) {

      String cheminFicSommaire = ecdeService
            .convertUrlEcdeToPath(urlEcdeSommaire);

      String repTraitement = getRepertoireTraitement(cheminFicSommaire);

      String cheminFichierResultatsXml = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_RESULTATS);

      return cheminFichierResultatsXml;

   }

   private String getDuration(long time) {
      String value = DurationFormatUtils.formatDurationWords(time, true, true);

      value = value.replace("second", "seconde");
      value = value.replace("hour", "heure");
      value = value.replace("day", "jour");

      return value;

   }

   /**
    * Comptages par rapport à l'id du traitement de masse
    * 
    * @param idTdm
    *           l'identifiant du traitement de masse
    * @param resultatTest
    *           l'objet de résultat du test en cours
    * @param comptageDfceAttendu
    *           : le comptage DFCE attendu. Mettre null si pas de comptage
    *           attendu
    * @return <ol>
    *         <li>Premier élément : comptage dans DFCE</li>
    *         </ol>
    */
   public List<Comptage> comptages(String idTdm, ResultatTest resultatTest,
         Long comptageDfceAttendu) {

      // Création de l'objet résultat
      List<Comptage> result = new ArrayList<Comptage>();

      // Comptage dans DFCE
      Comptage objComptageDfce = comptageDfce(idTdm, resultatTest,
            comptageDfceAttendu);
      result.add(objComptageDfce);

      // Renvoie le résultat
      return result;

   }

   private Comptage comptageDfce(String idTdm, ResultatTest resultatTest,
         Long comptageDfceAttendu) {

      ResultatTestLog resultatLog = resultatTest.getLog();

      resultatLog.appendLogLn("Comptage DFCE");
      Comptage comptageDfce = new Comptage();
      try {

         Long comptageDfceObtenu = dfceService.compteNbDocsTdm(UUID
               .fromString(idTdm));

         comptagesTraiteResultat(resultatTest, comptageDfce,
               comptageDfceObtenu, comptageDfceAttendu);

      } catch (IntegrationException e) {

         comptagesTraiteException(resultatTest, comptageDfce, e,
               comptageDfceAttendu);

      }
      resultatLog.appendLogNewLine();

      return comptageDfce;

   }

   private void comptagesTraiteResultat(ResultatTest resultatTest,
         Comptage objetComptage, Long comptageObtenu, Long comptageAttendu) {

      // Mémorise le comptage dans l'objet renvoyé en retour de la méthode
      objetComptage.setComptage(comptageObtenu);

      // Ajoute simplement le comptage dans les logs
      resultatTest.getLog().appendLogLn(Long.toString(comptageObtenu));

      // Si un comptage est attendu, on fait la comparaison
      if ((comptageAttendu != null)
            && (!comptageObtenu.equals(comptageAttendu))) {

         resultatTest.setStatus(TestStatusEnum.Echec);

         resultatTest.getLog().appendLogLn(
               "Erreur: on attendait " + comptageAttendu);

      }

   }

   private void comptagesTraiteException(ResultatTest resultatTest,
         Comptage objetComptage, Exception exception, Long comptageAttendu) {

      // Récupère l'exception sous forme de String
      String erreur = ExceptionUtils.exceptionToString(exception);

      // Mémorise l'erreur dans l'objet renvoyé en retour de la méthode
      objetComptage.setErreur(erreur);

      // Ajoute l'erreur dans les logs
      resultatTest.getLog().appendLogLn(erreur);

      // Si un comptage était attendu, on met le test en erreur
      if (comptageAttendu != null) {
         resultatTest.setStatus(TestStatusEnum.Echec);
      }

   }

   private CaptureMasseResultat fromCaptureMasseAncienService(
         ArchivageMasseResponse response) {

      CaptureMasseResultat result = new CaptureMasseResultat();
      result.setAppelAvecHashSommaire(false);

      return result;

   }

   private CaptureMasseResultat fromCaptureMasseAvecHash(
         ArchivageMasseAvecHashResponse response) {

      CaptureMasseResultat result = new CaptureMasseResultat();
      result.setAppelAvecHashSommaire(true);
      result.setIdTraitement(response.getArchivageMasseAvecHashResponse()
            .getUuid());

      return result;

   }

   /**
    * Methode permettant d'aller lire l'identifiant de traitement de masse dans
    * le fichier debut_traitement.flag.
    * 
    * @param urlEcdeSommaire
    *           url du fichier sommaire de l'ecde
    * @return String : identifiant du traitement de masse
    */
   public String readIdTdmInDebutTrait(String urlEcdeSommaire) {

      String idTdm = "";

      // Récupère le fichier de début de traitement
      String cheminFichierDebutFlag = getCheminFichierDebutFlag(urlEcdeSommaire);

      File fileDebutFlag = new File(cheminFichierDebutFlag);

      // test de la présence du fichier debut_traitement.flag
      if (fileDebutFlag.exists()) {

         Properties propFichierDebutFlag = new Properties();
         try {
            propFichierDebutFlag.load(new FileInputStream(fileDebutFlag));
            idTdm = (String) propFichierDebutFlag.get("idTraitementMasse");

         } catch (FileNotFoundException e) {
            // nothing to do
         } catch (IOException e) {
            // nothing to do
         }
      }
      return idTdm;
   }

}