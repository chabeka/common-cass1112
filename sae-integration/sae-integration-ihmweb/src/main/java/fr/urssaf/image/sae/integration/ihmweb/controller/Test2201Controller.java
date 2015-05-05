package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWSIsolationDonneesFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;


/**
 * 2200-IsolationDonnees-TestLibre
 */
@Controller
@RequestMapping(value = "test2201")
public class Test2201Controller extends AbstractTestWsController<TestWSIsolationDonneesFormulaire> {
   
   private final String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
   /**
    * Dénomination unique par s'assurer d'avoir toujours un seul résultat de recherche à 
    * chaque lancement du test
    */
   private final String metaDenomination = "Test 2201-IsolationDonnees-CS-Sans-Domaine-" + date;
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2201";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWSIsolationDonneesFormulaire getFormulairePourGet() {
      
      TestWSIsolationDonneesFormulaire formulaire = new TestWSIsolationDonneesFormulaire();
      RechercheFormulaire formRecherche = formulaire.getRecherche();
      
      CodeMetadonneeList codesMeta;
      codesMeta = ReferentielMetadonneesService.getMetadonneesExempleRechercheIsolation();
      codesMeta.add("Denomination");
      codesMeta.add("CodeOrganismeGestionnaire");
      codesMeta.add("DateFinConservation");
      codesMeta.add("DateDebutConservation");
      codesMeta.add("DureeConservation");
      
      String reqLucene = "Denomination:\"" + metaDenomination+"\" AND DomaineCotisant:true";
      
      formRecherche.setCodeMetadonnees(codesMeta);
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus); 
      formRecherche.setRequeteLucene(reqLucene);
      
      ViFormulaire viForm = formulaire.getViFormulaire();
      PagmList pagmList = new PagmList();
      pagmList.add("INT_PAGM_ATT_VIGI_ARCH_UNIT");
      
      viForm.setPagms(pagmList);
      viForm.setIssuer("INT_CS_ATT_VIGI");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      
      formulaire.setViFormulaire(viForm);
      
      //-- capture unitaire
      CaptureUnitaireFormulaire captUnitForm = formulaire.getCaptureUnitaire();

      //-- L'URL ECDE
      String url = "SAE_INTEGRATION/20110822/CaptureUnitaire-101-CaptureUnitaire-OK-Standard/documents/doc1.PDF";
      captUnitForm.setUrlEcde(getEcdeService().construitUrlEcde(url));
      captUnitForm.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnitForm.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-23");
      metadonnees.add("DateDebutConservation", "2011-09-01");
      metadonnees.add("Denomination", metaDenomination);
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      return formulaire;
   }
   
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWSIsolationDonneesFormulaire form) {

      String etape = form.getEtape();
      if ("1".equals(etape)) {
         etape1captureUnitaireAppelWs(form);
         PagmList pagmList = new PagmList();
         pagmList.add("INT_PAGM_ATT_AEPL_RECH");
         form.getViFormulaire().setPagms(pagmList);
      } else if ("2".equals(etape)) {
         recherche(form.getUrlServiceWeb(), form.getRecherche());
      }
   }

   private void etape1captureUnitaireAppelWs(TestWSIsolationDonneesFormulaire formulaire) {
      //-- Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();
      //-- Lance le test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireReponseAttendue(
            formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
   }
   
   private void verifieResultatN(int numeroResultatRecherche,
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest) {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      valeursAttendues.add("DateDebutConservation", "2011-09-01");
      valeursAttendues.add("DateFinConservation", "2014-08-31");
      valeursAttendues.add("Denomination", metaDenomination);
      valeursAttendues.add("DureeConservation", "1095");
      valeursAttendues.add("DomaineCotisant", "true");
      valeursAttendues.add("DomaineTechnique", "");
      valeursAttendues.add("DomaineComptable", "");
      valeursAttendues.add("DomaineRH", "");
      
      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            Integer.toString(numeroResultatRecherche), resultatTest, valeursAttendues);
   }

   
   private void recherche(String urlWebService, RechercheFormulaire formulaire) {
      getRechAvecNbResTestService().appelWsOpRechercheAvecNbResTestLibre(urlWebService, formulaire);
      int nbResultatsAttendus = 1;
      
      // Initialise
      ResultatTest resultatTest = formulaire.getResultats();

      // Appel de la méthode de test
      RechercheResponse response;
      response = getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(urlWebService,
                  formulaire, nbResultatsAttendus, false, null);
      
      //-- Vérifications en profondeur
      if ((response != null) && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {
         
         //--- Récupère l'unique résultat
         ResultatRechercheType resultatRecherche;
         resultatRecherche = response.getRechercheResponse().getResultats().getResultat()[0];
         resultatTest.setStatus(TestStatusEnum.Succes);
         
         //-- On véririfie le résultat
         verifieResultatN(1, resultatRecherche, resultatTest);
      }
   }
}
