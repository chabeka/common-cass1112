package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test212Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 212-CaptureMasse-Pile-OK-ECDE-local
 */
@Controller
@RequestMapping(value = "test212")
public class Test212Controller extends AbstractTestWsController<Test212Formulaire>{


   private static int COUNT_WAITED;
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "212";
   }
   
   
   private String getDebutUrlEcde(int index) {
      if(index==0){
         return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/", index);
      }else if(index==1){
         return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-2/", index);
      }else{
         return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local/", index);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test212Formulaire getFormulairePourGet() {

      Test212Formulaire formulaire = new Test212Formulaire();
      // Initialise le formulaire de capture de masse
      

      // comme on appel deux service de capture de mass on renseigne l'url avec les valeurs possibles du ficher ecdesources.xml
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde(0) + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      // initialisation de l'url non local pour l'appel de la capture de masse
      CaptureMasseFormulaire formCaptureNonLocal = formulaire
      .getCaptureMasseDeclenchementNonLocal();
      formCaptureNonLocal.setUrlSommaire(getDebutUrlEcde(1) + "sommaire.xml");
      formCaptureNonLocal.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      
      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde(0) + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // initialisation de l'url non local pour l'appel de la recherche
      CaptureMasseResultatFormulaire formResultatNonLocal = formulaire
      .getCaptureMasseResultatNonLocal();
      formResultatNonLocal.setUrlSommaire(getDebutUrlEcde(1) + "resultat.xml");
      formResultatNonLocal.getResultats().setStatus(TestStatusEnum.SansStatus);      
      
      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      
      // Requête LUCENE
      rechFormulaire
            .setRequeteLucene(getCasTest().getLuceneExemple());

      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaireNonLocal = formulaire.getRechFormulaireNonLocal();
      
      // le fichier listeCasTest.xml n'acceptant qu'une chaîne Lucene on spécifie le chaîne de recherche non local manuellement
      rechFormulaireNonLocal
            .setRequeteLucene("Denomination:\"Test 212-CaptureMasse-Pile-OK-ECDE-local-2\"");
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test212Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);
         

      } else if ("3".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());

      } else if ("4".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultatNonLocal());
                  

      } else if ("5".equals(etape)) {

         etape3Recherche(formulaire);

      } else if ("6".equals(etape)) {

         etape3Recherche(formulaire);

      } else if ("7".equals(etape)) {
         COUNT_WAITED = 10;
         etape5Comptages(formulaire);

      } else if ("8".equals(etape)) {
         COUNT_WAITED = 0;
         etape5Comptages(formulaire);

      }else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         Test212Formulaire formulaire) {
      CaptureMasseResultatFormulaire formCaptMassRes = null;
      CaptureMasseFormulaire captMassForm = null;
      String urlSommaire = null;
      
      // en fonction de l'étape 1 ou 2 on ajuste les paramètres local/non local
      // et on vide le résultat du test
      if (formulaire.getEtape().equals("1")) {
         formCaptMassRes = formulaire.getCaptureMasseResultat();
         urlSommaire = formulaire.getCaptureMasseDeclenchement()
               .getUrlSommaire();
         captMassForm = formulaire.getCaptureMasseDeclenchement();
      } else if (formulaire.getEtape().equals("2")) {
         formCaptMassRes = formulaire.getCaptureMasseResultatNonLocal();
         urlSommaire = formulaire.getCaptureMasseDeclenchementNonLocal()
               .getUrlSommaire();
         captMassForm = formulaire.getCaptureMasseDeclenchementNonLocal();
      }

      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(urlSommaire);

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            captMassForm);

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape3Recherche(Test212Formulaire formulaire) {
      RechercheFormulaire rechForm = null;
      if (formulaire.getEtape().equals("5")) {
         rechForm = formulaire.getRechFormulaire();
      } else if (formulaire.getEtape().equals("6")) {
         rechForm = formulaire.getRechFormulaireNonLocal();
      }
      // Appel le service de test de la recherche
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), rechForm, COUNT_WAITED, false,
                  null);

      ResultatTest resultatTest = rechForm.getResultats();

      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         // Récupère l'unique résultat
         ResultatRechercheType resultatRecherche = response
               .getRechercheResponse().getResultats().getResultat()[0];

         // Le vérifie
         verifieResultatRecherche(resultatRecherche, resultatTest, formulaire
               .getEtape());

         // Si le test n'est pas en échec, alors on peut le passer en succès,
         // car tout a pu être vérifié
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }
         if (formulaire.getEtape().equals("5")) {
            // Initialise le formulaire de consultation
            formulaire.getConsultFormulaire().setIdArchivage(
                  resultatRecherche.getIdArchive().toString());
         } else if (formulaire.getEtape().equals("6")) {
            // Initialise le formulaire de consultation
            formulaire.getConsultFormulaireNonLocal().setIdArchivage(
                  resultatRecherche.getIdArchive().toString());
         }

      }

   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         String etape) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      if ("5".equals(etape)) {
         valeursAttendues.add("CodeRND", "2.3.1.1.12");
         valeursAttendues.add("DateCreation", "2011-09-05");
         valeursAttendues.add("NumeroRecours", "1");
      }
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);
   }
     
   private void etape5Comptages(Test212Formulaire formulaire) {

      ResultatTest resultatTest = null;
      String idTdm = null;
      
      if (formulaire.getEtape().equals("7")) {
         // Récupération de l'objet ResultatTest
         resultatTest = formulaire.getComptagesFormulaire().getResultats();
      // Lecture de l'identifiant du traitement de masse
         idTdm = formulaire.getComptagesFormulaire().getIdTdm();
      } else if (formulaire.getEtape().equals("8")) {
         // Récupération de l'objet ResultatTest
         resultatTest = formulaire.getComptagesFormulaireNonLocal().getResultats();
      // Lecture de l'identifiant du traitement de masse
         idTdm = formulaire.getComptagesFormulaireNonLocal().getIdTdm();
      }

      resultatTest.clear();

      

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(COUNT_WAITED));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

  
}
