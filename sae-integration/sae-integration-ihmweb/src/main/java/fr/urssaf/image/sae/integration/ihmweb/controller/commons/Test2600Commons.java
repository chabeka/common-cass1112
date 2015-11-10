package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2600Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * Méthodes communes pour les tests 2600a, 2600b, 2600c, 2600d
 */
@Component
public class Test2600Commons {

   @Autowired
   private TestsControllerCommons testCommons;
   private String nomFichier ="doc1.PDF";

   private String getDenomination(String numeroTest) {
      if ("2600a".equals(numeroTest)) {
         return "Test 2600-IdGed-ArchivageUnitaire-OK-TestLibre";
      } else if ("2600b".equals(numeroTest)) {
         return "Test 2600-IdGed-ArchivageUnitaire-OK-TestLibre-PJ-URL";
      } else if ("2600c".equals(numeroTest)) {
         return "Test 2600-IdGed-ArchivageUnitaire-OK-TestLibre-PJ-sans-MTOM";
      } else if ("2600d".equals(numeroTest)) {
         return "Test 2600-IdGed-ArchivageUnitaire-OK-TestLibre-PJ-avec-MTOM";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   // Récupération de la valeur de l'IdGed en fonction du numéro du test
   private String getIdGed(String numeroTest) {
      if ("2600a".equals(numeroTest)) {
         return "f8325fc9-b12a-44ed-8072-8344d2478978";
      } else if ("2600b".equals(numeroTest)) {
         return "f8325fc9-b12a-44ed-8072-8344d2478979";
      } else if ("2600c".equals(numeroTest)) {
         return "f8325fc9-b12a-44ed-8072-8344d2478980";
      } else if ("2600d".equals(numeroTest)) {
         return "f8325fc9-b12a-44ed-8072-8344d2478981";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private ModeArchivageUnitaireEnum getModeArchivage(String numeroTest) {
      if ("2600a".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitaire;
      } else if ("2600b".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJUrlEcde;
      } else if ("2600c".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom;
      } else if ("2600d".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   
   private String getnomFichier(String numeroTest) {
      if ("2600a".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("2600b".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("2600c".equals(numeroTest)) {
         return nomFichier;
      } else if ("2600d".equals(numeroTest)) {
         return nomFichier;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   public final Test2600Formulaire getFormulairePourGet(String numeroTest) {

      Test2600Formulaire formulaire = new Test2600Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(testCommons
                  .getEcdeService()
                  .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/IdGed-2600-IdGed-ArchivageUnitaire-OK-TestLibre/documents/doc1.PDF"));

      // Le nom du fichier
      formCapture.setNomFichier(getnomFichier(numeroTest));

      // Le mode d'utilisation de la capture
      formCapture.setModeCapture(getModeArchivage(numeroTest));

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination", getDenomination(numeroTest));
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");      
      metadonnees.add("IdGed", getIdGed(numeroTest));      
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de recherche
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRecherche = formulaire.getRecherche();

      // Requête LUCENE
      formRecherche.setRequeteLucene(testCommons.getCasTest(numeroTest)
            .getLuceneExemple());

      // Métadonnées souhaitées
      // => La liste des métadonnées consultables
      CodeMetadonneeList codesMeta = testCommons.getRefMetas()
            .listeMetadonneesConsultables();
      formRecherche.setCodeMetadonnees(codesMeta);

      // Renvoie le formulaire
      return formulaire;

   }

   public final void doPost(Test2600Formulaire formulaire, String numeroTest) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2recherche(formulaire, getIdGed(numeroTest), getDenomination(numeroTest));

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test2600Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().clear();

      // Vide le dernier id d'archivage et le dernier sha1
      formulaire.setDernierIdArchivage(null);
      formulaire.setDernierSha1(null);

      // Lance le test
      CaptureUnitaireResultat consultResult = testCommons
            .getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1);

      // Si le test est en succès ...
      if (formCaptureEtp1.getResultats().getStatus().equals(
            TestStatusEnum.Succes)) {

         // On mémorise l'identifiant d'archivage et le sha-1
         formulaire.setDernierIdArchivage(consultResult.getIdArchivage());
         formulaire.setDernierSha1(consultResult.getSha1());

      }

   }

   private void etape2recherche(Test2600Formulaire formulaireTest2600,
         String idged, String denomination) {

      // Initialise
      RechercheFormulaire formulaire = formulaireTest2600.getRecherche();
      ResultatTest resultatTest = formulaire.getResultats();
      String urlServiceWeb = formulaireTest2600.getUrlServiceWeb();

      // Résultats attendus
      int nbResultatsAttendus = 1;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = testCommons.getRechercheTestService()
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
         verifieResultatRecherche(resultatRecherche, resultatTest, idged, denomination);

      }

      // Au mieux, le test est "à contrôler" (certaines métadonnées doivent être
      // vérifiées
      // "à la main")
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }

   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         String idged, String denomination) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("Periode", StringUtils.EMPTY);
      valeursAttendues.add("Siren", StringUtils.EMPTY);
      valeursAttendues.add("NniEmployeur", StringUtils.EMPTY);
      valeursAttendues.add("NumeroPersonne", StringUtils.EMPTY);
      valeursAttendues.add("Denomination", denomination);
      valeursAttendues.add("IdGed", idged);      
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
      valeursAttendues.add("VersionRND", testCommons.getTestConfig()
            .getVersionRND());
      valeursAttendues.add("CodeFonction", "2");
      valeursAttendues.add("CodeActivite", "3");
      valeursAttendues.add("DureeConservation", "1095");
      // valeursAttendues.add("DateDebutConservation",); // <= à vérifier
      // "à la main"
      // valeursAttendues.add("DateFinConservation",); // <= à vérifier
      // "à la main"
      valeursAttendues.add("Gel", "false");
      valeursAttendues.add("TracabilitePreArchivage", StringUtils.EMPTY);
      valeursAttendues.add("TracabilitePostArchivage", StringUtils.EMPTY);
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("TypeHash", "SHA-1");
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("NomFichier", "doc1.PDF");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("TailleFichier", "56587");
      valeursAttendues.add("IdTraitementMasse", StringUtils.EMPTY);
      valeursAttendues.add("ContratDeService", SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      valeursAttendues.add("DateSignature", StringUtils.EMPTY);
      valeursAttendues.add("JetonDePreuve", StringUtils.EMPTY);
      valeursAttendues.add("RUM", StringUtils.EMPTY);
      // valeursAttendues.add("DateArchivage",); // <= à vérifier "à la main"

      testCommons.getRechercheTestService().verifieResultatRecherche(
            resultatRecherche, numeroResultatRecherche, resultatTest,
            valeursAttendues);
      
   }

}
