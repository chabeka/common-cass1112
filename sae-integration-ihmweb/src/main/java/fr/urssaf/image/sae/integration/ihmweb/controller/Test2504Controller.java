package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsTransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertFormulaire;
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
 * 2504-Note-Transfert-Une-Note-OK
 */
@Controller
@RequestMapping(value = "test2504")
public class Test2504Controller extends
      AbstractTestWsController<TestWsTransfertFormulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2504";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsTransfertFormulaire getFormulairePourGet() {

      TestWsTransfertFormulaire formulaire = new TestWsTransfertFormulaire();
      
      //-- capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      //-- L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Note-2504-Note-Transfert-Une-Note-OK/documents/doc1.PDF"));

      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-23");
      metadonnees.add("DateDebutConservation", "2011-09-01");
      metadonnees.add("Denomination", "Test 2504-Note-Transfert-Une-Note-OK");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Note", "Note classique test 2504");      
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      //-- formulaire de recherche
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());
      
      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      codesMeta.add("DateDebutConservation");
      codesMeta.add("DateFinConservation");
      codesMeta.add("Denomination");
      codesMeta.add("DureeConservation");
      codesMeta.add("Note");
      
      formRecherche.setCodeMetadonnees(codesMeta);
      
      //-- formulaire de recherche
      RechercheFormulaire formRechercheGns = formulaire.getRechercheGns();
      formRechercheGns.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRechercheGns.setRequeteLucene(getCasTest().getLuceneExemple());
      formRechercheGns.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_TRANSFERT");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_CS_TRANSFERT_TRANSFERT");

      return formulaire;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsTransfertFormulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(formulaire);
      } 
      else if ("2".equals(etape)) {
         transfert(formulaire.getUrlServiceWeb(), formulaire.getTransfert(), formulaire.getViFormulaire());
         // Paramètres du VI
         ViFormulaire viForm = formulaire.getViFormulaire();
         viForm.setIssuer("CS_DEV_TOUTES_ACTIONS");
         viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
         viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
         PagmList pagmList = new PagmList();
         viForm.setPagms(pagmList);
         pagmList.add("PAGM_TOUTES_ACTIONS");

      } 
      else if ("3".equals(etape)) {
         rechercheGNT(formulaire.getUrlServiceWeb(), formulaire.getRecherche());
         formulaire.setUrlServiceWeb(getTestConfig().getUrlSaeServiceGns());        
      }
      else if ("4".equals(etape)) {
         rechercheGNS(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheGns());        
      }
   }
   
   private void etape1captureUnitaireAppelWs(TestWsTransfertFormulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat res = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      ResultatTest resultatTest = formCaptureEtp1.getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         // initialise l'identifiant du document archive
         TransfertFormulaire formTransfert = formulaire.getTransfert();
         formTransfert.setIdDocument(UUID.fromString(res.getIdArchivage()));
      }
   }

   private void transfert(String urlWebService, TransfertFormulaire formulaire, ViFormulaire viParams) {
      //-- Appel de la méthode de test
      getTransfertTestService().appelWsOpTransfertReponseAttendue(urlWebService, formulaire, viParams);
   }
   
   private void rechercheGNT(String urlServiceWeb, RechercheFormulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = 0;

      // Appel de la méthode de test
      getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus, false, null);
   }
   
   private void rechercheGNS(String urlServiceWeb, RechercheFormulaire formulaire) {

      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Résultats attendus
      int nbResultatsAttendus = 1;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus, false, null);

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
         resultatTest.setStatus(TestStatusEnum.AControler);
      }
   }
   
   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("DateDebutConservation", "2011-09-23");
      valeursAttendues.add("DateFinConservation", "2014-08-31");
      valeursAttendues.add("Denomination", "Test 2504-Note-Transfert-Une-Note-OK");
      valeursAttendues.add("DureeConservation", "1095");
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
   }
}
