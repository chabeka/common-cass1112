package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestFormulaireSuppressionCuRe;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 1601-Ged-Technique-OK-Suppression-Doc-Existant-Arret-DFCE
 */
@Controller
@RequestMapping(value = "test1601")
public class Test1601Controller extends
      AbstractTestWsController<TestFormulaireSuppressionCuRe> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1601";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestFormulaireSuppressionCuRe getFormulairePourGet() {

      TestFormulaireSuppressionCuRe formulaire = new TestFormulaireSuppressionCuRe();

      // capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Ged-Technique-1601-Ged-Technique-OK-Suppression-Doc-Arret-DFCE/documents/doc1.PDF"));

      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "1.A.X.X.X");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination",
            "Test 1601-Ged-Technique-OK-Suppression-Doc-Existant-Arret-DFCE");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees
            .add("Titre",
                  "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      metadonnees.add("TypeHash", "SHA-1");

      // formulaire de recherche
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("CodeActivite");
      codesMeta.add("CodeFonction");
      codesMeta.add("CodeRND");
      codesMeta.add("Titre");

      formRecherche.setCodeMetadonnees(codesMeta);

      // formulaire de suppression
      SuppressionFormulaire formSuppression = formulaire.getSuppression();
      formSuppression.getResultats().setStatus(TestStatusEnum.SansStatus);

      // formulaire de recherche
      RechercheFormulaire formRechercheApresSuppr = formulaire
            .getRechercheApresSuppr();
      formRechercheApresSuppr.getResultats().setStatus(
            TestStatusEnum.SansStatus);

      formRechercheApresSuppr.setCodeMetadonnees(codesMeta);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRechercheApresSuppr.setRequeteLucene(getCasTest().getLuceneExemple());

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestFormulaireSuppressionCuRe formulaire) {
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(formulaire);
      } else if ("2".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire.getRecherche(),
               formulaire.getViFormulaire());
      } else if ("3".equals(etape)) {
         suppression(formulaire.getUrlServiceWeb(), formulaire.getSuppression());
      } else if ("4".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheApresSuppr(), formulaire.getViFormulaire());
      }
   }

   private void etape1captureUnitaireAppelWs(
         TestFormulaireSuppressionCuRe formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat resultat = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1,
                  formulaire.getViFormulaire());

      // Si le test n'est pas en échec, alors on peut initialiser l'id du
      // document à supprimer
      if (!TestStatusEnum.Echec.equals(formulaire.getCaptureUnitaire()
            .getResultats().getStatus())) {
         formulaire.getSuppression().setIdDocument(
               UUID.fromString(resultat.getIdArchivage()));
      }
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

      valeursAttendues.add("CodeActivite", "");
      valeursAttendues.add("CodeFonction", "1");
      valeursAttendues.add("CodeRND", "1.A.X.X.X");
      valeursAttendues
            .add("Titre",
                  "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
   }

   private void suppression(String urlWebService,
         SuppressionFormulaire formulaire) {

      // Appel de la méthode de test
      getSuppressionTestService()
            .appelWsOpSuppressionSoapFault(
                  urlWebService,
                  formulaire,
                  null,
                  "sae_ErreurInterneSuppression",
                  new Object[] { "fr.urssaf.image.sae.storage.exception.ConnectionServiceEx: java.net.ConnectException: Connection refused: connect" });
   }

}
