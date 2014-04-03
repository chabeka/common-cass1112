package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
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
 * 1112-Droits-Conformite-All-ATT-AEPL
 */
@Controller
@RequestMapping(value = "test1112")
public class Test1112Controller extends
      AbstractTestWsController<TestFormulaireAll> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1112";
   }

   private static final int WAITED_COUNT =11;
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
      viForm.setIssuer("INT_CS_ATT_AEPL");
      PagmList pagmList = new PagmList();
      pagmList.add("INT_PAGM_ATT_AEPL_ALL");
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
                        "SAE_INTEGRATION/20110822/Droit-1112-Droits-Conformite-All-ATT-AEPL/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      // Des métadonnées exemples
      formCapture.setNomFichier("ADELPF_710_PSNV211157BPCA1L0000.pdf");
      MetadonneeValeurList metasExemples = ReferentielMetadonneesService
            .getMetadonneesExemplePourCapture();
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_CODE_ORG_PROPRIETAIRE,
            "UR750");
      metasExemples.modifieValeurMeta(SaeIntegrationConstantes.META_HASH,
      "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
      formCapture.getMetadonnees().addAll(metasExemples);
      formCapture.getMetadonnees().add("Denomination",
            "Test 1112-Droits-Conformite-All-ATT-AEPL");
      formCapture.getMetadonnees().modifieValeurMeta("CodeRND", "2.3.1.1.13");
      formCapture.getMetadonnees().add("NumeroRecours",
      "11");
      
      

      // Valeurs initiales des formulaires pour la capture de masse
      // Formulaire pour l'appel au WS de capture de masse
      CaptureMasseFormulaire formCaptMasseDecl = formulaire
            .getCaptureMasseDeclenchement();
      formCaptMasseDecl.setUrlSommaire(getEcdeService().construitUrlEcde(
            "SAE_INTEGRATION/20110822/Droit-1112-Droits-Conformite-All-ATT-AEPL/sommaire.xml"));
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
      codesMeta.add("CodeRND");
      codesMeta.add("DateCreation");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("Siren");
      
      
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

      if ("Capture unitaire".equals(etape)) {

         captureUnitaire(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureUnitaire(), formulaire.getViFormulaire());

      } else if ("Capture Masse #1".equals(etape)) {

         captureMasseEtape1AppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("Capture Masse #2".equals(etape)) {

         captureMasseEtape2LectureResultats(formulaire
               .getCaptureMasseResultat());

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

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement(), formulaire.getViFormulaire());
      // Renseigne le formulaire de l'étape 2
      
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());
      

   }

   private void captureMasseEtape2LectureResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService().testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void recherche(String urlWebService, RechercheFormulaire rechForm,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = rechForm.getResultats();
      
      RechercheResponse response =  getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(urlWebService,
            rechForm, WAITED_COUNT, false, TypeComparaison.NumeroRecours,viParams );
      
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
         verifieResultatN(2, resultatsTries.get(1), resultatTest, "2");
         verifieResultatN(3, resultatsTries.get(2), resultatTest, "3");
         verifieResultatN(4, resultatsTries.get(3), resultatTest, "4");
         verifieResultatN(5, resultatsTries.get(4), resultatTest, "5");
         verifieResultatN(6, resultatsTries.get(5), resultatTest, "6");
         verifieResultatN(7, resultatsTries.get(6), resultatTest, "7");
         verifieResultatN(8, resultatsTries.get(7), resultatTest, "8");
         verifieResultatN(9, resultatsTries.get(8), resultatTest, "9");
         verifieResultatN(10, resultatsTries.get(9), resultatTest, "10");
         verifieResultatN(11, resultatsTries.get(10), resultatTest, "11");

      }

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
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
      valeursAttendues.add("Denomination",
            "Test 1112-Droits-Conformite-All-ATT-AEPL");
      valeursAttendues.add("NumeroRecours", numeroRecours);
      
      if(ArrayUtils.contains(new String[]{"1","5","9","7","11"},numeroRecours)){
         valeursAttendues.add("CodeRND", "2.3.1.1.13");
      }
      if(ArrayUtils.contains(new String[]{"4","6","2","8","10"},numeroRecours)){
         valeursAttendues.add("CodeRND", "1.2.1.C.X");
      }
      
      if (ArrayUtils.contains(new String[]{"11"},numeroRecours)) {
         valeursAttendues.add("DateCreation", "2011-09-01");
         valeursAttendues.add("Siren", "");
      } else {
         valeursAttendues.add("DateCreation", "2007-04-01");
         valeursAttendues.add("Siren", "3090000001");
      }

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);

   }

}
