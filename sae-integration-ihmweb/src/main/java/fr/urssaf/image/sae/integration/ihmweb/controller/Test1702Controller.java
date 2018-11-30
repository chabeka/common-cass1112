package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1702Formulaire;
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
 * 1702-Ged-Technique-OK-Modification-Valeur-Meta-Avec-Regle
 */
@Controller
@RequestMapping(value = "test1702")
public class Test1702Controller extends
      AbstractTestWsController<Test1702Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1702";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1702Formulaire getFormulairePourGet() {

      
      Test1702Formulaire formulaire = new Test1702Formulaire();
      
      // capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Ged-Technique-1702-Ged-Technique-OK-Modification-Valeur-Meta-Avec-Regle/documents/doc1.PDF"));

      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination",
            "Test 1702-Ged-Technique-OK-Modification-Valeur-Meta-Avec-Regle");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      metadonnees.add("TypeHash", "SHA-1");

      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaire = formulaire.getRechFormulaire();
      
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      codeMetadonneeList.add("CodeRND");
      codeMetadonneeList.add("CodeFonction");
      codeMetadonneeList.add("CodeActivite");
      codeMetadonneeList.add("DateFinConservation");
      codeMetadonneeList.add("DureeConservation");
      
      rechFormulaire.setRequeteLucene(getCasTest().getLuceneExemple());
      rechFormulaire.setCodeMetadonnees(codeMetadonneeList);
      
      // formulaire de modification
      ModificationFormulaire formModification = formulaire.getModification();
      formModification.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      // Les métadonnées
      MetadonneeValeurList metaModif = new MetadonneeValeurList();
      metaModif.add("CodeRND",
            "2.1.1.2.1");
      formModification.setMetadonnees(metaModif);
      
      // Initialise le formulaire de recherche
      
      RechercheFormulaire rechFormulaireApresModif = formulaire.getRechFormulaireApresModif();
      
      CodeMetadonneeList codeMetadonneeListApresModif = new CodeMetadonneeList();
      codeMetadonneeListApresModif.add("CodeRND");
      codeMetadonneeListApresModif.add("CodeFonction");
      codeMetadonneeListApresModif.add("CodeActivite");
      codeMetadonneeListApresModif.add("DateDebutConservation");
      codeMetadonneeListApresModif.add("DateFinConservation");
      codeMetadonneeListApresModif.add("DureeConservation");
      
      rechFormulaireApresModif.setRequeteLucene(getCasTest().getLuceneExemple());
      rechFormulaireApresModif.setCodeMetadonnees(codeMetadonneeListApresModif);
      
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
   protected final void doPost(Test1702Formulaire formulaire) {
      String etape = formulaire.getEtape();
      
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(formulaire);
      } else if ("2".equals(etape)) {
         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechFormulaire(), formulaire.getViFormulaire());
      } else if ("3".equals(etape)) {
         modification(formulaire.getUrlServiceWeb(), formulaire
               .getModification());
      } else if ("4".equals(etape)) {
         rechercheApresModification(formulaire.getUrlServiceWeb(), formulaire
               .getRechFormulaireApresModif(), formulaire.getViFormulaire());
      } 
   }
   
   private void etape1captureUnitaireAppelWs(Test1702Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat resultat = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      // Si le test n'est pas en échec, alors on peut initialiser l'id du document à supprimer
      if (!TestStatusEnum.Echec.equals(formulaire.getCaptureUnitaire().getResultats().getStatus())) {
         formulaire.getModification().setIdDocument(UUID.fromString(resultat.getIdArchivage()));
      }
   }
   
   private void modification(String urlWebService,
         ModificationFormulaire formulaire) {

      // Appel de la méthode de test
      getModificationTestService().appelWsOpModificationReponseAttendue(
            urlWebService, formulaire, null);
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
      
      DateTime now = new DateTime().withTime(0, 0, 0, 0);
      DateTime dateFinConservation = now.plusDays(1095);
      DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("CodeFonction", "2");
      valeursAttendues.add("CodeActivite", "3");
      valeursAttendues.add("DateFinConservation", formatter.print(dateFinConservation));
      valeursAttendues.add("DureeConservation", "1095");
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
   }
   
   
   private void rechercheApresModification(String urlServiceWeb, RechercheFormulaire formulaire,
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
         verifieResultatNApresModif(1, resultatRecherche, resultatTest);

      }

      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }
   }
   
   private void verifieResultatNApresModif(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      
      DateTime now = new DateTime().withTime(0, 0, 0, 0);
      DateTime dateFinConservation = now.plusDays(2555);
      DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

      valeursAttendues.add("CodeRND", "2.1.1.2.1");
      valeursAttendues.add("CodeFonction", "2");
      valeursAttendues.add("CodeActivite", "1");
      valeursAttendues.add("DateDebutConservation", formatter.print(now));
      valeursAttendues.add("DateFinConservation", formatter.print(dateFinConservation));
      valeursAttendues.add("DureeConservation", "2555");
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
   }

}
