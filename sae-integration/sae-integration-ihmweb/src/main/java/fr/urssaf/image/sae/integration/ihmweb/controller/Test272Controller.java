package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

/**
 * Test 272<br>
 * <br>
 * La Capture de masse échoue car l’URL ECDE en lecture seul
 */
@Controller
@RequestMapping(value = "test272")
public class Test272Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "272";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-272-CaptureMasse-KO-URL-ECDE-Repertoire-Sans-Droits-Ecriture/");
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
            .setRequeteLucene("Denomination:\"Test 272-CaptureMasse-KO-URL-ECDE-Repertoire-Sans-Droits-Ecriture\"");

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

         etape2LectureResultat(getDebutUrlEcde() + "sommaire.xml", formulaire
               .getCaptureMasseResultat());

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire.getRechFormulaire(), formulaire
               .getUrlServiceWeb());

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
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAttendue(
            urlWebService, formulaire.getCaptureMasseDeclenchement(),
            "sae_CaptureEcdeDroitEcriture",
            new String[] { getDebutUrlEcde() + "sommaire.xml" });

   }

   /**
    * @param urlServiceWeb
    * @param captureMasseResultat
    */
   private void etape2LectureResultat(String urlEcde,
         CaptureMasseResultatFormulaire captureMasseResultat) {

      getCaptureMasseTestService().testResultatsTdmReponseAucunFichierAttendu(
            captureMasseResultat, urlEcde);

   }

   private void etape3Recherche(RechercheFormulaire formulaire,
         String urlWebService) {

      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlWebService, formulaire, 0, false, null);

   }

}
