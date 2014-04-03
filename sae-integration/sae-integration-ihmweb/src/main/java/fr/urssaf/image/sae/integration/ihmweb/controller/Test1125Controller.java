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
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCmRe;
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

/**
 * 1125-Droits-Conformite-Recherche-PRMD
 */
@Controller
@RequestMapping(value = "test1125")
public class Test1125Controller extends
      AbstractTestWsController<TestFormulaireDrCmRe> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1125";
   }
   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCmRe";
   }
   private static final int WAITED_COUNT = 4;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireDrCmRe getFormulairePourGet() {

      // Création du formulaire
      TestFormulaireDrCmRe formulaire = new TestFormulaireDrCmRe();

      // Valeurs initiales du formulaire pour les paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      viForm.setIssuer("CS_ANCIEN_SYSTEME");
      PagmList pagmList = new PagmList();
      pagmList.add("ROLE_TOUS;FULL");
      viForm.setPagms(pagmList);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);

      // Valeurs initiales des formulaires pour la capture de masse
      // Formulaire pour l'appel au WS de capture de masse
      CaptureMasseFormulaire formCaptMasseDecl = formulaire
            .getCaptureMasseDeclenchement();
      formCaptMasseDecl
            .setUrlSommaire(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1125-Droits-Conformite-Recherche-PRMD/sommaire.xml"));
      formCaptMasseDecl.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Formulaire de lecture des fichiers flag et du resultats.xml
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.setUrlSommaire(formCaptMasseDecl.getUrlSommaire());
      formCaptMassRes.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Valeurs initiales du formulaire pour la recherche
      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
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

      // Fin
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCmRe formulaire) {

      String etape = formulaire.getEtape();

      if ("1".equals(etape)) {

         captureMasseEtape1AppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         captureMasseEtape2LectureResultats(formulaire
               .getCaptureMasseResultat());
         
         ViFormulaire viForm = formulaire.getViFormulaire();
         viForm.setIssuer("INT_CS_ATT_VIGI_CODERND_231112");
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_ATT_VIGI_CODERND_231112_RECH");
         viForm.setPagms(pagmList);

      } else if ("3".equals(etape)) {

         recherche(formulaire.getUrlServiceWeb(), formulaire.getRechercheFormulaire(),
               formulaire.getViFormulaire());

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void captureMasseEtape1AppelWs(String urlWebService,
         TestFormulaireDrCmRe formulaire) {

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

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void recherche(String urlWebService, RechercheFormulaire rechForm,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = rechForm.getResultats();

      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlWebService, rechForm,
                  WAITED_COUNT, false, TypeComparaison.NumeroRecours, viParams);

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
         verifieResultatN(2, resultatsTries.get(1), resultatTest, "4");
         verifieResultatN(3, resultatsTries.get(2), resultatTest, "7");
         verifieResultatN(4, resultatsTries.get(3), resultatTest, "10");
      }

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         String numeroRecours) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateCreation", "2007-04-01");
      valeursAttendues.add("Denomination",
            "Test 1125-Droits-Conformite-Recherche-PRMD");
      valeursAttendues.add("NumeroRecours", numeroRecours);
      valeursAttendues.add("Siren", "3090000001");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);

   }

}
