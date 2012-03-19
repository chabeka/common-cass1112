package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 207-CaptureMasse-OK-IdTraitementMasse
 */
@Controller
@RequestMapping(value = "test207")
public class Test207Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "207";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-207-CaptureMasse-OK-IdTraitementMasse/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestStockageMasseAllFormulaire getFormulairePourGet() {

      TestStockageMasseAllFormulaire formulaire = new TestStockageMasseAllFormulaire();

      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      rechFormulaire
            .setRequeteLucene(getCasTest().getLuceneExemple());
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();

      String[] tabElement = new String[] { "CodeRND", "DateArchivage",
            "Denomination", "IdTraitementMasse" };

      codeMetadonneeList.addAll(Arrays.asList(tabElement));

      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestStockageMasseAllFormulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire);

      } else if ("4".equals(etape)) {

         etape4Consultation(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestStockageMasseAllFormulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService().testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape3Recherche(TestStockageMasseAllFormulaire formulaire) {
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechFormulaire(), 1, false, null);

      if (!TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
            .getResultats().getStatus())) {

         ResultatRechercheType results[] = response.getRechercheResponse()
               .getResultats().getResultat();

         int i = 0;
         while (i < results.length
               && !TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
                     .getResultats().getStatus())) {

            testMetaDonnees(formulaire.getRechFormulaire().getResultats(),
                  results[i], i + 1);

            i++;
         }

         if (!TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
               .getResultats().getStatus())) {

            formulaire.getConsultFormulaire().setIdArchivage(
                  results[0].getIdArchive().getUuidType());

            formulaire.getRechFormulaire().getResultats().setStatus(
                  TestStatusEnum.AControler);
         }

      }

   }

   private void testMetaDonnees(ResultatTest resultatTest,
         ResultatRechercheType resultatRecherche, int index) {
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      String numeroResultatRecherche = "1";
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("Denomination",
            "Test 207-CaptureMasse-OK-IdTraitementMasse");
      valeursAttendues.add("IdTraitementMasse", "12345678901234567890");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }

   private void etape4Consultation(TestStockageMasseAllFormulaire formulaire) {
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getConsultFormulaire(), null);
   }
}
