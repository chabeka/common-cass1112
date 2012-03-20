package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test101Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ConsultationResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectExtractor;

/**
 * 101-CaptureUnitaire-OK-Standard
 */
@Controller
@RequestMapping(value = "test101")
public class Test101Controller extends
      AbstractTestWsController<Test101Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "101";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test101Formulaire getFormulairePourGet() {

      Test101Formulaire formulaire = new Test101Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-101-CaptureUnitaire-OK-Standard/documents/doc1.PDF"));

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination", "Test 101-CaptureUnitaire-OK-Standard");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de recherche
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRecherche = formulaire.getRecherche();

      // Requête LUCENE
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Métadonnées souhaitées
      // => La liste des métadonnées consultables
      CodeMetadonneeList codesMeta = getRefMetas()
            .listeMetadonneesConsultables();
      formRecherche.setCodeMetadonnees(codesMeta);

      // Renvoie le formulaire
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test101Formulaire formulaire) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2recherche(formulaire);

      } else if ("3".equals(etape)) {

         etape3consultation(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test101Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().clear();

      // Vide le résultat du test précédent de l'étape 3
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().clear();
      formConsult.setIdArchivage(null);

      // Vide le dernier id d'archivage et le dernier sha1
      formulaire.setDernierIdArchivage(null);
      formulaire.setDernierSha1(null);

      // Lance le test
      CaptureUnitaireResultat consultResult = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1);

      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(
            TestStatusEnum.Succes)) {

         // On mémorise l'identifiant d'archivage et le sha-1
         formulaire.setDernierIdArchivage(consultResult.getIdArchivage());
         formulaire.setDernierSha1(consultResult.getSha1());

         // On affecte l'identifiant d'archivage à l'étape 2 (consultation)
         formConsult.setIdArchivage(consultResult.getIdArchivage());

      }

   }

   private void etape2recherche(Test101Formulaire formulaireTest101) {

      // Initialise
      RechercheFormulaire formulaire = formulaireTest101.getRecherche();
      ResultatTest resultatTest = formulaire.getResultats();
      String urlServiceWeb = formulaireTest101.getUrlServiceWeb();

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

      // Au mieux, le test est "à contrôler" (certaines métadonnées doivent être
      // vérifiées
      // "à la main")
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("Periode", StringUtils.EMPTY);
      valeursAttendues.add("Siren", StringUtils.EMPTY);
      valeursAttendues.add("NniEmployeur", StringUtils.EMPTY);
      valeursAttendues.add("NumeroPersonne", StringUtils.EMPTY);
      valeursAttendues.add("Denomination",
            "Test 101-CaptureUnitaire-OK-Standard");
      valeursAttendues.add("CodeCategorieV2", StringUtils.EMPTY);
      valeursAttendues.add("CodeSousCategorieV2", StringUtils.EMPTY);
      valeursAttendues.add("NumeroCompteInterne", StringUtils.EMPTY);
      valeursAttendues.add("NumeroCompteExterne", StringUtils.EMPTY);
      valeursAttendues.add("Siret", StringUtils.EMPTY);
      valeursAttendues.add("PseudoSiret", StringUtils.EMPTY);
      valeursAttendues.add("NumeroStructure", StringUtils.EMPTY);
      valeursAttendues.add("NumeroRecours", StringUtils.EMPTY);
      valeursAttendues.add("NumeroIntControle", StringUtils.EMPTY);
      valeursAttendues.add("DateCreation", "2011-09-01");
      valeursAttendues.add("DateReception", StringUtils.EMPTY);
      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("ApplicationTraitement", StringUtils.EMPTY);
      valeursAttendues.add("CodeOrganismeProprietaire", "AC750");
      valeursAttendues.add("CodeOrganismeGestionnaire", "CER69");
      valeursAttendues.add("SiteAcquisition", StringUtils.EMPTY);
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("VersionRND", getTestConfig().getVersionRND());
      valeursAttendues.add("CodeFonction", "2");
      valeursAttendues.add("CodeActivite", "3");
      valeursAttendues.add("DureeConservation", "1825");
      // valeursAttendues.add("DateDebutConservation",); // <= à vérifier
      // "à la main"
      // valeursAttendues.add("DateFinConservation",); // <= à vérifier
      // "à la main"
      valeursAttendues.add("Gel", "false");
      valeursAttendues.add("TracabilitrePreArchivage", StringUtils.EMPTY);
      valeursAttendues.add("TracabilitePostArchivage", StringUtils.EMPTY);
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("TypeHash", "SHA-1");
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("NomFichier", "doc1.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "56587");
      valeursAttendues.add("IdTraitementMasse", StringUtils.EMPTY);
      valeursAttendues.add("ContratDeService", "ATT_PROD_001");
      // valeursAttendues.add("DateArchivage",); // <= à vérifier "à la main"

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void etape3consultation(Test101Formulaire formulaire) {

      // Initialise
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      ResultatTest resultatTestConsult = formConsult.getResultats();

      // Le SHA-1 attendu
      String sha1attendu = null;
      String idArchivageDemande = formConsult.getIdArchivage(); // NOPMD
      String dernierIdArchivageCapture = formulaire.getDernierIdArchivage(); // NOPMD
      String dernierSha1capture = formulaire.getDernierSha1(); // NOPMD

      if ((idArchivageDemande.equals(dernierIdArchivageCapture))
            && (StringUtils.isNotBlank(dernierSha1capture))) {
         sha1attendu = formulaire.getDernierSha1();
      }

      // Lance le test
      ConsultationResultat response = getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formConsult, sha1attendu);

      // Vérifie les métadonnées attendues
      if (response != null) { // <= response est null si on a obtenu une
         // SoapFault

         MetadonneeValeurList metas = SaeServiceObjectExtractor
               .extraitMetadonnees(response.getMetadonnees());
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "Titre",
               "Attestation de vigilance");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "DateCreation", "2011-09-01");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "DateReception",
               StringUtils.EMPTY);
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "CodeOrganismeProprietaire",
               "AC750");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "CodeOrganismeGestionnaire",
               "CER69");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "CodeRND", "2.3.1.1.12");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "Hash",
               "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "NomFichier", "doc1.PDF");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "FormatFichier", "fmt/354");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "TailleFichier", "56587");
         getTestsMetasService().verifiePresenceEtValeurAvecLog(
               formConsult.getResultats(), metas, "ContratDeService",
               "ATT_PROD_001");

         // DateArchivage à vérifier à la main
         // getTestsMetasService().verifiePresenceEtValeurAvecLog(
         // formConsult.getResultats(),
         // metas,
         // "DateArchivage",
         // );

      }

      // Au mieux, le test est "à contrôler" (certaines métadonnées doivent être
      // vérifiées
      // "à la main")
      if (!TestStatusEnum.Echec.equals(resultatTestConsult.getStatus())) {
         resultatTestConsult.setStatus(TestStatusEnum.AControler);
      }

   }

}
