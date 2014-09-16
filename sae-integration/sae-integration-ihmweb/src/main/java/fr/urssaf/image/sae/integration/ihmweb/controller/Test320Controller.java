package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test320Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;

/**
 * 320-Recherche-OK-TrimGauche
 */
@Controller
@RequestMapping(value = "test320")
public class Test320Controller extends
      AbstractTestWsController<Test320Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "320";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test320Formulaire getFormulairePourGet() {

      Test320Formulaire formulaire = new Test320Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape n°1: Capture unitaire
      // -----------------------------------------------------------------------------
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      formCapture
            .setUrlEcde(this
                  .getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureUnitaire-101-CaptureUnitaire-OK-Standard/documents/doc1.PDF"));

      // Le nom du fichier
      formCapture.setNomFichier("doc1.PDF");

      // Le mode d'utilisation de la capture
      formCapture
            .setModeCapture(ModeArchivageUnitaireEnum.archivageUnitairePJContenuAvecMtom);

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formCapture.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination", "Test 320-Recherche-OK-TrimGauche");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      // un espace volontaire à gauche
      metadonnees.add("NouvelleMetaTrimGauche", " valeur");
      metadonnees.add("Periode", "PERI");
      metadonnees
            .add("Titre",
                  "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape n°2: Recherche #1
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRech1 = formulaire.getRecherche1();

      // Requête de recherche
      formRech1.setRequeteLucene(getCasTest().getLuceneExempleList().get(0));

      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta1 = formRech1.getCodeMetadonnees() ;
      codesMeta1.add("ApplicationProductrice");
      codesMeta1.add("CodeOrganismeGestionnaire");
      codesMeta1.add("CodeOrganismeProprietaire");
      codesMeta1.add("CodeRND");
      codesMeta1.add("DateCreation");
      codesMeta1.add("Denomination");
      codesMeta1.add("FormatFichier");
      codesMeta1.add("Hash");
      codesMeta1.add("NbPages");
      codesMeta1.add("NouvelleMetaTrimGauche");
      codesMeta1.add("Periode");
      codesMeta1.add("Titre");
      codesMeta1.add("TypeHash");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape n°2: Recherche #2
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRech2 = formulaire.getRecherche2();

      // Requête de recherche
      formRech2.setRequeteLucene(getCasTest().getLuceneExempleList().get(1));

      // Les métadonnées que l'on souhaite en retour
      // Aucune pour cette étape => la recherche n'est pas censée renvoyer de
      // résultats

      // -----------------------------------------------------------------------------
      // THE END
      // -----------------------------------------------------------------------------

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test320Formulaire formulaire) {

      String etape = formulaire.getEtape();

      if ("1".equals(etape)) {

         etape1captureUnitaire(formulaire);

      } else if ("2".equals(etape)) {

         etape2recherche1(formulaire);

      } else if ("3".equals(etape)) {

         etape3recherche2(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1captureUnitaire(Test320Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire
            .getCaptureUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      formulaire.getRecherche1().getResultats().clear();

      // Vide le résultat du test précédent de l'étape 3
      formulaire.getRecherche2().getResultats().clear();

      // Lance le test
      this.getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1);

   }

   private void etape2recherche1(Test320Formulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = 1;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formulaire.getRecherche1(),
                  nbResultatsAttendus, flagResultatsTronquesAttendu, null);

      // Vérifications en profondeur
      ResultatTest resultatTest = formulaire.getRecherche1().getResultats();
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Vérifie le résultat
         getRechercheTestService().verifieResultatRecherche(
               response.getRechercheResponse().getResultats().getResultat()[0],
               "1", resultatTest, getValeursAttendues());

      }

      // Si pas en échec, alors test en OK (tout a été vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private MetadonneeValeurList getValeursAttendues() {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeOrganismeGestionnaire", "CER69");
      valeursAttendues.add("CodeOrganismeProprietaire", "UR750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateCreation", "2011-09-05");
      valeursAttendues.add("Denomination", "Test 320-Recherche-OK-TrimGauche");
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("NouvelleMetaTrimGauche", "valeur");
      valeursAttendues.add("Periode", "PERI");
      valeursAttendues
            .add("Titre",
                  "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      valeursAttendues.add("TypeHash", "SHA-1");

      return valeursAttendues;

   }

   private void etape3recherche2(Test320Formulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = 0;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(), formulaire.getRecherche2(),
            nbResultatsAttendus, flagResultatsTronquesAttendu, null);

   }

}
