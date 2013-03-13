package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test104Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 * Méthodes communes pour les tests 104a, 104b, 104c, 104d
 */
@Component
public class Test104Commons {

   @Autowired
   private TestsControllerCommons testCommons;

   private String getDenomination(String numeroTest) {
      if ("104a".equals(numeroTest)) {
         return "Test 104-CaptureUnitaire-OK-Sans-Code-Activite";
      } else if ("104b".equals(numeroTest)) {
         return "Test 104-CaptureUnitaire-OK-Sans-Code-Activite-PJ-URL";
      } else if ("104c".equals(numeroTest)) {
         return "Test 104-CaptureUnitaire-OK-Sans-Code-Activite-PJ-sans-MTOM";
      } else if ("104d".equals(numeroTest)) {
         return "Test 104-CaptureUnitaire-OK-Sans-Code-Activite-PJ-avec-MTOM";
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   private ModeArchivageUnitaireEnum getModeArchivage(String numeroTest) {
      if ("104a".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitaire;
      } else if ("104b".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJUrlEcde;
      } else if ("104c".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom;
      } else if ("104d".equals(numeroTest)) {
         return ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }
   
   private String nomFichier ="doc1.PDF";
   private String getnomFichier(String numeroTest) {
      if ("104a".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("104b".equals(numeroTest)) {
         return StringUtils.EMPTY;
      } else if ("104c".equals(numeroTest)) {
         return nomFichier;
      } else if ("104d".equals(numeroTest)) {
         return nomFichier;
      } else {
         throw new IntegrationRuntimeException("Le numéro de test "
               + numeroTest + " est inconnu");
      }
   }

   public final Test104Formulaire getFormulairePourGet(String numeroTest) {

      Test104Formulaire formulaire = new Test104Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de capture unitaire
      // -----------------------------------------------------------------------------

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(testCommons.getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-104-CaptureUnitaire-OK-Sans-Code-Activite/documents/doc1.PDF"));
      
      // Le nom du fichier
      formCapture.setNomFichier(getnomFichier(numeroTest));

      // Le mode d'utilisation de la capture
      formCapture.setModeCapture(getModeArchivage(numeroTest));

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "1.A.X.X.X");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination",getDenomination(numeroTest));
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees
            .add("Titre",
                  "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape de recherche
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRecherche = formulaire.getRecherche();

      // Requête LUCENE
      formRecherche.setRequeteLucene(testCommons.getCasTest(numeroTest).getLuceneExemple());

      // Métadonnées souhaitées
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);
      codesMeta.add("CodeActivite");
      codesMeta.add("CodeFonction");
      codesMeta.add("CodeRND");
      codesMeta.add("Titre");

      // Renvoie le formulaire
      return formulaire;

   }

   public final void doPost(Test104Formulaire formulaire) {

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

   private void etape1captureUnitaire(Test104Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().clear();

      // Lance le test
      testCommons.getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
            formulaire.getUrlServiceWeb(), formCaptureEtp1);

   }

   private void etape2recherche(Test104Formulaire formulaireTest104) {

      // Initialise
      RechercheFormulaire formulaire = formulaireTest104.getRecherche();
      ResultatTest resultatTest = formulaire.getResultats();
      String urlServiceWeb = formulaireTest104.getUrlServiceWeb();

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

      valeursAttendues.add("CodeActivite", StringUtils.EMPTY);
      valeursAttendues.add("CodeFonction", "1");
      valeursAttendues.add("CodeRND", "1.A.X.X.X");
      valeursAttendues
            .add("Titre",
                  "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");

      testCommons.getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

}
