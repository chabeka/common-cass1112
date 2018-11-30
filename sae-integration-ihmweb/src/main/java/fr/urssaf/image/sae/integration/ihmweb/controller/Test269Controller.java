package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test269Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 269-CaptureMasse-KO-Deux-Lancements<br>
 * <br>
 * OBSOLETE depuis le lot 120511
 */
@Controller
@RequestMapping(value = "test269")
public class Test269Controller extends
      AbstractTestWsController<Test269Formulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT = 200;


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "269";
   }
   
   
   private String getDebutUrlEcde1() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-269-CaptureMasse-KO-Deux-Lancements-1/");
   }
   
   
   private String getDebutUrlEcde2() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-269-CaptureMasse-KO-Deux-Lancements-2/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test269Formulaire getFormulairePourGet() {

      Test269Formulaire formulaire = new Test269Formulaire();

      // Initialise le formulaire de capture de masse
      
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde1() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseFormulaire formCapture2 = formulaire
            .getCaptureMasseDeclenchementParallele();
      formCapture2.setUrlSommaire(getDebutUrlEcde2() + "sommaire.xml");
      formCapture2.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde1() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      
      // Initialise les formulaires de recherche
      
      RechercheFormulaire rechercheFormulaire = formulaire.getRechFormulaire();
      rechercheFormulaire
            .setRequeteLucene("Denomination:\"Test 269-CaptureMasse-KO-Deux-Lancements-1\"");
      CodeMetadonneeList codeMetadonneeList1 = new CodeMetadonneeList();
      rechercheFormulaire.setCodeMetadonnees(codeMetadonneeList1);
      codeMetadonneeList1.add("Denomination");

      RechercheFormulaire rechercheFormulaireParallele = formulaire
            .getRechFormulaireParallele();
      rechercheFormulaireParallele
            .setRequeteLucene("Denomination:\"Test 269-CaptureMasse-KO-Deux-Lancements-2\"");
      CodeMetadonneeList codeMetadonneeList2 = new CodeMetadonneeList();
      rechercheFormulaireParallele.setCodeMetadonnees(codeMetadonneeList2);
      codeMetadonneeList2.add("Denomination");

      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test269Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureMasseResultat(), formulaire
               .getCaptureMasseDeclenchement());

      } else if ("2".equals(etape)) {

         etape2captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureMasseResultat(), formulaire
               .getCaptureMasseDeclenchementParallele());

      } else if ("3".equals(etape)) {

         etape3captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("4".equals(etape)) {

         etape4Recherche(formulaire);

      } else if ("5".equals(etape)) {

         etape5Recherche(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         CaptureMasseResultatFormulaire formCaptMassRes,
         CaptureMasseFormulaire captureMasseFormulaire) {

      // Vide le résultat du test précédent de l'étape 2
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(captureMasseFormulaire.getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            captureMasseFormulaire);

   }

   private void etape2captureMasseAppelWs(String urlWebService,
         CaptureMasseResultatFormulaire formCaptMassRes,
         CaptureMasseFormulaire captureMasseFormulaire) {

      // Vide le résultat du test précédent de l'étape 2
      formCaptMassRes.getResultats().clear();
      // formCaptMassRes.setUrlSommaire(captureMasseFormulaire.getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAttendue(
            urlWebService, captureMasseFormulaire, "sae_CaptureMasseRefusee", null);

   }

   private void etape3captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape4Recherche(Test269Formulaire formulaire) {

      RechercheResponse response = getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(), formulaire.getRechFormulaire(),
            WAITED_COUNT,
            true, null);
      
      if (!TestStatusEnum.Echec.equals(formulaire.getRechFormulaire()
            .getResultats().getStatus())) {

         ResultatRechercheType results[] = response.getRechercheResponse()
               .getResultats().getResultat();

         ResultatTest resultatTest = formulaire.getRechFormulaire().getResultats();

         for (int i=0;i<WAITED_COUNT;i++) {
            verifieResultat(results[i],resultatTest,i);
         }
         
         
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

            resultatTest.setStatus(TestStatusEnum.Succes);
            
         }
         
      }
   }
   
   
   private void verifieResultat(
         ResultatRechercheType resultatRecherche,
         ResultatTest resultatTest,
         int index) {
      
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      String numeroResultatRecherche = Integer.toString(index+1);

      valeursAttendues.add("Denomination", "Test 269-CaptureMasse-KO-Deux-Lancements-1");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
      
   }

   private void etape5Recherche(Test269Formulaire formulaire) {

      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(),
            formulaire.getRechFormulaireParallele(), 0, false, null);

   }

}
