package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireAll;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;

/**
 * 1154-Droits-Archivage-Masse-KO
 */
@Controller
@RequestMapping(value = "test1154")
public class Test1154Controller extends
      AbstractTestWsController<TestFormulaireAll> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1154";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCuCmReCo";
   }

   private static final int WAITED_COUNT = 1;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireAll getFormulairePourGet() {

      // Création du formulaire
      TestFormulaireAll formulaire = new TestFormulaireAll();

      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer("INT_CS_ATT_VIGI");
      PagmList pagmList = new PagmList();
      pagmList.add("INT_PAGM_ATT_VIGI_ALL_SAUF_AM");
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
                        "SAE_INTEGRATION/20110822/Droit-1154-Droits-Archivage-Masse-KO/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      // Des métadonnées exemples
      formCapture.setNomFichier("ADELPF_710_PSNV211157BPCA1L0000.pdf");
      MetadonneeValeurList metasExemples = ReferentielMetadonneesService
            .getMetadonneesExemplePourCapture();
      metasExemples.modifieValeurMeta(
            SaeIntegrationConstantes.META_CODE_ORG_PROPRIETAIRE, "UR750");
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_HASH,
            "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      formCapture.getMetadonnees().addAll(metasExemples);
      formCapture.getMetadonnees().add("Denomination",
            "Test 1154-Droits-Archivage-Masse-KO");

      // Valeurs initiales des formulaires pour la capture de masse
      // Formulaire pour l'appel au WS de capture de masse
      CaptureMasseFormulaire formCaptMasseDecl = formulaire
            .getCaptureMasseDeclenchement();
      formCaptMasseDecl
            .setUrlSommaire(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1154-Droits-Archivage-Masse-KO/sommaire.xml"));
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
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("CodeOrganismeProprietaire");
      codesMeta.add("CodeOrganismeGestionnaire");
      codesMeta.add("CodeRND");
      codesMeta.add("DateCreation");
      codesMeta.add("Denomination");

      formRecherche.setCodeMetadonnees(codesMeta);
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Valeurs initiales du formulaire pour la consutlation
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Fin
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireAll formulaire) {

      String etape = formulaire.getEtape();

      if ("1".equals(etape)) {

         captureUnitaire(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureUnitaire(), formulaire.getViFormulaire());

      } else if ("2".equals(etape)) {

         captureMasseEtape1AppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("3".equals(etape)) {

         captureMasseEtape2LectureResultats(formulaire
               .getCaptureMasseDeclenchement().getUrlSommaire(), formulaire
               .getCaptureMasseResultat());

      } else if ("4".equals(etape)) {

         recherche(formulaire);

      } else if ("5".equals(etape)) {

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
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
            urlWebService, formulaire, viParams);

   }

   private void captureMasseEtape1AppelWs(String urlWebService,
         TestFormulaireAll formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(null);
      String[] result = new String[] { "" };
      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAttendue(
            urlWebService, formulaire.getCaptureMasseDeclenchement(),
            formulaire.getViFormulaire(), "sae_DroitsInsuffisants", result);
      // Renseigne le formulaire de l'étape 2

      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

   }

   private void captureMasseEtape2LectureResultats(String urlEcde,
         CaptureMasseResultatFormulaire captureMasseResultat) {

      getCaptureMasseTestService().testResultatsTdmReponseAucunFichierAttendu(
            captureMasseResultat, urlEcde);
   }

   private void recherche(TestFormulaireAll formulaire) {

      // Initialise
      ResultatTest resultatTest = formulaire.getRecherche().getResultats();

      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formulaire.getRecherche(),
                  WAITED_COUNT, false, TypeComparaison.NumeroRecours,
                  formulaire.getViFormulaire());

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Tri les résultats par ordre croissant de DateCreation
         List<ResultatRechercheType> resultatsTries = Arrays.asList(response
               .getRechercheResponse().getResultats().getResultat());
         Collections.sort(resultatsTries, new ResultatRechercheComparator(
               TypeComparaison.NumeroRecours));

         // Vérifie chaque résultat
         verifieResultatN(1, resultatsTries.get(0), resultatTest, "1");

      }

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);

         ResultatRechercheType results[] = response.getRechercheResponse()
               .getResultats().getResultat();
         formulaire.getConsultation().setIdArchivage(
               results[0].getIdArchive().getUuidType());
      }

   }

   private void consultation(String urlWebService,
         ConsultationFormulaire formulaire, ViFormulaire viParams) {

      // Appel de la méthode de test
      getConsultationTestService().appelWsOpConsultationTestLibre(
            urlWebService, formulaire, viParams);

   }

   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         String numeroRecours) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeOrganismeProprietaire", "UR750");
      valeursAttendues.add("CodeOrganismeGestionnaire", "CER69");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateCreation", "2011-09-01");
      valeursAttendues.add("Denomination",
            "Test 1154-Droits-Archivage-Masse-KO");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);

   }

}