package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.axis2.AxisFault;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.Comptage;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.TransfertMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.TransfertMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.TransfertMasseResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.dfce.DfceService;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.utils.ExceptionUtils;

@Service
public class TransfertMasseTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private EcdeService ecdeService;

   @Autowired
   private DfceService dfceService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private TransfertMasseResultat appelWsOpTransMasse(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         TransfertMasseFormulaire formulaire, WsTestListener wsListener) {

      TransfertMasseResultat result = null;

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
      SaeServiceLogUtils.logAppelTransfertMasse(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {

         // Selon si l'on demande l'opération archivageMasse ou
         // archivageMasseAvecHash

         TransfertMasse paramsService = SaeServiceObjectFactory
               .buildTransfertMasseRequest(formulaire.getUrlSommaire(),
                     formulaire.getHash(), formulaire.getTypeHash());

         TransfertMasseResponse response = service
               .transfertMasse(paramsService);

         // Construction de l'objet modèle de résultat
         result = fromTransfertMasse(response);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"transfertMasse\" :");
         SaeServiceLogUtils.logResultatTransfertMasse(resultatTest, result);

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

   /**
    * Test libre de l'appel à l'opération "transfertMasse" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final TransfertMasseResultat appelWsOpTransMasseTestLibre(
         String urlServiceWeb, TransfertMasseFormulaire formulaire,
         ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      return appelWsOpTransMasse(urlServiceWeb, ViStyle.VI_OK, viParams,
            formulaire, testLibre);

   }

   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire
    */
   public final void regardeResultatsTdm(
         TransfertMasseResultatFormulaire formulaire) {

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
         log.appendLogLn("=> Le traitement de masse est considéré comme terminé");
      } else {
         log.appendLogLn("NON");
         log.appendLogLn("=> Le traitement de masse n'est pas considéré comme terminé (pas encore commencé ou en cours)");
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
            log.appendLogLn("Erreur : le fichier flag est présent, on devrait trouver un fichier résultats");

         }

      }

   }

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

   private TransfertMasseResultat fromTransfertMasse(
         TransfertMasseResponse response) {

      TransfertMasseResultat result = new TransfertMasseResultat();
      result.setAppelAvecHashSommaire(true);
      result.setIdTraitement(response.getTransfertMasseResponse().getUuid());

      return result;

   }

}
