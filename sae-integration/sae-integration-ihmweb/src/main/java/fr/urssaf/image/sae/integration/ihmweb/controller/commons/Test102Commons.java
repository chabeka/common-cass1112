package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test102Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * Méthodes communes pour les tests 102a, 102b, 102c, 102d
 */
@Component
public class Test102Commons {

   @Autowired
   private TestsControllerCommons testCommons;

   private String getDenomination(String numeroTest) {
      if ("102a".equals(numeroTest)) {
         return "Test 102-CaptureUnitaire-OK-EnrichissementEcrasement";
      } else if ("102b".equals(numeroTest)) {
         return "Test 102-CaptureUnitaire-OK-EnrichissementEcrasement-PJ-URL";
      } else if ("102c".equals(numeroTest)) {
         return "Test 102-CaptureUnitaire-OK-EnrichissementEcrasement-PJ-sans-MTOM";
      } else if ("102d".equals(numeroTest)) {
         return "Test 102-CaptureUnitaire-OK-EnrichissementEcrasement-PJ-avec-MTOM";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private ModeArchivageUnitaireEnum getModeArchivage(String numeroTest) {
      if ("102a".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitaire;
      } else if ("102b".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJUrlEcde;
      } else if ("102c".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom;
      } else if ("102d".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   private String nomFichier ="doc1.PDF";
   private String getnomFichier(String numeroTest) {
      if ("102a".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("102b".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("102c".equals(numeroTest)) {
         return nomFichier;
      } else if ("102d".equals(numeroTest)) {
         return nomFichier;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   public final Test102Formulaire getFormulairePourGet(String numeroTest) {

      Test102Formulaire formulaire = new Test102Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(testCommons
                  .getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-102-CaptureUnitaire-OK-EnrichissementEcrasement/documents/doc1.PDF"));

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
      metadonnees.add("DateCreation", "2011-09-23");
      metadonnees.add("DateDebutConservation", "2011-09-01");
      metadonnees.add("Denomination", getDenomination(numeroTest));
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
      formRecherche.setRequeteLucene(testCommons.getCasTest(numeroTest)
            .getLuceneExemple());

      // Métadonnées souhaitées
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);
      codesMeta.add("DateDebutConservation");
      codesMeta.add("DateFinConservation");
      codesMeta.add("Denomination");
      codesMeta.add("DureeConservation");

      // Renvoie le formulaire
      return formulaire;

   }

   public final void doPost(Test102Formulaire formulaire, String numeroTest) {

      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2recherche(formulaire, getDenomination(numeroTest));

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test102Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().clear();

      // Lance le test
      testCommons.getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1);

   }

   private void etape2recherche(Test102Formulaire formulaireTest102,
         String denomination) {

      // Initialise
      RechercheFormulaire formulaire = formulaireTest102.getRecherche();
      ResultatTest resultatTest = formulaire.getResultats();
      String urlServiceWeb = formulaireTest102.getUrlServiceWeb();

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
         verifieResultatRecherche(resultatRecherche, resultatTest, denomination);

      }

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         String denomination) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("DateDebutConservation", "2011-09-01");
      valeursAttendues.add("DateFinConservation", "2016-08-30");
      valeursAttendues.add("Denomination", denomination);
      valeursAttendues.add("DureeConservation", "1095");

      testCommons.getRechercheTestService().verifieResultatRecherche(
            resultatRecherche, numeroResultatRecherche, resultatTest,
            valeursAttendues);

   }

}
