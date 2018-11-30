package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWSIsolationDonneesFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 2251-IsolationDonnees-CS-Meta-DomaineCotisant
 */
@Controller
@RequestMapping(value = "test2251")
public class Test2251Controller extends AbstractTestWsController<TestWSIsolationDonneesFormulaire> {
   
   private final String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
   /**
    * Dénomination unique par s'assurer d'avoir toujours un seul résultat de recherche à 
    * chaque lancement du test
    */
   private final String metaDenomination = "Test 2250-IsolationDonnees-CS-Plusieurs-Domaine-" + date;
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2251";
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWSIsolationDonneesFormulaire getFormulairePourGet() {
      
      TestWSIsolationDonneesFormulaire formulaire = new TestWSIsolationDonneesFormulaire();
      
      ViFormulaire viForm = formulaire.getViFormulaire();
      PagmList pagmList = new PagmList();
      pagmList.add("PAGM_TOUTES_ACTIONS");
      
      viForm.setPagms(pagmList);
      viForm.setIssuer("CS_DEV_TOUTES_ACTIONS");
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
      metadonnees.add("DomaineCotisant", "true");
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
      captureUnitaireAppelWs(form);
   }

   private void captureUnitaireAppelWs(TestWSIsolationDonneesFormulaire formulaire) {
      //-- Initialise
      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      
      final String urlWs = formulaire.getUrlServiceWeb();
      final ViFormulaire viForm = formulaire.getViFormulaire();
      final String soapFaultAttendu = "sae_CaptureMetadonneesInterdites";
      
      //-- Lance le test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            urlWs, formCapture, ViStyle.VI_OK, viForm, soapFaultAttendu, new String[]{"DomaineCotisant"});
   }
}
