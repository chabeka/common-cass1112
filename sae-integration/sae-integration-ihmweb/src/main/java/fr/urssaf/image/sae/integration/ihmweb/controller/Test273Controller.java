package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestStockageMasseAllFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.NonIntegratedDocumentType;

/**
 * Test 273<br>
 * <br>
 * La Capture de masse est interrompue et pas de redémarrage de dfce après la
 * pause
 */
@Controller
@RequestMapping(value = "test273")
public class Test273Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   /**
    * 
    */
   private static final int WAITED_COUNT = 5000;


   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "273";
   }
   
   
   private String getDebutUrlEcde() {
      return getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureMasse-273-CaptureMasse-KO-JarArretTomCatSansRepriseAvecStockage/");
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
            .setRequeteLucene("Denomination:\"Test 273-CaptureMasse-KO-JarArretTomCatSansRepriseAvecStockage\"");

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
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement());

   }

   /**
    * @param urlServiceWeb
    * @param captureMasseResultat
    */
   private void etape2LectureResultat(String urlEcde,
         CaptureMasseResultatFormulaire captureMasseResultat) {

      ErreurType error = new ErreurType();
      error.setCode("SAE-CA-BUL003");
      error.setLibelle("La capture de masse en mode 'Tout ou rien' "
            + "a été interrompue. " + "Une procédure d'exploitation "
            + "a été initialisée pour supprimer les données qui "
            + "auraient pu être stockées.");

      ListeErreurType errorList = new ListeErreurType();
      errorList.getErreur().add(error);

      NonIntegratedDocumentType documentType = new NonIntegratedDocumentType();
      documentType.setErreurs(errorList);

      getCaptureMasseTestService().testResultatsTdmReponseKOAttendue(
            captureMasseResultat, WAITED_COUNT, documentType);

   }

   private void etape3Recherche(RechercheFormulaire formulaire,
         String urlWebService) {

      getRechercheTestService().appelWsOpRechercheTestLibre(urlWebService,
            formulaire);
      formulaire.getResultats().setStatus(TestStatusEnum.AControler);

   }

}
