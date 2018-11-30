package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1100Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;

/**
 * 1100-Droits-TestLibre
 */
@Controller
@RequestMapping(value = "test1100")
public class Test1100Controller extends
      AbstractTestWsController<Test1100Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1100";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1100Formulaire getFormulairePourGet() {

      // Création du formulaire
      Test1100Formulaire formulaire = new Test1100Formulaire();

      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      PagmList pagmList = new PagmList();
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);

      // Valeurs initiales du formulaire pour la capture unitaire
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Un exemple d'URL ECDE de fichier à capturer
      // (qui correspond à un document réellement existant sur l'ECDE
      // d'intégration)
      formCapture
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-100-CaptureUnitaire-TestLibre/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      // Des métadonnées exemples
      MetadonneeValeurList metasExemples = ReferentielMetadonneesService
            .getMetadonneesExemplePourCapture();
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_HASH,
            "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      formCapture.getMetadonnees().addAll(metasExemples);
      formCapture.getMetadonnees().add("Denomination",
            "Test 1100-Droits-TestLibre");

      // Valeurs initiales des formulaires pour la capture de masse
      // Formulaire pour l'appel au WS de capture de masse
      CaptureMasseFormulaire formCaptMasseDecl = formulaire
            .getCaptureMasseDeclenchement();
      formCaptMasseDecl.setUrlSommaire(getEcdeService().construitUrlEcde(
            "SAE_INTEGRATION/20110822/CaptureMasse-200/sommaire.xml"));
      formCaptMasseDecl.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Formulaire de lecture des fichiers flag et du resultats.xml
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.setUrlSommaire(formCaptMasseDecl.getUrlSommaire());
      formCaptMassRes.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Formulaire de comptage dans DFCE
      ComptagesTdmFormulaire comptageFormulaire = formulaire
            .getComptagesFormulaire();
      comptageFormulaire.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Valeurs initiales du formulaire pour la recherche
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());
      CodeMetadonneeList codesMeta = ReferentielMetadonneesService
            .getMetadonneesExemplePourRecherche();
      formRecherche.setCodeMetadonnees(codesMeta);
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Valeurs initiales du formulaire pour la consutlation
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.setIdArchivage(SaeServiceTestService.getIdArchivageExemple());
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Fin
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1100Formulaire formulaire) {

      String etape = formulaire.getEtape();

      if ("Capture unitaire".equals(etape)) {

         captureUnitaire(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureUnitaire(), formulaire.getViFormulaire());

      } else if ("Capture Masse #1".equals(etape)) {

         captureMasseEtape1AppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("Capture Masse #2".equals(etape)) {

         captureMasseEtape2LectureResultats(formulaire
               .getCaptureMasseResultat());

      } else if ("Capture Masse #3".equals(etape)) {

         captureMasseEtape3Comptages(formulaire.getComptagesFormulaire());

      } else if ("Recherche".equals(etape)) {

         recherche(formulaire.getUrlServiceWeb(), formulaire.getRecherche(),
               formulaire.getViFormulaire());

      } else if ("Consultation".equals(etape)) {

         consultation(formulaire.getUrlServiceWeb(), formulaire
               .getConsultation(), formulaire.getViFormulaire());

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void captureUnitaire(String urlWebService,
         CaptureUnitaireFormulaire formulaire, ViFormulaire viParams) {

      // Appel de la méthode de test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireUrlEcdeTestLibre(
            urlWebService, formulaire, viParams);

   }

   private void captureMasseEtape1AppelWs(String urlWebService,
         Test1100Formulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(null);

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseTestLibre(urlWebService,
            formulaire.getCaptureMasseDeclenchement(),
            formulaire.getViFormulaire());

      // Renseigne le formulaire de l'étape 2
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

   }

   private void captureMasseEtape2LectureResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService().regardeResultatsTdm(formulaire);

   }

   private void captureMasseEtape3Comptages(ComptagesTdmFormulaire formulaire) {

      // Initialisation de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest, null);

      // Test sans status de résultat
      resultatTest.setStatus(TestStatusEnum.SansStatus);

   }

   private void recherche(String urlWebService, RechercheFormulaire rechForm,
         ViFormulaire viParams) {

      getRechercheTestService().appelWsOpRechercheTestLibre(urlWebService,
            rechForm, viParams);

   }

   private void consultation(String urlWebService,
         ConsultationFormulaire formulaire, ViFormulaire viParams) {

      // Appel de la méthode de test
      getConsultationTestService().appelWsOpConsultationTestLibre(
            urlWebService, formulaire, viParams);

   }

}
