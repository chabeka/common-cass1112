package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSansSoapFault;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;
import fr.urssaf.image.sae.integration.ihmweb.utils.Base64Utils;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;

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

   private void appelWsOpArchiMasse(String urlServiceWeb,
         String ficRessourceVi, CaptureMasseFormulaire formulaire,
         WsTestListener wsListener) {

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
      SaeServiceStub service = SaeServiceStubUtils.getServiceStub(
            urlServiceWeb, ficRessourceVi);

      // Appel du service web et gestion de erreurs
      try {

         // Construction du paramètre d'entrée de l'opération
         ArchivageMasse paramsService = SaeServiceObjectFactory
               .buildArchivageMasseRequest(formulaire.getUrlSommaire());

         // Appel du service web
         service.archivageMasse(paramsService);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log
               .appendLogLn("Détails de la réponse obtenue de l'opération \"archivageMasse\" :");
         log.appendLogLn("Le service n'a pas renvoyé d'erreur");

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
   public final void appelWsOpArchiMasseTestLibre(String urlServiceWeb,
         CaptureMasseFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      appelWsOpArchiMasse(urlServiceWeb, ViUtils.FIC_VI_OK, formulaire,
            testLibre);

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
      appelWsOpArchiMasse(urlServiceWeb, ViUtils.FIC_VI_SANS_VI, formulaire,
            testAuth);

   }

   /**
    * appel de l'archivage de masse avec en attente aucune saop fault
    * 
    * @param urlWebService
    *           adresse du WS
    * @param formulaire
    *           formulaire affiché
    */
   public final void appelWsOpArchiMasseOKAttendu(String urlWebService,
         CaptureMasseFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplSansSoapFault();

      // Appel de la méthode "générique" de test
      appelWsOpArchiMasse(urlWebService, ViUtils.FIC_VI_OK, formulaire,
            testLibre);

      File file = new File(getCheminFichierDebutFlag(formulaire
            .getUrlSommaire()));

      TestStockageMasseAllFormulaire parentForm = (TestStockageMasseAllFormulaire) formulaire
            .getParent();

      try {
         int i = 0;
         while (!file.exists() && i < 10) {
            Thread.sleep(1001);
            i++;
         }
      } catch (InterruptedException e) {
         formulaire.getResultats().getLog().appendLog(e.toString());
      }

      try {
         if (file.exists()) {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);

            String host = properties.getProperty("hostIP");
            host = "http://" + host + ":8080/sae/SAETest.do";

            parentForm.setLinkToMonitoring(host);

            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
               fileReader = new FileReader(file);
               bufferedReader = new BufferedReader(fileReader);
               String line;
               formulaire.getResultats().getLog().appendLogLn(
                     "Contenu du fichier debut_traitement.flag :");
               while ((line = bufferedReader.readLine()) != null) {
                  formulaire.getResultats().getLog().appendLogLn(line);
               }
            } catch (Exception e) {
               // nothing to do
            } finally {
               try {
                  if (bufferedReader != null) {
                     bufferedReader.close();
                  }
               } catch (Exception e) {
                  // nothing to do
               }

               try {
                  if (fileReader != null) {
                     fileReader.close();
                  }
               } catch (Exception e) {
                  // nothing to do
               }
            }

         }
      } catch (FileNotFoundException e) {
         formulaire.getResultats().getLog().appendLog(e.toString());
      } catch (IOException e) {
         formulaire.getResultats().getLog().appendLog(e.toString());
      }

   }

   /**
    * appel de l'archivage de masse avec en attente une saop fault dont on
    * fournit le code
    * 
    * @param urlWebService
    *           adresse du WS
    * @param formulaire
    *           formulaire affiché
    */
   public final void appelWsOpArchiMasseSoapFaultAttendue(String urlWebService,
         CaptureMasseFormulaire formulaire, String soapFault, String[] args) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)

      SoapFault faultAttendue = refSoapFault.findSoapFault(soapFault);

      WsTestListener testLibre = new WsTestListenerImplSoapFault(faultAttendue,
            args);

      // Appel de la méthode "générique" de test
      appelWsOpArchiMasse(urlWebService, ViUtils.FIC_VI_OK, formulaire,
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

            // Affiche dans le log un résumé du fichier resultats.xml
            SaeServiceLogUtils.logResultatsXml(resultatTest, objResultatXml,
                  cheminFichierResultatsXml);

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

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(resultatTest, objResultatXml,
               cheminFichierResultatsXml);

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

      boolean fileExists = fileResultatExists(formulaire);

      if (fileExists) {

         String urlSommaire = formulaire.getUrlSommaire();
         String cheminFichierResultatsXml = getCheminFichierResultatsXml(urlSommaire);
         ResultatTestLog log = formulaire.getResultats().getLog();
         ResultatsType objResultatXml = ecdeService
               .chargeResultatsXml(cheminFichierResultatsXml);

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(formulaire.getResultats(),
               objResultatXml, cheminFichierResultatsXml);

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
            findNonIntegratedDocument(formulaire, documentType, objResultatXml
                  .getNonIntegratedDocuments().getNonIntegratedDocument(),
                  index);
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

         // Affiche dans le log un résumé du fichier resultats.xml
         SaeServiceLogUtils.logResultatsXml(formulaire.getResultats(),
               objResultatXml, cheminFichierResultatsXml);

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

         // Ajoute un lien de téléchargement du fichier
         String cheminBase64 = Base64Utils.encode(cheminFichierDebutFlag);
         resultatTest.getLiens().ajouteLien(
               SaeIntegrationConstantes.NOM_FIC_DEB_FLAG_TDM,
               "download.do?ecdefilename=" + cheminBase64);

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
         log
               .appendLogLn("Le fichier concerné est en lecture seule, aucune écriture n'est possible.");
         captureMasseResultat.getResultats().setStatus(TestStatusEnum.Echec);
      } else {
         log.appendLogLn("Pas de fichier de traitement présent sur " + urlEcde);
         captureMasseResultat.getResultats().setStatus(TestStatusEnum.Succes);
      }

   }

   /**
    * @param formulaire
    * @param documentType
    * @param nonIntegratedDocument
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

}
