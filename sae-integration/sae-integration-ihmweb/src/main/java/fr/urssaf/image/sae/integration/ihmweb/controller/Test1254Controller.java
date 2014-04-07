package fr.urssaf.image.sae.integration.ihmweb.controller;

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
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;

/**
 * 1254-PKI-Verification-KO-Nom-Certificat-VI-OK-Flag-KO
 */
@Controller
@RequestMapping(value = "test1254")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1254Controller extends
      AbstractTestWsController<TestFormulaireDrCmRe> {

   private static final int WAITED_COUNT = 0;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1254";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCmRe";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/PKI-1254-PKI-Verification-KO-Nom-Certificat-VI-OK-Flag-KO/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireDrCmRe getFormulairePourGet() {

      TestFormulaireDrCmRe formulaire = new TestFormulaireDrCmRe();
      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("CodeRND");
      codesMeta.add("DateCreation");
      codesMeta.add("Denomination");
      codesMeta.add("NumeroRecours");
      codesMeta.add("Siren");
      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_ATT_VIGI_PKI_FALSE_TEST5");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_ATT_VIGI_PKI_FALSE_TEST5_ARCH_MASSE");
      viForm
            .setIdCertif(SaeIntegrationConstantes.PKI_IGC_AED_PNR_APPLI_TEST);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCmRe formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseDeclenchement()
               .getUrlSommaire(), formulaire.getCaptureMasseResultat());
         formulaire.getViFormulaire().setIssuer("INT_CS_ATT_VIGI_PKI_FALSE_TEST4");
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_ATT_VIGI_PKI_FALSE_TEST4_ALL");
         formulaire.getViFormulaire().setPagms(pagmList);
         formulaire.getViFormulaire()
            .setIdCertif(SaeIntegrationConstantes.PKI_IGC_CELL_INTEG_APPLI_TEST_1);

      } else if ("3".equals(etape)) {

         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureMasseAppelWs(String urlWebService,
         TestFormulaireDrCmRe formulaire) {
      
      String[] arguments = { "Le certificat utilisé pour signer le VI n'est pas issu de l'IGC définie dans le Contrat de Service" };

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAttendue(
            urlWebService, formulaire.getCaptureMasseDeclenchement(),
            formulaire.getViFormulaire(), "wsse_FailedCheck_Message", arguments);

   }

   private void etape2captureMasseResultats(String urlEcde,
         CaptureMasseResultatFormulaire captureMasseResultat) {

      getCaptureMasseTestService().testResultatsTdmReponseAucunFichierAttendu(
            captureMasseResultat, urlEcde);
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = WAITED_COUNT;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlServiceWeb, formulaire, nbResultatsAttendus,
            flagResultatsTronquesAttendu, TypeComparaison.NumeroRecours,
            viParams);

      // On passe le test à OK si tous les contrôles sont passées
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
