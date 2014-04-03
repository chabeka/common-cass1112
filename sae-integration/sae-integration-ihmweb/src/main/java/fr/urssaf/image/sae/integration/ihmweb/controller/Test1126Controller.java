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
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 1126-Droits-Conformite-Consultation-PRMD
 */
@Controller
@RequestMapping(value = "test1126")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1126Controller extends
      AbstractTestWsController<TestFormulaireDrCuRe> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1126";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "testDrCuRe";
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
                        "SAE_INTEGRATION/20110822/Droit-1126-Droits-Conformite-Consultation-PRMD/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));

      // Le nom du fichier
      captUnit.setNomFichier("ADELPF_710_PSNV211157BPCA1L0000.pdf");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination",
            "Test 1126-Droits-Conformite-Consultation-PRMD");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3");
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
      codesMeta.add("CodeRND");
      codesMeta.add("ContratDeService");
      codesMeta.add("Denomination");
      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("CS_ANCIEN_SYSTEME");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("ROLE_TOUS;FULL");

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
         
         ViFormulaire viForm = formulaire.getViFormulaire();
         viForm.setIssuer("INT_CS_ATT_VIGI_CODERND_231112");
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_ATT_VIGI_CODERND_231112_RECH");
         viForm.setPagms(pagmList);
      } else if ("2".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire(), formulaire.getViFormulaire());
      }
   }

   private void etape1captureUnitaireAppelWs(TestFormulaireDrCuRe formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptUnit();

      // Lance le test
      getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire,
         ViFormulaire viParams) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = 1;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus,
                  flagResultatsTronquesAttendu, null);

      // Vérifications en profondeur
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Récupère l'unique résultat
         ResultatRechercheType resultatRecherche = response
               .getRechercheResponse().getResultats().getResultat()[0];

         // Le vérifie
         verifieResultatN(1, resultatRecherche, resultatTest);

      }

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {


      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("ContratDeService", "CS_ANCIEN_SYSTEME");
      valeursAttendues.add("Denomination", "Test 1126-Droits-Conformite-Consultation-PRMD");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
     
   }

}
