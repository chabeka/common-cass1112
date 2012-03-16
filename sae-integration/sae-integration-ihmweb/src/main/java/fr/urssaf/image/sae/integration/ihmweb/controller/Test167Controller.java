package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeArchivageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;

/**
 * 167-CaptureUnitaire-KO-NomFichierVide
 */
@Controller
@RequestMapping(value = "test167")
public class Test167Controller extends
      AbstractTestWsController<TestWsCaptureUnitaireFormulaire> {

   

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "167";
   }
   
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsCaptureUnitaireFormulaire getFormulairePourGet() {

      TestWsCaptureUnitaireFormulaire formulaire = new TestWsCaptureUnitaireFormulaire();

      CaptureUnitaireFormulaire formCapture = formulaire.getCaptureUnitaire();
      formCapture.setUrlEcde(getEcdeService().construitUrlEcde("SAE_INTEGRATION/20110822/CaptureUnitaire-167-CaptureUnitaire-KO-NomFichierVide/documents/doc1.PDF"));
      formCapture.setModeCapture(ModeArchivageUnitaireEnum.archivageUnitairePJContenuSansMtom);

      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      metadonnees.add(new MetadonneeValeur("ApplicationProductrice", "ADELAIDE"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeGestionnaire", "CER69"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeProprietaire", "AC750"));
      metadonnees.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));
      metadonnees.add(new MetadonneeValeur("DateCreation", "2011-09-01"));
      metadonnees.add(new MetadonneeValeur("Denomination","Test 167-CaptureUnitaire-KO-NomFichierVide"));
      metadonnees.add(new MetadonneeValeur("FormatFichier", "fmt/354"));
      metadonnees.add(new MetadonneeValeur("Hash","a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metadonnees.add(new MetadonneeValeur("NbPages", "2"));
      metadonnees.add(new MetadonneeValeur("Titre", "Attestation de vigilance"));
      metadonnees.add(new MetadonneeValeur("TypeHash", "SHA-1"));
      formCapture.setMetadonnees(metadonnees);

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsCaptureUnitaireFormulaire formulaire) {

      captureUnitaire(formulaire.getUrlServiceWeb(), formulaire
            .getCaptureUnitaire());

   }

   private void captureUnitaire(String urlServiceWeb,
         CaptureUnitaireFormulaire formulaire) {

      // Appel de la méthode de test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            urlServiceWeb, formulaire, ViUtils.FIC_VI_OK,
            "sae_NomFichierVide", null);

   }

}
