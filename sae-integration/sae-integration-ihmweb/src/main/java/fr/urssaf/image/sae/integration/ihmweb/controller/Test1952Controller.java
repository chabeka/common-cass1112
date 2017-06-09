package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import fr.urssaf.image.sae.integration.ihmweb.service.dfce.DfceService;

/**
 * 1952-Transfert-KO-Suppression-GNT-KO
 */
@Controller
@RequestMapping(value = "test1952")
public class Test1952Controller extends
      AbstractTestWsController<TestWsTransfertFormulaire> {
   
   @Autowired
   DfceService dfceService;
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1952";
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
                        "SAE_INTEGRATION/20110822/Transfert-1952-T-KO-SUPPRESSION-GNT-KO/documents/doc1.PDF"));

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
      metadonnees.add("Denomination", "Test 1952-Transfert-KO-Suppression-GNT-KO");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
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
         //-- On gel le documents
         String base = getTestConfig().getDfceBase();
         dfceService.freezeDocument(formulaire.getTransfert().getIdDocument(), base);
         
         //-- On transfert le document
         transfert(formulaire.getUrlServiceWeb(), formulaire.getTransfert(), formulaire.getViFormulaire());
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_CS_TRANSFERT_RECHERCHE");
         formulaire.getViFormulaire().setPagms(pagmList);
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
      //-- Appel de la méthode de transfert
      String refSoapFault = "sae_ErreurInterneTransfert";
      String[] args = new String[] {formulaire.getIdDocument().toString()};
      getTransfertTestService().appelWsOpTransfertSoapFault(urlWebService, formulaire, viParams, refSoapFault, args);
   }
   
   private void rechercheGNT(String urlServiceWeb, RechercheFormulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = 1;
      
      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

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
   }
   
   private void rechercheGNS(String urlServiceWeb, RechercheFormulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = 0;

      // Appel de la méthode de test
      getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(urlServiceWeb,
                  formulaire, nbResultatsAttendus, false, null);
   }
   
   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("DateDebutConservation", "2011-09-01");
      valeursAttendues.add("DateFinConservation", "2014-08-31");
      valeursAttendues.add("Denomination", "Test 1952-Transfert-KO-Suppression-GNT-KO");
      valeursAttendues.add("DureeConservation", "1095");
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest,
            valeursAttendues);
   }
   

}