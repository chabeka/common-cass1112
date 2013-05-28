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
 * 206-CaptureMasse-OK-Toutes-metadonnees-specifiables
 */
@Controller
@RequestMapping(value = "test206")
public class Test206Controller extends
      AbstractTestWsController<TestStockageMasseAllFormulaire> {

   private static final int COUNT_WAITED = 1;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "206";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/CaptureMasse-206-CaptureMasse-OK-Toutes-metadonnees-specifiables/");
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

      // Requête LUCENE
      rechFormulaire.setRequeteLucene(getCasTest().getLuceneExemple());

      // Métadonnées souhaitées
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      rechFormulaire.setCodeMetadonnees(codesMeta);
      codesMeta.add("ApplicationProductrice");
      codesMeta.add("ApplicationTraitement");
      codesMeta.add("CodeCategorieV2");
      codesMeta.add("CodeOrganismeGestionnaire");
      codesMeta.add("CodeOrganismeProprietaire");
      codesMeta.add("CodeProduitV2");
      codesMeta.add("CodeRND");
      codesMeta.add("CodeSousCategorieV2");
      codesMeta.add("CodeTraitementV2");
      codesMeta.add("DateCourrierV2");
      codesMeta.add("DateCreation");
      codesMeta.add("DateDebutConservation");
      codesMeta.add("DateFinConservation");
      codesMeta.add("DateReception");
      codesMeta.add("DateSignature");
      codesMeta.add("Denomination");
      codesMeta.add("FormatFichier");
      codesMeta.add("Hash");
      codesMeta.add("IdTraitementMasse");
      codesMeta.add("JetonDePreuve");
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
      codesMeta.add("ReferenceDocumentaire");
      codesMeta.add("RIBA");
      codesMeta.add("RUM");
      codesMeta.add("Siren");
      codesMeta.add("Siret");
      codesMeta.add("SiteAcquisition");
      codesMeta.add("TailleFichier");
      codesMeta.add("Titre");
      codesMeta.add("TracabilitePreArchivage");
      codesMeta.add("TypeHash");

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

      } else if ("5".equals(etape)) {

         etape5Comptages(formulaire);

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

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape3Recherche(TestStockageMasseAllFormulaire formulaire) {

      // Appel le service de test de la recherche
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getRechFormulaire(), COUNT_WAITED, false, null);

      ResultatTest resultatTest = formulaire.getRechFormulaire().getResultats();

      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         // Récupère l'unique résultat
         ResultatRechercheType resultatRecherche = response
               .getRechercheResponse().getResultats().getResultat()[0];

         // Le vérifie
         verifieResultatRecherche(resultatRecherche, resultatTest);

         // Si le test n'est pas en échec, alors on peut le passer en succès,
         // car tout a pu être vérifié
         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
            resultatTest.setStatus(TestStatusEnum.Succes);
         }

         // Initialise le formulaire de consultation
         formulaire.getConsultFormulaire().setIdArchivage(
               resultatRecherche.getIdArchive().toString());

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
      valeursAttendues.add("CodeProduitV2", "QD75A");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("CodeSousCategorieV2", "11");
      valeursAttendues.add("CodeTraitementV2", "RD75.L02");
      valeursAttendues.add("DateCourrierV2", "2013-03-13");
      valeursAttendues.add("DateCreation", "2011-09-05");
      valeursAttendues.add("DateDebutConservation", "2011-09-02");
      valeursAttendues.add("DateFinConservation", "2016-08-31");
      valeursAttendues.add("DateReception", "2011-09-01");
      valeursAttendues.add("DateSignature", "2012-09-04");
      valeursAttendues.add("Denomination",
            "Test 206-CaptureMasse-OK-Toutes-metadonnees-specifiables");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("IdTraitementMasse", "123654");
      valeursAttendues.add("JetonDePreuve", "1A2B3C4D5E6F7G8H9I");
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("NniEmployeur", "148032541101648");
      valeursAttendues.add("NomFichier", "doc1.PDF");
      valeursAttendues.add("NumeroCompteExterne", "30148032541101600");
      valeursAttendues.add("NumeroCompteInterne", "719900");
      valeursAttendues.add("NumeroIntControle", "57377");
      valeursAttendues.add("NumeroPersonne", "123854");
      valeursAttendues.add("NumeroRecours", "1");
      valeursAttendues.add("NumeroStructure", "000050221");
      valeursAttendues.add("Periode", "PERI");
      valeursAttendues.add("PseudoSiret", "4914736610005");
      valeursAttendues.add("ReferenceDocumentaire",
            "1234567890AZERTyuio1234567890");
      valeursAttendues.add("RIBA", "99/39405R00006204505");
      valeursAttendues.add("RUM", "24534Y8465435413Y012312356690123");
      valeursAttendues.add("Siren", "0123456789");
      valeursAttendues.add("Siret", "12345678912345");
      valeursAttendues.add("SiteAcquisition", "CER44");
      valeursAttendues.add("TailleFichier", "56587");
      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("TracabilitePreArchivage",
            "Tracabilite pre archivage");
      valeursAttendues.add("TypeHash", "SHA-1");

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
      valeursMetaAttendus.add(new MetadonneeValeur("Denomination",
            "Test 206-CaptureMasse-OK-Toutes-metadonnees-specifiables"));
      valeursMetaAttendus.add(new MetadonneeValeur("Hash",
            "a2f93f1f121ebba0faef2c0596f2f126eacae77b"));

      // Appel du service de vérification
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getConsultFormulaire(),
                  "a2f93f1f121ebba0faef2c0596f2f126eacae77b",
                  codeMetaAttendues, valeursMetaAttendus);

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      ResultatTest resultatTest = formulaire.getConsultFormulaire()
            .getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void etape5Comptages(TestStockageMasseAllFormulaire formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(COUNT_WAITED));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
