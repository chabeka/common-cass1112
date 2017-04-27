package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModificationMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ModificationMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ModificationMasseResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.dfce.DfceService;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;

@Service
public class ModificationMasseTestService {

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private EcdeService ecdeService;

   @Autowired
   private DfceService dfceService;

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private ModificationMasseResultat appelWsOpModifMasse(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         ModificationMasseFormulaire formulaire, WsTestListener wsListener) {

      ModificationMasseResultat result = null;

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

      SaeServiceLogUtils.logAppelModificationMasse(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      try {

         ModificationMasse paramsService = SaeServiceObjectFactory
               .buildModificationMasseRequest(formulaire.getUrlSommaire(),
                     formulaire.getHash(), formulaire.getTypeHash(),
                     formulaire.getCodeTraitement());

         ModificationMasseResponse response = service
               .modificationMasse(paramsService);

         result = fromModificationMasse(response);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"modificationMasse\" :");
         SaeServiceLogUtils.logResultatModificationMasse(resultatTest, result);

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
    * Test libre de l'appel à l'opération "modificationMasse" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    * @param viParams
    *           les paramètres du VI
    */
   public final ModificationMasseResultat appelWsOpModifMasseTestLibre(
         String urlServiceWeb, ModificationMasseFormulaire formulaire,
         ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      return appelWsOpModifMasse(urlServiceWeb, ViStyle.VI_OK, viParams,
            formulaire, testLibre);

   }
   
   /**
    * Regarde les résultats d'un traitement de masse
    * 
    * @param formulaire
    *           le formulaire
    */
   public final void regardeResultatsTdm(
         ModificationMasseResultatFormulaire formulaire) {

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

   private ModificationMasseResultat fromModificationMasse(
         ModificationMasseResponse response) {

      ModificationMasseResultat result = new ModificationMasseResultat();
      result.setAppelAvecHashSommaire(true);
      result.setIdTraitement(response.getModificationMasseResponse().getUuid());

      return result;

   }
}
