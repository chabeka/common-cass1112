package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
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

      // Initialise le formulaire de capture de masse
      
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      
      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      rechFormulaire
            .setRequeteLucene(getCasTest().getLuceneExemple());
      
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);
      codeMetadonneeList.add("CodeRND");
      codeMetadonneeList.add("DateArchivage");
      codeMetadonneeList.add("Denomination");
      codeMetadonneeList.add("IdTraitementMasse");
      

      // Initialise le formulaire de consultation
      
      ConsultationFormulaire formConsult = formulaire.getConsultFormulaire();
      
      CodeMetadonneeList codeMetaConsult = new CodeMetadonneeList();
      formConsult.setCodeMetadonnees(codeMetaConsult);
      codeMetaConsult.add("Denomination");
      codeMetaConsult.add("Hash");
      

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
      
      // Les codes des métadonnées attendues
      CodeMetadonneeList codeMetaAttendues = new CodeMetadonneeList();
      codeMetaAttendues.add("Denomination");
      codeMetaAttendues.add("Hash");
      
      // Valeurs des métadonnées attendues
      List<MetadonneeValeur> valeursMetaAttendus = new ArrayList<MetadonneeValeur>();
      valeursMetaAttendus.add(new MetadonneeValeur("Denomination","Test 207-CaptureMasse-OK-IdTraitementMasse"));
      valeursMetaAttendus.add(new MetadonneeValeur("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      
      // Appel du service de vérification
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getConsultFormulaire(),
                  "a2f93f1f121ebba0faef2c0596f2f126eacae77b",
                  codeMetaAttendues,
                  valeursMetaAttendus);
      
      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      ResultatTest resultatTest = formulaire.getConsultFormulaire().getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
      
   }
}
