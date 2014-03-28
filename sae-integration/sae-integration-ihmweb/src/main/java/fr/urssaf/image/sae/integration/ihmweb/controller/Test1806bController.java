package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireDrCuRe;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

/**
 * 1805-2-FCP-CU-OK-Identification-Validation-Monitor-FMT354-KO
 */
@Controller
@RequestMapping(value = "test1806b")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1806bController extends
      AbstractTestWsController<TestFormulaireDrCuRe> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1806b";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testFcpCuReTrace";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireDrCuRe getFormulairePourGet() {

      TestFormulaireDrCuRe formulaire = new TestFormulaireDrCuRe();

      // capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptUnit();

      // L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/FCP-1806-2-FCP-CU-OK-Identification-Validation-Strict-FMT354-KO/documents/doc1.PDF"));

      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-23");
      metadonnees.add("DateDebutConservation", "2011-09-01");
      metadonnees.add("Denomination",
            "Test 1806-2-FCP-CU-OK-Identification-Validation-Strict-FMT354-KO");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "c3a2c0e577b1e7b734c6e1ad03a70edf7a2262cd");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // formulaire de recherche

      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("FormatFichier");
      codesMeta.add("Denomination");

      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_FORMAT_FMT_354_TTS");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_FORMAT_FMT_354_TTS_ARCH_UNIT");

      return formulaire;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireDrCuRe formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(formulaire);
         // PagmList pagmList = new PagmList();
         // pagmList.add("INT_PAGM_ATT_VIGI_RECH");
         // formulaire.getViFormulaire().setPagms(pagmList);
      } else if ("2".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
      }
   }

   private void etape1captureUnitaireAppelWs(TestFormulaireDrCuRe formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptUnit();

      // Lance le test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            formulaire.getUrlServiceWeb(), formCaptureEtp1, ViStyle.VI_OK,
            formulaire.getViFormulaire(), "sae_FormatFichierInconnu", null);

      // Si le test n'est pas en échec, alors on peut le passer a "A controler",
      // car il faut vérifier que l'on trouve dans les logs de debug des traces
      // d'identification
      ResultatTest resultatTest = formCaptureEtp1.getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = 0;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlServiceWeb, formulaire, nbResultatsAttendus,
            flagResultatsTronquesAttendu, null);

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
   }
}
