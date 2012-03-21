package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test103Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * 103-CaptureUnitaire-OK-ToutesMetasSpecifiables
 */
@Controller
@RequestMapping(value = "test103")
public class Test103Controller extends
      AbstractTestWsController<Test103Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "103";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test103Formulaire getFormulairePourGet() {

      Test103Formulaire formulaire = new Test103Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureUnitaire-103-CaptureUnitaire-OK-ToutesMetasSpecifiables/documents/doc1.PDF"));
      formCapture.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("ApplicationTraitement", "ATTESTATIONS");
      metadonnees.add("CodeCategorieV2", "4");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("CodeSousCategorieV2", "11");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("DateDebutConservation", "2011-09-02");
      metadonnees.add("DateReception", "2011-09-01");
      metadonnees.add("Denomination",
            "Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("IdTraitementMasse", "123654");
      metadonnees.add("NbPages", "2");
      metadonnees.add("NniEmployeur", "148032541101648");
      metadonnees.add("NumeroCompteExterne", "30148032541101600");
      metadonnees.add("NumeroCompteInterne", "719900");
      metadonnees.add("NumeroIntControle", "57377");
      metadonnees.add("NumeroPersonne", "123854");
      metadonnees.add("NumeroRecours", "20080798");
      metadonnees.add("NumeroStructure", "000050221");
      metadonnees.add("Periode", "PERI");
      metadonnees.add("PseudoSiret", "4914736610005");
      metadonnees.add("Siren", "0123456789");
      metadonnees.add("Siret", "12345678912345");
      metadonnees.add("SiteAcquisition", "CER44");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TracabilitrePreArchivage", "Traçabilité pré archivage");
      metadonnees.add("TypeHash", "SHA-1");
      metadonnees.add("VersionRND", "5.3");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de recherche
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRecherche = formulaire.getRecherche();

      // Requête LUCENE
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Métadonnées souhaitées
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("ApplicationTraitement");
      codesMeta.add("CodeCategorieV2");
      codesMeta.add("CodeOrganismeGestionnaire");
      codesMeta.add("CodeOrganismeProprietaire");
      codesMeta.add("CodeRND");
      codesMeta.add("CodeSousCategorieV2");
      codesMeta.add("DateCreation");
      codesMeta.add("DateDebutConservation");
      codesMeta.add("DateReception");
      codesMeta.add("Denomination");
      codesMeta.add("FormatFichier");
      codesMeta.add("Hash");
      codesMeta.add("IdTraitementMasse");
      codesMeta.add("NbPages");
      codesMeta.add("NniEmployeur");
      codesMeta.add("NomFichier");
      codesMeta.add("NumeroCompteExterne");
      codesMeta.add("NumeroCompteInterne");
      codesMeta.add("NumeroIntControle");
      codesMeta.add("NumeroPersonne");
      codesMeta.add("NumeroRecours");
      codesMeta.add("NumeroStructure");
      codesMeta.add("Periode");
      codesMeta.add("PseudoSiret");
      codesMeta.add("Siren");
      codesMeta.add("Siret");
      codesMeta.add("SiteAcquisition");
      codesMeta.add("TailleFichier");
      codesMeta.add("Titre");
      codesMeta.add("TracabilitrePreArchivage");
      codesMeta.add("TypeHash");
      codesMeta.add("VersionRND");

      // Renvoie le formulaire
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test103Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2recherche(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test103Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().clear();

      // Lance le test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
            formulaire.getUrlServiceWeb(), formCaptureEtp1);

   }

   private void etape2recherche(Test103Formulaire formulaireTest103) {

      // Initialise
      RechercheFormulaire formulaire = formulaireTest103.getRecherche();
      ResultatTest resultatTest = formulaire.getResultats();
      String urlServiceWeb = formulaireTest103.getUrlServiceWeb();

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
         verifieResultatRecherche(resultatRecherche, resultatTest);

      }

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("ApplicationTraitement", "ATTESTATIONS");
      valeursAttendues.add("CodeCategorieV2", "4");
      valeursAttendues.add("CodeOrganismeGestionnaire", "CER69");
      valeursAttendues.add("CodeOrganismeProprietaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("CodeSousCategorieV2", "11");
      valeursAttendues.add("DateCreation", "2011-09-05");
      valeursAttendues.add("DateDebutConservation", "2011-09-02");
      valeursAttendues.add("DateReception", "2011-09-01");
      valeursAttendues.add("Denomination",
            "Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("IdTraitementMasse", "123654");
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("NniEmployeur", "148032541101648");
      valeursAttendues.add("NomFichier", "doc1.PDF");
      valeursAttendues.add("NumeroCompteExterne", "30148032541101600");
      valeursAttendues.add("NumeroCompteInterne", "719900");
      valeursAttendues.add("NumeroIntControle", "57377");
      valeursAttendues.add("NumeroPersonne", "123854");
      valeursAttendues.add("NumeroRecours", "20080798");
      valeursAttendues.add("NumeroStructure", "000050221");
      valeursAttendues.add("Periode", "PERI");
      valeursAttendues.add("PseudoSiret", "4914736610005");
      valeursAttendues.add("Siren", "0123456789");
      valeursAttendues.add("Siret", "12345678912345");
      valeursAttendues.add("SiteAcquisition", "CER44");
      valeursAttendues.add("TailleFichier", "56587");
      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("TracabilitrePreArchivage",
            "Traçabilité pré archivage");
      valeursAttendues.add("TypeHash", "SHA-1");
      valeursAttendues.add("VersionRND", "5.3");

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

}
